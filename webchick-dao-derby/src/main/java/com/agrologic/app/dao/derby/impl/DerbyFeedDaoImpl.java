package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.FeedDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyFeedDaoImpl extends FeedDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFeedDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FEED", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table FEED from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE FEED "
                + "( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL , "
                + "TYPE VARCHAR(45) , "
                + "AMOUNT INT NOT NULL, "
                + "DATE VARCHAR(20),"
                + "ACCOUNTNUMBER INT ,"
                + "TOTAL DOUBLE"
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.FEED ";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Feed feed) throws SQLException {
//        String sql = "INSERT INTO FEED ( FLOCKID, TYPE, AMOUNT, DATE, ACCOUNTNUMBER, TOTAL ) "
//                + " VALUES (?,?,?,?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setLong(1, feed.getFlockId());
//            prepstmt.setLong(2, feed.getType());
//            prepstmt.setInt(3, feed.getAmount());
//            prepstmt.setString(4, feed.getDate());
//            prepstmt.setFloat(5, feed.getNumberAccount());
//            prepstmt.setFloat(6, feed.getTotal());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert Feed To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.FEED ";
        jdbcTemplate.execute(sql);
    }
}



