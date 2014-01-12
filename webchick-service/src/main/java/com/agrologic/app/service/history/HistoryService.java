package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;

import java.sql.SQLException;
import java.util.List;

/**
 * History com.agrologic.app.service.history.HistoryService interface. This class contains method needed to manipulate with
 * history data.
 *
 * @author Valery Manakhimov
 */
public interface HistoryService {
    public String PER_DAY_HISTORY_STRING = "DAY";
    public String PER_HOUR_HISTORY_STRING = "HOUR";
    public String PER_DAY_AND_HOUR_HISTORY_STRING = "DAY;HOUR";

    /**
     * Get list of data that have been defined as historical data .
     *
     * @return the list of history data
     * @throws SQLException if failed to get data from database .
     */
    public List<Data> getHistoryData() throws SQLException;

    /**
     * Get list of data that have been defined as historical data per grow day .
     *
     * @return the list of history data per grow day
     * @throws SQLException if failed to get data from database .
     */
    public List<Data> getPerDayHistoryData(Long langId) throws SQLException;

    /**
     * Get list of data that have been defined as historical data per hour
     *
     * @return the list of history data per hour
     * @throws SQLException if failed to get data from database .
     */
    public List<Data> getPerHourHistoryData(Long langId) throws SQLException;

    /**
     * Get list of data that have been defined as historical data per hour and per day .
     *
     * @return the list of history data per hour adn per day
     * @throws SQLException if failed to get data from database .
     */
    public List<Data> getPerDayAndHourHistoryData(Long langId) throws SQLException;
}

