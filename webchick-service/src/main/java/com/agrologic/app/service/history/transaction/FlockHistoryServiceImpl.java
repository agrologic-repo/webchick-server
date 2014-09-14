package com.agrologic.app.service.history.transaction;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.history.FromDayToDay;
import com.agrologic.app.service.history.FlockHistoryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Valery on 12/30/13.
 */
public class FlockHistoryServiceImpl implements FlockHistoryService {
    private FlockDao flockDao;

    public FlockHistoryServiceImpl() {
        this(DaoType.MYSQL);

    }

    public FlockHistoryServiceImpl(DaoType daoType) {
        flockDao = DbImplDecider.use(daoType).getDao(FlockDao.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flock getFlock(long flockId) throws SQLException {
        return flockDao.getById(flockId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
    public Collection<String> getFlockPerHourReportsTitlesUsingGraphObjects(Long flockId, Integer growDay, Long langId)
            throws SQLException {
        String defaultValues = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
        //Todo: should find more good solution for this hard coded
        Map<Integer, Long> tableForSortingValues = new TreeMap<Integer, Long>();
        tableForSortingValues.put(0, 3122L);
        tableForSortingValues.put(1, 3107L);
        tableForSortingValues.put(2, 3142L);
        tableForSortingValues.put(3, 1301L);
        tableForSortingValues.put(4, 1302L);

        Map<Integer, String> historyList = new TreeMap<Integer, String>();
        // put default values
        for (Map.Entry<Integer, Long> entry : tableForSortingValues.entrySet()) {
            historyList.put(entry.getKey(), defaultValues);

        }

        Collection<Data> perHourHistoryDataList = getFlockPerHourReportsData(flockId, growDay, langId);
        for (Data data : perHourHistoryDataList) {
            String values = flockDao.getHistory24(flockId, growDay, data.getHistoryHourDNum());
            if (values == null || values.equals("-1") || values.equals("")) {
                values = defaultValues;
            }
            //Todo: should find more good solution for this hard coded
            for (Map.Entry<Integer, Long> entry : tableForSortingValues.entrySet()) {
                if (data.getId().equals(entry.getValue())) {
                    historyList.put(entry.getKey(), values);
                }
            }
        }
        return new ArrayList<String>(historyList.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getFlockPerHourReportsData(Long flockId, Integer growDay, Long langId) throws SQLException {
        return flockDao.getFlockPerHourHistoryData(flockId, growDay, langId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> getFlockPerDayNotParsedReports(long flockId) throws SQLException {
        return getFlockPerDayNotParsedReportsWithinRange(flockId, new FromDayToDay());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> getFlockPerDayNotParsedReportsWithinRange(long flockId, FromDayToDay fromDayToDay)
            throws SQLException {
        Map<Integer, String> historyByGrowDay;
        if (fromDayToDay.useRange()) {
            historyByGrowDay = flockDao.getFlockPerDayNotParsedReports(flockId,
                    fromDayToDay.getFromDay(),
                    fromDayToDay.getToDay());
        } else {
            historyByGrowDay = flockDao.getFlockPerDayNotParsedReports(flockId);
        }
        return historyByGrowDay;
    }
}
