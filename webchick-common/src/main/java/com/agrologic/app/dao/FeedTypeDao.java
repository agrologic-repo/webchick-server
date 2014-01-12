package com.agrologic.app.dao;

import com.agrologic.app.model.FeedType;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the {@link  com.agrologic.app.model.FeedType}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.FeedType} objects.
 *
 * @author Valery Manakhimov
 */
public interface FeedTypeDao {

    /**
     * Insert a new alarm row to table alarms .
     *
     * @param feedType an objects that encapsulates an feedType attributes .
     * @throws java.sql.SQLException if failed to insert new feedType to the database .
     */
    public void insert(FeedType feedType) throws SQLException;

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
    public FeedType getById(Long id) throws SQLException;

    /**
     * @param cellinkId
     * @return
     * @throws java.sql.SQLException
     */
    public List<FeedType> getAllByCellinkId(Long cellinkId) throws SQLException;
}


