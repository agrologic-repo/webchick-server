package com.agrologic.app.dao;

import com.agrologic.app.model.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDao {
    /**
     * @param transaction
     * @throws java.sql.SQLException
     */
    public void insert(Transaction transaction) throws SQLException;

    /**
     * @param id
     * @throws java.sql.SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    public Transaction getById(Long id) throws SQLException;

    /**
     * @param flockId
     * @return
     * @throws java.sql.SQLException
     */
    public List<Transaction> getAllByFlockId(Long flockId) throws SQLException;

    /**
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    public String getCurrencyById(Long id) throws SQLException;
}


