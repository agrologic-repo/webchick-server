
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Fuel;


import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface FuelDao {

    public void insert(Fuel fuel) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Fuel getById(Long id) throws SQLException;

    public List<Fuel> getAllByFlockId(Long flockId) throws SQLException;
}


