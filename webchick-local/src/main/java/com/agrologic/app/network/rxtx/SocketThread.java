package com.agrologic.app.network.rxtx;


import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseLoadAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.except.ObjectDoesNotExist;
import com.agrologic.app.except.SerialPortControlFailure;
import com.agrologic.app.gui.rxtx.StatusPanel;
import com.agrologic.app.gui.rxtx.WCSLWindow;
import com.agrologic.app.messaging.*;
import com.agrologic.app.model.Controller;
import com.agrologic.app.util.LocalUtil;
import com.agrologic.app.util.StatusChar;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public final class SocketThread extends Observable implements Runnable, Network {

    private final int eotDelay;
    private final int nxtDelay;
    private final int sotDelay;
    private final int maxError;
    private SerialPortControl com;
    private List<MessageManager> controllerManagers;
    private DatabaseManager dbManager;
    private NetworkState networkState;
    private ResponseMessage responseMessage;
    private RequestIndex reqIndex;
    private RequestMessageQueue requestQueue;
    private ResponseMessageMap responseMessageMap;
    private Message sendMessage;
    private WCSLWindow wcsl;
    private StatusPanel statusPanel;
    private boolean DEBUG = false;
    private Logger logger = Logger.getLogger(SocketThread.class);

    public SocketThread(WCSLWindow wcsl, DatabaseManager dbManager)
            throws NumberFormatException, ObjectDoesNotExist, SerialPortControlFailure {
        super();
        this.wcsl = wcsl;
        this.networkState = NetworkState.STATE_STARTING;
        Configuration configuration = new Configuration();

        this.dbManager = dbManager;
        this.requestQueue = new RequestMessageQueue();
        this.responseMessageMap = new ResponseMessageMap();
        this.controllerManagers = new ArrayList<MessageManager>();
        this.reqIndex = new RequestIndex();
        // sets network attributes
        this.sotDelay = Integer.parseInt(configuration.getSotDelay());
        this.eotDelay = Integer.parseInt(configuration.getEotDelay());
        this.nxtDelay = Integer.parseInt(configuration.getNextDelay());
        this.maxError = Integer.parseInt(configuration.getMaxErrors());

        // trying to open comport
        if (com == null) {
            final String comport = configuration.getComPort();
            try {
                com = new SerialPortControl(comport, this);
                logger.info("Communication port opened successfully!");
            } catch (SerialPortControlFailure e) {
                logger.error(e);
                throw new SerialPortControlFailure(e.getMessage(), e);
            }
        }

        try {
            // parsingReceiveBuffer controller message managers
            logger.info("Initialization messages");
            initControllerMessageManagers(configuration);
        } catch (ObjectDoesNotExist e) {
            closingComport();
            logger.error(e.getMessage());
            throw new ObjectDoesNotExist(e.getMessage());
        }
    }

    private void initControllerMessageManagers(Configuration settingPreferences) throws ObjectDoesNotExist {
        DatabaseLoadAccessor dla = dbManager.getDatabaseLoader();
        List<Controller> tempControllers =
                dla.getUser().getCellinkById(Long.parseLong(settingPreferences.getCellinkId())).getControllers();

        // Here we transform each controller into
        // the observer the manager of messages
        DatabaseAccessor dbaccessor = dbManager.getDatabaseGeneralService();
        for (Controller c : tempControllers) {
            MessageManager cmm = new MessageManager(c, dbaccessor);
            controllerManagers.add(cmm);
        }
    }

    @Override
    public void run() {
        int errCount = 0;
        boolean stop = false;
        logger.info("Run socket thread ");
        while (!stop) {
            try {
                switch (networkState) {
                    case STATE_IDLE:
                        LocalUtil.sleep(10);
                        break;

                    case STATE_STOP:
                        stop = true;
                        closingComport();

                        break;

                    case STATE_STARTING:
                        startingCommunication();

                        break;

                    case STATE_SEND:
                        sendRequestHandler();

                        break;

                    case STATE_BUSY:
                        LocalUtil.sleep(100);
                        statusPanel.setProgress("" + StatusChar.getChar());
                        statusPanel.setControllerStatus(getControllersStatus());
                        break;

                    case STATE_DELAY:
                        LocalUtil.sleep(nxtDelay);
                        setThreadState(NetworkState.STATE_SEND);
                        requestQueue.notifyToCreateRequestToChange();

                        break;

                    case STATE_READ:

                        // check if there is a data to change
                        responseMessage = (ResponseMessage) com.read();
                        logger.info(responseMessage);
                        statusPanel.setReceiveMsg(responseMessage.getIndex() + " " + responseMessage);
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
                                setThreadState(NetworkState.STATE_ERROR);
                            }
                        }

                        break;

                    case STATE_ERROR:

                        setThreadState(NetworkState.STATE_DELAY);
                        errCount++;
                        if (errCount < maxError) {
                            responseMessage = new ResponseMessage(null);
                            requestQueue.setReplyForPreviousRequestPending(true);
                            logger.info("Error count : " + errCount);
                            logger.info("Error [" + responseMessage + "]");
                            statusPanel.setReceiveMsg("" + responseMessage + " " + errCount);
                        } else {
                            responseMessage = new ResponseMessage(null);
                            logger.info("Error count : " + errCount);
                            logger.info("Error [" + responseMessage + "]");
                            statusPanel.setReceiveMsg("" + responseMessage + " " + errCount);
                            errCount = 0;
                            responseMessageMap.put((RequestMessage) sendMessage, responseMessage);

                            // we need at least one controller in on state
                            // otherwise we we set all controllers on
                            if (allControllersOff()) {
                                setControllersOn();
                            }
                        }

                        break;

                    case STATE_TIMEOUT:
                        setThreadState(NetworkState.STATE_DELAY);
                        errCount++;
                        if (errCount < maxError) {
                            requestQueue.setReplyForPreviousRequestPending(true);
                            logger.info(responseMessage + " Error count : " + errCount);
                            statusPanel.setReceiveMsg("" + responseMessage + " " + errCount);
                        } else {
                            logger.info("Error count : " + errCount);
                            logger.info("Error count " + errCount + " : SOT Error");
                            statusPanel.setReceiveMsg("" + responseMessage + " " + errCount);
                            errCount = 0;
                            responseMessageMap.put((RequestMessage) sendMessage, new ResponseMessage(null));
                            // we need at least one controller in on state
                            // otherwise we we set all controllers on
                            if (allControllersOff()) {
                                setControllersOn();
                            }
                        }

                        break;

                    case STATE_ABORT:
                        stop = true;
                        closingComport();
                        wcsl.showErrorMsg("Network Error", "Connection aborted .\nCheck USB cable connection !");

                        break;

                    default:
                        break;
                }
            } catch (IOException ex) {
                if (DEBUG) {
                    logger.error("connection aborted , error in network." + ex);
                }
                networkState = NetworkState.STATE_ABORT;
                logger.error("Connection aborted \n Input\\Output  error.", ex);
                JOptionPane.showMessageDialog(null, "Connection aborted \n Input\\Output  error.");
            } catch (Exception e) {
                closingComport();
                stop = true;
                logger.error("connection aborted , unknown error.", e);
                JOptionPane.showMessageDialog(null, "Connection aborted \n Unknown error.");
            }
        }
        logger.info("Stop socket thread ");
    }

    private void sendRequestHandler() throws IOException {
        // checking the state of terminal
        setThreadState(NetworkState.STATE_BUSY);
        sendMessage = requestQueue.getRequest();
        if (sendMessage == null) {
            setThreadState(NetworkState.STATE_DELAY);
        } else {
            logger.debug(sendMessage);
            logger.debug(reqIndex);
            sendMessage.setIndex(reqIndex.getIndex());
            com.write("V" + reqIndex.getIndex(), sendMessage, sotDelay, eotDelay);
            logger.info("Sent [" + sendMessage + "]");
            logger.info("Wait for data...");
            statusPanel.setSendMsg("V" + reqIndex.getIndex() + sendMessage);
            // check if there is a data to change
            reqIndex.nextIndex();
        }
    }

    /**
     * @throws HeadlessException
     */
    private void startingCommunication() throws HeadlessException {
        // Here we transform each controller into
        // the observer the manager of messages
        for (MessageManager cmm : controllerManagers) {
            requestQueue.addObserver(cmm);
            responseMessageMap.addObserver(cmm);
        }

        // prepare send messages
        requestQueue.notifyToPrepareRequests();

        if (controllerManagers.size() > 0) {
            setThreadState(NetworkState.STATE_DELAY);
        } else {
            JOptionPane.showMessageDialog(wcsl, "Can not start communication.\n No active controllers .");
            setThreadState(NetworkState.STATE_STOP);
        }
    }

    /**
     * Closing communication port a
     */
    public void closingComport() {
        if (com != null) {
            com.close();
            logger.info("Com port was closed");
        }
    }

    /**
     * Set status panel
     *
     * @param sp the status panel
     */
    public void setStatusPanel(StatusPanel sp) {
        statusPanel = sp;
    }

    /**
     * Return network state
     *
     * @return network state
     */
    @Override
    public synchronized NetworkState getNetworkState() {
        return networkState;
    }

    /**
     * @param networkState
     */
    @Override
    public synchronized void setThreadState(NetworkState networkState) {
        synchronized (this) {
            this.networkState = networkState;
            if (networkState == NetworkState.STATE_STOP) {
                System.out.println("Stopped");
            }
        }
    }

    /**
     * Return controllers status string.
     *
     * @return statusString the controllers status
     */
    private String getControllersStatus() {
        final StringBuilder statusString = new StringBuilder();
        for (int i = controllerManagers.size() - 1; i >= 0; i--) {
            Controller c = controllerManagers.get(i).getController();
            statusString.append(c.getStatusString());
        }

        return statusString.toString();
    }

    /**
     * Scanning controllers and checking if all controllers in "Off" status
     *
     * @return true if controller in "Off" state, otherwise return false
     */
    private boolean allControllersOff() {
        for (MessageManager cmm : controllerManagers) {
            if (cmm.getController().isOn()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Set all controllers to state to "On" status
     */
    private void setControllersOn() {
        for (MessageManager cmm : controllerManagers) {
            cmm.getController().switchOn();
        }
    }
}