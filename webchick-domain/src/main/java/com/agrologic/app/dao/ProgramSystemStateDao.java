package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramSystemState;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * DAO for the {@link  com.agrologic.app.model.ProgramSystemState}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.ProgramSystemState} objects.
 *
 * @author Valery Manakhimov
 */
public interface ProgramSystemStateDao {
    /**
     * Insert a new programSystemState row to table systemStates .
     *
     * @param programSystemState an objects that encapsulates an systemState attributes .
     * @throws SQLException if failed to insert new systemState to the database .
     */
    public void insert(ProgramSystemState programSystemState) throws SQLException;

    /**
     * Updates an existing programSystemState row in table systemState
     *
     * @param programSystemState an object that encapsulates an systemState attributes
     * @throws SQLException if failed to update the programSystemState in the database
     */
    public void update(ProgramSystemState programSystemState) throws SQLException;

    /**
     * Removes an programSystemState from the database
     *
     * @param dataId    the data id
     * @param number    the system state number
     * @param programId the program id
     * @throws SQLException if failed to remove the programSystemState from the database
     */
    public void remove(Long dataId, Integer number, Long programId) throws SQLException;

    /**
     * Save program system states that was assigned by user to given program .
     *
     * @param programId      the program id
     * @param systemStateMap the map with program alarm id and selected bit numbers .
     * @throws SQLException
     */
    public void assignSystemStateToGivenProgram(Long programId, SortedMap<Long, Map<Integer, String>> systemStateMap) throws SQLException;

    /**
     * Insert program system states rows
     *
     * @param programSystemStates the list of program system states
     * @throws SQLException if failed to insert the program system state list to the database
     */
    void insertProgramSystemState(Collection<ProgramSystemState> programSystemStates) throws SQLException;

    /**
     * Retrieves program system states by program id.
     *
     * @param programId the program id
     * @return programAlarms a collection the list of program system states
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    public List<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException;

    /**
     * Retrieves program system states by program id.
     *
     * @param programId the program id
     * @return programAlarms a collection the list of program system states
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException;

    /**
     * Retrieves selected program alarm by program id and text
     *
     * @param programId the program id
     * @param text
     * @return the list of program system states
     * @throws SQLException if failed to retrieve all program alarm from the database
     */
    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, String[] text) throws SQLException;
}


