package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.RelayDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyRelayDaoImpl extends RelayDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyRelayDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "RELAYNAMES", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "RELAYBYLANGUAGE", null);

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
    }

    private void createTableRelay() throws SQLException {
        String sqlQuery = "CREATE TABLE RELAYNAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (ID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new RELAYBYLANGUAGE Table", e);
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
    public void deleteFromTable() throws SQLException {
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



