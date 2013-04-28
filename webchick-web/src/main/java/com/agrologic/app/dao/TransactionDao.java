package com.agrologic.app.dao;

import com.agrologic.app.model.TransactionDto;



import java.sql.SQLException;

import java.util.List;

public interface TransactionDao {

    public void insert(TransactionDto transaction) throws SQLException;

    public void remove(Long id) throws SQLException;

    public TransactionDto getById(Long id) throws SQLException;

    public List<TransactionDto> getAll() throws SQLException;

    public List<TransactionDto> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}



