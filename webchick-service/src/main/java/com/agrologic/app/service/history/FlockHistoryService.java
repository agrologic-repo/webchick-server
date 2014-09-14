package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.history.FromDayToDay;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
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
     * Get reset time for specified grow day management. Reset time used to set beginning of the coordinates for
     * the graph series
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return reset time
     * @throws SQLException if failed to get management data from database
     */
    public Long getResetTime(long flockId, int growDay) throws SQLException;

    /**
     * Get collection of {@link Data} that have been defined as per hour historical data . This history data was
     * received from controller that belongs to specified flock and grow day in specified language .
     *
     * @param flockId the ID of flock
     * @param growDay the grow day
     * @param langId  the ID of language
     * @return collection of data object
     * @throws SQLException if failed to get data from database .
     */
    public Collection<Data> getFlockPerHourReportsData(Long flockId, Integer growDay, Long langId) throws SQLException;

    /**
     * Get collection of strings that used for creating per hour report graphs. These titles are sorted in the same
     * manner in which these data are transmitted when requesting data charts with controllers .Getting reports titles
     * specified by flock , grow day and in specified language.
     *
     * @param flockId the ID of flock
     * @param growDay the grow day
     * @param langId  the ID of language
     * @return collection of titles
     * @throws java.sql.SQLException if failed to get strings from database
     */
    public Collection<String> getFlockPerHourReportsTitlesUsingGraphObjects(Long flockId, Integer growDay, Long langId)
            throws SQLException;

    /**
     * Get per day reports strings of specified flock. The reports string is not parsed. Result contains Integer keys,
     * that defined a grow day and String values that defined not parsed per grow day reports .
     *
     * @param flockId the flock id
     * @return the map with management data per grow day
     * @throws java.sql.SQLException if failed to get  management data from database
     */
    public Map<Integer, String> getFlockPerDayNotParsedReports(long flockId) throws SQLException;

    /**
     * Get per day reports strings of specified flock within range days . The reports string is not parsed.
     * Result contains Integer keys, that defined a grow day and values that defined not parsed per grow day reports .
     *
     * @param flockId      the flock id
     * @param fromDayToDay the range days
     * @return per day reports grouped in pairs : grow day and not parsed report string that was received from controller
     * @throws java.sql.SQLException if failed to get strings from database
     */
    public Map<Integer, String> getFlockPerDayNotParsedReportsWithinRange(long flockId, FromDayToDay fromDayToDay)
            throws SQLException;
}
