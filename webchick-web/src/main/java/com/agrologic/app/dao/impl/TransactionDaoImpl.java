
/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.TransactionDao;
import com.agrologic.app.model.TransactionDto;



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
public class TransactionDaoImpl extends ConnectorDao implements TransactionDao {
    private TransactionDto makeTransaction(ResultSet rs) throws SQLException {
        TransactionDto transaction = new TransactionDto();

        transaction.setId(rs.getLong("ID"));
        transaction.setFlockId(rs.getLong("FlockID"));
        transaction.setName(rs.getString("Name"));
        transaction.setExpenses(rs.getFloat("Expenses"));
        transaction.setRevenues(rs.getFloat("Revenues"));

        return transaction;
    }

    private List<TransactionDto> makeTransactionList(ResultSet rs) throws SQLException {
        List<TransactionDto> transactionList = new ArrayList<TransactionDto>();

        while (rs.next()) {
            transactionList.add(makeTransaction(rs));
        }

        return transactionList;
    }

    @Override
    public void insert(TransactionDto transaction) throws SQLException {
        String            sqlQuery = "insert into transaction values (?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, transaction.getFlockId());
            prepstmt.setString(3, transaction.getName());
            prepstmt.setFloat(4, transaction.getExpenses());
            prepstmt.setFloat(5, transaction.getRevenues());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Transaction To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from transaction where ID=?";
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
    public TransactionDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from transaction where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeTransaction(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Transaction " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TransactionDto> getAll() throws SQLException {
        String            sqlQuery = "select * from transaction";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);

            ResultSet rs = prepstmt.executeQuery();

            return makeTransactionList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve All Transactions");
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

            throw new SQLException("Cannot Retrieve Transaction " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TransactionDto> getAllByFlockId(Long flockId) throws SQLException {
        String            sqlQuery = "select * from transaction where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeTransactionList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve All Transaction of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



