package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.FuelDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Feed;
import com.agrologic.app.model.Fuel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelDaoImpl implements FuelDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(FuelDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FuelDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("fuel");
        this.dao = dao;
    }

    @Override
    public void insert(Fuel fuel) throws SQLException {
        logger.debug("Inserting fuel with type [{}]", fuel.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", fuel.getFlockId());
        valuesToInsert.put("Amount", fuel.getAmount());
        valuesToInsert.put("Date", fuel.getDate());
        valuesToInsert.put("NumberAccount", fuel.getNumberAccount());
        valuesToInsert.put("Price", fuel.getPrice());
        valuesToInsert.put("Total", fuel.getTotal());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove fuel with id [{}]", id);
        String sql = "delete from fuel where ID=?";
        jdbcTemplate.update(sql, id);
    }


    @Override
    public Fuel getById(Long id) throws SQLException {
        logger.debug("Get fuel with id [{}]", id);
        String sql = "select * from fuel where ID=?";
        List<Fuel> fuelList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.fuel());
        if (fuelList.isEmpty()) {
            return null;
        }
        return fuelList.get(0);
    }

    @Override
    public List<Fuel> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all fuel data with flock id {} ", flockId);
        String sql = "select * from fuel where FlockID=?";
        return jdbcTemplate.query(sql,new Object[]{flockId},  RowMappers.fuel());
    }
}
