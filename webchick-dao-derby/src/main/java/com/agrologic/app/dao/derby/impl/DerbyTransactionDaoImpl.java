package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.TransactionDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyTransactionDaoImpl extends TransactionDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyTransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "TRANSACTIONS", null);
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table TRANSACTIONS from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE TRANSACTIONS ( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL, "
                + "NAME VARCHAR(45) NOT NULL, "
                + "EXPENSES DOUBLE NOT NULL, "
                + "REVENUES DOUBLE NOT NULL "
                + ")";
        jdbcTemplate.execute(sql);
    }


    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.TRANSACTIONS ";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Transaction transaction) throws SQLException {
//        String sqlQuery = "INSERT INTO TRANSACTIONS ( FLOCKID, NAME, EXPENSES, REVENUES ) "
//                + " VALUES (?,?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, transaction.getFlockId());
//            prepstmt.setString(2, transaction.getName());
//            prepstmt.setFloat(3, transaction.getExpenses());
//            prepstmt.setFloat(4, transaction.getRevenues());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert Transaction To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.TRANSACTIONS ";
        jdbcTemplate.execute(sql);
    }
}



