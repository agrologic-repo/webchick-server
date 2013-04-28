
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.model.AlarmDto;



import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: AlarmDaoImpl <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class AlarmDaoImpl extends ConnectorDao implements AlarmDao {
    private AlarmDto makeAlarm(ResultSet rs) throws SQLException {
        AlarmDto alarm = new AlarmDto();

        alarm.setId(rs.getLong("ID"));
        alarm.setText(rs.getString("Name"));

        try {
            alarm.setUnicodeText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {
            alarm.setUnicodeText(rs.getString("Name"));
        }

        return alarm;
    }

    private List<AlarmDto> makeAlarmList(ResultSet rs) throws SQLException {
        List<AlarmDto> alarms = new ArrayList<AlarmDto>();

        while (rs.next()) {
            alarms.add(makeAlarm(rs));
        }

        return alarms;
    }

    @Override
    public void insert(AlarmDto alarm) throws SQLException {
        String            sqlQuery = "insert into alarmnames values (?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, alarm.getId());
            prepstmt.setString(2, alarm.getText());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Alarms To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(AlarmDto alarm) throws SQLException {
        String            sqlQuery = "update alarmnames set Name=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, alarm.getText());
            prepstmt.setLong(2, alarm.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Update Alarms In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from alarmnames where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve Alarms From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Long alarmId, Long langId, String translation) throws SQLException {
        String sqlQuery =
            "insert into alarmbylanguage values (?,?,?) on duplicate key update UnicodeName=values(UnicodeName)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, alarmId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert AlarmByLanguage To DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public AlarmDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from alarmnames where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeAlarm(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Alarms From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<AlarmDto> getAll() throws SQLException {
        String     sqlQuery = "select * from alarmnames order by ID,Name";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeAlarmList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Alarms From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<AlarmDto> getAll(Long langId) throws SQLException {
        String sqlQuery = "select a1.id, a1.name, a2.alarmid, a2.langid, a2.unicodename from alarmnames a1 "
                          + "left join alarmbylanguage a2 on a1.id=a2.alarmid and langid=" + langId;
        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeAlarmList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve Alarms From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }
}



