/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.agrologic.app.network;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.messaging.*;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
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

    private static final int DATATABLE_SIZE = 65535;
    private static final int HIGH_16BIT_ON_MASK = 0x8000;
    private static final int HIGH_32BIT_OFF_MASK = 0x0000FFFF;
    private static final int SHIFT_16_BIT = 16;
    private static final int REQUEST_CYCLE = 2; //request cycle must be minimum 5
    private static final long SET_CLOCK_DATA_ID = 1309;
    private int histRequestCycle = REQUEST_CYCLE;
    private int hist24RequestCycle = REQUEST_CYCLE;
    private Controller controller;
    /**
     * dao objects
     */
    private DatabaseAccessor dbaccessor;
    private ControllerDao controllerDao;
    private FlockDao flockDao;
    private DataDao dataDao;
    /**
     * model objects
     */
    private Data data;
    private Flock flock;
    private HashMap<Long, Data> onlineDataItems;
    /**
     * network objects
     */
//    private RequestMessage prevRequestToSend;
    private RequestMessage requestToSend;
    private RequestMessage oldRequestToSend;
    private RequestPriorityQueue requests;
    private RequestQueueHistory24 requestsHistory24;
    private Logger logger;
    private boolean debug = false;
    private boolean justStarted = true;
    private boolean updatedFlag = true;

    /**
     * Copy constructor
     *
     * @param controller the object to copy
     */
    public MessageManager(Controller controller) {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller.getNetName());
        DaoFactory daoFactory = DbImplDecider.getDaoFactory(DaoType.MYSQL);
        this.controllerDao = daoFactory.getControllerDao();
        this.flockDao = daoFactory.getFlockDao();
        this.dataDao = daoFactory.getDataDao();
        this.logger = Logger.getLogger(MessageManager.class);
    }

    public MessageManager(Controller controller, DatabaseAccessor dbaccessor) {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller.getNetName());
        this.dbaccessor = dbaccessor;
        this.controllerDao = dbaccessor.getControllerDao();
        this.flockDao = dbaccessor.getFlockDao();
        this.dataDao = dbaccessor.getDataDao();
        this.onlineDataItems = (HashMap<Long, Data>) controller.getOnlineData();
        this.logger = Logger.getLogger(MessageManager.class);
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
                        createRequestIfNeedTo(o);
                    }
                    break;

                case CREATE_REQUEST_TO_WRITE:
                    if (requestShouldBeCreated()) {
                        boolean success = createRequestToWrite();
                        if (success) {
                            updatedFlag = false;
                            ((RequestMessageQueue) o).addRequest(requestToSend);
                        }
                    }
                    break;

                case UPDATE:
                    updateDataList((ResponseMessageMap) o);
                    justStarted = false;
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
     * @param o
     * @throws SQLException
     */
    public void createRequestIfNeedTo(Observable o) throws SQLException {
        boolean success = createRequest();
        if (success) {
            updatedFlag = false;
            ((RequestMessageQueue) o).addRequest(requestToSend);
        }
    }

    /**
     * Creating request and add to queue.
     */
    private boolean createRequest() throws SQLException {
        if (updatedFlag == false) {
            return false;
        }
        if (graphsShouldBeRequested() && isSetClockInOnlineData()) {
            //2. create graph request hourly.
            requestToSend = new RequestMessage(MessageType.REQUEST_GRAPHS, controller.getNetName());
        } else if (historyShouldBeRequested()) {
            //3.1. create history daily
            try {
                requestToSend = createHistoryRequest();
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                return false;
            }
        } else if (history24HourShouldBeRequested()) {
            //3.2. create history 24 hours
            try {
                requestToSend = create24HourHistoryRequest();
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                return false;
            }
////        } else if (histogramShouldBeRequested()) {
////            //3.3. create histogram 24 hours
////            reqToSend = new RequestMessage(MessageType.REQUEST_HISTOGRAM, getNetName() , "A");
        } else {
            //4. create request for one of the next message types
            try {
                requestToSend = requests.next();
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                return false;
            }
        }
        return true;
    }

    /**
     * @return @throws SQLException
     */
    private boolean createRequestToWrite() throws SQLException {
        if (isAnyDataToChange()) {//1. create request to write if it necessary.
            requestToSend = new RequestMessage(
                    MessageType.REQUEST_TO_WRITE, controller.getNetName(), data.getType(), data.getValue());
            data = null;
            return true;
        }
        return false;
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
        data = dataDao.getChangedDataValue(controller.getId());
        if (data != null) {
            controllerDao.removeChangedValue(controller.getId(), data.getId());
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
        if (timeSinceUpdated > controller.ONE_HOUR) {
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
            requestsHistory24 = new RequestQueueHistory24(controller.getNetName(), growDay);
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
        if (controller.getNetName().equals("T901")) {
            System.out.println(controller);
        }
        Map<RequestMessage, ResponseMessage> responses = responseMessageMap.getResponseMap();
        boolean exist = responseMessageMap.isMapCountainsRequest(requestToSend);

        if (exist) {
            final MessageType msgType = requestToSend.getMessageType();
            final ResponseMessage response = responses.get(requestToSend);
            Timestamp currTime = new Timestamp(System.currentTimeMillis());
            switch (msgType) {
                case REQUEST_TO_WRITE:
                    updateSingleOnlineData(response, requestToSend);
                    Long dataId = requestToSend.getDataId();
                    Long dataVal = requestToSend.getValue();
                    controllerDao.updateControllerData(controller.getId(), dataId, dataVal);
                    controllerDao.removeChangedValue(controller.getId(), dataId);
                    break;
                case REQUEST_CHANGED:
                    updateOnlineData(response, true);
                    controllerDao.updateControllerData(controller.getId(), getOnlineData().values());
                    break;

                case REQUEST_PANEL:
                    updateOnlineData(response);
                    controllerDao.updateControllerData(controller.getId(), getOnlineData().values());
                    break;

                case REQUEST_CHICK_SCALE:
                    updateOnlineData(response);
                    controllerDao.updateControllerData(controller.getId(), getOnlineData().values());
                    break;

                case REQUEST_CONTROLLER:
                    updateOnlineData(response);
                    controllerDao.updateControllerData(controller.getId(), getOnlineData().values());
                    break;

                case REQUEST_HISTOGRAM:
                    String datasetHistograsm = new String(response.getBuffer(), 0,
                            response.getBuffer().length);
                    controllerDao.updateControllerHistogram(
                            controller.getId(), requestToSend.getPlate(), datasetHistograsm,
                            new Timestamp(System.currentTimeMillis()));
                    controller.setGraphUpdateTime(currTime.getTime());
                    break;

                case REQUEST_EGG_COUNT:
                    updateOnlineData(response);
                    controllerDao.updateControllerData(controller.getId(), getOnlineData().values());
                    break;

                case REQUEST_GRAPHS:
                    String values = new String(response.getBuffer(), 0, response.getBuffer().length);
                    controllerDao.updateControllerGraph(controller.getId(), values, currTime);
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
        if (onlineDataItems == null || onlineDataItems.isEmpty()) {
            final Collection<Data> dataItems = dataDao.getAll();
            onlineDataItems = new HashMap<Long, Data>();
            for (Data d : dataItems) {
                onlineDataItems.put(d.getId(), (Data) d.clone());
            }
        }
    }

    /**
     * Update online data.
     *
     * @param response the response message
     */
    public void updateOnlineData(Message response) {
        updateOnlineData(response, false);
    }

    /**
     * Update online data and skip diagnostic pair that is in last pairs .
     *
     * @param response the response message @skipDiagnostic if true , skip last pairs .
     */
    private void updateOnlineData(Message response, boolean skipDiagnostic) {
        byte[] buffer = response.getBuffer();
        String responseString = new String(buffer, 0, buffer.length);
        logger.error(response);
        StringTokenizer token = new StringTokenizer(responseString, " ");
        int countTokens = token.countTokens();
        int count = 0;

        LOOP:
        while (token.hasMoreTokens()) {// loop for running in
            if (skipDiagnostic == true && count == countTokens - 2) {
                String changedData = token.nextToken();
                String unchangedData = token.nextToken();
                if (debug) {
                    logger.debug("======================Diagnostic====================");
                    logger.debug("Recieved: " + changedData + "; changed : " + unchangedData + ";");
                    logger.debug("================================================");
                }
                break;
            }
            try {//
                String dataIdString = token.nextToken();// get key data
                if (!token.hasMoreTokens()) {
                    break LOOP;
                }
                count++;

                String valueString = token.nextToken();// get value data
                count++;
                long dataId = Long.parseLong(dataIdString);
                int value = Integer.parseInt(valueString);
                int type = (int) dataId;// type of value (like 4096)
                if ((type & 0xC000) != 0xC000) {
                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
                } else {
                    dataId = (type & 0xFFFF);
                }

                if (onlineDataItems.containsKey(dataId)) {
                    if (onlineDataItems.get(dataId).isDoubleBuffer()) {
                        token.nextToken();// skip this key
                        count++;
                        int highValue = value;
                        boolean negative = ((highValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
                        if (negative) {
                            // two's compliment action
                            highValue = twosCompliment(highValue);
                        }
                        highValue <<= SHIFT_16_BIT;
                        valueString = token.nextToken();// get low value
                        count++;
                        value = Integer.parseInt(valueString);

                        int lowValue = value;
                        negative = ((lowValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
                        if (negative) {
                            // two's compliment action
                            lowValue = twosCompliment(lowValue);
                        }
                        value = highValue + lowValue;
                    }
                    // here we actually must to update data
                    try {
                        // if updated data sent to controller to change than skip update method.
                        if (data != null && data.getId().equals(dataId)) {
                            //skip update
                            if (debug) {
                                logger.debug("data id to write " + dataId);
                            }
                            data = null;
                        } else {
                            onlineDataItems.get(dataId).setValue((long) value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Update online data.
     *
     * @param response the response message
     */
    public void updateSingleOnlineData(Message response, Message request) {
        byte[] buffer = response.getBuffer();
        String worker = new String(buffer, 0, buffer.length);
        StringTokenizer token = new StringTokenizer(worker, " ");

        LOOP:
        while (token.hasMoreTokens()) {// loop for running in
            try {//
                String dataIdString = token.nextToken();// get key data
                if (!token.hasMoreTokens()) {
                    break LOOP;
                }
                String valueString = token.nextToken();// get value data
                long dataId = Long.parseLong(dataIdString);
                int value = Integer.parseInt(valueString);
                int type = (int) dataId;// type of value (4096)
                if ((type & 0xC000) != 0xC000) {
                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
                } else {
                    dataId = (type & 0xFFFF);
                }
                if (onlineDataItems.containsKey(dataId)) {
                    int fs = request.toString().indexOf(" ");
                    int ls = request.toString().lastIndexOf(" ");
                    String rv = request.toString().substring(fs + 1, ls);
                    int v = Integer.parseInt(rv);
                    if (v == value) {
                        // here we actually must to update data
                        onlineDataItems.get(dataId).setValue((long) value);
                    }
                }
            } catch (Exception ex) {
                logger.error("Unknown exception. ", ex);
            }
        }
    }

    /**
     * Return updatedValues with data to update.
     *
     * @return updatedValues the sorted map with updated values.
     */
    public SortedMap<Long, Data> getUpdatedOnlineData() {
        SortedMap<Long, Data> updatedValues = new TreeMap<Long, Data>();
        for (long key = 0; key < DATATABLE_SIZE; key++) {
            if (onlineDataItems.containsKey(key)) {
                Data d = onlineDataItems.get(key);
                if (d.isUpdated()) {
                    updatedValues.put(key, d);
                    d.setUpdated(false);
                }
            }
        }
        return updatedValues;
    }

    /**
     * Return online data .
     *
     * @return the online data.
     */
    public HashMap<Long, Data> getOnlineData() {
        return onlineDataItems;
    }

    /**
     * Check the onlineDataItems if the values of data already exist
     *
     * @return true if there is values exists, otherwise false
     */
    public boolean isOnlineDataReady() {
        boolean result = true;
        Iterator<Data> iter = getOnlineData().values().iterator();
        while (iter.hasNext()) {
            Data data = (Data) iter.next();
            if (!data.isDataReady()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Return true if set clock of controller is in online data .
     *
     * @return true if set clock in online data , otherwise false.
     */
    private boolean isSetClockInOnlineData() {
        if (onlineDataItems == null) {
            return false;
        }

        if (!onlineDataItems.containsKey(SET_CLOCK_DATA_ID)) {
            return false;
        }

        Data d = onlineDataItems.get(SET_CLOCK_DATA_ID);
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
        boolean exist = responseMessageMap.isMapCountainsRequest(requestToSend);
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
     * Return number after two's compliment for integer type.
     *
     * @param val the number
     * @return number the number after two's compliment
     */
    private int twosCompliment(int val) {
        int tVal = val;
        if (tVal != -1) {
            //two's compliment action
            tVal = Math.abs(tVal);
            tVal = ~tVal;
            tVal &= HIGH_32BIT_OFF_MASK;
            tVal += 1;
        }
        return tVal;
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
        if (timeSinceUpdated > controller.ONE_HOUR) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("MessageManager : ").append(controller.getNetName()).toString();

    }
}