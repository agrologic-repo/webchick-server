
package com.agrologic.app.dao;

import com.agrologic.app.model.Fuel;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the {@link  com.agrologic.app.model.Fuel}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Fuel} objects.
 *
 * @author Valery Manakhimov
 */
public interface FuelDao {
    /**
     * @param fuel
     * @throws SQLException
     */
    public void insert(Fuel fuel) throws SQLException;

    /**
     * @param id
     * @throws SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public Fuel getById(Long id) throws SQLException;

    /**
     * @param flockId
     * @return
     * @throws SQLException
     */
    public List<Fuel> getAllByFlockId(Long flockId) throws SQLException;
}


