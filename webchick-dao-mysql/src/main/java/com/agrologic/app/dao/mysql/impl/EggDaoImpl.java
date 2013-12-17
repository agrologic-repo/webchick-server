package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.EggDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Eggs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EggDaoImpl implements EggDao {

    protected final Logger logger = LoggerFactory.getLogger(GasDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public EggDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("eggs");
    }

    @Override
    public void insert(Eggs eggs) throws SQLException {
        logger.debug("Inserting eggs with type [{}]", eggs.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", eggs.getFlockId());
        valuesToInsert.put("Day", eggs.getDay());
        valuesToInsert.put("NumOfBirds", eggs.getNumOfBirds());
        valuesToInsert.put("EggsQuantity", eggs.getEggQuantity());
        valuesToInsert.put("SoftShelled", eggs.getSoftShelled());
        valuesToInsert.put("Cracked", eggs.getCracked());
        valuesToInsert.put("FeedConsump", eggs.getFeedConsump());
        valuesToInsert.put("WaterConsump", eggs.getWaterConsump());
        valuesToInsert.put("DailyMortal", eggs.getDailyMortal());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Eggs eggs) throws SQLException {
        logger.debug("Update eggs ");
        String sql = "update eggs set NumOfBirds=?,EggsQuantity=?,SoftShelled=? ,Cracked=?, FeedConsump=?, " +
                " WaterConsump=?, DailyMortal=? where FlockID=? and Day=?";
        jdbcTemplate.update(sql, new Object[]{eggs.getNumOfBirds(), eggs.getEggQuantity(), eggs.getSoftShelled(),
                eggs.getCracked(), eggs.getFeedConsump(), eggs.getWaterConsump(), eggs.getDailyMortal(), eggs.getFlockId(),
                eggs.getDay()});
    }

    @Override
    public void remove(Long flockId, Integer day) throws SQLException {
        logger.debug("Remove eggs with flock id and day [{} {}]", flockId, day);
        String sql = "delete from eggs where FlockID=? and Day=?";
        jdbcTemplate.update(sql, flockId, day);
    }

    @Override
    public Eggs getById(Long id) throws SQLException {
        logger.debug("Get eggs with id [{}]", id);
        String sql = "select * from eggs where ID=?";
        List<Eggs> eggsList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.eggs());
        if (eggsList.isEmpty()) {
            return null;
        }
        return eggsList.get(0);
    }

    @Override
    public List<Eggs> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all eggs data with flock id {} ", flockId);
        String sql = "select * from eggs where FlockID=?";
        return jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.eggs());
    }
}
