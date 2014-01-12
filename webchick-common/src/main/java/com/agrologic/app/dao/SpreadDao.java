package com.agrologic.app.dao;

import com.agrologic.app.model.Spread;

import java.sql.SQLException;
import java.util.List;

public interface SpreadDao {
    /**
     * @param spread hte spread
     * @throws java.sql.SQLException
     */
    public void insert(Spread spread) throws SQLException;

    /**
     * @param id
     * @throws java.sql.SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    public Spread getById(Long id) throws SQLException;

    /**
     * @param flockId
     * @return
     * @throws java.sql.SQLException
     */
    public List<Spread> getAllByFlockId(Long flockId) throws SQLException;

    /**
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    public String getCurrencyById(Long id) throws SQLException;
}