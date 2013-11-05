package com.agrologic.app.dao;

import com.agrologic.app.model.Spread;

import java.sql.SQLException;
import java.util.List;

public interface SpreadDao {
    /**
     *
     * @param spread hte spread
     * @throws SQLException
     */
    public void insert(Spread spread) throws SQLException;

    /**
     *
     * @param id
     * @throws SQLException
     */
    public void remove(Long id) throws SQLException;

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Spread getById(Long id) throws SQLException;

    /**
     *
     * @param flockId
     * @return
     * @throws SQLException
     */
    public List<Spread> getAllByFlockId(Long flockId) throws SQLException;

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public String getCurrencyById(Long id) throws SQLException;
}