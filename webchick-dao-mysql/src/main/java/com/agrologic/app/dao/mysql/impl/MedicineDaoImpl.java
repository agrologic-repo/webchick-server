package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.MedicineDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Gas;
import com.agrologic.app.model.Medicine;
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

public class MedicineDaoImpl implements MedicineDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(MedicineDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MedicineDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("medicine");
        this.dao = dao;
    }

    @Override
    public void insert(Medicine medicine) throws SQLException {
        logger.debug("Inserting medicine with flock id [{}]", medicine.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", medicine.getFlockId());
        valuesToInsert.put("Amount", medicine.getAmount());
        valuesToInsert.put("Name", medicine.getName());
        valuesToInsert.put("Price", medicine.getPrice());
        valuesToInsert.put("Total", medicine.getTotal());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove medicine with id [{}]", id);
        String sql = "delete from medicine where ID=?";
        jdbcTemplate.update(sql, id);

    }

    @Override
    public Medicine getById(Long id) throws SQLException {
        logger.debug("Get medicine with id [{}]", id);
        String sql = "select * from medicine where ID=?";
        List<Medicine> medicineList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.medicine());
        if (medicineList.isEmpty()) {
            return null;
        }
        return medicineList.get(0);
    }

    @Override
    public List<Medicine> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all medicine data with flock id {} ", flockId);
        String sql = "select * from medicine where FlockID=?";
        return jdbcTemplate.query(sql,new Object[]{flockId}, RowMappers.medicine());
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
