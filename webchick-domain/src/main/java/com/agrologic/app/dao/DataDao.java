package com.agrologic.app.dao;

import com.agrologic.app.model.Data;


import java.sql.SQLException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author JanL
 */
public interface DataDao {

    public static final String CANNOT_RETREIVE_DATA = "Cannot Retreive Data From In DataBase";
    public static final String TRANSACTION_ROLLED_BACK = "Transaction is being rolled back";

    void insert(Data data) throws SQLException;

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
     * @param programId the program id
     * @param langId the language id
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

    // void insertControllerDataValues(Long controllerId, Iterator<Entry<Long, Integer>> dataValues) throws SQLException;
    void update(Data data) throws SQLException;

    void updateRelays() throws SQLException;

    void remove(Long dataId) throws SQLException;

    int getCount() throws SQLException;

    int getCountTranslation() throws SQLException;

    Data getById(Long dataId) throws SQLException;

    Data getGrowDay(Long controllerId) throws SQLException;

    Data getChangedDataValue(Long controllerId) throws SQLException;

    Data getSetClockByController(Long controllerId) throws SQLException;

    Collection<Data> getAll() throws SQLException;

    Collection<Data> getAllBySpecial(Integer special) throws SQLException;

    Collection<Data> getAllWithTranslation() throws SQLException;

    Collection<Data> getControllerData(Long controllerId) throws SQLException;

    Collection<Data> getControllerDataValues(Long controllerId, Long programId) throws SQLException;

    Collection<Data> getControllerRelays(Long controllerId) throws SQLException;

    Collection<Data> getOnlineTableDataList(Long programId, Long controllerId, Long tableId, Long langId)
            throws SQLException;

    Collection<Data> getOnlineTableDataList(Long controllerId, Long programId, Long screenId, Long tableId, Long langId)
            throws SQLException;

    /**
     * Return collection of data objects .
     * @param programId the program id
     * @param screenId  the screen id
     * @param tableId   the table id
     * @param langId    the language id
     * @return          the collection of data objects
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getOnScreenDataList(Long programId, Long screenId, Long tableId, Long langId) throws SQLException;

    /**
     * Uncheck unused data on screens
     * @param programId the program id
     * @throws SQLException if failed to execute sql command
     */
    void uncheckNotUsedDataOnAllScreens(Long programId) throws SQLException;

    /**
     * Retrieves data relays by program id in no special order
     *
     * @param programId the program id
     * @return a list of DataDto objects, each object reflects a row in table data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataRelays(Long programId) throws SQLException;

    /**
     * Retrieves data datas by program id in no special order
     *
     * @param programId a program id
     * @return a list of DataDto objects, each object reflects a row in table data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataAlarms(Long programId) throws SQLException;

    /**
     * Retrieves data system states by program id in no special order
     *
     * @param programId a program id
     * @return a list of DataDto objects, each object reflects a row in table data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getProgramDataSystemStates(Long programId) throws SQLException;

    /**
     * Retrieves special data from special data table.
     *
     * @param programId the program id
     * @param langId the language id
     * @return a list of DataDto objects, each object reflects a row in table spacial data table
     * @throws SQLException if failed to retrieve data from the database
     */
    Collection<Data> getSpecialData(Long programId, Long langId) throws SQLException;
}


