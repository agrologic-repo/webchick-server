/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.TransactionDao;
import com.agrologic.app.model.Transaction;

//~--- JDK imports ------------------------------------------------------------

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
public class TransactionDaoImpl implements TransactionDao {

    protected DaoFactory dao;

    public TransactionDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    private Transaction makeTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("ID"));
        transaction.setFlockId(rs.getLong("FlockID"));
        transaction.setName(rs.getString("Name"));
        transaction.setExpenses(rs.getFloat("Expenses"));
        transaction.setRevenues(rs.getFloat("Revenues"));
        return transaction;
    }

    private List<Transaction> makeTransactionList(ResultSet rs) throws SQLException {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        while (rs.next()) {
            transactionList.add(makeTransaction(rs));
        }
        return transactionList;
    }

    @Override
    public void insert(Transaction transaction) throws SQLException {
        String sqlQuery = "insert into transactions values (?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, transaction.getFlockId());
            prepstmt.setString(3, transaction.getName());
            prepstmt.setFloat(4, transaction.getExpenses());
            prepstmt.setFloat(5, transaction.getRevenues());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Transaction To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from transactions where ID=?";
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
    public Transaction getById(Long id) throws SQLException {
        String sqlQuery = "select * from transactions where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeTransaction(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Transaction " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Transaction> getAll() throws SQLException {
        String sqlQuery = "select * from transactions";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);

            ResultSet rs = prepstmt.executeQuery();

            return makeTransactionList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Transactions");
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

            throw new SQLException("Cannot Retrieve Transaction " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Transaction> getAllByFlockId(Long flockId) throws SQLException {
        String sqlQuery = "select * from transactions where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();
            return makeTransactionList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Transaction of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
