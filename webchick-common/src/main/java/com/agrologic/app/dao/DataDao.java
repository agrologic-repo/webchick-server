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
     * @throws java.sql.SQLException if failed to insert new data to the database .
     */
    void insert(Data data) throws SQLException;

    /**
     * Updates an existing data row in table datatable
     *
     * @param data an object that encapsulates a data attributes
     * @throws java.sql.SQLException if failed to update the data in the database
     */
    void update(Data data) throws SQLException;

    /**
     * Removes a data from the datatable database
     *
     * @param dataId the id of the data to be removed from the database
     * @throws java.sql.SQLException if failed to remove the data from the database
     */
    void remove(Long dataId) throws SQLException;

    /**
     * Insert data list
     *
     * @param dataList the data list
     * @throws java.sql.SQLException if failed to insert to the data table
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
     * @throws java.sql.SQLException if failed to insert to the data by language table
     */
    void insertTranslation(Collection<Data> dataList) throws SQLException;

    /**
     * Insert data into association table table data.
     *
     * @param dataList the dataList
     * @throws java.sql.SQLException if failed to insert to the data by language table
     */
    void insertTableData(Long tableId, Long screenId, Long programId, Collection<Data> dataList) throws SQLException;

    /**
     * Insert new data to table data
     *
     * @param programId the id of program
     * @param screenId  the id of screen
     * @param tableId   the id of table
     * @param dataId    the id of data
     * @param display   the string to display 'yes' or 'no'
     * @param position  the number of position
     * @throws java.sql.SQLException if failed to update the data in the table data
     */
    void insertDataToTable(Long programId, Long screenId, Long tableId, Long dataId, String display,
                           Integer position) throws SQLException;

    /**
     * Insert data id , table id , and program id into association table datatable
     *
     * @param newProgramId the id of added program
     * @param oldProgramId the id of selected program to get data data from it
     * @throws java.sql.SQLException if failed to execute the query
     */
    void insertDataList(Long newProgramId, Long oldProgramId) throws SQLException;

    /**
     * @param programId
     * @param dataId
     * @param langId
     * @param label
     * @throws java.sql.SQLException if failed to execute the query
     */
    void insertSpecialData(Long programId, Long dataId, Long langId, String label) throws SQLException;

    /**
     * Insert translation of data to table databylanguage .
     *
     * @param dataId    the data id
     * @param langId    the language id
     * @param translate the translation
     * @throws java.sql.SQLException if failed to execute the query
     */
    void insertDataTranslation(Long dataId, Long langId, String translate) throws SQLException;

    /**
     * Unchecked data on table that not used in given program id and controller
     *
     * @param programId    the program id
     * @param controllerId the controller id that used to get actual data
     * @throws java.sql.SQLException if failed to execute the query
     */
    void uncheckNotUsedDataOnAllScreens(Long programId, Long controllerId) throws SQLException;

    /**
     * Remove all data in specified table from the database.
     *
     * @param programId the program id
     * @throws java.sql.SQLException if failed to remove the data from the tabledata
     */
    void removeDataFromTable(Long programId) throws SQLException;

    /**
     * Remove all data in specified table from the database.
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param tableId   the table id
     * @throws java.sql.SQLException if failed to remove the data from the tabledata
     */
    void removeDataFromTable(Long programId, Long screenId, Long tableId) throws SQLException;

    /**
     * Removes a data from the datatable database
     *
     * @param tableId the id of the table
     * @param dataId  the id of the data
     * @throws java.sql.SQLException if failed to remove the data from the tabledata
     */
    void removeDataFromTable(Long programId, Long screenId, Long tableId, Long dataId) throws SQLException;

    void removeSpecialDataFromTable(Long programId, Long dataId) throws SQLException;

    void saveChanges(Long programId, Long screenId, Long tableId, Map<Long, String> showOnTableMap,
                     Map<Long, Integer> posOnTableMap) throws SQLException;

    void clearControllerData(Long controllerId) throws SQLException;

    void moveData(Long screenId, Long programId, Long tableId) throws SQLException;

    void migrate(String statment) throws SQLException;

    Data getById(Long dataId) throws SQLException;

    Data getById(Long dataId, Long langId) throws SQLException;

    Data getSetClockByController(Long controllerId) throws SQLException;

    Data getSetDateByController(long controllerId) throws SQLException;

    Data getGrowDay(Long controllerId) throws SQLException;

    Data getChangedDataValue(Long controllerId) throws SQLException;

    Collection<Data> find(Long type) throws SQLException;

    Collection<Data> getAll() throws SQLException;

    /**
     * Retrieves data relays from datatable
     *
     * @return a list of Data objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getRelays() throws SQLException;

    /**
     * Retrieves data alarms from datatable
     *
     * @return a list of Data objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getAlarms() throws SQLException;

    /**
     * Retrieves data system states from database
     *
     * @return a list of Data objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getSystemStates() throws SQLException;

    Collection<Data> getSpecial(String string) throws SQLException;

    Collection<Data> getTableDataList(Long programId, Long screenId, Long tableId, String display) throws SQLException;

    Collection<Data> getTableDataList(Long programId, Long screenId, Long tableId, Long langId, String display)
            throws SQLException;

    Collection<Data> getHistoryDataList() throws SQLException;

    Collection<Data> getHistoryDataListByCriteria(String criteria, Long langId) throws SQLException;

    Collection<Data> getAllBySpecial(Integer special) throws SQLException;

    Collection<Data> getAllWithTranslation() throws SQLException;

    Collection<Data> getControllerDataValues(Long controllerId) throws SQLException;

    Collection<Data> getPerHourHistoryDataByControllerValues(Long controllerId) throws SQLException;

    Map<Long, Long> getUpdatedControllerDataValues(Long controllerId) throws SQLException;

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
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getOnScreenDataList(Long programId, Long screenId, Long tableId, Long langId) throws SQLException;

    /**
     * Retrieves data relays by program id in no special order
     *
     * @param programId the program id
     * @return a list of Data objects, each object reflects a row in table data table
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataRelays(Long programId) throws SQLException;

    /**
     * Retrieves data data by program id in no special order
     *
     * @param programId a program id
     * @return a list of Data objects, each object reflects a row in table data table
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataAlarms(Long programId) throws SQLException;

    /**
     * Retrieves data system states by program id in no special order
     *
     * @param programId a program id
     * @return a list of Data objects, each object reflects a row in table data table
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataSystemStates(Long programId) throws SQLException;

    /**
     * Retrieves special data from special data table.
     *
     * @param programId the program id
     * @param langId    the language id
     * @return a list of Data objects, each object reflects a row in table spacial data table
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    Collection<Data> getSpecialData(Long programId, Long langId) throws SQLException;

    /**
     * Copy special data states of selected program
     *
     * @param newProgramId the id of added program
     * @param selectedProgramId the id of selected program to get data data from it
     * @throws java.sql.SQLException if failed to execute the query
     */
    void copySpecialData(Long newProgramId, Long selectedProgramId);
}


