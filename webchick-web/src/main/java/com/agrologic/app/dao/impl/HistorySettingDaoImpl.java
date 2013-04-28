
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.HistorySettingDao;
import com.agrologic.app.model.HistorySettingDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class HistorySettingDaoImpl extends ConnectorDao implements HistorySettingDao {
    private HistorySettingDto makeHistorySetting(final ResultSet rs) throws SQLException {
        HistorySettingDto histSetting = new HistorySettingDto();

        histSetting.setProgramId(rs.getLong("programID"));
        histSetting.setDataId(rs.getLong("DataID"));
        histSetting.setChecked(rs.getString("Checked"));

        return histSetting;
    }

    private List<HistorySettingDto> makeHistorySettingList(final ResultSet rs) throws SQLException {
        List<HistorySettingDto> historySettingList = new ArrayList<HistorySettingDto>();

        while (rs.next()) {
            historySettingList.add(makeHistorySetting(rs));
        }

        return historySettingList;
    }

    @Override
    public List<HistorySettingDto> getHistorySetting(Long programId) throws SQLException {
        String            sqlQuery = "select * from historysetting where programId=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeHistorySettingList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Language ID From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void saveHistorySetting(List<HistorySettingDto> hsl) throws SQLException {
        String sqlQuery = "insert into historysetting" + " (ProgramID,DataID,Checked)"
                          + " values (?,?,?) on duplicate key update Checked=values(Checked)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            for (HistorySettingDto hs : hsl) {
                prepstmt.setLong(1, hs.getProgramId());
                prepstmt.setLong(2, hs.getDataId());
                prepstmt.setString(3, hs.getChecked());
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Save Screens In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<HistorySettingDto> getSelectedHistorySetting(Long programId) throws SQLException {
        String            sqlQuery = "select * from historysetting where programId=? and checked like '%true%'";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeHistorySettingList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Language ID From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



