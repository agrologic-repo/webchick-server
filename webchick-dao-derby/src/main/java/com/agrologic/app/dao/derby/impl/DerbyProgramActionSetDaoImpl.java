package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramActionSetDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyProgramActionSetDaoImpl extends ProgramActionSetDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramActionSetDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMACTIONSET", null);
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMACTIONSET from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableActionSetByProgram();
    }

    private void createTableActionSetByProgram() throws SQLException {
        String sql = "CREATE TABLE PROGRAMACTIONSET " + "(" + "DATAID INT NOT NULL , "
                + "VALUEID INT NOT NULL , "
                + "SCREENID INT NOT NULL , "
                + "PROGRAMID INT NOT NULL , "
                + "POSITION INT NOT NULL , "
                + "DISPLAYONPAGE  varchar(3) NOT NULL , "
                + "PRIMARY KEY (VALUEID,SCREENID,PROGRAMID)" + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.PROGRAMACTIONSET ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.PROGRAMACTIONSET ";
        jdbcTemplate.execute(sql);
    }
}



