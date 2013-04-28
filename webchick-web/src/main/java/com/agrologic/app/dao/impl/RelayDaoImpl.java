
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.RelayDao;
import com.agrologic.app.model.RelayDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: RelayDaoImpl <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class RelayDaoImpl extends ConnectorDao implements RelayDao {
    private RelayDto makeRelay(ResultSet rs) throws SQLException {
        RelayDto relay = new RelayDto();

        relay.setId(rs.getLong("ID"));
        relay.setText(rs.getString("Name"));

        try {
            relay.setUnicodeText(rs.getString("UnicodeText"));
        } catch (SQLException ex) {
            relay.setUnicodeText(rs.getString("Name"));
        }

        return relay;
    }

    private List<RelayDto> makeRelayList(ResultSet rs) throws SQLException {
        List<RelayDto> relays = new ArrayList<RelayDto>();

        while (rs.next()) {
            relays.add(makeRelay(rs));
        }

        return relays;
    }

    @Override
    public void insert(RelayDto relay) throws SQLException {
        String            sqlQuery = "insert into relaynames values (?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, relay.getId());
            prepstmt.setString(2, relay.getText());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Relay To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Long relayId, Long langId, String translation) throws SQLException {
        String sqlQuery =
            "insert into relaybylanguage values (?,?,?) on duplicate key update UnicodeText=values(UnicodeText)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, relayId);
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
    public void update(RelayDto relay) throws SQLException {
        String            sqlQuery = "update relaynames set Name=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, relay.getText());
            prepstmt.setLong(2, relay.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Update Relay In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from relaynames where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve RelayNames From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public RelayDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from relaynames where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeRelay(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve RelayNames From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<RelayDto> getAll() throws SQLException {
        String     sqlQuery = "select * from relaynames order by ID,Name";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeRelayList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<RelayDto> getAll(Long langId) throws SQLException {
        String sqlQuery = "select r1.id, r1.name, r2.relayid, r2.langid, r2.unicodetext from relaynames r1 "
                          + "left join relaybylanguage r2 on r1.id=r2.relayid and langid=" + langId;
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeRelayList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve Relay From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }
}



