package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramSystemState;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface ProgramSysStateDao {
    public void insert(ProgramSystemState programSystemState) throws SQLException;

    public void update(ProgramSystemState programSystemState) throws SQLException;

    public void remove(Long dataId, Integer number, Long programId) throws SQLException;

    public void insertSystemStates(Long programId, SortedMap<Long, Map<Integer, String>> systemStateMap)
            throws SQLException;

    public List<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException;

    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException;

    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, String[] text) throws SQLException;
}


