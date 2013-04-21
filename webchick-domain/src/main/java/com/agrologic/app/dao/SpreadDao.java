
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.Spread;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface SpreadDao {

    public void insert(Spread gaz) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Spread getById(Long id) throws SQLException;

    public List<Spread> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
