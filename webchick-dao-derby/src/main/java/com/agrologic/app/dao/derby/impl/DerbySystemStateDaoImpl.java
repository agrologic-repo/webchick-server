package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.SystemStateDaoImpl;
import java.sql.*;

public class DerbySystemStateDaoImpl extends SystemStateDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbySystemStateDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet        rs   = dbmd.getTables(null, "APP", "SYSTEMSTATENAMES", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "SYSTEMSTATEBYLANGUAGE", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "PROGRAMSYSSTATES", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table SYSTEMSTATENAMES from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableSystemState();
        createTableSystemStateByLang();
        createTableSystemStateByProgram();
    }

    private void createTableSystemState() throws SQLException {
        String sqlQuery = "CREATE TABLE SYSTEMSTATENAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                          + "PRIMARY KEY (ID))";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new SYSTEMSTATENAMES Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableSystemStateByLang() throws SQLException {
        String sqlQuery = "CREATE TABLE SYSTEMSTATEBYLANGUAGE " + "(SYSTEMSTATEID INT NOT NULL , "
                          + "LANGID INT NOT NULL , " + "UNICODENAME VARCHAR(200) NOT NULL, "
                          + "PRIMARY KEY (SYSTEMSTATEID,LANGID))";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new SYSTEMSTATEBYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableSystemStateByProgram() throws SQLException {
        String sqlQuery = "CREATE TABLE PROGRAMSYSSTATES " + "(" + "DATAID INT NOT NULL , " + "NUMBER INT NOT NULL , "
                          + "TEXT VARCHAR(200) NOT NULL, " + "PROGRAMID INT NOT NULL , "
                          + "SYSTEMSTATENUMBER INT NOT NULL , " + "SYSTEMSTATETEXTID INT NOT NULL , "
                          + "PRIMARY KEY (DATAID,NUMBER,PROGRAMID)" + ")";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new SYSTEMSTATESBYPROGRAM Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.SYSTEMSTATE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table table ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.SYSTEMSTATE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table systemstate ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



