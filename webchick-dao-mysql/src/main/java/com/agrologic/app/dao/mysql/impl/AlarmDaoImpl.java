package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.ProgramAlarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class AlarmDaoImpl implements AlarmDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertTranslate;

    public AlarmDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("alarmnames");
        this.jdbcInsertTranslate = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsertTranslate.setTableName("alarmbylanguage");
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Alarm alarm) {
        logger.debug("Creating alarm with id [{}]", alarm.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("id", alarm.getId());
        valuesToInsert.put("name", alarm.getText());
        jdbcInsert.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Collection<Alarm> alarmList) {
        // there is duplicate alarm elements in alarmList we need only unique elements
        Collection<Alarm> alarmCollection = Util.getUniqueElements(alarmList);
        for (Alarm alarm : alarmCollection) {
            insert(alarm);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTranslation(Long alarmId, Long langId, String translation) {
        logger.debug("Creating alarm translation with id [{}] and language id [{}] ", alarmId, langId);
        String sqlQuery =
                "insert into alarmbylanguage values (?,?,?) on duplicate key update UnicodeName=values(UnicodeName)";
        jdbcTemplate.update(sqlQuery, new Object[]{alarmId, langId, translation});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTranslation(final Collection<Alarm> alarms) {
        List<Alarm> alarmList = new ArrayList(alarms);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Alarm alarm : alarmList) {
            Object[] values = new Object[]{
                    alarm.getId(),
                    alarm.getLangId(),
                    alarm.getUnicodeText()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("insert into alarmbylanguage values (?,?,?) ", batch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertProgramAlarms(Collection<ProgramAlarm> programAlarms) throws SQLException {
        List<ProgramAlarm> programAlarmList = new ArrayList(programAlarms);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (ProgramAlarm programAlarm : programAlarmList) {
            Object[] values = new Object[]{
                    programAlarm.getDataId(),
                    programAlarm.getDigitNumber(),
                    programAlarm.getText(),
                    programAlarm.getProgramId(),
                    programAlarm.getDigitNumber(),
                    programAlarm.getAlarmTextId()
            };
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("insert into programalarms values (?,?,?,?,?,?)", batch);
    }

    @Override
    public void update(Alarm alarm) throws SQLException {
        logger.debug("Update alarm with id [{}]", alarm.getId());
        String sqlQuery = "update alarmnames set Name=? where ID=?";
        jdbcTemplate.update(sqlQuery, new Object[]{alarm.getText(), alarm.getId()});
    }

    @Override
    public void remove(Long id) {
        logger.debug("Delete alarm with id [{}]", id);
        String sqlQuery = "delete from alarmnames where ID=?";
        jdbcTemplate.update(sqlQuery, new Object[]{id});
    }

    @Override
    public Alarm getById(Long id) throws SQLException {
        logger.debug("Get alarm with id [{}]", id);
        String sqlQuery = "select * from alarmnames where ID=?";
        List<Alarm> alarms = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.alarm());
        if (alarms.isEmpty()) {
            return null;
        }
        return alarms.get(0);
    }

    @Override
    public Collection<Alarm> getAll() throws SQLException {
        logger.debug("Get all alarm names ");
        String sqlQuery = "select * from alarmnames order by id,name";
        List<Alarm> alarms = jdbcTemplate.query(sqlQuery, RowMappers.alarm());
        return alarms;
    }

    @Override
    public Collection<Alarm> getAll(Long langId) throws SQLException {
        logger.debug("Get all alarm names of given language id ");
        String sqlQuery = "select alarmnames.id, alarmnames.name, alarmbylanguage.alarmid, alarmbylanguage.langid, " +
                "alarmbylanguage.unicodename from alarmnames  left join alarmbylanguage  " +
                "on alarmnames.id=alarmbylanguage.alarmid and alarmbylanguage.langid=" + langId;
        List<Alarm> alarms = jdbcTemplate.query(sqlQuery, RowMappers.alarm());
        return alarms;
    }

    @Override
    public Collection<Alarm> getAllWithTranslation() throws SQLException {
        logger.debug("Get all alarm names with translation in all languages ");
        String sqlQuery = "select * from alarmnames "
                + "join alarmbylanguage on alarmnames.id=alarmbylanguage.alarmid "
                + "order by alarmbylanguage.langid , alarmbylanguage.alarmid ";
        List<Alarm> alarms = jdbcTemplate.query(sqlQuery, RowMappers.alarm());
        return alarms;
    }

    @Override
    public Collection<ProgramAlarm> getAllProgramAlarms(final Long programId) throws SQLException {
        logger.debug("Get all program alarms of given program id ");
        String sqlQuery = "select * from programalarms where ProgramID=?";
        List<ProgramAlarm> programAlarms = jdbcTemplate.query(sqlQuery, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, programId);
            }
        }, RowMappers.programAlarm());
        return programAlarms;
    }

    @Override
    public Collection<ProgramAlarm> getSelectedProgramAlarms(final Long programId) throws SQLException {
        logger.debug("Get all program alarms of given program id that was selected");
        String sqlQuery = "select * from programalarms where ProgramID=? and TEXT not Like '%None%' "
                + "and Text not Like '%Damy%' order by DataID,AlarmNumber";
        List<ProgramAlarm> programAlarms = jdbcTemplate.query(sqlQuery, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, programId);
            }
        }, RowMappers.programAlarm());
        return programAlarms;
    }

    @Override
    public Collection<ProgramAlarm> getSelectedProgramAlarms(final Long programId, final Long langId) throws SQLException {
        logger.debug("Get all program alarms of given program id and given language id  that was selected");
        String sqlQuery = "select * from programalarms inner join alarmbylanguage on " +
                "alarmbylanguage.alarmid = programalarms.alarmtextid " +
                "and alarmbylanguage.langid=? and programalarms.programid=? and programalarms.text not Like '%None%' " +
                "and programalarms.text not Like '%Damy%' order by programalarms.dataid, programalarms.digitnumber";

        List<ProgramAlarm> programAlarms = jdbcTemplate.query(sqlQuery, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, langId);
                ps.setLong(2, programId);
            }
        }, RowMappers.programAlarm());
        return programAlarms;
    }
}



