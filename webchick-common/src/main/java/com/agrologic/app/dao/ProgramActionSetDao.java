package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramActionSet;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * DAO for the {@link  com.agrologic.app.model.ActionSet}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.ActionSet} objects.
 *
 * @author Valery Manakhimov
 */

public interface ProgramActionSetDao {
    /**
     * Insert a new program action set row to table program action set .
     *
     * @param programActionSet an objects that encapsulates an action set attributes .
     * @throws java.sql.SQLException if failed to insert new relay to the database .
     */
    public void insert(ProgramActionSet programActionSet) throws SQLException;

    /**
     * Update program relay
     *
     * @param programActionSet the program  action set with new data
     * @throws java.sql.SQLException if failed to update the program  action set
     */
    public void update(ProgramActionSet programActionSet) throws SQLException;

    /**
     * Remove program relay with given program id,  data id and digit number .
     *
     * @param programId the program id
     * @param valueId   the value id
     * @throws java.sql.SQLException if failed to remove the program action set
     */
    public void remove(Long programId, Long valueId) throws SQLException;

    public void insertProgramActionSet(ProgramActionSet programActionSet) throws SQLException;

    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId)
            throws SQLException;

    public void saveChanges(Long programId, Long screenId, Map<Long, String> showDataMap, Map<Long, Integer> posDataMap)
            throws SQLException;

    public void insertProgramActionSetList(Collection<ProgramActionSet> programActionSets) throws SQLException;

    public void insertProgramActionSetList(Collection<ProgramActionSet> programActionSets, Long programId)
            throws SQLException;

    /**
     * Get the program action set by value id  and program id
     *
     * @param valueId   the id of action set object
     * @param programId the program id
     * @return ActionSet an objects that encapsulates an action set attributes
     * @throws java.sql.SQLException if failed to get to the action set by value
     */
    public ProgramActionSet getById(Long valueId, Long programId) throws SQLException;

    /**
     * Retrieves selected program action set by program id
     *
     * @param programId the program id
     * @return the list of program action set
     * @throws java.sql.SQLException if failed to retrieve all program action set from the database
     */
    public List<ProgramActionSet> getAllOnScreen(Long programId) throws SQLException;

    /**
     * Retrieves selected program action set by program id  and language id
     *
     * @param programId the program id
     * @param langId    the language id
     * @return the list of program action set
     * @throws java.sql.SQLException if failed to retrieve all program action set from the database
     */
    public List<ProgramActionSet> getAllOnScreen(Long programId, Long langId) throws SQLException;

    /**
     * Retrieves all program action set by program id
     *
     * @param programId the program id
     * @return the list of program action set
     * @throws java.sql.SQLException if failed to retrieve all program action set from the database
     */
    public List<ProgramActionSet> getAll(Long programId) throws SQLException;

    /**
     * Retrieves program action set by program id  and language id
     *
     * @param programId the program id
     * @param langId    the language id
     * @return the list of program action set
     * @throws java.sql.SQLException if failed to retrieve all program action set from the database
     */
    public List<ProgramActionSet> getAll(Long programId, Long langId) throws SQLException;
}



