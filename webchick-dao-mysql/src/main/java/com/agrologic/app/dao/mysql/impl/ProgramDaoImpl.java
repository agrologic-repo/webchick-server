package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Program;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramDaoImpl implements ProgramDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(ProgramDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ProgramDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("programs");
        this.dao = dao;
    }

    @Override
    public void insert(Program program) throws SQLException {
        logger.debug("Creating program with id [{}]", program.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("programid", program.getId());
        valuesToInsert.put("name", program.getName());
        valuesToInsert.put("created", program.getCreatedDate());
        valuesToInsert.put("modified", program.getModifiedDate());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Program program) throws SQLException {
        logger.debug("Update program with id [{}]", program.getId());
        jdbcTemplate.update("update programs set Name=?, Modified=? where ProgramID=?",
                new Object[]{program.getName(), program.getModifiedDate(), program.getId()});
    }

    @Override
    public void remove(Long id) throws SQLException {
        Validate.notNull(id, "Id can not be null");
        logger.debug("Delete program with id [{}]", id);
        jdbcTemplate.update("delete from programs where ProgramID=?", new Object[]{id});
    }

    @Override
    public Integer count() throws SQLException {
        logger.debug("Count all programs ");
        String sqlQuery = "select count(*) as count from programs";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }

    @Override
    public Integer count(String searchText) throws SQLException {
        logger.debug("Count all programs with search text ");
        String sqlQuery = "select count(*) as count from programs where name like ?";
        Object[] objects = new Object[]{"%" + searchText + "%"};
        return jdbcTemplate.queryForObject(sqlQuery, objects, Integer.class);
    }

    @Override
    public Boolean isProgramWithGivenIdExist(Long id) throws SQLException {
        Program p = getById(id);
        return p != null;
    }

    @Override
    public Program getById(Long id) throws SQLException {
        logger.debug("Get alarm with id [{}]", id);
        String sqlQuery = "select * from programs where ProgramID=?";
        List<Program> programs = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.program());
        if (programs.isEmpty()) {
            return null;
        }
        return programs.get(0);
    }

    @Override
    public Collection<Program> getAll() throws SQLException {
        logger.debug("Get all programs  ");
        String sqlQuery = "select * from programs";
        return jdbcTemplate.query(sqlQuery, RowMappers.program());
    }

    @Override
    public Collection<Program> getAll(String searchText) throws SQLException {
        logger.debug("Get all programs with given search text " + searchText);
        String sqlQuery = "select * from programs where name like ? ";
        Object[] objects = new Object[]{"%" + searchText + "%"};
        return jdbcTemplate.query(sqlQuery, objects, RowMappers.program());
    }

    @Override
    public Collection<Program> getAllByUserId(String searchText, Long userId) throws SQLException {
        logger.debug("Get all programs belongs to specified user with given search text " + searchText);
        String sqlQuery = "select * from programs where programid in "
                + "(select programid from controllers where cellinkid in "
                + "(select cellinkid from cellinks where userid=?)) and name like ?";
        Object[] objects = new Object[]{userId, "%" + searchText + "%"};
        return jdbcTemplate.query(sqlQuery, objects, RowMappers.program());
    }

    @Override
    public Collection<Program> getAllByUserCompany(String searchText, String company) throws SQLException {
        logger.debug("Get all programs belongs to specified company with given search text " + searchText);
        String sqlQuery = "select * from programs where programid in "
                + "(select distinct programid from controllers where cellinkid in "
                + "(select cellinkid from cellinks where userid in "
                + "(select userid from users where company=?) and programid<>1)) and name like ?";
        Object[] objects = new Object[]{company, "%" + searchText + "%"};
        return jdbcTemplate.query(sqlQuery, objects, RowMappers.program());
    }

    @Override
    public Collection<Program> getAll(String searchText, String index) throws SQLException {
        logger.debug("Get all programs belongs to specified company with given search text " + searchText);
        String sqlQuery = "select * from programs where name like ? limit ? ,25 ";
        Object[] objects = new Object[]{(searchText == null ? "%%" : "%" + searchText + "%"),
                index == null ? 0 : Integer.valueOf(index)};
        return jdbcTemplate.query(sqlQuery, objects, RowMappers.program());
    }
}
