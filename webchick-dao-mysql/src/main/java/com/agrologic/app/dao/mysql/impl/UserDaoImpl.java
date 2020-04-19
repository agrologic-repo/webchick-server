package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.UserDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.User;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {
    protected final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    protected final SimpleJdbcInsert jdbcInsert;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("UserId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insert(User user) throws SQLException {
        logger.debug("Inserting user with id [{}]", user.getLogin());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        if (user.getId() != null) {
            valuesToInsert.put("userid", user.getId());
        }
        valuesToInsert.put("name", user.getLogin());
        valuesToInsert.put("name", user.getLogin());
        valuesToInsert.put("password", user.getPassword());
        valuesToInsert.put("firstname", user.getFirstName());
        valuesToInsert.put("lastname", user.getLastName());
        valuesToInsert.put("role", user.getRole().getValue());
        valuesToInsert.put("state", user.getState());
        valuesToInsert.put("phone", user.getPhone());
        valuesToInsert.put("email", user.getEmail());
        valuesToInsert.put("company", user.getCompany());
        Number key = jdbcInsert.executeAndReturnKey(valuesToInsert);
        if (key != null) {
            logger.debug("User with id [{}] successfully inserted .", key.longValue());
            return key.longValue();
        }
        throw new RuntimeException("Cannot retrieve primary key");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(User user) {
        logger.debug("Update user with id [{}]", user.getId());
        jdbcTemplate.update("update users set Name=?,Password=?,FirstName=?,LastName=?,Role=?,Phone=?,Email=?, " +
                "Company=? where UserID=?",
                new Object[]{user.getLogin(), user.getPassword(), user.getFirstName(), user.getLastName(),
                        user.getRole().getValue(), user.getPhone(), user.getEmail(), user.getCompany(), user.getId()});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long id) throws SQLException {
        Validate.notNull(id, "Id can not be null");
        logger.debug("delete from users where UserID=?", id);
        jdbcTemplate.update("delete from users where userID=?", new Object[]{id});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getById(Long id) throws SQLException {
        logger.debug("Get user with id [{}]", id);
        String sqlQuery = "select * from users where UserID=?";
        List<User> users = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.user());
        if (users.isEmpty()) {
            return new User();
        }
        return users.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User validate(String loginName, String loginPassword) throws SQLException {
        logger.debug("Validation user name and password ");
        String sqlQuery = "select * from users where Name = ? and Password = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, new Object[]{loginName, loginPassword}, RowMappers.user());
        if (users.isEmpty()) {
            return new User();
        }
        return users.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean loginEnabled(String login) {
        logger.debug("Check if user login already exist ");
        String sqlQuery = "select * from users where Name = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, new Object[]{login}, RowMappers.user());
        return users.isEmpty() ? true : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer count() throws SQLException {
        logger.debug("Count all users ");
        String sqlQuery = "select count(*) from users";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer count(Integer role, String company, String search) throws SQLException {
        String sqlQuery = "select count(*) from users where " +
                "(role=? or ? is null) and (company = ? or ? is null) and name like ? ";
        return jdbcTemplate.queryForObject(sqlQuery,
                new Object[]{role, role, company, company, (search == null ? "%%" : "%" + search + "%")}, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getUserCompanies() throws SQLException {
        logger.debug("Get all users companies ");
        String sqlQuery = "select distinct company from users order by company";
        return jdbcTemplate.query(sqlQuery, new ParameterizedRowMapper<String>() {
            public String mapRow(ResultSet rs, int arg1) throws SQLException {
                return rs.getString(1);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAll() {
        logger.debug("Get all users ");
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, RowMappers.user());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAllByRole(Integer role) throws SQLException {
        logger.debug("Get all users by role ");
        String sqlQuery = "select * from users where Role =?";
        return jdbcTemplate.query(sqlQuery, new Object[]{role}, RowMappers.user());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAll(Integer role, String company, String search) throws SQLException {
        logger.debug("Get all users by role ");
        String sqlQuery = "select * from users where " +
                "(role=? or ? is null) and (company = ? or ? is null) and name like ? ";
        return jdbcTemplate.query(sqlQuery,
                new Object[]{role, role, company, company, (search == null ? "%%" : "%" + search + "%")},
                RowMappers.user());
    }

    @Override
    public Collection<User> getAll(Integer role, String company, String search, String index) throws SQLException {
        logger.debug("Get all users by role , company with given search text  ");
        String sqlQuery = "select * from users where (role=? or ? is null) and (company = ? or ? is null) " +
                "and name like ? limit ? ,25  ";
        return jdbcTemplate.query(sqlQuery,
                new Object[]{role, role, company, company, (search == null ? "%%" : "%" + search + "%"),
                        index == null ? 0 : Integer.valueOf(index)},
                RowMappers.user());
    }
}
