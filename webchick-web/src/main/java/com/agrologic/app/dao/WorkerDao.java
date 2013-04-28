
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;


import com.agrologic.app.model.WorkerDto;



import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author JanL
 */
public interface WorkerDao {

    public void insert(WorkerDto worker) throws SQLException;

    public void remove(Long id) throws SQLException;

    public WorkerDto getById(Long id) throws SQLException;

    public List<WorkerDto> getAllByCellinkId(Long cellinkId) throws SQLException;

    public String getCurrencyById(Long id) throws SQLException;
}



