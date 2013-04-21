package com.agrologic.app.dao.mysql.impl;

import java.sql.*;
import java.util.*;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.network.MessageManager;
import com.agrologic.app.util.ControllerUtil;

/**
 * Title: ControllerDaoImpl - Encapsulate all SQL queries to database that are related to controllers <br> Description:
 * Contains 3 types of SQL methods:<ul> <li>regular jdbc statements</li> <li>prepared statements<br></li></ul>
 * Copyright: Copyright (c) 2008 <br>
 *
 * @version 1.0 <br>
 */
public class ControllerDaoImpl implements ControllerDao {

    protected DaoFactory dao;

    public ControllerDaoImpl() {
        this(DaoType.MYSQL);
    }

    public ControllerDaoImpl(DaoType daoType) {
        this.dao = DaoFactory.getDaoFactory(daoType);
    }

    @Override
    public void insert(Controller controller) throws SQLException {
        String sqlQuery = "insert into controllers values (?,?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
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
            throw new SQLException("Cannot Insert Controller To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(List<Controller> controllers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertControllerDataValues(Long controllerId, Iterator<Map.Entry<Long, Data>> dataValues) throws SQLException {
        String sqlQuery = "insert into controllerdata values (?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            while (dataValues.hasNext()) {
                Map.Entry<Long, Data> entry = dataValues.next();
                prepstmt = con.prepareStatement(sqlQuery);
                prepstmt.setLong(1, controllerId);
                prepstmt.setLong(2, entry.getKey());
                prepstmt.setLong(3, entry.getValue().getValue());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch(); // Execute every 200 items.
                    System.out.print(".");
                }
                i++;
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
                    throw new SQLException("Transaction is being rolled back", ex);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(Controller controller) throws SQLException {
        String sqlQuery = "update controllers set Title=?, NetName=?, ControllerName=?, Area=?, ProgramID=?, "
                + "Active=? where ControllerID=? and CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, controller.getTitle());
            prepstmt.setString(2, controller.getNetName());
            prepstmt.setString(3, controller.getName());
            prepstmt.setLong(4, controller.getArea());
            prepstmt.setLong(5, controller.getProgramId());
            prepstmt.setBoolean(6, controller.isActive());
            prepstmt.setLong(7, controller.getId());
            prepstmt.setLong(8, controller.getCellinkId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Update Controller In DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from controllers where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Delete Controller From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeChangedValue(Long controllerId, Long dataId) throws SQLException {
        String sqlQuery = "delete from newcontrollerdata where ControllerID=? and DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Delete Controller From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeControllerData(Long controllerId) throws SQLException {
        String sqlQuery = "delete from controllerdata where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Delete Controller Data From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void resetControllerData(Long controllerId) throws SQLException {
        String sqlQuery = "update controllerdata set value=-1 where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Reset Controller Data In DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateControllerData(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlQuery = "insert into controllerdata (ControllerID,DataID,Value) "
                + "VALUES (?,?,?) on duplicate key update Value=values(Value)";

        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, dataId);
            prepstmt.setLong(3, value);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Update Controller Data. ", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateControllerData(Long controllerId, Collection<Data> onlineData) throws SQLException {
        final String sqlQuery = "insert into controllerdata (ControllerID, DataID,Value) "
                + "VALUES (?,?,?) on duplicate key update value=values(value)";


        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);

            prepstmt = con.prepareStatement(sqlQuery);
            for (Data dc : onlineData) {
                prepstmt.setLong(1, controllerId);
                prepstmt.setLong(2, dc.getId());
                prepstmt.setLong(3, dc.getValue());
                prepstmt.executeUpdate();
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
    public void updateControllerGraph(Long controllerId, String values, Timestamp updateTime) throws SQLException {
        String sqlQuery = "insert into graph24hours (ControllerID,Dataset,UpdateTime) "
                + "VALUES (?,?,?) on duplicate key update Dataset=VALUES(Dataset) , UpdateTime=VALUES(UpdateTime)";
        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setString(2, values);
            prepstmt.setTimestamp(3, updateTime);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("SQLException: " + e.getMessage());
        } finally {
            prepstmt.close();
            dao.closeConnection(con);

        }
    }

    @Override
    public void updateControllerHistogram(Long controllerId, String plate, String values, Timestamp updateTime)
            throws SQLException {
        String sqlQuery = "insert into histogram24hour (ControllerID, Plate, Histogram, UpdateTime) "
                + "VALUES (?,?,?,?) on duplicate key update Histogram=VALUES(Histogram) , UpdateTime=VALUES(UpdateTime)";
        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setString(2, plate);
            prepstmt.setString(3, values);
            prepstmt.setTimestamp(4, updateTime);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("SQLException: " + e.getMessage());
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public final Timestamp getUpdatedGraphTime(Long controllerId) throws SQLException {
        String sqlQuery = "select UpdateTime as time from graph24hours where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("time");
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("SQLException: " + e.getMessage());
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Timestamp getHistogramUpdateTime(Long controllerId) throws SQLException {
        String sqlQuery = "select UpdateTime as time from history24hours where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("time");
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Updated Time of Histogram Data");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);

        }
    }

    @Override
    public Controller getById(Long controllerId) throws SQLException {
        String sqlQuery = "select * from controllers where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return ControllerUtil.makeController(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Controller From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void sendNewDataValueToController(Long controllerId, Long dataId, Long value)
            throws SQLException {
        String sqlQuery = "insert into newcontrollerdata (ControllerID,DataID,Value) "
                + "VALUES (?,?,?) on duplicate key update Value=values(Value)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, dataId);
            prepstmt.setLong(3, value);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Can not add new value into newcontrollerdata", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveNewDataValueOnController(Long controllerId, Long dataId, Long value)
            throws SQLException {
        String sqlQuery = "insert into controllerdata (ControllerID,DataID,Value) "
                + "VALUES (?,?,?) on duplicate key update Value=values(Value)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, dataId);
            prepstmt.setLong(3, value);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Can not add new value into controllerdata table", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public String getControllerGraph(Long controllerId) throws SQLException {
        String sqlQuery = "select Dataset from graph24hours where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Dataset");
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Controller Graph Data From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public boolean isDataReady(Long userId) throws SQLException {

        String sqlQuery = "select dataid from controllerdata where controllerid in "
                + "(select controllerid from controllers where cellinkid in "
                + "(select cellinkid from cellinks where userid=? ))";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userId);
            ResultSet rs = prepstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Data From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public boolean isControllerDataReady(Long controllerId) throws SQLException {

        String sqlQuery = "select dataid from controllerdata where controllerid=? and dataid=0";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Data From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<String> getControllerNames() throws SQLException {
        String sqlQuery = "select distinct controllername from controllers";
        Statement stmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<String> companies = new ArrayList<String>();
            while (rs.next()) {
                companies.add(rs.getString("ControllerName"));
            }
            return companies;
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Controller Names From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Controller> getAll() throws SQLException {
        String sqlQuery = "select * from controllers";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return ControllerUtil.makeControllerList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Controller> getAllByCellink(Long cellinkId) throws SQLException {
        String sqlQuery = "select * from controllers where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            ResultSet rs = prepstmt.executeQuery();
            return ControllerUtil.makeControllerList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Controllers By Cellink From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Controller> getActiveCellinkControllers(Long cellinkId) throws SQLException {
        String sqlQuery = "select * from controllers where CellinkID=? and Active=1";
//        and controllerid=57 ";//or controllerid = 372
//        String sqlQuery = "select * from controllers ";
//        String sqlQuery = "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY key ASC) AS rownumber,  columns FROM controllers  ) AS foo "
//                + " WHERE rownumber <= 1";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            ResultSet rs = prepstmt.executeQuery();
            return ControllerUtil.makeControllerList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Active Controllers From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}