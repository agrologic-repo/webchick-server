package com.agrologic.app.messaging;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PerHourReportRequestQueue {
    private int size;
    private int count;
    private final String netname;
    private CyclicQueue<RequestMessage> requests;
    private Collection<Data> perHourReportDataList;
    private List<RequestMessage> activeRequests = new ArrayList<RequestMessage>();
    private CyclicQueue<RequestMessage> ac_req;
    private FlockDao flockDao;
    private DataDao dataDao;

    public PerHourReportRequestQueue(Controller controller, DataDao dataDao) throws SQLException { // queue null
        this.dataDao = dataDao;
        this.netname = controller.getNetName();
        this.perHourReportDataList = dataDao.getPerHourHistoryData();// all types with historyDopt HOUR
        this.size = perHourReportDataList.size();
        this.count = 0;
        Integer onlineGrowDay = (dataDao.getGrowDay(controller.getId()).getValue().intValue());
        ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), (onlineGrowDay - 1), perHourReportDataList);
    }

//    public PerHourReportRequestQueue(Controller controller,  Integer updated_gr_d_max, Flock flock, FlockDao flockDao) throws SQLException {
//        this.flockDao = flockDao;
//        this.netname = controller.getNetName();
//        Integer maxUptGrowDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
//        this.perHourReportDataList = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), maxUptGrowDay, 1L);
//        this.size = perHourReportDataList.size();
//        this.count = 0;
//        ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), updated_gr_d_max - 1, perHourReportDataList);
//    }

    public PerHourReportRequestQueue(Controller controller,  Boolean max, Integer updated_gr_d, Flock flock, FlockDao flockDao) throws SQLException {
        this.flockDao = flockDao;
        this.netname = controller.getNetName();

        if (max) {// true, updated_gr_d_min
            int num = flockDao.getNamberOfRowsFromHist24(flock.getFlockId(), updated_gr_d);
            int num2 = flockDao.getNamberOfRowsFromHist24(flock.getFlockId(), updated_gr_d + 1);
            if (num < num2){
                updated_gr_d = updated_gr_d + 1;
            }
            this.perHourReportDataList = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), updated_gr_d, 1L);
            this.size = perHourReportDataList.size();
            this.count = 0;
            ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), updated_gr_d - 1, perHourReportDataList);
        }else { // false, updated_gr_d_max
            int num = flockDao.getNamberOfRowsFromHist24(flock.getFlockId(), updated_gr_d);
            int num2 = flockDao.getNamberOfRowsFromHist24(flock.getFlockId(), updated_gr_d - 1);
            if (num < num2){
                updated_gr_d = updated_gr_d - 1;
            }
            this.perHourReportDataList = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), updated_gr_d, 1L);
            this.size = perHourReportDataList.size();
            this.count = 0;
            ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), updated_gr_d + 1, perHourReportDataList);
        }
    }

    public void recreateRequests_(Controller controller, Boolean max, Integer updated_gr_d, Flock flock, FlockDao flockDao) throws SQLException {
        this.flockDao = flockDao;
        this.activeRequests.clear();
        this.perHourReportDataList.clear();
        Integer maxUptGrowDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
        this.perHourReportDataList = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), maxUptGrowDay, 1L);
        this.size = perHourReportDataList.size();
        this.count = 0;
        if (max){
            ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), updated_gr_d - 1, perHourReportDataList);
        } else {
            ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), updated_gr_d + 1, perHourReportDataList);
        }
    }

    //get number of rows updated_gr_d - 2 and number of rows updated_gr_d - 1 if it is != create with update_gr_d

//    public void recreateRequests_(Controller controller, Flock flock, FlockDao flockDao, Integer updated_gr_d_min) throws SQLException {
//        this.flockDao = flockDao;
//        this.activeRequests.clear();
//        this.perHourReportDataList.clear();
//        Integer maxUptGrowDay = flockDao.getUpdatedGrowDayHistory24(flock.getFlockId());
//        this.perHourReportDataList = flockDao.getFlockPerHourHistoryData(flock.getFlockId(), maxUptGrowDay, 1L);
//        this.size = perHourReportDataList.size();
//        this.count = 0;
//        ac_req = MessageFactory.createPerHourHistoryRequests_(controller.getNetName(), updated_gr_d_min - 1, perHourReportDataList);
//    }

    public final RequestMessage next() throws IllegalAccessException {
        return requests.nextElem();
    }

    public final RequestMessage next_() throws IllegalAccessException {
        return ac_req.nextElem();
    }

    public int getSize() {
        return size;
    }

    public boolean isCycleComplete() {
        return ac_req.isCycleCompleted();
    }

    public void resetCycleFlag() {
//        requests.resetCycleFlag();
        ac_req.resetCycleFlag();
    }

    public boolean count_size(){
        if (count == size)
            return true;
        else
            return false;
    }

    public void dec_count(){
        this.count++;
    }

}

