package com.agrologic.app.dao;

import com.agrologic.app.model.FeedType;
import java.sql.SQLException;
import java.util.List;

public interface FeedTypeDao {

    public void insert(FeedType gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public FeedType getById(Long id) throws SQLException;

    public List<FeedType> getAllByCellinkId(Long cellinkId) throws SQLException;
}


