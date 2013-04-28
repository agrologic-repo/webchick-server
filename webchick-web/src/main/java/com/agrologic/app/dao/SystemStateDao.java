package com.agrologic.app.dao;

import com.agrologic.app.model.SystemStateDto;



import java.sql.SQLException;

import java.util.List;

public interface SystemStateDao {
    public void insert(SystemStateDto systemState) throws SQLException;

    public void update(SystemStateDto systemState) throws SQLException;

    public void remove(Long id) throws SQLException;

    public SystemStateDto getById(Long id) throws SQLException;

    public List<SystemStateDto> getAll() throws SQLException;

    public void insertTranslation(Long screenId, Long langId, String translation) throws SQLException;

    public List<SystemStateDto> getAll(Long langId) throws SQLException;
}
