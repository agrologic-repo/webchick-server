
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.dao.HistorySettingDao;
import com.agrologic.app.model.HistorySetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO : have to fix HistorySettingDao
public class HistorySettingDaoImpl implements HistorySettingDao {

    protected final Logger logger = LoggerFactory.getLogger(HistorySettingDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public HistorySettingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("historysetting");
    }

    private HistorySetting makeHistorySetting(final ResultSet rs) throws SQLException {
        HistorySetting histSetting = new HistorySetting();
        histSetting.setProgramId(rs.getLong("programID"));
        histSetting.setDataId(rs.getLong("DataID"));
        histSetting.setChecked(rs.getString("Checked"));
        return histSetting;
    }

    private List<HistorySetting> makeHistorySettingList(final ResultSet rs) throws SQLException {
        List<HistorySetting> historySettingList = new ArrayList<HistorySetting>();

        while (rs.next()) {
            historySettingList.add(makeHistorySetting(rs));
        }

        return historySettingList;
    }

    @Override
    public List<HistorySetting> getHistorySetting(Long programId) throws SQLException {
        String sqlQuery = "select * from historysetting where programId=?";
        return null;
        //jdbcTemplate.execute(sqlQuery);
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, programId);
//
//            ResultSet rs = prepstmt.executeQuery();
//
//            return makeHistorySettingList(rs);
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//
//            throw new SQLException("Cannot Retrieve Language ID From DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
    }

    @Override
    public void saveHistorySetting(List<HistorySetting> hsl) throws SQLException {
        String sqlQuery = "insert into historysetting" + " (ProgramID,DataID,Checked)"
                + " values (?,?,?) on duplicate key update Checked=values(Checked)";
        jdbcTemplate.execute(sqlQuery);
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            con.setAutoCommit(false);
//            prepstmt = con.prepareStatement(sqlQuery);
//
//            for (HistorySetting hs : hsl) {
//                prepstmt.setLong(1, hs.getProgramId());
//                prepstmt.setLong(2, hs.getDataId());
//                prepstmt.setString(3, hs.getChecked());
//                prepstmt.addBatch();
//            }
//
//            prepstmt.executeBatch();
//            con.commit();
//            con.setAutoCommit(true);
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//
//            throw new SQLException("Cannot Save Screens In DataBase", e);
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
    }

    @Override
    public List<HistorySetting> getSelectedHistorySetting(Long programId) throws SQLException {

        String sqlQuery = "select * from historysetting where programId=? and checked like '%true%'";
        return null;

//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, programId);
//            ResultSet rs = prepstmt.executeQuery();
//            return makeHistorySettingList(rs);
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//
//            throw new SQLException("Cannot Retrieve Language ID From DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
    }
}



