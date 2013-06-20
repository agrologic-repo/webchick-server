package com.agrologic.app.dao;

import com.agrologic.app.model.Distrib;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the {@link  com.agrologic.app.model.Distrib}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Distrib} objects.
 *
 * @author Valery Manakhimov
 */
public interface DistribDao {
    /**
     * Insert a new distrib row to table distrib .
     *
     * @param distrib an objects that encapsulates an alarm attributes .
     * @throws SQLException if failed to insert new distrib to the database .
     */
    public void insert(Distrib distrib) throws SQLException;

    /**
     * Remove distrib form database
     *
     * @param id the distrib id
     * @throws SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * Retrieve distrib by given id
     *
     * @param id the given distrib id
     * @return Distrib
     * @throws SQLException
     */
    public Distrib getById(Long id) throws SQLException;

    /**
     * Retrieve distrib with given flock id
     *
     * @param flockId the flock id
     * @return List of distrib
     * @throws SQLException
     */
    public List<Distrib> getAllByFlockId(Long flockId) throws SQLException;
}


