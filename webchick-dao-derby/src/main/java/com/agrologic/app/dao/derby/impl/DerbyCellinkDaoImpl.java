package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.CellinkDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyCellinkDaoImpl extends CellinkDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {
    public final static String APP_SCHEMA = "APP";
    public final static String CELLINK_TABLE = "CELLINKS";

    public DerbyCellinkDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, APP_SCHEMA, CELLINK_TABLE, null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table cellink from DataBase", e);
        }
        return true;
    }

    @Override
    public void createTable() throws SQLException {
        logger.info("create table cellink if not exist");
        String sql = "CREATE TABLE CELLINKS ( " + "CELLINKID INT NOT NULL,  " + "NAME VARCHAR(25) NOT NULL, "
                + "PASSWORD VARCHAR(25) NOT NULL, " + "USERID INT NOT NULL, "
                + "TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL, " + "PORT INT DEFAULT 0, "
                + "IP VARCHAR(16), " + "STATE INT DEFAULT 0 , " + "SCREENID INT DEFAULT 1 , "
                + "SIM VARCHAR(15), " + "ACTUAL SMALLINT DEFAULT 0 , " + "TYPE VARCHAR(45) , "
                + "VERSION VARCHAR(45) , " + "PRIMARY KEY (CELLINKID), "
                + "FOREIGN KEY (USERID) REFERENCES USERS(USERID))";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Cellink cellink) throws SQLException {
//        logger.debug("Creating cellink with name [{}]", cellink.getName());
//        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
//        valuesToInsert.put("cellinkid", cellink.getId());
//        valuesToInsert.put("name", cellink.getName());
//        valuesToInsert.put("password", cellink.getPassword());
//        valuesToInsert.put("userid", cellink.getUserId());
//        valuesToInsert.put("sim", cellink.getSimNumber());
//        valuesToInsert.put("type", cellink.getType());
//        valuesToInsert.put("version", cellink.getVersion());
//        valuesToInsert.put("time", cellink.getTime());
//        valuesToInsert.put("state", cellink.getState());
//        valuesToInsert.put("screenid", cellink.getScreenId());
//        valuesToInsert.put("actual", cellink.isActual());
//        jdbcInsert.execute(valuesToInsert);
//    }

    @Override
    public void dropTable() throws SQLException {
        logger.debug("Drop cellink table");
        String sql = "DROP TABLE APP.CELLINK ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        logger.debug("Delete cellink data ");
        String sql = "DELETE FROM APP.CELLINK ";
        jdbcTemplate.execute(sql);
    }
}



