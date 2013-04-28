
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.SpreadDao;
import com.agrologic.app.model.SpreadDto;



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
public class SpreadDaoImpl extends ConnectorDao implements SpreadDao {
    private SpreadDto makeSpread(ResultSet rs) throws SQLException {
        SpreadDto spread = new SpreadDto();

        spread.setId(rs.getLong("ID"));
        spread.setFlockId(rs.getLong("FlockID"));
        spread.setAmount(rs.getInt("Amount"));
        spread.setDate(rs.getString("Date"));
        spread.setNumberAccount(rs.getInt("NumberAccount"));
        spread.setPrice(rs.getFloat("Price"));
        spread.setTotal(rs.getFloat("Total"));

        return spread;
    }

    private List<SpreadDto> makeSpreadList(ResultSet rs) throws SQLException {
        List<SpreadDto> spreadList = new ArrayList<SpreadDto>();

        while (rs.next()) {
            spreadList.add(makeSpread(rs));
        }

        return spreadList;
    }

    @Override
    public void insert(SpreadDto spread) throws SQLException {
        String            sqlQuery = "insert into spread values (?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, spread.getFlockId());
            prepstmt.setInt(3, spread.getAmount());
            prepstmt.setString(4, spread.getDate());
            prepstmt.setInt(5, spread.getNumberAccount());
            prepstmt.setFloat(6, spread.getPrice());
            prepstmt.setFloat(7, spread.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Spread To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from spread where ID=?";
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
    public SpreadDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from spread where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeSpread(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Spread " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<SpreadDto> getAllByFlockId(Long flockId) throws SQLException {
        String            sqlQuery = "select * from spread where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeSpreadList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve All Spread of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        String            sqlQuery = "select * from currency where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Symbol");
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Spread " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



