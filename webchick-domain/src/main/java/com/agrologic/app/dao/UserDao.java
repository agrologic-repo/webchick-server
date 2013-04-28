
/*
* UserDao.java
*
* Created on 11 ��� 2008, 10:30
*
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
 */
package com.agrologic.app.dao;


import com.agrologic.app.model.User;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Title: UserDao - Encapsulate all SQL queries to database that are related to USERS<br> Description: Contains 3 types
 * of SQL methods:<ul> <li>regular jdbc statements</li> <li>prepared statements<br> </li> </ul> Copyright: Copyright (c)
 * 2007
 *
 * @author Valery Manakhimov
 * @version 1.0
 */
public interface UserDao {

    /**
     * Inserts a new user row to table user .
     *
     * @param user an objects that encapsulates an user attributes.
     * @throws SQLException if failed to insert new user to the database.
     */
    void insert(User user) throws SQLException;

    /**
     * Updates an existing user row in table user
     *
     * @param user an object that encapsulates a user attributes
     * @throws SQLException if failed to update the user in the database
     */
    void update(User user) throws SQLException;

    /**
     * Removes a user from the database
     *
     * @param id the id of the user to be removed from the database
     * @throws SQLException if failed to remove the user from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Check if login already exist in database.
     *
     * @param login the login name.
     * @return true if login enabled, otherwise false .
     * @throws SQLException if failed to check user login.
     */

    /**
     * Check if name already exist in database.
     *
     * @param name the name
     * @return true if name enabled, otherwise false .
     * @throws SQLException
     */
    Boolean loginEnabled(String name) throws SQLException;

    /**
     * Gets total the number of Users
     *
     * @return countRows the number of users in database
     * @throws SQLException if failed to get the number of users from the database
     */
    Integer getTotalNumUsers() throws SQLException;

    /**
     * Gets user by it id
     *
     * @param id an user id
     * @return user an objects that encapsulates an user attributes
     * @throws SQLException if failed to retrieve user from the database
     */
    User getById(Long id) throws SQLException;

    /**
     * Authenticate user name and password.
     *
     * @param name login name
     * @param password login password
     * @return User an User object that encapsulate the user attribute or null
     * @throws SQLException if failed to check user login
     */
    User validate(String name, String password) throws SQLException;

    /**
     * Retrieves companies.
     *
     * @return companies the list of companies.
     * @throws SQLException if failed to get companies.
     */
    Collection<String> getUserCompanies() throws SQLException;

    /**
     * Retrieves all users in no special order .
     *
     * @return users a vector of User objects, each object reflects a row in table users.
     * @throws SQLException if failed to retrieve all users from the database
     */
    Collection<User> getAll() throws SQLException;

    /**
     * Retrieves all users by roles
     *
     * @param role a role of user
     * @return users a vector of User objects, each object reflects a row in table users
     * @throws SQLException if failed to retrieve all users from the database
     */
    Collection<User> getAllByRole(Integer role) throws SQLException;

    /**
     * Retrieves users that match the specified criteria
     *
     * @param role the role that represent user role in system <br> (values: -1 - all, 0 - guest, 1 - user, 2 -
     * administrator, 3 - distributor)
     * @param company the company of user
     * @param text the text that compare to user login name
     * @return the list of users that match the criteria
     * @throws SQLException if failed to retrieve users from the database.
     */
    Collection<User> getAll(Integer role, String company, String text) throws SQLException;
}


