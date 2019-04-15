package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.ProgramAlarmDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyProgramAlarmDaoImpl extends ProgramAlarmDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramAlarmDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMALARMS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMALARMS from DataBase", e);
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
        String sql = "CREATE TABLE PROGRAMALARMS " + "(" + "DATAID INT NOT NULL , "
                + "DIGITNUMBER INT NOT NULL , " + "TEXT VARCHAR(200) NOT NULL, "
                + "PROGRAMID INT NOT NULL , " + "ALARMNUMBER INT NOT NULL , " + "ALARMTEXTID INT NOT NULL , "
                + "PRIMARY KEY (DATAID,DIGITNUMBER,PROGRAMID)" + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.PROGRAMALARMS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE FROM APP.PROGRAMALARMS ";
        jdbcTemplate.execute(sql);
    }
}
