package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DistribDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Distrib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistribDaoImpl implements DistribDao {

    protected final Logger logger = LoggerFactory.getLogger(DistribDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public DistribDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("distribute");
    }

    /**
     * (@inh)
     */
    @Override
    public void insert(Distrib distrib) throws SQLException {
        logger.debug("Inserting distribute with id [{}]", distrib.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("flockid", distrib.getFlockId());
        valuesToInsert.put("accountnumber", distrib.getAccountNumber());
        valuesToInsert.put("sex", distrib.getSex());
        valuesToInsert.put("target", distrib.getTarget());
        valuesToInsert.put("numofbirds", distrib.getNumOfBirds());
        valuesToInsert.put("weight", distrib.getWeight());
        valuesToInsert.put("quantitya", distrib.getQuantityA());
        valuesToInsert.put("quantityb", distrib.getQuantityB());
        valuesToInsert.put("quantityc", distrib.getQuantityC());
        valuesToInsert.put("badveterinary", distrib.getBadVeterinary());
        valuesToInsert.put("badanother", distrib.getBadAnother());
        valuesToInsert.put("pricea", distrib.getQuantityA());
        valuesToInsert.put("priceb", distrib.getQuantityB());
        valuesToInsert.put("pricec", distrib.getQuantityC());
        valuesToInsert.put("agedistreb", distrib.getBadVeterinary());
        valuesToInsert.put("averageweight", distrib.getBadAnother());
        valuesToInsert.put("dta", distrib.getDtA());
        valuesToInsert.put("dtb", distrib.getDtB());
        valuesToInsert.put("dtc", distrib.getDtC());
        valuesToInsert.put("dtveterinary", distrib.getDtVeterinary());
        valuesToInsert.put("dtanother", distrib.getDtAnother());
        valuesToInsert.put("calcsum", distrib.getTotal());
        valuesToInsert.put("handsum", distrib.getHandSum());
        valuesToInsert.put("total", distrib.getTotal());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove distribute with id [{}]", id);
        String sql = "delete from distribute where ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Distrib getById(Long id) throws SQLException {
        logger.debug("Get distribute with id [{}]", id);
        String sql = "select * from distribute where ID=?";
        List<Distrib> distibutes = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.distrib());
        if (distibutes.isEmpty()) {
            return null;
        }
        return distibutes.get(0);
    }

    @Override
    public List<Distrib> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all distribute data ");
        String sql = "select * from distribute where FlockID=?";
        return jdbcTemplate.query(sql,new Object[]{flockId},  RowMappers.distrib());
    }
}
