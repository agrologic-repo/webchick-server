package com.agrologic.app.dao;

import com.agrologic.app.model.Worker;

import java.sql.SQLException;
import java.util.List;

public interface WorkerDao {
    /**
     * Inserts a new worker row to table worker .
     *
     * @param worker an objects that encapsulates an user attributes.
     * @throws java.sql.SQLException if failed to insert new worker to the database.
     */
    public void insert(Worker worker) throws SQLException;

    /**
     * Removes a worker from the database
     *
     * @param id the worker id
     * @throws java.sql.SQLException if failed to remove the worker from the database
     */
    public void remove(Long id) throws SQLException;

    /**
     * Removes a worker from the database
     *
     * @param id the id of the worker to be removed from the database
     * @throws java.sql.SQLException if failed to retrieve the worker from the database
     */
    public Worker getById(Long id) throws SQLException;

    /**
     * Retrieves all workers by cellink id
     *
     * @param cellinkId the cellink id
     * @return workers a vector of Worker objects, each object reflects a row in table worker
     * @throws java.sql.SQLException if failed to retrieve all workers from the database
     */
    public List<Worker> getAllByCellinkId(Long cellinkId) throws SQLException;

    /**
     * Return currency string
     *
     * @param id the worker id
     * @return currency currency string
     * @throws java.sql.SQLException if failed to retrieve the currency from the database
     */
    public String getCurrencyById(Long id) throws SQLException;
}