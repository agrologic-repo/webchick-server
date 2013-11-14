package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramRelayDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyProgramRelayDaoImpl extends ProgramRelayDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramRelayDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMRELAYS", null);
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMRELAYS from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableRelayByProgram();
    }

    private void createTableRelayByProgram() throws SQLException {
        String sql = "CREATE TABLE PROGRAMRELAYS " + "(" + "DATAID INT NOT NULL , " + "BITNUMBER INT NOT NULL , "
                + "TEXT VARCHAR(200) NOT NULL, " + "PROGRAMID INT NOT NULL , "
                + "RELAYNUMBER INT NOT NULL , " + "RELAYTEXTID INT NOT NULL , "
                + "PRIMARY KEY (DATAID,BITNUMBER,PROGRAMID)" + ")";
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



