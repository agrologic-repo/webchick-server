
package com.agrologic.app.dao;

import com.agrologic.app.model.Fuel;
import java.sql.SQLException;
import java.util.List;

public interface FuelDao {

    public void insert(Fuel fuel) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Fuel getById(Long id) throws SQLException;

    public List<Fuel> getAllByFlockId(Long flockId) throws SQLException;
}


