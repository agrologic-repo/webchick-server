package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.SystemStateDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbySystemStateDaoImpl extends SystemStateDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbySystemStateDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "SYSTEMSTATENAMES", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "SYSTEMSTATEBYLANGUAGE", null);

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
    }

    private void createTableSystemState() throws SQLException {
        String sqlQuery = "CREATE TABLE SYSTEMSTATENAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (ID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new SYSTEMSTATEBYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.SYSTEMSTATENAMES ";
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
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.SYSTEMSTATENAMES ";
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



