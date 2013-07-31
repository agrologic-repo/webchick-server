package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerDaoImpl implements ControllerDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ControllerDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("controllers");
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Controller controller) throws SQLException {
        logger.debug("Creating controller with name [{}]", controller.getName());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("cellinkid", controller.getCellinkId());
        valuesToInsert.put("title", controller.getTitle());
        valuesToInsert.put("netname", controller.getNetName());
        valuesToInsert.put("controllername", controller.getName());
        valuesToInsert.put("programid", controller.getProgramId());
        valuesToInsert.put("area", controller.getArea());
        valuesToInsert.put("active", controller.isActive());
        jdbcInsert.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Controller controller) throws SQLException {
        logger.debug("Update controller with id [{}]", controller.getId());
        jdbcTemplate.update("update controllers set Title=?, NetName=?, ControllerName=?, Area=?, ProgramID=?, "
                + "Active=? where ControllerID=? ",
                new Object[]{controller.getTitle(), controller.getNetName(), controller.getName(),
                        controller.getArea(), controller.getProgramId(), controller.isActive(), controller.getId()});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long id) throws SQLException {
        Validate.notNull(id, "Id can not be null");
        logger.debug("Delete controller with id [{}]", id);
        jdbcTemplate.update("delete from controllers where ControllerID=?", new Object[]{id});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Collection<Controller> controllers) throws SQLException {
        // there is duplicate controller elements in controllerList we need only unique elements
        Collection<Controller> controllerCollection = Util.getUniqueElements(controllers);
        for (Controller controller : controllerCollection) {
            insert(controller);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void resetControllerData(Long id) throws SQLException {
        Validate.notNull(id, "Id can not be null");
        logger.debug("Set data value to -1 on controller with id [{}]", id);
        jdbcTemplate.update("update controllerdata set value=-1 where ControllerID=?", new Object[]{id});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControllerData(Long id, Long dataId, Long value) throws SQLException {
        Validate.notNull(id, "Id can not be null");
        logger.debug("Set data value on controller with id [{}]", id);
        jdbcTemplate.update("insert into controllerdata (ControllerID,DataID,Value) "
                + "values (?,?,?) on duplicate key update value=values(Value)", new Object[]{id, dataId, value});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControllerData(final Long id, final Collection<Data> onlineData) throws SQLException {
        Validate.notNull(id, "Id can not be null");
        logger.debug("Set collection of data value of controller with id [{}]", id);
        final String sql = "insert into controllerdata (ControllerID, DataID,Value) "
                + "values (?,?,?) on duplicate key update value=values(value)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Data data = Lists.newArrayList(onlineData).get(i);
                ps.setLong(1, id);
                ps.setLong(2, data.getId());
                ps.setLong(3, data.getValue());
            }

            @Override
            public int getBatchSize() {
                return onlineData.size();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControllerGraph(Long id, String values, Timestamp updateTime) throws SQLException {
        logger.debug("Set graphs data of controller with id [{}]", id);
        String sqlQuery = "insert into graph24hours (ControllerID,Dataset,UpdateTime) "
                + "values (?,?,?) on duplicate key update Dataset=values(Dataset) , UpdateTime=VALUES(UpdateTime)";
        jdbcTemplate.update(sqlQuery, new Object[]{id, values, updateTime});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControllerHistogram(Long id, String plate, String values, Timestamp updateTime)
            throws SQLException {
        logger.debug("Set histogram data of controller with id [{}]", id);
        String sqlQuery = "insert into histogram24hour (ControllerID, Plate, Histogram, UpdateTime) "
                + "values(?,?,?,?) on duplicate key update Histogram=values(Histogram) , UpdateTime=values(UpdateTime)";
        jdbcTemplate.update(sqlQuery, new Object[]{id, plate, values, updateTime});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeControllerData(Long id) throws SQLException {
        logger.debug("Delete controller data of controller with id [{}]", id);
        String sqlQuery = "delete from controllerdata where ControllerID=?";
        jdbcTemplate.update(sqlQuery, new Object[]{id});
    }

    @Override
    public void removeChangedValue(Long id, Long dataId) throws SQLException {
        logger.debug("Delete controller data that was sent to change to controller with id [{}]", id);
        String sqlQuery = "delete from newcontrollerdata where ControllerID=? and DataID=?";
        jdbcTemplate.update(sqlQuery, new Object[]{id, dataId});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNewDataValueToController(Long id, Long dataId, Long value) throws SQLException {
        logger.debug("Add new value to change on controller with id [{}]", id);
        String sqlQuery = "insert into newcontrollerdata (ControllerID,DataID,Value) "
                + "values(?,?,?) on duplicate key update Value=values(Value)";
        jdbcTemplate.update(sqlQuery, new Object[]{id, dataId, value});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNewDataValueOnController(Long id, Long dataId, Long value)
            throws SQLException {
        logger.debug("Add new value to change on controller with id [{}]", id);
        String sqlQuery = "insert into newcontrollerdata (ControllerID,DataID,Value) "
                + "values(?,?,?) on duplicate key update Value=values(Value)";
        jdbcTemplate.update(sqlQuery, new Object[]{id, dataId, value});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Timestamp getUpdatedGraphTime(Long id) throws SQLException {
        logger.debug("Get updated time of graphs on controller with id [{}]", id);
        String sqlQuery = "select UpdateTime as time from graph24hours where ControllerID=? ";
        List<Timestamp> result = jdbcTemplate.query(sqlQuery, new Object[]{id}, new RowMapper<Timestamp>() {
            @Override
            public Timestamp mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getTimestamp(rowNum);
            }
        });
        return result.get(0);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     */
    @Override
    public Timestamp getUpdatedHistoryTime(Long id) throws SQLException {
        logger.debug("Get updated time of histogram on controller with id [{}]", id);
        String sqlQuery = "select UpdateTime as time from history24hours where ControllerID=?";
        List<Timestamp> result = jdbcTemplate.query(sqlQuery, new Object[]{id}, new RowMapper<Timestamp>() {
            @Override
            public Timestamp mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getTimestamp(rowNum);
            }
        });
        return result.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller getById(Long id) throws SQLException {
        logger.debug("Get controller with id [{}]", id);
        String sqlQuery = "select * from controllers where controllerid=?";
        List<Controller> controllers = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.controller());
        if (controllers.isEmpty()) {
            return null;
        }
        return controllers.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getControllerGraph(Long id) throws SQLException {
        logger.debug("Get string with graphs values of controller with id [{}]", id);
        String sqlQuery = "select Dataset from graph24hours where ControllerID=?";
        List<String> result = jdbcTemplate.query(sqlQuery, new Object[]{id}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(rowNum);
            }
        });
        return result.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataReady(Long userId) throws SQLException {
        logger.debug("Check if any data of given user already loaded ");
        String sqlQuery = "select dataid from controllerdata where controllerid in "
                + "(select controllerid from controllers where cellinkid in "
                + "(select cellinkid from cellinks where userid=? ))";

        List<Integer> result = jdbcTemplate.query(sqlQuery, new Object[]{userId}, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(rowNum);
            }
        });
        return result.isEmpty() ? false : true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getControllerNames() throws SQLException {
        logger.debug("Get list of controller names");
        String sqlQuery = "select distinct controllername from controllers";
        List<String> result = jdbcTemplate.queryForList(sqlQuery, String.class);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Controller> getAll() throws SQLException {
        logger.debug("Get all controllers ");
        String sqlQuery = "select * from controllers";
        return jdbcTemplate.query(sqlQuery, RowMappers.controller());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Controller> getAllByCellink(Long cellinkId) throws SQLException {
        logger.debug("Get all controllers that belongs to cellink with id [{}]", cellinkId);
        String sqlQuery = "select * from controllers where cellinkid=?";
        return jdbcTemplate.query(sqlQuery, new Object[]{cellinkId}, RowMappers.controller());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Controller> getActiveCellinkControllers(Long cellinkId) throws SQLException {
        logger.debug("Get all controllers that belongs to active cellink with id [{}]", cellinkId);
        String sqlQuery = "select * from controllers where cellinkid=? and active=1";
        return jdbcTemplate.query(sqlQuery, new Object[]{cellinkId}, RowMappers.controller());
    }
}
