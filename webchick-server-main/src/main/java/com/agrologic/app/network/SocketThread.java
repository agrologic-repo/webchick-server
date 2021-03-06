package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.messaging.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.model.Controller;
import com.agrologic.app.util.ApplicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SocketThread implements Runnable {
    private final ClientSessions clientSessions;
    private List<MessageManager> messageManagers;
    private Cellink cellink;
    private CellinkDao cellinkDao;
    private ControllerDao controllerDao;
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
    private RequestIndex reqIndex;
    private long keepAliveTime;
    private boolean stopThread = false;
    private Logger logger = LoggerFactory.getLogger(SocketThread.class);
    private NetworkState networkState = NetworkState.STATE_ACCEPT_KEEP_ALIVE;

    public SocketThread(Cellink cellink) {
        this.cellink = cellink;
        this.clientSessions = null;
    }

    public SocketThread(ClientSessions clientSessions, Socket socket) throws IOException {
        this.cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
        this.commControl = new CommControl(socket);
        this.requestQueue = new RequestMessageQueue();
        this.responseMessageMap = new ResponseMessageMap();
        this.reqIndex = new RequestIndex();

        Configuration configuration = new Configuration();
        this.sotDelay = Integer.parseInt(configuration.getSotDelay());
        this.eotDelay = Integer.parseInt(configuration.getEotDelay());
        this.nxtDelay = Integer.parseInt(configuration.getNextDelay());
        this.maxError = Integer.parseInt(configuration.getMaxErrors());
        this.keepAliveTimeout = configuration.getKeepalive();

        this.clientSessions = clientSessions;
        this.messageManagers = new ArrayList<MessageManager>();
    }

    public void setServerFacade(ServerUI serverFacade) {
        this.serverFacade = serverFacade;
    }

    /**
     * Main loop with all communication states , when connection to cellink started.
     */
    @Override
    public void run() {

        int errCount = 0;
        try {
            while (!stopThread) {

                try {
                    switch (getThreadState()) {

                        case STATE_ACCEPT_KEEP_ALIVE:
                            acceptCellink();
                            break;

                        case STATE_KEEP_ALIVE_TIMEOUT:
                            waitKeepAlive();
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
                } catch (SQLException ex) {
                    logger.error("Database action failed.\nUpdate cellink " + cellink.getId() + " was failed.");
                    networkState = NetworkState.STATE_ABORT;
                } catch (Exception ex) {
                    networkState = NetworkState.STATE_DELAY;
                }
            }
        } finally {
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
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }
        }
    }

    private void stopThreadHandler() throws SQLException {
        stopThread = true;
        cellink.setState(CellinkState.STATE_OFFLINE);
        cellinkDao.update(cellink);
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.info(" STOP state [ " + cellink + " ] ");
        }
    }

    private void abortThreadHandler() {
        setThreadState(NetworkState.STATE_STOP);
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.info(cellink.getName() + " abort connection");
        }
    }

    private int timeoutErrorHandler(int errCount) {

        responseMessage = (ResponseMessage) commControl.read();
        if (responseMessage.getErrorCodes().equals(Message.ErrorCodes.SOT_ERROR)) {
            errCount++;
        }
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.error(cellink.getName() + " [" + responseMessage + "] error count : " + errCount);
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
            setThreadState(NetworkState.STATE_STOP);
            if (withLogger) {
                logger.error("Connection to cellink " + cellink.getName() + " is lost . ");
            }
        }
        return errCount;
    }

    private int errorHandler(int errCount) {
        boolean withLogger = getWithLogging();
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
                logger.debug("Error count : " + errCount);
            }
            errCount = 0;
            // we need at least one controller in on state
            // otherwise we we set all controllers on
            if (allControllersOff()) {
                setControllersOn();
            }
        }
        if (withLogger) {
            logger.debug(cellink.getName() + " error count : " + errCount);
            logger.error("Exception: ", responseMessage);
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
        boolean withLogger = getWithLogging();
        // if checkbox with logger checked the send receive messages are shown in traffic log
        if (withLogger) {
            logger.info(cellink.getName() + " sent message [ V" + sendMessage.getIndex() + sendMessage + " ]");
            logger.info(cellink.getName() + " received message [ " + responseMessage + " ]");
            String reqIdx = sendMessage.getIndex();
            String resIdx = responseMessage.getIndex();
            logger.info("Request index : " + reqIdx + "  Response index : " + resIdx);
        }
        //
        if (responseMessage.getMessageType() == MessageType.ERROR) {
            MessageType sendMessageType = sendMessage.getMessageType();

            if (sendMessageType == MessageType.REQUEST_TO_WRITE) {
                responseMessage.setMessageType(MessageType.SKIP_RESPONSE_TO_WRITE);
                responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
                setThreadState(NetworkState.STATE_DELAY);

            } else if (sendMessageType == MessageType.REQUEST_EGG_COUNT
                    || sendMessageType == MessageType.REQUEST_CHICK_SCALE
                    || sendMessageType == MessageType.REQUEST_CHANGED
                    || sendMessageType == MessageType.REQUEST_GRAPHS) {

                responseMessage.setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
                responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
                setThreadState(NetworkState.STATE_DELAY);

            } else {
                setThreadState(NetworkState.STATE_ERROR);

            }
        } else {
            if (sendMessage.getIndex().equals(responseMessage.getIndex()) || sendMessage.getIndex().equals("100")) {
                if (sendMessage.getMessageType() == MessageType.REQUEST_HISTORY || sendMessage.getMessageType() == MessageType.REQUEST_PER_HOUR_REPORTS) {
                    responseMessage.setMessageType(MessageType.RESPONSE_DATA);
                }
                responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
                errCount = 0;
                setThreadState(NetworkState.STATE_DELAY);
            } else {
                if (withLogger) {
                    logger.error(cellink.getName() + " [response index error]");
                }

                commControl.clearInputStreamWithDelayForSilence();
                setThreadState(NetworkState.STATE_ERROR);
            }
        }
        return errCount;
    }

    private void sendRequestHandler() throws SQLException, IOException, Exception {
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
                boolean withLogger = getWithLogging();
                if (withLogger) {
                    logger.info(cellink.getName() + " sending message [V" + sendMessage.getIndex() + sendMessage + "]");
                }
                setThreadState(NetworkState.STATE_BUSY);
            } catch (IOException e) {
                logger.debug("Connection to cellink [{}] lost .", cellink, e);
                throw new IOException(e);
            } catch (Exception e) {
                logger.debug("Connection to cellink [{}] lost .", cellink, e);
                throw new Exception(e);
            }
        }
        reqIndex.nextIndex();
    }

    private void startingCommunication() throws SQLException {
        cellink.setState(CellinkState.STATE_RUNNING);
        cellinkDao.update(cellink);

        Collection<Controller> tempControllers = controllerDao.getActiveCellinkControllers(cellink.getId());
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
        ApplicationUtil.sleepSeconds(1);
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
        long timeSinceLastKeepAlive = System.currentTimeMillis() - keepAliveOccurTime;
        int keepAliveInterval = (int) (keepAliveTimeout * TimeUnit.MINUTES.toMillis(1L));
        return timeSinceLastKeepAlive > keepAliveInterval;
    }

    private boolean isCellinkTimedOut() throws SQLException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp updateTime = cellinkDao.getUpdatedTime(cellink.getId());
        long oneHour = TimeUnit.HOURS.toMillis(1L);
        long diff = currentTime.getTime() - updateTime.getTime();
        return (diff > oneHour);
    }

    private boolean isCellinkStopped() throws SQLException {
        return cellinkDao.getState(cellink.getId()) == CellinkState.STATE_STOP;
    }

    private void errorTimeout(int errorCount) {
        responseMessage = new ResponseMessage(null);
        responseMessage.setMessageType(MessageType.TIME_OUT_ERROR);
        responseMessage.setErrorCodes(Message.ErrorCodes.TIME_OUT_ERROR);
        responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.debug("Error count : " + errorCount);
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
}
