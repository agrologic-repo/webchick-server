
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.TransactionDaoImpl;
import com.agrologic.app.model.Transaction;
import java.sql.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyTransactionDaoImpl extends TransactionDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyTransactionDaoImpl() {
        this(DaoType.DERBY);
    }

    public DerbyTransactionDaoImpl(DaoType daoType) {
        super(daoType);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "TRANSACTIONS", null);
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table TRANSACTION from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE TRANSACTIONS ( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT NOT NULL, "
                + "NAME VARCHAR(45) NOT NULL, "
                + "EXPENSES DOUBLE NOT NULL, "
                + "REVENUES DOUBLE NOT NULL "
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Cannot create new TRANSACTION Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }


    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.TRANSACTIONS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table transaction ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Transaction transaction) throws SQLException {
        String sqlQuery = "INSERT INTO TRANSACTIONS ( FLOCKID, NAME, EXPENSES, REVENUES ) "
                + " VALUES (?,?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, transaction.getFlockId());
            prepstmt.setString(2, transaction.getName());
            prepstmt.setFloat(3, transaction.getExpenses());
            prepstmt.setFloat(4, transaction.getRevenues());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Transaction To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.TRANSACTIONS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table transaction ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
