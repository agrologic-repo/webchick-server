package com.agrologic.app.dao;

import com.agrologic.app.model.Feed;


import java.sql.SQLException;
import java.util.List;

public interface FeedDao {

    public void insert(Feed gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Feed getById(Long id) throws SQLException;

    public List<Feed> getAllByFlockId(Long flockId) throws SQLException;
}


