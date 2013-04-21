
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.ControllerDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class DerbyControllerDaoImpl extends ControllerDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyControllerDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean tableExist() throws SQLException {
        Connection con = null;

        try {
            con = dao.getConnection();

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, "APP", "CONTROLLERS", null);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot get  table Cellink from DataBase", e);
        } finally {
            dao.closeConnection(con);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        createControllerTable();
        createControllerDataTable();
        createNewControllerData();
        createGraphTable();
    }

    private void createControllerTable() throws SQLException {
        String sqlQuery = "CREATE TABLE CONTROLLERS ( " + "CONTROLLERID INT NOT NULL,  " + "CELLINKID INT NOT NULL, "
                + "TITLE VARCHAR(200) , " + "NETNAME VARCHAR(45) , " + "CONTROLLERNAME VARCHAR(45) , "
                + "PROGRAMID INT NOT NULL, " + "AREA INT DEFAULT 0, " + "ACTIVE SMALLINT DEFAULT 0, "
                + "PRIMARY KEY (CONTROLLERID), " + " FOREIGN KEY (CELLINKID) REFERENCES CELLINKS(CELLINKID)) ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new Controller Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createControllerDataTable() throws SQLException {
        String sqlQuery = "CREATE TABLE CONTROLLERDATA ( " + "DATAID INT NOT NULL," + "CONTROLLERID INT NOT NULL,"
                + "VALUE  INT DEFAULT 0," + "PRIMARY KEY (DATAID, CONTROLLERID),"
                + "FOREIGN KEY (DATAID) REFERENCES DATATABLE (DATAID), "
                + "FOREIGN KEY (CONTROLLERID) REFERENCES CONTROLLERS (CONTROLLERID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create Controllerdata Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createNewControllerData() throws SQLException {
        String sqlQuery = "CREATE TABLE NEWCONTROLLERDATA " + "(CONTROLLERID INT NOT NULL, " + "DATAID INT NOT NULL , "
                + "VALUE INT NOT NULL ," + "CONSTRAINT CNTRLDTA_PK PRIMARY KEY (CONTROLLERID,DATAID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create Newcontrollerdata Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    private void createGraphTable() throws SQLException {
        String sqlQuery = "CREATE TABLE GRAPH24HOURS " + "(CONTROLLERID INT NOT NULL, "
                + "DATASET  VARCHAR(5000) , "
                + "UPDATETIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "CONSTRAINT CNTRGRAPH_PK PRIMARY KEY (CONTROLLERID))";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.execute(sqlQuery);
        } catch (Exception e) {
            throw new SQLException("Cannot create new GRAPH24HOURS Table", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Controller controller) throws SQLException {
        insertControllers(controller);
        insertControllerDataValues(controller.getId(), controller.getDataValues());
    }

    private void insertControllers(Controller controller) throws SQLException {
        String sqlQuery = "INSERT INTO CONTROLLERS "
                + "(CONTROLLERID, CELLINKID, TITLE, NETNAME, CONTROLLERNAME, PROGRAMID, AREA, ACTIVE) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, controller.getId());
            prepstmt.setLong(2, controller.getCellinkId());
            prepstmt.setString(3, controller.getTitle());
            prepstmt.setString(4, controller.getNetName());
            prepstmt.setString(5, controller.getName());
            prepstmt.setLong(6, controller.getProgramId());
            prepstmt.setInt(7, controller.getArea());
            prepstmt.setBoolean(8, controller.isActive());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot New Controller To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertControllerDataValues(Long controllerId, Iterator<Map.Entry<Long, Data>> dataValues)
            throws SQLException {
        String sqlQuery = "INSERT INTO CONTROLLERDATA (DATAID, CONTROLLERID, VALUE) VALUES(?,?,-1) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            while (dataValues.hasNext()) {
                Map.Entry<Long, Data> entry = dataValues.next();

                prepstmt.setLong(1, entry.getKey());
                prepstmt.setLong(2, controllerId);
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);

                    throw new SQLException("Transaction is being rolled back");
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateControllerData(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM CONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        String sqlInsertQuery = "INSERT INTO CONTROLLERDATA (CONTROLLERID, DATAID, VALUE) VALUES (?, ?, ?)";
        String sqlUpdateQuery = "UPDATE CONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        PreparedStatement prepstmtSelect = null;
        PreparedStatement prepstmtInsert = null;
        PreparedStatement prepstmtUpdate = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
            prepstmtSelect.setLong(1, controllerId);
            prepstmtSelect.setLong(2, dataId);

            ResultSet rs = prepstmtSelect.executeQuery();

            if (rs.next()) {

                // turn off autocommit
                int exist = rs.getInt("EXIST");

                if (exist == 1) {
                    prepstmtUpdate.setLong(1, value);
                    prepstmtUpdate.setLong(2, controllerId);
                    prepstmtUpdate.setLong(3, dataId);
                    prepstmtUpdate.executeUpdate();
                } else {
                    prepstmtInsert.setLong(1, controllerId);
                    prepstmtInsert.setLong(2, dataId);
                    prepstmtInsert.setLong(3, value);
                    prepstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Can't insert new row to controllerdata table ", e);
        } finally {
            prepstmtSelect.close();
            prepstmtInsert.close();
            prepstmtUpdate.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateControllerData(Long controllerId, Collection<Data> onlineData) throws SQLException {
        final String sqlSelectQuery =
                "SELECT COUNT(VALUE) AS EXIST FROM CONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        final String sqlInsertQuery = "INSERT INTO CONTROLLERDATA (CONTROLLERID, DATAID, VALUE) VALUES (?, ?, ?)";
        final String sqlUpdateQuery = "UPDATE CONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        PreparedStatement prepstmtSelect = null;
        PreparedStatement prepstmtInsert = null;
        PreparedStatement prepstmtUpdate = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
            prepstmtSelect.setLong(1, controllerId);

            for (Data dc : onlineData) {
                prepstmtSelect.setLong(1, controllerId);
                prepstmtSelect.setLong(2, dc.getId());

                ResultSet rs = prepstmtSelect.executeQuery();

                if (rs.next()) {

                    // turn off autocommit
                    int exist = rs.getInt("exist");

                    if (exist == 1) {
                        con.setAutoCommit(false);
                        prepstmtUpdate.setLong(2, controllerId);
                        prepstmtUpdate.setLong(3, dc.getId());
                        prepstmtUpdate.setLong(1, dc.getValue());
                        prepstmtUpdate.addBatch();
                    } else {
                        con.setAutoCommit(false);
                        prepstmtInsert.setLong(1, controllerId);
                        prepstmtInsert.setLong(2, dc.getId());
                        prepstmtInsert.setLong(3, dc.getValue());
                        prepstmtInsert.addBatch();
                    }
                }

                con.setAutoCommit(true);
            }

            con.setAutoCommit(false);
            prepstmtInsert.executeBatch();
            prepstmtUpdate.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);

                    throw new SQLException("Transaction is being rolled back");
                }
            }
        } finally {
            prepstmtSelect.close();
            prepstmtInsert.close();
            prepstmtUpdate.close();
            dao.closeConnection(con);
        }
    }

    public void updateControllerGraph(Long controllerId, String values, Timestamp updateTime) throws SQLException {
        final String sqlSelectQuery = "SELECT COUNT(DATASET) AS EXIST FROM GRAPH24HOURS WHERE CONTROLLERID=?";
        final String sqlInsertQuery = "INSERT INTO GRAPH24HOURS (CONTROLLERID, DATASET, UPDATETIME) VALUES (?, ?, ?)";
        final String sqlUpdateQuery = "UPDATE GRAPH24HOURS SET DATASET=? ,UPDATETIME=? WHERE CONTROLLERID=?";
        PreparedStatement prepstmtSelect = null;
        PreparedStatement prepstmtInsert = null;
        PreparedStatement prepstmtUpdate = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
            prepstmtSelect.setLong(1, controllerId);

            ResultSet rs = prepstmtSelect.executeQuery();

            if (rs.next()) {

                // turn off autocommit
                int exist = rs.getInt("exist");

                if (exist == 1) {
                    prepstmtUpdate.setString(1, values);
                    prepstmtUpdate.setTimestamp(2, updateTime);
                    prepstmtUpdate.setLong(3, controllerId);
                    prepstmtUpdate.executeUpdate();
                } else {
                    prepstmtInsert.setLong(1, controllerId);
                    prepstmtInsert.setString(2, values);
                    prepstmtInsert.setTimestamp(3, updateTime);
                    prepstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Transaction is being rolled back");
        } finally {
            prepstmtSelect.close();
            prepstmtInsert.close();
            prepstmtUpdate.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void sendNewDataValueToController(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM NEWCONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        String sqlInsertQuery = "INSERT INTO NEWCONTROLLERDATA (CONTROLLERID, DATAID, VALUE) VALUES (?, ?, ?)";
        String sqlUpdateQuery = "UPDATE NEWCONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        PreparedStatement prepstmtSelect = null;
        PreparedStatement prepstmtInsert = null;
        PreparedStatement prepstmtUpdate = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
            prepstmtSelect.setLong(1, controllerId);
            prepstmtSelect.setLong(2, dataId);

            ResultSet rs = prepstmtSelect.executeQuery();

            if (rs.next()) {

                // turn off autocommit
                int exist = rs.getInt("exist");

                if (exist == 1) {
                    prepstmtUpdate.setLong(2, controllerId);
                    prepstmtUpdate.setLong(3, dataId);
                    prepstmtUpdate.setLong(1, value);
                    prepstmtUpdate.executeUpdate();
                } else {
                    prepstmtInsert.setLong(1, controllerId);
                    prepstmtInsert.setLong(2, dataId);
                    prepstmtInsert.setLong(3, value);
                    prepstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Can't insert new row to newcontrollerdata table ", e);
        } finally {
            prepstmtSelect.close();
            prepstmtInsert.close();
            prepstmtUpdate.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveNewDataValueOnController(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlSelectQuery = "SELECT COUNT(VALUE) AS EXIST FROM CONTROLLERDATA WHERE CONTROLLERID=? AND DATAID=?";
        String sqlInsertQuery = "INSERT INTO CONTROLLERDATA (CONTROLLERID, DATAID, VALUE) VALUES (?, ?, ?)";
        String sqlUpdateQuery = "UPDATE CONTROLLERDATA SET VALUE=? WHERE CONTROLLERID=? AND DATAID=?";
        PreparedStatement prepstmtSelect = null;
        PreparedStatement prepstmtInsert = null;
        PreparedStatement prepstmtUpdate = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmtSelect = con.prepareStatement(sqlSelectQuery);
            prepstmtInsert = con.prepareStatement(sqlInsertQuery);
            prepstmtUpdate = con.prepareStatement(sqlUpdateQuery);
            prepstmtSelect.setLong(1, controllerId);
            prepstmtSelect.setLong(2, dataId);

            ResultSet rs = prepstmtSelect.executeQuery();

            if (rs.next()) {

                // turn off autocommit
                int exist = rs.getInt("exist");

                if (exist == 1) {
                    prepstmtUpdate.setLong(2, controllerId);
                    prepstmtUpdate.setLong(3, dataId);
                    prepstmtUpdate.setLong(1, value);
                    prepstmtUpdate.executeUpdate();
                } else {
                    prepstmtInsert.setLong(1, controllerId);
                    prepstmtInsert.setLong(2, dataId);
                    prepstmtInsert.setLong(3, value);
                    prepstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Can't insert new row to controllerdata table ", e);
        } finally {
            prepstmtSelect.close();
            prepstmtInsert.close();
            prepstmtUpdate.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        String sqlQueryFlock = "DROP TABLE APP.CONTROLLERS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table controller ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeFromTable() throws SQLException {
        String sqlQueryFlock = "DELETE  FROM APP.CONTROLLERS ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlQueryFlock);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot drop table controller ", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
