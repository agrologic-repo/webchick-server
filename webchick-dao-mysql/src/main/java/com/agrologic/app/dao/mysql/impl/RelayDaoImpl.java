package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.RelayDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Relay;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.*;

public class RelayDaoImpl implements RelayDao {
    protected final Logger logger = LoggerFactory.getLogger(RelayDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public RelayDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("relaynames");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Relay relay) {
        logger.debug("Inserting relay with id [{}]", relay.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("id", relay.getId());
        valuesToInsert.put("name", relay.getText());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Relay relay) throws SQLException {
        logger.debug("Update relay with id [{}]", relay.getId());
        jdbcTemplate.update("update relaynames set Name=? where ID=?", new Object[]{relay.getText(), relay.getId()});
    }

    @Override
    public void remove(Long id) throws SQLException {
        Validate.notNull(id, "ID can not be null");
        logger.debug("Delete relay name with id [{}]", id);
        jdbcTemplate.update("delete from relaynames where ID=?", new Object[]{id});
    }

    @Override
    public void insert(Collection<Relay> relayList) throws SQLException {
        Collection<Relay> relayCollection = Util.getUniqueElements(relayList);
        for (Relay relay : relayCollection) {
            insert(relay);
        }
    }

    @Override
    public void insertTranslation(Long relayId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into relaybylanguage values (?,?,?) on duplicate key update UnicodeText=values(UnicodeText)";
        jdbcTemplate.update(sqlQuery, new Object[]{relayId, langId, translation});
    }

    @Override
    public void insertTranslation(Collection<Relay> relays) throws SQLException {
        List<Relay> relayList = new ArrayList(relays);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Relay relay : relayList) {
            Object[] values = new Object[]{
                    relay.getId(),
                    relay.getLangId(),
                    relay.getUnicodeText()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("insert into relaybylanguage values (?,?,?) ", batch);
    }

    @Override
    public Relay getById(Long id) throws SQLException {
        String sqlQuery = "select * from relaynames where ID=?";
        List<Relay> relays = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.relay());
        if (relays.isEmpty()) {
            return null;
        }
        return relays.get(0);
    }

    @Override
    public Collection<Relay> getAll() throws SQLException {
        logger.debug("Get all relay names ");
        return jdbcTemplate.query("select * from relaynames order by id,name", RowMappers.relay());
    }

    @Override
    public Collection<Relay> getAll(Long langId) throws SQLException {
        String sqlQuery = "select r1.id, r1.name, r2.relayid, r2.langid, r2.unicodetext from relaynames r1 "
                + "left join relaybylanguage r2 on r1.id=r2.relayid and langid=" + langId;
        logger.debug("Get all relay names with given language id [{}] ", langId);
        return jdbcTemplate.query(sqlQuery, RowMappers.relay());
    }

    @Override
    public Collection<Relay> getAllWithTranslation() throws SQLException {
        String sqlQuery = "select * from relaynames "
                + "join relaybylanguage on relaynames.id=relaybylanguage.relayid "
                + "order by relaybylanguage.langid , relaybylanguage.relayid";
        logger.debug("Get all relay names with translation to all languages ");
        return jdbcTemplate.query(sqlQuery, RowMappers.relay());
    }
}
