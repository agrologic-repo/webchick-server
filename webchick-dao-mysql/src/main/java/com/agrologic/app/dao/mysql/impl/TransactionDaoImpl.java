package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.TransactionDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDaoImpl implements TransactionDao {

    protected final Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("transactions");
        this.jdbcInsert.setGeneratedKeyName("ID");
    }

    @Override
    public void insert(Transaction transaction) throws SQLException {
        logger.debug("Inserting transaction with type [{}]", transaction.getFlockId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("FlockID", transaction.getFlockId());
        valuesToInsert.put("Name", transaction.getName());
        valuesToInsert.put("Expenses", transaction.getExpenses());
        valuesToInsert.put("Revenues", transaction.getRevenues());
        jdbcInsert.setGeneratedKeyName("ID");
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void remove(Long id) throws SQLException {
        logger.debug("Remove transaction with id [{}]", id);
        String sql = "delete from transactions where ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Transaction getById(Long id) throws SQLException {
        logger.debug("Get transactions with id [{}]", id);
        String sql = "select * from transactions where ID=?";
        List<Transaction> transactionList = jdbcTemplate.query(sql, new Object[]{id}, RowMappers.transaction());
        if (transactionList.isEmpty()) {
            return null;
        }
        return transactionList.get(0);
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        logger.debug("Get currency ");
        String sql = "select * from currency where ID=?";
        List<String> stringList = jdbcTemplate.queryForList(sql, new Object[]{id}, String.class);
        if (stringList.isEmpty()) {
            return null;
        }
        return stringList.get(0);
    }

    @Override
    public List<Transaction> getAllByFlockId(Long flockId) throws SQLException {
        logger.debug("Get all transactions data with flock id {} ", flockId);
        String sql = "select * from transactions where FlockID=?";

        DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
        ResultSet rs = dbmd.getTables(null, "APP", "TRANSACTION", null);
        if (!rs.next()) {
            System.out.println("not exist");
        }

        return jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.transaction());
    }
}
