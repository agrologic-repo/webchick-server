
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.ProgramSystemState;
import com.agrologic.app.model.SystemState;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

import java.util.Collection;

/**
 * Title: SystemStateDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public interface SystemStateDao {

    /**
     * Insert a new systemState row to table systemStates .
     *
     * @param systemState an objects that encapsulates an systemState attributes .
     * @throws SQLException if failed to insert new systemState to the database .
     */
    void insert(SystemState systemState) throws SQLException;

    /**
     * Insert systemState names
     *
     * @param systemStateList the systemState list
     * @throws SQLException if failed to insert to the systemState table
     */
    void insert(Collection<SystemState> systemStateList) throws SQLException;

    /**
     * Insert systemState translation row to systemState by language table. <br> This is a dictionary of systemState
     * types .
     *
     * @param systemStateId the id of systemStates
     * @param langId the id of language
     * @param translation the translation text
     * @throws SQLException if failed to insert to the systemState by language table
     */
    void insertTranslation(Long screenId, Long langId, String translation) throws SQLException;

    /**
     * Insert systemState list translation to systemState by language table. <br> This is a dictionary of systemState
     * types .
     *
     * @param systemStateList the systemState list
     * @throws SQLException if failed to insert to the systemState by language table
     */
    void insertTranslation(Collection<SystemState> systemStateList) throws SQLException;

    void insertProgramSystemState(Collection<ProgramSystemState> programSystemStates) throws SQLException;

    /**
     * Updates an existing systemState row in table systemState
     *
     * @param systemState an object that encapsulates an systemState attributes
     * @throws SQLException if failed to update the systemState in the database
     */
    void update(SystemState systemState) throws SQLException;

    /**
     * Removes an systemState from the database
     *
     * @param id the id of the systemState to be removed from the database
     * @throws SQLException if failed to remove the systemState from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Get the systemState by id
     *
     * @param id the id of systemState object
     * @return systemState an objects that encapsulates an systemState attributes
     * @throws SQLException if failed to insert to the systemState by language table
     */
    SystemState getById(Long id) throws SQLException;

    /**
     * Retrieves all systemStates
     *
     * @return systemStates a vector of SystemState objects, each object reflects a row in table systemState
     * @throws SQLException if failed to retrieve all systemState from the database
     */
    Collection<SystemState> getAll() throws SQLException;

    /**
     * Retrieves all systemStates by language
     *
     * @return systemStates a vector of SystemState objects, each object reflects a row in table systemState
     * @throws SQLException if failed to retrieve all systemState from the database
     */
    Collection<SystemState> getAll(Long langId) throws SQLException;

    /**
     * Retrieves all system states with translation
     *
     * @return relays a Collection of SystemState objects, each object reflects a row in table system state
     * @throws SQLException if failed to retrieve all system state from the database
     */
    Collection<SystemState> getAllWithTranslation() throws SQLException;

    /**
     * Retrieves selected program system states by program id that was selected .
     *
     * @param programId the program id
     * @return Collection of ProgramSystemState object , each object reflects a row in table programsysstates
     * @throws SQLException if failed to retrieve all program system state from the database
     */
    Collection<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException;

    /**
     * Retrieves program alarms by program id that was selected .
     *
     * @param programId the program id
     * @return programAlarms a Collection of ProgramAlarm objects, each object reflects a row of program alarm
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    Collection<ProgramSystemState> getSelectedProgramSystemStates(Long programId) throws SQLException;

    /**
     * Retrieves selected program systemstate by program id and language id.
     *
     * @param langId the language id
     * @param programId the program id
     * @return Collection of ProgramSystemState object , each object reflects a row in table programsysstates
     * @throws SQLException if failed to retrieve all program system states from the database
     */
    Collection<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
