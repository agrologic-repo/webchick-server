
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.model.SystemStateDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: SystemStateDaoImpl <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class SystemStateDaoImpl extends ConnectorDao implements SystemStateDao {
    private SystemStateDto makeSystemState(ResultSet rs) throws SQLException {
        SystemStateDto systemState = new SystemStateDto();

        systemState.setId(rs.getLong("ID"));
        systemState.setText(rs.getString("Name"));

        try {
            systemState.setUnicodeText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {
            systemState.setUnicodeText(rs.getString("Name"));
        }

        return systemState;
    }

    private List<SystemStateDto> makeRelayList(ResultSet rs) throws SQLException {
        List<SystemStateDto> systemStates = new ArrayList<SystemStateDto>();

        while (rs.next()) {
            systemStates.add(makeSystemState(rs));
        }

        return systemStates;
    }

    @Override
    public void insert(SystemStateDto systemstate) throws SQLException {
        String            sqlQuery = "insert into systemstatenames values (?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, systemstate.getId());
            prepstmt.setString(2, systemstate.getText());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert system state To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(SystemStateDto systemstate) throws SQLException {
        String            sqlQuery = "update systemstatenames set Name=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, systemstate.getText());
            prepstmt.setLong(2, systemstate.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Update system state In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from systemstatenames where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Remove system states From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Long systemstateId, Long langId, String translation) throws SQLException {
        String sqlQuery =
            "insert into systemstatebylanguage values (?,?,?) on duplicate key update UnicodeName=values(UnicodeName)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, systemstateId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Translation To DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public SystemStateDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from systemstatenames where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeSystemState(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<SystemStateDto> getAll() throws SQLException {
        String     sqlQuery = "select * from systemstatenames order by ID,Name";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeRelayList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<SystemStateDto> getAll(Long langId) throws SQLException {
        String sqlQuery =
            "select s1.id, s1.name, s2.systemstateid, s2.langid, s2.unicodename from systemstatenames s1 "
            + "left join systemstatebylanguage s2 on s1.id=s2.systemstateid and langid=" + langId;
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeRelayList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve SystemState From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }
}



