package com.agrologic.app.dao;

import com.agrologic.app.model.Feed;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the {@link  com.agrologic.app.model.Feed}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Feed} objects.
 *
 * @author Valery Manakhimov
 */
public interface FeedDao {
    /**
     * Insert a new alarm row to table alarms .
     *
     * @param feed an objects that encapsulates an feed attributes .
     * @throws java.sql.SQLException if failed to insert new feed to the database .
     */
    public void insert(Feed feed) throws SQLException;

    /**
     * @param id
     * @throws java.sql.SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    public Feed getById(Long id) throws SQLException;

    /**
     * @param flockId
     * @return
     * @throws java.sql.SQLException
     */
    public List<Feed> getAllByFlockId(Long flockId) throws SQLException;
}


