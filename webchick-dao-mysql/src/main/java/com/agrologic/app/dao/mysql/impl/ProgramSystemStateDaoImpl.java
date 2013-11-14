
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.ProgramSystemStateDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.ProgramSystemState;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.*;

/**
 * {@inheritDoc}
 */
public class ProgramSystemStateDaoImpl implements ProgramSystemStateDao {
    protected final Logger logger = LoggerFactory.getLogger(ProgramSystemStateDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    protected DaoFactory dao;

    /**
     * {@inheritDoc}
     */
    public ProgramSystemStateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("programsysstates");
    }

    @Override
    public void insert(ProgramSystemState programSystemState) throws SQLException {
        logger.debug("Inserting program systemState [{}] [{}] ", programSystemState, programSystemState.getSystemStateTextId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("dataid", programSystemState.getDataId());
        valuesToInsert.put("number", programSystemState.getNumber());
        valuesToInsert.put("text", programSystemState.getText());
        valuesToInsert.put("programid", programSystemState.getProgramId());
        valuesToInsert.put("systemstatenumber", programSystemState.getSystemStateNumber());
        valuesToInsert.put("systemstatetextid", programSystemState.getSystemStateTextId());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(ProgramSystemState programSystemState) throws SQLException {
        String sqlQuery = "update programsysstates set Text=? , systemstateTextID=? where DataID=? and Number=? " +
                "and ProgramID=?";
        logger.debug("Update program programSystemState [{}]", programSystemState);
        jdbcTemplate.update(sqlQuery, new Object[]{programSystemState.getText(), programSystemState.getSystemStateTextId(),
                programSystemState.getDataId(), programSystemState.getSystemStateNumber(), programSystemState.getProgramId()});
    }

    @Override
    public void remove(Long dataId, Integer number, Long programId) throws SQLException {
        String sqlQuery = "delete from programsysstates where DataId=? and Number=? and ProgramID=?";
        Validate.notNull(dataId, "Data id  can not be null");
        Validate.notNull(number, "Bit number  can not be null");
        Validate.notNull(programId, "Program id can not be null");
        logger.debug("Delete program system state with dataid: [{}] ,  Number: [{}] , Program ID: [{}] ",
                new String[]{dataId.toString(), number.toString(), programId.toString()});
        jdbcTemplate.update(sqlQuery, new Object[]{dataId, number, programId});

    }

    @Override
    public void assignSystemStateToGivenProgram(Long programId, SortedMap<Long, Map<Integer, String>> systemStateMap)
            throws SQLException {
        String sqlQuery = "insert into programsysstates " +
                "(DataID,Number,Text,ProgramID,SystemStateNumber,SystemStateTextID) " +
                "values (?,?,?,?,?,?) on duplicate key " +
                "update Text=values(Text),SystemStateTextID=values(SystemStateTextID)," +
                "SystemStateNumber=values(SystemStateNumber)";
        Set<Long> dataIdList = systemStateMap.keySet();
        final int maxNumbers = 10;
        int stateNumber, systemStateIndex = 0;
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Long dataId : dataIdList) {
            Map<Integer, String> systemStateNumberMaps = systemStateMap.get(dataId);
            Set<Integer> numbers = systemStateNumberMaps.keySet();
            for (Integer number : numbers) {
                String stateText = systemStateNumberMaps.get(number);
                Pair<Long, String> pair = parseSystemStateText(stateText);
                stateNumber = systemStateIndex * maxNumbers + number;
                Object[] values = new Object[]{
                        dataId,
                        number,
                        pair.getText(),
                        programId,
                        stateNumber,
                        pair.getId()
                };
                batch.add(values);
                logger.debug("values : ", values);
            }
            systemStateIndex++;
        }
        jdbcTemplate.batchUpdate(sqlQuery, batch);
    }

    @Override
    public void insertProgramSystemState(Collection<ProgramSystemState> programSystemStates) throws SQLException {
        List<ProgramSystemState> programRelayList = new ArrayList(programSystemStates);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (ProgramSystemState programRelay : programRelayList) {
            Object[] values = new Object[]{
                    programRelay.getDataId(),
                    programRelay.getNumber(),
                    programRelay.getText(),
                    programRelay.getProgramId(),
                    programRelay.getSystemStateNumber(),
                    programRelay.getSystemStateTextId()
            };
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("insert into programsysstates values (?,?,?,?,?,?)", batch);
    }

    @Override
    public List<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException {
        String sqlQuery = "select * from programsysstates where ProgramID=? " +
                "and TEXT not Like '%None%' and Text not Like '%Damy%'";
        logger.debug("Get all programsysstates assigned to program without those None and Dummy");
        return jdbcTemplate.query(sqlQuery, RowMappers.programSystemState());
    }

    @Override
    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from programsysstates left join systemstatebylanguage on "
                + "systemstatebylanguage.SystemStateID = programsysstates.SystemStateTextID "
                + "and systemstatebylanguage.LangID=? where programsysstates.ProgramID=? "
                + "and programsysstates.Text not Like '%None%' and  programsysstates.Text not Like '%Damy%'";// order by DataID,BitNumber";
        logger.debug("Get all system states assigned to program without those None and Dummy in given language id");
        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.programSystemState());
    }

    @Override
    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, String[] text) throws SQLException {
        String sqlQuery = "select * from programsysstates where ProgramID=" + programId;
        if (text != null) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                        + " order by a.DataID,a.Number ";
            }
        }
        Object[] params = new Object[text.length];
        if ((text != null) && (text.length > 0)) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                        + " order by a.DataID,a.DigitNumber ";
                params[i] = programId;
            }
        }

        logger.debug("Get all program system states with given language id ");
        return jdbcTemplate.query(sqlQuery, params, RowMappers.programSystemState());
    }

    private Pair parseSystemStateText(String systemStateText) {
        StringTokenizer token = new StringTokenizer(systemStateText, "-");
        String text = token.nextToken().trim();
        text = text.replaceAll("[\n\r]", "");
        String ids = token.nextToken();
        Long id = null;
        try {
            id = Long.parseLong(ids);
            System.out.println(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Pair(id, text);
    }

    /**
     * Inner class for using to encapsulate systemState text and systemState id
     *
     * @param <Long>
     * @param <String>
     */
    class Pair<Long, String> {
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



