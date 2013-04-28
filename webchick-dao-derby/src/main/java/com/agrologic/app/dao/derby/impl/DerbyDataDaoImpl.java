
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.DataDaoImpl;
import java.sql.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyDataDaoImpl extends DataDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {


    public DerbyDataDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "DATATABLE", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table DATATABLE from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createDataTable();
        createTableDataByLang();
        createTableData();
        createSpecialDataLabel();
    }

    public void createDataTable() throws SQLException {
        String sqlQuery = "CREATE TABLE DATATABLE " + "(DATAID INT NOT NULL , " + "TYPE INT NOT NULL , "
                + "STATUS SMALLINT NOT NULL, " + "READONLY SMALLINT NOT NULL, "
                + "TITLE VARCHAR(100) NOT NULL, " + "FORMAT INT NOT NULL, " + "LABEL VARCHAR(100) NOT NULL, "
                + "ISRELAY SMALLINT NOT NULL, " + "ISSPECIAL INT NOT NULL, " + "PRIMARY KEY (DATAID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new DATATABLE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createTableData() throws SQLException {
        String sqlQuery = "CREATE TABLE TABLEDATA " + "(DATAID INT NOT NULL , " + "TABLEID INT NOT NULL , "
                + "SCREENID INT NOT NULL, " + "PROGRAMID INT NOT NULL, "
                + "DISPLAYONTABLE VARCHAR(10) NOT NULL, " + "POSITION INT NOT NULL, "
                + "PRIMARY KEY (DATAID, TABLEID,SCREENID, PROGRAMID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new DATATABLE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createTableDataByLang() throws SQLException {
        String sqlQuery = "CREATE TABLE DATABYLANGUAGE " + "(DATAID INT NOT NULL , " + " LANGID INT NOT NULL , "
                + "UNICODELABEL VARCHAR(500) NOT NULL, "
                + "CONSTRAINT DTBYLANG_PK PRIMARY KEY (DATAID, LANGID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new DATABYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createSpecialDataLabel() throws SQLException {
        String sqlQuery = "CREATE TABLE SPECIALDATALABELS " + "(DATAID INT NOT NULL , " + "PROGRAMID INT NOT NULL , "
                + "LANGID INT NOT NULL , " + "SPECIALLABEL VARCHAR(500) NOT NULL, "
                + "PRIMARY KEY (DATAID,PROGRAMID,LANGID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new DATABYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.DATATABLE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table datatable ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.DATATABLE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table datatable ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



