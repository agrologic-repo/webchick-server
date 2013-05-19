package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.AlarmDaoImpl;
import com.agrologic.app.model.Alarm;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.Collection;

public class DerbyAlarmDaoImpl extends AlarmDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {


    public DerbyAlarmDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public void insert(Alarm alarm) {
        super.insert(alarm);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void insert(Collection<Alarm> alarmList) throws SQLException {
        super.insert(alarmList);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "ALARMNAMES", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "ALARMBYLANGUAGE", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "PROGRAMALARMS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table ALARMNAMES from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableAlarm();
        createTableAlarmByLang();
        createTableAlarmByProgram();
    }

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
            throw new SQLException("Cannot create new ALARMBYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableAlarmByProgram() throws SQLException {
        String sqlQuery = "CREATE TABLE PROGRAMALARMS " + "(" + "DATAID INT NOT NULL , "
                + "DIGITNUMBER INT NOT NULL , " + "TEXT VARCHAR(200) NOT NULL, "
                + "PROGRAMID INT NOT NULL , " + "ALARMNUMBER INT NOT NULL , " + "ALARMTEXTID INT NOT NULL , "
                + "PRIMARY KEY (DATAID,DIGITNUMBER,PROGRAMID)" + ")";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new ALARMBYPROGRAM Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.ALARM ";
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
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE FROM APP.ALARM ";
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



