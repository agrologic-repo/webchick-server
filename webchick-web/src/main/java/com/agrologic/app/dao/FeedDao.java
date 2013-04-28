
package com.agrologic.app.dao;


import com.agrologic.app.model.FeedDto;



import java.sql.SQLException;

import java.util.List;

public interface FeedDao {

    public void insert(FeedDto gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public FeedDto getById(Long id) throws SQLException;

    public List<FeedDto> getAllByFlockId(Long flockId) throws SQLException;
}



