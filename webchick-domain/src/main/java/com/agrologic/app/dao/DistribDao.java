
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Distrib;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author JanL
 */
public interface DistribDao {

    public void insert(Distrib gas) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Distrib getById(Long id) throws SQLException;

    public List<Distrib> getAllByFlockId(Long flockId) throws SQLException;
}


