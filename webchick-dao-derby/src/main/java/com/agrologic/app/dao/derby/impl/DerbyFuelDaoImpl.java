package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.FuelDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyFuelDaoImpl extends FuelDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFuelDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public boolean tableExist() throws SQLException {


        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FUEL", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table FUEL from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE FUEL ( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL, "
                + "AMOUNT INT NOT NULL, "
                + "DATE VARCHAR(10) NOT NULL, "
                + "NUMBERACCOUNT INT NOT NULL, "
                + "PRICE FLOAT NOT NULL, "
                + "TOTAL FLOAT NOT NULL "
                + ")";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Fuel fuel) throws SQLException {
//        String sql = "INSERT INTO FUEL (FLOCKID, AMOUNT, DATE, NUMBERACCOUNT, PRICE, TOTAL) "
//                + "VALUES (?,?,?,?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setLong(1, fuel.getFlockId());
//            prepstmt.setInt(2, fuel.getAmount());
//            prepstmt.setString(3, fuel.getDate());
//            prepstmt.setInt(4, fuel.getNumberAccount());
//            prepstmt.setFloat(5, fuel.getPrice());
//            prepstmt.setFloat(6, fuel.getTotal());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert Fuel To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.FUEL ";
        jdbcTemplate.execute(sql);

    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.FUEL ";
        jdbcTemplate.execute(sql);
    }
}



