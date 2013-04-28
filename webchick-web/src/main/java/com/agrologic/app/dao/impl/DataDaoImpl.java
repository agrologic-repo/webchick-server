/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.impl;

import com.agrologic.app.dao.DataDao;
import com.agrologic.app.model.DataDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DataDaoImpl
 *
 * @author JanL
 */
public class DataDaoImpl extends ConnectorDao implements DataDao {

    private DataDto makeData(ResultSet rs) throws SQLException {
        DataDto data = new DataDto();
        data.setId(rs.getLong("DataID"));
        data.setType(rs.getLong("Type"));
        data.setStatus(rs.getBoolean("Status"));
        data.setReadonly(rs.getBoolean("ReadOnly"));
        data.setTitle(rs.getString("Title"));
        data.setFormat(rs.getInt("Format"));
        data.setLabel(rs.getString("Label"));
        data.setIsRelay(rs.getBoolean("IsRelay"));
        data.setSpecial(rs.getInt("IsSpecial"));

//        // if ishistory doesn't occur in result set ignore
//        try {
//            data.setPosition(rs.getInt("IsHistory"));
//        } catch (SQLException ex) {
//        }

        // if position doesn't occur in result set ignore
        try {
            data.setPosition(rs.getInt("Position"));
        } catch (SQLException ex) {
        }

        // if value doesn't occur in result set ignore
        try {
            data.setValue((Long) rs.getLong("Value"));
        } catch (SQLException ex) {
        }

        // if position doesn't occur in result set ignore
        try {
            data.setDisplay(rs.getString("DisplayOnTable"));
        } catch (SQLException ex) {
        }

        // if unicode doesn't occur in result set ignore
        try {
            data.setUnicodeLabel(rs.getString("UnicodeLabel"));
            if (data.getUnicodeLabel().equals("")) {
                data.setUnicodeLabel(rs.getString("Label"));
            }
        } catch (SQLException ex) {
            data.setUnicodeLabel(rs.getString("Label"));
        } catch (Exception ex) {
            data.setUnicodeLabel(rs.getString("Label"));
        }

        try {
            String sl = rs.getString("SpecialLabel");
            if (sl != null) {
                data.setUnicodeLabel(rs.getString("SpecialLabel"));
            }
        } catch (SQLException ex) { /*
             * ignore
             */

        }

        return data;
    }

    private List<DataDto> makeDataList(ResultSet rs) throws SQLException {
        List<DataDto> datas = new ArrayList<DataDto>();
        while (rs.next()) {
            datas.add(makeData(rs));
        }
        return datas;
    }

