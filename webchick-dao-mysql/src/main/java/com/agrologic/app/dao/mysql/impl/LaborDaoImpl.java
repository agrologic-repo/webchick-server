package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.LaborDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Gas;
import com.agrologic.app.model.Labor;
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

public class LaborDaoImpl implements LaborDao {

    protected final Logger logger = LoggerFactory.getLogger(LaborDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public LaborDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("labor");
    }

    @Override
    public void insert(Labor labor) throws SQLException {
        logger.debug("Inserting labor with type [{}]", labor.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("Date", labor.getDate());
        valuesToInsert.put("WorkerID", labor.getWorkerId());
        valuesToInsert.put("Hours", labor.getHours());
        valuesToInsert.put("Salary", labor.getSalary());
        valuesToInsert.put("FlockID", labor.getFlockId());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove labor with id [{}]", id);
        String sql = "delete from labor where ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Labor getById(Long id) throws SQLException {
        logger.debug("Get labor with id [{}]", id);
        String sql = "select * from labor where ID=?";
        List<Labor> laborList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.labor());
        if (laborList.isEmpty()) {
            return null;
        }
        return laborList.get(0);
    }

    @Override
    public List<Labor> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all labor data with flock id {} ", flockId);
        String sql = "select * from labor where FlockID=?";
        return jdbcTemplate.query(sql,new Object[]{flockId},  RowMappers.labor());
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        logger.debug("Get currency ");
        String sql = "select * from currency where ID=?";
        List<String> stringList = jdbcTemplate.queryForList(sql, new Object[]{id}, String.class);
        if (stringList.isEmpty()) {
            return null;
        }
        return stringList.get(0);
    }
}
