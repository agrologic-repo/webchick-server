package com.agrologic.app.dao;


import com.agrologic.app.model.MedicineDto;



import java.sql.SQLException;

import java.util.List;

public interface MedicineDao {

    public void insert(MedicineDto gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public MedicineDto getById(Long id) throws SQLException;

    public List<MedicineDto> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}



