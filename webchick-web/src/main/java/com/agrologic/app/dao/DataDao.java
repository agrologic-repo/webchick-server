package com.agrologic.app.dao;



import com.agrologic.app.model.DataDto;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataDao {

    public void insert(DataDto data) throws SQLException;

    public void update(DataDto data) throws SQLException;

    public void remove(Long dataId) throws SQLException;

    public DataDto getByType(Long type) throws SQLException;

    public DataDto getById(Long dataId) throws SQLException;

    public DataDto getById(Long dataId, Long langId) throws SQLException;

    public DataDto getSetClockByController(long controllerId) throws SQLException;

    public DataDto getSetDateByController(long controllerId) throws SQLException;

    public DataDto getGrowDay(Long controllerId) throws SQLException;

    public void insertDataToTable(Long programId, Long screenId, Long tableId, Long dataId, String display,
                                  Integer position) throws SQLException;

    public void updateOnScreenTableData(Long controllerId, Long programId, Long screenId, Long tableId)
            throws SQLException;

    public void uncheckNotUsedDataOnAllScreens(Long programId) throws SQLException;

    public void uncheckNotUsedDataOnAllScreens(Long programId, Long controllerId) throws SQLException;

    public void removeDataFromTable(Long programId, Long screenId, Long tableId) throws SQLException;

    public void removeDataFromTable(Long programId, Long screenId, Long tableId, Long dataId) throws SQLException;

    public void removeSpecialDataFromTable(Long programId, Long dataId) throws SQLException;

    public void insertDataList(Long newProgramId, Long oldProgramId) throws SQLException;

    public void insertSpecialData(Long programId, Long dataId, Long langId, String label) throws SQLException;

    public void insertDataTranslation(Long dataId, Long langId, String translate) throws SQLException;

    public void saveChanges(Long programId, Long screenId, Long tableId, Map<Long, String> showOnTableMap,
                            Map<Long, Integer> posOnTableMap) throws SQLException;

    public DataDto getControllerGrowDay(Long controllerId) throws SQLException;

    public List<DataDto> getAll() throws SQLException;

    public List<DataDto> getRelays() throws SQLException;

    public List<DataDto> getAlarms() throws SQLException;

    public List<DataDto> getSystemStates() throws SQLException;

    public List<DataDto> getProgramDataRelays(Long programId) throws SQLException;

    public List<DataDto> getProgramDataAlarms(Long programId) throws SQLException;

    public List<DataDto> getProgramDataSystemStates(Long programId) throws SQLException;

    public List<DataDto> getOnScreenControllerValue(Long cellinkId, Long controllerId, Long screenId)
            throws SQLException;

    public List<DataDto> getOnScreenControllerData(Long controllerId) throws SQLException;

    public List<DataDto> getTableDataList(Long programId, Long screenId, Long tableId, String display)
            throws SQLException;

    public List<DataDto> getTableDataList(Long programId, Long screenId, Long tableId, Long langId, String display)
            throws SQLException;

    public List<DataDto> getOnlineTableDataList(Long programId, Long controllerId, Long tableId, Long langId)
            throws SQLException;

    public List<DataDto> getOnlineTableDataList(Long programId, Long controllerId, Long screenId, Long tableId,
            Long langId) throws SQLException;

    public List<DataDto> getHistoryDataList() throws SQLException;

    public List<DataDto> getDataOnScreen(Long programid, Long screenId) throws SQLException;

    public void clearControllerData(Long controllerId)  throws SQLException;

    public void moveData(Long screenId, Long programId, Long tableId)  throws SQLException;
}

