package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ActionSetDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyActionSetDaoImpl extends ActionSetDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyActionSetDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "ACTIONSET", null);
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table ACTIONSET from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableActionSet();
        createTableActionSetByLang();
    }

    private void createTableActionSet() throws SQLException {
        String sql = "CREATE TABLE ACTIONSET " + "("
                + "DATAID INT NOT NULL , "
                + "VALUEID INT NOT NULL , "
                + "LABEL  varchar(150) NOT NULL, "
                + "PRIMARY KEY (VALUEID)" + ")";
        jdbcTemplate.execute(sql);
    }

    private void createTableActionSetByLang() throws SQLException {
        logger.info("create table action set by language if not exist");
        String sql = "CREATE TABLE ACTIONSETBYLANGUAGE (VALUEID INT NOT NULL , LANGID INT NOT NULL , " +
                "UNICODETEXT VARCHAR(200) NOT NULL, PRIMARY KEY (VALUEID,LANGID))";
        jdbcTemplate.execute(sql);

    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.ACTIONSET ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.ACTIONSET ";
        jdbcTemplate.execute(sql);
    }
}



