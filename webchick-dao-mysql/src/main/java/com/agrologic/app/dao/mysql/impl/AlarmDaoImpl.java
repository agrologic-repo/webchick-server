package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Alarm;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.*;

/**
 * An implementation of {@link AlarmDao} that is based on JdbcTemplate and working
 * with database.
 *
 * @author Valery Manakhimov
 */
public class AlarmDaoImpl implements AlarmDao {
    protected final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    protected final SimpleJdbcInsert jdbcInsert;
    protected final SimpleJdbcInsert jdbcInsertTranslate;

    public AlarmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("alarmnames");
        this.jdbcInsertTranslate = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsertTranslate.setTableName("alarmbylanguage");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Alarm alarm) {
        logger.debug("Inserting alarm with id [{}]", alarm.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("id", alarm.getId());
        valuesToInsert.put("name", alarm.getText());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Alarm alarm) {
        logger.debug("Update alarm with id [{}]", alarm.getId());
        jdbcTemplate.update("update alarmnames set Name=? where ID=?", new Object[]{alarm.getText(), alarm.getId()});
    }

    @Override
    public void remove(Alarm alarm) {
        Validate.notNull(alarm, "Alarm can not be null");
        logger.debug("Delete alarm with id [{}]", alarm.getId());
        jdbcTemplate.update("delete from alarmnames where ID=?", new Object[]{alarm.getId()});
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
        logger.debug("Inserting alarm translation with id [{}] and language id [{}] ", alarmId, langId);
        String sqlQuery = "insert into alarmbylanguage(alarmid,langid,unicodename) values (?,?,?) " +
                "on duplicate key update UnicodeName=values(UnicodeName)";
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
        jdbcTemplate.batchUpdate("insert into alarmbylanguage values (?,?,?) ",
                batch);
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
        return jdbcTemplate.query(sqlQuery, RowMappers.alarm());
    }

    @Override
    public Collection<Alarm> getAll(Long langId) throws SQLException {
        logger.debug("Get all alarm names of given language id ");
        String sqlQuery = "select alarmnames.id, alarmnames.name, alarmbylanguage.alarmid, alarmbylanguage.langid, " +
                "alarmbylanguage.unicodename from alarmnames  left join alarmbylanguage " +
                "on alarmnames.id=alarmbylanguage.alarmid and alarmbylanguage.langid=" + langId;
        return jdbcTemplate.query(sqlQuery, RowMappers.alarm());
    }

    @Override
    public Collection<Alarm> getAllWithTranslation() throws SQLException {
        logger.debug("Get all alarm names with translation in all languages ");
        String sqlQuery = "select * from alarmnames "
                + "join alarmbylanguage on alarmnames.id=alarmbylanguage.alarmid "
                + "order by alarmbylanguage.langid , alarmbylanguage.alarmid ";
        return jdbcTemplate.query(sqlQuery, RowMappers.alarm());
    }

    @Override
    public void copyAlarms(Long newProgramId, Long selectedProgramId) {
        logger.debug("Insert alarms ");
        final String sqlQuery = "insert into programalarms (DataID,DigitNumber,Text,ProgramID,AlarmNumber,AlarmTextID) "
                + " (select DataID,DigitNumber,Text,?,AlarmNumber,AlarmTextID from programalarms where programid=?)";
        jdbcTemplate.update(sqlQuery, new Object[]{newProgramId, selectedProgramId});
    }
}



