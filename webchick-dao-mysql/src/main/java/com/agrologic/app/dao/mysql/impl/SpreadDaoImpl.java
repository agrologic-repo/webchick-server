package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.SpreadDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Spread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpreadDaoImpl implements SpreadDao {

    protected final Logger logger = LoggerFactory.getLogger(SpreadDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public SpreadDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("SPREAD");
        this.jdbcInsert.setGeneratedKeyName("ID");
    }

    @Override
    public void insert(Spread spread) throws SQLException {
        logger.debug("Inserting spread with type [{}]", spread.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", spread.getFlockId());
        valuesToInsert.put("Amount", spread.getAmount());
        valuesToInsert.put("Date", spread.getDate());
        valuesToInsert.put("NumberAccount", spread.getNumberAccount());
        valuesToInsert.put("Price", spread.getPrice());
        valuesToInsert.put("Total", spread.getTotal());

        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove spread with id [{}]", id);
        String sql = "delete from spread where ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Spread getById(Long id) throws SQLException {
        logger.debug("Get spread with id [{}]", id);
        String sql = "select * from spread where ID=?";
        List<Spread> gasList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.spread());
        if (gasList.isEmpty()) {
            return null;
        }
        return gasList.get(0);
    }

    @Override
    public List<Spread> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all spread data with flock id {} ", flockId);
        String sql = "select * from spread where FlockID=?";
        return jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.spread());
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
