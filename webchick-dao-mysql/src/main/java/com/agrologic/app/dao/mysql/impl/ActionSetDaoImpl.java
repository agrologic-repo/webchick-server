package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.ActionSetDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.ActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.*;
/**
 * An implementation of {@link com.agrologic.app.dao.ActionSetDao} that is based on JdbcTemplate and working
 * with database.
 *
 * @author Valery Manakhimov
 */
public class ActionSetDaoImpl implements ActionSetDao {

    protected final Logger logger = LoggerFactory.getLogger(ActionSetDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ActionSetDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("actionset");
    }

    @Override
    public void insert(ActionSet ActionSet) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(ActionSet ActionSet) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(Long ActionSetId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertProgramActionSet(ActionSet actionSet) throws SQLException {
        logger.debug("Inserting action set with id [{}] and value [{}]", actionSet.getDataId(), actionSet.getValueId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("DataId", actionSet.getDataId());
        valuesToInsert.put("ValueId", actionSet.getValueId());
        valuesToInsert.put("ScreenId", actionSet.getScreenId());
        valuesToInsert.put("ProgramId", actionSet.getProgramId());
        valuesToInsert.put("Position", actionSet.getPosition());
        valuesToInsert.put("DisplayOnPage", actionSet.isDisplayOnPage());
        SimpleJdbcInsert jdbcLocalInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcLocalInsert.setTableName("programactionset");
        jdbcLocalInsert.execute(valuesToInsert);
    }

    @Override
    public ActionSet getById(Long id) throws SQLException {
        logger.debug("Get action set with id [{}]", id);
        String sqlQuery = "select * from actionset where ValueID=?";
        List<ActionSet> actionSets = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.actionSet());
        if (actionSets.isEmpty()) {
            return null;
        }
        return actionSets.get(0);
    }

    @Override
    public List<ActionSet> getAll() throws SQLException {
        logger.debug("Get all action set ");
        String sqlQuery = "select * from actionset";
        return jdbcTemplate.query(sqlQuery, RowMappers.actionSet());
    }

    @Override
    public List<ActionSet> getAll(Long programId) throws SQLException {
        // call method with default language english
        return getAll(programId, Long.valueOf(1));
    }

    @Override
    public List<ActionSet> getAll(Long programId, Long langId) throws SQLException {
        logger.debug("Get all action set assigned to program id {} and language id {} ", new Object[]{programId, langId});
        String sqlQuery = "select * from actionset as actset left join programactionset as progactset "
                + "on actset.ValueID=progactset.ValueID left join actionsetbylanguage "
                + "on actionsetbylanguage.ValueID=actset.ValueID and actionsetbylanguage.langid=? where programid=?";

        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.actionSet());
    }

    @Override
    public List<ActionSet> getAllOnScreen(Long programId) throws SQLException {
        // call method with default language english
        return getAllOnScreen(programId, Long.valueOf(1));
    }

    @Override
    public List<ActionSet> getAllOnScreen(Long programId, Long langId) throws SQLException {
        logger.debug("Get all action set assigned to program id {} and language id {} ", new Object[]{programId, langId});
        String sqlQuery = "select * from actionset as actset left join programactionset as progactset "
                + "on actset.ValueID=progactset.ValueID left join actionsetbylanguage "
                + "on actionsetbylanguage.ValueID=actset.ValueID and actionsetbylanguage.langid=? "
                + "where programid=? and actset.ValueID=progactset.ValueID and progactset.DisplayOnPage='yes'";

        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.actionSet());
    }

    @Override
    public void insertActionSetList(List<ActionSet> actionsetList, Long programId) throws SQLException {
        // there is duplicate actionSet elements in actionsetList we need only unique elements
        Collection<ActionSet> actionSetCollection = Util.getUniqueElements(actionsetList);
        for (ActionSet actionSet : actionSetCollection) {
            actionSet.setProgramId(programId);
            insertProgramActionSet(actionSet);
        }
    }

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

    @Override
    public void insertActionSetTranslation(Long valueId, Long langId, String translate) throws SQLException {
        logger.debug("Insert translation for action set  with value id [{}]" , valueId);
        String sql =
                "insert into tablebylanguage values (?,?,?) on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        jdbcTemplate.update(sql, new Object[]{valueId,langId,translate});
    }
}



