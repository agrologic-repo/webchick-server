package com.agrologic.app.dao;

import com.agrologic.app.model.Labor;
import java.sql.SQLException;

import java.util.List;

public interface LaborDao {

    public void insert(Labor labor) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Labor getById(Long id) throws SQLException;

    public List<Labor> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}


