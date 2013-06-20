package com.agrologic.app.dao;

import java.sql.SQLException;

/**
 * DAO for objects that will drop tables
 *
 * @author Valery Manakhimov
 */
public interface DropableDao {
    static final String CANNOT_DROP_TABLE = "Cannot Drop Table";

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
    abstract void dropTable() throws SQLException;
}
