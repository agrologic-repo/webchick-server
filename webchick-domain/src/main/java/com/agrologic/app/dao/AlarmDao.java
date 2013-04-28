
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;


import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.ProgramAlarm;


import java.sql.SQLException;

import java.util.Collection;

/**
 * Title: AlarmDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public interface AlarmDao {

    /**
     * Insert a new alarm row to table alarms .
     *
     * @param alarm an objects that encapsulates an alarm attributes .
     * @throws SQLException if failed to insert new alarm to the database .
     */
    void insert(Alarm alarm) throws SQLException;

    /**
     * Insert alarm names
     *
     * @param alarmList the alarm list
     * @throws SQLException if failed to insert to the alarm table
     */
    void insert(Collection<Alarm> alarmList) throws SQLException;

    /**
     * Insert alarm translation row to alarm by language table. <br> This is a dictionary of alarm types .
     *
     * @param alarmId the id of alarms
     * @param langId the id of language
     * @param translation the translation text
     * @throws SQLException if failed to insert to the alarm by language table
     */
    void insertTranslation(Long alarmId, Long langId, String translation) throws SQLException;

    /**
     * Insert alarm list translation to alarm by language table. <br> This is a dictionary of alarm types .
     *
     * @param alarmList the alarm list
     * @throws SQLException if failed to insert to the alarm by language table
     */
    void insertTranslation(Collection<Alarm> alarmList) throws SQLException;

    /**
     * Insert program alarm list translation to program alarm by language table. <br> This is a dictionary of alarm
     * types .
     *
     * @param programAlarms list
     * @throws SQLException if failed to insert to the program alarm by language table
     */
    void insertProgramAlarms(Collection<ProgramAlarm> programAlarms) throws SQLException;

    /**
     * Updates an existing alarm row in table alarm
     *
     * @param alarm an object that encapsulates an alarm attributes
     * @throws SQLException if failed to update the alarm in the database
     */
    void update(Alarm alarm) throws SQLException;

    /**
     * Removes an alarm from the database
     *
     * @param id the id of the alarm to be removed from the database
     * @throws SQLException if failed to remove the alarm from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Get the alarm by id
     *
     * @param id the id of alarm object
     * @return alarm an objects that encapsulates an alarm attributes
     * @throws SQLException if failed to insert to the alarm by language table
     */
    Alarm getById(Long id) throws SQLException;

    /**
     * Retrieves all alarms
     *
     * @return alarms a Collection of Alarm objects, each object reflects a row in table alarm
     * @throws SQLException if failed to retrieve all alarm from the database
     */
    Collection<Alarm> getAll() throws SQLException;

    /**
     * Retrieves all alarms by language
     *
     * @return alarms a Collection of Alarm objects, each object reflects a row in table alarm
     * @throws SQLException if failed to retrieve all alarm from the database
     */
    Collection<Alarm> getAll(Long langId) throws SQLException;

    /**
     * Retrieves all alarms with translation
     *
     * @return alarm a Collection of Alarm objects, each object reflects a row in table alarm
     * @throws SQLException if failed to retrieve all alarm from the database
     */
    Collection<Alarm> getAllWithTranslation() throws SQLException;

    /**
     * Retrieves program alarms by program id.
     *
     * @param programId the program id
     * @return programAlarms a Collection of ProgramAlarm objects, each object reflects a row of program alarm
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    Collection<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException;

    /**
     * Retrieves program alarms by program id that was selected .
     *
     * @param programId the program id
     * @return programAlarms a Collection of ProgramAlarm objects, each object reflects a row of program alarm
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    Collection<ProgramAlarm> getSelectedProgramAlarms(Long programId) throws SQLException;

    /**
     * Retrieves selected program alarm by program id and language id.
     *
     * @param langId the language id
     * @param programId the program id
     * @return Collection of ProgramAlarm object , each object reflects a row in table programalarms
     * @throws SQLException if failed to retrieve all program relays from the database
     */
    Collection<ProgramAlarm> getSelectedProgramAlarms(Long programId, Long langId) throws SQLException;
}


