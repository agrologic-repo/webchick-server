package com.agrologic.app.dao;

import com.agrologic.app.model.ActionSet;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ActionSetDao {

    public ActionSet getById(Long valueId) throws SQLException;

    public void insert(ActionSet ActionSet) throws SQLException;

    public void update(ActionSet ActionSet) throws SQLException;

    public void remove(Long ActionSetId) throws SQLException;

    public void insertActionSetToTable(Long programId) throws SQLException;

    public void updateActionSetInTable(Long programId) throws SQLException;

    public void removeActionSetFromTable(Long programId) throws SQLException;

    public List<ActionSet> getAll() throws SQLException;

    public List<ActionSet> getAll(Long programId) throws SQLException;

    public List<ActionSet> getAll(Long programId, Long langId) throws SQLException;

    public List<ActionSet> getAllOnScreen(Long programid) throws SQLException;

    public List<ActionSet> getAllOnScreen(Long programId, Long langId) throws SQLException;

    public void insertActionSetList(List<ActionSet> actionsetList, Long programId) throws SQLException;

    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId)
            throws SQLException;

    public void saveChanges(Long programId, Long screenId, Map<Long, String> showTableMap,
                            Map<Long, Integer> posDataMap)
            throws SQLException;

    public void insertActionSetTranslation(Long actionsetId, Long langId, String translate) throws SQLException;
}



