
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ControllerDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DerbyControllerDaoImpl extends ControllerDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyControllerDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "CONTROLLERS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table cellink from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createControllerTable();
        createControllerDataTable();
        createNewControllerData();
        createGraphTable();
    }

    private void createControllerTable() throws SQLException {
        logger.info("create table controllers if not exist");
        String sql = "CREATE TABLE CONTROLLERS ( " + "CONTROLLERID INT NOT NULL,  " + "CELLINKID INT NOT NULL, "
                + "TITLE VARCHAR(200) , " + "NETNAME VARCHAR(45) , " + "CONTROLLERNAME VARCHAR(45) , "
                + "PROGRAMID INT NOT NULL, " + "AREA INT DEFAULT 0, " + "ACTIVE SMALLINT DEFAULT 0,"
                + " HOUSETYPE VARCHAR(45) ,  "
                + "PRIMARY KEY (CONTROLLERID), " + " FOREIGN KEY (CELLINKID) REFERENCES CELLINKS(CELLINKID)) ";
        jdbcTemplate.execute(sql);
    }

    private void createControllerDataTable() throws SQLException {
        logger.info("create table controllerdata if not exist");
        String sql = "CREATE TABLE CONTROLLERDATA ( " + "DATAID INT NOT NULL," + "CONTROLLERID INT NOT NULL,"
                + "VALUE  INT DEFAULT 0," + "PRIMARY KEY (DATAID, CONTROLLERID),"
                + "FOREIGN KEY (DATAID) REFERENCES DATATABLE (DATAID), "
                + "FOREIGN KEY (CONTROLLERID) REFERENCES CONTROLLERS (CONTROLLERID))";
        jdbcTemplate.execute(sql);
    }

    private void createNewControllerData() throws SQLException {
        logger.info("create table newcontrollerdata if not exist");
        String sql = "CREATE TABLE NEWCONTROLLERDATA " + "(CONTROLLERID INT NOT NULL, " + "DATAID INT NOT NULL , "
                + "VALUE INT NOT NULL ," + "CONSTRAINT CNTRLDTA_PK PRIMARY KEY (CONTROLLERID,DATAID))";
        jdbcTemplate.execute(sql);
    }

    private void createGraphTable() throws SQLException {
        logger.info("create table graph24hours if not exist");
        String sql = "CREATE TABLE GRAPH24HOURS " + "(CONTROLLERID INT NOT NULL, "
                + "DATASET  VARCHAR(5000) , "
                + "UPDATETIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "CONSTRAINT CNTRGRAPH_PK PRIMARY KEY (CONTROLLERID))";
        jdbcTemplate.execute(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Controller controller) throws SQLException {
        logger.debug("Creating controller with name [{}]", controller.getName());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("CONTROLLERID", controller.getId());
        valuesToInsert.put("CELLINKID", controller.getCellinkId());
        valuesToInsert.put("TITLE", controller.getTitle());
        valuesToInsert.put("NETNAME", controller.getNetName());
        valuesToInsert.put("CONTROLLERNAME", controller.getName());
        valuesToInsert.put("PROGRAMID", controller.getProgramId());
        valuesToInsert.put("AREA", controller.getArea());
        valuesToInsert.put("ACTIVE", controller.isActive());
        valuesToInsert.put("HOUSETYPE", controller.getHouseType());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void updateControllerData(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM CONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        String sqlUpdateQuery = "UPDATE CONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        int exist = jdbcTemplate.queryForInt(sqlSelectQuery, controllerId, dataId);
        if (exist == 1) {
            jdbcTemplate.update(sqlUpdateQuery, value, controllerId, dataId);
        } else {
            SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcControllerDataInsert.setTableName("CONTROLLERDATA");
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            valuesToInsert.put("controllerid", controllerId);
            valuesToInsert.put("dataid", dataId);
            valuesToInsert.put("value", value);
            jdbcInsert.execute(valuesToInsert);
        }
    }

    @Override
    public void updateControllerData(final Long controllerId, final Collection<Data> onlineData) throws SQLException {
        final String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM CONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        final String sqlUpdateQuery = "UPDATE CONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        final Collection<Data> dataToUpdate = new ArrayList<Data>();

        for (Data data : onlineData) {
            int exist = jdbcTemplate.queryForInt(sqlSelectQuery, controllerId, data.getId());
            if (exist == 1) {
                dataToUpdate.add(data);
            } else {
                SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
                jdbcControllerDataInsert.setTableName("CONTROLLERDATA");
                Map<String, Object> valuesToInsert = new HashMap<String, Object>();
                valuesToInsert.put("CONTROLLERID", controllerId);
                valuesToInsert.put("DATAID", data.getId());
                valuesToInsert.put("VALUE", data.getValue());
                jdbcControllerDataInsert.execute(valuesToInsert);
            }
        }

        jdbcTemplate.batchUpdate(sqlUpdateQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Data data = Lists.newArrayList(dataToUpdate).get(i);
                ps.setLong(1, data.getValue());
                ps.setLong(2, controllerId);
                ps.setLong(3, data.getId());
            }

            @Override
            public int getBatchSize() {
                return dataToUpdate.size();
            }
        });
    }

    public void updateControllerGraph(Long controllerId, String values, Timestamp updateTime) throws SQLException {
        final String sqlSelectQuery = "SELECT COUNT(DATASET) AS EXIST FROM GRAPH24HOURS WHERE CONTROLLERID=?";
        final String sqlUpdateQuery = "UPDATE GRAPH24HOURS SET DATASET=? ,UPDATETIME=? WHERE CONTROLLERID=?";

        int exist = jdbcTemplate.queryForInt(sqlSelectQuery, controllerId);

        if (exist == 1) {
            jdbcTemplate.update(sqlUpdateQuery, values, updateTime, controllerId);
        } else {
            SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcControllerDataInsert.setTableName("GRAPH24HOURS");
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            valuesToInsert.put("dataset", values);
            valuesToInsert.put("updatetime", updateTime);
            valuesToInsert.put("controllerId", controllerId);
            jdbcControllerDataInsert.execute(valuesToInsert);
        }
    }

    @Override
    public void sendNewDataValueToController(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM NEWCONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        String sqlUpdateQuery = "UPDATE NEWCONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        int exist = jdbcTemplate.queryForInt(sqlSelectQuery, controllerId, dataId);
        if (exist == 1) {
            jdbcTemplate.update(sqlUpdateQuery, new Object[]{controllerId, dataId, value});

        } else {
            SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcControllerDataInsert.setTableName("NEWCONTROLLERDATA");
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            valuesToInsert.put("CONTROLLERID", controllerId);
            valuesToInsert.put("DATAID", dataId);
            valuesToInsert.put("VALUE", value);
            jdbcControllerDataInsert.execute(valuesToInsert);
        }
    }

    @Override
    public void saveNewDataValueOnController(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM CONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        String sqlUpdateQuery = "UPDATE CONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";

        int exist = jdbcTemplate.queryForInt(sqlSelectQuery, controllerId, dataId);
        if (exist == 1) {
            jdbcTemplate.update(sqlUpdateQuery, new Object[]{value, controllerId, dataId});

        } else {
            SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcControllerDataInsert.setTableName("CONTROLLERDATA");
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            valuesToInsert.put("CONTROLLERID", controllerId);
            valuesToInsert.put("DATAID", dataId);
            valuesToInsert.put("VALUE", value);
            jdbcControllerDataInsert.execute(valuesToInsert);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        logger.debug("Drop controllers table");

        String sql = "DROP TABLE APP.CONTROLLERS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        logger.debug("Delete controllers table");
        String sql = "DELETE  FROM APP.CONTROLLERS ";
        jdbcTemplate.execute(sql);
    }
}



