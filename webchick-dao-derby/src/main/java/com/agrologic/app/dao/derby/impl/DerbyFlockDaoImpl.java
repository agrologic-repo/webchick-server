package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mysql.impl.FlockDaoImpl;
import com.agrologic.app.model.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DerbyFlockDaoImpl extends FlockDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFlockDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FLOCKS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table FLOCK from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createFlock();
        createFlockHistory();
        createFlockHistory24();
        createHistoryN();
    }

    public void createFlock() throws SQLException {
        String sql = "CREATE TABLE FLOCKS "
                + "( "
                + "FLOCKID INT NOT NULL , "
                + "CONTROLLERID INT NOT NULL, "
                + "NAME VARCHAR(45) NOT NULL, "
                + "STATUS VARCHAR(10) NOT NULL, "
                + "STARTDATE VARCHAR(20)  , "
                + "ENDDATE VARCHAR(20)  , "
                + "PROGRAMID INT , "
                + "QUANTITYMALE INT, "
                + "QUANTITYFEMALE INT,"
                + "QUANTITYELECT INT,"
                + "QUANTITYSPREAD INT,"
                + "QUANTITYWATER INT,"
                + "ELECTBEGIN INT,"
                + "ELECTEND INT,"
                + "FUELBEGIN INT,"
                + "FUELEND INT,"
                + "GASBEGIN INT,"
                + "GASEND INT,"
                + "WATERBEGIN INT,"
                + "WATEREND INT,"
                + "COSTCHICKMALE REAL, "
                + "COSTCHICKFEMALE REAL, "
                + "COSTELECT REAL, "
                + "COSTFUEL REAL, "
                + "COSTFUELEND REAL, "
                + "COSTGAS REAL, "
                + "COSTGASEND REAL, "
                + "COSTWATER REAL, "
                + "COSTSPREAD REAL, "
                + "COSTMALEKG REAL, "
                + "FUELADD REAL, "
                + "GASADD REAL, "
                + "FEEDADD REAL, "
                + "SPREADADD REAL, "
                + "EXPENSES REAL, "
                + "REVENUES REAL, "
                + "COSTPERKG REAL, "
                + "TOTALELECT REAL, "
                + "TOTALFUEL REAL, "
                + "TOTALGAS REAL, "
                + "TOTALWATER REAL, "
                + "TOTALSPREAD REAL, "
                + "TOTALMEDIC REAL, "
                + "TOTALCHICKS REAL, "
                + "TOTALLABOR REAL, "
                + "TOTALFEED REAL, "
                + "CURRENCY VARCHAR(10),"
                + "PRIMARY KEY (FLOCKID)"
                + ")";
        jdbcTemplate.execute(sql);
    }

    public void createFlockHistory() throws SQLException {
        String sql = "CREATE TABLE FLOCKHISTORY "
                + "("
                + "FLOCKID INT NOT NULL , "
                + "GROWDAY INT NOT NULL , "
                + "HISTORYDATA VARCHAR(1500) NOT NULL, "
                + "PRIMARY KEY (FLOCKID, GROWDAY)"
                + ")";
        jdbcTemplate.execute(sql);
    }

    public void createFlockHistory24() throws SQLException {
        String sql = "CREATE TABLE FLOCKHISTORY24 "
                + "("
                + "FLOCKID INT NOT NULL , "
                + "GROWDAY INT NOT NULL ,"
                + "DNUM VARCHAR(5) NOT NULL,"
                + "HISTORYDATA VARCHAR(1500) NOT NULL,"
                + "PRIMARY KEY (FLOCKID, GROWDAY, DNUM)"
                + ")";
        jdbcTemplate.execute(sql);
    }

    public void createHistoryN() throws SQLException {
        String sql = "CREATE TABLE HISTORYN "
                + "("
                + "DN VARCHAR(5) NOT NULL, "
                + "FULLNAME VARCHAR(50) NOT NULL , "
                + "FORMAT INT NOT NULL, "
                + "STANDARD INT NOT NULL, "
                + "NAME VARCHAR(45) NOT NULL, "
                + "PRIMARY KEY (DN)"
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.FLOCKS ";
        jdbcTemplate.execute(sql);

        sql = "DROP TABLE APP.FLOCKHISTORY ";
        jdbcTemplate.execute(sql);

        sql = "DROP TABLE APP.FLOCKHISTORY24 ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void updateHistoryByGrowDay(Long flockId, Integer growDay, String values) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(HISTORYDATA) AS EXIST FROM FLOCKHISTORY WHERE FLOCKID=? AND GROWDAY=?";
        String sqlUpdateQuery = "UPDATE FLOCKHISTORY SET HISTORYDATA=? WHERE FLOCKID=? AND GROWDAY=?";
        int exist = jdbcTemplate.queryForInt(sqlSelectQuery, flockId, growDay);
        if (exist == 1) {
            jdbcTemplate.update(sqlUpdateQuery, values, flockId, growDay);
        } else {
            SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcControllerDataInsert.setTableName("FLOCKHISTORY");
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            valuesToInsert.put("FLOCKID", flockId);
            valuesToInsert.put("GROWDAY", growDay);
            valuesToInsert.put("HISTORYDATA", values);
            jdbcControllerDataInsert.execute(valuesToInsert);
        }

    }

    @Override
    public void updateHistory24ByGrowDay(Long flockId, Integer growDay, String dnum, String values) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(HISTORYDATA) AS EXIST FROM FLOCKHISTORY24 WHERE FLOCKID=? AND GROWDAY=?" +
                " AND DNUM=? ";
        String sqlUpdateQuery = "UPDATE FLOCKHISTORY24 SET HISTORYDATA=? WHERE FLOCKID=? AND GROWDAY=? AND DNUM=? ";
        int exist = jdbcTemplate.queryForInt(sqlSelectQuery, flockId, growDay, dnum);
        if (exist == 1) {
            jdbcTemplate.update(sqlUpdateQuery, values, flockId, growDay, dnum);
        } else {
            SimpleJdbcInsert jdbcControllerDataInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcControllerDataInsert.setTableName("FLOCKHISTORY24");
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            valuesToInsert.put("FLOCKID", flockId);
            valuesToInsert.put("GROWDAY", growDay);
            valuesToInsert.put("DNUM", dnum);
            valuesToInsert.put("HISTORYDATA", values);
            jdbcControllerDataInsert.execute(valuesToInsert);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE FROM APP.FLOCK ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public Collection<Data> getFlockPerHourHistoryData(Long flockId, Integer growDay, Long langId) throws SQLException {
//        String sql = "select * from flockhistory24 as f24 " +
//                "left join datatable as dt on dt.historydnum like '%' || f24.DNUM || '%' " +
//                "left join databylanguage as dbl on dbl.dataid=dt.dataid and dbl.langid=? " +
//                "where f24.flockid=? and f24.growday=? and dt.historyopt like '%HOUR%' order by dt.type ";
        String sql = "select * from flockhistory24 " +
                "inner join datatable on datatable.historydnum=flockhistory24.dnum " +
                "left join databylanguage on databylanguage.dataid=datatable.dataid and databylanguage.langid=? where flockid=? and growday=?";
        return jdbcTemplate.query(sql, new Object[]{langId, flockId, growDay}, RowMappers.data());

    }
}
