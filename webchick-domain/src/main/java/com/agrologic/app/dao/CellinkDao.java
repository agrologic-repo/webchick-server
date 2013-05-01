package com.agrologic.app.dao;


import com.agrologic.app.model.Cellink;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Collection;

public interface CellinkDao {

    /**
     * Insert new cellink data to the database.
     *
     * @param cellink the new cellink
     * @throws SQLException if failed to execute statement.
     */
    void insert(Cellink cellink) throws SQLException;

    /**
     *
     * @param cellinks
     */
    void insert(Collection<Cellink> cellinks);

    /**
     * Update cellink .
     *
     * @param cellink the cellink with new data.
     * @throws SQLException if failed to execute statement.
     */
    void update(Cellink cellink) throws SQLException;

    /**
     * Removes a cellink from the database
     *
     * @param id the id of the cellink to be removed from the database
     * @throws SQLException if failed to remove the cellink from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Retrieve cellink by specified id.
     *
     * @param id the cellink id.
     * @return cellink
     * @throws SQLException if failed to execute statement.
     */
    Cellink getById(Long id) throws SQLException;

    /**
     * Validate cellink by name and password.
     *
     * @param name the cellink name
     * @param password the cellink password
     * @return the cellink if name and password valid, otherwise null.
     * @throws SQLException if failed to execute statement.
     */
    Cellink validate(String name, String password) throws SQLException;

    /**
     * Return actual cellink.
     *
     * @return actual cellink
     * @throws SQLException if failed to execute statement.
     */
    Cellink getActualCellink() throws SQLException;

    /**
     * Retrieve all cellinks from database.
     *
     * @return Collection of all cellink from cellink table.
     * @throws SQLException if failed to execute statement.
     */
    Collection<Cellink> getAll() throws SQLException;

    /**
     * Retrieve Collection of cellinks owned to user with specified user id.
     *
     * @param userId the user id.
     * @return Collection of cellinks owned to user with specified user id.
     * @throws SQLException if failed to execute statement.
     */
    Collection<Cellink> getAllUserCellinks(Long userId) throws SQLException;

    /**
     * Return state of cellink with specified id.
     *
     * @param id the cellink id
     * @return state of cellink with specified id.
     * @throws SQLException if failed to execute sql query.
     */
    int getState(Long id) throws SQLException;

    /**
     * Retrieve update time that cellink sent data.
     *
     * @param id the cellink id.
     * @return updated time the updated time
     * @throws SQLException if failed to execute statement.
     */
    Timestamp getUpdatedTime(Long id) throws SQLException;

    /**
     * Count rows
     *
     * @return count the number of cellinks in database
     * @throws SQLException if failed to execute statement.
     */
    int count() throws SQLException;
}


