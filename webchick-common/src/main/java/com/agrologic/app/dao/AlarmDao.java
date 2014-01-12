package com.agrologic.app.dao;

import com.agrologic.app.model.Alarm;

import java.sql.SQLException;
import java.util.Collection;

/**
 * DAO for the {@link  com.agrologic.app.model.Alarm}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Alarm} objects.
 *
 * @author Valery Manakhimov
 */
public interface AlarmDao {
    /**
     * schema name
     */
    public final static String APP_SCHEMA = "APP";
    /**
     * the alarm names
     */
    public final static String ALARMNAMES_TABLE = "ALARMNAMES";
    /**
     * alarm by language
     */
    public final static String ALARMBYLANGUAGE_TABLE = "ALARMBYLANGUAGE";

    /**
     * Insert a new alarm row to table alarms .
     *
     * @param alarm an objects that encapsulates an alarm attributes .
     * @throws java.sql.SQLException if failed to insert new alarm to the database .
     */
    void insert(Alarm alarm) throws SQLException;

    /**
     * Updates an existing alarm row in table alarm
     *
     * @param alarm an object that encapsulates an alarm attributes
     * @throws java.sql.SQLException if failed to update the alarm in the database
     */
    void update(Alarm alarm) throws SQLException;

    /**
     * Removes an alarm from the database
     *
     * @param alarm the id of the alarm to be removed from the database
     * @throws java.sql.SQLException if failed to remove the alarm from the database
     */
    void remove(Alarm alarm) throws SQLException;

    /**
     * Insert alarm names
     *
     * @param alarmList the alarm list
     * @throws java.sql.SQLException if failed to insert to the alarm table
     */
    void insert(Collection<Alarm> alarmList) throws SQLException;

    /**
     * Insert alarm translation row to alarm by language table. <br> This is a dictionary of alarm types .
     *
     * @param alarmId     the id of alarms
     * @param langId      the id of language
     * @param translation the translation text
     * @throws java.sql.SQLException if failed to insert to the alarm by language table
     */
    void insertTranslation(Long alarmId, Long langId, String translation) throws SQLException;

    /**
     * Insert alarm list translation to alarm by language table. <br> This is a dictionary of alarm types .
     *
     * @param alarmList the alarm list
     * @throws java.sql.SQLException if failed to insert to the alarm by language table
     */
    void insertTranslation(Collection<Alarm> alarmList) throws SQLException;

    /**
     * Get the alarm by id
     *
     * @param id the id of alarm object
     * @return alarm an objects that encapsulates an alarm attributes
     * @throws java.sql.SQLException if failed to insert to the alarm by language table
     */
    Alarm getById(Long id) throws SQLException;

    /**
     * Retrieves all alarms
     *
     * @return alarms a Collection of Alarm objects, each object reflects a row in table alarm
     * @throws java.sql.SQLException if failed to retrieve all alarm from the database
     */
    Collection<Alarm> getAll() throws SQLException;

    /**
     * Retrieves all alarms by language
     *
     * @return alarms a Collection of Alarm objects, each object reflects a row in table alarm
     * @throws java.sql.SQLException if failed to retrieve all alarm from the database
     */
    Collection<Alarm> getAll(Long langId) throws SQLException;

    /**
     * Retrieves all alarms with translation
     *
     * @return alarm a Collection of Alarm objects, each object reflects a row in table alarm
     * @throws java.sql.SQLException if failed to retrieve all alarm from the database
     */
    Collection<Alarm> getAllWithTranslation() throws SQLException;
}


