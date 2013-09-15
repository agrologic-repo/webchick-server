package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.CellinkDaoImpl;
import com.agrologic.app.model.Cellink;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DerbyCellinkDaoImpl extends CellinkDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {
    public final static String APP_SCHEMA = "APP";
    public final static String ALARMNAMES_TABLE = "ALARMNAMES";
    public final static String ALARMBYLANGUAGE_TABLE = "ALARMBYLANGUAGE";

    public DerbyCellinkDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "CELLINKS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table Cellink from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE CELLINKS ( " + "CELLINKID INT NOT NULL,  " + "NAME VARCHAR(25) NOT NULL, "
                + "PASSWORD VARCHAR(25) NOT NULL, " + "USERID INT NOT NULL, "
                + "TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL, " + "PORT INT DEFAULT 0, "
                + "IP VARCHAR(16), " + "STATE INT DEFAULT 0 , " + "SCREENID INT DEFAULT 1 , "
                + "SIM VARCHAR(15), " + "ACTUAL SMALLINT DEFAULT 0 , " + "TYPE VARCHAR(45) , "
                + "VERSION VARCHAR(45) , " + "PRIMARY KEY (CELLINKID), "
                + "FOREIGN KEY (USERID) REFERENCES USERS(USERID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new Cellink Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Cellink cellink) throws SQLException {
        logger.debug("Creating cellink with name [{}]", cellink.getName());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("cellinkid", cellink.getId());
        valuesToInsert.put("name", cellink.getName());
        valuesToInsert.put("password", cellink.getPassword());
        valuesToInsert.put("userid", cellink.getUserId());
        valuesToInsert.put("sim", cellink.getSimNumber());
        valuesToInsert.put("type", cellink.getType());
        valuesToInsert.put("version", cellink.getVersion());
        valuesToInsert.put("time", cellink.getTime());
        valuesToInsert.put("state", cellink.getState());
        valuesToInsert.put("screenid", cellink.getScreenId());
        valuesToInsert.put("actual", cellink.isActual());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.CELLINK ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table cellink ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE FROM APP.CELLINK ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table cellink ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



