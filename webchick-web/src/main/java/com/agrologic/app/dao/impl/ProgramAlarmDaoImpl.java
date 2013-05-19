
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.ProgramAlarmDao;
import com.agrologic.app.model.ProgramAlarm;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Title: ProgramAlarmDaoImpl <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class ProgramAlarmDaoImpl extends ConnectorDao implements ProgramAlarmDao {
    private ProgramAlarm makeProgramAlarm(ResultSet rs) throws SQLException {
        ProgramAlarm programAlarm = new ProgramAlarm();

        programAlarm.setDataId(rs.getLong("DataID"));
        programAlarm.setDigitNumber(rs.getInt("DigitNumber"));
        programAlarm.setText(rs.getString("Text"));
        programAlarm.setProgramId(rs.getLong("ProgramID"));

        try {
            programAlarm.setText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {    /* ignore */
        }

        return programAlarm;
    }

    private List<ProgramAlarm> makeProgramAlarmList(ResultSet rs) throws SQLException {
        List<ProgramAlarm> programAlarms = new ArrayList<ProgramAlarm>();

        while (rs.next()) {
            programAlarms.add(makeProgramAlarm(rs));
        }

        return programAlarms;
    }

    @Override
    public void insert(ProgramAlarm programAlarm) throws SQLException {
        String            sqlQuery = "insert into programalrams values (?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setInt(2, programAlarm.getDigitNumber());
            prepstmt.setString(3, programAlarm.getText());
            prepstmt.setLong(4, programAlarm.getProgramId());
            prepstmt.setLong(5, programAlarm.getAlarmTextId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Insert ProgramRelay To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(ProgramAlarm programAlarm) throws SQLException {
        String sqlQuery =
            "update programrelays set Text=?, alarmTextID=? where DataID=? and DigitNumber=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, programAlarm.getText());
            prepstmt.setLong(2, programAlarm.getAlarmTextId());
            prepstmt.setLong(3, programAlarm.getDataId());
            prepstmt.setInt(4, programAlarm.getDigitNumber());
            prepstmt.setLong(5, programAlarm.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Update ProgramRelays In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long dataId, Integer digitNumber, Long programId) throws SQLException {
        String            sqlQuery = "delete from programrelays where DataId=? and DigitNumber=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setInt(2, digitNumber);
            prepstmt.setLong(3, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Delete ProgramRelays From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertAlarms(Long programId, Map<Long, Map<Integer, String>> alarmMap) throws SQLException {
        String sqlQuery = "insert into programalarms" + " (DataID,DigitNumber,Text,ProgramID,AlarmNumber,AlarmTextID)"
                          + " values (?,?,?,?,?,?) on duplicate key update Text=values(Text),"
                          + " AlarmTextID=values(AlarmTextID)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> dataIdList  = alarmMap.keySet();
            final int digitsBits  = 10;
            int       relayIndex  = 0,
                      alarmNumber = 0;

            for (Long dataId : dataIdList) {
                Map<Integer, String> alarmDigitMaps = alarmMap.get(dataId);
                Set<Integer>         digits         = alarmDigitMaps.keySet();

                for (Integer digit : digits) {
                    String             alarmText = alarmDigitMaps.get(digit);
                    Pair<Long, String> pair      = parseAlarmText(alarmText);

                    alarmNumber = relayIndex * digitsBits + digit;
                    prepstmt.setLong(1, dataId);
                    prepstmt.setInt(2, digit);
                    prepstmt.setString(3, pair.getText());
                    prepstmt.setLong(4, programId);
                    prepstmt.setLong(5, alarmNumber);
                    prepstmt.setLong(6, pair.getId());
                    prepstmt.addBatch();
                }

                relayIndex++;
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Insert ProgramDto To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException {
        String            sqlQuery = "select * from programalarms where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramAlarmList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramAlarm> getAllProgramAlarms(Long programId, Long langId) throws SQLException {
        String sqlQuery =
            "select * from programalarms as progalarm "
            + " join (select AlarmID,LangID,UnicodeName from alarmbylanguage ) "
            + " as abl where abl.AlarmID = progalarm.AlarmTextID and abl.LangID=? and ProgramID=? order by DataID,DigitNumber";
        String[] strings = new String[] { "None", "Damy" };

        for (int i = 0; i < strings.length; i++) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + strings[i]
                       + "%' order by a.DataID,a.DigitNumber ";
        }

        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramAlarmList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramAlarm> getAllProgramAlarms(Long programId, String[] text) throws SQLException {
        String sqlQuery = "select * from programalarms where ProgramID=" + programId;

        if ((text != null) && (text.length > 0)) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                           + " order by a.DataID,a.DigitNumber ";
            }
        }

        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeProgramAlarmList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve ProgramAlarms From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    private Pair parseAlarmText(String alarmText) {
        int digIndx = alarmText.length() - 1;
        int len     = 2;

        while (!Character.isLetter(alarmText.charAt(digIndx)) && (len > 0)) {
            digIndx--;
            len--;
        }

        String text = alarmText.substring(0, digIndx + 1);
        String ids  = alarmText.substring(digIndx + 1);
        Long   id   = null;

        try {
            id = Long.parseLong(ids);
            id++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new Pair(id, text);
    }

    /**
     * Inner class for using to incapsulate alarm text and alarm id
     *
     * @param <Long>
     * @param <String>
     */
    static class Pair<Long, String> {
        Long   id;
        String text;

        Pair(Long id, String text) {
            this.id   = id;
            this.text = text;
        }

        public Long getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }
}



