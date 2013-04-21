
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.Transaction;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface TransactionDao {

    public void insert(Transaction transaction) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Transaction getById(Long id) throws SQLException;

    public List<Transaction> getAll() throws SQLException;

    public List<Transaction> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
