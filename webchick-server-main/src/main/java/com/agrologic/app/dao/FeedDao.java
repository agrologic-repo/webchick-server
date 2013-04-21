
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.Feed;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface FeedDao {

    public void insert(Feed gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Feed getById(Long id) throws SQLException;

    public List<Feed> getAllByFlockId(Long flockId) throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
