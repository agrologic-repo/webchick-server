package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Valery on 1/15/14.
 */
public interface ControllerHistoryService {
    /**
     * Return collection controller data  of specified controller that can be used for creation per hour
     * history request .
     *
     * @param controllerId the controller id
     * @return collection of data objects
     * @throws SQLException if failed
     */
    Collection<Data> getControllerPerHourHistoryData(Long controllerId) throws SQLException;
}
