package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.ProgramAlarm;
import com.agrologic.app.dao.mappers.AlarmUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmDaoImpl implements AlarmDao {
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    protected final DaoFactory dao;

    public AlarmDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("alarmnames");
        this.dao = dao;
    }

    @Override
    public void insert(Alarm alarm) {
        logger.debug("Creating alarm with id [{}]", alarm.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("id", alarm.getId());
        valuesToInsert.put("name", alarm.getText());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void insert(Collection<Alarm> alarmList) throws SQLException {
        // there is duplicate alarm elements in alarmList we need only unique elements
        Collection<Alarm> uniqueAlarmList = AlarmUtil.getUniqueElements(alarmList);
        for (Alarm alarm : uniqueAlarmList) {
            insert(alarm);
        }
    }

    @Override
    public void insertTranslation(Long alarmId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into alarmbylanguage values (?,?,?) on duplicate key update UnicodeName=values(UnicodeName)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, alarmId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Alarm Translation To DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Collection<Alarm> alarmList) throws SQLException {
        String sqlQuery = "insert into alarmbylanguage values (?,?,?) ";
        // + "on duplicate key update UnicodeName=values(UnicodeName)";
        // INSERT INTO alarmbylanguage (?,?,UnicodeName) (SELECT '?' FROM alarmbylanguage WHERE UnicodeName = '?' HAVING count(*)=0)
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);
            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (Alarm alarm : alarmList) {
                prepstmt.setLong(1, alarm.getId());
                prepstmt.setLong(2, alarm.getLangId());
                prepstmt.setString(3, alarm.getUnicodeText());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch();    // Execute every 200 items.
                    System.out.print(".");
                }

                i++;
            }

            System.out.print("");
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException("Cannot Insert Alarm Name Translation. Transaction is being rolled back",
                            ex);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertProgramAlarms(Collection<ProgramAlarm> programAlarms) throws SQLException {
        String sqlQuery = "insert into programalarms values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            for (ProgramAlarm programAlarm : programAlarms) {
                prepstmt.setLong(1, programAlarm.getDataId());
                prepstmt.setInt(2, programAlarm.getDigitNumber());
                prepstmt.setString(3, programAlarm.getText());
                prepstmt.setLong(4, programAlarm.getProgramId());
                prepstmt.setInt(5, programAlarm.getDigitNumber());
                prepstmt.setLong(6, programAlarm.getAlarmTextId());
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);

                    throw new SQLException("Transaction is being rolled back", ex);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(Alarm alarm) throws SQLException {
        String sqlQuery = "update alarmnames set Name=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, alarm.getText());
            prepstmt.setLong(2, alarm.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Update Alarm In DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from alarmnames where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Remove Alarm From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Alarm getById(Long id) throws SQLException {
        String sqlQuery = "select * from alarmnames where ID=?";
        List<Alarm> alarms = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.alarm());
        if (alarms.isEmpty()) {
            return null;
        }
        return alarms.get(0);
    }

    @Override
    public Collection<Alarm> getAll() throws SQLException {
        String sqlQuery = "select * from alarmnames order by ID,Name";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return AlarmUtil.makeAlarmList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Alarms From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Alarm> getAll(Long langId) throws SQLException {
        String sqlQuery = "select a1.id, a1.name, a2.alarmid, a2.langid, a2.unicodename from alarmnames a1 "
                + "left join alarmbylanguage a2 on a1.id=a2.alarmid and langid=" + langId;
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return AlarmUtil.makeAlarmList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve All Alarms By Specified Language From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Alarm> getAllWithTranslation() throws SQLException {
        String sqlQuery = "select * from alarmnames "
                + "join alarmbylanguage on alarmnames.id=alarmbylanguage.alarmid "
                + "order by alarmbylanguage.langid , alarmbylanguage.alarmid ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);
            return AlarmUtil.makeAlarmList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Alarms With Translation From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException {
        String sqlQuery = "select * from programalarms where ProgramID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            return AlarmUtil.makeProgramAlarmList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramAlarm From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramAlarm> getSelectedProgramAlarms(Long programId) throws SQLException {
        String sqlQuery = "select * from programalarms where ProgramID=? and TEXT not Like '%None%' "
                + "and Text not Like '%Damy%' order by DataID,AlarmNumber";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            return AlarmUtil.makeProgramAlarmList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramAlarm From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramAlarm> getSelectedProgramAlarms(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from programalarms as pa inner join  alarmbylanguage abl on "
                + "abl.alarmid = pa.alarmtextid and abl.LangID=? and pa.programid=? and pa.text not Like '%None%' "
                + "and pa.text not Like '%Damy%' order by pa.dataid,pa.digitnumber";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            return AlarmUtil.makeProgramAlarmList(prepstmt.executeQuery());
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve ProgramAlarm From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);

        }
    }
}



