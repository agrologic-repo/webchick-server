package com.agrologic.app.dao;

import java.sql.SQLException;

/**
 * DAO for objects that will remove all data from table
 *
 * @author Valery Manakhimov
 */

public interface RemovebleDao {
    static final String CANNOT_DELETE_DATA_FROM_TABLE = "Cannot Delete Rows From Table";

    /**
     * Check if table exist in database
     *
     * @return true if table exist , otherwise false.
     * @throws SQLException if failed to execute statement.
     */
    abstract boolean tableExist() throws SQLException;

    /**
     * Create table in database .
     *
     * @throws SQLException if failed to execute statement.
     */
    abstract void deleteFromTable() throws SQLException;
}
