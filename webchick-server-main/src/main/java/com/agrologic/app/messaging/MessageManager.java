package com.agrologic.app.messaging;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.network.CommandType;
import com.agrologic.app.util.ApplicationUtil;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Object for collecting messages that occur during the communication
 *
 * @author Valery Manakhimov
 * @version 1.0 <br>
 */

////Created for each Controller

public class MessageManager implements Observer {
    public static final int DATA_TABLE_SIZE = 65535;
    private static final int REQUEST_CYCLE = 5; //request cycle must be minimum 5
    private static final long SET_CLOCK_DATA_ID = 1309;
    private int perPerDayReportRequestCycle = REQUEST_CYCLE;
    private int perHourReportRequestCycle = REQUEST_CYCLE;
    private Controller controller;
    private ControllerDao controllerDao;
    private FlockDao flockDao;
    private DataDao dataDao;
    private Flock flock;
    private MessageParser messageParser;
    private HashMap<Long, Data> onlineDatatable;
    private RequestMessage requestToSend;
    private RequestPriorityQueue requests;
    private PerHourReportRequestQueue perHourReportRequestQueue;
    private Logger logger;
    private boolean updatedFlag = true;
    private boolean requestCreated = false;

    public MessageManager(Controller controller) {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller);/////////////////////////////////////////////////////////////////////////////////////
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
    public MessageManager(Controller controller, DatabaseAccessor databaseAccessor) throws SQLException {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller);
        this.controllerDao = databaseAccessor.getControllerDao();
        this.flockDao = databaseAccessor.getFlockDao();
        this.dataDao = databaseAccessor.getDataDao();
        this.logger = Logger.getLogger(MessageManager.class);
    }

    public void recreateRequests() {
        this.requests = new RequestPriorityQueue(controller);
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
            ApplicationUtil.sleep(10);
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
            logger.error("Error during access to the database. ", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Creating request and add to queue.
     */

    private void createRequest() throws SQLException, ParseException {
        if (updatedFlag == false) {
            setRequestCreated(false);
        }
        if (graphsShouldBeRequested() && isSetClockInOnlineData()) {
            //2. create graph request hourly.
            requestToSend = MessageFactory.createGraphRequest(controller.getNetName());//T901* 56\r
            setRequestCreated(true);
        } else if (dailyReportShouldBeRequested()) {
            //3.1. create management daily
            try {
                requestToSend = createDailyReportRequest();//%T901h1 167\r
                setRequestCreated(true);
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                recreateRequests();
                setRequestCreated(false);
            }
        }
        else if (perHourReportsShouldBeRequested()) {
//            //3.2. create management 24 hours
//            try {
//                requestToSend = createPerHourReportsRequest();// D
//                setRequestCreated(true);
//            } catch (IllegalAccessException ex) {
////                controller.switchOff();
//                recreateRequests();
//                setRequestCreated(false);
//            }
//////        } else if (histogramShouldBeRequested()) {
//////            //3.3. create histogram 24 hours
//////            reqToSend = new RequestMessage(MessageType.REQUEST_HISTOGRAM, getNetName() , "A");
        }
        else {
            //4. create request for one of the next message types
            try {
                requestToSend = requests.next();//a,b...
                setRequestCreated(true);
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                recreateRequests();
                setRequestCreated(false);
            }
        }
    }

    /**
     * Create request to change data in controller.The request creates if there any data to change in database.
     * Get from database the data and also remove to avoid creating request with same data .
     *
     * @return @throws SQLException if failed to get data
     */
    private void createRequestToWrite() throws SQLException {
        if (isAnyDataToChange()) {//1. create request to write if it necessary.
            Data data = messageParser.getDataToSend();
            controllerDao.removeChangedValue(controller.getId(), data.getId());
            requestToSend = new MessageFactory().createWriteRequest(controller.getNetName(), data.getType(), data.getValue());
            messageParser.setDataToSend(null);
            setRequestCreated(true);
        } else {
            setRequestCreated(false);
        }
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
     * Return true if there is any new data value in database.
     *
     * @return true if controller data should be changes
     * @throws SQLException if failed to get data from database
     */
    private boolean isAnyDataToChange() throws SQLException {
        Data data = dataDao.getChangedDataValue(controller.getId());
        if (data != null && messageParser != null) {
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
//        Long t = TimeUnit.HOURS.toMillis(1L);
        if (timeSinceUpdated > TimeUnit.HOURS.toMillis(1L)) {
            return true;
        }
        return false;
    }

    /**
     * Return true if new flock is created or\and management should be updated.
     *
     * @return true if management should be requested.
     * @throws SQLException
     */

//    private boolean dailyReportShouldBeRequested() throws SQLException {
//        // lasy initialisation of flock
//        if (flock == null) {
//            flock = flockDao.getOpenFlockByController(controller.getId());
//        }
//        // flock is opened
//        if (flock != null) {
//            if (perPerDayReportRequestCycle == 0 && (perHourReportRequestQueue == null || perHourReportRequestQueue.isCycleComplete())) {
//                resetHistReqCycle();
//                if (perHourReportRequestQueue != null) {
//                    perHourReportRequestQueue.resetCycleFlag();
//                }
//                Data growDay = dataDao.getGrowDay(controller.getId());
//                if (growDay == null || growDay.getValue() == 1) {
//                    return false;
//                }
//                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
//                if (updatedGrowDay == null || growDay.getValue() > updatedGrowDay + 1) {
//                    return true;
//                }
//            }
//            decPerDayReportRequestCycle();
//        }
//        return false;
//    }

    private boolean dailyReportShouldBeRequested() throws SQLException {
        // lasy initialisation of flock
        if (flock == null) {
            flock = flockDao.getOpenFlockByController(controller.getId());
        }
        // flock is opened
        if (flock != null) {
            if (perPerDayReportRequestCycle == 0 && (perHourReportRequestQueue == null || perHourReportRequestQueue.isCycleComplete())) {
                resetHistReqCycle();
                if (perHourReportRequestQueue != null) {
                    perHourReportRequestQueue.resetCycleFlag();
                }
                Data growDay = dataDao.getGrowDay(controller.getId());
                if (growDay == null || growDay.getValue() == 1) {
                    return false;
                }
                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
                if (updatedGrowDay == null || growDay.getValue() > updatedGrowDay + 1) {
                    return true;
                }
            }
            decPerDayReportRequestCycle();
//            return true;
        }
        return false;
    }

    /**
     * Return true if new flock is and management should be updated.
     *
     * @return true if management should be requested.
     * @throws SQLException
     */
    private boolean perHourReportsShouldBeRequested() throws SQLException {

        // lasy initialization of flock
        if (flock == null) {
            flock = flockDao.getOpenFlockByController(controller.getId());
        }

        Collection<Data> perHourReportDataList = dataDao.getPerHourHistoryDataByControllerValues(controller.getId());
        if (perHourReportDataList.isEmpty()) {
            return false;
        }

        // flock is opened
        if (flock != null) {
            if (perPerDayReportRequestCycle == 0) {
                Data growDay = dataDao.getGrowDay(controller.getId());
                if (growDay == null) {
                    return false;
                }

                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
                try {
                    if (updatedGrowDay == null || (growDay.getValue() > updatedGrowDay + 1)) {
                        return true;
                    } else {
                        int perHourReportCount = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), updatedGrowDay, 1L).size();
                        if (perHourReportRequestQueue != null){ // 04/01/2018
                            int perHourRecordQueueCount = perHourReportRequestQueue.getSize();
                            if (perHourReportCount < perHourRecordQueueCount) {
                                return true;
                            } else {
                                perHourReportRequestQueue.recreateRequests(updatedGrowDay + 1);
                                resetPerHourReportRequestCycle();
                                return false;
                            }
                        }// 04/01/2018
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return false;
    }

    private void resetHistReqCycle() {
        perPerDayReportRequestCycle = REQUEST_CYCLE;
    }

    private void decPerDayReportRequestCycle() {
        if (perPerDayReportRequestCycle > 0) {
            perPerDayReportRequestCycle--;
        }
    }

    private void resetPerHourReportRequestCycle() {
        perHourReportRequestCycle = REQUEST_CYCLE;
    }

    private void decPerHourReportRequestCycle() {
        if (perHourReportRequestCycle > 0) {
            perHourReportRequestCycle--;
        }
    }

    /**
     * Create management request for this controller.
     *
     * @return requestHistory the management request.
     * @throws SQLException
     */
    private RequestMessage createDailyReportRequest() throws SQLException, IllegalAccessException {
        Integer growDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
        if (growDay == null) {
            growDay = 1;
        } else {
            growDay += 1;
        }
        return MessageFactory.createDailyReportRequests(controller.getNetName(), growDay);
    }

    /**
     * Create management request for this controller.
     *
     * @return requestHistory the management request.
     * @throws SQLException
     */
    private RequestMessage createPerHourReportsRequest() throws SQLException, IllegalAccessException {

        Collection<Data> perHourReportDataList = dataDao.getPerHourHistoryDataByControllerValues(controller.getId());
        if (perHourReportRequestQueue == null) {
            perHourReportRequestQueue = new PerHourReportRequestQueue(controller.getNetName(), perHourReportDataList);
        }

        Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
        if (updatedGrowDay == null) {
            updatedGrowDay = 1;
        }

        if (perHourReportRequestQueue.getGrowDay().equals(updatedGrowDay)) {
            int perHourReportCount = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), updatedGrowDay, 1L).size();
            int perHourRecordQueueCount = perHourReportRequestQueue.getSize();
            if (perHourReportCount >= perHourRecordQueueCount) {
                perHourReportRequestQueue.recreateRequests(updatedGrowDay + 1);
            }
        } else {
            perHourReportRequestQueue.recreateRequests(updatedGrowDay);
        }
        return perHourReportRequestQueue.next();
    }

    /**
     * Update online data map
     *
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
                    controllerDao.updateControllerHistogram(controller.getId(), requestToSend.getPlate(), datasetHistogram, new Timestamp(System.currentTimeMillis()));
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
                        if (controller.getName().startsWith("Image") && response.getFormat() == Message.Format.BINARY) {
                            String fixedResponse = messageParser.parseHistoryIfComProtocolBinary(response);
                            flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, fixedResponse);
                        } else {
                            flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, response.toString());
                        }
                    }
                    break;

                case REQUEST_PER_HOUR_REPORTS:
                    Integer growDay24 = requestToSend.getGrowDay();
                    String dnum = requestToSend.getDnum();
                    if (response.toString().equals("Request Error")) {
                        flockDao.updateHistory24ByGrowDay(flock.getFlockId(), growDay24, dnum, "-1");
                    } else {
                        if (controller.getName().startsWith("Image") && response.getFormat() == Message.Format.BINARY) {
                            String fixedResponse = messageParser.parseHistory24IfComProtocolBinary(response);
                            flockDao.updateHistory24ByGrowDay(flock.getFlockId(), growDay24, dnum, fixedResponse);
                        } else {
                            flockDao.updateHistory24ByGrowDay(flock.getFlockId(), growDay24, dnum, response.toString());
                        }
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
        if (this.messageParser == null) {
            final Collection<Data> dataItems = dataDao.getAll();
            onlineDatatable = new HashMap<Long, Data>();
            for (Data d : dataItems) {
                onlineDatatable.put(d.getId(), (Data) d.clone());
            }
            this.messageParser = new MessageParser(onlineDatatable);
        }
    }

    /**
     * Return updatedValues with data to update .
     *
     * @return updatedValues the sorted map with updated values.
     */
    public SortedMap<Long, Data> getUpdatedOnlineData() {
        SortedMap<Long, Data> updatedValues = new TreeMap<Long, Data>();
        onlineDatatable = messageParser.getParsedDataMap();
        for (long key = 0; key < DATA_TABLE_SIZE; key++) {
            Data d = onlineDatatable.get(key);
            if (d != null) {
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
            if (requestToSend.isUnusedType(controller.getName())) {
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
        final Timestamp updateTime = controllerDao.getHistogramUpdatedTime(controller.getId());
        if (updateTime == null) {
            return true;
        }
        final long timeSinceUpdated = System.currentTimeMillis() - updateTime.getTime();
        if (timeSinceUpdated > TimeUnit.HOURS.toMillis(1L)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("MessageManager : ").append(controller.getNetName()).toString();
    }
}




//    private void checkAndFixFlock (Data onlineGrowDay) throws SQLException { // 14/11/2017
//        long updatedGrowDay;
//        long onlGrowDay;
//        Date currentDay;
//        SimpleDateFormat sdf;
//        Flock newFlock;
//        long maxFlockId;
//
//        sdf = new SimpleDateFormat("d/M/yyyy");
//        currentDay = getCurrentDay();
//        onlGrowDay = onlineGrowDay.getValue();
//        if(flockDao.getUpdatedGrowDayHistory(flock.getFlockId()) != null) {
//            updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
//            if ((updatedGrowDay > (onlGrowDay - 1)) && (onlGrowDay != -1) && (flock.getStatus().toLowerCase().equals("Open".toLowerCase())) && (onlGrowDay != 1)) {
//                flock.setStatus("Close");
//                flock.setEndDate(sdf.format(currentDay));
//                flockDao.updateFlockStatus(flock);
//                flockDao.updateFlockEndDay(flock);
//                newFlock = new Flock ();// 27/11/2017
//                maxFlockId = flockDao.getMaxFlockId();// 27/11/2017
//                newFlock.setFlockId(maxFlockId + 1);// 27/11/2017
//                newFlock.setStartDate(sdf.format(currentDay));// 27/11/2017
//                newFlock.setControllerId(flock.getControllerId());// 27/11/2017
//                newFlock.setFlockName(flock.getFlockName());// 27/11/2017
//                newFlock.setStatus("Open");// 27/11/2017
//                flockDao.insert(newFlock);// 27/11/2017
//                flock = flockDao.getOpenFlockByController(controller.getId());// 27/11/2017
//            }
//        }
//    } // 14/11/2017

//    private Date addDays(Date date, int days) {// added 13/06/2017
//        Calendar cal;
//        cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DATE, days); //minus number would decrement the days
//        return cal.getTime();
//    } // added 13/06/2017
//
//    private Date getCurrentDay(){ // added 13/06/2017
//        Calendar cal;
//        Date date;
//
//        cal = Calendar.getInstance();
//
//        return date = cal.getTime();
//    } // added 13/06/2017


/////////////////////////////////////////////////////for test
//    private boolean dailyReportShouldBeRequested() throws SQLException, ParseException { //for web
//        Data growDay = dataDao.getGrowDay(controller.getId()); // added 13/06/2017
//        // lasy initialisation of flock
//        if (flock == null) {
//            flock = flockDao.getOpenFlockByController(controller.getId());
//        }
//        // flock is opened
//        if (flock != null) {
//            if(growDay != null) { // 14/11/2017 // && growDay != -1
//                checkAndFixStartDay(growDay);// added 13/06/2017
//                checkAndFixFlock(growDay);// 14/11/2017
//            } // 14/11/2017
//            if (perPerDayReportRequestCycle == 0 && (perHourReportRequestQueue == null || perHourReportRequestQueue.isCycleComplete())) {
//                resetHistReqCycle();
//                if (perHourReportRequestQueue != null) {
//                    perHourReportRequestQueue.resetCycleFlag();
//                }
////                Data growDay = dataDao.getGrowDay(controller.getId()); changed 13/06/2017
//                if (growDay == null) {
//                    return false;
//                }
//                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
//                if (updatedGrowDay == null || growDay.getValue() > updatedGrowDay + 1) {///////////////need to check!!!!!!!!!!!!!!!!
//                    Date currentDay; // 11/01/2018
//                    SimpleDateFormat sdf; // 11/01/2018
//
//                    sdf = new SimpleDateFormat("d/M/yyyy"); // 11/01/2018
//                    currentDay = Calendar.getInstance().getTime(); // 11/01/2018
//
//                    if(!(flock.getStartTime().equals(sdf.format(currentDay)))){ // 11/01/2018
//                        return true; // 11/01/2018
//                    } // 11/01/2018
//                }
//            }
//            decPerDayReportRequestCycle();
//        }
//        return false;
//    }
/////////////////////////////////////////////////////for test
//    private boolean dailyReportShouldBeRequested() throws SQLException, ParseException { //for web
//        Data growDay = dataDao.getGrowDay(controller.getId()); // added 13/06/2017
//        // lasy initialisation of flock
//        if (flock == null) {
//            flock = flockDao.getOpenFlockByController(controller.getId());
//        }
//        // flock is opened
//        if (flock != null) {
//            if(growDay != null) { // 14/11/2017 // && growDay != -1
//                checkAndFixFlock(growDay);// 14/11/2017
//                checkAndFixStartDay(growDay);// added 13/06/2017
////                checkAndFixFlock(growDay);// 14/11/2017
//            } // 14/11/2017
//            if (perPerDayReportRequestCycle == 0 && (perHourReportRequestQueue == null || perHourReportRequestQueue.isCycleComplete())) {
//                resetHistReqCycle();
//                if (perHourReportRequestQueue != null) {
//                    perHourReportRequestQueue.resetCycleFlag();
//                }
////                Data growDay = dataDao.getGrowDay(controller.getId()); changed 13/06/2017
//                if (growDay == null) {
//                    return false;
//                }
//                Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
//                if (updatedGrowDay == null || growDay.getValue() > updatedGrowDay + 1 && growDay != null && growDay.getValue() != 1) {
//                    return true;
//                }
//            }
//            decPerDayReportRequestCycle();
//        }
//        return false;
//    }

//    private void checkAndFixStartDay(Data onlineGrowDay) throws ParseException, SQLException { // added 13/06/2017
//        Integer updatedGrowDay;
//
//        updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
//        if (updatedGrowDay != null) {
//            if (onlineGrowDay.getValue().intValue() != -1 && !(updatedGrowDay + 1 == (onlineGrowDay.getValue())) && flock.getStatus().equals("Open".toLowerCase())) {
//                Date currentDay, onlineGrowDayDate, startDate, startDateToSet;
//                SimpleDateFormat sdf;
//                Data resetTime;
//
//                sdf = new SimpleDateFormat("d/M/yyyy");
//                if (flock != null) {
//                    startDate = sdf.parse(flock.getStartTime());
//                    onlineGrowDayDate = addDays(startDate, onlineGrowDay.getValue().intValue());
//                    currentDay = getCurrentDay();
//                    if (currentDay.compareTo(onlineGrowDayDate) != 0) {
//                        startDateToSet = addDays(currentDay, -((onlineGrowDay.getValue().intValue() - 1)));
//                        flock.setStartDate(sdf.format(startDateToSet));
//                        flockDao.updateFlockStartDay(flock);
//                    }
//                }
//            }
//        }
//    } // added 13/06/2017