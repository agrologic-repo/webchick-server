
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
public abstract class SchemaDao {

    /**
     * Create schema in database .
     *
     * @throws SQLException if failed to execute statement.
     */
    public abstract void createSchema(String schema) throws SQLException;

    /**
     * Drop schema in database .
     *
     * @throws SQLException if failed to execute statement.
     */
    public abstract void dropSchema(String schema) throws SQLException;
}


