package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramAlarm;



import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public interface ProgramAlarmDao {
    public void insert(ProgramAlarm programAlarm) throws SQLException;

    public void update(ProgramAlarm programAlarm) throws SQLException;

    public void remove(Long dataId, Integer digitNumber, Long programId) throws SQLException;

    public void insertAlarms(Long programId, Map<Long, Map<Integer, String>> alarmMap) throws SQLException;

    public List<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException;

    public List<ProgramAlarm> getAllProgramAlarms(Long programId, Long langId) throws SQLException;

    public List<ProgramAlarm> getAllProgramAlarms(Long programId, String[] text) throws SQLException;
}



