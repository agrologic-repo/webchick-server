package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramAlarm;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ProgramRelayDao {
    public void insert(ProgramAlarm programRelay) throws SQLException;

    public void update(ProgramAlarm programRelay) throws SQLException;

    public void remove(Long dataId, Integer bitNumber, Long programId) throws SQLException;

    public void insertRelays(Long programId, Map<Long, Map<Integer, String>> dataRelayMap) throws SQLException;

    public List<Long> getRelayNumberTypes(Long programId) throws SQLException;

    public List<ProgramAlarm> getAllProgramRelays(Long programId) throws SQLException;

    public List<ProgramAlarm> getAllProgramRelays(Long programId, Long langId) throws SQLException;

    public List<ProgramAlarm> getAllProgramRelays(Long programId, String[] text) throws SQLException;
}


