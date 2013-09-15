package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.UserDaoImpl;
import com.agrologic.app.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class DerbyUserDaoImpl extends UserDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyUserDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
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
        logger.debug("Creating user with id [{}]", user.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("userid", user.getId());
        valuesToInsert.put("name", user.getLogin());
        valuesToInsert.put("password", user.getPassword());
        valuesToInsert.put("firstname", user.getFirstName());
        valuesToInsert.put("lastname", user.getLastName());
        valuesToInsert.put("role", user.getRole().getValue());
        valuesToInsert.put("state", user.getState());
        valuesToInsert.put("phone", user.getPhone());
        valuesToInsert.put("email", user.getEmail());
        valuesToInsert.put("company", user.getCompany());
        jdbcInsert.execute(valuesToInsert);
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



