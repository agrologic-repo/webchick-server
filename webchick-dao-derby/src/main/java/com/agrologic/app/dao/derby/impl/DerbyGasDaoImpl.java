package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.GasDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyGasDaoImpl extends GasDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyGasDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "GAS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table GAS from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE GAS ( "
                + "ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT  NOT NULL, "
                + "AMOUNT INT  NOT NULL, "
                + "DATE VARCHAR(10) NOT NULL, "
                + "NUMBERACCOUNT INT NOT NULL, "
                + "PRICE FLOAT NOT NULL, "
                + "TOTAL FLOAT NOT NULL "
                + ")";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Gas gas) throws SQLException {
//        String sql = "INSERT INTO GAS (FLOCKID, AMOUNT, DATE, NUMBERACCOUNT, PRICE, TOTAL) "
//                + "VALUES (?,?,?,?,?,?) ";
//        jdbcTemplate.execute(sql);
//    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.GAS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.GAS ";
        jdbcTemplate.execute(sql);
    }
}



