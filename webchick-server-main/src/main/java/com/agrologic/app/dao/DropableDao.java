/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public interface DropableDao {
    static final String CANNOT_DROP_TABLE  = "Cannot Drop Table";

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
