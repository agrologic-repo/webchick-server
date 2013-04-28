package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import com.agrologic.app.util.UserUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDaoImpl implements UserDao {
    protected DaoFactory dao;

    public UserDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    @Override
    public void insert(User user) throws SQLException {
        String sqlQuery =
                "INSERT INTO USERS (USERID, NAME, PASSWORD, FIRSTNAME, LASTNAME, ROLE, STATE, PHONE, EMAIL, COMPANY)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setString(2, user.getLogin());
            prepstmt.setString(3, user.getPassword());
            prepstmt.setString(4, user.getFirstName());
            prepstmt.setString(5, user.getLastName());
            prepstmt.setInt(6, user.getUserRole().getValue());
            prepstmt.setInt(7, user.getState());
            prepstmt.setString(8, user.getPhone());
            prepstmt.setString(9, user.getEmail());
            prepstmt.setString(10, user.getCompany());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert New User To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String sqlQuery =
                "update users set Name=?,Password=?,FirstName=?,LastName=?,Role=?,Phone=?,Email=?, Company=? where UserID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, user.getLogin());
            prepstmt.setString(2, user.getPassword());
            prepstmt.setString(3, user.getFirstName());
            prepstmt.setString(4, user.getLastName());
            prepstmt.setInt(5, user.getUserRole().getValue());
            prepstmt.setString(6, user.getPhone());
            prepstmt.setString(7, user.getEmail());
            prepstmt.setString(8, user.getCompany());
            prepstmt.setLong(9, user.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Update User In DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long userId) throws SQLException {
        String sqlQuery = "delete from users where UserID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Delete User From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Integer getTotalNumUsers() throws SQLException {
        String sqlQuery = "select count(*) as num from users";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt("num");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public User getById(Long userId) throws SQLException {
        String sqlQuery = "select * from users where UserID=" + userId;
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return UserUtil.makeUser(rs);
            } else {
                return new User();
            }
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Retrieve User By id From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public User validate(String loginName, String loginPassword) throws SQLException {
        String sqlQuery = "select * from users where Name = ? and Password = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, loginName);
            prepstmt.setString(2, loginPassword);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return UserUtil.makeUser(rs);
            } else {
                return new User();
            }
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Validate User In DataBase ", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Boolean loginEnabled(String login) throws SQLException {
        String sqlQuery = "select * from users where Name = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, login);

            ResultSet rs = prepstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Validate User In DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<String> getUserCompanies() throws SQLException {
        String sqlQuery = "select distinct company from users";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<String> companies = new ArrayList<String>();

            while (rs.next()) {
                companies.add(rs.getString("Company"));
            }

            return companies;
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Retrieve Companies From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<User> getAll() throws SQLException {
        String sqlQuery = "select * from users";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return UserUtil.makeUserList(rs);
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<User> getAllByRole(Integer role) throws SQLException {
        String sqlQuery = "select * from users where Role =?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, role);

            ResultSet rs = prepstmt.executeQuery();

            return UserUtil.makeUserList(rs);
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<User> getAll(Integer role, String company, String searchText) throws SQLException {
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
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return UserUtil.makeUserList(rs);
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Retrieve Company From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}
