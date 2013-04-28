package com.agrologic.app.dao;

import com.agrologic.app.model.FuelDto;



import java.sql.SQLException;

import java.util.List;

public interface FuelDao {

    public void insert(FuelDto fuel) throws SQLException;

    public void remove(Long id) throws SQLException;

    public FuelDto getById(Long id) throws SQLException;

    public List<FuelDto> getAllByFlockId(Long flockId) throws SQLException;
}



