package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramAlarmDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyProgramAlarmDaoImpl extends ProgramAlarmDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramAlarmDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
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
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMALARMS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMALARMS from DataBase", e);
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
        createTableProgramAlarm();
    }

    private void createTableProgramAlarm() throws SQLException {
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
            throw new SQLException("Cannot create new PROGRAMALARMS Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.PROGRAMALARMS ";
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
        String sqlQueryFlock = "DELETE FROM APP.PROGRAMALARMS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table program alarm ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}
