
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.RelayDaoImpl;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyRelayDaoImpl extends RelayDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyRelayDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet        rs   = dbmd.getTables(null, "APP", "RELAYNAMES", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "RELAYBYLANGUAGE", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "PROGRAMRELAYS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table RELAYNAMES from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableRelay();
        createTableRelayByLang();
        createTableRelayByProgram();
    }

    private void createTableRelay() throws SQLException {
        String sqlQuery = "CREATE TABLE RELAYNAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                          + "PRIMARY KEY (ID))";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new RELAYNAMES Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableRelayByLang() throws SQLException {
        String sqlQuery = "CREATE TABLE RELAYBYLANGUAGE " + "(RELAYID INT NOT NULL , " + "LANGID INT NOT NULL , "
                          + "UNICODETEXT VARCHAR(200) NOT NULL, " + "PRIMARY KEY (RELAYID,LANGID))";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new RELAYBYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableRelayByProgram() throws SQLException {
        String sqlQuery = "CREATE TABLE PROGRAMRELAYS " + "(" + "DATAID INT NOT NULL , " + "BITNUMBER INT NOT NULL , "
                          + "TEXT VARCHAR(200) NOT NULL, " + "PROGRAMID INT NOT NULL , "
                          + "RELAYNUMBER INT NOT NULL , " + "RELAYTEXTID INT NOT NULL , "
                          + "PRIMARY KEY (DATAID,BITNUMBER,PROGRAMID)" + ")";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new PROGRAMRELAYS Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.RELAY ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table relay ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.RELAY ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table relay ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
