package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ProgramSystemStateDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyProgramSystemStateDaoImpl extends ProgramSystemStateDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyProgramSystemStateDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "PROGRAMSYSSTATES", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table PROGRAMSYSSTATES from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableSystemStateByProgram();
    }

    private void createTableSystemStateByProgram() throws SQLException {
        String sqlQuery = "CREATE TABLE PROGRAMSYSSTATES " + "(" + "DATAID INT NOT NULL , " + "NUMBER INT NOT NULL , "
                + "TEXT VARCHAR(200) NOT NULL, " + "PROGRAMID INT NOT NULL , "
                + "SYSTEMSTATENUMBER INT NOT NULL , " + "SYSTEMSTATETEXTID INT NOT NULL , "
                + "PRIMARY KEY (DATAID,NUMBER,PROGRAMID)" + ")";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new PROGRAMSYSSTATES Table", e);
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



