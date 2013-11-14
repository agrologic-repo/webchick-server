package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.AlarmDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyAlarmDaoImpl extends AlarmDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyAlarmDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
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
        logger.info("create table alarm names if not exist");
        String sql = "CREATE TABLE ALARMNAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, PRIMARY KEY (ID))";
        jdbcTemplate.execute(sql);
    }

    private void createTableAlarmByLang() throws SQLException {
        logger.info("create table alarm by language names if not exist");
        String sql = "CREATE TABLE ALARMBYLANGUAGE (ALARMID INT NOT NULL , LANGID INT NOT NULL , " +
                "UNICODENAME VARCHAR(200) NOT NULL, PRIMARY KEY (ALARMID,LANGID))";
        jdbcTemplate.execute(sql);

    }

    @Override
    public void dropTable() throws SQLException {
        logger.info("drop table alarm names");
        String sql = "DROP TABLE APP.ALARMNAMES ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        logger.info("delete all from table alarm names");
        String sql = "DELETE FROM APP.ALARMNAMES ";
        jdbcTemplate.execute(sql);
    }
}
