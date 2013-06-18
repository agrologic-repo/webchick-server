package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramAlarm;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProgramAlarmDao {
    /**
     * Insert program alarm
     *
     * @param programAlarm the program alarm
     * @throws SQLException if failed to insert to the program alarms
     */
    public void insert(ProgramAlarm programAlarm) throws SQLException;

    /**
     * Update program alarm
     *
     * @param programAlarm the program alarm with new data
     * @throws SQLException if failed to update the program alarms
     */
    public void update(ProgramAlarm programAlarm) throws SQLException;

    /**
     * Remove program alarm with given program id,  data id and digit number .
     *
     * @param dataId      the data id of program alarm
     * @param digitNumber the digit number
     * @param programId   the program id
     * @throws SQLException if failed to remove the program alarms
     */
    public void remove(Long dataId, Integer digitNumber, Long programId) throws SQLException;

    /**
     * Save program alarms that was assigned by user to given program .
     *
     * @param programId the program id
     * @param alarmMap  the map with program alarm id and selected bit numbers .
     * @throws SQLException
     */
    public void assignAlarmsToGivenProgram(Long programId, Map<Long, Map<Integer, String>> alarmMap) throws SQLException;

    /**
     * Insert program alarms collection
     *
     * @param programAlarms the list of program alarms
     * @throws SQLException if failed to insert to the program alarms collection
     */
    public void insert(Collection<ProgramAlarm> programAlarms) throws SQLException;

    /**
     * Retrieves program alarms by program id.
     *
     * @param programId the program id
     * @return programAlarms a collection the list of program alarms
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    public List<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException;

    /**
     * Retrieves selected program alarm by program id and language id.
     *
     * @param programId the program id
     * @param langId    the language id
     * @return Collection of ProgramAlarm object , each object reflects a row in table program alarms
     * @throws SQLException
     */
    public List<ProgramAlarm> getAllProgramAlarms(Long programId, Long langId) throws SQLException;

    /**
     * Retrieves selected program alarm by program id and language id.
     *
     * @param programId
     * @param text
     * @return
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    public List<ProgramAlarm> getAllProgramAlarms(Long programId, String[] text) throws SQLException;
}


