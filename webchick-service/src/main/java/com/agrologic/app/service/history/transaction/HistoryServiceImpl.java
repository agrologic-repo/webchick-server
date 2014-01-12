package com.agrologic.app.service.history.transaction;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Data;
import com.agrologic.app.service.history.HistoryService;

import java.sql.SQLException;
import java.util.List;

/**
 * History data service class, contains methods needed to manipulate with history data .
 * <p/>
 * Created by Valery on 12/30/13.
 */
public class HistoryServiceImpl implements HistoryService {

    private DataDao dataDao;

    public HistoryServiceImpl() {
        this.dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
    }

    /**
     * {@inheritDoc}
     */
    public List<Data> getHistoryData() throws SQLException {
        List<Data> dataList = (List<Data>) dataDao.getAllBySpecial(Data.HISTORY);
        return dataList;
    }

    /**
     * {@inheritDoc}
     */
    public List<Data> getPerDayHistoryData(Long langId) throws SQLException {
        List<Data> dataList = (List<Data>) dataDao.getHistoryDataListByCriteria(PER_DAY_HISTORY_STRING, langId);
        return dataList;
    }

    /**
     * {@inheritDoc}
     */
    public List<Data> getPerHourHistoryData(Long langId) throws SQLException {
        List<Data> dataList = (List<Data>) dataDao.getHistoryDataListByCriteria(PER_HOUR_HISTORY_STRING, langId);
        return dataList;
    }

    /**
     * {@inheritDoc}
     */
    public List<Data> getPerDayAndHourHistoryData(Long langId) throws SQLException {
        List<Data> dataList = (List<Data>) dataDao.getHistoryDataListByCriteria(PER_DAY_AND_HOUR_HISTORY_STRING, langId);
        return dataList;
    }
}
