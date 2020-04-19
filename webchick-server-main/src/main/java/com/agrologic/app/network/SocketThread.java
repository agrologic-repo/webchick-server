package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.messaging.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.util.ApplicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SocketThread implements Runnable {
    private final ClientSessions clientSessions;
    private List<MessageManager> messageManagers;
    private Cellink cellink;
    private CellinkDao cellinkDao;
    private ControllerDao controllerDao;
    private FlockDao flockDao;
    private Message sendMessage;
    private ResponseMessage responseMessage;
    private ResponseMessageMap responseMessageMap;
    private RequestMessageQueue requestQueue;
    private ServerUI serverFacade;
    private CommControl commControl;
    private int sotDelay;
    private int eotDelay;
    private int nxtDelay;
    private int maxError;
    private int keepAliveTimeout;
    private int mrpTimeout;
    private RequestIndex reqIndex;
    private long keepAliveTime;
    private long mrpTime;
    private boolean stopThread = false;
    private Logger logger = LoggerFactory.getLogger(SocketThread.class);
    private NetworkState networkState;
    private int runTimeMinMRP;
    private long startRunTimeMRP;
    private int counterMrp;
    private boolean flagMrp;
    boolean withLogger = false;
    private SocketThreadDebugLogger socketThreadDebugLogger;

    public SocketThread(Cellink cellink) {
        this.cellink = cellink;
        this.clientSessions = null;
        this.socketThreadDebugLogger = new SocketThreadDebugLogger();
    }

    public SocketThread(ClientSessions clientSessions, Socket socket, int counterMrp) throws IOException {
        this.cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
        this.flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
        this.commControl = new CommControl(socket);
        this.requestQueue = new RequestMessageQueue();
        this.responseMessageMap = new ResponseMessageMap();
        this.reqIndex = new RequestIndex();

        this.networkState = NetworkState.STATE_ACCEPT_KEEP_ALIVE;

        Configuration configuration = new Configuration();
        this.sotDelay = Integer.parseInt(configuration.getSotDelay());
        this.eotDelay = Integer.parseInt(configuration.getEotDelay());
        this.nxtDelay = Integer.parseInt(configuration.getNextDelay());
        this.maxError = Integer.parseInt(configuration.getMaxErrors());
        this.keepAliveTimeout = configuration.getKeepalive();
        this.mrpTimeout = 2;
        this.mrpTime = System.currentTimeMillis();
        this.counterMrp = counterMrp;
        this.flagMrp = false;

        this.clientSessions = clientSessions;
        this.messageManagers = new ArrayList<MessageManager>();
        this.socketThreadDebugLogger = new SocketThreadDebugLogger();
    }

    public void setServerFacade(ServerUI serverFacade) {
        this.serverFacade = serverFacade;
    }

    /**
     * Main loop with all communication states , when connection to cellink started.
     */
    @Override
    public void run() {

//        int mrpCount = 0;
        int errCount = 0;
        try {
            while (!stopThread) {
                withLogger = getWithLogging();
                try {
                    switch (getThreadState()) {

                        case STATE_ACCEPT_KEEP_ALIVE:
                            acceptCellink();
                            break;

                        case STATE_KEEP_ALIVE_TIMEOUT:
                            waitKeepAlive();
                            waitMrpTimeout();
                            break;

                        case STATE_STARTING:
                            startingCommunication();
                            break;

                        case STATE_SEND:
                            sendRequestHandler();
                            break;

                        case STATE_BUSY:
                            setThreadState(commControl.doread(sotDelay, eotDelay));
                            break;

                        case STATE_READ:
                            errCount = receiveResponseHandler(errCount);
                            break;

                        case STATE_DELAY:
                            waitDelayTime();
                            requestQueue.notifyToCreateRequestToChange();

                            break;

                        case STATE_ERROR:
                            errCount = errorHandler(errCount);
                            break;

                        case STATE_TIMEOUT:
                            errCount = timeoutErrorHandler(errCount);
                            break;

                        case STATE_ABORT:
                            abortThreadHandler();
                            break;

                        case STATE_STOP:
                        default:
                            stopThreadHandler();
                            break;

                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    logger.error("Connection aborted , ArrayIndexOutOfBoundsException .", ex);
                    networkState = NetworkState.STATE_ABORT;
                } catch (IOException ex) {
                    logger.error("Connection to cellink was lost .", ex);
                    networkState = NetworkState.STATE_ABORT;
                    logger.debug("Exception occured : ", ex);
                } catch (SQLException ex) {
                    logger.error("Database action failed.\nUpdate cellink " + cellink.getId() + " was failed.");
                    networkState = NetworkState.STATE_ABORT;
                    logger.debug("Exception occured : ", ex);
                } catch (NullPointerException ex) {
                    logger.debug("Connection to cellink [{}] stoped ", cellink);
                    networkState = NetworkState.STATE_STOP;
                    logger.debug("Exception occured : ", ex);
                } catch (Exception ex) {
                    networkState = NetworkState.STATE_DELAY;
                    logger.debug("Exception occured : ", ex);
                }

            }
        }
        finally {
            if (cellink != null) {
                logger.info("close connection : " + cellink);
                ApplicationUtil.sleepSeconds(5);

                // check if session was removed because of duplication socket was opened before
                SocketThread socketToClose = clientSessions.getSessions().get(cellink.getId());
                if (socketToClose.getCommControl().equals(commControl)) {
                    clientSessions.closeSession(cellink.getId());
                }

                commControl.close();
                try {
                    removeControllersData();
//                    fix_start_date_and_flock();
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }
        }
    }

    private void stopThreadHandler() throws SQLException {
//        fix_start_date_and_flock();
        stopThread = true;
        cellink.setState(CellinkState.STATE_OFFLINE);
        cellinkDao.update(cellink);

        if (withLogger) {
            //logger.info(" STOP state [ " + cellink + " ] ");
            socketThreadDebugLogger.printLog(" STOP state [ " + cellink + " ] ");
        }
    }

    private void abortThreadHandler() {
        setThreadState(NetworkState.STATE_STOP);

        if (withLogger) {
//            logger.info(cellink.getName() + " abort connection");
            socketThreadDebugLogger.printLog(cellink.getName() + " abort connection");

        }
    }

    private int timeoutErrorHandler(int errCount) {

        responseMessage = (ResponseMessage) commControl.read();
        if (responseMessage.getErrorCodes().equals(Message.ErrorCodes.SOT_ERROR)) {
            errCount++;
        }

        if (withLogger) {
//            logger.error(cellink.getName() + " [" + responseMessage + "] error count : " + errCount);
            socketThreadDebugLogger.printLog(cellink.getName() + " [" + responseMessage + "] error count : " + errCount);
        }
        if (errCount < maxError) {
            requestQueue.setReplyForPreviousRequestPending(true);
            responseMessage = (ResponseMessage) commControl.read();
            setThreadState(NetworkState.STATE_DELAY);
        } else {
            errorTimeout(errCount);
            errCount = 0;
            // we need at least one controller in on state
            // otherwise we well set all controllers off
            if (allControllersOff()) {
                setControllersOn();
            }
//            setThreadState(NetworkState.STATE_STOP);
            setThreadState(NetworkState.STATE_DELAY);
            if (withLogger) {
//                logger.error("Connection to cellink {} is delayed . ",  cellink.getName() );
                socketThreadDebugLogger.printLog("Connection to cellink {} is delayed . " +  cellink.getName());
                //logger.error("DELAY " + cellink.getName());
            }
        }
        return errCount;
    }

    private int errorHandler(int errCount) {

        errCount++;
        if (errCount < maxError) {
            requestQueue.setReplyForPreviousRequestPending(true);
            responseMessage = (ResponseMessage) commControl.read();
        } else {
            responseMessage = new ResponseMessage(null);
            responseMessage.setMessageType(MessageType.ERROR);
            responseMessage.setErrorCodes(Message.ErrorCodes.SOT_ERROR);
            responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
            if (withLogger) {
//                logger.debug("Error count : " + errCount);
                socketThreadDebugLogger.printLog("Error count : " + errCount);
            }
            errCount = 0;
            // we need at least one controller in on state
            // otherwise we we set all controllers on
            if (allControllersOff()) {
                setControllersOn();
            }
        }
        if (withLogger) {
            socketThreadDebugLogger.printLog(cellink.getName() + " error count : " + errCount);
//            logger.debug(cellink.getName() + " error count : " + errCount);
            socketThreadDebugLogger.printLog("Exception: " +  responseMessage);
//            logger.error("Exception: ", responseMessage);
        }
        setThreadState(NetworkState.STATE_DELAY);
        return errCount;
    }

    private void waitDelayTime() {
        ApplicationUtil.sleep(nxtDelay);
        setThreadState(NetworkState.STATE_SEND);
    }

    private int receiveResponseHandler(int errCount) {
        responseMessage = (ResponseMessage) commControl.read();

        // if checkbox with logger checked the send receive messages are shown in traffic log
        if (withLogger) {
//            logger.info(cellink.getName() + " sent message [ V" + sendMessage.getIndex() + sendMessage + " ]");
            StringBuilder sb = new StringBuilder();
            sb.append(cellink.getName() + " received message [ " + responseMessage + " ]").append('\n');
            String reqIdx = sendMessage.getIndex();
            String resIdx = responseMessage.getIndex();
            sb.append("Request index : " + reqIdx + "  Response index : " + resIdx).append('\n');
            socketThreadDebugLogger.printLog(sb.toString());
        }
        if (responseMessage.getMessageType() == MessageType.ERROR) {
            MessageType sendMessageType = sendMessage.getMessageType();
            if (sendMessageType == MessageType.REQUEST_TO_WRITE) {
                responseMessage.setMessageType(MessageType.SKIP_RESPONSE_TO_WRITE);
                responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
                setThreadState(NetworkState.STATE_DELAY);
            } else if (sendMessageType == MessageType.REQUEST_EGG_COUNT
                    || sendMessageType == MessageType.REQUEST_CHICK_SCALE
                    || sendMessageType == MessageType.REQUEST_CHANGED) {
                responseMessage.setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
                responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
                setThreadState(NetworkState.STATE_DELAY);
            } else if (sendMessageType == MessageType.REQUEST_HISTORY) {
                errCount = processMessage(errCount, withLogger);
            } else {
                setThreadState(NetworkState.STATE_ERROR);
            }
        } else if (responseMessage.getMessageType() == MessageType.SKIP_UNUSED_RESPONSE && sendMessage.getMessageType() == MessageType.REQUEST_HISTORY){
            responseMessage.setMessageType(MessageType.REQUEST_HISTORY);
            responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
            setThreadState(NetworkState.STATE_DELAY);
        } else {
            errCount = processMessage(errCount, withLogger);
        }
        return errCount;
    }

    private int processMessage(int errCount, boolean withLogger) {
        if (sendMessage.getIndex().equals(responseMessage.getIndex()) || sendMessage.getIndex().equals("100")) {
            errCount = 0;
            if (sendMessage.getMessageType() == MessageType.REQUEST_HISTORY || sendMessage.getMessageType() == MessageType.REQUEST_PER_HOUR_REPORTS) {
                responseMessage.setMessageType(MessageType.RESPONSE_DATA);
            }
            responseMessageMap.put((RequestMessage) sendMessage, responseMessage);

            setThreadState(NetworkState.STATE_DELAY);
        } else {
            if (withLogger) {
                socketThreadDebugLogger.printLog(cellink.getName() + " [response index error]");
//                logger.error(cellink.getName() + " [response index error]");
            }
            commControl.clearInputStreamWithDelayForSilence();
            setThreadState(NetworkState.STATE_ERROR);
        }
        return errCount;
    }

    private void sendRequestHandler() throws SQLException, IOException, Exception {
        if(cellink.getType().toLowerCase().contains("MRP".toLowerCase())){
                if (isRunTimeTimedOut() && cellink.getState() == 3 && flagMrp == true) {
                    logger.info("Disconnecting cellink [" + cellink + "], mrp timeout .");
                    flagMrp = false;
                    networkState = NetworkState.STATE_STOP;
                } else if (isCellinkStopped()) {
                    logger.info("Disconnecting cellink [" + cellink + "], user mrp disconnected .");
                    networkState = NetworkState.STATE_STOP;
                } else {
                    try {
                        sendMessage = requestQueue.getRequest();
                        sendMessage.setIndex(reqIndex.getIndex());
                        commControl.write("V" + reqIndex.getIndex(), sendMessage);

                        if (withLogger) {
                            socketThreadDebugLogger.printLog(cellink.getName() + " sending message [V" + sendMessage.getIndex() + sendMessage + "]");
//                            logger.info(cellink.getName() + " sending message [V" + sendMessage.getIndex() + sendMessage + "]");
                        }
                        setThreadState(NetworkState.STATE_BUSY);
                    } catch (IOException e) {
                        logger.debug("Connection to cellink [{}] lost .", cellink, e);
                        throw new IOException(e);
                    } catch (NullPointerException e) {
                        logger.debug("Connection to cellink [{}] stoped ", cellink);
                        setThreadState(NetworkState.STATE_STOP);
                } catch (Exception e) {
                        logger.debug("Connection to cellink [{}] lost .", cellink, e);
                        throw new Exception(e);
                    }
                }
            reqIndex.nextIndex();
        } else {
            if (isCellinkTimedOut()) {
                logger.info("Disconnecting cellink [" + cellink + "], user activity timeout .");
                networkState = NetworkState.STATE_STOP;
            } else if (isCellinkStopped()) {
                logger.info("Disconnecting cellink [" + cellink + "], user disconnected .");
                networkState = NetworkState.STATE_STOP;
            } else {
                try {
                    sendMessage = requestQueue.getRequest();
                    sendMessage.setIndex(reqIndex.getIndex());
                    commControl.write("V" + reqIndex.getIndex(), sendMessage);

                    if (withLogger) {
                        socketThreadDebugLogger.printLog(cellink.getName() + " sending message [V" + sendMessage.getIndex() + sendMessage + "]");
//                      logger.info(cellink.getName() + " sending message [V" + sendMessage.getIndex() + sendMessage + "]");
                    }
                    setThreadState(NetworkState.STATE_BUSY);
                } catch (IOException e) {
                    logger.debug("Connection to cellink [{}] lost .", cellink, e);
                    throw new IOException(e);
                } catch (NullPointerException ex) { //added 17/09/2017
                    logger.debug("Connection to cellink [{}] stoped ", cellink);
                    networkState = NetworkState.STATE_STOP; // added 17/09/2017
                } catch (Exception e) {
                    logger.debug("Connection to cellink [{}] lost .", cellink, e);
                    throw new Exception(e);
                }
            }
            reqIndex.nextIndex();
        }
    }

    private void startingCommunication() throws SQLException {
        cellink.setState(CellinkState.STATE_RUNNING);
        cellinkDao.update(cellink);
        startRunTimeMRP = System.currentTimeMillis();
        Collection<Controller> tempControllers = null;
        tempControllers = controllerDao.getActiveCellinkControllers(cellink.getId());

        if(tempControllers.size() != 0 && tempControllers.size() < 4){
            runTimeMinMRP = tempControllers.size() * 10;
        } else {
            runTimeMinMRP = tempControllers.size() * 5;
        }

        // Here we transform each controller into
        // the observer the manager of messages
        for (Controller c : tempControllers) {
            MessageManager mm = new MessageManager(c);
            messageManagers.add(mm);
            requestQueue.addObserver(mm);
            responseMessageMap.addObserver(mm);
        }
        // prepare write messages
        requestQueue.notifyToPrepareRequests();
        setThreadState(NetworkState.STATE_SEND);
        // read header changed by cellink version.
        commControl.setCellinkVersion(cellink.getVersion());
    }

    private void waitKeepAlive() throws IOException, SQLException {
        if (isKeepAliveTime(keepAliveTime)) {
            if (!(cellink.getState() == 3)){
                if (commControl.availableData() > 0) {
                    setThreadState(NetworkState.STATE_ACCEPT_KEEP_ALIVE);
                } else {
                    setThreadState(NetworkState.STATE_STOP);
                    cellink.setState(CellinkState.STATE_OFFLINE);
                    cellinkDao.update(cellink);
                    commControl.close();
                    stopThread = true;
                }
            }
        }
        ApplicationUtil.sleepSeconds(1);
    }

    private void waitMrpTimeout() throws IOException, SQLException, ParseException {
        boolean flag = false;

        Collection<Controller> controllers = controllerDao.getActiveCellinkControllers(cellink.getId());

        if (isMrpTime()) {
            if (cellink.getType().toLowerCase().contains("MRP".toLowerCase()) && cellink.getState() == CellinkState.STATE_ONLINE) {
                for (Controller c : controllers) {
                    Flock flock = flockDao.getOpenFlockByController(c.getId());
                    if (flock != null) {
                        if (flock.getStatus().toLowerCase().contains("Open".toLowerCase())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
                            sdf.setLenient(false);
                            Date startDate = sdf.parse(flock.getStartTime());
                            Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
                            Date currentDay = getCurrentDay();
                            Date updatedDate;
                            if (updatedGrowDay != null) {
                                updatedDate = add_days_to_date(startDate, updatedGrowDay);
                                if (currentDay.compareTo(updatedDate) != 0) {
                                    mrpTime = System.currentTimeMillis();
                                    flag = true;
                                }
                            } else {
                                mrpTime = System.currentTimeMillis();
                                flag = true;
                            }
                        }
                    }
                }
                if (flag) {
                    runTimeMinMRP = controllers.size() * 5;
                    networkState = NetworkState.STATE_STARTING;
                }
            }
        }
    }

    private void acceptCellink() {
        if (isAccessAllowed()) {
            setThreadState(NetworkState.STATE_KEEP_ALIVE_TIMEOUT);
        } else {
            stopThread = true;
        }
    }

    /**
     * When socket opened , we need to read and validate reading data .
     *
     * @return true if received message is valid. otherwise false .
     */
    private boolean isAccessAllowed() {
        try {
            byte[] buffer = new byte[KeepAliveMessage.BUFFER_SIZE];
            if (commControl.read(buffer) == -1) { // try to read
                logger.info("No available data in stream.");
                logger.info("The client IP:PORT [ " + commControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }

            KeepAliveMessage keepAlive;
            try {
                keepAlive = KeepAliveMessage.parseIncomingBytes(buffer);
                logger.debug("Received keep alive [{}]", keepAlive);
            } catch (WrongMessageFormatException e) {
                logger.info("KeepAlive message validation error.", e);
                logger.info("The client IP:PORT [ " + commControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }
            cellink = cellinkDao.validate(keepAlive.getUsername(), keepAlive.getPassword());
            if (cellink.getValidate() == true) {

                clientSessions.closeDuplicateSession(this);

                RequestMessage msg = new MessageFactory().createKeepAlive(keepAliveTimeout);

                commControl.write(msg);
                logger.debug("Sent answer: [{}]", msg);

                clientSessions.closeDuplicateSession(this);

                // stop running duplicated thread
                cellink.registrate(commControl.getSocket());
                cellink.setState(CellinkState.STATE_ONLINE);
                cellink.setVersion(keepAlive.getVersion());
                cellinkDao.update(cellink);
                keepAliveTime = System.currentTimeMillis();

                clientSessions.openSession(cellink.getId(), this);

                logger.info("validation cellink ok  [ " + cellink + " ]");
                return true;
            } else {
                logger.info("validation cellink failed  [ " + commControl.getSocket().getRemoteSocketAddress() + " ]");
                commControl.write(new MessageFactory().createErrorMessage());
                logger.error("The client IP:PORT [ " + commControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }
        } catch (SocketException ex) {
            logger.trace("Client abruptly killed the connection without calling close !");
            logger.trace("The client IP:PORT [ " + commControl.getSocket().getRemoteSocketAddress() + " ]");
            return false;
        } catch (IOException ex) {
            logger.trace(ex.getMessage());
            return false;
        } catch (SQLException ex) {
            logger.info(ex.getMessage());
            logger.trace("Exception: ", ex);
            return false;
        }
    }

    public Cellink getCellink() {
        return cellink;
    }

    private boolean isKeepAliveTime(long keepAliveOccurTime) {
        // if running return false
        long timeSinceLastKeepAlive = System.currentTimeMillis() - keepAliveOccurTime;
        int keepAliveInterval = (int) (keepAliveTimeout * TimeUnit.MINUTES.toMillis(1L));
        return timeSinceLastKeepAlive > keepAliveInterval;
    }

      private boolean isMrpTime(){
        long timeSinceLastMrp;
        int mrpInterval;
        timeSinceLastMrp = System.currentTimeMillis() - mrpTime;
        mrpInterval = (int) (mrpTimeout * TimeUnit.MINUTES.toMillis(1L));
        if(timeSinceLastMrp > mrpInterval){
            return true;
        } else {
            return false;
        }
    }

    private boolean isCellinkTimedOut() throws SQLException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp updateTime = cellinkDao.getUpdatedTime(cellink.getId());
        long oneHour = TimeUnit.HOURS.toMillis(1L);
        long diff = currentTime.getTime() - updateTime.getTime();
        return (diff > oneHour);
    }

    private boolean isRunTimeTimedOut() {
        if (startRunTimeMRP + (runTimeMinMRP * 60000) >= System.currentTimeMillis()){
            return false;
        } else {
            return true;
        }
    }

    private boolean isCellinkStopped() throws SQLException {
        return cellinkDao.getState(cellink.getId()) == CellinkState.STATE_STOP;
    }

    private void errorTimeout(int errorCount) {
        responseMessage = new ResponseMessage(null);
        responseMessage.setMessageType(MessageType.TIME_OUT_ERROR);
        responseMessage.setErrorCodes(Message.ErrorCodes.TIME_OUT_ERROR);
        responseMessageMap.put((RequestMessage) sendMessage, responseMessage);

        if (withLogger) {
            socketThreadDebugLogger.printLog("Error count : " + errorCount);
//          logger.debug("Error count : " + errorCount);
        }
    }

    public synchronized final NetworkState getThreadState() {
        return networkState;
    }

    public synchronized void setThreadState(NetworkState networkState) {
        this.networkState = networkState;
    }

    public synchronized void stopRunning() {
        stopThread = true;
    }

    private void removeControllersData() throws SQLException {
        for (MessageManager cmm : messageManagers) {
            controllerDao.resetControllerData(cmm.getController().getId());
        }
    }

    /**
     * Scanning controller list and check if they all in Off state
     *
     * @return true if controllers are off, otherwise true
     */
    private boolean allControllersOff() {
        for (MessageManager cmm : messageManagers) {
            if (cmm.getController().isOn()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets all controllers to On
     */
    private void setControllersOn() {
        logger.info("Switch ON controllers");
        for (MessageManager cmm : messageManagers) {
            cmm.getController().switchOn();
        }
    }

    private boolean getWithLogging() {
        Iterator<Cellink> iter = serverFacade.iterator();
        while (iter.hasNext()) {
            Cellink c = iter.next();
            if (c.equals(cellink)) {
                return c.isWithLogging();
            }
        }
        return false;
    }

    public CommControl getCommControl() {
        return commControl;
    }

    //TODO: remove it, it's written for tests
    public boolean isStopThread() {
        return stopThread;
    }

    private Date add_days_to_date(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        Date datte;

        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        datte = cal.getTime();

        return datte;
    }

    private Date getCurrentDay(){
        Date date;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();

        return date;
    }
}
