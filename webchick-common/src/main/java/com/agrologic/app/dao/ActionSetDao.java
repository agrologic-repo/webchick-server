package com.agrologic.app.dao;

import com.agrologic.app.model.ActionSet;

import java.sql.SQLException;
import java.util.Collection;

/**
 * DAO for the {@link  com.agrologic.app.model.ActionSet}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.ActionSet} objects.
 *
 * @author Valery Manakhimov
 */
public interface ActionSetDao {
    /**
     * Insert a new action set row to table action set .
     *
     * @param actionSet an objects that encapsulates an action set attributes .
     * @throws java.sql.SQLException if failed to insert new alarm to the database .
     */
    void insert(ActionSet actionSet) throws SQLException;

    /**
     * Updates an existing action set row in table action set
     *
     * @param actionSet an object that encapsulates an action set attributes
     * @throws java.sql.SQLException if failed to update the action set in the database
     */
    void update(ActionSet actionSet) throws SQLException;

    /**
     * Removes an actionSet from the database
     *
     * @param valueId the id of the actionSet to be removed from the database
     * @throws java.sql.SQLException if failed to remove the action set from the database
     */
    void remove(Long valueId) throws SQLException;

    /**
     * Add translation to action set button in specified language
     *
     * @param valueId   the value id
     * @param langId    the language id
     * @param translate the translation text
     * @throws SQLException if failed to insert to the action set by language table
     */
    void insertActionSetTranslation(Long valueId, Long langId, String translate) throws SQLException;

    /**
     * Insert the list of system state names
     *
     * @param actionSetsList the action set list
     * @throws java.sql.SQLException if failed to insert to the action set table
     */
    void insert(Collection<ActionSet> actionSetsList) throws SQLException;

    /**
     * Insert the list of action set with translation to action set by language table.
     * This is a dictionary of action set types .
     *
     * @param actionSetsList the action set list with translation to all languages that exist in system
     * @throws java.sql.SQLException if failed to insert to the action set by language table
     */
    void insertTranslation(Collection<ActionSet> actionSetsList) throws SQLException;

    /**
     * Get the action set by value id
     *
     * @param valueId the id of action set object
     * @return ActionSet an objects that encapsulates an action set attributes
     * @throws java.sql.SQLException if failed to get to the action set by value
     */
    ActionSet getById(Long valueId) throws SQLException;

    /**
     * Retrieves all action set
     *
     * @return Collection of ActionSet objects, each object reflects a row in table action set
     * @throws java.sql.SQLException if failed to retrieve all action set from the database
     */
    Collection<ActionSet> getAll() throws SQLException;

    /**
     * Retrieves all action set with translation
     *
     * @return Collection of action set objects, each object reflects a row in table action set
     * @throws java.sql.SQLException if failed to retrieve all action set from the database
     */
    Collection<ActionSet> getAllWithTranslation() throws SQLException;
}



