package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.RelayDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyRelayDaoImpl extends RelayDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyRelayDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "RELAYNAMES", null);
            if (!rs.next()) {
                return false;
            }

            rs = dbmd.getTables(null, "APP", "RELAYBYLANGUAGE", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table RELAYNAMES from DataBase", e);
        }
        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableRelay();
        createTableRelayByLang();
    }

    private void createTableRelay() throws SQLException {
        String sql = "CREATE TABLE RELAYNAMES " + "(ID INT NOT NULL , " + "NAME VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (ID))";
        jdbcTemplate.execute(sql);

    }

    private void createTableRelayByLang() throws SQLException {
        String sql = "CREATE TABLE RELAYBYLANGUAGE " + "(RELAYID INT NOT NULL , " + "LANGID INT NOT NULL , "
                + "UNICODETEXT VARCHAR(200) NOT NULL, " + "PRIMARY KEY (RELAYID,LANGID))";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.RELAY ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.RELAY ";
        jdbcTemplate.execute(sql);
    }
}



