
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;


import com.agrologic.app.model.Screen;


import java.sql.SQLException;

import java.util.Collection;

/**
 *
 * @author JanL
 */
public interface ScreenDao {
    static final String CANNOT_RETRIEVE_SCREENS_MESSAGE = "Cannot Retrieve Screens From Database\n";
    static final String CANNOT_INSERT_SCREEN_MESSAGE    = "Cannot Insert Screen To DataBase\n";
    static final String CANNOT_UPDATE_SCREEN_MESSAGE    = "Cannot Update Screen In DataBase\n";
    static final String CANNOT_DELETE_SCREEN_MESSAGE    = "Cannot Delete Screen From DataBase\n";
    static final String TRANSACTION_ROLLED_BACK         = "Transaction rolled back\n";

    /**
     * Inserts a new screen row to table screen .
     *
     * @param screen a screen object that encapsulate a screen attributes.
     * @throws SQLException if failed to insert new user to the database.
     */
    void insert(Screen screen) throws SQLException;

    /**
     * Insert screen list into screen tables
     *
     * @param screenList the screen list
     * @throws SQLException if failed to insert to the screen table .
     */
    void insert(Collection<Screen> screenList) throws SQLException;

    /**
     * Insert screen list translation to screen by language table. <br> This is a dictionary of data types .
     *
     * @param screenList the screen list
     * @throws SQLException if failed to insert to the screen by language table
     */
    void insertTranslation(Collection<Screen> screenList, Long langId) throws SQLException;

    /**
     * Updates an existing screen row in table screen
     *
     * @param screen an object that encapsulates a screen attributes
     * @throws SQLException if failed to update the user in the database
     */
    void update(Screen screen) throws SQLException;

    /**
     * Removes a screen from the database
     *
     * @param id the id of the screen to be removed from the database
     * @throws SQLException if failed to remove the screen from the database
     */
    /**
     * Removes a screen from the database
     *
     * @param programId the program id
     * @param screenId  the old program id , the program id that was selected by user.
     * @throws SQLException
     */

    void remove(Long programId, Long screenId) throws SQLException;

    /**
     * Insert into screen table all screens from old program to new program .
     *
     * @param newProgramId the new program id
     * @param oldProgramId the old program id , the program id that was selected by user.
     * @throws SQLException if failed to insert the screens from the database.
     */
    void insertDefaultScreens(Long newProgramId, Long oldProgramId) throws SQLException;

    /**
     * Get screen by specified program id and screen id .
     *
     * @param programId the program id
     * @param screenId the screen id
     * @return screen by program id
     * @throws SQLException if failed to get the screens by program id from the database.
     */
    Screen getById(Long programId, Long screenId) throws SQLException;

    /**
     * Get screen by specified program id and screen id and language id.
     *
     * @param programId the program id
     * @param screenId the screen id
     * @param langId the language id
     * @return screen
     * @throws SQLException if failed to get the screens by program and language from the database.
     */
    Screen getById(Long programId, Long screenId, Long langId) throws SQLException;

    /**
     * Retrieve all screens from database.
     *
     * @return collection with screen objects
     * @throws SQLException if failed to retrieve the screens from the database.
     */
    Collection<Screen> getAll() throws SQLException;

    /**
     * Retrieve all screens from database by program id.
     *
     * @param programId the specified program id
     * @return collection with screen objects selected by program id
     * @throws SQLException if failed to retrieve the screens from the database.
     */
    Collection<Screen> getAllProgramScreens(Long programId) throws SQLException;

    /**
     * Retrieve all screens from database by program id and language id.
     *
     * @param programId the specified program id
     * @param langId    the specified language id
     * @return collection with screen objects selected by program id and language id
     * @throws SQLException if failed to retrieve the screens from the database.
     */
    Collection<Screen> getAllProgramScreens(Long programId, Long langId) throws SQLException;
}


