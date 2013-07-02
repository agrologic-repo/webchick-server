package com.agrologic.app.dao;

import com.agrologic.app.model.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public void insert(UserDto user) throws SQLException;

    public void update(UserDto user) throws SQLException;

    public void remove(Long id) throws SQLException;

    public List<String> getUserCompanies() throws SQLException;

    public Boolean checkNewLoginName(String name) throws SQLException;

    public Integer getTotalNumUsers() throws SQLException;

    public UserDto getById(Long id) throws SQLException;

    public UserDto validate(String name, String password) throws SQLException;

    public List<UserDto> getAll() throws SQLException;

    public List<UserDto> getAll(Integer role, String company, String searchText) throws SQLException;

    public List<UserDto> getAllUsers() throws SQLException;

    public List<UserDto> getAllByRole(Integer role) throws SQLException;
}



