
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.LaborDaoImpl;
import com.agrologic.app.model.Labor;
import java.sql.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyLaborDaoImpl extends LaborDaoImpl implements CreatebleDao, RemovebleDao {

    public DerbyLaborDaoImpl() {
        this(DaoType.DERBY);
    }

    public DerbyLaborDaoImpl(DaoType daoType) {
        super(daoType);
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE LABOR "
                + "( "
                + "ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + " DATE VARCHAR(10) DEFAULT NULL,"
                + " WORKERID INT  NOT NULL,"
                + " HOURS INT  NOT NULL,"
                + " SALARY DOUBLE NOT NULL,"
                + " FLOCKID INT  NOT NULL "
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new labor Table", e);
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
            ResultSet rs = dbmd.getTables(null, "APP", "LABOR", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table DESTRIBUTE from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.LABOR ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table labor ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Labor labor) throws SQLException {

        String sqlQuery = "INSERT INTO LABOR ( DATE, WORKERID, HOURS, SALARY, FLOCKID ) "
                + " VALUES (?,?,?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, labor.getDate());
            prepstmt.setLong(2, labor.getWorkerId());
            prepstmt.setInt(3, labor.getHours());
            prepstmt.setFloat(4, labor.getSalary());
            prepstmt.setLong(5, labor.getFlockId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Labor To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.LABOR ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table labor ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
