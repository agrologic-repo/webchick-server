package com.agrologic.app.dao;

import java.sql.SQLException;

public interface RemovebleDao {
    static final String CANNOT_REMOVE_TABLE  = "Cannot Remove Table";

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
    abstract void removeFromTable() throws SQLException;
}
