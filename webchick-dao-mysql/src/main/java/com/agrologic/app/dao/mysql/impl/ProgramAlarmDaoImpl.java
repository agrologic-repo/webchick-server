
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.dao.ProgramAlarmDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.ProgramAlarm;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.*;

public class ProgramAlarmDaoImpl implements ProgramAlarmDao {

    protected final Logger logger = LoggerFactory.getLogger(ProgramAlarmDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    /**
     * {@inheritDoc}
     */
    public ProgramAlarmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("programalrams");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(ProgramAlarm programAlarm) throws SQLException {
        logger.debug("Inserting program alarm [{}] [{}]", programAlarm, programAlarm.getAlarmTextId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("digitnumber", programAlarm.getDigitNumber());
        valuesToInsert.put("text", programAlarm.getText());
        valuesToInsert.put("programid", programAlarm.getProgramId());
        valuesToInsert.put("alarmtextid", programAlarm.getAlarmTextId());
        jdbcInsert.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProgramAlarm programAlarm) throws SQLException {
        String sqlQuery = "update programalarms set Text=?, alarmTextID=? where DataID=? and DigitNumber=? " +
                "and ProgramID=?";
        logger.debug("Update program alarm [{}]", programAlarm);
        jdbcTemplate.update(sqlQuery, new Object[]{programAlarm.getText(), programAlarm.getAlarmTextId(),
                programAlarm.getDataId(), programAlarm.getDigitNumber(), programAlarm.getProgramId()});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long dataId, Integer digitNumber, Long programId) throws SQLException {
        Validate.notNull(dataId, "Data id  can not be null");
        Validate.notNull(digitNumber, "Digit number  can not be null");
        Validate.notNull(programId, "Program id can not be null");
        logger.debug("Delete program alarm with dataid: [{}] ,  digitNumber: [{}] , program id: [{}] ",
                new String[]{dataId.toString(), digitNumber.toString(), programId.toString()});
        String sqlQuery = "delete from programalarms where DataId=? and DigitNumber=? and ProgramID=?";
        jdbcTemplate.update(sqlQuery, new Object[]{dataId, digitNumber, programId});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignAlarmsToGivenProgram(Long programId, Map<Long, Map<Integer, String>> alarmMap) throws SQLException {
        String sqlQuery = "insert into programalarms" + " (DataID,DigitNumber,Text,ProgramID,AlarmNumber,AlarmTextID)"
                + " values (?,?,?,?,?,?) on duplicate key update Text=values(Text), AlarmTextID=values(AlarmTextID)";
        Set<Long> dataIdList = alarmMap.keySet();
        final int digitsBits = 10;
        int relayIndex = 0, alarmNumber = 0;
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Long dataId : dataIdList) {
            Map<Integer, String> alarmDigitMap = alarmMap.get(dataId);
            Set<Integer> digits = alarmDigitMap.keySet();
            for (Integer digit : digits) {
                String alarmText = alarmDigitMap.get(digit);
                Pair<Long, String> pair = parseAlarmText(alarmText);
                alarmNumber = relayIndex * digitsBits + digit;
                Object[] values = new Object[]{
                        dataId,
                        digit,
                        pair.getText(),
                        programId,
                        alarmNumber,
                        pair.getId()
                };
                batch.add(values);
                logger.debug("values : ", values);
            }
            relayIndex++;
        }
        jdbcTemplate.batchUpdate(sqlQuery, batch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Collection<ProgramAlarm> programAlarms) throws SQLException {
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
    public void removeAllProgramAlarms(Long programId) throws SQLException {
        Validate.notNull(programId, "Program id can not be null");
        logger.debug("Delete program alarm with dataid: [{}] ,  digitNumber: [{}] , program id: [{}] ",
                new String[]{programId.toString()});
        String sqlQuery = "delete from programalarms where ProgramID=?";
        jdbcTemplate.update(sqlQuery, new Object[]{programId});

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException {
        String sqlQuery = "select * from programalarms where ProgramID=?";
        logger.debug("Get all program alarms");
        return jdbcTemplate.query(sqlQuery, new Object[]{programId}, RowMappers.programAlarm());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramAlarm> getAllProgramAlarms(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from programalarms left join alarmbylanguage " +
                "on alarmbylanguage.AlarmID = programalarms.AlarmTextID and alarmbylanguage.LangID=?  " +
                "where programalarms.ProgramID=? and programalarms.Text not Like '%None%' " +
                "and  programalarms.Text not Like '%Damy%'";
        logger.debug("Get all program alarms with given language id ");
        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.programAlarm());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramAlarm> getAllProgramAlarms(Long programId, String[] text) throws SQLException {
        String sqlQuery = "select * from programalarms where ProgramID=" + programId;
        Object[] params = new Object[text.length];
        if ((text != null) && (text.length > 0)) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                        + " order by a.DataID,a.DigitNumber ";
                params[i] = programId;
            }
        }

        logger.debug("Get all program alarms with given language id ");
        return jdbcTemplate.query(sqlQuery, params, RowMappers.programAlarm());
    }

    private Pair parseAlarmText(String alarmText) {
        int digIndx = alarmText.length() - 1;
        int len = 2;

        while (!Character.isLetter(alarmText.charAt(digIndx)) && (len > 0)) {
            digIndx--;
            len--;
        }

        String text = alarmText.substring(0, digIndx + 1).trim();
        text = text.replaceAll("[\n\r]", "");
        String ids = alarmText.substring(digIndx + 1).trim();
        Long id = null;

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
        Long id;
        String text;

        Pair(Long id, String text) {
            this.id = id;
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



