package com.agrologic.app.dao;

import com.agrologic.app.model.GasDto;



import java.sql.SQLException;

import java.util.List;

public interface GasDao {

    public void insert(GasDto gas) throws SQLException;

    public void remove(Long id) throws SQLException;

    public GasDto getById(Long id) throws SQLException;

    public List<GasDto> getAllByFlockId(Long flockId) throws SQLException;
}



