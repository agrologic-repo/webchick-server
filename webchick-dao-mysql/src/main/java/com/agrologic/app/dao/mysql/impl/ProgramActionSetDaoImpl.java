package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.dao.ProgramActionSetDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.ProgramActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * {@inheritDoc}
 */
public class ProgramActionSetDaoImpl implements ProgramActionSetDao {
    protected final Logger logger = LoggerFactory.getLogger(ProgramActionSetDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    /**
     * {@inheritDoc}
     */
    public ProgramActionSetDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("programactionset");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(ProgramActionSet programActionSet) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProgramActionSet programActionSet) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long programId, Long valueId) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertProgramActionSet(ProgramActionSet programActionSet) throws SQLException {
        logger.debug("Inserting action set with id [{}] and value [{}]", programActionSet.getDataId(), programActionSet.getValueId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("DataId", programActionSet.getDataId());
        valuesToInsert.put("ValueId", programActionSet.getValueId());
        valuesToInsert.put("ScreenId", programActionSet.getScreenId());
        valuesToInsert.put("ProgramId", programActionSet.getProgramId());
        valuesToInsert.put("Position", programActionSet.getPosition());
        valuesToInsert.put("DisplayOnPage", programActionSet.isDisplayOnPage());
        SimpleJdbcInsert jdbcLocalInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcLocalInsert.setTableName("programactionset");
        jdbcLocalInsert.execute(valuesToInsert);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProgramActionSet getById(Long id, Long programId) throws SQLException {
        logger.debug("Get program action set with id [{}]", id);
        String sqlQuery = "select * from programactionset where ValueID=? and ProgramID=?";
        List<ProgramActionSet> programActionSets = jdbcTemplate.query(sqlQuery, new Object[]{id, programId},
                RowMappers.programAcctionSet());
        if (programActionSets.isEmpty()) {
            return null;
        }
        return programActionSets.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramActionSet> getAllOnScreen(Long programId) throws SQLException {
        // call method with default language english
        return getAllOnScreen(programId, 1L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProgramActionSet> getAllOnScreen(Long programId, Long langId) throws SQLException {
        logger.debug("Get all action set assigned to program id {} and language id {} ", new Object[]{programId, langId});
        String sqlQuery = "select * from actionset as actset left join programactionset as progactset "
                + "on actset.ValueID=progactset.ValueID left join actionsetbylanguage "
                + "on actionsetbylanguage.ValueID=actset.ValueID and actionsetbylanguage.langid=? "
                + "where programid=? and actset.ValueID=progactset.ValueID and progactset.DisplayOnPage='yes'";

        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.programAcctionSet());
    }

    @Override
    public List<ProgramActionSet> getAll(Long programId) throws SQLException {
        // call method with default language english
        return getAll(programId, 1L);
    }

    @Override
    public List<ProgramActionSet> getAll(Long programId, Long langId) throws SQLException {
        logger.debug("Get all action set assigned to program id {} and language id {} ", new Object[]{programId, langId});
        String sqlQuery = "select * from actionset as actset left join programactionset as progactset "
                + "on actset.ValueID=progactset.ValueID left join actionsetbylanguage "
                + "on actionsetbylanguage.ValueID=actset.ValueID and actionsetbylanguage.langid=? "
                + "where programid=? and actset.ValueID=progactset.ValueID";

        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.programAcctionSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertProgramActionSetList(Collection<ProgramActionSet> programActionsetList)
            throws SQLException {
        // there is duplicate actionSet elements in actionsetList we need only unique elements
        Collection<ProgramActionSet> programActionSetCollection = Util.getUniqueElements(programActionsetList);
        for (ProgramActionSet actionSet : programActionSetCollection) {
            insertProgramActionSet(actionSet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertProgramActionSetList(Collection<ProgramActionSet> programActionsetList, Long programId)
            throws SQLException {
        // there is duplicate actionSet elements in actionsetList we need only unique elements
        Collection<ProgramActionSet> programActionSetCollection = Util.getUniqueElements(programActionsetList);
        for (ProgramActionSet actionSet : programActionSetCollection) {
            actionSet.setProgramId(programId);
            insertProgramActionSet(actionSet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveChanges(final Map<Long, String> showMap, Map<Long, Integer> positionMap, final Long programId)
            throws SQLException {
        logger.debug("Save changes of action set position and show ");
        final String sql = "update programactionset set DisplayOnPage=?, Position=? where ValueID=? and ProgramID=?";
        final List<Long> actionSetList = new ArrayList(showMap.size());
        final List<String> showFlags = new ArrayList(showMap.size());
        final List<Integer> positions = new ArrayList(showMap.size());
        Set<Long> keys = showMap.keySet();
        for (Long valueId : keys) {
            actionSetList.add(valueId);
            showFlags.add(showMap.get(valueId));
            positions.add(positionMap.get(valueId));
        }

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, showFlags.get(i));
                ps.setInt(2, positions.get(i));
                ps.setLong(3, actionSetList.get(i));
                ps.setLong(4, programId);
            }

            @Override
            public int getBatchSize() {
                return showMap.size();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveChanges(final Long programId, final Long screenId, final Map<Long, String> showMap,
                            final Map<Long, Integer> posMap) throws SQLException {
        logger.debug("Save changes of action set position and show ");
        final String sql = "update programactionset set DisplayOnPage=?, Position=? where ValueID=? and ProgramID=?";
        final List<Long> actionSetList = new ArrayList(showMap.size());
        final List<String> showFlags = new ArrayList(showMap.size());
        final List<Integer> positions = new ArrayList(showMap.size());
        Set<Long> keys = showMap.keySet();
        for (Long valueId : keys) {
            actionSetList.add(valueId);
            showFlags.add(showMap.get(valueId));
            positions.add(posMap.get(valueId));
        }

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, showFlags.get(i));
                ps.setInt(2, positions.get(i));
                ps.setLong(3, actionSetList.get(i));
                ps.setLong(4, programId);
            }

            @Override
            public int getBatchSize() {
                return showMap.size();
            }
        });
    }

//    @Override
//    public void insertActionSetTranslation(Long valueId, Long langId, String translate) throws SQLException {
//        logger.debug("Insert translation for action set with value id [{}]", valueId);
//        String sql = "insert into actionsetbylanguage values (?,?,?) " +
//                "on duplicate key update UnicodeText=values(UnicodeText)";
//        jdbcTemplate.update(sql, new Object[]{valueId, langId, translate});
//    }
}



