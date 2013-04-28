package com.agrologic.app.dao;

import com.agrologic.app.model.AlarmDto;


import java.sql.SQLException;
import java.util.List;

public interface AlarmDao {

    public void insert(AlarmDto alarm) throws SQLException;

    public void update(AlarmDto alarm) throws SQLException;

    public void remove(Long id) throws SQLException;

    public void insertTranslation(Long alarmId, Long langId, String translation) throws SQLException;

    public AlarmDto getById(Long id) throws SQLException;

    public List<AlarmDto> getAll() throws SQLException;

    public List<AlarmDto> getAll(Long langId) throws SQLException;
}