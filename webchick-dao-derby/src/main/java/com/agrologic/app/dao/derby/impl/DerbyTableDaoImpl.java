package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.TableDaoImpl;

import com.agrologic.app.model.Table;



import java.sql.*;

public class DerbyTableDaoImpl extends TableDaoImpl implements CreatebleDao, DropableDao, RemovebleDao  {

    public DerbyTableDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet        rs   = dbmd.getTables(null, "APP", "SCREENTABLE", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table SCREENTABLE from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableScreenTable();
        createTableScreenTableByLanguage();
    }

    private void createTableScreenTable() throws SQLException {
        String sqlQuery = "CREATE TABLE SCREENTABLE " + "(TABLEID INT NOT NULL , " + "SCREENID INT NOT NULL , "
                          + "PROGRAMID INT NOT NULL , " + "TITLE VARCHAR(250) NOT NULL, "
                          + "DISPLAYONSCREEN VARCHAR(10) NOT NULL, " + "POSITION INT NOT NULL, "
                          + "PRIMARY KEY (TABLEID,SCREENID,PROGRAMID), "
                          + "FOREIGN KEY (SCREENID,PROGRAMID) REFERENCES SCREENS(SCREENID,PROGRAMID))";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot create new SCREENTABLE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createTableScreenTableByLanguage() throws SQLException {
        String sqlQuery = "CREATE TABLE TABLEBYLANGUAGE " + "(TABLEID INT NOT NULL , " + "LANGID INT NOT NULL , "
                          + "UNICODETITLE VARCHAR(500) NOT NULL, " + "PRIMARY KEY (TABLEID, LANGID))";
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot create new TABLEBYLANGUAGE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Table table) throws SQLException {
        String            sqlQuery = "insert into screentable values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, table.getId());
            prepstmt.setLong(2, table.getScreenId());
            prepstmt.setLong(3, table.getProgramId());
            prepstmt.setString(4, table.getTitle());
            prepstmt.setString(5, table.getDisplay());
            prepstmt.setInt(6, table.getPosition());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert ScreenTable To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.SCREENTABLE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table screentable ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.SCREENTABLE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table screentable ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



