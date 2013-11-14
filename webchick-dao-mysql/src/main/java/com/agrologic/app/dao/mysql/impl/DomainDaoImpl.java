package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DomainDao;
import com.agrologic.app.dao.mappers.RowMappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DomainDaoImpl implements DomainDao {

    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    protected final Logger logger = LoggerFactory.getLogger(DomainDaoImpl.class);

    public DomainDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("domains");
    }

    @Override
    public String getDomain() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLogoPath(String domain) throws SQLException {
        logger.debug("Get logo path ");
        String sqlQuery = "select logopath from domains where domain=? ";
        List<String> domainlogoPaths = jdbcTemplate.queryForList(sqlQuery, new Object[]{domain}, String.class);
        if (domainlogoPaths.isEmpty()) {
            return "resources/images/agrologiclogo.png";
        }
        return domainlogoPaths.get(0);
    }

    @Override
    public String getCompany(String domain) throws SQLException {
        logger.debug("Get company ");
        String sqlQuery = "select company from domains where domain=? ";
        List<String> companies = jdbcTemplate.queryForList(sqlQuery, new Object[]{domain}, String.class);
        if (companies.isEmpty()) {
            return "Agrologic";
        }
        return companies.get(0);
    }
}



