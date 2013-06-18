
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.dao.DaoFactory;
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

/**
 * Title: ProgramAlarmDaoImpl <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class ProgramAlarmDaoImpl implements ProgramAlarmDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    /**
     * {@inheritDoc}
     */
    public ProgramAlarmDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("programalrams");
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(ProgramAlarm programAlarm) throws SQLException {
        logger.debug("Creating program alarm with id [{}]", programAlarm.getProgramId());
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
        String sqlQuery = "delete from programrelays where DataId=? and DigitNumber=? and ProgramID=?";
        Validate.notNull(dataId, "Data id  can not be null");
        Validate.notNull(digitNumber, "Digit number  can not be null");
        Validate.notNull(programId, "Program id can not be null");
        logger.debug("Delete program alarm with dataid: [{}] ,  digitNumber: [{}] , program id: [{}] ",
                new String[]{dataId.toString(), digitNumber.toString(), programId.toString()});
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
            Map<Integer, String> alarmDigitMaps = alarmMap.get(dataId);
            Set<Integer> digits = alarmDigitMaps.keySet();
            for (Integer digit : digits) {
                String alarmText = alarmDigitMaps.get(digit);
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
        String sqlQuery =
                "select * from programalarms " +
                        "left join alarmbylanguage on alarmbylanguage.AlarmID = programalarms.AlarmTextID " +
                        "and alarmbylanguage.LangID=? " +
                        "where programalarms.ProgramID=?";//as progalarm ";
//                        + " join (select AlarmID,LangID,UnicodeName from alarmbylanguage ) "
//                        + " as abl where abl.AlarmID = progalarm.AlarmTextID and abl.LangID=? and ProgramID=? order by DataID,DigitNumber";
//        String[] strings = new String[]{"None", "Damy"};
//
//        for (int i = 0; i < strings.length; i++) {
//            sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + strings[i]
//                    + "%' order by a.DataID,a.DigitNumber ";
//        }
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



