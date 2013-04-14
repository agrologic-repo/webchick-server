/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.mysql.impl.CellinkDaoImpl;
import com.agrologic.app.dao.mysql.impl.ControllerDaoImpl;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.messaging.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.model.Controller;
import com.agrologic.app.util.ByteUtil;
import com.agrologic.app.util.Util;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Title: ServerNetThread <br> Description: <br> Copyright: Copyright (c) 2009 <br>
 *
 * @version 1.0 <br>
 */
public class SocketThread extends Thread implements Network {
    public static final int BUFFER_SIZE = 256;
    public static final int SLEEP_TIME_BEFORE_CLOSE_SOCKET = 5000;
    private List<MessageManager> contMsgManager;
    private Cellink cellink;
    private CellinkDao cellinkDao;
    private ControllerDao controllerDao;
    private Message sendMessage;
    private ResponseMessage responseMessage;
    private ResponseMessageMap responseMessageMap;
    private RequestMessageQueue requestQueue;
    private ServerUI serverFacade;
    private final ClientSessions clientSessions;
    /**
     * communication
     */
    private ComControl comControl;
    private int sotDelay;
    private int eotDelay;
    private int nxtDelay;
    private int maxError;
    private int keepAliveTimeout;
    private RequestIndex reqIndex;
    private long keepAliveTime;
    private boolean stopThread = false;
    private Logger logger = Logger.getLogger(SocketThread.class);
    private NetworkState networkState = NetworkState.STATE_ACCEPT_KEEP_ALIVE;
    private static final int ONE_MINUTE = 60000;


    public SocketThread(Cellink cellink){
        this.cellink = cellink;
        this.clientSessions = null;
    }

    public SocketThread(ClientSessions clientSessions, Socket socket, Configuration configuration) throws IOException {
        this.comControl = new ComControl(socket);
        this.cellinkDao = new CellinkDaoImpl();
        this.controllerDao = new ControllerDaoImpl();
        this.requestQueue = new RequestMessageQueue();
        this.responseMessageMap = new ResponseMessageMap();
        this.reqIndex = new RequestIndex();
        this.sotDelay = Integer.parseInt(configuration.getSotDelay());
        this.eotDelay = Integer.parseInt(configuration.getEotDelay());
        this.nxtDelay = Integer.parseInt(configuration.getNextDelay());
        this.maxError = Integer.parseInt(configuration.getMaxErrors());
        this.keepAliveTimeout = configuration.getKeepalive();
        this.clientSessions = clientSessions;
        this.contMsgManager = new ArrayList<MessageManager>();
    }

