package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.FeedDaoImpl;
import com.agrologic.app.model.Feed;
import java.sql.*;

public class DerbyFeedDaoImpl extends FeedDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {



    public DerbyFeedDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FEED", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table FEED from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE FEED "
                + "( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL , "
                + "TYPE VARCHAR(45) , "
                + "AMOUNT INT NOT NULL, "
                + "DATE VARCHAR(20),"
                + "ACCOUNTNUMBER INT ,"
                + "TOTAL DOUBLE"
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new FEED Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
        }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.FEED ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table feed ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Feed feed) throws SQLException {
        String sqlQuery = "INSERT INTO FEED ( FLOCKID, TYPE, AMOUNT, DATE, ACCOUNTNUMBER, TOTAL ) "
                + " VALUES (?,?,?,?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, feed.getFlockId());
            prepstmt.setLong(2, feed.getType());
            prepstmt.setInt(3, feed.getAmount());
            prepstmt.setString(4, feed.getDate());
            prepstmt.setFloat(5, feed.getNumberAccount());
            prepstmt.setFloat(6, feed.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Feed To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.FEED ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table feed ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }

    }
}



