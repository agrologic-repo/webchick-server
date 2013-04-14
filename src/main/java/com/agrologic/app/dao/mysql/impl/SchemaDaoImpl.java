
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.SchemaDao;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class SchemaDaoImpl extends SchemaDao {
    protected DaoFactory dao;

    public SchemaDaoImpl() {
        this(DaoType.MYSQL);
    }

    public SchemaDaoImpl(DaoType daoType) {
        this.dao = DaoFactory.getDaoFactory(daoType);
    }

    @Override
    public void createSchema(String schema) throws SQLException {
        String     sqlQuery = "CREATE SCHEMA " + schema + " ";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot create schema " + schema, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropSchema(String schema) throws SQLException {
        String            sqlQuery = "DROP SCHEMA ? RESTRICT";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, schema);
            prepstmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot drop schema " + schema, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
