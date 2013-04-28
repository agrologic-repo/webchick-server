package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.SpreadDao;
import com.agrologic.app.model.Spread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpreadDaoImpl implements SpreadDao {

    protected DaoFactory dao;

    public SpreadDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    private Spread makeSpread(ResultSet rs) throws SQLException {
        Spread spread = new Spread();

        spread.setId(rs.getLong("ID"));
        spread.setFlockId(rs.getLong("FlockID"));
        spread.setAmount(rs.getInt("Amount"));
        spread.setDate(rs.getString("Date"));
        spread.setNumberAccount(rs.getInt("NumberAccount"));
        spread.setPrice(rs.getFloat("Price"));
        spread.setTotal(rs.getFloat("Total"));

        return spread;
    }

    private List<Spread> makeSpreadList(ResultSet rs) throws SQLException {
        List<Spread> spreadList = new ArrayList<Spread>();

        while (rs.next()) {
            spreadList.add(makeSpread(rs));
        }

        return spreadList;
    }

    @Override
    public void insert(Spread spread) throws SQLException {
        String sqlQuery = "INSERT INTO SPREAD (FLOCKID, AMOUNT, DATE, NUMBERACCOUNT, PRICE, TOTAL) "
                + "VALUES (?,?,?,?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, spread.getFlockId());
            prepstmt.setInt(2, spread.getAmount());
            prepstmt.setString(3, spread.getDate());
            prepstmt.setInt(4, spread.getNumberAccount());
            prepstmt.setFloat(5, spread.getPrice());
            prepstmt.setFloat(6, spread.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Spread To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from spread where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Delete Controller From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Spread getById(Long id) throws SQLException {
        String sqlQuery = "select * from spread where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeSpread(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Spread " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Spread> getAllByFlockId(Long flockId) throws SQLException {
        String sqlQuery = "select * from spread where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeSpreadList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Spread of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        String sqlQuery = "select * from currency where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Symbol");
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Spread " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
