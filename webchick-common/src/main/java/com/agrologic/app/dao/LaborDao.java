package com.agrologic.app.dao;

import com.agrologic.app.model.Labor;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the {@link  com.agrologic.app.model.Labor}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Labor} objects.
 *
 * @author Valery Manakhimov
 */

public interface LaborDao {
    /**
     * Add labor
     *
     * @param labor the labor
     * @throws java.sql.SQLException
     */
    public void insert(Labor labor) throws SQLException;

    /**
     * Remove labor
     *
     * @param id the id of labor
     * @throws java.sql.SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * Get labor from database
     *
     * @param id the id of labor
     * @return Labor
     * @throws java.sql.SQLException
     */
    public Labor getById(Long id) throws SQLException;

    /**
     * Retrieve all labels with given flock id
     *
     * @param flockId the flock id
     * @return List with labors
     * @throws java.sql.SQLException
     */
    public List<Labor> getAllByFlockId(Long flockId) throws SQLException;

    /**
     * Get currency by controller cellink id
     *
     * @param id the cellink id
     * @return String the currency
     * @throws java.sql.SQLException
     */
    public String getCurrencyById(Long id) throws SQLException;
}


