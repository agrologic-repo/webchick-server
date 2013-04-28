package com.agrologic.app.dao.impl;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.web.UserRole;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: CellinkDaoImpl - Encapsulate all SQL queries to database that are related to cellink <br>
 * Description: Contains 3 types of SQL methods:<ul>
 *                        <li>regular jdbc statements</li>
 *                        <li>prepared statements<br></li></ul>
 * Copyright:   Copyright (c) 2008 <br>
 * Company:     Agro Logic LTD. <br>
 * @author Valery Manakhimov <br>
 * @version 1.0 <br>
 */
public class CellinkDaoImpl extends ConnectorDao implements CellinkDao {

    /**
     * Help to create cellink from result set .
     * @param rs a result set
     * @return cellink an objects that encapsulates a cellink attributes
     * @throws java.sql.SQLException
     */
    private CellinkDto makeCellink(ResultSet rs) throws SQLException {
        CellinkDto cellink = new CellinkDto();
        cellink.setId(rs.getLong("CellinkID"));
        cellink.setName(rs.getString("Name"));
        cellink.setPassword(rs.getString("Password"));
        cellink.setTime(rs.getTimestamp("Time"));
        cellink.setIp(rs.getString("IP"));
        cellink.setPort(rs.getInt("Port"));
        cellink.setState(rs.getInt("State"));
        cellink.setScreenId(rs.getLong("ScreenID"));
        cellink.setSimNumber(rs.getString("SIM"));
        cellink.setUserId(rs.getLong("UserID"));
        cellink.setType(rs.getString("Type"));
        cellink.setVersion(rs.getString("Version"));
        cellink.setActual(rs.getBoolean("Actual"));
        cellink.setValidate(true);

        return cellink;
    }

    /**
     * Help to create list of cellinks from result set
     * @param rs a result set
     * @return users a list of CellinkDto objects
     * @throws java.sql.SQLException
     */
    private List<CellinkDto> makeCellinkList(ResultSet rs) throws SQLException {
        List<CellinkDto> cellinks = new ArrayList<CellinkDto>();
        while (rs.next()) {
            cellinks.add(makeCellink(rs));
        }
        return cellinks;
    }

