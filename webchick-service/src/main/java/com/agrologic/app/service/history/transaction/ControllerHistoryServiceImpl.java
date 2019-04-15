package com.agrologic.app.service.history.transaction;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Data;
import com.agrologic.app.service.history.ControllerHistoryService;

import java.sql.SQLException;
import java.util.Collection;

public class ControllerHistoryServiceImpl implements ControllerHistoryService {
    private DataDao dataDao;

    public ControllerHistoryServiceImpl() {
        this.dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
    }

    @Override
    public Collection<Data> getControllerPerHourHistoryData(Long controllerId) throws SQLException {
        return dataDao.getPerHourHistoryDataByControllerValues(controllerId);


    }
}
