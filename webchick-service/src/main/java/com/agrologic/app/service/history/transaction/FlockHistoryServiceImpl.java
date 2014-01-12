package com.agrologic.app.service.history.transaction;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.history.FromDayToDayParam;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Valery on 12/30/13.
 */
public class FlockHistoryServiceImpl implements FlockHistoryService {
    private FlockDao flockDao;
    private HistoryService historyService;

    public FlockHistoryServiceImpl() {
        historyService = new HistoryServiceImpl();
        flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
    }

    /**
     * {@inheritDoc}
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
        return getFlockHistoryWithinRange(flockId, new FromDayToDayParam());
    }

    /**
     * Get flock management data within range
     *
     * @param flockId           the flock id
     * @param fromDayToDayParam the range days
     * @return the map with management data per grow day
     * @throws SQLException if failed to get  management data from database
     */
    public Map<Integer, String> getFlockHistoryWithinRange(long flockId, FromDayToDayParam fromDayToDayParam)
    throws SQLException {
        Map<Integer, String> historyByGrowDay;
        if (fromDayToDayParam.useRange()) {
            historyByGrowDay = flockDao.getAllHistoryByFlock(flockId,
                    fromDayToDayParam.getFromDay(),
                    fromDayToDayParam.getToDay());
        } else {
            historyByGrowDay = flockDao.getAllHistoryByFlock(flockId);
        }
        return historyByGrowDay;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getFlockHistory24Hour(long flockId, int growDay, Long langId) throws SQLException {
        List<String> flockHistoryList = new ArrayList<String>();
        List<Data> perHourHistoryDataList = getFlockPerHourHistoryData(flockId, growDay, langId);
        for (Data data : perHourHistoryDataList) {
            String historyValues = flockDao.getHistory24(flockId, growDay, data.getHistoryHourDNum());
            if (historyValues == null || historyValues.equals("-1") || historyValues.equals("")) {
                historyValues = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
            }
            flockHistoryList.add(historyValues);
        }
        return flockHistoryList;
    }

    /**
     * {@inheritDoc}
     */
    public Long getResetTime(long flockId, int growDay) throws SQLException {
        Integer resetTime = flockDao.getResetTime(flockId, growDay);
        if (resetTime != null) {
            return DataFormat.convertToTimeFormat(resetTime.longValue());
        } else {
            return 0L;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getPerHourHistoryDataTitles(Long flockId, int growDay, Long langId) throws SQLException {
        List<String> perHourHistoryDataTitles = new ArrayList<String>();
        List<Data> perHourHistoryDataList = getFlockPerHourHistoryData(flockId, growDay, langId);
        perHourHistoryDataTitles.add("Grow day " + growDay + "\n Hour(24)");
        for (Data data : perHourHistoryDataList) {
            perHourHistoryDataTitles.add(data.getLabel());
        }
        return perHourHistoryDataTitles;
    }

    @Override
    public List<Data> getFlockPerDayHistoryData(Long flockId, Long langId) throws SQLException {
        return (List<Data>) flockDao.getFlockPerDayHistoryData(flockId, langId);
    }

    @Override
    public List<Data> getFlockPerHourHistoryData(Long flockId, Integer growDay, Long langId) throws SQLException {
        return (List<Data>) flockDao.getFlockPerHourHistoryData(flockId, growDay, langId);
    }

    /**
     * {@inheritDoc}
     */
    public Map<Integer, String> parseHistory24(long resetTime, String values) {
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
