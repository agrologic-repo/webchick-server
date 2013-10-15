package com.agrologic.app.service;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.management.PerHourHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.Flock;
import com.agrologic.app.web.GrowDayRangeParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FlockHistoryService {

    private FlockDao flockDao;
    private DataDao dataDao;

    public FlockHistoryService() {
        dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
    }

    /**
     * Get flock by flock id
     *
     * @param flockId the flock id
     * @return flock the object that encapsulate flock data
     * @throws SQLException if failed to get  flock from database
     */
    public Flock getFlock(long flockId) throws SQLException {
        return flockDao.getById(flockId);
    }

    /**
     * Get flock management data
     *
     * @param flockId the flock id
     * @return the map with management data per grow day
     * @throws SQLException if failed to get  management data from database
     */
    public Map<Integer, String> getFlockHistory(long flockId) throws SQLException {
        return getFlockHistoryWithinRange(flockId, new GrowDayRangeParam());
    }

    /**
     * Get flock management data within range
     *
     * @param flockId           the flock id
     * @param growDayRangeParam the range days
     * @return the map with management data per grow day
     * @throws SQLException if failed to get  management data from database
     */
    public Map<Integer, String> getFlockHistoryWithinRange(long flockId, GrowDayRangeParam growDayRangeParam)
            throws SQLException {
        Map<Integer, String> historyByGrowDay;
        if (growDayRangeParam.useRange()) {
            historyByGrowDay = flockDao.getAllHistoryByFlock(flockId,
                    growDayRangeParam.getFromDay(),
                    growDayRangeParam.getToDay());
        } else {
            historyByGrowDay = flockDao.getAllHistoryByFlock(flockId);
        }
        return historyByGrowDay;
    }

    /**
     * Get flock 24 hour management data by grow day
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return list of 24 hour management data values
     * @throws SQLException if failed to get management data from database
     */
    public List<String> getFlockHistory24Hour(long flockId, int growDay) throws SQLException {
        List<String> flockHistoryList = new ArrayList<String>();
        for (PerHourHistoryDataType perHourHistoryDataType : PerHourHistoryDataType.values()) {
            String historyValues = flockDao.getHistory24(flockId, growDay, perHourHistoryDataType.getName());
            if (historyValues == null || historyValues.equals("-1 ") || historyValues.equals("")) {
                historyValues = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
            }
            flockHistoryList.add(historyValues);
        }
        return flockHistoryList;
    }

    /**
     * Get reset time for specified grow day management. Reset time used to set beginning of the coordinates for
     * the graphs
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return reset time
     * @throws SQLException if failed to get management data from database
     */
    public Long getResetTime(long flockId, int growDay) throws SQLException {
        Long resetTime = flockDao.getResetTime(flockId, growDay).longValue();
        if (resetTime != null) {
            resetTime = DataFormat.convertToTimeFormat(resetTime);
        } else {
            resetTime = Long.valueOf("0");
        }
        return resetTime;
    }

    /**
     * Get per hour history data titles
     *
     * @return the data titles for per hour history data
     * @throws SQLException if failed to get titles from database
     */
    public List<String> getPerHourHistoryDataTitles(int growDay) throws SQLException {
        List<String> perHourHistoryDataTitles = new ArrayList<String>();
        perHourHistoryDataTitles.add("Grow day " + growDay + "\n Hour(24)");
        for (PerHourHistoryDataType perHourHistoryDataType : PerHourHistoryDataType.values()) {
            String title = flockDao.getDNHistory24(perHourHistoryDataType.getName());
            perHourHistoryDataTitles.add(title);
        }
        return perHourHistoryDataTitles;
    }

    /**
     * Get per grow day history data titles
     *
     * @return the data titles for per hour history data
     * @throws SQLException if failed to get titles from database
     */
    public List<String> getPerGrowDayHistoryDataTitles() throws SQLException {
        List<String> perGrowDayHistoryDataTitles = new ArrayList<String>();
        for (PerGrowDayHistoryDataType perGrowDayHistoryDataType : PerGrowDayHistoryDataType.values()) {
            Data data = dataDao.getById(perGrowDayHistoryDataType.getId(), 1L);
            String title = data.getLabel();
            perGrowDayHistoryDataTitles.add(title);
        }
        return perGrowDayHistoryDataTitles;

    }

    private Map<Integer, String> parseHistory24(long resetTime, String values) {
        String[] valueList = values.split(" ");
        Map<Integer, String> valuesMap = new TreeMap<Integer, String>();
        int j = (int) resetTime / 100;
        for (int i = 0; i < 24; i++) {
            if (j == 24) {
                j = 0;
            }
            valuesMap.put(j++, valueList[i]);
        }
        return valuesMap;
    }
}
