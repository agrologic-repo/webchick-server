package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Flock;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.util.*;

public class FlockDaoImpl implements FlockDao {
    protected final DaoFactory dao;
    protected final Logger logger = LoggerFactory.getLogger(FlockDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    protected final SimpleJdbcInsert jdbcInsert;

    public FlockDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("flocks");
        this.dao = dao;
    }

    @Override
    public void insert(Flock flock) throws SQLException {
        logger.debug("Creating flock with id [{}]", flock.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("flockid", flock.getFlockId());
        valuesToInsert.put("controllerid", flock.getControllerId());
        valuesToInsert.put("name", flock.getFlockName());
        valuesToInsert.put("status", flock.getStatus());
        valuesToInsert.put("startdate", flock.getStartTime());
        valuesToInsert.put("enddate", flock.getEndTime());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Flock flock) throws SQLException {
        String sql = "update flocks set ControllerId=?, Name=?, Status=?,StartDate=?,EndDate=? where FlockID=?";
        logger.debug("Update flock with id [{}]", flock.getFlockId());
        jdbcTemplate.update(sql,
                new Object[]{flock.getControllerId(), flock.getFlockName(), flock.getStatus(), flock.getStartTime(),
                        flock.getEndTime()});
    }

    @Override
    public void remove(Long flockId) throws SQLException {
        Validate.notNull(flockId, "Flock ID can not be null");
        logger.debug("Delete flock with id [{}]", flockId);
        int i = jdbcTemplate.update("delete from flocks where FlockID=?", new Object[]{flockId});
    }

    @Override
    public void updateFlockDetail(Flock flock) throws SQLException {
        String sql = "update flocks set "
                + "QuantityMale=?, QuantityFemale=?, QuantityElect=?, QuantitySpread=?, QuantityWater=?, "
                + "ElectBegin=?, ElectEnd=?, FuelBegin=?, FuelEnd=?, GasBegin=?, GasEnd=?, WaterBegin=?, "
                + "WaterEnd=?, CostChickMale=?, CostChickFemale=?, CostElect=?, CostFuel=?, CostFuelEnd=?, CostGas=?, "
                + "CostGasEnd=?, CostWater=?, CostSpread=?, CostMaleKg=?, FuelAdd=?, GasAdd=?, FeedAdd=?, SpreadAdd=?, "
                + "Expenses=?, Revenues=?, "
                + "TotalElect=?, TotalFuel=?, TotalGas=?, TotalWater=?, TotalSpread=?, TotalMedic=?, TotalChicks=?, "
                + "TotalLabor=?, TotalFeed=?,  Currency=? where FlockID=? ";

        logger.debug("Update flock details with id [{}]", flock.getFlockId());
        jdbcTemplate.update(sql,
                new Object[]{
                        flock.getQuantityMale(),
                        flock.getQuantityFemale(),
                        flock.getQuantityElect(),
                        flock.getQuantitySpread(),
                        flock.getQuantityWater(),
                        flock.getElectBegin(),
                        flock.getElectEnd(),
                        flock.getFuelBegin(),
                        flock.getFuelEnd(),
                        flock.getGasBegin(),
                        flock.getGasEnd(),
                        flock.getWaterBegin(),
                        flock.getWaterEnd(),
                        flock.getCostChickMale(),
                        flock.getCostChickFemale(),
                        flock.getCostElect(),
                        flock.getCostFuel(),
                        flock.getCostFuelEnd(),
                        flock.getCostGas(),
                        flock.getCostGasEnd(),
                        flock.getCostWater(),
                        flock.getCostSpread(),
                        flock.getCostMaleKg(),
                        flock.getFuelAdd(),
                        flock.getGasAdd(),
                        flock.getFeedAdd(),
                        flock.getSpreadAdd(),
                        flock.getExpenses(),
                        flock.getRevenues(),
                        flock.getTotalElect(),
                        flock.getTotalFuel(),
                        flock.getTotalGas(),
                        flock.getTotalWater(),
                        flock.getTotalSpread(),
                        flock.getTotalMedic(),
                        flock.getTotalChicks(),
                        flock.getTotalLabor(),
                        flock.getTotalFeed(),
                        flock.getCurrency(),
                        flock.getFlockId()

                });
    }

    @Override
    public void close(Long flockId, String endDate) throws SQLException {
        String sql = "update flocks set Status='Close' , EndDate=? where FlockID=? ";
        Validate.notNull(flockId, "Flock ID can not be null");
        logger.debug("Close flock with id [{}]", flockId);
        jdbcTemplate.update(sql, new Object[]{flockId, endDate});
    }

    @Override
    public void updateHistoryByGrowDay(Long flockId, Integer growDay, String values) throws SQLException {
        Validate.notNull(flockId, "Flock ID can not be null");
        logger.debug("Update history by grow day in flock with id [{}]", flockId);
        String sql = "insert into flockhistory (FlockID,GrowDay,HistoryData) VALUES (?,?,?) " +
                "on duplicate key update HistoryData=VALUES(HistoryData)";
        int rows = jdbcTemplate.update(sql, new Object[]{flockId, growDay, values});
        System.out.println(rows);
    }

    @Override
    public void updateHistory24ByGrowDay(Long flockId, Integer growDay, String dnum, String values) throws SQLException {
        logger.debug("Update history 24 hours by grow day in flock with id [{}]", flockId);
        String sql = "insert into flockhistory24 (FlockID, GrowDay, DNum, HistoryData) values (?,?,?,?) " +
                "on duplicate key update HistoryData=values(HistoryData)";
        int rows = jdbcTemplate.update(sql, new Object[]{flockId, growDay, dnum, values});
        System.out.println(rows);
    }

    @Override
    public void removeAllHistoryInFlockByGrowDay(Long flockId, Integer growDay) throws SQLException {
        logger.debug("Delete flock history in flock with id [{}]", flockId);
        String sql = "delete from flockhistory where flockid=? and growday=?";
        jdbcTemplate.update(sql, new Object[]{flockId, growDay});
    }

    @Override
    public void removeAllHistoryInFlock(Long flockId) throws SQLException {
        String sql = "delete from flockhistory where flockid=?";
        logger.debug("Delete flock history in flock with id [{}]", flockId);
        jdbcTemplate.update(sql, new Object[]{flockId});
    }

    @Override
    public void removeAllHistoryOf24hourInFlock(Long flockId) throws SQLException {
        String sql = "delete from flockhistory24 where flockid=?";
        logger.debug("Delete flock history 24 hour in flock with id [{}]", flockId);
        jdbcTemplate.update(sql, new Object[]{flockId});
    }

    public Integer getUpdatedGrowDay(Long flockId) throws SQLException {
        String sql = "select growday from flockhistory where growday = "
                + "(select max(GrowDay) from flockhistory where FlockID=?)";
        logger.debug("Get last updated history grow day in flock with id [{}]", flockId);
        return jdbcTemplate.queryForInt(sql, new Object[]{flockId}, Integer.class);
    }

    @Override
    public Integer getUpdatedGrowDayHistory(Long flockId) throws SQLException {
        String sql = "select growday from flockhistory where flockid=? and growday = "
                + "(select max(GrowDay) from flockhistory where flockid=?)";
        logger.debug("Get last updated history grow day in flock with id [{}]", flockId);
        List<Integer> result = jdbcTemplate.queryForList(sql, new Object[]{flockId, flockId}, Integer.class);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public Integer getUpdatedGrowDayHistory24(Long flockId) throws SQLException {
        String sql = "select max(growday) as growday from flockhistory24  where flockid=? and DNum ="
                + " (select max(DNum) from flockhistory24 where FlockID=?)";
        logger.debug("Get last updated history of 24 hours grow day in flock with id [{}]", flockId);
        List<Integer> result = jdbcTemplate.queryForList(sql, new Object[]{flockId, flockId}, Integer.class);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public Integer getResetTime(Long flockId, Integer growDay) throws SQLException {
        String sql = "select historydata from flockhistory24 where FlockID=? and GrowDay=? and DNum='D70'";
        logger.debug("Get reset time for history data in flock with id [{}]", flockId);
        return jdbcTemplate.queryForInt(sql, new Object[]{flockId, growDay}, Integer.class);
    }

    @Override
    public Map<String, String> getHistoryN() {
        Map<String, String> historyNums = new HashMap<String, String>();
        historyNums.put("D18", "Inside Temp");
        historyNums.put("D19", "Outside Temp");
        historyNums.put("D20", "Humidity");
        historyNums.put("D21", "Water");
        historyNums.put("D72", "Feed");
        return historyNums;
    }

    @Override
    public Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException {
        String sql = "select * from flockhistory where flockid=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{flockId});
        Map<Integer, String> historyByGrowDay = new TreeMap<Integer, String>();
        while (rowSet.next()) {
            Integer growDay = rowSet.getInt("GrowDay");
            String history = rowSet.getString("HistoryData");
            historyByGrowDay.put(growDay, history);
        }
        return historyByGrowDay;
    }

    @Override
    public Map<Integer, String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException {
        String sql = "select * from flockhistory where flockid=? and growday between ? and ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{flockId, fromDay, toDay});
        Map<Integer, String> historyByGrowDay = new TreeMap<Integer, String>();
        while (rowSet.next()) {
            Integer growDay = rowSet.getInt("GrowDay");
            String history = rowSet.getString("HistoryData");
            historyByGrowDay.put(growDay, history);
        }
        return historyByGrowDay;
    }

    @Override
    public Map<Integer, String> getAllHistory24ByFlockAndDnum(Long flockId, String dnum) throws SQLException {
        String sql = "select * from flockhistory24 where  flockid=? and dnum=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{flockId, dnum});
        Map<Integer, String> historyByGrowDay = new TreeMap<Integer, String>();
        while (rowSet.next()) {
            Integer growDay = rowSet.getInt("GrowDay");
            String history = rowSet.getString("HistoryData");
            historyByGrowDay.put(growDay, history);
        }
        return historyByGrowDay;
    }

    public List<Integer> getHistory24GrowDays(Long flockId) throws SQLException {
        String sql = "select distinct growday from flockhistory24 where flockid=?";
        List<Integer> resultsList = jdbcTemplate.queryForList(sql, new Object[]{flockId}, Integer.class);
        return resultsList;
    }

    @Override
    public String getDNHistory24(String dn) throws SQLException {
        String sql = "select name from historyn where dn=?";
        String result = jdbcTemplate.queryForObject(sql, new Object[]{dn}, String.class);
        return result;
    }

    @Override
    public String getHistory24(Long flockId, Integer growDay, String dn) throws SQLException {
        String sql = "select * from flockhistory24 where flockid=? and growday=? and dnum=?";
        String result = jdbcTemplate.queryForObject(sql, new Object[]{flockId, growDay, dn}, String.class);
        return result;
    }

    @Override
    public Flock getById(Long flockId) throws SQLException {
        logger.debug("Get flock with id [{}]", flockId);
        String sql = "select * from flocks where FlockID=?";
        List<Flock> flocks = jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.flock());
        if (flocks.isEmpty()) {
            return null;
        }
        return flocks.get(0);
    }

    @Override
    public Flock getOpenFlockByController(Long controllerId) throws SQLException {
        logger.debug("Get open flock that belongs to controller with id [{}]", controllerId);
        String sql = "select * from flocks where ControllerID=? and Status='Open'";
        List<Flock> flocks = jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.flock());
        if (flocks.isEmpty()) {
            return null;
        }
        return flocks.get(0);
    }

    @Override
    public Collection<Flock> getAll() throws SQLException {
        logger.debug("Get all flocks");
        String sql = "select * from flocks ";
        return jdbcTemplate.query(sql, RowMappers.flock());
    }

    @Override
    public Collection<Flock> getAllFlocksByController(Long controllerId) throws SQLException {
        String sql = "select * from flocks where ControllerID=?";
        logger.debug("Get all flocks that belongs to controller with id [{}]", controllerId);
        return jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.flock());
    }
}
