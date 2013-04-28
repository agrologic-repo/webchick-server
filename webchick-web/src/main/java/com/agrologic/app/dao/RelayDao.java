package com.agrologic.app.dao;

import com.agrologic.app.model.RelayDto;



import java.sql.SQLException;

import java.util.List;

public interface RelayDao {
    public void insert(RelayDto relay) throws SQLException;

    public void insertTranslation(Long relayId, Long langId, String translation) throws SQLException;

    public void update(RelayDto relay) throws SQLException;

    public void remove(Long relayId) throws SQLException;

    public RelayDto getById(Long id) throws SQLException;

    public List<RelayDto> getAll() throws SQLException;

    public List<RelayDto> getAll(Long langId) throws SQLException;
}



