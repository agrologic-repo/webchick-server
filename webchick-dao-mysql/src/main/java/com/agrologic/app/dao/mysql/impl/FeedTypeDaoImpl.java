package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.FeedTypeDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Feed;
import com.agrologic.app.model.FeedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedTypeDaoImpl implements FeedTypeDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(FeedTypeDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FeedTypeDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("feedtypes");
        this.dao = dao;
    }

    @Override
    public void insert(FeedType feedType) throws SQLException {
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FeedType", feedType.getFeedType());
        valuesToInsert.put("Price", feedType.getPrice());
        valuesToInsert.put("CellinkID", feedType.getCellinkId());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove feed type with id [{}]", id);
        String sql = "delete from feedtypes where ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public FeedType getById(Long id) throws SQLException {
        logger.debug("Get feed types with id [{}]", id);
        String sql = "select * from feedtypes where ID=?";
        List<FeedType> feedList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.feedType());
        if (feedList.isEmpty()) {
            return null;
        }
        return feedList.get(0);
    }

    @Override
    public List<FeedType> getAllByCellinkId(Long cellinkId) throws SQLException {
        logger.debug("Get all feed type with cellink id {} ", cellinkId);
        String sql = "select * from feedtypes where CellinkID=?";
        return jdbcTemplate.query(sql,new Object[]{cellinkId}, RowMappers.feedType());
    }
}