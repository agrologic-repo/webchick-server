package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.ProgramRelayDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.ProgramRelay;
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
public class ProgramRelayDaoImpl implements ProgramRelayDao {
    private final Logger logger = LoggerFactory.getLogger(ProgramRelayDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    protected DaoFactory dao;

    /**
     * {@inheritDoc}
     */
    public ProgramRelayDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("programrelays");
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(ProgramRelay programRelay) throws SQLException {
        logger.debug("Creating program relay [{}] [{}] ", programRelay, programRelay.getRelayTextId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("dataid", programRelay.getDataId());
        valuesToInsert.put("bitnumber", programRelay.getBitNumber());
        valuesToInsert.put("text", programRelay.getText());
        valuesToInsert.put("programid", programRelay.getProgramId());
        valuesToInsert.put("relaynumber", programRelay.getRelayNumber());
        valuesToInsert.put("relaytextid", programRelay.getRelayTextId());
        jdbcInsert.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProgramRelay programRelay) throws SQLException {
        String sqlQuery = "update programrelays set Text=?, relayTextID=? where DataID=? and BitNumber=? and ProgramID=?";
        logger.debug("Update program programRelay [{}]", programRelay);
        jdbcTemplate.update(sqlQuery, new Object[]{programRelay.getText(), programRelay.getRelayTextId(),
                programRelay.getDataId(), programRelay.getBitNumber(), programRelay.getProgramId()});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long dataId, Integer bitNumber, Long programId) throws SQLException {
        String sqlQuery = "delete from programrelays where DataId=? and BitNumber=? and ProgramID=?";
        Validate.notNull(dataId, "Data id  can not be null");
        Validate.notNull(bitNumber, "Bit number  can not be null");
        Validate.notNull(programId, "Program id can not be null");
        logger.debug("Delete program relay with dataid: [{}] ,  bitNumber: [{}] , program id: [{}] ",
                new String[]{dataId.toString(), bitNumber.toString(), programId.toString()});
        jdbcTemplate.update(sqlQuery, new Object[]{dataId, bitNumber, programId});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Collection<ProgramRelay> programRelays) throws SQLException {
        List<ProgramRelay> programRelayList = new ArrayList(programRelays);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (ProgramRelay programAlarm : programRelayList) {
            Object[] values = new Object[]{
                    programAlarm.getDataId(),
                    programAlarm.getBitNumber(),
                    programAlarm.getText(),
                    programAlarm.getProgramId(),
                    programAlarm.getRelayNumber(),
                    programAlarm.getRelayTextId()
            };
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("insert into programrelays values (?,?,?,?,?,?)", batch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignRelaysToGivenProgram(Long programId, Map<Long, Map<Integer, String>> relayMap) throws SQLException {
        String sqlQuery = "insert into programrelays (DataID, BitNumber, Text, ProgramID, RelayNumber, RelayTextID) " +
                "values (?,?,?,?,?,?) on duplicate key " +
                "update Text=values(Text),RelayTextID=values(RelayTextID),RelayNumber=values(RelayNumber)";

        Set<Long> dataIdList = relayMap.keySet();
        final int maxBits = 16;
        int relayIndex = 0, relayNumber;

        List<Object[]> batch = new ArrayList<Object[]>();
        for (Long dataId : dataIdList) {
            Map<Integer, String> relayBitMap = relayMap.get(dataId);
            Set<Integer> bits = relayBitMap.keySet();
            for (Integer bit : bits) {
                String relayText = relayBitMap.get(bit);
                Pair<Long, String> pair = parseRelayText(relayText);
                relayNumber = relayIndex * maxBits + bit;
                Object[] values = new Object[]{
                        dataId,
                        bit,
                        pair.getText(),
                        programId,
                        relayNumber,
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
    public List<ProgramRelay> getAllProgramRelays(Long programId) throws SQLException {
        String sqlQuery = "select * from programrelays where ProgramID=? and TEXT not Like '%'?'%' and Text not Like '%'?'%'";
        logger.debug("Get all relays assigned to program without those None and Dummy");
        return jdbcTemplate.query(sqlQuery, new Object[]{programId, "None", "Damy"}, RowMappers.programRelay());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramRelay> getAllProgramRelays(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from programrelays "
                + "left join relaybylanguage on relaybylanguage.RelayID=programrelays.RelayTextID "
                + "and relaybylanguage.langid=? "
                + "where programrelays.programid=? "
                + "and programrelays.Text not Like '%None%' and  programrelays.Text not Like '%Damy%'";// order by DataID,BitNumber";
        logger.debug("Get all relays assigned to program without those None and Dummy in given language id");
        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.programRelay());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramRelay> getAllProgramRelays(Long programId, String[] text) throws SQLException {
        String sqlQuery = "select * from programrelays where ProgramID=" + programId;
        Object[] params = new Object[text.length];
        if ((text != null) && (text.length > 0)) {
            for (int i = 0; i < text.length; i++) {
                sqlQuery = "select * from (" + sqlQuery + ") as a where a.Text not Like '%" + text[i]
                        + " order by a.DataID,a.DigitNumber ";
                params[i] = programId;
            }
        }

        logger.debug("Get all program relays with given language id ");
        return jdbcTemplate.query(sqlQuery, params, RowMappers.programRelay());
    }

    /**
     * {@inheritDoc}
     */
    private Pair parseRelayText(String relayText) {
        StringTokenizer token = new StringTokenizer(relayText, "-");
        String text = token.nextToken().trim();
        text = text.replaceAll("[\n\r]", "");
        String ids = token.nextToken();
        Long id = null;

        try {
            id = Long.parseLong(ids);
        } catch (Exception ex) {
        }

        return new Pair(id, text);
    }

    /**
     * Inner class for using to incapsulate relay text and relay id
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



