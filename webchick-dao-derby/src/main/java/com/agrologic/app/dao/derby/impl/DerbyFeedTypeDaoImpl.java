
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.FeedTypeDaoImpl;
import com.agrologic.app.model.FeedType;
import java.sql.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyFeedTypeDaoImpl extends FeedTypeDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyFeedTypeDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "FEEDTYPES", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table FEEDTYPES from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE FEEDTYPES ( "
                + "ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FEEDTYPE VARCHAR(100) NOT NULL, "
                + "PRICE DOUBLE NOT NULL, "
                + "CELLINKID INT NOT NULL)";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot create new FEEDTYPES Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.FEEDTYPES ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Can not drop table feedtype ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(FeedType feedType) throws SQLException {
        String sqlQuery = "INSERT INTO FEEDTYPES (FEEDTYPE, PRICE, CELLINKID) "
                + "VALUES (?,?,?) " ;
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, feedType.getFeedType());
            prepstmt.setFloat(2, feedType.getPrice());
            prepstmt.setLong(3, feedType.getCellinkId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert FeedTypes To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.FEEDTYPE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table feedtype ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



