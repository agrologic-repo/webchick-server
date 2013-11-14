package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ScreenDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

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

//    @Override
//    public void insertTranslation(Collection<Screen> screenList, Long langId) throws SQLException {
//        String sqlSelectQuery = "SELECT COUNT(UNICODETITLE) AS EXIST FROM SCREENBYLANGUAGE WHERE SCREENID=? AND LANGID=?";
//        String sqlInsertQuery = "INSERT INTO SCREENBYLANGUAGE (SCREENID, LANGID, UNICODETITLE) VALUES (?, ?, ?)";
//        String sqlUpdateQuery = "UPDATE SCREENBYLANGUAGE SET UNICODETITLE=? WHERE SCREENID=? AND LANGID=?";
//        PreparedStatement prepstmtSelect = null;
//        PreparedStatement prepstmtInsert = null;
//        PreparedStatement prepstmtUpdate = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
//            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
//            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
//            con.setAutoCommit(false);
//            int i = 0;
//            int u = 0;
//            for (Screen screen : screenList) {
//                prepstmtSelect.setLong(1, screen.getId());
//                prepstmtSelect.setLong(2, langId);
//
//                ResultSet rs = prepstmtSelect.executeQuery();
//
//                if (rs.next()) {
//
//                    // turn off autocommit
//                    int exist = rs.getInt("exist");
//
//                    if (exist == 1) {
//                        prepstmtUpdate.setLong(2, screen.getId());
//                        prepstmtUpdate.setLong(3, langId);
//                        prepstmtUpdate.setString(1, (screen.getUnicodeTitle() == null)
//                                ? ""
//                                : screen.getUnicodeTitle());
//                        prepstmtUpdate.addBatch();
//                        u++;
//                    } else {
//                        prepstmtInsert.setLong(1, screen.getId());
//                        prepstmtInsert.setLong(2, langId);
//                        prepstmtInsert.setString(3, (screen.getUnicodeTitle() == null)
//                                ? ""
//                                : screen.getUnicodeTitle());
//                        prepstmtInsert.addBatch();
//                        i++;
//                    }
//                }
//            }
//            if (i > 0) {
//                prepstmtInsert.executeBatch();
//            }
//            if (u > 0) {
//                prepstmtUpdate.executeBatch();
//            }
//            con.commit();
//            con.setAutoCommit(true);
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//
//            if (con != null) {
//                try {
//                    con.rollback();
//                } catch (SQLException ex) {
//                    dao.printSQLException(ex);
//
//                    throw new SQLException(TRANSACTION_ROLLED_BACK, ex);
//                }
//            }
//        } finally {
//            prepstmtSelect.close();
//            prepstmtInsert.close();
//            prepstmtUpdate.close();
//            dao.closeConnection(con);
//        }
//    }

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



