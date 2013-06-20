package com.agrologic.app.dao;

import com.agrologic.app.model.Gas;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the {@link  com.agrologic.app.model.Gas}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Gas} objects.
 *
 * @author Valery Manakhimov
 */
public interface GasDao {
    /**
     * Insert Gas use to the database
     *
     * @param gas the gas
     * @throws SQLException
     */
    public void insert(Gas gas) throws SQLException;

    /**
     * Remove gas from database
     *
     * @param id the gas id
     * @throws SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * Get gas by id
     *
     * @param id the gas id
     * @return Gas the gas
     * @throws SQLException
     */
    public Gas getById(Long id) throws SQLException;

    /**
     * Get gas by flock id
     *
     * @param flockId the flock id
     * @return list of gas
     * @throws SQLException
     */
    public List<Gas> getAllByFlockId(Long flockId) throws SQLException;
}


