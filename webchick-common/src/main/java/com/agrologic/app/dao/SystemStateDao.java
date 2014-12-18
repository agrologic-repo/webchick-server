package com.agrologic.app.dao;

import com.agrologic.app.model.SystemState;

import java.sql.SQLException;
import java.util.Collection;

/**
 * DAO for the {@link  com.agrologic.app.model.SystemState}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.SystemState} objects.
 *
 * @author Valery Manakhimov
 */

public interface SystemStateDao {

    /**
     * Insert a new systemState row to table systemStates .
     *
     * @param systemState an objects that encapsulates an systemState attributes .
     * @throws java.sql.SQLException if failed to insert new systemState to the database .
     */
    void insert(SystemState systemState) throws SQLException;

    /**
     * Updates an existing systemState row in table systemState
     *
     * @param systemState an object that encapsulates an systemState attributes
     * @throws java.sql.SQLException if failed to update the systemState in the database
     */
    void update(SystemState systemState) throws SQLException;

    /**
     * Removes an systemState from the database
     *
     * @param id the id of the systemState to be removed from the database
     * @throws java.sql.SQLException if failed to remove the systemState from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Insert the list of system state names
     *
     * @param systemStateList the systemState list
     * @throws java.sql.SQLException if failed to insert to the system state names table
     */
    void insert(Collection<SystemState> systemStateList) throws SQLException;

    /**
     * Insert systemState translation row to system state by language table. This is a dictionary of systemState names.
     *
     * @param screenId    the screen id
     * @param langId      the id of language
     * @param translation the translation text
     * @throws java.sql.SQLException if failed to insert to the systemState by language table
     */
    void insertTranslation(Long screenId, Long langId, String translation) throws SQLException;

    /**
     * Insert the list of system sate with translation to systemState by language table.
     * This is a dictionary of systemState types .
     *
     * @param systemStateList the systemState list with translation to all languages that exist in system
     * @throws java.sql.SQLException if failed to insert to the systemState by language table
     */
    void insertTranslation(Collection<SystemState> systemStateList) throws SQLException;

    /**
     * Get the systemState by id
     *
     * @param id the id of systemState object
     * @return systemState an objects that encapsulates an systemState attributes
     * @throws java.sql.SQLException if failed to insert to the systemState by language table
     */
    SystemState getById(Long id) throws SQLException;

    /**
     * Retrieves all systemStates
     *
     * @return Collection of SystemState objects, each object reflects a row in table systemState
     * @throws java.sql.SQLException if failed to retrieve all systemState from the database
     */
    Collection<SystemState> getAll() throws SQLException;

    /**
     * Retrieves all systemStates by language
     *
     * @return Collection of SystemState objects, each object reflects a row in table systemState
     * @throws java.sql.SQLException if failed to retrieve all systemState from the database
     */
    Collection<SystemState> getAll(Long langId) throws SQLException;

    /**
     * Retrieves all system states with translation
     *
     * @return Collection of SystemState objects, each object reflects a row in table system state
     * @throws java.sql.SQLException if failed to retrieve all system state from the database
     */
    Collection<SystemState> getAllWithTranslation() throws SQLException;

    /**
     * Copy system states of selected program
     *
     * @param newProgramId the id of added program
     * @param selectedProgramId the id of selected program to get data data from it
     * @throws java.sql.SQLException if failed to execute the query
     */
    void copySystemStates(Long newProgramId, Long selectedProgramId);
}