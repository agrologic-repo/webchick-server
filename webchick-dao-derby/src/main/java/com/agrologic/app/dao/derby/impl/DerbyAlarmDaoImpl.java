package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.AlarmDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyAlarmDaoImpl extends AlarmDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {
    public final static String APP_SCHEMA = "APP";
    public final static String ALARMNAMES_TABLE = "ALARMNAMES";
    public final static String ALARMBYLANGUAGE_TABLE = "ALARMBYLANGUAGE";

    public DerbyAlarmDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, APP_SCHEMA, ALARMNAMES_TABLE, null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, APP_SCHEMA, ALARMBYLANGUAGE_TABLE, null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table " + ALARMNAMES_TABLE + " from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void createTable() throws SQLException {
        createTableAlarm();
        createTableAlarmByLang();
    }

    /**
     * {@inheritDoc }
     */
    private void createTableAlarm() throws SQLException {
        String sqlQuery = "CREATE TABLE ALARMNAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (ID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new ALARMNAMES Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableAlarmByLang() throws SQLException {
        String sqlQuery = "CREATE TABLE ALARMBYLANGUAGE " + "(ALARMID INT NOT NULL , " + "LANGID INT NOT NULL , "
                + "UNICODENAME VARCHAR(200) NOT NULL, " + "PRIMARY KEY (ALARMID,LANGID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new " + ALARMBYLANGUAGE_TABLE + " Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.ALARMNAMES ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table alarm ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE FROM APP.ALARMNAMES ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table alarm ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}
