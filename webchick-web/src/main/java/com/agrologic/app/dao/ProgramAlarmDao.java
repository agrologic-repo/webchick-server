package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramAlarmDto;



import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public interface ProgramAlarmDao {
    public void insert(ProgramAlarmDto programAlarm) throws SQLException;

    public void update(ProgramAlarmDto programAlarm) throws SQLException;

    public void remove(Long dataId, Integer digitNumber, Long programId) throws SQLException;

    public void insertAlarms(Long programId, Map<Long, Map<Integer, String>> alarmMap) throws SQLException;

    public List<ProgramAlarmDto> getAllProgramAlarms(Long programId) throws SQLException;

    public List<ProgramAlarmDto> getAllProgramAlarms(Long programId, Long langId) throws SQLException;

    public List<ProgramAlarmDto> getAllProgramAlarms(Long programId, String[] text) throws SQLException;
}



