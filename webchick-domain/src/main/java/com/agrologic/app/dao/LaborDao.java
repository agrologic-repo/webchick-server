
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;



import com.agrologic.app.model.Labor;
import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface LaborDao {

    public void insert(Labor labor) throws SQLException;

    public void remove(Long id) throws SQLException;

    public Labor getById(Long id) throws SQLException;

    public List<Labor> getAllByFlockId(Long flockId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}


