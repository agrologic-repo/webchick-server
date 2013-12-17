package com.agrologic.app.dao.mysql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collection;

public abstract class AbstractDaoImpl {

    protected final Logger logger;
    protected final JdbcTemplate jdbcTemplate;
    protected final SimpleJdbcInsert jdbcInsert;

    public AbstractDaoImpl(JdbcTemplate jdbcTemplate, Class clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    public abstract <T> void insert(T t);

    public abstract <T> void update(T t);

    public abstract <T> void remove(T t);

    public abstract <T> T getById(long id);

    public abstract <T> Collection<T> getAll();
}
