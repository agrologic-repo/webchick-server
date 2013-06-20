package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramRelayDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyProgramRelayDaoImpl extends ProgramRelayDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramRelayDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMRELAYS", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMRELAYS from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableRelayByProgram();
    }

    private void createTableRelayByProgram() throws SQLException {
        String sqlQuery = "CREATE TABLE PROGRAMRELAYS " + "(" + "DATAID INT NOT NULL , " + "BITNUMBER INT NOT NULL , "
                + "TEXT VARCHAR(200) NOT NULL, " + "PROGRAMID INT NOT NULL , "
                + "RELAYNUMBER INT NOT NULL , " + "RELAYTEXTID INT NOT NULL , "
                + "PRIMARY KEY (DATAID,BITNUMBER,PROGRAMID)" + ")";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new PROGRAMRELAYS Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.PROGRAMRELAYS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table relay ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.PROGRAMRELAYS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table relay ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



