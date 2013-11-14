package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.WorkerDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyWorkerDaoImpl extends WorkerDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyWorkerDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "WORKERS", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table workers from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE WORKERS "
                + "( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "NAME VARCHAR(45) NOT NULL, "
                + "DEFINE VARCHAR(100) NOT NULL, "
                + "PHONE VARCHAR(20) NOT NULL, "
                + "HOURCOST DOUBLE NOT NULL, "
                + "CELLINKID INT NOT NULL "
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.WORKERS ";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Worker worker) throws SQLException {
//        String sqlQuery = "INSERT INTO WORKERS ( NAME, DEFINE, PHONE, HOURCOST, CELLINKID ) "
//                + " VALUES (?,?,?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setString(1, worker.getName());
//            prepstmt.setString(2, worker.getDefine());
//            prepstmt.setString(3, worker.getPhone());
//            prepstmt.setFloat(4, worker.getHourCost());
//            prepstmt.setLong(5, worker.getCellinkId());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert Worker To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.WORKERS ";
        jdbcTemplate.execute(sql);
    }
}



