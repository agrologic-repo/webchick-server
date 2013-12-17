package com.agrologic.app.dao;

import com.agrologic.app.model.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDao {
    /**
     * @param transaction
     * @throws SQLException
     */
    public void insert(Transaction transaction) throws SQLException;

    /**
     * @param id
     * @throws SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public Transaction getById(Long id) throws SQLException;

    /**
     * @param flockId
     * @return
     * @throws SQLException
     */
    public List<Transaction> getAllByFlockId(Long flockId) throws SQLException;

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public String getCurrencyById(Long id) throws SQLException;
}


