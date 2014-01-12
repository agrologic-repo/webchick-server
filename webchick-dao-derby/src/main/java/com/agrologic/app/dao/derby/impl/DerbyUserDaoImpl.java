package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.UserDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class DerbyUserDaoImpl extends UserDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyUserDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "USERS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table Users from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE USERS " + "(USERID INT NOT NULL , " + "NAME VARCHAR(45) NOT NULL, "
                + "PASSWORD VARCHAR(45) NOT NULL, " + "FIRSTNAME VARCHAR(45) NOT NULL, "
                + "LASTNAME VARCHAR(45) NOT NULL, " + "ROLE INTEGER NOT NULL, " + "STATE SMALLINT NOT NULL, "
                + "PHONE VARCHAR(45) NOT NULL, " + "EMAIL VARCHAR(45) NOT NULL, "
                + "COMPANY VARCHAR(45) NOT NULL, " + "PRIMARY KEY (USERID))";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.USERS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.USERS ";
        jdbcTemplate.execute(sql);
    }
}



