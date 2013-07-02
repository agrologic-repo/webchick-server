package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.UserDaoImpl;
import com.agrologic.app.model.User;

import java.sql.*;


public class DerbyUserDaoImpl extends UserDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyUserDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "USERS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table Users from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE USERS " + "(USERID INT NOT NULL , " + "NAME VARCHAR(45) NOT NULL, "
                + "PASSWORD VARCHAR(45) NOT NULL, " + "FIRSTNAME VARCHAR(45) NOT NULL, "
                + "LASTNAME VARCHAR(45) NOT NULL, " + "ROLE INTEGER NOT NULL, " + "STATE SMALLINT NOT NULL, "
                + "PHONE VARCHAR(45) NOT NULL, " + "EMAIL VARCHAR(45) NOT NULL, "
                + "COMPANY VARCHAR(45) NOT NULL, " + "PRIMARY KEY (USERID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new User Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(User user) throws SQLException {
        String sqlQuery =
                "INSERT INTO USERS (USERID, NAME, PASSWORD, FIRSTNAME, LASTNAME, ROLE, STATE, PHONE, EMAIL, COMPANY)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, user.getId());
            prepstmt.setString(2, user.getLogin());
            prepstmt.setString(3, user.getPassword());
            prepstmt.setString(4, user.getFirstName());
            prepstmt.setString(5, user.getLastName());
            prepstmt.setInt(6, user.getUserRole().getValue());
            prepstmt.setInt(7, user.getState());
            prepstmt.setString(8, user.getPhone());
            prepstmt.setString(9, user.getEmail());
            prepstmt.setString(10, user.getCompany());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert New User To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.USERS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table users ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.USERS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table users ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