    public void setServreFacade(ServerUI serverFacade) {
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
                            setThreadState(comControl.doread(sotDelay, eotDelay));
                            break;

                        case STATE_READ:
                            errCount = recvResponseHandler(errCount);
                            break;

                        case STATE_DELAY:
                            waitDelayTime();
                            requestQueue.getRequestToChange();

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
                }
            }
        } finally {
            if (cellink != null) {
                logger.info("close connection : " + cellink);
                Util.sleep(SLEEP_TIME_BEFORE_CLOSE_SOCKET);
                comControl.close();
                try {
                    removeControllersData();
                } catch (SQLException ex) {
                    logger.error(ex);
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
        errCount++;
        if (errCount < maxError) {
            requestQueue.setReplyRequest(true);
            responseMessage = (ResponseMessage) comControl.read();

        } else {
            errorTimeout(errCount);
            errCount = 0;
            // we need at least one controller in on state
            // otherwise we well set all controllres on
            if (allControllersOff()) {
                setControllersOn();
            }
        }
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.debug(cellink.getName() + " error count : " + errCount);
            logger.error(cellink.getName() + " [" + responseMessage + "]");
        }
        setThreadState(NetworkState.STATE_DELAY);
        return errCount;
    }

    private int errorHandler(int errCount) {
        boolean withLogger = getWithLogging();
        errCount++;
        if (errCount < maxError) {
            requestQueue.setReplyRequest(true);
            responseMessage = (ResponseMessage) comControl.read();
        } else {
            responseMessage = new ResponseMessage(null);
            responseMessage.setMessageType(MessageType.ERROR);
            responseMessage.setErrorCode(Message.SOT_ERROR);
            responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
            if (withLogger) {
                logger.debug("Error count : " + errCount);
            }
            errCount = 0;
            // we need at least one controller in on state
            // otherwise we we set all controllres on
            if (allControllersOff()) {
                setControllersOn();
            }
        }
        if (withLogger) {
            logger.debug(cellink.getName() + " error count : " + errCount);
            logger.error(responseMessage);
        }
        setThreadState(NetworkState.STATE_DELAY);
        return errCount;
    }

    private void waitDelayTime() {
        Util.sleep(nxtDelay);
        setThreadState(NetworkState.STATE_SEND);
    }

    private int recvResponseHandler(int errCount) {
        responseMessage = (ResponseMessage) comControl.read();
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.info(cellink.getName() + " sent message [ " + "V" + sendMessage.getIndex()
                    + sendMessage + " ]");
            logger.info(cellink.getName() + " recieved message [ " + responseMessage + " ]");
            String reqIdx = sendMessage.getIndex();
            String resIdx = responseMessage.getIndex();
            logger.info("Request index : " + reqIdx + "  Response index : " + resIdx);
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
            } else {
                setThreadState(NetworkState.STATE_ERROR);
            }
        } else {
            if (sendMessage.getIndex().equals(responseMessage.getIndex())
                    || sendMessage.getIndex().equals("100")) {
                errCount = 0;

                if (sendMessage.getMessageType() == MessageType.REQUEST_HISTORY
                        || sendMessage.getMessageType() == MessageType.REQUEST_HISTORY_24_HOUR) {
                    responseMessage.setMessageType(MessageType.RESPONSE_DATA);
                }
                responseMessageMap.put((RequestMessage) sendMessage, responseMessage);
                setThreadState(NetworkState.STATE_DELAY);
            } else {
                if (withLogger) {
                    logger.error(cellink.getName() + " [response index error]");
                }
                setThreadState(NetworkState.STATE_ERROR);
            }
        }
        return errCount;
    }

    /**
     *
     * @throws SQLException
     * @throws IOException
     */
    private void sendRequestHandler() throws SQLException, IOException {
        if (isCellinkTimedOut()) {
            logger.info("Disconnecting cellink [" + cellink + "], user activity timeout .");
            networkState = NetworkState.STATE_STOP;
        } else if (isCellinkStoped()) {
            logger.info("Disconnecting cellink [" + cellink + "], user disconnected .");
            networkState = NetworkState.STATE_STOP;
        } else {
            sendMessage = requestQueue.getRequest();
            sendMessage.setIndex(reqIndex.getIndex());
            comControl.write("V" + reqIndex.getIndex(), sendMessage);
            boolean withLogger = getWithLogging();
            if (withLogger) {
                logger.info(cellink.getName() + " sending message [" + "V" + sendMessage.getIndex()
                        + sendMessage + "]");
            }
            setThreadState(NetworkState.STATE_BUSY);
        }
        reqIndex.nextIndex();
    }

    private void startingCommunication() throws SQLException {
        cellink.setState(CellinkState.STATE_RUNNING);
        cellinkDao.update(cellink);

        List<Controller> tempControllers = (List<Controller>) controllerDao.getActiveCellinkControllers(cellink.getId());
        // Here we transform each controller into
        // the observer the manager of messages
        for (Controller c : tempControllers) {
            MessageManager mm = new MessageManager(c);
            contMsgManager.add(mm);
            requestQueue.addObserver(mm);
            responseMessageMap.addObserver(mm);
        }
        // prepare write messages
        requestQueue.prepareRequests();
        setThreadState(NetworkState.STATE_SEND);
        // read header changed by cellink version.
        comControl.setCellinkVersion(cellink.getVersion());
    }

    private void waitKeepAlive() throws IOException, SQLException {
        int state = cellinkDao.getState(cellink.getId());
        if (state == CellinkState.STATE_START
                || state == CellinkState.STATE_RESTART) {
            setThreadState(NetworkState.STATE_STARTING);
        } else {
            if (isKeepAliveTime(keepAliveTime)) {
                if (comControl.availableData() > 0) {
                    setThreadState(NetworkState.STATE_ACCEPT_KEEP_ALIVE);
                } else {
                    setThreadState(NetworkState.STATE_STOP);
                    cellink.setState(CellinkState.STATE_OFFLINE);
                    cellinkDao.update(cellink);
                    comControl.close();
                    stopThread = true;
                }
            } else {
                //checking the state of terminal
                state = cellinkDao.getState(cellink.getId());
                if (state == CellinkState.STATE_START
                        || state == CellinkState.STATE_RESTART) {
                    setThreadState(NetworkState.STATE_STARTING);
                }
            }
        }
        Util.sleep(100);
    }

    private void acceptCellink() {
        if (isAccessAllowed()) {
            setThreadState(NetworkState.STATE_KEEP_ALIVE_TIMEOUT);
        } else {
            //comControl.close();
            stopThread = true;
        }
    }

    /**
     * When socket opened , we need to read and validate reading data.
     *
     * @return true if received message is valid. otherwise false
     */
    private boolean isAccessAllowed() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            if (comControl.read(buffer) == -1) { // try to read
                logger.info("No available data in stream.");
                logger.info("The client IP:PORT [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }

            int stx = ByteUtil.indexOf(buffer, Message.STX);
            int etx = ByteUtil.indexOf(buffer, Message.ETX);
            if (stx < 0 || etx < 0) {
                logger.info("KeepAlive message validation error.");
                logger.info("The client IP:PORT [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }
            byte[] data = Arrays.copyOfRange(buffer, stx + 1, etx);
            List<byte[]> dataList = ByteUtil.split(data, Message.RS);

            int PASS_INDEX = 0;
            int NAME_INDEX = 1;
            int VERS_INDEX = 2;
            data = dataList.get(PASS_INDEX);
            String psswd = new String(data, 0, data.length);
            data = dataList.get(NAME_INDEX);
            String name = new String(data, 0, data.length);
            String vers = "N/A";
            if (dataList.size() > VERS_INDEX) {
                data = dataList.get(VERS_INDEX);
                vers = new String(data, 0, data.length);
            }

            cellink = cellinkDao.validate(name, psswd);
            if (cellink.getValidate() == true) {
                comControl.write(new RequestMessage(MessageType.KEEP_ALIVE, keepAliveTimeout));
                clientSessions.closeDuplicateSession(this);
                // stop running duplicated thread
                cellink.registrate(comControl.getSocket());
                cellink.setState(CellinkState.STATE_ONLINE);
                cellink.setVersion(vers);
                cellinkDao.update(cellink);
                keepAliveTime = System.currentTimeMillis();
                clientSessions.openSession(cellink.getId(), this);
                logger.info("validation cellink ok  [ " + cellink + " ]");
                return true;
            } else {
                logger.info("validation cellink failed  [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
                comControl.write(new RequestMessage(MessageType.ERROR));
                logger.error("The client IP:PORT [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }

        } catch (SocketException ex) {
            logger.trace("Client abruptly killed the connection without calling close !");
            logger.trace("The client IP:PORT [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
            return false;
        } catch (IOException ex) {
            logger.trace(ex.getMessage());
            return false;
        } catch (SQLException ex) {
            logger.info(ex.getMessage());
            logger.trace(ex);
            return false;
        }
    }

    public Cellink getCellink() {
        return cellink;
    }

    private boolean isKeepAliveTime(long keepAliveOccuredTime) {
        long timeSinceLastKeepalive = System.currentTimeMillis() - keepAliveOccuredTime;
        int keepAliveInterval = keepAliveTimeout * ONE_MINUTE;
        if (timeSinceLastKeepalive > keepAliveInterval) {
            return true;
        }
        return false;
    }

    private boolean isCellinkTimedOut() throws SQLException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp updateTime = cellinkDao.getUpdatedTime(cellink.getId());
        if ((currentTime.getTime() - updateTime.getTime()) > Cellink.ONLINE_TIMEOUT) {
            return true;
        }
        return false;
    }

    private boolean isCellinkStoped() throws SQLException {
        if (cellinkDao.getState(cellink.getId()) == CellinkState.STATE_STOP) {
            return true;
        }
        return false;
    }

    private void errorTimeout(int errorCount) {
        responseMessage = new ResponseMessage(null);
        responseMessage.setMessageType(MessageType.TIME_OUT_ERROR);
        responseMessage.setErrorCode(Message.TMO_ERROR);
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
        for (MessageManager cmm : contMsgManager) {
            controllerDao.resetControllerData(cmm.getController().getId());
        }
    }

    /**
     * Scanning controller list and check if they all in Off state
     *
     * @return true if controllers are off, otherwise true
     */
    private boolean allControllersOff() {
        for (MessageManager cmm : contMsgManager) {
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
        for (MessageManager cmm : contMsgManager) {
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

    public ComControl getComControl() {
        return comControl;
    }

    public void setNetworkState(NetworkState networkState) {
        this.networkState = networkState;
    }

    public NetworkState getNetworkState() {
        return networkState;
    }
    //TODO: remove it, it's written for tests
    public boolean isStopThread() {
        return stopThread;
    }
}
