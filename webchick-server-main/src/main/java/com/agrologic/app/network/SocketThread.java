/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.messaging.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.model.Controller;
import com.agrologic.app.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Title: ServerNetThread <br> Description: <br> Copyright: Copyright (c) 2009 <br>
 *
 * @version 1.0 <br>
 */
public class SocketThread implements Runnable, Network {
    public static final int ONE_MINUTE = 60000;
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
    private ComControl comControl;
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

    public SocketThread(ClientSessions clientSessions, Socket socket, Configuration configuration) throws IOException {
        this.cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.controllerDao =DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
        this.comControl = new ComControl(socket);
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
                            setThreadState(comControl.doread(sotDelay, eotDelay));
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
        errCount++;
        if (errCount < maxError) {
            requestQueue.setReplyForPreviousRequestPending(true);
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
            requestQueue.setReplyForPreviousRequestPending(true);
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
            logger.error("Exception: ", responseMessage);
        }
        setThreadState(NetworkState.STATE_DELAY);
        return errCount;
    }

    private void waitDelayTime() {
        Util.sleep(nxtDelay);
        setThreadState(NetworkState.STATE_SEND);
    }

    private int receiveResponseHandler(int errCount) {
        responseMessage = (ResponseMessage) comControl.read();
        boolean withLogger = getWithLogging();
        if (withLogger) {
            logger.info(cellink.getName() + " sent message [ " + "V" + sendMessage.getIndex()
                    + sendMessage + " ]");
            logger.info(cellink.getName() + " received message [ " + responseMessage + " ]");
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

    private void sendRequestHandler() throws SQLException, IOException {
        if (isCellinkTimedOut()) {
            logger.info("Disconnecting cellink [" + cellink + "], user activity timeout .");
            networkState = NetworkState.STATE_STOP;
        } else if (isCellinkStopped()) {
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
        requestQueue.notifyToPrepareRequests();
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

            KeepAliveMessage keepAlive;
            try {
                keepAlive = KeepAliveMessage.parseIncomingBytes(buffer);
                logger.debug("Received keep alive [{}]", keepAlive);
            } catch (WrongMessageFormatException e) {
                logger.info("KeepAlive message validation error.");
                logger.info("The client IP:PORT [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
                return false;
            }
            cellink = cellinkDao.validate(keepAlive.getUsername(), keepAlive.getPassword());
            if (cellink.getValidate() == true) {
                RequestMessage msg = new MessageFactory().createKeepAlive(keepAliveTimeout);
                comControl.write(msg);
                logger.debug("Sent answer: [{}]", msg);

                clientSessions.closeDuplicateSession(this);
                // stop running duplicated thread
                cellink.registrate(comControl.getSocket());
                cellink.setState(CellinkState.STATE_ONLINE);
                cellink.setVersion(keepAlive.getVersion());
                cellinkDao.update(cellink);
                keepAliveTime = System.currentTimeMillis();
                clientSessions.openSession(cellink.getId(), this);
                logger.info("validation cellink ok  [ " + cellink + " ]");
                return true;
            } else {
                logger.info("validation cellink failed  [ " + comControl.getSocket().getRemoteSocketAddress() + " ]");
                comControl.write(new MessageFactory().createErrorMessage());
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
            logger.trace("Exception: ", ex);
            return false;
        }
    }

    public Cellink getCellink() {
        return cellink;
    }

    private boolean isKeepAliveTime(long keepAliveOccuredTime) {
        long timeSinceLastKeepalive = System.currentTimeMillis() - keepAliveOccuredTime;
        int keepAliveInterval = keepAliveTimeout * ONE_MINUTE;
        return timeSinceLastKeepalive > keepAliveInterval;
    }

    private boolean isCellinkTimedOut() throws SQLException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp updateTime = cellinkDao.getUpdatedTime(cellink.getId());
        return (currentTime.getTime() - updateTime.getTime()) > Cellink.ONLINE_TIMEOUT;
    }

    private boolean isCellinkStopped() throws SQLException {
        return cellinkDao.getState(cellink.getId()) == CellinkState.STATE_STOP;
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
