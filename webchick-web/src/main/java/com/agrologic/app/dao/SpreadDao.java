package com.agrologic.app.dao;

import com.agrologic.app.model.SpreadDto;



import java.sql.SQLException;

import java.util.List;

public interface SpreadDao {

    public void insert(SpreadDto gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public SpreadDto getById(Long id) throws SQLException;

    public List<SpreadDto> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}



