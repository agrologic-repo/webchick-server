package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.FlockDaoImpl;
import com.agrologic.app.model.Flock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DerbyFlockDaoImpl extends FlockDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFlockDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FLOCKS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table FLOCK from DataBase", e);
        } finally {
            dao.closeConnection(con);
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
        String sqlQuery = "CREATE TABLE FLOCKS "
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


        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot create new Flocks Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createFlockHistory() throws SQLException {
        String sqlQuery = "CREATE TABLE FLOCKHISTORY "
                + "("
                + "FLOCKID INT NOT NULL , "
                + "GROWDAY INT NOT NULL , "
                + "HISTORYDATA VARCHAR(1500) NOT NULL, "
                + "PRIMARY KEY (FLOCKID, GROWDAY)"
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot create new FlockHistory Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createFlockHistory24() throws SQLException {
        String sqlQuery = "CREATE TABLE FLOCKHISTORY24 "
                + "("
                + "FLOCKID INT NOT NULL , "
                + "GROWDAY INT NOT NULL ,"
                + "DNUM VARCHAR(5) NOT NULL,"
                + "HISTORYDATA VARCHAR(1500) NOT NULL,"
                + "PRIMARY KEY (FLOCKID, GROWDAY, DNUM)"
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot create new FlockHistory Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createHistoryN() throws SQLException {

        String sqlQuery = "CREATE TABLE HISTORYN "
                + "("
                + "DN VARCHAR(5) NOT NULL, "
                + "FULLNAME VARCHAR(50) NOT NULL , "
                + "FORMAT INT NOT NULL, "
                + "STANDARD INT NOT NULL, "
                + "NAME VARCHAR(45) NOT NULL, "
                + "PRIMARY KEY (DN)"
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot create new HistoryN Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.FLOCKS ";
        String sqlQueryFlockHistory = "DROP TABLE APP.FLOCKHISTORY ";
        String sqlQueryFlockHistory24 = "DROP TABLE APP.FLOCKHISTORY24 ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
            stmt.executeUpdate(sqlQueryFlockHistory);
            stmt.executeUpdate(sqlQueryFlockHistory24);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table flock ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public boolean isSchemaExist() throws SQLException {
        String sqlQuery = "SELECT APP FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = agrodb";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FLOCKS", null);
            if (!rs.next()) {
                return false;
            } else {
                String schema = rs.getString("TABLE_SCHEM");
                return true;
            }


        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot create new Schema Table ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
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
        valuesToInsert.put("startdate", flock.getEndTime());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void updateHistoryByGrowDay(Long flockId, Integer growDay, String values) throws SQLException {
        String sqlQuery = "INSERT INTO FLOCKHISTORY (FLOCKID, GROWDAY, HISTORYDATA) VALUES (?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setInt(2, growDay);
            prepstmt.setString(3, values);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Update flock management error ", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateHistory24ByGrowDay(Long flockId, Integer growDay, String dnum, String values) throws SQLException {
        String sqlQuery = "INSERT INTO FLOCKHISTORY24 (FLOCKID, GROWDAY, DNUM, HISTORYDATA) VALUES (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setInt(2, growDay);
            prepstmt.setString(3, dnum);
            prepstmt.setString(4, values);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            if (!e.getSQLState().equals("23505")) {//Found duplicate from database view
                dao.printSQLException(e);
                throw new SQLException("Update flock management 24 hours error.", e);
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE FROM APP.FLOCK ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table flock ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}
