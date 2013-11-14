package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.TableDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyTableDaoImpl extends TableDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyTableDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "SCREENTABLE", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table SCREENTABLE from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableScreenTable();
        createTableScreenTableByLanguage();
    }

    private void createTableScreenTable() throws SQLException {
        String sql = "CREATE TABLE SCREENTABLE " + "(TABLEID INT NOT NULL , " + "SCREENID INT NOT NULL , "
                + "PROGRAMID INT NOT NULL , " + "TITLE VARCHAR(250) NOT NULL, "
                + "DISPLAYONSCREEN VARCHAR(10) NOT NULL, " + "POSITION INT NOT NULL, "
                + "PRIMARY KEY (TABLEID,SCREENID,PROGRAMID), "
                + "FOREIGN KEY (SCREENID,PROGRAMID) REFERENCES SCREENS(SCREENID,PROGRAMID))";
        jdbcTemplate.execute(sql);

    }

    private void createTableScreenTableByLanguage() throws SQLException {
        String sql = "CREATE TABLE TABLEBYLANGUAGE " + "(TABLEID INT NOT NULL , " + "LANGID INT NOT NULL , "
                + "UNICODETITLE VARCHAR(500) NOT NULL, " + "PRIMARY KEY (TABLEID, LANGID))";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Table table) throws SQLException {
//        String sql = "insert into screentable values (?,?,?,?,?,?)";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setObject(1, table.getId());
//            prepstmt.setLong(2, table.getScreenId());
//            prepstmt.setLong(3, table.getProgramId());
//            prepstmt.setString(4, table.getTitle());
//            prepstmt.setString(5, table.getDisplay());
//            prepstmt.setInt(6, table.getPosition());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//
//            throw new SQLException("Cannot Insert ScreenTable To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.SCREENTABLE ";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.SCREENTABLE ";
        jdbcTemplate.execute(sql);
    }
}



