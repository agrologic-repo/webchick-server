
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Table;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Title: ITableDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
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
    /*
     * exception messages
     */
    public static final String CANNOT_INSERT_TABLE = "Cannot Insert ScreenTable From DataBase";
    public static final String CANNOT_UPDATE_TABLE = "Cannot Update ScreenTable From DataBase";
    public static final String CANNOT_DELETE_TABLE = "Cannot Delete ScreenTable From DataBase";
    public static final String CANNOT_RETREIEVE_TABLE = "Cannot Retrieve ScreenTable From DataBase";

    /**
     * Inserts a new table row to table table .
     *
     * @param table an objects that encapsulates an table attributes.
     * @throws SQLException if failed to insert new table to the database.
     */
    void insert(Table table) throws SQLException;

    /**
     * Insert table list into screen tables
     *
     * @param tableList the table list
     * @throws SQLException if failed to insert to the screen table .
     */
    void insert(Collection<Table> tableList) throws SQLException;

    /**
     * Insert table list translation to table by language table. This is a dictionary of data types .
     *
     * @param tableList the table list
     * @throws SQLException if failed to insert to the table by language table
     */
    void insertTranslation(Collection<Table> tableList) throws SQLException;

    /**
     * Updates an existing table row in table table
     *
     * @param table an object that encapsulates a table attributes
     * @throws SQLException if failed to update the table in the database
     */
    void update(Table table) throws SQLException;

    /**
     * Removes a table from the database
     *
     * @param programId  the id of the program
     * @param screenId   the id of the screen
     * @param tableId    the id of the table
     * @throws SQLException if failed to remove the table from the database
     */
    void remove(Long programId, Long screenId, Long tableId) throws SQLException;

    /**
     * @see TableDao#getTableById(Long programId, Long screenId, Long tableId, Long langId) ()
     */
    Table getById(Long programId, Long screenId, Long tableId, Long langId) throws SQLException;

    /**
     * Gets table that belong to specified screen and program with title by specified language.
     *
     * @param programId the program id
     * @param screenId the screen id
     * @param tableId the table id
     * @param langId the language id
     * @return table an objects that encapsulates an table attributes
     * @throws SQLException if failed to retrieve table from the database
     */
    Table getTableById(Long programId, Long screenId, Long tableId, Long langId) throws SQLException;

    /**
     * Retrieve all tables from database in all available languages
     *
     * @return tables a collection of Table objects, each object reflects a row in table tables.
     * @throws SQLException if failed to retrieve all tables from the database.
     */
    Collection<Table> getAllWithTranslation() throws SQLException;

    /**
     * Retrieves all tables by specified program.
     *
     * @param programId the program id
     * @param screenId the screen id
     * @param langId the language id
     * @return tables a collection of Table objects, each object reflects a row in table tables.
     * @throws SQLException if failed to retrieve all tables from the database.
     */
    Collection<Table> getAllScreenTables(Long programId, Long screenId, Long langId) throws SQLException;

    /**
     * Retrieves all tables in no special order .
     *
     * @return tables a vector of User objects, each object reflects a row in table tables.
     * @throws SQLException if failed to retrieve all tables from the database
     */
    Collection<Table> getAllScreenTables(Long programId, Long screenId, Long langId, int showAll) throws SQLException;
}


