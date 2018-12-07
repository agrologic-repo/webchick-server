package com.agrologic.app.dao;

import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * DAO for the {@link  com.agrologic.app.model.Controller}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Controller} objects.
 *
 * @author Valery Manakhimov
 */
public interface ControllerDao {

    /**
     * Insert new controller data to the database.
     *
     * @param controller the new cellink
     * @throws java.sql.SQLException if failed to execute statement.
     */
    void insert(Controller controller) throws SQLException;

    /**
     * Updates an existing controller row in table controller
     *
     * @throws java.sql.SQLException if failed to remove the controller from the database
     */
    void update(Controller controller) throws SQLException;

    /**
     * Removes a controller from the database
     *
     * @throws java.sql.SQLException if failed to remove the controller from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Insert collection of controllers into the database .
     *
     * @param controllers the collection of controllers
     */
    void insert(Collection<Controller> controllers) throws SQLException;

    /**
     * Insert collection of controller data values into the controller data table .
     * <p/>
     * This method is only for using in derby dao implementation .
     * If value of data already exist method just update the value with new value that was
     * received from controller during communication.
     *
     * @param controllerId the controller id to insert data values
     * @param dataValues   the Iterator of Map.Entry pairs of data id and data objects with value
     * @throws java.sql.SQLException if failed to insert
     */
    void insertControllerDataValues(Long controllerId, Iterator<Map.Entry<Long, Data>> dataValues) throws SQLException;

    /**
     * Set controller data value to -1.
     *
     * @param controllerId the controller id
     * @throws java.sql.SQLException if failed to set value.
     */
    void resetControllerData(Long controllerId) throws SQLException;

    /**
     * Update controller data value .
     *
     * @param controllerId the controller id .
     * @param dataId       the data id to change the value.
     * @param value        the new value
     * @throws java.sql.SQLException if failed to change value .
     */
    void updateControllerData(Long controllerId, Long dataId, Long value) throws SQLException;

    /**
     * Insert or update collection of controller data values into the controller data table .
     * <p/>
     * If value of data already exist method just update the value with new value that was
     * received from controller during communication.
     *
     * @param controllerId the controller id to insert data values
     * @param onlineData   the collection of data objects with new values
     * @throws java.sql.SQLException if failed to change value
     */
    void updateControllerData(Long controllerId, Collection<Data> onlineData) throws SQLException;

    Long getControllerDataValue(Long dataId, Long controllerId) throws SQLException;

    /**
     * Update an existing controller graphs rows in table graph24hours .
     *
     * @param controllerId the controller id
     * @param values       the values that represent 24 hour graph for controller.
     * @param updateTime   the time of requested graph.
     * @throws java.sql.SQLException if failed to update values
     */
    void updateControllerGraph(Long controllerId, String values, Timestamp updateTime) throws SQLException;

    /**
     * Update an existing controller graphs rows in table graph24hours .
     *
     * @param controllerId the controller id
     * @param values       the values that represent 24 hour graph for controller.
     * @param updateTime   the time of requested graph.
     * @throws java.sql.SQLException if failed to update values
     */
    void updateControllerHistogram(Long controllerId, String plate, String values, Timestamp updateTime)
            throws SQLException;

    /**
     * Remove controller data of specified controller .
     *
     * @param controllerId the controller id .
     * @throws java.sql.SQLException if failed to remove controller data.
     */
    void removeControllerData(Long controllerId) throws SQLException;

    /**
     * Remove value that has to be changed on the controller .
     *
     * @param controllerId the controller id .
     * @param dataId       the data id .
     * @throws java.sql.SQLException if failed to remove data value .
     */
    void removeChangedValue(Long controllerId, Long dataId) throws SQLException;

    /**
     * Remove all value that has to be changed on the controller .
     *
     * @param controllerId the controller id .
     * @throws java.sql.SQLException if failed to remove data value .
     */
    void removeAllChangedValue(Long controllerId) throws SQLException;

    /**
     * Insert new data value into #newcontrollerdata table .
     * <p/>
     * This data value have to be sent to controller during communication .
     *
     * @param controllerId the controller id
     * @param dataId       the data id
     * @param value        the new value
     * @throws java.sql.SQLException if failed to execute sql query.
     */
    void sendNewDataValueToController(Long controllerId, Long dataId, Long value) throws SQLException;

    /**
     * Insert new data value into #controllerdata table .
     * <p/>
     * We need to change this table because the data that displayed on screen shows values from this table.
     * Regarding change value on screen , user want to see new value when he changes the data.
     *
     * @param controllerId the controller id
     * @param dataId       the data id
     * @param value        the new value
     * @throws java.sql.SQLException if failed to execute sql query.
     */
    void saveNewDataValueOnController(Long controllerId, Long dataId, Long value) throws SQLException;

    /**
     * Get controller graph representing in the string
     *
     * @param id the controller id
     * @return the string that represent graph
     * @throws java.sql.SQLException if failed to execute sql query.
     */
    String getControllerGraph(Long id) throws SQLException;

    /**
     * Retrieve last updated time of graph24hour table of specified controller.
     *
     * @param controllerId the controller id.
     * @return the last update time.
     * @throws java.sql.SQLException if failed to retrieve last update time .
     */
    Timestamp getUpdatedGraphTime(Long controllerId) throws SQLException;

    /**
     * Get update time of history 24 hours .
     *
     * @param id the controller id.
     * @return histogram data update time .
     * @throws java.sql.SQLException if failed to get updated time of histogram.
     */
    Timestamp getHistogramUpdatedTime(Long id) throws SQLException;

    /**
     * Retrieve controller row by given id from table 'controllers'
     *
     * @param id the id of controller
     * @return the controller object
     * @throws java.sql.SQLException if failed to retrieve controller .
     */
    Controller getById(Long id) throws SQLException;

    /**
     * Retrieve controllers of specified cellink.
     *
     * @param cellinkId the cellink id
     * @return the array Collection of controllers.
     * @throws java.sql.SQLException if failed to retrieve controllers.
     */
    Collection<Controller> getAllByCellink(Long cellinkId) throws SQLException;

    /**
     * Retrieve active controllers of specified cellink .
     *
     * @param cellinkId the cellink id.
     * @return the array Collection of controllers
     * @throws java.sql.SQLException if failed to retrieve active controllers.
     */
    Collection<Controller> getActiveCellinkControllers(Long cellinkId) throws SQLException;

    /**
     * Retrieve controller names { Vision 911, Image II and etc.}
     *
     * @return collection of string with controller names.
     * @throws java.sql.SQLException if failed to retrieve controllers names .
     */
    Collection<String> getControllerNames() throws SQLException;

    /**
     * Retrieve all controllers from database.
     *
     * @return collection of controllers .
     * @throws java.sql.SQLException if failed to retrieve controllers .
     */
    Collection<Controller> getAll() throws SQLException;

    /**
     * Retrieve all send string names by creteria
     *
     * @return the map with send string names
     * @throws java.sql.SQLException if failed to retrieve send string names.
     */
    Map<String, String> getControllerSendStringNames(String searchText) throws SQLException;
}