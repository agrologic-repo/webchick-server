package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyProgramDaoImpl extends ProgramDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table programs from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE PROGRAMS "
                + "(PROGRAMID INT NOT NULL , "
                + "NAME VARCHAR(45) NOT NULL, "
                + "CREATED VARCHAR(45) NOT NULL, "
                + "MODIFIED VARCHAR(45) NOT NULL, "
                + "PRIMARY KEY (PROGRAMID))";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.PROGRAMS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE FROM APP.PROGRAMS ";
        jdbcTemplate.execute(sql);
    }
}

