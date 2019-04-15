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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MessageManager implements Observer {
    public static final int DATA_TABLE_SIZE = 65535;
    private static final int REQUEST_CYCLE = 5; //request cycle must be minimum 5
    private static final long SET_CLOCK_DATA_ID = 1309;
    private int per_day_request_cycle = REQUEST_CYCLE;
    private int per_hour_request_cycle = REQUEST_CYCLE;
    private Controller controller;
    private ControllerDao controllerDao;
    private FlockDao flockDao;
    private DataDao dataDao;
    private Flock flock;
    private MessageParser messageParser;
    private HashMap<Long, Data> onlineDatatable;
    private RequestMessage requestToSend;
    private RequestPriorityQueue requests;
    private PerHourReportRequestQueue hour_queue;
    private Logger logger;
    private boolean updatedFlag = true;
    private boolean requestCreated = false;
//    private Integer updated_gr_d_min;
    private boolean flag = false;

    public MessageManager(Controller controller) {
        this.controller = controller;
        this.requests = new RequestPriorityQueue(controller);
        this.controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
        this.flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
        this.dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        this.logger = Logger.getLogger(MessageManager.class);
    }

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

    private void createRequest() throws SQLException, ParseException {
        if (updatedFlag == false) {
            setRequestCreated(false);
        }
        if (graphsShouldBeRequested() && isSetClockInOnlineData()) {
            requestToSend = MessageFactory.createGraphRequest(controller.getNetName());//T901* 56\r
            setRequestCreated(true);
        } else if (dailyReportShouldBeRequested()) {
            try {
                requestToSend = createDailyReportRequest();//%T901h1 167\r
                setRequestCreated(true);
            } catch (IllegalAccessException ex) {
                controller.switchOff();
                recreateRequests();
                setRequestCreated(false);
            }
        } else if (perHourReportsShouldBeRequested()) {
            try {
                requestToSend = createPerHourReportsRequest();// D
                setRequestCreated(true);
            } catch (IllegalAccessException ex) {
//                controller.switchOff();
                recreateRequests();
                setRequestCreated(false);
            }
////        } else if (histogramShouldBeRequested()) {
////            //3.3. create histogram 24 hours
////            reqToSend = new RequestMessage(MessageType.REQUEST_HISTOGRAM, getNetName() , "A");
        } else {
            //4. create request for one of the next message types
            try {
                if (per_hour_request_cycle == 0){
                    per_hour_request_cycle = REQUEST_CYCLE;
                }
                checkAndFixFlock();
                requestToSend = requests.next();//a,b... f
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

    private boolean isAnyDataToChange() throws SQLException {
        Data data = dataDao.getChangedDataValue(controller.getId());
        if (data != null && messageParser != null) {
            messageParser.setDataToSend(data);
            return true;
        }
        return false;
    }

    private boolean graphsShouldBeRequested() throws SQLException {
        final Timestamp updateTime = controllerDao.getUpdatedGraphTime(controller.getId());
        if (updateTime == null) {
            return true;
        }
        final long timeSinceUpdated = System.currentTimeMillis() - updateTime.getTime();
        if (timeSinceUpdated > TimeUnit.HOURS.toMillis(1L)) {
            return true;
        }
        return false;
    }

    private boolean dailyReportShouldBeRequested() throws SQLException {
        flock = flockDao.getOpenFlockByController(controller.getId());

        if (flock != null) {
            if (per_day_request_cycle == 0 && (hour_queue == null || hour_queue.isCycleComplete())) {
                resetHistReqCycle();
                if (hour_queue != null) {
                    hour_queue.resetCycleFlag();
                }
                Data growDay = dataDao.getGrowDay(controller.getId());

                if (growDay == null || growDay.getValue() == 1 || growDay.getValue() == -1) {
                    return false;
                }

                Integer updatedGrowDayMax = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
                Integer updatedGrowDayMin = flockDao.getUpdatedGrowDayHistoryMin(flock.getFlockId());

                if (updatedGrowDayMax == null) {
                    return true;
                } else if (((growDay.getValue().intValue() - 1) - updatedGrowDayMax) > 0) {
                    return true;
                } else if (updatedGrowDayMin != 1) {
                    return true;
                }
            }
            decPerDayReportRequestCycle();
//        }
        }
        return false;
    }

    private boolean perHourReportsShouldBeRequested() throws SQLException {
        Collection<Data> data_list = dataDao.getPerHourHistoryDataByControllerValues(controller.getId());
        if (data_list.isEmpty()) {
            return false;
        }
            flock = flockDao.getOpenFlockByController(controller.getId());
        if (flock != null && !(controller.getName().equals("ECM12"))) {
            if (per_day_request_cycle == 0) {

                Data growDay = dataDao.getGrowDay(controller.getId());
                Integer grDay = growDay.getValue().intValue();
                Integer updated_gr_d_max = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());


                if (grDay != -1) {
                    if (growDay == null || growDay.getValue().intValue() == -1 || per_hour_request_cycle == 0) {
                        return false;
                    } else {
                        Integer updated_gr_d_min = flockDao.getUpdatedGrowDayHistory24Min(flock.getFlockId());
                        if (updated_gr_d_max == null) {
                            return true;
                        } else if (hour_queue != null && !hour_queue.count_size()){
                            return true;
                        } else if ((updated_gr_d_max + 1) != grDay) {
                            return true;
                        } else if (updated_gr_d_min == 0){
                            return false;
                        } else if (updated_gr_d_min > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void resetHistReqCycle() {
        per_day_request_cycle = REQUEST_CYCLE;
    }

    private void decPerDayReportRequestCycle() {
        if (per_day_request_cycle > 0) {
            per_day_request_cycle--;
        }
    }

    private void decPerHourReportRequestCycle() {
        if (per_hour_request_cycle > 0) {
            per_hour_request_cycle--;
        }
    }

//    private void resetPerHourReportRequestCycle() {
//        perHourReportRequestCycle = REQUEST_CYCLE;
//    }

//    private void resetPerHourReportRequestCycle() {
//        perHourReportRequestCycle = REQUEST_CYCLE;
//        hour_queue.set_count(0);
//    }

//    private void decPerHourReportRequestCycle() {
//        if (perHourReportRequestCycle > 0) {
//            perHourReportRequestCycle--;
//        }
//    }

    /**
     * Create management request for this controller.
     *
     * @return requestHistory the management request.
     * @throws SQLException
     */
    private RequestMessage createDailyReportRequest() throws SQLException, IllegalAccessException {
        RequestMessage retRequest = null;

        Integer growDay = dataDao.getGrowDay(controller.getId()).getValue().intValue();
        Integer growDayMax = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
        Integer growDayMin = flockDao.getUpdatedGrowDayHistoryMin(flock.getFlockId());

        if (growDayMax == null){
            growDayMax  = growDay - 1;
            retRequest = MessageFactory.createDailyReportRequests(controller.getNetName(), growDayMax);
        } else if (((growDay - 1) - growDayMax) > 0){
            retRequest = MessageFactory.createDailyReportRequests(controller.getNetName(), growDayMax + 1);
        } else if (growDayMin != 1){
            growDayMin -= 1;
            retRequest = MessageFactory.createDailyReportRequests(controller.getNetName(), growDayMin);
        }

        return retRequest;
    }

//    private RequestMessage createDailyReportRequest() throws SQLException, IllegalAccessException {
//                Integer growDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());// get online grow day
//        if (growDay == null) {
//            growDay = 1;
//        } else {
//            growDay += 1;// else groe day -- ad 1
//        }
//        return MessageFactory.createDailyReportRequests(controller.getNetName(), growDay);
//    }

    private RequestMessage createPerHourReportsRequest() throws SQLException, IllegalAccessException {
        RequestMessage retRequest = null;

        Integer grDay = dataDao.getGrowDay(controller.getId()).getValue().intValue();
        Integer updated_gr_d_max = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
        Integer updated_gr_d_min = flockDao.getUpdatedGrowDayHistory24Min(flock.getFlockId());

        if (hour_queue == null) {

            if (updated_gr_d_max == null) {
                hour_queue = new PerHourReportRequestQueue(controller, dataDao);// online gr day --
                flag = true;
            } else if (((grDay - 1) - updated_gr_d_max > 0)) {
                hour_queue = new PerHourReportRequestQueue(controller, false, updated_gr_d_max, flock, flockDao);
                flag = true;
            } else if (updated_gr_d_min != 0 ) {
                hour_queue = new PerHourReportRequestQueue(controller, true, updated_gr_d_min, flock, flockDao);
                flag = true;
            }
        } else if (hour_queue != null && hour_queue.count_size() && flag) {
            if (updated_gr_d_max == null) {
                hour_queue.recreateRequests_(controller, true, grDay - 1, flock, flockDao);
            } else if (((grDay - 1) - updated_gr_d_max > 0)) {
                hour_queue.recreateRequests_(controller, false, updated_gr_d_max, flock, flockDao);
            } else if (updated_gr_d_min != 0) {
                hour_queue.recreateRequests_(controller, true, updated_gr_d_min, flock, flockDao);
            }

        }

        decPerHourReportRequestCycle();

        retRequest = hour_queue.next_();
        hour_queue.dec_count();

        return retRequest;
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
        boolean exist = responseMessageMap.isContainRequest(requestToSend);// key: %T901a 111\r value: 4096 250 4097 201 ...

        if (exist) {
            final MessageType msgType = requestToSend.getMessageType(); //REQUEST_PANEL
            final ResponseMessage response = responses.get(requestToSend);//value: 4096 250 4097 201 ...
            Timestamp currTime = new Timestamp(System.currentTimeMillis());
            switch (msgType) {
                case REQUEST_TO_WRITE:
//                    messageParser.parseShortResponse(response, requestToSend);
                    messageParser.parseShortResponse(response, requestToSend, controller);
                    Long dataId = requestToSend.getDataId();
                    Long dataVal = requestToSend.getValue();
                    controllerDao.updateControllerData(controller.getId(), dataId, dataVal);
                    controllerDao.removeChangedValue(controller.getId(), dataId);
                    break;
                case REQUEST_CHANGED:
//                    messageParser.parseResponse(response, true);
                    messageParser.parseResponse(response, true, controller);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_PANEL:
//                    messageParser.parseResponse(response, false);
                    messageParser.parseResponse(response, true, controller);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_CHICK_SCALE:
//                    messageParser.parseResponse(response, false);
                    messageParser.parseResponse(response, true, controller);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_CONTROLLER:
//                    messageParser.parseResponse(response, false);
                    messageParser.parseResponse(response, true, controller);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_HISTOGRAM:
                    String datasetHistogram = new String(response.getBuffer(), 0, response.getBuffer().length);
                    controllerDao.updateControllerHistogram(controller.getId(), requestToSend.getPlate(), datasetHistogram, new Timestamp(System.currentTimeMillis()));
                    controller.setGraphUpdateTime(currTime.getTime());
                    break;

                case REQUEST_EGG_COUNT:
//                    messageParser.parseResponse(response, false);
                    messageParser.parseResponse(response, true, controller);
                    controllerDao.updateControllerData(controller.getId(), getUpdatedOnlineData().values());
                    break;

                case REQUEST_GRAPHS://*
                    controllerDao.updateControllerGraph(controller.getId(), response.toString(), currTime);
                    controller.setGraphUpdateTime(currTime.getTime());
                    break;

                case REQUEST_HISTORY://h
                    //parse and save parsed.
                    Integer growDay = requestToSend.getGrowDay();
                    if (response.toString().equals("Request Error")) {
                        flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, "-1");
                    } else {
                        // parse and save
                        flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, response.toString());
                        //fix start date

//                        if (controller.getName().startsWith("Image") && response.getFormat() == Message.Format.BINARY) {
//                            String fixedResponse = messageParser.parseHistoryIfComProtocolBinary(response);
//                            flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, fixedResponse);
//                        } else {
//                            flockDao.updateHistoryByGrowDay(flock.getFlockId(), growDay, response.toString());
//                        }
                    }
                    checkAndFixStartDate();
                    break;

                case REQUEST_PER_HOUR_REPORTS://D
                    Integer growDay24 = requestToSend.getGrowDay();
                    String dnum = requestToSend.getDnum();
                    if (response.toString().equals("Request Error")) {
//                        if (growDay24 == 1){
//                            flockDao.updateHistory24ByGrowDay(flock.getFlockId(), growDay24, dnum, "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ");
//                        }
                    } else {
                        if (response.toString().equals("0 0")){

                        } else if (controller.getName().startsWith("Image") && response.getFormat() == Message.Format.BINARY) {
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
//            if (requestToSend.getMessageType() == MessageType.REQUEST_HISTORY) {//
//                responseMessageMap.removeResponse(requestToSend);//
//            }//
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

    private void checkAndFixStartDate() throws SQLException {
        if (flock != null) {
            Data onlineGrowDay = dataDao.getGrowDay(controller.getId());
            Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            Date currentDay = getCurrentDay();

            if (onlineGrowDay != null &&
                    onlineGrowDay.getValue() != -1 &&
                    onlineGrowDay.getValue() > 1 &&
                    updatedGrowDay != null &&
                    flock != null &&
                    flock.getStatus().toLowerCase().equals("Open".toLowerCase())) {

                Date start_date_to_set = add_days_to_date(currentDay, -(onlineGrowDay.getValue().intValue() - 1));
                flock.setStartDate(sdf.format(start_date_to_set));
                flockDao.updateFlockStartDay(flock);

            }
        }
    }

    private void checkAndFixFlock() throws SQLException {
        if (flock != null) {
            Data onlineGrowDay = dataDao.getGrowDay(controller.getId());
            Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            Date currentDay = getCurrentDay();

            if (onlineGrowDay != null &&
                onlineGrowDay.getValue() != -1 &&
                updatedGrowDay!= null &&
                updatedGrowDay >= onlineGrowDay.getValue() &&
                flock != null &&
                flock.getStatus().toLowerCase().equals("Open".toLowerCase())) {

                Date start_date_to_set = add_days_to_date(currentDay, -(onlineGrowDay.getValue().intValue() - 1));
                Integer lastInt = flockDao.getUpdatedGrowDayHistory(flock.getFlockId());
                Date last = add_days_to_date(start_date_to_set, (lastInt - 1));
                flock.setStatus("Close");
                flock.setEndDate(sdf.format(last));
                flockDao.update(flock);

            }
        }
    }
}