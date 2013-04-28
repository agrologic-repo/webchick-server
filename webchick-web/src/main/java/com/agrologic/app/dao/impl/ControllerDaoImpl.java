

package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.model.ControllerDto;



import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: ControllerDaoImpl - Encapsulate all SQL queries to database that are related to controllers <br>
 * Description: Contains 3 types of SQL methods:<ul>
 *                        <li>regular jdbc statements</li>
 *                        <li>prepared statements<br></li></ul>
 * Copyright:   Copyright (c) 2008 <br>
 * Company:     AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.0 <br>
 */
public class ControllerDaoImpl extends ConnectorDao implements ControllerDao {

    /**
     * Help to create controller from result set
     * @param rs a result set
     * @return controller and object that encapsulate a cellink attributes
     * @throws java.sql.SQLException
     */
    private ControllerDto makeController(ResultSet rs) throws SQLException {
        ControllerDto controller = new ControllerDto();

        controller.setId(rs.getLong("ControllerID"));
        controller.setNetName(rs.getString("NetName"));
        controller.setTitle(rs.getString("Title"));
        controller.setName(rs.getString("ControllerName"));
        controller.setArea(rs.getInt("Area"));
        controller.setCellinkId(rs.getLong("CellinkID"));
        controller.setProgramId(rs.getLong("ProgramID"));
        controller.setActive(rs.getBoolean("Active"));

        return controller;
    }

    /**
     * Help to create list of controller from result set
     * @param rs a result set
     * @return controllers a list of ControllerDto objects
     * @throws java.sql.SQLException
     */
    private List<ControllerDto> makeControllerList(ResultSet rs) throws SQLException {
        List<ControllerDto> controllers = new ArrayList<ControllerDto>();

        while (rs.next()) {
            controllers.add(makeController(rs));
        }

        return controllers;
    }

    /**
     * Inserts a new ControllerDto row to table controller
     * @param controller    an objects that encapsulates a controller attributes
     * @throws SQLException    if failed to insert new controller to the database
     */
    @Override
    public void insert(ControllerDto controller) throws SQLException {
        String            sqlQuery = "insert into controllers values (?,?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
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
            throw new SQLException("Cannot New ControllerDto To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Updates an existing controller row in table controller
     * @param newController a object that encapsulates a controller attributes
     * @throws SQLException    if failed to remove the controller from the database
     */
    @Override
    public void update(ControllerDto newController) throws SQLException {
        String sqlQuery =
            "update controllers set Title=?, NetName=?, ControllerName=?, Area=?, ProgramID=?, Active=? where ControllerID=? and CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, newController.getTitle());
            prepstmt.setString(2, newController.getNetName());
            prepstmt.setString(3, newController.getName());
            prepstmt.setLong(4, newController.getArea());
            prepstmt.setLong(5, newController.getProgramId());
            prepstmt.setBoolean(6, newController.isActive());
            prepstmt.setLong(7, newController.getId());
            prepstmt.setLong(8, newController.getCellinkId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Update ControllerDto In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Removes a controller from the database
     * @param controllerId  a controller id of the controller to be removed from the database
     * @throws SQLException    if failed to remove the controller from the database
     */
    @Override
    public void remove(Long controllerId) throws SQLException {
        String            sqlQuery = "delete from controllers where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Delete ControllerDto From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves a ControllerDto by controller id
     * @param cellinkId        a controller id
     * @return controller   a ControllerDto object
     * @throws SQLException    if failed to retreive ControllerDto from the database
     */
    @Override
    public ControllerDto getById(Long controllerId) throws SQLException {
        String            sqlQuery = "select * from controllers where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeController(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all ControllerDto in no special order.
     * @return controllers  a vector of ControllerDto objects, each object reflects a row in table controllers
     * @throws SQLException    if failed to retrieve all Controllers from the database
     */
    @Override
    public List<ControllerDto> getAll() throws SQLException {
        String     sqlQuery = "select * from controllers";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeControllerList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all ControllerDto by cellink id in no special order
     * @param cellinkId a cellink id
     * @return controllers a vector of ControllerDto objects, each object reflects a row in table controllers
     * @throws SQLException    if failed to retrieve all Controllers from the database
     */
    @Override
    public List<ControllerDto> getAllByCellinkId(Long cellinkId) throws SQLException {
        String            sqlQuery = "select * from controllers where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);

            ResultSet rs = prepstmt.executeQuery();

            return makeControllerList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public String getControllerGraph(Long controllerId) throws SQLException {
        String            sqlQuery = "select Dataset from graph24hours where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Dataset");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void sendNewDataValueToController(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlQuery =
            "insert into newcontrollerdata (ControllerID,DataID,Value) VALUES (?,?,?) on duplicate key update Value=values(Value)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, dataId);
            prepstmt.setLong(3, value);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Can not add new value into newcontrollerdata", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void saveNewDataValueOnController(Long controllerId, Long dataId, Long value) throws SQLException {
        String sqlQuery =
            "insert into controllerdata (ControllerID,DataID,Value) VALUES (?,?,?) on duplicate key update Value=values(Value)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, dataId);
            prepstmt.setLong(3, value);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Can not add new value into controllerdata table", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void removeControllerData(Long controllerId) throws SQLException {
        String            sqlQuery = "delete from controllerdata where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Delete Controller Data From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<String> getControllerNames() throws SQLException {
        String     sqlQuery = "select distinct controllername from controllers";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet    rs        = stmt.executeQuery(sqlQuery);
            List<String> companies = new ArrayList<String>();

            while (rs.next()) {
                companies.add(rs.getString("ControllerName"));
            }

            return companies;
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Controller Names From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public boolean isDataReady(Long userId) throws SQLException {
        String sqlQuery = "select dataid from controllerdata " + "where controllerid in "
                          + "(select controllerid from controllers " + "where cellinkid in "
                          + "(select cellinkid from cellinks where userid=? ))";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public boolean isControllerDataReady(Long controllerId) throws SQLException {
        String            sqlQuery = "select dataid from controllerdata " + "where controllerid=? and dataid=0";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all active controllers by cellink id in no special order
     * @param cellinkId        a cellink id
     * @return controllers  a vector of ControllerDto objects, each object reflects a row in table controllers
     * @throws SQLException    if failed to retrieve all Controllers from the database
     */
    @Override
    public List<ControllerDto> getAllActiveByCellinkId(Long cellinkId) throws SQLException {
        String            sqlQuery = "select * from controllers where CellinkID=? and active=1";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);

            ResultSet rs = prepstmt.executeQuery();

            return makeControllerList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



