package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;

import com.agrologic.app.model.Cellink;

import com.agrologic.app.util.CellinkUtil;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.Collection;

/**
 * Title: CellinkDaoImpl - Encapsulate all SQL queries to database that are
 * related to farm <br> Description: Contains 3 types of SQL methods:<ul>
 * <li>regular jdbc statements</li> <li>prepared statements<br></li></ul>
 * Copyright: Copyright (c) 2008 <br>
 *
 * @version 1.0 <br>
 */
public class CellinkDaoImpl implements CellinkDao {

    protected DaoFactory dao;

    public CellinkDaoImpl(DaoFactory dao) {
        this.dao = dao;
    }

    @Override
    public void insert(Cellink cellink) throws SQLException {
        String sqlQuery = "insert into cellinks (Name, Password, UserID, SIM, Type, Version, State, ScreenID, Actual) "
                + "values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, cellink.getName());
            prepstmt.setString(2, cellink.getPassword());
            prepstmt.setLong(3, cellink.getUserId());
            prepstmt.setString(4, cellink.getSimNumber());
            prepstmt.setString(5, cellink.getType());
            prepstmt.setString(6, cellink.getVersion());
            prepstmt.setInt(7, cellink.getState());
            prepstmt.setLong(8, cellink.getScreenId());
            prepstmt.setBoolean(9, cellink.isActual());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert new Cellink to the DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void insert(Collection<Cellink> cellinks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Cellink cellink) throws SQLException {
        String sqlQuery = "update cellinks set Name=?, Password=?, UserID=?, Time=?,"
                + "Port=?, Ip=? ,State=?, Version=? , Actual=? " + " where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, cellink.getName());
            prepstmt.setString(2, cellink.getPassword());
            prepstmt.setLong(3, cellink.getUserId());
            prepstmt.setTimestamp(4, cellink.getTime());
            prepstmt.setInt(5, cellink.getPort());
            prepstmt.setString(6, cellink.getIp());
            prepstmt.setInt(7, cellink.getState());
            prepstmt.setString(8, cellink.getVersion());
            prepstmt.setBoolean(9, cellink.isActual());
            prepstmt.setLong(10, cellink.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Update Cellink In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from cellink where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Delete User From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Cellink getById(Long id) throws SQLException {
        String sqlQuery = "select * from cellinks where CellinkID=" + id;
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return CellinkUtil.makeCellink(rs);
            } else {
                return new Cellink();
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Farms From DataBase");
        } finally {
            try {
                stmt.close();
                dao.closeConnection(con);
            } catch (SQLException ex) {
                throw new SQLException("close statment error.");
            }
        }
    }

    @Override
    public Cellink validate(String name, String password) throws SQLException {
        String sqlQuery = "select * from cellinks where Name = ? and Password = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, name);
            prepstmt.setString(2, password);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return CellinkUtil.makeCellink(rs);
            } else {
                Cellink cellink = new Cellink();

                cellink.setName(name);
                cellink.setPassword(password);

                return cellink;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot validate users From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Cellink getActualCellink() throws SQLException {
        String sqlQuery = "select * from cellinks where actual=1";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return CellinkUtil.makeCellink(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Actual Cellink From DataBase.");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Cellink> getAll() throws SQLException {
        String sqlQuery = "select * from cellinks";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return CellinkUtil.makeCellinkList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Cellink From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Cellink> getAllUserCellinks(Long userID) throws SQLException {
        String sqlQuery = "select * from cellinks where UserID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userID);

            ResultSet rs = prepstmt.executeQuery();

            return CellinkUtil.makeCellinkList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Farms From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public int getState(Long id) throws SQLException {
        return getById(id).getState();
    }

    @Override
    public Timestamp getUpdatedTime(Long id) throws SQLException {
        return getById(id).getTime();
    }

    @Override
    public int count() throws SQLException {
        String sqlQuery = "select count(*) as count from cellinks";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt("count");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Count Programs From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
