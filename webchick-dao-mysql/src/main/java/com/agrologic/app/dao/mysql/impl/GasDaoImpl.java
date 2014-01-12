package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.GasDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Gas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GasDaoImpl implements GasDao {

    protected final Logger logger = LoggerFactory.getLogger(GasDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public GasDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("gas");
        this.jdbcInsert.setGeneratedKeyName("ID");
    }

    @Override
    public void insert(Gas gas) throws SQLException {
        logger.debug("Inserting gas with type [{}]", gas.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", gas.getFlockId());
        valuesToInsert.put("Amount", gas.getAmount());
        valuesToInsert.put("Date", gas.getDate());
        valuesToInsert.put("NumberAccount", gas.getNumberAccount());
        valuesToInsert.put("Price", gas.getPrice());
        valuesToInsert.put("Total", gas.getTotal());
        jdbcInsert.execute(valuesToInsert);

    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove gas with id [{}]", id);
        String sql = "delete from gas where ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Gas getById(Long id) throws SQLException {
        logger.debug("Get gas with id [{}]", id);
        String sql = "select * from gas where ID=?";
        List<Gas> gasList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.gas());
        if (gasList.isEmpty()) {
            return null;
        }
        return gasList.get(0);
    }

    @Override
    public List<Gas> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all gas data with flock id {} ", flockId);
        String sql = "select * from gas where FlockID=?";
        return jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.gas());
    }
}
