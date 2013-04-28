package com.agrologic.app.dao;

import com.agrologic.app.model.FeedTypeDto;



import java.sql.SQLException;

import java.util.List;

public interface FeedTypeDao {
    public void insert(FeedTypeDto gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public FeedTypeDto getById(Long id) throws SQLException;

    public List<FeedTypeDto> getAllByCellinkId(Long cellinkId) throws SQLException;
}



