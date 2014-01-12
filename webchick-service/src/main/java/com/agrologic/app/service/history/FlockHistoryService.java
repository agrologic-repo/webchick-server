package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.history.FromDayToDayParam;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FlockHistoryService {

    /**
     * Get flock by flock id
     *
     * @param flockId the flock id
     * @return flock the object that encapsulate flock data
     * @throws java.sql.SQLException if failed to get  flock from database
     */
    public Flock getFlock(long flockId) throws SQLException;

    /**
     * Get flock management data
     *
     * @param flockId the flock id
     * @return the map with management data per grow day
     * @throws java.sql.SQLException if failed to get  management data from database
     */
    public Map<Integer, String> getFlockHistory(long flockId) throws SQLException;

    /**
     * Get flock management data within range
     *
     * @param flockId           the flock id
     * @param fromDayToDayParam the range days
     * @return the map with management data per grow day
     * @throws java.sql.SQLException if failed to get  management data from database
     */
    public Map<Integer, String> getFlockHistoryWithinRange(long flockId, FromDayToDayParam fromDayToDayParam)
            throws SQLException;

    /**
     * Get flock 24 hour management data by grow day
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return list of 24 hour management data values
     * @throws java.sql.SQLException if failed to get management data from database
     */
    public List<String> getFlockHistory24Hour(long flockId, int growDay, Long langId) throws SQLException;

    /**
     * Get reset time for specified grow day management. Reset time used to set beginning of the coordinates for
     * the graphs
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return reset time
     * @throws java.sql.SQLException if failed to get management data from database
     */
    public Long getResetTime(long flockId, int growDay) throws SQLException;

    /**
     * Get per hour history data titles
     *
     * @return the data titles for per hour history data
     * @throws java.sql.SQLException if failed to get titles from database
     */
    public List<String> getPerHourHistoryDataTitles(Long flockId, int growDay, Long langId) throws SQLException;

    public Collection<Data> getFlockPerDayHistoryData(Long flockId, Long langId) throws SQLException;

    public Collection<Data> getFlockPerHourHistoryData(Long flockId, Integer growDay, Long langId) throws SQLException;

    public Map<Integer, String> parseHistory24(long resetTime, String values);
}
