
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;


import com.agrologic.app.model.FeedType;


import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface FeedTypeDao {

    public void insert(FeedType gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public FeedType getById(Long id) throws SQLException;

    public List<FeedType> getAllByCellinkId(Long cellinkId) throws SQLException;
}


