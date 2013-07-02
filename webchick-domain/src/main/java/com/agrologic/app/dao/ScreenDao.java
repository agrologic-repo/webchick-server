package com.agrologic.app.dao;

import com.agrologic.app.model.Screen;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public interface ScreenDao {
    static final String CANNOT_RETRIEVE_SCREENS_MESSAGE = "Cannot Retrieve Screens From Database\n";
    static final String CANNOT_INSERT_SCREEN_MESSAGE = "Cannot Insert Screen To DataBase\n";
    static final String CANNOT_UPDATE_SCREEN_MESSAGE = "Cannot Update Screen In DataBase\n";
    static final String CANNOT_DELETE_SCREEN_MESSAGE = "Cannot Delete Screen From DataBase\n";
    static final String TRANSACTION_ROLLED_BACK = "Transaction rolled back\n";

    /**
     * Inserts a new screen row to table screen .
     *
     * @param screen a screen object that encapsulate a screen attributes.
     * @throws SQLException if failed to insert new user to the database.
     */
    void insert(Screen screen) throws SQLException;

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
     * @param programId the program id
     * @param screenId  the old program id , the program id that was selected by user.
     * @throws SQLException
     */

    void remove(Long programId, Long screenId) throws SQLException;

    /**
     * Insert screen list into screen tables
     *
     * @param screenList the screen list
     * @throws SQLException if failed to insert to the screen table .
     */
    void insert(Collection<Screen> screenList) throws SQLException;

    /**
     * Insert screen list translation to screen by language table.
     * <p> This is a dictionary of data types .
     *
     * @param screenList the screen list
     * @throws SQLException if failed to insert to the screen by language table
     */
    void insertTranslation(Collection<Screen> screenList, Long langId) throws SQLException;

    /**
     * Copy screens from specified program to new specified program screen table.
     *
     * @param newProgramId the new program id
     * @param oldProgramId the old program id , the program id that was selected by user.
     * @throws SQLException if failed to insert the screens to the database.
     */
    void insertDefaultScreens(Long newProgramId, Long oldProgramId) throws SQLException;

    /**
     * Insert translation of screen in specified language.
     *
     * @param screenId    the screen id
     * @param langId      the language id
     * @param translation the translated text for screen
     * @throws SQLException if failed to insert the translation
     */
    void insertTranslation(Long screenId, Long langId, String translation) throws SQLException;

    /**
     * @param screen
     * @throws SQLException
     */
    void insertExistScreen(Screen screen) throws SQLException;

    /**
     * Save the changed position order of screen in design screens page.
     *
     * @param showMap     the map of screen ids and text yes\no for showing screen on page
     * @param positionMap the map of screen ids and position on page for each screen
     * @param programId   the program id
     * @throws SQLException if failed to save the changes
     */
    void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId) throws SQLException;

    /**
     * Return the next position for new screen.
     *
     * @param programId the program id
     * @return position the position for new screen
     * @throws SQLException if failed to get the position
     */
    int getNextScreenPosByProgramId(Long programId) throws SQLException;

    /**
     * Finding screen id of screen that was set as second screen after main screen.
     *
     * @param programId the program id
     * @return the screen id of second screen
     * @throws SQLException if failed to get the screen id
     */
    Long getSecondScreenAfterMain(Long programId) throws SQLException;

    /**
     * Get screen by specified program id and screen id .
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @return screen by program id
     * @throws SQLException if failed to get the screens by program id from the database.
     */
    Screen getById(Long programId, Long screenId) throws SQLException;

    /**
     * Get screen by specified program id and screen id and language id.
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param langId    the language id
     * @return screen
     * @throws SQLException if failed to get the screens by program and language from the database.
     */
    Screen getById(Long programId, Long screenId, Long langId) throws SQLException;

    /**
     * Retrieve all screens by specified program id , language id . ShowAll mean to get all screens or only screens
     * that was selected (with checked checkbox ) on design screen page.
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param langId    the language id
     * @param showAll   true if checked screens , false otherwise
     * @return
     * @throws SQLException
     */
    Screen getById(Long programId, Long screenId, Long langId, boolean showAll) throws SQLException;

    /**
     * @see ScreenDao#getAllProgramScreens(Long)
     */
    Collection<Screen> getAllByProgramId(Long programId) throws SQLException;

    /**
     * @see ScreenDao#getAllScreensByProgramAndLang(Long, Long, boolean)
     */
    Collection<Screen> getAllScreensByProgramAndLang(Long programId, Long langId, boolean showAll) throws SQLException;

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


