
package com.agrologic.app.dao.impl;

import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.UserDto;
import com.agrologic.app.utils.Base64;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: UserDaoImpl - Encapsulate all SQL queries to database that are related to USERS<br>
 * Description: Contains 3 types of SQL methods:<ul>
 * <li>regular jdbc statements</li>
 * <li>prepared statements<br></li></ul>
 * Copyright:    Copyright (c) 2007
 *
 * @author Valery Manakhimov
 * @version 1.0
 */
public class UserDaoImpl extends ConnectorDao implements UserDao {

    /**
     * Help to create user from result set .
     *
     * @param rs a result set
     * @return user an objects that encapsulates a user attributes
     * @throws java.sql.SQLException
     */
    private UserDto makeUser(ResultSet rs) throws SQLException {
        UserDto user = new UserDto();
        user.setId(rs.getLong("UserID"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setLogin(rs.getString("Name"));
        user.setPassword(rs.getString("Password"));
        String decpsswd = new String(Base64.decode(rs.getString("Password")));
        user.setPassword(decpsswd);

        user.setRole(rs.getInt("Role"));
        user.setPhone(rs.getString("Phone"));
        user.setEmail(rs.getString("Email"));
        user.setCompany(rs.getString("Company"));
        user.setValidate(true);
        return user;
    }

    /**
     * Help to create vector of users from result set
     *
     * @param rs a result set
     * @return users    a vector of UserDto objects
     * @throws java.sql.SQLException
     */
    private List<UserDto> makeUserList(ResultSet rs) throws SQLException {
        List<UserDto> users = new ArrayList<UserDto>();
        while (rs.next()) {
            users.add(makeUser(rs));
        }
        return users;
    }

    /**
     * Help to create user from result set .
     *
     * @param rs a result set
     * @return user an objects that encapsulates a user attributes
     * @throws java.sql.SQLException
     */
    private UserDto makeUserContent(ResultSet rs) throws SQLException {
        UserDto user = new UserDto();
        user.setId(rs.getLong("UserID"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setLogin(rs.getString("Name"));
        user.setPassword(rs.getString("Password"));
        user.setRole(rs.getInt("Role"));
        user.setPhone(rs.getString("Phone"));
        user.setValidate(true);
        return user;
    }

    /**
     * Help to create cellink from result set .
     *
     * @param rs a result set
     * @return cellink an objects that encapsulates a cellink attributes
     * @throws java.sql.SQLException
     */
    private CellinkDto makeCellinkContent(ResultSet rs) throws SQLException {
        CellinkDto cellink = new CellinkDto();
        cellink.setId(rs.getLong("CellinkID"));
        cellink.setName(rs.getString("Name"));
        cellink.setPassword(rs.getString("Password"));
        cellink.setTime(rs.getTimestamp("Time"));
        cellink.setIp(rs.getString("IP"));
        cellink.setPort(rs.getInt("Port"));
        cellink.setState(rs.getInt("State"));
        cellink.setScreenId(rs.getLong("ScreenID"));
        cellink.setUserId(rs.getLong("UserID"));
        cellink.setValidate(true);

        return cellink;
    }

    /**
     * Help to create controller from result set
     *
     * @param rs a result set
     * @return controller an object that encapsulate a cellink attributes
     * @throws java.sql.SQLException
     */
    private ControllerDto makeControllerConntent(ResultSet rs) throws SQLException {
        ControllerDto controller = new ControllerDto();
        controller.setId(rs.getLong("ControllerID"));
        controller.setNetName(rs.getString("NetName"));
        controller.setTitle(rs.getString("Title"));
        controller.setName(rs.getString("ControllerName"));
        controller.setCellinkId(rs.getLong("CellinkID"));
        controller.setProgramId(rs.getLong("ProgramID"));
        return controller;
    }

    /**
     * Help to create vector of users from result set
     *
     * @param rs a result set
     * @return users a vector of User objects
     * @throws java.sql.SQLException
     */
    private List<UserDto> makeUserContentList(ResultSet rs) throws SQLException {
        List<UserDto> users = new ArrayList<UserDto>();
        while (rs.next()) {
            UserDto newUser = makeUserContent(rs);
            CellinkDto newCellink = makeCellinkContent(rs);
            ControllerDto controller = makeControllerConntent(rs);

            UserDto user = findUser(users, newUser);
            if (user != null) {
                CellinkDto cellink = findCellink(user.getCellinks(), newCellink);
                if (cellink != null) {
                    cellink.addController(controller);
                } else {
                    newCellink.addController(controller);
                    user.addCellink(newCellink);
                    users.add(newUser);
                }
            } else {
                newCellink.addController(controller);
                newUser.addCellink(newCellink);
                users.add(newUser);
            }
        }
        return users;
    }

    private UserDto findUser(List<UserDto> users, UserDto findUser) {
        for (UserDto u : users) {
            if (u.equals(findUser)) {
                return u;
            }
        }
        return null;
    }

    private CellinkDto findCellink(List<CellinkDto> cellinks, CellinkDto findCellink) {
        for (CellinkDto u : cellinks) {
            if (u.equals(findCellink)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Inserts a new user row to table user
     *
     * @param user an objects that encapsulates a user attributes
     * @throws SQLException if failed to insert new user to the database
     */
    @Override
    public void insert(UserDto user) throws SQLException {
        String sqlQuery = "insert into users values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setString(2, user.getLogin());
            prepstmt.setString(3, user.getPassword());
            prepstmt.setString(4, user.getFirstName());
            prepstmt.setString(5, user.getLastName());
            prepstmt.setInt(6, user.getRole());
            prepstmt.setInt(7, user.getRole());
            prepstmt.setString(8, user.getPhone());
            prepstmt.setString(9, user.getEmail());
            prepstmt.setString(10, user.getCompany());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Insert New UserDto To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Updates an existing user row in table user
     *
     * @param user an object that encapsulates a user attributes
     * @throws SQLException if failed to update the user in the database
     */
    @Override
    public void update(UserDto user) throws SQLException {
        String sqlQuery = "update users set Name=?,Password=?,FirstName=?,LastName=?,Role=?,Phone=?,Email=?, Company=? where UserID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, user.getLogin());
            prepstmt.setString(2, user.getPassword());
            prepstmt.setString(3, user.getFirstName());
            prepstmt.setString(4, user.getLastName());
            prepstmt.setInt(5, user.getRole());
            prepstmt.setString(6, user.getPhone());
            prepstmt.setString(7, user.getEmail());
            prepstmt.setString(8, user.getCompany());
            prepstmt.setLong(9, user.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Update User In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Removes a user from the database
     *
     * @param userId the id of the user to be removed from the database
     * @throws SQLException if failed to remove the user from the database
     */
    @Override
    public void remove(Long userId) throws SQLException {
        String sqlQuery = "delete from users where UserID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Delete User From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Gets the number of Users
     *
     * @return countRows    the number of users in database
     * @throws SQLException if failed to get the number of users from the database
     */
    @Override
    public Integer getTotalNumUsers() throws SQLException {
        String sqlQuery = "select count(*) as num from users";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt("num");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Gets user by it id
     *
     * @param userId an user id
     * @return user         an objects that encapsulates a user attributes
     * @throws SQLException if failed to retrive user from the database
     */
    @Override
    public UserDto getById(Long userId) throws SQLException {
        String sqlQuery = "select * from users where UserID=" + userId;
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                return makeUser(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve User By id From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Checks a user login and returns the user from the database
     *
     * @param loginName     login name of the user
     * @param loginPassword login password of the user
     * @return User         an User object that encapsulate the user attribute or null
     * @throws SQLException if failed to check user login
     */
    @Override
    public UserDto validate(String loginName, String loginPassword) throws SQLException {
        String sqlQuery = "select * from users where Name = ? and Password = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, loginName);
            prepstmt.setString(2, loginPassword);

            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeUser(rs);
            } else {
                return new UserDto();
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Validate User In DataBase ", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public Boolean checkNewLoginName(String loginName) throws SQLException {
        String sqlQuery = "select * from users where Name = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, loginName);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Validate User In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all users in no special order
     *
     * @return users        a vector of User objects, each object reflects a row in table users
     * @throws SQLException if failed to retrieve all users from the database
     */
    @Override
    public List<UserDto> getAll() throws SQLException {
        String sqlQuery = "select * from users";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeUserList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all users in no special order
     *
     * @return users        a vector of User objects, each object reflects a row in table users
     * @throws SQLException if failed to retrieve all users from the database
     */
    @Override
    public List<UserDto> getAll(Integer role, String company, String searchText) throws SQLException {
        String sqlQuery = "select * from users";
        Statement stmt = null;
        Connection con = null;

        if (role != 0) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where role=" + role;
        }

        if (!company.equals("All")) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where company='" + company + "'";
        }

        if (!company.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as c where name like '%" + searchText + "%'";
        }

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeUserList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Company From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all users by group level in no special order
     *
     * @param role a group level of user
     * @return users  a vector of User objects, each object reflects a row in table users
     * @throws SQLException if failed to retrieve all users from the database
     */
    @Override
    public List<UserDto> getAllByRole(Integer role) throws SQLException {
        String sqlQuery = "select * from users where Role =?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, role);
            ResultSet rs = prepstmt.executeQuery();
            return makeUserList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<UserDto> getAllUsers() throws SQLException {
        String sqlQuery = "select * from users left join (cellinks,controllers) "
                + "on (users.UserID = cellinks.UserID and cellinks.CellinkID = controllers.CellinkID)";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeUserContentList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<String> getUserCompanies() throws SQLException {
        String sqlQuery = "select distinct company from users";
        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<String> companies = new ArrayList<String>();
            while (rs.next()) {
                companies.add(rs.getString("Company"));
            }
            return companies;
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Companies From DataBase", e);
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }
}
