
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.FuelDao;
import com.agrologic.app.model.FuelDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JanL
 */
public class FuelDaoImpl extends ConnectorDao implements FuelDao {
    private FuelDto makeFuel(ResultSet rs) throws SQLException {
        FuelDto fuel = new FuelDto();

        fuel.setId(rs.getLong("ID"));
        fuel.setFlockId(rs.getLong("FlockID"));
        fuel.setAmount(rs.getInt("Amount"));
        fuel.setDate(rs.getString("Date"));
        fuel.setNumberAccount(rs.getInt("NumberAccount"));
        fuel.setPrice(rs.getFloat("Price"));
        fuel.setTotal(rs.getFloat("Total"));

        return fuel;
    }

    private List<FuelDto> makeFuelList(ResultSet rs) throws SQLException {
        List<FuelDto> fuelList = new ArrayList<FuelDto>();

        while (rs.next()) {
            fuelList.add(makeFuel(rs));
        }

        return fuelList;
    }

    @Override
    public void insert(FuelDto fuel) throws SQLException {
        String            sqlQuery = "insert into fuel values (?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, fuel.getFlockId());
            prepstmt.setInt(3, fuel.getAmount());
            prepstmt.setString(4, fuel.getDate());
            prepstmt.setInt(5, fuel.getNumberAccount());
            prepstmt.setFloat(6, fuel.getPrice());
            prepstmt.setFloat(7, fuel.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Fuel To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from fuel where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Delete Controller From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public FuelDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from fuel where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeFuel(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Fuel " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<FuelDto> getAllByFlockId(Long flockId) throws SQLException {
        String            sqlQuery = "select * from fuel where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeFuelList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve All Fuel of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



