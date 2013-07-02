package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.CellinkDaoImpl;
import com.agrologic.app.model.Cellink;

import java.sql.*;

public class DerbyCellinkDaoImpl extends CellinkDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyCellinkDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
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
        String sqlQuery = "INSERT INTO CELLINKS "
                + "(CELLINKID, NAME, PASSWORD, USERID, TIME, PORT, IP, STATE, SCREENID, ACTUAL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellink.getId());
            prepstmt.setString(2, cellink.getName());
            prepstmt.setString(3, cellink.getPassword());
            prepstmt.setLong(4, cellink.getUserId());
            prepstmt.setTimestamp(5, cellink.getTime());
            prepstmt.setInt(6, cellink.getPort());
            prepstmt.setString(7, cellink.getIp());
            prepstmt.setInt(8, cellink.getState());
            prepstmt.setLong(9, cellink.getScreenId());
            prepstmt.setBoolean(10, cellink.isActual());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Insert new Cellink to the DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
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



