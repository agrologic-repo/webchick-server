package com.agrologic.app.service.impl;

import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import com.agrologic.app.service.UserManagerService;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Valery on 11/08/2014.
 */
public class UserManagerServiceImpl implements UserManagerService {
    private UserDao userDao;

    public UserManagerServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void insert(User user) throws SQLException {
        userDao.insert(user);
    }

    @Override
    public void update(User user) throws SQLException {
        userDao.update(user);
    }

    @Override
    public void remove(Long id) throws SQLException {
        userDao.remove(id);
    }

    @Override
    public Boolean loginEnabled(String name) throws SQLException {
        return userDao.loginEnabled(name);
    }

    @Override
    public Integer count() throws SQLException {
        return userDao.count();
    }

    @Override
    public Integer count(Integer role, String company, String search) throws SQLException {
        return userDao.count(role, company, search);
    }

    @Override
    public User getById(Long id) throws SQLException {
        return userDao.getById(id);
    }

    @Override
    public User validate(String name, String password) throws SQLException {
        return userDao.validate(name, password);
    }

    @Override
    public Collection<String> getUserCompanies() throws SQLException {
        return userDao.getUserCompanies();
    }

    @Override
    public Collection<User> getAll() throws SQLException {
        return userDao.getAll();
    }

    @Override
    public Collection<User> getAllByRole(Integer role) throws SQLException {
        return userDao.getAllByRole(role);
    }

    @Override
    public Collection<User> getAll(Integer role, String company, String text) throws SQLException {
        return userDao.getAll(role, company, text);
    }

    @Override
    public Collection<User> getAll(Integer role, String company, String search, String index) throws SQLException {
        return userDao.getAll(role, company, search, index);
    }
}
