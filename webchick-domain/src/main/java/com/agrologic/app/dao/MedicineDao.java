package com.agrologic.app.dao;

import com.agrologic.app.model.Medicine;
import java.sql.SQLException;
import java.util.List;

public interface MedicineDao {

    public void insert(Medicine gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Medicine getById(Long id) throws SQLException;

    public List<Medicine> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}


