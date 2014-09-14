package com.agrologic.app.dao;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * DAO for the {@link  com.agrologic.app.model.Flock}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Flock} objects.
 *
 * @author Valery Manakhimov
 */
public interface FlockDao {

    /**
     * Insert a new alarm row to table alarms .
     *
     * @param flock an objects that encapsulates an flock attributes .
     * @throws java.sql.SQLException if failed to insert new data to the database .
     */
    void insert(Flock flock) throws SQLException;

    /**
     * Updates an existing flock row in table flocks
     *
     * @param flock an object that encapsulates a flock attributes
     * @throws java.sql.SQLException if failed to update the data in the database
     */
    void update(Flock flock) throws SQLException;

    /**
     * Removes a data from the flocks database
     *
     * @param flockId the id of the flock to be removed from the database
     * @throws java.sql.SQLException if failed to remove the data from the database
     */
    void remove(Long flockId) throws SQLException;

    void close(Long flockId, String endDate) throws SQLException;

    void updateFlockDetail(Flock flock) throws SQLException;

    void updateHistoryByGrowDay(Long flockId, Integer growDay, String values) throws SQLException;

    void updateHistory24ByGrowDay(Long flockId, Integer growDay, String dnum, String values) throws SQLException;

    void removeAllHistoryInFlockByGrowDay(Long flockId, Integer growDay) throws SQLException;

    void removeAllHistoryInFlock(Long flockId) throws SQLException;

    void removeAllHistoryOf24hourInFlock(Long flockId) throws SQLException;

    String getHistory24(Long flockId, Integer growDay, String dn) throws SQLException;

    Integer getResetTime(Long flockId, Integer growDay) throws SQLException;

    Integer getUpdatedGrowDayHistory(Long flockId) throws SQLException;

    Integer getUpdatedGrowDayHistory24(Long flockId) throws SQLException;

    Integer getFlockTotalFeedConsumption(Long flockId) throws SQLException;

    Flock getById(Long flockId) throws SQLException;

    Flock getOpenFlockByController(Long controllerId) throws SQLException;

    Collection<Flock> getAll() throws SQLException;

    Collection<Flock> getAllFlocksByController(Long controllerId) throws SQLException;

    Collection<Data> getFlockPerDayHistoryData(Long flockId) throws SQLException;

    Collection<Data> getFlockPerHourHistoryData(Long flockId, Integer growDay, Long langId) throws SQLException;

    Map<String, String> getHistoryN();

    Map<Integer, String> getFlockPerDayNotParsedReports(Long flockId) throws SQLException;

    Map<Integer, String> getFlockPerDayNotParsedReports(Long flockId, int fromDay, int toDay) throws SQLException;

    Map<Integer, String> getAllHistory24ByFlockAndDnum(Long flockId, String dnum) throws SQLException;
}
