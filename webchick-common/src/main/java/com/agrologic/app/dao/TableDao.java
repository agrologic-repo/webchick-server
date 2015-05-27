package com.agrologic.app.dao;

import com.agrologic.app.model.Table;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public interface TableDao {
    /**
     * show all screen tables the value 0
     */
    public static final int SHOW_ALL = 0;
    /**
     * show screen tables that not checked by user the value 1
     */
    public static final int SHOW_UNCHECKED = 1;
    /**
     * show screen tables that checked by user the value 2
     */
    public static final int SHOW_CHECKED = 2;
    /**
     * exception messages
     */
    public static final String CANNOT_INSERT_TABLE = "Cannot Insert ScreenTable From DataBase";
    public static final String CANNOT_UPDATE_TABLE = "Cannot Update ScreenTable From DataBase";
    public static final String CANNOT_DELETE_TABLE = "Cannot Delete ScreenTable From DataBase";
    public static final String CANNOT_RETRIEVE_TABLE = "Cannot Retrieve ScreenTable From DataBase";

    /**
     * Inserts a new table row to table table .
     *
     * @param table an objects that encapsulates an table attributes.
     * @throws java.sql.SQLException if failed to insert new table to the database.
     */
    void insert(Table table) throws SQLException;

    /**
     * Updates an existing table row in table table
     *
     * @param table an object that encapsulates a table attributes
     * @throws java.sql.SQLException if failed to update the table in the database
     */
    void update(Table table) throws SQLException;

    /**
     * Removes a table from the database
     *
     * @param programId the id of the program
     * @param screenId  the id of the screen
     * @param tableId   the id of the table
     * @throws java.sql.SQLException if failed to remove the table from the database
     */
    void remove(Long programId, Long screenId, Long tableId) throws SQLException;

    /**
     * Copy existing table
     *
     * @param table the table that was selected
     * @throws java.sql.SQLException if failed to copy table.
     */
    void copyTable(Table table) throws SQLException;

    /**
     * Copy tables from specified program to new specified program table .
     *
     * @param newProgramId the new program id
     * @param oldProgramId the old program id , the program id that was selected by user.
     * @throws java.sql.SQLException if failed to insert the table to the database.
     */
    void insertDefaultTables(Long oldProgramId, Long newProgramId) throws SQLException;

    /**
     * Insert translation of table in specified language.
     *
     * @param langId      the language id
     * @param translation the translated text for table
     * @throws java.sql.SQLException if failed to insert the translation
     */
    void insertTableTranslation(Long tableId, Long langId, String translation) throws SQLException;

    /**
     * Get the table from one screen and moves to another screen .
     *
     * @param table       the table
     * @param oldScreenId the old screen id
     * @throws java.sql.SQLException if failed to move table
     */
    void moveTable(Table table, Long oldScreenId) throws SQLException;

    /**
     * Save the changed position order of table in design tables page.
     *
     * @param showMap     the map of table ids and text yes\no for showing table on page
     * @param positionMap the map of table ids and position on page for each table
     * @param programId   the program id
     * @param screenId    the screen id
     * @throws java.sql.SQLException if failed to save the changes
     */
    void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long screenId, Long programId)
            throws SQLException;

    /**
     * Move table row position up .
     *
     * @param programId the program id
     * @param screenId the screen id
     * @param tableId the table id
     * @param position the position
     * @throws SQLException if failed to move table row position
     */
    void moveUp(Long programId,Long screenId,Long tableId,Integer position) throws SQLException;

    /**
     * Move table row position down .
     *
     * @param programId the program id
     * @param screenId the screen id
     * @param tableId the table id
     * @param position the position
     * @throws SQLException if failed to move table row position
     */
    void moveDown(Long programId,Long screenId,Long tableId,Integer position) throws SQLException;

    /**
     * Insert table list into screen tables
     *
     * @param tableList the table list
     * @throws java.sql.SQLException if failed to insert to the screen table .
     */
    void insert(Collection<Table> tableList) throws SQLException;

    /**
     * Insert table list translation to table by language table. This is a dictionary of data types .
     *
     * @param tableList the table list
     * @throws java.sql.SQLException if failed to insert to the table by language table
     */
    void insertTranslation(Collection<Table> tableList) throws SQLException;

    /**
     * Unchecked table on screen that not used in given program id
     *
     * @param programId the program id
     * @throws java.sql.SQLException if failed to execute the query
     */
    void uncheckNotUsedTableOnAllScreens(Long programId) throws SQLException;

    /**
     * Get table by specified programId, screenId, tableId .
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param tableId   the table d
     * @return Table object or null if not exist
     * @throws java.sql.SQLException if failed to get the table form database
     */
    Table getById(Long programId, Long screenId, Long tableId) throws SQLException;

    /**
     * Gets table that belong to specified screen and program with title by specified language.
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param tableId   the table id
     * @param langId    the language id
     * @return table an objects that encapsulates an table attributes
     * @throws java.sql.SQLException if failed to retrieve table from the database
     */
    Table getById(Long programId, Long screenId, Long tableId, Long langId) throws SQLException;

    /**
     * Retrieve all tables from the database
     *
     * @return the collection of tables
     * @throws java.sql.SQLException if failed to retrieve tables from the database
     */
    Collection<Table> getAll() throws SQLException;

    /**
     * Retrieve all tables from database in all available languages
     *
     * @return tables a collection of Table objects, each object reflects a row in table tables.
     * @throws java.sql.SQLException if failed to retrieve all tables from the database.
     */
    Collection<Table> getAllWithTranslation() throws SQLException;

    /**
     * Retrieves all tables in no special order with specified program id , screen id and show  flag .
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param showAll   the show all flag
     * @return tables a list of Table objects, each object reflects a row in table tables.
     * @throws java.sql.SQLException if failed to retrieve all tables from the database
     */
    Collection<Table> getScreenTables(Long programId, Long screenId, Boolean showAll) throws SQLException;

    /**
     * Retrieves all tables in no special order with specified program id , screen id and show on page flag
     * (true\false).
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param langId    the long id
     * @param showAll   the show all flag
     * @return tables a list of Table objects, each object reflects a row in table tables.
     * @throws java.sql.SQLException if failed to retrieve all tables from the database
     */
    Collection<Table> getScreenTables(Long programId, Long screenId, Long langId, Boolean showAll) throws SQLException;

}


