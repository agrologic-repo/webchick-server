package com.agrologic.app.dao;

import com.agrologic.app.model.TableDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TableDao {

    public void insert(TableDto table) throws SQLException;

    public void update(TableDto table) throws SQLException;

    public void remove(Long programId, Long screenId, Long tableId) throws SQLException;

    public void insertExsitTable(TableDto table) throws SQLException;

    public void insertDefaultTables(Long programId, Long newProgramId) throws SQLException;

    public void insertTableTranslation(Long tableId, Long langId, String translation) throws SQLException;

    public void moveTable(TableDto table, Long screenId) throws SQLException;

    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long screenId, Long programId)
            throws SQLException;

    public TableDto getById(Long programId, Long screenId, Long tableId) throws SQLException;

    public List<TableDto> getAll() throws SQLException;

    public List<TableDto> getScreenTables(Long programId, Long screenId, Long langId, boolean showAll)
            throws SQLException;

    public List<TableDto> getAllScreenTables(String screenIdList) throws SQLException;

    public List<TableDto> getAllScreenTables(Long programId, Long screenId, String display) throws SQLException;

    public List<TableDto> getAllScreenTables(Long programId, Long screenId, Long langId, String display)
            throws SQLException;
}