    /**
     * Inserts a new data row to table datatable
     *
     * @param data an objects that encapsulates a data attributes
     * @throws SQLException if failed to insert new data to the database
     */
    @Override
    public void insert(DataDto data) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Updates an existing data row in table datatable
     *
     * @param data an object that encapsulates a data attributes
     * @throws SQLException if failed to update the data in the database
     */
    @Override
    public void update(DataDto data) throws SQLException {
        String sqlQuery = "update datatable set Label=? where DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, data.getLabel());
            prepstmt.setLong(2, data.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Update Data ");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Removes a data from the datatable database
     *
     * @param dataId the id of the data to be removed from the database
     * @throws SQLException if failed to remove the data from the database
     */
    @Override
    public void remove(Long dataId) throws SQLException {
        String sqlQuery = "delete from datatable where DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Insert new data to datatable database
     *
     * @param programId the id of program
     * @param screenId the id of screen
     * @param tableId the id of table
     * @param dataId the id of data
     * @param display the string to display 'yes' or 'no'
     * @param position the number of position
     * @throws java.sql.SQLException if failed to update the data in the tabledata
     */
    @Override
    public void insertDataToTable(Long programId, Long screenId, Long tableId, Long dataId, String display, Integer position) throws SQLException {
        String sqlQuery = "insert into tabledata values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setLong(2, tableId);
            prepstmt.setLong(3, screenId);
            prepstmt.setLong(4, programId);
            prepstmt.setString(5, display);
            prepstmt.setInt(6, position);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Insert Data To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Update a data in datatable database
     *
     * @param programId the id of program
     * @param screenId the id of screen
     * @param tableId the id of table
     * @param dataId the id of data
     * @throws java.sql.SQLException if failed to update the data in the tabledata
     */
    @Override
    public void updateOnScreenTableData(Long controllerId, Long programId, Long screenId, Long tableId) throws SQLException {
        String sqlQuery = "update tabledata set displayontable='no'"
                + " where tabledata.programid=? and screenid=? and tableid=? and tabledata.dataid not in "
                + " (select dataid from datatable where datatable.dataid in "
                + " (select dataid from controllerdata "
                + " where controllerdata.dataid=datatable.dataid and controllerdata.controllerid=?))";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.setLong(4, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Update Data ");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Unchecked data on table that not used in given program id
     *
     * @param programId the program id
     * @throws SQLException if failed to execute the query
     */
    @Override
    public void uncheckNotUsedDataOnAllScreens(Long programId) throws SQLException {
        String sqlQuery = "update tabledata set displayontable='no' where programid=? and dataid not in "
                + " (select dataid from controllerdata where controllerid=103)";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot Update Data ");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Unchecked data on table that not used in given program id
     *
     * @param programId the program id
     * @throws SQLException if failed to execute the query
     */
    @Override
    public void uncheckNotUsedDataOnAllScreens(Long programId, Long controllerId) throws SQLException {
        String sqlQuery = "update tabledata set displayontable='no' where programid=? and dataid not in "
                + " (select dataid from controllerdata where controllerid=?)";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot Update Data ");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Removes a data from the datatable database
     *
     * @param tableId the id of the table
     * @param dataId the id of the data
     * @throws SQLException if failed to remove the data from the tabledata
     */
    @Override
    public void removeDataFromTable(Long programId, Long screenId, Long tableId) throws SQLException {
        String sqlQuery = "delete from tabledata where ProgramID=? and ScreenID=? and TableID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Removes a data from the datatable database
     *
     * @param tableId the id of the table
     * @param dataId the id of the data
     * @throws SQLException if failed to remove the data from the tabledata
     */
    @Override
    public void removeDataFromTable(Long programId, Long screenId, Long tableId, Long dataId) throws SQLException {
        String sqlQuery = "delete from tabledata where ProgramID=? and ScreenID=? and TableID=? and DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.setLong(4, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void removeSpecialDataFromTable(Long programId, Long dataId) throws SQLException {
        String sqlQuery = "delete from specialdatalabels where ProgramID=? and DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Insert data id , table id , and program id into association table datatable
     *
     * @param newProgramId the id of added program
     * @param oldProgramId the id of selected program to get data data from it
     * @param tableIdMap the map with
     * @throws java.sql.SQLException
     */
    @Override
    public void insertDataList(Long newProgramId, Long selectedProgramId) throws SQLException {
        String sqlQuery = "insert into tabledata (DataID,TableID,ScreenID,ProgramID,DisplayOnTable,Position ) "
                + "(select DataID,TableID, ScreenID,?, DisplayOnTable, Position from tabledata "
                + "where ProgramID=?)";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, newProgramId);
            prepstmt.setLong(2, selectedProgramId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retrieve data list From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertSpecialData(Long programId, Long dataId, Long langId, String label) throws SQLException {
        String sqlQuery = "insert into specialdatalabels values (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, langId);
            prepstmt.setString(4, label);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Insert Special label To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertDataTranslation(Long dataId, Long langId, String translate) throws SQLException {
        String sqlQuery = "insert into databylanguage values (?,?,?) on duplicate key update UnicodeLabel=values(UnicodeLabel)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translate);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Insert Translation To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Save changes that maded by user in show tables page
     *
     * @param programId the id of program
     * @param screenId the id of screen
     * @param tableId the id of program
     * @param showOnTableMap the map with table id and show on table string ('yes' or 'no')
     * @param posOnTableMap the map with table id and its position on screen
     * @throws java.sql.SQLException if failed to save table in database
     */
    @Override
    public void saveChanges(Long programId, Long screenId, Long tableId, Map<Long, String> showOnTableMap, Map<Long, Integer> posOnTableMap) throws SQLException {
        String sqlQuery = "update tabledata set DisplayOnTable=?, Position=? where DataID=? and TableID=? and ScreenId=? and ProgramId=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);

            prepstmt = con.prepareStatement(sqlQuery);
            Set<Long> keys = showOnTableMap.keySet();
            for (Long dataId : keys) {
                final String show = showOnTableMap.get(dataId);
                Integer pos = posOnTableMap.get(dataId);
                prepstmt.setString(1, show);
                prepstmt.setInt(2, pos);
                prepstmt.setLong(3, dataId);
                prepstmt.setLong(4, tableId);
                prepstmt.setLong(5, screenId);
                prepstmt.setLong(6, programId);
                prepstmt.addBatch();
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Gets data by it id
     *
     * @param dataId a data id
     * @return an objects that encapsulates a data attributes
     * @throws SQLException if failed to retrieve data from the database
     */
    @Override
    public DataDto getByType(Long type) throws SQLException {
        String sqlQuery = "select * from datatable where Type=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, type);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Gets data by it id
     *
     * @param dataId a data id
     * @return an objects that encapsulates a data attributes
     * @throws SQLException if failed to retrieve data from the database
     */
    @Override
    public DataDto getById(Long dataId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Gets data by it id and by language id.
     *
     * @param dataId a data id
     * @return an objects that encapsulates a data attributes
     * @throws SQLException if failed to retrieve data from the database
     */
    @Override
    public DataDto getById(Long dataId, Long langId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join databylanguage on datatable.DataID=databylanguage.DataID "
                + "and datatable.DataID= ? and databylanguage.LangID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setLong(2, langId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public DataDto getSetDateByController(long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable"
                + " inner join controllerdata on datatable.DataID=controllerdata.DataID"
                + " and datatable.DataID=1894 and controllerdata.ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public DataDto getSetClockByController(long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable"
                + " inner join controllerdata on datatable.DataID=controllerdata.DataID"
                + " and datatable.DataID=1309 and controllerdata.ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public DataDto getGrowDay(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable"
                + " inner join controllerdata on datatable.DataID=controllerdata.DataID"
                + " and datatable.DataID=800 and controllerdata.ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public DataDto getControllerGrowDay(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join controllerdata on controllerdata.DataID=datatable.dataid "
                + "and controllerdata.controllerid=? and datatable.dataid=800 ";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeData(rs);
            } else {
                return new DataDto();
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves all data in no special order
     *
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve all data from the database
     */
    @Override
    public List<DataDto> getAll() throws SQLException {
        String sqlQuery = "select * from datatable order by DataID";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeDataList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves data relays by program id in no special order
     *
     * @param programId a program id
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws SQLException if failed to retrieve data from the database
     */
    @Override
    public List<DataDto> getProgramDataRelays(Long programId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID in "
                + "(select distinct DataID  from programrelays where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Validate User In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves data alarms by program id in no special order
     *
     * @param programId a program id
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws SQLException if failed to retrieve data from the database
     */
    @Override
    public List<DataDto> getProgramDataAlarms(Long programId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID in "
                + "(select distinct DataID from programalarms where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Validate User In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves data system states by program id in no special order
     *
     * @param programId a program id
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws SQLException if failed to retrieve data from the database
     */
    @Override
    public List<DataDto> getProgramDataSystemStates(Long programId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID in "
                + "(select distinct DataID  from programsysstates where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Validate User In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves data relays from datatable
     *
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    @Override
    public List<DataDto> getRelays() throws SQLException {
        String sqlQuery = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text='Relays')";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Relays From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves data alarms from datatable
     *
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    @Override
    public List<DataDto> getAlarms() throws SQLException {
        String sqlQuery = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text='Alarms')";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Alarms From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves data system states from database
     *
     * @return a list of DataDto objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    @Override
    public List<DataDto> getSystemStates() throws SQLException {
        String sqlQuery = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text='System States')";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve System States From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<DataDto> getOnScreenControllerData(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join controllerdata on controllerdata.DataID=datatable.dataid "
                + "and controllerdata.controllerid=?";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<DataDto> getOnScreenControllerValue(Long cellinkId, Long controllerId, Long screenId) throws SQLException {
        String sqlQuery = "select * from controllerdata As d JOIN "
                + "(select DataID, position from screendata where ScreenID=? and CellinkID=?)"
                + "as s where d.DataID=s.DataID and d.ControllerID=? order by position";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            prepstmt.setLong(2, controllerId);
            prepstmt.setLong(3, screenId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
        /*
         * SELECT * FROM controllerdata As d JOIN (SELECT DataID, position FROM screendata where ScreenID=1 and
         * CellinkID=1) as s where d.DataID=s.DataID and d.ControllerID=1 order by position
         */

    }

    @Override
    public List<DataDto> getTableDataList(Long programId, Long screenId, Long tableId, Long langId, String display) throws SQLException {

        String sqlQuery = "select * from tabledata "
                + "left join specialdatalabels "
                + "on specialdatalabels.dataid=tabledata.dataid "
                + "and specialdatalabels.programid=tabledata.programid "
                + "and specialdatalabels.langid=? "
                + "left join datatable "
                + "on datatable.dataid=tabledata.dataid "
                + "left join databylanguage "
                + "on databylanguage.dataid=tabledata.dataid and databylanguage.langid=? "
                + "where tabledata.programid=? "
                + "and tabledata.screenid=? "
                + "and tableid=? "
                + "order by position";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, langId);
            prepstmt.setLong(3, programId);
            prepstmt.setLong(4, screenId);
            prepstmt.setLong(5, tableId);

            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Data List From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    /**
     * Retrieves on table data from database
     *
     * @param programId the id of program
     * @param screenId the id of screen
     * @param tableId the id of table
     * @param display the string to display data on page 'yes' or 'no'
     * @return a list of DataDto objects, each object reflects a row in table data table
     * @throws java.sql.SQLException if failed to retrieve all data from the database
     */
    @Override
    public List<DataDto> getTableDataList(Long programId, Long screenId, Long tableId, String display) throws SQLException {
        String sqlQuery = "select datatable.DataID as DataID,"
                + "datatable.Type as Type,"
                + "datatable.Status as Status,"
                + "datatable.ReadOnly as ReadOnly,"
                + "datatable.Title as Title,"
                + "datatable.Format as Format,"
                + "datatable.Label as Label,"
                + "datatable.IsSpecial as IsSpecial,"
                + "datatable.IsRelay as IsRelay,"
                + "tabledata.TableID as TableID,"
                + "tabledata.ProgramID as ProgramID,"
                + "tabledata.ScreenID as ScreenID,"
                + "tabledata.DisplayOnTable as DisplayOnTable,"
                + "tabledata.Position as Position "
                + "from tabledata "
                + "left join datatable on datatable.DataID=tabledata.DataID order by position asc";

        if (programId != null && screenId != null && tableId != null) {
            sqlQuery = " select * from (" + sqlQuery + ") as a where "
                    + " a.TableID=" + tableId + " and "
                    + " a.ScreenID = " + screenId + " and "
                    + " a.ProgramID = " + programId
                    + " order by a.Position";
        }


        if (display != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where DisplayOnTable='" + display + "'";
        }

        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Data List From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<DataDto> getOnlineTableDataList(Long programId, Long controllerId, Long tableId, Long langId) throws SQLException {
        String sqlQuery = "select * from controllerdata as cd join ("
                + "select datatable.DataID as DataID,"
                + "datatable.Type as Type,"
                + "datatable.Status as Status,"
                + "datatable.ReadOnly as ReadOnly,"
                + "datatable.Title as Title,"
                + "datatable.Format as Format,"
                + "datatable.Label as Label,"
                + "datatable.IsSpecial as IsSpecial,"
                + "datatable.IsRelay as IsRelay,"
                + "tabledata.TableID as TableID,"
                + "tabledata.DisplayOnTable as DisplayOnTable,"
                + "tabledata.Position as Position,"
                + "databylanguage.UnicodeLabel as UnicodeLabel "
                + "from datatable "
                + "left join tabledata "
                + "on datatable.DataID = tabledata.DataID and tabledata.ProgramID=? "
                + " left join databylanguage "
                + " on databylanguage.DataID = tabledata.DataID and databylanguage.LangID=? ) "
                + " as dt "
                + " left join "
                + " (select * from specialdatalabels where ProgramID=? and LangID=?)"
                + " as sdl on sdl.DataID=cd.DataID "
                + " where cd.DataID=dt.DataID and dt.DisplayOnTable='yes' and dt.TableID= ? and cd.controllerid=? "
                + " order by dt.position";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, langId);
            prepstmt.setLong(3, programId);
            prepstmt.setLong(4, langId);
            prepstmt.setLong(5, tableId);
            prepstmt.setLong(6, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<DataDto> getOnlineTableDataList(Long programId, Long controllerId, Long screenId, Long tableId, Long langId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join tabledata on tabledata.programid=? "
                + " and tabledata.screenid=? "
                + " and tabledata.tableid=? "
                + " and tabledata.dataid=datatable.dataid  "
                + " and tabledata.displayontable='yes'"
                + " left join databylanguage on datatable.dataid=databylanguage.dataid and databylanguage.langid=? "
                + " left join(select * from specialdatalabels where programid=? and langid=?)"
                + " as sdl on sdl.DataID=datatable.DataID"
                + " inner join "
                + " controllerdata on controllerdata.dataid=datatable.dataid and controllerdata.controllerid=? "// and controllerdata.value <>-1
                + " order by position";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.setLong(4, langId);
            prepstmt.setLong(5, programId);
            prepstmt.setLong(6, langId);
            prepstmt.setLong(7, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive Data From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<DataDto> getHistoryDataList() throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join databylanguage on datatable.DataID=databylanguage.DataID"
                + " and databylanguage.LangID=1 and datatable.isspecial=5 order by datatable.DataID";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<DataDto> getDataOnScreen(Long programId, Long screenId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join tabledata on tabledata.programid=? and tabledata.screenid=? "
                + "and tabledata.dataid=datatable.dataid order by position";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            ResultSet rs = prepstmt.executeQuery();
            return makeDataList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void clearControllerData(Long controllerId) throws SQLException {
        String sqlQuery = "delete from controllerdata where controllerid=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Clear Controller Data From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    public void moveData(Long screenId, Long programId, Long tableId ) throws SQLException {
        String sqlQuery = "update tabledata set screenid=? where programid=? and tableid=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screenId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, tableId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot move data ");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}