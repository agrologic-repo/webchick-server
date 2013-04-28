
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;


import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ScreenDaoImpl;

import com.agrologic.app.model.Screen;



import java.sql.*;

import java.util.Collection;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyScreenDaoImpl extends ScreenDaoImpl implements CreatebleDao , DropableDao , RemovebleDao {

    public DerbyScreenDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "SCREENS", null);
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_EXECUTE_QUERY, e);
        } finally {
            dao.closeConnection(con);
        }
        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createTableScreen();
        createTableScreenByLanguage();
    }

    public void createTableScreen() throws SQLException {
        String sqlQuery = "CREATE TABLE SCREENS " + "(SCREENID INT NOT NULL , " + "PROGRAMID INT NOT NULL , "
                + "TITLE VARCHAR(150) NOT NULL, " + "DISPLAYONPAGE VARCHAR(10) NOT NULL DEFAULT 'yes', "
                + "POSITION INT NOT NULL, " + "DESCRIPT VARCHAR(250) NOT NULL, "
                + "PRIMARY KEY (SCREENID,PROGRAMID), "
                + "FOREIGN KEY (PROGRAMID) REFERENCES PROGRAMS(PROGRAMID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_CREATE_TABLE, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    public void createTableScreenByLanguage() throws SQLException {
        String sqlQuery = "CREATE TABLE SCREENBYLANGUAGE " + "(SCREENID INT NOT NULL , " + "LANGID INT NOT NULL , "
                + "UNICODETITLE VARCHAR(1000) NOT NULL, " + "PRIMARY KEY (SCREENID, LANGID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_CREATE_TABLE, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Screen screen) throws SQLException {
        String sqlQuery = "insert into screens values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, screen.getId());
            prepstmt.setLong(2, screen.getProgramId());
            prepstmt.setString(3, screen.getTitle());
            prepstmt.setString(4, screen.getDisplay());
            prepstmt.setInt(5, screen.getPosition());
            prepstmt.setString(6, screen.getDescript());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_INSERT_SCREEN_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Collection<Screen> screenList, Long langId) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(UNICODETITLE) AS EXIST FROM SCREENBYLANGUAGE WHERE SCREENID=? AND LANGID=?";
        String sqlInsertQuery = "INSERT INTO SCREENBYLANGUAGE (SCREENID, LANGID, UNICODETITLE) VALUES (?, ?, ?)";
        String sqlUpdateQuery = "UPDATE SCREENBYLANGUAGE SET UNICODETITLE=? WHERE SCREENID=? AND LANGID=?";
        PreparedStatement prepstmtSelect = null;
        PreparedStatement prepstmtInsert = null;
        PreparedStatement prepstmtUpdate = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
            con.setAutoCommit(false);
            int i = 0;
            int u = 0;
            for (Screen screen : screenList) {
                prepstmtSelect.setLong(1, screen.getId());
                prepstmtSelect.setLong(2, langId);

                ResultSet rs = prepstmtSelect.executeQuery();

                if (rs.next()) {

                    // turn off autocommit
                    int exist = rs.getInt("exist");

                    if (exist == 1) {
                        prepstmtUpdate.setLong(2, screen.getId());
                        prepstmtUpdate.setLong(3, langId);
                        prepstmtUpdate.setString(1, (screen.getUnicodeTitle() == null)
                                ? ""
                                : screen.getUnicodeTitle());
                        prepstmtUpdate.addBatch();
                        u++;
                    } else {
                        prepstmtInsert.setLong(1, screen.getId());
                        prepstmtInsert.setLong(2, langId);
                        prepstmtInsert.setString(3, (screen.getUnicodeTitle() == null)
                                ? ""
                                : screen.getUnicodeTitle());
                        prepstmtInsert.addBatch();
                        i++;
                    }
                }
            }
            if (i > 0) {
                prepstmtInsert.executeBatch();
            }
            if (u > 0) {
                prepstmtUpdate.executeBatch();
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);

                    throw new SQLException(TRANSACTION_ROLLED_BACK, ex);
                }
            }
        } finally {
            prepstmtSelect.close();
            prepstmtInsert.close();
            prepstmtUpdate.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.SCREENS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table screens ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.SCREENS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table screens ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}



