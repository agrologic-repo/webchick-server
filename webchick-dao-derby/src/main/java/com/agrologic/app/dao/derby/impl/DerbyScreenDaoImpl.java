package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ScreenDaoImpl;
import com.agrologic.app.model.Screen;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DerbyScreenDaoImpl extends ScreenDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyScreenDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "SCREENS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(CANNOT_EXECUTE_QUERY, e);
        }
        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableScreen();
        createTableScreenByLanguage();
    }

    public void createTableScreen() throws SQLException {
        String sql = "CREATE TABLE SCREENS " + "(SCREENID INT NOT NULL , " + "PROGRAMID INT NOT NULL , "
                + "TITLE VARCHAR(150) NOT NULL, " + "DISPLAYONPAGE VARCHAR(10) NOT NULL DEFAULT 'yes', "
                + "POSITION INT NOT NULL, " + "DESCRIPT VARCHAR(250) NOT NULL, "
                + "PRIMARY KEY (SCREENID,PROGRAMID), "
                + "FOREIGN KEY (PROGRAMID) REFERENCES PROGRAMS(PROGRAMID))";
        jdbcTemplate.execute(sql);
    }

    public void createTableScreenByLanguage() throws SQLException {
        String sql = "CREATE TABLE SCREENBYLANGUAGE " + "(SCREENID INT NOT NULL , " + "LANGID INT NOT NULL , "
                + "UNICODETITLE VARCHAR(1000) NOT NULL, " + "PRIMARY KEY (SCREENID, LANGID))";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Screen screen) throws SQLException {
//        String sql = "insert into screens values (?,?,?,?,?,?)";
//        jdbcTemplate.execute(sql);
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setObject(1, screen.getId());
//            prepstmt.setLong(2, screen.getProgramId());
//            prepstmt.setString(3, screen.getTitle());
//            prepstmt.setString(4, screen.getDisplay());
//            prepstmt.setInt(5, screen.getPosition());
//            prepstmt.setString(6, screen.getDescript());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//
//            throw new SQLException(CANNOT_INSERT_SCREEN_MESSAGE, e);
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void insertTranslation(Collection<Screen> screenList, final Long langId) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(UNICODETITLE) AS EXIST FROM SCREENBYLANGUAGE WHERE SCREENID=? AND LANGID=?";
        String sqlUpdateQuery = "UPDATE SCREENBYLANGUAGE SET UNICODETITLE=? WHERE SCREENID=? AND LANGID=?";

        final Collection<Screen> screensToUpdate = new ArrayList<Screen>();


        for (Screen screen : screenList) {
            int exist = jdbcTemplate.queryForInt(sqlSelectQuery, screen.getId(), langId);
            if (exist == 1) {
                screensToUpdate.add(screen);
            } else {
                SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
                simpleJdbcInsert.setTableName("SCREENBYLANGUAGE");
                Map<String, Object> valuesToInsert = new HashMap<String, Object>();
                valuesToInsert.put("SCREENID", screen.getId());
                valuesToInsert.put("LANGID", langId);
                valuesToInsert.put("UNICODETITLE", screen.getUnicodeTitle());
                simpleJdbcInsert.execute(valuesToInsert);
            }
        }

        jdbcTemplate.batchUpdate(sqlUpdateQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Screen screen = Lists.newArrayList(screensToUpdate).get(i);
                ps.setString(1, screen.getUnicodeTitle());
                ps.setLong(2, screen.getId());
                ps.setLong(3, langId);
            }

            @Override
            public int getBatchSize() {
                return screensToUpdate.size();
            }
        });
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.SCREENS ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.SCREENS ";
        jdbcTemplate.execute(sql);
    }
}



