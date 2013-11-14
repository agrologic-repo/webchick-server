package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.DataDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyDataDaoImpl extends DataDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyDataDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "DATATABLE", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table DATATABLE from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createDataTable();
        createTableDataByLang();
        createTableData();
        createSpecialDataLabel();
    }

    public void createDataTable() throws SQLException {
        logger.debug("Create DATATABLE table");
        String sql = "CREATE TABLE DATATABLE " + "(DATAID INT NOT NULL , " + "TYPE INT NOT NULL , "
                + "STATUS SMALLINT NOT NULL, " + "READONLY SMALLINT NOT NULL, "
                + "TITLE VARCHAR(100) NOT NULL, " + "FORMAT INT NOT NULL, " + "LABEL VARCHAR(100) NOT NULL, "
                + "ISRELAY SMALLINT NOT NULL, " + "ISSPECIAL INT NOT NULL, " + "PRIMARY KEY (DATAID))";
        jdbcTemplate.execute(sql);
    }

    public void createTableData() throws SQLException {
        logger.debug("Create TABLEDATA table");
        String sql = "CREATE TABLE TABLEDATA " + "(DATAID INT NOT NULL , " + "TABLEID INT NOT NULL , "
                + "SCREENID INT NOT NULL, " + "PROGRAMID INT NOT NULL, "
                + "DISPLAYONTABLE VARCHAR(10) NOT NULL, " + "POSITION INT NOT NULL, "
                + "PRIMARY KEY (DATAID, TABLEID,SCREENID, PROGRAMID))";
        jdbcTemplate.execute(sql);
    }

    public void createTableDataByLang() throws SQLException {
        logger.debug("Create DATABYLANGUAGE table");
        String sql = "CREATE TABLE DATABYLANGUAGE " + "(DATAID INT NOT NULL , " + " LANGID INT NOT NULL , "
                + "UNICODELABEL VARCHAR(500) NOT NULL, "
                + "CONSTRAINT DTBYLANG_PK PRIMARY KEY (DATAID, LANGID))";
        jdbcTemplate.execute(sql);
    }

    public void createSpecialDataLabel() throws SQLException {
        logger.debug("Create SPECIALDATALABELS table");
        String sql = "CREATE TABLE SPECIALDATALABELS " + "(DATAID INT NOT NULL , " + "PROGRAMID INT NOT NULL , "
                + "LANGID INT NOT NULL , " + "SPECIALLABEL VARCHAR(500) NOT NULL, "
                + "PRIMARY KEY (DATAID,PROGRAMID,LANGID))";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        logger.debug("Drop DATATABLE table");
        String sql = "DROP TABLE APP.DATATABLE ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        logger.debug("Drop DATATABLE table");
        String sql = "DELETE  FROM APP.DATATABLE ";
        jdbcTemplate.execute(sql);
    }
}



