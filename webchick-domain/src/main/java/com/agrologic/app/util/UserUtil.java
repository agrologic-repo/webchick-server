package com.agrologic.app.util;

import com.agrologic.app.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserUtil {

    /**
     * Help to create user from result set .
     *
     * @param rs a result set
     * @return user an objects that encapsulates a user attributes
     * @throws SQLException if failed
     */
    public static User makeUser(final ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("UserID"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setLogin(rs.getString("Name"));
        user.setPassword(rs.getString("Password"));
        String decpsswd = new String(Base64.decode(rs.getString("Password")));
        user.setPassword(decpsswd);
        user.setState(rs.getInt("State"));
        user.setUserRole(rs.getInt("Role"));
        user.setPhone(rs.getString("Phone"));
        user.setEmail(rs.getString("Email"));
        user.setCompany(rs.getString("Company"));
        user.setValidate(true);
        return user;
    }

    /**
     * Help to create vector of users from result set .
     *
     * @param rs a result set
     * @return users a vector of User objects
     * @throws java.sql.SQLException if failed to execute statement.
     */
    public static Collection<User> makeUserList(final ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<User>();

        while (rs.next()) {
            users.add(makeUser(rs));
        }

        return users;
    }
}


