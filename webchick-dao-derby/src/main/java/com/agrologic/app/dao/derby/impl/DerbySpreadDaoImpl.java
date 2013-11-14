package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.SpreadDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbySpreadDaoImpl extends SpreadDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbySpreadDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "SPREAD", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table SPREAD from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE SPREAD "
                + "("
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL, "
                + "AMOUNT INT NOT NULL, "
                + "DATE VARCHAR(10) NOT NULL, "
                + "NUMBERACCOUNT INT NOT NULL, "
                + "PRICE DOUBLE NOT NULL, "
                + "TOTAL DOUBLE NOT NULL"
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.SPREAD ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.SPREAD ";
        jdbcTemplate.execute(sql);
    }
}



