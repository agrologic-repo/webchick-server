package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.ActionSetDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.ActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(ActionSet actionSet) throws SQLException {
        logger.debug("Inserting action set with name [{}]", actionSet.getLabel());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("DataId", actionSet.getDataId());
        valuesToInsert.put("ValueID", actionSet.getValueId());
        valuesToInsert.put("Label", actionSet.getLabel());
        jdbcInsert.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ActionSet actionSet) throws SQLException {
        logger.debug("update action set with id [{}]", actionSet.getLabel());
        String sql = "update actionset set Label=? where DataID=?";
        jdbcTemplate.update(sql, new Object[]{actionSet.getLabel(), actionSet.getLabel()});

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long valueId) throws SQLException {
        logger.debug("remove action set with id [{}]", valueId);
        String sql = "delete from actionset where ValueID=?";
        jdbcTemplate.update(sql, new Object[]{valueId});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertActionSetTranslation(Long valueId, Long langId, String translate) throws SQLException {
        logger.debug("Insert translation for action set with value id [{}]", valueId);
        String sql = "insert into actionsetbylanguage values (?,?,?) " +
                "on duplicate key update UnicodeText=values(UnicodeText)";
        jdbcTemplate.update(sql, new Object[]{valueId, langId, translate});
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Collection<ActionSet> actionSetsList) throws SQLException {
        // there is duplicate ActionSet elements in actionSetsList we need only unique elements
        Collection<ActionSet> actionSetCollection = Util.getUniqueElements(actionSetsList);
        for (ActionSet actionSet : actionSetCollection) {
            insert(actionSet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTranslation(Collection<ActionSet> actionSets) throws SQLException {
        String sqlQuery = "insert into actionsetbylanguage values (?,?,?) ";
        List<ActionSet> systemStateList = new ArrayList(actionSets);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (ActionSet actionSet : systemStateList) {
            Object[] values = new Object[]{
                    actionSet.getValueId(),
                    actionSet.getLangId(),
                    actionSet.getUnicodeLabel()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sqlQuery, batch);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActionSet> getAll() throws SQLException {
        logger.debug("Get all action set ");
        String sqlQuery = "select * from actionset";
        return jdbcTemplate.query(sqlQuery, RowMappers.actionSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ActionSet> getAllWithTranslation() throws SQLException {
        logger.debug("Get all action set ");
        String sqlQuery = "select * from actionset " +
                "left join actionsetbylanguage on actionset.valueid=actionsetbylanguage.valueid";
        return jdbcTemplate.query(sqlQuery, RowMappers.actionSet());
    }
}



