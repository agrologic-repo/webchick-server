
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Gas;


import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface GasDao {

    public void insert(Gas gas) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Gas getById(Long id) throws SQLException;

    public List<Gas> getAllByFlockId(Long flockId) throws SQLException;
}

