package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.WorkerDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Worker;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerDaoImpl implements WorkerDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(WorkerDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public WorkerDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("workers");
        this.dao = dao;
    }

    @Override
    public void insert(Worker worker) throws SQLException {
        logger.debug("Inserting worker with name [{}]", worker.getName());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("name", worker.getName());
        valuesToInsert.put("define", worker.getDefine());
        valuesToInsert.put("phone", worker.getPhone());
        valuesToInsert.put("hourcost", worker.getHourCost());
        valuesToInsert.put("cellinkid", worker.getCellinkId());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        Validate.notNull(id, " Worker id can not be null");
        logger.debug("Delete workers with id [{}]", id);
        jdbcTemplate.update("delete from workers where ID=?", new Object[]{id});
    }

    @Override
    public Worker getById(Long id) throws SQLException {
        logger.debug("Get worker with id [{}]", id);
        String sql = "select * from workers where ID=?";
        List<Worker> workers = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.worker());
        if (workers.isEmpty()) {
            return null;
        }
        return workers.get(0);
    }

    @Override
    public List<Worker> getAllByCellinkId(Long cellinkId) throws SQLException {
        logger.debug("Get all alarm names of given language id ");
        String sql = "select * from workers where CellinkID=?";
        return jdbcTemplate.query(sql, new Object[]{cellinkId}, RowMappers.worker());
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        logger.debug("Get currency string with id  [{}]", id);
        String sql = "select * from currency where ID=?";
        List<String> currency = jdbcTemplate.queryForList(sql, new Object[]{id}, String.class);
        if (currency.isEmpty()) {
            return null;
        }
        return currency.get(0);
    }
}
