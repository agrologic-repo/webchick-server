package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramSystemStateDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyProgramSystemStateDaoImpl extends ProgramSystemStateDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramSystemStateDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMSYSSTATES", null);
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMSYSSTATES from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableSystemStateByProgram();
    }

    private void createTableSystemStateByProgram() throws SQLException {
        String sql = "CREATE TABLE PROGRAMSYSSTATES " + "(" + "DATAID INT NOT NULL , " + "NUMBER INT NOT NULL , "
                + "TEXT VARCHAR(500) NOT NULL, " + "PROGRAMID INT NOT NULL , "
                + "SYSTEMSTATENUMBER INT NOT NULL , " + "SYSTEMSTATETEXTID INT NOT NULL , "
                + "PRIMARY KEY (DATAID,NUMBER,PROGRAMID)" + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.PROGRAMRELAYS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.PROGRAMRELAYS ";
        jdbcTemplate.execute(sql);
    }
}



