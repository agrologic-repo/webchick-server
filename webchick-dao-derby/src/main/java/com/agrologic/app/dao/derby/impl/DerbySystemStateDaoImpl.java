package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.SystemStateDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbySystemStateDaoImpl extends SystemStateDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbySystemStateDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "SYSTEMSTATENAMES", null);

            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "SYSTEMSTATEBYLANGUAGE", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table SYSTEMSTATENAMES or SYSTEMSTATEBYLANGUAGE from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableSystemState();
        createTableSystemStateByLang();
    }

    private void createTableSystemState() throws SQLException {
        String sql = "CREATE TABLE SYSTEMSTATENAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (ID))";
        jdbcTemplate.execute(sql);
    }

    private void createTableSystemStateByLang() throws SQLException {
        String sql = "CREATE TABLE SYSTEMSTATEBYLANGUAGE " + "(SYSTEMSTATEID INT NOT NULL , "
                + "LANGID INT NOT NULL , " + "UNICODENAME VARCHAR(500) NOT NULL, "
                + "PRIMARY KEY (SYSTEMSTATEID,LANGID))";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.SYSTEMSTATENAMES ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.SYSTEMSTATENAMES ";
        jdbcTemplate.execute(sql);
    }
}



