/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.agrologic.app.messaging;

import com.agrologic.app.common.CommonConstant;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.network.CommandType;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Title: ControllerMessageManager <br> Description: Decorator for Controller and Observer for MessageManager<br>
 * Copyright: Copyright (c) 2009 <br>
 *
 * @author Valery Manakhimov
 * @version 1.0 <br>
 */
public class MessageManager implements Observer {
    private static final int REQUEST_CYCLE = 2; //request cycle must be minimum 5
    private static final long SET_CLOCK_DATA_ID = 1309;
    private int histRequestCycle = REQUEST_CYCLE;
    private int hist24RequestCycle = REQUEST_CYCLE;
    private Controller controller;
    private ControllerDao controllerDao;
    private FlockDao flockDao;
    private DataDao dataDao;
    private Flock flock;
    private MessageParser messageParser;
    private HashMap<Long, Data> onlineDatatable;
    private RequestMessage requestToSend;
    private RequestPriorityQueue requests;
    private RequestMessageQueueHistory24 requestsHistory24;
    private Logger logger;
    private boolean updatedFlag = true;
    private boolean requestCreated = false;

    public MessageManager(Controller controller) {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller.getNetName());
        this.controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
        this.flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
        this.dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        this.logger = Logger.getLogger(MessageManager.class);
    }

    /**
     * This constructor create MessageManager for webchick-local module .
     *
     * @param controller
     * @param databaseAccessor
     */
    public MessageManager(Controller controller, DatabaseAccessor databaseAccessor) {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller.getNetName());
        this.controllerDao = databaseAccessor.getControllerDao();
        this.flockDao = databaseAccessor.getFlockDao();
        this.dataDao = databaseAccessor.getDataDao();
        this.logger = Logger.getLogger(MessageManager.class);
    }

    private boolean isRequestCreated() {
        return requestCreated;
    }

    private void setRequestCreated(boolean requestCreated) {
        this.requestCreated = requestCreated;
    }
    /**
     * Method works when observable object MessageManager notify observers.(as specified by {@link Observer#update})
     *
     * @param o the MessageManager object
     * @param c the command object to execute
     */
    @Override
    public void update(Observable o, Object c) {
        try {
            CommandType command = (CommandType) c;
            switch (command) {
                case CREATE_REQUEST:
                    if (requestShouldBeCreated()) {
                        createRequest();
                        if (isRequestCreated()) {
                            updatedFlag = false;
                            ((RequestMessageQueue) o).addRequest(requestToSend);
                        }
                    }
                    break;

                case CREATE_REQUEST_TO_WRITE:
                    if (requestShouldBeCreated()) {
                        createRequestToWrite();
                        if (isRequestCreated()) {
                            updatedFlag = false;
                            ((RequestMessageQueue) o).addRequest(requestToSend);
                        }
                    }
                    break;

                case UPDATE:
                    updateDataList((ResponseMessageMap) o);
                    break;

                case SKIP_TO_WRITE:
                    updateDataList((ResponseMessageMap) o);
                    break;

                case SKIP_UNUSED:
                    errorHandler((ResponseMessageMap) o);
                    break;

                case ERROR:
                    errorHandler((ResponseMessageMap) o);
                    break;

                default:
                    break;
            }

        } catch (SQLException ex) {
            logger.error("Error during access to the databse. ", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Creating request and add to queue.
     */
    private void createRequest() throws SQLException {
        if (updatedFlag == false) {
            setRequestCreated(false);
        }
        if (graphsShouldBeRequested() && isSetClockInOnlineData()) {
            //2. create graph request hourly.
            requestToSend = new MessageFactory().createGraphRequest(controller.getNetName());
            setRequestCreated(true);
        } else if (historyShouldBeRequested()) {
            //3.1. create history daily
            try {
                requestToSend = createHistoryRequest();
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                setRequestCreated(false);
            }
        } else if (history24HourShouldBeRequested()) {
            //3.2. create history 24 hours
            try {
                requestToSend = create24HourHistoryRequest();
                setRequestCreated(true);
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                setRequestCreated(false);
            }
////        } else if (histogramShouldBeRequested()) {
////            //3.3. create histogram 24 hours
////            reqToSend = new RequestMessage(MessageType.REQUEST_HISTOGRAM, getNetName() , "A");
        } else {
            //4. create request for one of the next message types
            try {
                requestToSend = requests.next();
                setRequestCreated(true);
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                setRequestCreated(false);
            }
        }
    }

    /**
     *
     * @return @throws SQLException
     */
    private void createRequestToWrite() throws SQLException {
        if (isAnyDataToChange()) {//1. create request to write if it necessary.
            Data data = messageParser.getDataToSend();
            controllerDao.removeChangedValue(controller.getId(), data.getId());
            requestToSend = new MessageFactory().createWriteRequest(controller.getNetName(), data.getType(), data.getValue());
            messageParser.setDataToSend(null);
            setRequestCreated(true);
        }
        setRequestCreated(false);
    }

    /**
     * Test if need to create request for this controller.
     * Also set controller state to ON if it was in off state
     *
     * @return true if request fro this controller should be created.
     */
    public boolean requestShouldBeCreated() {
        // if controller in off state
        // don't create requests
        if (controller.isOn()) {
            return true;
        } else {
            if (controller.shouldExitFromOfState()) {
                controller.switchOn();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Test if there is any new data value in database.
     *
     * @return true if controller data should be changes
     * @throws SQLException
     */
    private boolean isAnyDataToChange() throws SQLException {
        Data data = dataDao.getChangedDataValue(controller.getId());
        if (data != null) {
            messageParser.setDataToSend(data);
            return true;
        }
        return false;
    }

    /**
     * Return true if took 1 hour with the last update graphs or never been updated.
     *
     * @return true if never been updated or took 1 hour with the last update.
     * @throws SQLException
     */
    private boolean graphsShouldBeRequested() throws SQLException {
        final Timestamp updateTime = controllerDao.getUpdatedGraphTime(controller.getId());
        if (updateTime == null) {
            return true;
        }
        final long timeSinceUpdated = System.currentTimeMillis() - updateTime.getTime();
        if (timeSinceUpdated > CommonConstant.ONE_HOUR) {
            return true;
        }
        return false;
    }

    /**
     * Return true if new flock is created or\and history should be updated.
     *
     * @return true if history should be requested.
     * @throws SQLException
     */
    private boolean historyShouldBeRequested() throws SQLException {
        // lasy initialisation of flock
        if (flock == null) {
            flock = flockDao.getOpenFlockByController(controller.getId());
        }
        // flock is opened
        if (flock != null) {
            System.out.println(" History Request Cycle : " + histRequestCycle);
            if (histRequestCycle == 0 && (requestsHistory24 == null || requestsHistory24.isCycleComplete())) {
                resetHistReqCycle();
                if (requestsHistory24 != null) {
                    requestsHistory24.resetCycleFlag();
                }
                Data growDay = dataDao.getGrowDay(controller.getId());
                if (growDay == null) {
                    return false;
                }
                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
                if (updatedGrowDay == null || growDay.getValue() > updatedGrowDay + 1) {
                    return true;
                }
            }
            decHistReqCycle();
        }
        return false;
    }

    /**
     * Return true if new flock is and history should be updated.
     *
     * @return true if history should be requested.
     * @throws SQLException
     */
    private boolean history24HourShouldBeRequested() throws SQLException {
        // lasy initialisation of flock
        if (flock == null) {
            flock = flockDao.getOpenFlockByController(controller.getId());
        }

        // flock is opened
        if (flock != null) {
            System.out.println(" History24 Request Cycle : " + hist24RequestCycle);
            if (hist24RequestCycle == 0) {
                Data growDay = dataDao.getGrowDay(controller.getId());
                if (growDay == null) {
                    return false;
                }
                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
                try {
                    if (updatedGrowDay == null || (growDay.getValue() > updatedGrowDay + 1)) {
                        return true;
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            decHist24ReqCycle();
        }
        return false;
    }

    private void resetHistReqCycle() {
        histRequestCycle = REQUEST_CYCLE;
    }

    private void decHistReqCycle() {
        if (histRequestCycle > 0) {
            histRequestCycle--;
        }
    }

    private void resetHist24ReqCycle() {
        hist24RequestCycle = REQUEST_CYCLE;
    }

    private void decHist24ReqCycle() {
        if (hist24RequestCycle > 0) {
            hist24RequestCycle--;
        }
    }

    /**
     * Create history request for this controller.
     *
     * @return requestHistory the history request.
     * @throws SQLException
     */
    private RequestMessage createHistoryRequest() throws SQLException, IllegalAccessException {
        Integer growDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
        if (growDay == null) {
            growDay = 1;
        } else {
            growDay += 1;
        }
        return new RequestMessage(MessageType.REQUEST_HISTORY, controller.getNetName(), growDay, null);
    }

    /**
     * Create history request for this controller.
     *
     * @return requestHistory the history request.
     * @throws SQLException
     */
    private RequestMessage create24HourHistoryRequest() throws SQLException, IllegalAccessException {
        Integer growDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
        if (growDay == null) {
            growDay = 1;
        }

        if (requestsHistory24 == null) {
            requestsHistory24 = new RequestMessageQueueHistory24(controller.getNetName(), growDay);
        }

        RequestMessage returnRequest = requestsHistory24.next();
        if (requestsHistory24.isCycleComplete()) {
            requestsHistory24.recreateHistory24Requests(growDay + 1);
            resetHist24ReqCycle();
        }
        return returnRequest;
    }

    /**
     * @param responseMessageMap
     * @throws SQLException
     */
    public void updateDataList(ResponseMessageMap responseMessageMap) throws SQLException {
        createOnlineData();

        Map<RequestMessage, ResponseMessage> responses = responseMessageMap.getResponseMap();
        boolean exist = responseMessageMap.isContainRequest(requestToSend);

        if (exist) {
            final MessageType msgType = requestToSend.getMessageType();
            final ResponseMessage response = responses.get(requestToSend);
            Timestamp currTime = new Timestamp(System.currentTimeMillis());
            switch (msgType) {
                case REQUEST_TO_WRITE:
                    messageParser.parseShortResponse(response, requestToSend);
                    Long dataId = requestToSend.getDataId();
                    Long dataVal = requestToSend.getValue();
                    controllerDao.updateControllerData(controller.getId(), dataId, dataVal);
                    controllerDao.removeChangedValue(controller.getId(), dataId);
                    break;
                case REQUEST_CHANGED:
                    messageParser.parseResponse(response, true);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_PANEL:
                    messageParser.parseResponse(response, false);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_CHICK_SCALE:
                    messageParser.parseResponse(response, false);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_CONTROLLER:
                    messageParser.parseResponse(response, false);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_HISTOGRAM:
                    String datasetHistogram = new String(response.getBuffer(), 0, response.getBuffer().length);
                    controllerDao.updateControllerHistogram(
                            controller.getId(), requestToSend.getPlate(), datasetHistogram,
                            new Timestamp(System.currentTimeMillis()));
                    controller.setGraphUpdateTime(currTime.getTime());
                    break;

                case REQUEST_EGG_COUNT:
                    messageParser.parseResponse(response, false);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_GRAPHS:
                    controllerDao.updateControllerGraph(controller.getId(), response.toString(), currTime);
                    controller.setGraphUpdateTime(currTime.getTime());
                    break;

                case REQUEST_HISTORY:
                    Integer growDay = requestToSend.getGrowDay();
                    if (response.toString().equals("Request Error")) {
                        flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, "-1");
                    } else {
                        flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, response.toString());
                    }
                    break;

                case REQUEST_HISTORY_24_HOUR:
                    Integer growDay24 = requestToSend.getGrowDay();
                    String dnum = requestToSend.getDnum();
                    if (response.toString().equals("Request Error")) {
                        flockDao.updateHistory24ByGrowDay(flock.getFlockId(), growDay24, dnum, "-1");
                    } else {
                        flockDao.updateHistory24ByGrowDay(flock.getFlockId(), growDay24, dnum, response.toString());
                    }
                    break;

                default:
                    break;
            }
            // remove response by request.
            responseMessageMap.removeResponse(requestToSend);
            updatedFlag = true;
        }
    }

    /**
     * Lazy initialization of onlineData. Get List of Data objects from database and store into TreeMap onlineData.
     *
     * @throws SQLException
     */
    private void createOnlineData() throws SQLException {
        if(this.messageParser == null) {
            final Collection<Data> dataItems = dataDao.getAll();
            onlineDatatable = new HashMap<Long, Data>();
            for (Data d : dataItems) {
                onlineDatatable.put(d.getId(), (Data) d.clone());
            }
            this.messageParser = new MessageParser(onlineDatatable);
        }
    }

    /**
     * Return updatedValues with data to update.
     *
     * @return updatedValues the sorted map with updated values.
     */
    public SortedMap<Long, Data> getUpdatedOnlineData() {
        SortedMap<Long, Data> updatedValues = new TreeMap<Long, Data>();
        onlineDatatable = messageParser.getParsedDataMap();
        for (long key = 0; key < CommonConstant.DATA_TABLE_SIZE; key++) {
            if (onlineDatatable.containsKey(key)) {
                Data d = onlineDatatable.get(key);
                if (d.isUpdated()) {
                    updatedValues.put(key, d);
                    d.setUpdated(false);
                }
            }
        }
        return updatedValues;
    }

    /**
     * Return true if set clock of controller is in online data .
     *
     * @return true if set clock in online data , otherwise false.
     */
    private boolean isSetClockInOnlineData() {
        if (messageParser == null) {
            return false;
        }

        onlineDatatable = messageParser.getParsedDataMap();
        if (!onlineDatatable.containsKey(SET_CLOCK_DATA_ID)) {
            return false;
        }

        Data d = onlineDatatable.get(SET_CLOCK_DATA_ID);
        if (d.getValue() == null) {
            return false;
        }
        return true;
    }

    /**
     * Remove unused requests from request queue if data doesn't exist in controller and remove error response from
     * response map.
     *
     * @param responseMessageMap the message manager
     * @throws SQLException
     */
    private void errorHandler(final ResponseMessageMap responseMessageMap) throws SQLException {
        boolean exist = responseMessageMap.isContainRequest(requestToSend);
        if (exist) {
            updatedFlag = true;
            if (requestToSend.isUnusedType()) {
                requests.removedUnusedFromQueue(requestToSend);
            }
            if (requestToSend.getMessageType() == MessageType.REQUEST_PANEL
                    || requestToSend.getMessageType() == MessageType.REQUEST_CONTROLLER
                    || requestToSend.getMessageType() == MessageType.REQUEST_GRAPHS) {
                controller.switchOff();
                controllerDao.resetControllerData(controller.getId());
            }
            if (requestToSend.getMessageType() == MessageType.REQUEST_CHANGED) {
                requests.remove(requestToSend);
                requests.initReaTimeRequest();
            }
            if (requestToSend.getMessageType() == MessageType.REQUEST_HISTORY) {
                responseMessageMap.removeResponse(requestToSend);
            }
            responseMessageMap.removeResponse(requestToSend);
        }
    }

    /**
     * Return controller object
     *
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @return
     * @throws SQLException
     */
    private boolean histogramShouldBeRequested() throws SQLException {
        final Timestamp updateTime = controllerDao.getHistogramUpdateTime(controller.getId());
        if (updateTime == null) {
            return true;
        }
        final long timeSinceUpdated = System.currentTimeMillis() - updateTime.getTime();
        if (timeSinceUpdated > CommonConstant.ONE_HOUR) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("MessageManager : ").append(controller.getNetName()).toString();
    }
}