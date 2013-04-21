
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public interface CreatebleDao {
    static final String CANNOT_EXECUTE_QUERY = "Cannot Execute Query";
    static final String CANNOT_CREATE_TABLE  = "Cannot Create New Table";

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
    abstract void createTable() throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
