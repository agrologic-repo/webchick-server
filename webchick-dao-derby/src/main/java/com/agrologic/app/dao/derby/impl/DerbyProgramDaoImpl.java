package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramDaoImpl;
import java.sql.*;

public class DerbyProgramDaoImpl extends ProgramDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;
        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet        rs   = dbmd.getTables(null, "APP", "PROGRAMS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table programs from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE PROGRAMS "
                + "(PROGRAMID INT NOT NULL , "
                + "NAME VARCHAR(45) NOT NULL, "
                + "CREATED VARCHAR(45) NOT NULL, "
                + "MODIFIED VARCHAR(45) NOT NULL, "
                + "PRIMARY KEY (PROGRAMID))";
        Statement  stmt = null;
        Connection con  = null;
        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new PROGRAMS Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.PROGRAMS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table program ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE FROM APP.PROGRAMS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot remove content of table program ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}

