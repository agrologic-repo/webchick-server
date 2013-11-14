package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.LaborDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyLaborDaoImpl extends LaborDaoImpl implements CreatebleDao, RemovebleDao {

    public DerbyLaborDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE LABOR "
                + "( "
                + "ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + " DATE VARCHAR(10) DEFAULT NULL,"
                + " WORKERID INT  NOT NULL,"
                + " HOURS INT  NOT NULL,"
                + " SALARY DOUBLE NOT NULL,"
                + " FLOCKID INT  NOT NULL "
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "LABOR", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table DESTRIBUTE from DataBase", e);
        }

        return true;
    }

    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.LABOR ";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Labor labor) throws SQLException {
//
//        String sql = "INSERT INTO LABOR ( DATE, WORKERID, HOURS, SALARY, FLOCKID ) "
//                + " VALUES (?,?,?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setString(1, labor.getDate());
//            prepstmt.setLong(2, labor.getWorkerId());
//            prepstmt.setInt(3, labor.getHours());
//            prepstmt.setFloat(4, labor.getSalary());
//            prepstmt.setLong(5, labor.getFlockId());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert Labor To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.LABOR ";
        jdbcTemplate.execute(sql);
    }
}



