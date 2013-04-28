
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.DistribDaoImpl;
import com.agrologic.app.model.Distrib;
import java.sql.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyDistribDaoImpl extends DistribDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyDistribDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE DISTRIBUTE ( "
                + " ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + " FLOCKID INT ,"
                + " DATE VARCHAR(10) ,"
                + " ACCOUNTNUMBER INT ,"
                + " SEX VARCHAR(45) ,"
                + " TARGET VARCHAR(45) ,"
                + " NUMOFBIRDS INT ,"
                + " WEIGHT INT ,"
                + " QUANTITYA INT ,"
                + " QUANTITYB INT  ,"
                + " QUANTITYC INT  ,"
                + " BADVETERINARY INT  ,"
                + " BADANOTHER INT  ,"
                + " PRICEA DOUBLE ,"
                + " PRICEB DOUBLE ,"
                + " PRICEC DOUBLE ,"
                + " AGEDISTREB INT  ,"
                + " AVERAGEWEIGHT INT  ,"
                + " DTA VARCHAR(45) ,"
                + " DTB VARCHAR(45) ,"
                + " DTC VARCHAR(45) ,"
                + " DTVETERINARY VARCHAR(45) ,"
                + " DTANOTHER VARCHAR(45) ,"
                + " CALCSUM DOUBLE ,"
                + " HANDSUM DOUBLE ,"
                + " TOTAL DOUBLE )";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new DISTRIBUTE Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;
        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "DISTRIBUTE", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get table DISTRIBUTE from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }
        return true;
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.DISTRIBUTE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table DISTRIBUTE ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Distrib distrib) throws SQLException {
        String sqlQuery = "INSERT INTO DISTRIBUTE ( FLOCKID, ACCOUNTNUMBER, SEX, TARGET,"
                + " NUMOFBIRDS, WEIGHT, QUANTITYA, QUANTITYB, QUANTITYC, BADVETERINARY,BADANOTHER, "
                + " PRICEA, PRICEB, PRICEC, AGEDISTREB, AVERAGEWEIGHT, DTA, DTB, DTC ,"
                + " DTVETERINARY, DTANOTHER, CALCSUM , HANDSUM, TOTAL) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, distrib.getFlockId());
            prepstmt.setInt(2, distrib.getAccountNumber());
            prepstmt.setString(3, distrib.getSex());
            prepstmt.setString(4, distrib.getTarget());
            prepstmt.setInt(5, distrib.getNumOfBirds());
            prepstmt.setInt(6, distrib.getWeight());
            prepstmt.setInt(7, distrib.getQuantityA());
            prepstmt.setInt(8, distrib.getQuantityB());
            prepstmt.setInt(9, distrib.getQuantityC());
            prepstmt.setInt(10, distrib.getBadVeterinary());
            prepstmt.setInt(11, distrib.getBadAnother());
            prepstmt.setDouble(12, distrib.getPriceA());
            prepstmt.setDouble(13, distrib.getPriceB());
            prepstmt.setDouble(14, distrib.getPriceC());
            prepstmt.setInt(15, distrib.getAgeDistrib());
            prepstmt.setInt(16, distrib.getAverageWeight());
            prepstmt.setString(17, distrib.getDtA());
            prepstmt.setString(18, distrib.getDtB());
            prepstmt.setString(19, distrib.getDtC());
            prepstmt.setString(20, distrib.getDtVeterinary());
            prepstmt.setString(21, distrib.getDtAnother());
            prepstmt.setDouble(22, distrib.getCalcSum());
            prepstmt.setDouble(23, distrib.getHandSum());
            prepstmt.setDouble(24, distrib.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Distribute To The DataBase ");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.DISTRIB ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table distrib ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }

    }
}



