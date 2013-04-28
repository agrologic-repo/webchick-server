package com.agrologic.app.dao;

import com.agrologic.app.model.LaborDto;

import java.sql.SQLException;
import java.util.List;

public interface LaborDao {

    public void insert(LaborDto labor) throws SQLException;

    public void remove(Long id) throws SQLException;

    public LaborDto getById(Long id) throws SQLException;

    public List<LaborDto> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}



