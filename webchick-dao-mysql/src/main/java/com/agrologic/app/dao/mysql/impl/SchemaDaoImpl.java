package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.SchemaDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaDaoImpl extends SchemaDao {
    protected DaoFactory dao;

    public SchemaDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    @Override
    public void createSchema(String schema) throws SQLException {
        String sqlQuery = "CREATE SCHEMA " + schema + " ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot create schema " + schema, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropSchema(String schema) throws SQLException {
        String sqlQuery = "DROP SCHEMA ? RESTRICT";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, schema);
            prepstmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot drop schema " + schema, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
