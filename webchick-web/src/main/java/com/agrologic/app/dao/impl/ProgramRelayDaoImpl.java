
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.ProgramRelayDao;
import com.agrologic.app.model.ProgramRelayDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Title: ProgramRelayDaoImpl <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class ProgramRelayDaoImpl extends ConnectorDao implements ProgramRelayDao {
    private ProgramRelayDto makeProgramRelay(ResultSet rs) throws SQLException {
        ProgramRelayDto programRelay = new ProgramRelayDto();

        programRelay.setDataId(rs.getLong("DataID"));
        programRelay.setBitNumber(rs.getInt("BitNumber"));
        programRelay.setText(rs.getString("Text"));
        programRelay.setProgramId(rs.getLong("ProgramID"));
        programRelay.setRelayNumber(rs.getInt("RelayNumber"));

        try {
            programRelay.setUnicodeText(rs.getString("UnicodeText"));
        } catch (SQLException ex) {    /* ignore */
            programRelay.setUnicodeText(rs.getString("Text"));
        }

        return programRelay;
    }

    private List<ProgramRelayDto> makeProgramRelayList(ResultSet rs) throws SQLException {
        List<ProgramRelayDto> programRelays = new ArrayList<ProgramRelayDto>();

        while (rs.next()) {
            programRelays.add(makeProgramRelay(rs));
        }

        return programRelays;
    }

    @Override
    public void insert(ProgramRelayDto programRelay) throws SQLException {
        String            sqlQuery = "insert into programrelays values (?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setInt(2, programRelay.getBitNumber());
            prepstmt.setString(3, programRelay.getText());
            prepstmt.setInt(4, programRelay.getIndex());
            prepstmt.setLong(5, programRelay.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Insert ProgramRelayDto To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(ProgramRelayDto programRelay) throws SQLException {
        String            sqlQuery = "update programrelays set Text=? Index=? where DataID=? and BitNumber=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, programRelay.getText());
            prepstmt.setInt(2, programRelay.getIndex());
            prepstmt.setLong(3, programRelay.getDataId());
            prepstmt.setInt(4, programRelay.getBitNumber());
            prepstmt.setLong(5, programRelay.getProgramId());
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
    public void remove(Long dataId, Integer bitNumber, Long programId) throws SQLException {
        String            sqlQuery = "delete from programrelays where DataId=? and BitNumber=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setInt(2, bitNumber);
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
    public void insertRelays(Long programId, Map<Long, Map<Integer, String>> dataRelayMap) throws SQLException {
        String sqlQuery =
            "insert into programrelays (DataID,BitNumber,Text,ProgramID,RelayNumber, RelayTextID) values (?,?,?,?,?,?) on duplicate key update Text=values(Text),RelayTextID=values(RelayTextID),RelayNumber=values(RelayNumber)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> dataIdList  = dataRelayMap.keySet();
            final int maxBits     = 16;
            int       relayIndex  = 0,
                      relayNumber = 0;

            for (Long dataId : dataIdList) {
                Map<Integer, String> relayBitMaps = dataRelayMap.get(dataId);
                Set<Integer>         bits         = relayBitMaps.keySet();

                for (Integer bit : bits) {
                    String             relayText = relayBitMaps.get(bit);
                    Pair<Long, String> pair      = parseRelayText(relayText);

                    relayNumber = relayIndex * maxBits + bit;
                    prepstmt.setLong(1, dataId);
                    prepstmt.setInt(2, bit);
                    prepstmt.setString(3, pair.getText());
                    prepstmt.setLong(4, programId);
                    prepstmt.setLong(5, relayNumber);
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
    public List<ProgramRelayDto> getAllProgramRelays(Long programId) throws SQLException {
        String sqlQuery =
            "select * from programrelays where ProgramID=? and TEXT not Like '%None%' and Text not Like '%Damy%' order by DataID,BitNumber";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramRelayList(rs);
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
    public List<ProgramRelayDto> getAllProgramRelays(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from programrelays as progrelay "
                          + " join (select RelayID,LangID,UnicodeText from relaybylanguage  ) "
                          + " as rbl where rbl.RelayID = progrelay.RelayTextID and rbl.LangID=? and ProgramID=? "
                          + " and Text not Like '%None%' and Text not Like '%Damy%' order by DataID,BitNumber";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramRelayList(rs);
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
    public List<Long> getRelayNumberTypes(Long programId) throws SQLException {
        String            sqlQuery = "select distinct DataID FROM programrelays where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet  rs         = prepstmt.executeQuery();
            List<Long> relayTypes = new ArrayList<Long>();

            while (rs.next()) {
                relayTypes.add(rs.getLong("DataID"));
            }

            return relayTypes;
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
    public List<ProgramRelayDto> getAllProgramRelays(Long programId, String[] text) throws SQLException {
        String sqlQuery = "select * from programrelays where ProgramID=" + programId;

        if ((text != null) && (text.length > 0)) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                           + " order by a.DataID,a.BitNumber ";
            }
        }

        Statement  stmt = null;
        Connection con  = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeProgramRelayList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    private Pair parseRelayText(String relayText) {
        StringTokenizer token = new StringTokenizer(relayText, "-");
        String          text  = token.nextToken();
        String          ids   = token.nextToken();
        Long            id    = null;

        try {
            id = Long.parseLong(ids);
        } catch (Exception ex) {}

        return new Pair(id, text);
    }

    /**
     * Inner class for using to incapsulate relay text and relay id
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



