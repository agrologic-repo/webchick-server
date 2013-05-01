package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.GasDaoImpl;
import com.agrologic.app.model.Gas;
import java.sql.*;

public class DerbyGasDaoImpl extends GasDaoImpl implements CreatebleDao , DropableDao , RemovebleDao {

    public DerbyGasDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "GAS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table GAS from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE GAS ( "
                + "ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT  NOT NULL, "
                + "AMOUNT INT  NOT NULL, "
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
            throw new SQLException("Cannot create new Gas Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Gas gas) throws SQLException {
        String sqlQuery = "INSERT INTO GAS (FLOCKID, AMOUNT, DATE, NUMBERACCOUNT, PRICE, TOTAL) "
                + "VALUES (?,?,?,?,?,?) " ;
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, gas.getFlockId());
            prepstmt.setInt(2, gas.getAmount());
            prepstmt.setString(3, gas.getDate());
            prepstmt.setInt(4, gas.getNumberAccount());
            prepstmt.setFloat(5, gas.getPrice());
            prepstmt.setFloat(6, gas.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Gas To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.GAS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table gas ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.GAS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table gas ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



