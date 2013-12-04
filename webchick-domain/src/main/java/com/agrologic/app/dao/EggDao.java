package com.agrologic.app.dao;

import com.agrologic.app.model.Eggs;

import java.sql.SQLException;
import java.util.List;

public interface EggDao {
    /**
     * Insert Eggs use to the database
     *
     * @param eggs the eggs
     * @throws java.sql.SQLException
     */
    public void insert(Eggs eggs) throws SQLException;

    /**
     * Edit eggs
     *
     * @param eggs the eggs
     * @throws SQLException
     */
    public void update(Eggs eggs) throws SQLException;

    /**
     * Remove eggs from database
     *
     * @param flockId the flock Id
     * @param day     the day
     * @throws SQLException
     */
    public void remove(Long flockId, Integer day) throws SQLException;

    /**
     * Get eggs by id
     *
     * @param id the eggs id
     * @return Eggs the eggs
     * @throws SQLException
     */
    public Eggs getById(Long id) throws SQLException;

    /**
     * Get eggs by flock id
     *
     * @param flockId the flock id
     * @return list of eggs
     * @throws SQLException
     */
    public List<Eggs> getAllByFlockId(Long flockId) throws SQLException;
}
