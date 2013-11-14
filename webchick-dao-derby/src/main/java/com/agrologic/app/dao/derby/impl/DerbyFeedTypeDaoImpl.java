package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.FeedTypeDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyFeedTypeDaoImpl extends FeedTypeDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFeedTypeDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();

            ResultSet rs = dbmd.getTables(null, "APP", "FEEDTYPES", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table FEEDTYPES from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE FEEDTYPES ( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FEEDTYPE VARCHAR(100) NOT NULL, "
                + "PRICE DOUBLE NOT NULL, "
                + "CELLINKID INT NOT NULL)";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.FEEDTYPES ";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(FeedType feedType) throws SQLException {
//        String sql = "INSERT INTO FEEDTYPES (FEEDTYPE, PRICE, CELLINKID) "
//                + "VALUES (?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setString(1, feedType.getFeedType());
//            prepstmt.setFloat(2, feedType.getPrice());
//            prepstmt.setLong(3, feedType.getCellinkId());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert FeedTypes To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.FEEDTYPE ";
        jdbcTemplate.execute(sql);

    }
}



