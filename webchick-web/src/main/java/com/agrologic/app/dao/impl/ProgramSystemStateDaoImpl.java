
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.ProgSysStateDao;
import com.agrologic.app.model.ProgramSystemStateDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;

/**
 * Title: ProgramSystemStateDaoImpl <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class ProgramSystemStateDaoImpl extends ConnectorDao implements ProgSysStateDao {
    private ProgramSystemStateDto makeProgramSystemState(ResultSet rs) throws SQLException {
        ProgramSystemStateDto programSystemState = new ProgramSystemStateDto();

        programSystemState.setDataId(rs.getLong("DataID"));
        programSystemState.setNumber(rs.getInt("Number"));
        programSystemState.setText(rs.getString("Text"));
        programSystemState.setProgramId(rs.getLong("ProgramID"));
        programSystemState.setSystemStateNumber(rs.getInt("SystemStateNumber"));

        try {
            programSystemState.setText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {    /* ignore */
        }

        return programSystemState;
    }

    private List<ProgramSystemStateDto> makeProgramSystemStateList(ResultSet rs) throws SQLException {
        List<ProgramSystemStateDto> programAlarms = new ArrayList<ProgramSystemStateDto>();

        while (rs.next()) {
            programAlarms.add(makeProgramSystemState(rs));
        }

        return programAlarms;
    }

    @Override
    public void insert(ProgramSystemStateDto programSystemState) throws SQLException {
        String            sqlQuery = "insert into programsysstates values (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setInt(2, programSystemState.getNumber());
            prepstmt.setString(3, programSystemState.getText());
            prepstmt.setLong(4, programSystemState.getProgramId());
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
    public void update(ProgramSystemStateDto programSystemState) throws SQLException {
        String            sqlQuery = "update programsysstates set Text=? where DataID=? and Number=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, programSystemState.getText());
            prepstmt.setLong(2, programSystemState.getDataId());
            prepstmt.setInt(3, programSystemState.getNumber());
            prepstmt.setLong(4, programSystemState.getProgramId());
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
    public void remove(Long dataId, Integer number, Long programId) throws SQLException {
        String            sqlQuery = "delete from programsysstates where DataId=? and Number=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setInt(2, number);
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
    public List<ProgramSystemStateDto> getAllProgramSystemStates(Long programId) throws SQLException {
        String            sqlQuery = "select * from programsysstates where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramSystemStateList(rs);
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
    public List<ProgramSystemStateDto> getAllProgramSystemStates(Long programId, Long langId) throws SQLException {
        String sqlQuery =
            "select * from programsysstates as progstates "
            + " join (select SystemStateID,LangID,UnicodeName from systemstatebylanguage ) "
            + " as sbl where sbl.SystemStateID = progstates.SystemStateTextID and sbl.LangID=? and ProgramID=? order by DataID,Number";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramSystemStateList(rs);
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
    public List<ProgramSystemStateDto> getAllProgramSystemStates(Long programId, String[] text) throws SQLException {
        String sqlQuery = "select * from programsysstates where ProgramID=" + programId;

        if (text != null) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                           + " order by a.DataID,a.Number ";
            }
        }

        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeProgramSystemStateList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertSystemStates(Long programId, SortedMap<Long, Map<Integer, String>> systemStateMap)
            throws SQLException {
        String sqlQuery =
            "insert into programsysstates (DataID,Number,Text,ProgramID,SystemStateNumber,SystemStateTextID) values (?,?,?,?,?,?) on duplicate key update Text=values(Text),SystemStateTextID=values(SystemStateTextID),SystemStateNumber=values(SystemStateNumber)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> dataIdList  = systemStateMap.keySet();
            final int maxNumbers  = 10;
            int       relayIndex  = 0,
                      stateNumber = 0;

            for (Long dataId : dataIdList) {
                Map<Integer, String> systemStateNumberMaps = systemStateMap.get(dataId);
                Set<Integer>         numbers               = systemStateNumberMaps.keySet();

                for (Integer number : numbers) {
                    String             stateText = systemStateNumberMaps.get(number);
                    Pair<Long, String> pair      = parseSystemStateText(stateText);

                    stateNumber = relayIndex * maxNumbers + number;
                    prepstmt.setLong(1, dataId);
                    prepstmt.setInt(2, number);
                    prepstmt.setString(3, pair.getText());
                    prepstmt.setLong(4, programId);
                    prepstmt.setLong(5, stateNumber);
                    prepstmt.setLong(6, pair.getId());
                    prepstmt.addBatch();
                }

                relayIndex++;
            }

            int[] executeBatch = prepstmt.executeBatch();

            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Insert ProgramDto To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    private Pair parseSystemStateText(String relayText) {
        StringTokenizer token = new StringTokenizer(relayText, "-");
        String          text  = token.nextToken();
        String          ids   = token.nextToken();

//      int digIndx = relayText.length()-1;
//      int len = 2;
//      while(!Character.isLetter(relayText.charAt(digIndx)) && len > 0) {
//          digIndx--;
//          len--;
//      }
//      String text = relayText.substring(0, digIndx+1);
//      String ids  = relayText.substring(digIndx+1);
        Long id = null;

        try {
            id = Long.parseLong(ids);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new Pair(id, text);
    }

    /**
     * Inner class for using to incapsulate relay text and relay id
     *
     * @param <Long>
     * @param <String>
     */
    class Pair<Long, String> {
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



