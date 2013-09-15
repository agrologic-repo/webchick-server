package com.agrologic.app.dao;

import com.agrologic.app.model.Data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * DAO for the {@link  com.agrologic.app.model.Data}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Data} objects.
 *
 * @author Valery Manakhimov
 */
public interface DataDao {

    static final String CANNOT_RETRIEVE_DATA_FROM_DATABASE = "Cannot Retrieve Data From DataBase";
    static final String TRANSACTION_ROLLED_BACK = "Transaction is being rolled back";

    /**
     * Insert a new alarm row to table alarms .
     *
     * @param data an objects that encapsulates an data attributes .
     * @throws SQLException if failed to insert new data to the database .
     */
    void insert(Data data) throws SQLException;

    void update(Data data) throws SQLException;

    void remove(Long dataId) throws SQLException;

    /**
     * Insert data list
     *
     * @param dataList the data list
     * @throws SQLException if failed to insert to the data table
     */
    void insert(Collection<Data> dataList) throws SQLException;

    /**
     * Insert special data to special data labels table
     *
     * @param specialList the list of Data objects with special data labels
     * @param programId   the program id
     * @param langId      the language id
     */
    void insertSpecialList(List<Data> specialList, Long programId, Long langId) throws SQLException;

    /**
     * Insert data list translation to data by language table. <br> This is a dictionary of data types .
     *
     * @param dataList the data list
     * @throws SQLException if failed to insert to the data by language table
     */
    void insertTranslation(Collection<Data> dataList) throws SQLException;

    /**
     * Insert data into association table table data.
     *
     * @param dataList the dataList
     * @throws SQLException if failed to insert to the data by language table
     */
    void insertTableData(Long tableId, Long screenId, Long programId, Collection<Data> dataList) throws SQLException;

    void insertDataToTable(Long programId, Long screenId, Long tableId, Long dataId, String display,
                           Integer position) throws SQLException;

    void insertDataList(Long newProgramId, Long oldProgramId) throws SQLException;

    void insertSpecialData(Long programId, Long dataId, Long langId, String label) throws SQLException;

    void insertDataTranslation(Long dataId, Long langId, String translate) throws SQLException;

    void uncheckNotUsedDataOnAllScreens(Long programId, Long controllerId) throws SQLException;

    void removeDataFromTable(Long programId) throws SQLException;

    void removeDataFromTable(Long programId, Long screenId) throws SQLException;

    void removeDataFromTable(Long programId, Long screenId, Long tableId) throws SQLException;

    void removeDataFromTable(Long programId, Long screenId, Long tableId, Long dataId) throws SQLException;

    void removeSpecialDataFromTable(Long programId, Long dataId) throws SQLException;

    void saveChanges(Long programId, Long screenId, Long tableId, Map<Long, String> showOnTableMap,
                     Map<Long, Integer> posOnTableMap) throws SQLException;

    Data getById(Long dataId) throws SQLException;

    Data getById(Long dataId, Long langId) throws SQLException;

    Data getSetClockByController(Long controllerId) throws SQLException;

    Data getSetDateByController(long controllerId) throws SQLException;

    Data getGrowDay(Long controllerId) throws SQLException;

    Data getChangedDataValue(Long controllerId) throws SQLException;

    Collection<Data> getAll() throws SQLException;

    List<Data> getRelays() throws SQLException;

    List<Data> getAlarms() throws SQLException;

    List<Data> getSystemStates() throws SQLException;

    List<Data> getTableDataList(Long programId, Long screenId, Long tableId, String display)
            throws SQLException;

    List<Data> getTableDataList(Long programId, Long screenId, Long tableId, Long langId, String display)
            throws SQLException;

    List<Data> getHistoryDataList() throws SQLException;

    void clearControllerData(Long controllerId) throws SQLException;

    void moveData(Long screenId, Long programId, Long tableId) throws SQLException;

    Collection<Data> getAllBySpecial(Integer special) throws SQLException;

    Collection<Data> getAllWithTranslation() throws SQLException;

    Collection<Data> getControllerData(Long controllerId) throws SQLException;

    Collection<Data> getControllerDataValues(Long controllerId) throws SQLException;

    Collection<Data> getOnlineTableDataList(Long controllerId, Long programId, Long screenId, Long tableId, Long langId)
            throws SQLException;

    /**
     * Return collection of data objects .
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param tableId   the table id
     * @param langId    the language id
     * @return the collection of data objects
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getOnScreenDataList(Long programId, Long screenId, Long tableId, Long langId) throws SQLException;

    /**
     * Retrieves data relays by program id in no special order
     *
     * @param programId the program id
     * @return a list of Data objects, each object reflects a row in table data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataRelays(Long programId) throws SQLException;

    /**
     * Retrieves data data by program id in no special order
     *
     * @param programId a program id
     * @return a list of Data objects, each object reflects a row in table data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataAlarms(Long programId) throws SQLException;

    /**
     * Retrieves data system states by program id in no special order
     *
     * @param programId a program id
     * @return a list of Data objects, each object reflects a row in table data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataSystemStates(Long programId) throws SQLException;

    /**
     * Retrieves special data from special data table.
     *
     * @param programId the program id
     * @param langId    the language id
     * @return a list of Data objects, each object reflects a row in table spacial data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getSpecialData(Long programId, Long langId) throws SQLException;
}


