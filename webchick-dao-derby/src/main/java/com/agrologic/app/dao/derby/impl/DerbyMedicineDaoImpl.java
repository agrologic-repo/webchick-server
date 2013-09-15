package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.MedicineDaoImpl;
import com.agrologic.app.model.Medicine;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DerbyMedicineDaoImpl extends MedicineDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyMedicineDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory daoFactory) {
        super(jdbcTemplate, daoFactory);
    }


    @Override
    public void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE MEDICINE ( "
                + "ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT  NOT NULL, "
                + "AMOUNT INT  NOT NULL, "
                + "NAME VARCHAR(50) NOT NULL, "
                + "PRICE DOUBLE NOT NULL, "
                + "TOTAL DOUBLE NOT NULL "
                + ")";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new MEDICINE Table", e);
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
            ResultSet rs = dbmd.getTables(null, "APP", "MEDICINE", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table MEDICINE from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.MEDICINE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table medicine ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Medicine medicnie) throws SQLException {
        String sqlQuery = "INSERT INTO MEDICINE ( FLOCKID, AMOUNT,NAME,PRICE, TOTAL ) "
                + " VALUES (?,?,?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, medicnie.getFlockId());
            prepstmt.setInt(2, medicnie.getAmount());
            prepstmt.setString(3, medicnie.getName());
            prepstmt.setFloat(4, medicnie.getPrice());
            prepstmt.setFloat(5, medicnie.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Medicine To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.MEDICINE ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table medicine ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



