package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.FuelDaoImpl;
import com.agrologic.app.model.Fuel;



import java.sql.*;

public class DerbyFuelDaoImpl extends FuelDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFuelDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FUEL", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table FUEL from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE FUEL ( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL, "
                + "AMOUNT INT NOT NULL, "
                + "DATE VARCHAR(10) NOT NULL, "
                + "NUMBERACCOUNT INT NOT NULL, "
                + "PRICE FLOAT NOT NULL, "
                + "TOTAL FLOAT NOT NULL "
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new FUEL Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Fuel fuel) throws SQLException {
        String sqlQuery = "INSERT INTO FUEL (FLOCKID, AMOUNT, DATE, NUMBERACCOUNT, PRICE, TOTAL) "
                + "VALUES (?,?,?,?,?,?) " ;
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, fuel.getFlockId());
            prepstmt.setInt(2, fuel.getAmount());
            prepstmt.setString(3, fuel.getDate());
            prepstmt.setInt(4, fuel.getNumberAccount());
            prepstmt.setFloat(5, fuel.getPrice());
            prepstmt.setFloat(6, fuel.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Fuel To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.FUEL ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table fuel ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.FUEL ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table fuel ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



