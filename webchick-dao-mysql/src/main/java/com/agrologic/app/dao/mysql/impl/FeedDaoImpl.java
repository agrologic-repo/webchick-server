package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.FeedDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedDaoImpl implements FeedDao {

    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    protected final Logger logger = LoggerFactory.getLogger(FeedDaoImpl.class);

    public FeedDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("feed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Feed feed) throws SQLException {
        logger.debug("Inserting feed with type [{}]", feed.getType());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", feed.getFlockId());
        valuesToInsert.put("Type", feed.getType());
        valuesToInsert.put("Amount", feed.getAmount());
        valuesToInsert.put("Date", feed.getDate());
        valuesToInsert.put("AccountNumber", feed.getNumberAccount());
        valuesToInsert.put("Total", feed.getTotal());
        jdbcInsert.execute(valuesToInsert);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove feed with id [{}]", id);
        String sql = "delete from feed where ID=?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Feed getById(Long id) throws SQLException {
        logger.debug("Get feed with id [{}]", id);
        String sql = "select * from feed where ID=?";
        List<Feed> feedList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.feed());
        if (feedList.isEmpty()) {
            return null;
        }
        return feedList.get(0);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Feed> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all feed data with flock id {} ", flockId);
        String sql = "select * from feed where FlockID=?";
        return jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.feed());
    }
}