    /**
     * Inserts a new cellink row to table cellink
     * @param cellink          an objects that encapsulates a cellink attributes
     * @throws SQLException    if failed to insert new cellink to the database
     */
    @Override
    public void insert(CellinkDto cellink) throws SQLException {
        String sqlQuery = "insert into cellinks (Name, Password, UserID, SIM, Type, Version, State, ScreenID, Actual) values (?,?,?,?,?,?,?,?, ?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, cellink.getName());
            prepstmt.setString(2, cellink.getPassword());
            prepstmt.setLong(3, cellink.getUserId());
            prepstmt.setString(4, cellink.getSimNumber());
            prepstmt.setString(5, cellink.getType());
            prepstmt.setString(6, cellink.getVersion());
            prepstmt.setInt(7, cellink.getState());
            prepstmt.setLong(8, cellink.getScreenId());
            prepstmt.setBoolean(9, cellink.isActual());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Insert new CellinkDto to the DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Updates an existing cellink row in table cellink
     * @param cellink       an object that encapsulates a cellink attributes
     * @throws SQLException    if failed to update the user in the database
     */
    @Override
    public void update(CellinkDto cellink) throws SQLException {
        String sqlQuery = "update cellinks set Name=?,Password=?,UserID=?,Time=?,Port=?, Ip=?, State=? , SIM=?, Type=?, Version=?  where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, cellink.getName());
            prepstmt.setString(2, cellink.getPassword());
            prepstmt.setLong(3, cellink.getUserId());
            prepstmt.setTimestamp(4, cellink.getTime());
            prepstmt.setInt(5, cellink.getPort());
            prepstmt.setString(6, cellink.getIp());
            prepstmt.setInt(7, cellink.getState());
            prepstmt.setString(8, cellink.getSimNumber());
            prepstmt.setString(9, cellink.getType());
            prepstmt.setString(10, cellink.getVersion());
            prepstmt.setLong(11, cellink.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Update User In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Removes a cellink from the database
     * @param cellinkId        the id of the cellink to be removed from the database
     * @throws SQLException    if failed to remove the cellink from the database
     */
    @Override
    public void remove(Long cellinkId) throws SQLException {
        String sqlQuery = "delete from cellinks where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Delete User From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public int count() throws SQLException {
        String sqlQuery = "select count(*) as count from cellinks";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                return rs.getInt("count");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Count Programs From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public int count(long userId, int role, String company, Integer state, String type, String name) throws SQLException {
        String sqlQuery = "select * from cellinks";

        if (!name.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as c where c.Name like '%" + name + "%'";
        }

        if (state != -1) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.State=" + state;
        }
        if (!type.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as d where d.Type like '%" + type + "%'";
        }

        if (role == UserRole.ADVANCED) {
            sqlQuery = "select * from (" + sqlQuery + ") as a  where userid in (select userid from users where company = '" + company + "') ";
        } else if(role == UserRole.ADMINISTRATOR) {
            sqlQuery = "select * from (" + sqlQuery + ") as a ";
        } else if(role == UserRole.REGULAR) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where userid=" + userId;
        }
        sqlQuery = "select count(*) as count from (" + sqlQuery + ") as e";

        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                return rs.getInt("count");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Count Programs From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Inserts a screen data rows to table screen data
     * @param cellinkId        the id of the cellink
     * @throws SQLException    if failed to insert new cellink to the database
     */
    @Override
    public void insertScreenData(Long cellinkId) throws SQLException {
        String sqlQuery = "insert into screendata (ScreenID,DataID, CellinkID) "
                + "(select ScreenID,DataID, ? from defaultscreendata);";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Insert new CellinkDto to the DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void disconnect(Long cellinkId) throws SQLException {
        long oldstate = 3;
        long newstate = 4;
        changeState(cellinkId, oldstate, newstate);
    }

    @Override
    public void disconnectStarted(Long cellinkId) throws SQLException {
        long oldstate = 2;
        long newstate = 4;
        changeState(cellinkId, oldstate, newstate);
    }

    public void changeState(Long cellinkId, Long oldState, Long newState) throws SQLException {
        String sqlQuery = "update cellinks set state=? where cellinkid=? and state=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, newState);
            prepstmt.setLong(2, cellinkId);
            prepstmt.setLong(3, oldState);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Update Cellink In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves a cellink from the database by id
     * @param id            the id of the cellink to get from the database
     * @return CellinkDto         an object that encapsulates a cellink attribue
     * @throws SQLException    if failed to get the cellink from the database
     */
    @Override
    public CellinkDto getById(Long cellinkId) throws SQLException {
        String sqlQuery = "select * from cellinks where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeCellink(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Cannot Retrieve All Farms From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Checks a cellink login and returns the cellink from the database
     * @param Name      a cellink name
     * @param Password     a cellink owner
     * @return CellinkDto         an object object that encapsulates a cellink attributes
     * @throws SQLException    if failed validation cellink
     */
    @Override
    public CellinkDto validate(String Name, String Password) throws SQLException {
        String sqlQuery = "select * from cellinks where Name = ? and Password = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, Name);
            prepstmt.setString(2, Password);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeCellink(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Cannot validate users From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Returns actual cellink.
     * @return actual cellink.
     * @throws SQLException     if failed validation farm
     */
    @Override
    public CellinkDto getActualCellink() throws SQLException {
        String sqlQuery = "select * from cellinks where actual=1";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                return makeCellink(rs);
            } else {
                return null;
            }

        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Actual Cellink From DataBase.");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all Farms in no special order
     * @return celinks     a vector of CellinkDto objects, each object reflects a row in table cellink
     * @throws SQLException if failed to retreive all users ids from the database
     */
    @Override
    public List<CellinkDto> getAll() throws SQLException {
        String sqlQuery = "select * from cellinks";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Cannot Retrieve All Farms From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrievs all CellinkDto names in no special order by owner
     * @param userId
     * @return celinks        a vector of CellinkDto objects, each object reflects a row in table cellink
     * @throws SQLException
     */
    @Override
    public List<CellinkDto> getAllUserCellinks(Long userId) throws SQLException {
        String sqlQuery = "select * from cellinks";

        if (userId != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.UserID=" + userId;
        }

        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Cannot Retrieve All Farms From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<CellinkDto> getAll(Integer state) throws SQLException {
        String sqlQuery = "select * from cellinks";

        if (state != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.State=" + state;
        }

        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Cannot Retrieve All Farms From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<CellinkDto> getAll(Long userId, Integer state, String type, String name) throws SQLException {
        String sqlQuery = "select * from cellinks";

        if (userId != -1) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.UserID=" + userId;
        }
        if (!name.equals("")) {
            System.out.println();
            sqlQuery = "select * from (" + sqlQuery + ") as c where c.Name like '%" + name + "%'";
        }

        if (state != -1) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.State=" + state;
        }
        if (!type.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as d where d.Type like '%" + type + "%'";
        }

        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve All Cellinks From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<CellinkDto> getAll(String company, Integer state, String type, String name) throws SQLException {
        String sqlQuery = "select * from cellinks where userid in (select userid from users where company = '" + company + "')";

        if (!name.equals("")) {
            System.out.println();
            sqlQuery = "select * from (" + sqlQuery + ") as c where c.Name like '%" + name + "%'";
        }

        if (state != -1) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.State=" + state;
        }
        if (!type.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as d where d.Type like '%" + type + "%'";
        }

        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve All Cellinks From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<CellinkDto> getAll(String company, Integer state, String type, String name, String index) throws SQLException {
        String sqlQuery = "select * from cellinks where userid in (select userid from users where company = '" + company + "') limit " + index + ",25 ";

        if (!name.equals("")) {
            System.out.println();
            sqlQuery = "select * from (" + sqlQuery + ") as c where c.Name like '%" + name + "%'";
        }

        if (state != -1) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.State=" + state;
        }
        if (!type.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as d where d.Type like '%" + type + "%'";
        }

        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve All Cellinks From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<CellinkDto> getAll(int role, String company, Integer state, String type, String name, String index) throws SQLException {
        String sqlQuery = "select * from cellinks ";

        if (!name.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as c where c.Name like '%" + name + "%'";
        }

        if (state != -1) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.State=" + state;
        }

        if (!type.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as d where d.Type like '%" + type + "%'";
        }

        if (role == UserRole.ADVANCED) {
            sqlQuery = "select * from (" + sqlQuery + ") as b  where userid in (select userid from users where company = '" + company + "') limit " + index + ",25 ";
        } else if(role == UserRole.ADMINISTRATOR) {
            sqlQuery = "select * from (" + sqlQuery + ") as b  limit " + index + ",25 ";
        }

        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeCellinkList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve All Cellinks From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }
}
