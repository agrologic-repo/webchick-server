package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.mappers.DataUtil;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataDaoImpl implements DataDao {

    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public DataDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("datatable");
        this.dao = dao;
    }

    @Override
    public void insert(Data data) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Updates an existing data row in table datatable
     *
     * @param data an object that encapsulates a data attributes
     * @throws SQLException if failed to update the data in the database
     */
    @Override
    public void update(Data data) throws SQLException {
        String sqlQuery = "update datatable set Label=? where DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, data.getLabel());
            prepstmt.setLong(2, data.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Update Data ");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
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
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(final Collection<Data> dataList) throws SQLException {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        // there is duplicate data elements in dataList we need only unique elements
        Collection<Data> uniqueDataList = Util.getUniqueElements(dataList);
        String sqlQuery = "INSERT INTO DATATABLE "
                + "(DATAID, TYPE, STATUS, READONLY, TITLE, FORMAT, LABEL, ISRELAY, ISSPECIAL ) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (Data data : uniqueDataList) {
                prepstmt.setLong(1, data.getId());
                prepstmt.setLong(2, data.getType());
                prepstmt.setBoolean(3, data.isStatus());
                prepstmt.setBoolean(4, data.getReadonly());
                prepstmt.setString(5, data.getTitle());
                prepstmt.setInt(6, data.getFormat());
                prepstmt.setString(7, data.getLabel());
                prepstmt.setBoolean(8, data.getIsRelay());
                prepstmt.setInt(9, data.getSpecial());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch(); // Execute every 1000 items.
                }
                i++;
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException(TRANSACTION_ROLLED_BACK, e);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertSpecialList(List<Data> specialList, Long programId, Long langId) throws SQLException {
        if (specialList == null || specialList.isEmpty()) {
            return;
        }

        // there is duplicate data elements in dataList we need only unique elements
        Collection<Data> uniqueDataList = DataUtil.getUniqueElements(specialList);

        String sqlQuery = "INSERT INTO SPECIALDATALABELS "
                + "(DATAID, PROGRAMID, LANGID, SPECIALLABEL ) "
                + "VALUES (?, ?, ?, ?)";

        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (Data data : uniqueDataList) {
                prepstmt.setLong(1, data.getId());
                prepstmt.setLong(2, programId);
                prepstmt.setLong(3, langId);
                prepstmt.setString(4, data.getUnicodeLabel());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch(); // Execute every 1000 items.
                }
                i++;
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException(TRANSACTION_ROLLED_BACK, e);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Collection<Data> dataList) throws SQLException {
        String sqlQuery = "insert into databylanguage values (?, ?, ?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);
            for (Data data : dataList) {
                prepstmt.setLong(1, data.getId());
                prepstmt.setLong(2, data.getLangId());
                prepstmt.setString(3, data.getUnicodeLabel());
                prepstmt.addBatch();
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException(TRANSACTION_ROLLED_BACK, e);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTableData(Long tableId, Long screenId, Long programId, Collection<Data> dataList) throws SQLException {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        String sqlQuery = "INSERT INTO TABLEDATA "
                + "(DATAID, TABLEID, SCREENID, PROGRAMID, DISPLAYONTABLE, POSITION ) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (Data data : dataList) {
                prepstmt.setLong(1, data.getId());
                prepstmt.setLong(2, tableId);
                prepstmt.setLong(3, screenId);
                prepstmt.setLong(4, programId);
                prepstmt.setString(5, data.getDisplay());
                prepstmt.setInt(6, data.getPosition());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    int[] res = prepstmt.executeBatch(); // Execute every 1000 items.
                    System.out.print("." + res.length);
                }
                i++;
            }
            int[] res = prepstmt.executeBatch();
            System.out.print("." + res.length);
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException(TRANSACTION_ROLLED_BACK, e);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    /**
     * Insert new data to datatable database
     *
     * @param programId the id of program
     * @param screenId  the id of screen
     * @param tableId   the id of table
     * @param dataId    the id of data
     * @param display   the string to display 'yes' or 'no'
     * @param position  the number of position
     * @throws java.sql.SQLException if failed to update the data in the tabledata
     */
    @Override
    public void insertDataToTable(Long programId, Long screenId, Long tableId, Long dataId, String display, Integer position) throws SQLException {
        String sqlQuery = "insert into tabledata values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
            dao.closeConnection(con);
        }
    }

    /**
     * Insert data id , table id , and program id into association table datatable
     *
     * @param newProgramId the id of added program
     * @param oldProgramId the id of selected program to get data data from it
     * @throws java.sql.SQLException
     */
    @Override
    public void insertDataList(Long newProgramId, Long oldProgramId) throws SQLException {
        String sqlQuery = "insert into tabledata (DataID,TableID,ScreenID,ProgramID,DisplayOnTable,Position ) "
                + "(select DataID,TableID, ScreenID,?, DisplayOnTable, Position from tabledata "
                + "where ProgramID=?)";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, newProgramId);
            prepstmt.setLong(2, oldProgramId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            throw new SQLException("Cannot Retrieve data list From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertSpecialData(Long programId, Long dataId, Long langId, String label) throws SQLException {
        String sqlQuery = "insert into specialdatalabels values (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, langId);
            prepstmt.setString(4, label);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Special label To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertDataTranslation(Long dataId, Long langId, String translate) throws SQLException {
        String sqlQuery = "insert into databylanguage values (?,?,?) on duplicate key update UnicodeLabel=values(UnicodeLabel)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translate);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Translation To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Data getById(Long dataId) throws SQLException {
        String sqlQuery = "select * from datatable where dataid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, dataId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return DataUtil.makeData(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Get Data ID " + dataId + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Data getById(Long dataId, Long langId) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Data getGrowDay(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable as d inner join "
                + "           (select DataID, value from controllerdata where ControllerID=?) "
                + "               as cd on d.DataID=cd.DataID and d.DataID=800";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return DataUtil.makeData(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot retrieve changed controller data");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Data getSetClockByController(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + " inner join controllerdata on datatable.DataID=controllerdata.DataID"
                + " and datatable.DataID=1309 and controllerdata.ControllerID=?";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return DataUtil.makeData(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_DATA_FROM_DATABASE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Data getSetDateByController(long controllerId) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Data getChangedDataValue(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable as d inner join "
                + "           (select DataID, value from newcontrollerdata where ControllerID=?) "
                + "               as cd on d.DataID=cd.DataID";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return DataUtil.makeData(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Update Registration Data");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getAll() throws SQLException {
        String sqlQuery = "select * from datatable";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Validate User In DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    /**
     * Retrieves data relays from datatable
     *
     * @return a list of Data objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    @Override
    public List<Data> getRelays() throws SQLException {
        String sqlQuery = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text='Relays')";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Relays From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    /**
     * Retrieves data alarms from datatable
     *
     * @return a list of Data objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    @Override
    public List<Data> getAlarms() throws SQLException {
        String sqlQuery = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text='Alarms')";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Alarms From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    /**
     * Retrieves data system states from database
     *
     * @return a list of Data objects, each object reflects a row in table datatable
     * @throws java.sql.SQLException if failed to retrieve data from the database
     */
    @Override
    public List<Data> getSystemStates() throws SQLException {
        String sqlQuery = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text='System States')";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve System States From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Data> getTableDataList(Long programId, Long screenId, Long tableId, String display) throws SQLException {
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
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Data List From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Data> getTableDataList(Long programId, Long screenId, Long tableId, Long langId, String display) throws SQLException {
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
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, langId);
            prepstmt.setLong(3, programId);
            prepstmt.setLong(4, screenId);
            prepstmt.setLong(5, tableId);

            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Data List From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Data> getHistoryDataList() throws SQLException {
        String sqlQuery = "select * from datatable "
                + "inner join databylanguage on datatable.DataID=databylanguage.DataID"
                + " and databylanguage.LangID=1 and datatable.isspecial=5 order by datatable.DataID";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retreive DataDto From In DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void clearControllerData(Long controllerId) throws SQLException {
        String sqlQuery = "delete from controllerdata where controllerid=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Clear Controller Data From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void moveData(Long screenId, Long programId, Long tableId) throws SQLException {
        String sqlQuery = "update tabledata set screenid=? where programid=? and tableid=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getAllWithTranslation() throws SQLException {
        String sqlQuery = " select * from datatable "
                + "left join databylanguage "
                + "on datatable.dataid=databylanguage.dataid "
                + "order by datatable.dataid ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_DATA_FROM_DATABASE, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getControllerData(Long cid) throws SQLException {
        String sqlQuery = "select * from controllerdata  where ControllerID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cid);
            ResultSet rs = prepstmt.executeQuery();
            List<Data> dataValueList = (List<Data>) DataUtil.makeDataValueList(rs);
            return dataValueList;
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_DATA_FROM_DATABASE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getControllerDataValues(Long cid, Long programId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "as dt inner join controllerdata "
                + "as cd on dt.dataid=cd.dataid and cd.controllerid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cid);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_DATA_FROM_DATABASE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getOnlineTableDataList(Long controllerId, Long programId, Long screenId, Long tableId,
                                                   Long langId) throws SQLException {
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
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.setLong(4, langId);
            prepstmt.setLong(5, programId);
            prepstmt.setLong(6, langId);
            prepstmt.setLong(7, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Data From In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
//        String sqlQuery = "select * from datatable "
//                + "left join controllerdata cd on cd.dataid=datatable.dataid and cd.controllerid=? and cd.value=-1 "
//                + "left join databylanguage on databylanguage.dataid=datatable.dataid and databylanguage.langid=? "
//                + "where datatable.dataid in (select tabledata.dataid from tabledata  where tabledata.programid=? "
//                + "and tabledata.screenid=? and tabledata.tableid=?)";// and tabledata.displayontable='yes'
//
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, controllerId);
//            prepstmt.setLong(2, langId);
//            prepstmt.setLong(3, programId);
//            prepstmt.setLong(4, screenId);
//            prepstmt.setLong(5, tableId);
//            ResultSet rs = prepstmt.executeQuery();
//            return DataUtil.makeDataList(rs);
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException(CANNOT_RETRIEVE_DATA_FROM_DATABASE, e);
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
    }

    @Override
    public Collection<Data> getOnScreenDataList(Long programId, Long screenId, Long tableId, Long langId)
            throws SQLException {
        String sqlQuery = "select * from tabledata as td "
                + "left join datatable on datatable.dataid=td.dataid "
                + "left join databylanguage on datatable.dataid=databylanguage.dataid and databylanguage.langid=? "
                + "left join specialdatalabels on specialdatalabels.dataid=td.dataid "
                + "and specialdatalabels.programid=td.programid and specialdatalabels.langid=? "
                + "where td.programid=? "
                + "and td.screenid=? "
                + "and td.tableid=? "
                + "and td.displayontable='yes' "
                + "order by td.position ";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            //prepstmt.setLong(1, 57);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, langId);
            prepstmt.setLong(3, programId);
            prepstmt.setLong(4, screenId);
            prepstmt.setLong(5, tableId);
            ResultSet rs = prepstmt.executeQuery();
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//            rsmd.getColumnLabel(columnsNumber);
//            for (int i = 1; i <= columnsNumber; i++) {
//                rs.next();
//                System.out.print(rsmd.getColumnLabel(i) + '\t');
//                System.out.println(rs.getObject(i));
//            }
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_DATA_FROM_DATABASE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
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
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot Update Data ");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    /**
     * Remove all data in specified table from the database.
     *
     * @param programId the program id
     * @param screenId  the screen id
     * @param tableId   the table id
     * @throws SQLException if failed to remove the data from the tabledata
     */
    @Override
    public void removeDataFromTable(Long programId, Long screenId, Long tableId) throws SQLException {
        String sqlQuery = "delete from tabledata where ProgramID=? and ScreenID=? and TableID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.closeConnection(con);
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    /**
     * Removes a data from the datatable database
     *
     * @param tableId the id of the table
     * @param dataId  the id of the data
     * @throws SQLException if failed to remove the data from the tabledata
     */
    @Override
    public void removeDataFromTable(Long programId, Long screenId, Long tableId, Long dataId) throws SQLException {
        String sqlQuery = "delete from tabledata where ProgramID=? and ScreenID=? and TableID=? and DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, tableId);
            prepstmt.setLong(4, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.closeConnection(con);
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeSpecialDataFromTable(Long programId, Long dataId) throws SQLException {
        String sqlQuery = "delete from specialdatalabels where ProgramID=? and DataID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, dataId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.closeConnection(con);
            throw new SQLException("Cannot Delete Data From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Long programId, Long screenId, Long tableId, Map<Long, String> showOnTableMap, Map<Long, Integer> posOnTableMap) throws SQLException {
        String sqlQuery = "update tabledata set DisplayOnTable=?, Position=? where DataID=? and TableID=? and ScreenId=? and ProgramId=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getProgramDataRelays(Long programId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID in "
                + "(select distinct DataID  from programrelays where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getProgramDataAlarms(Long programId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID in "
                + "(select distinct DataID from programalarms where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramAlarms From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getProgramDataSystemStates(Long programId) throws SQLException {
        String sqlQuery = "select * from datatable where DataID in "
                + "(select distinct DataID  from programsysstates where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve ProgramSystemStates From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getSpecialData(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from specialdatalabels "
                + "inner join datatable on datatable.dataid=specialdatalabels.dataid "
                + "where programId=? and langId=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, langId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Special Data Labels From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getAllBySpecial(Integer special) throws SQLException {
        String sqlQuery = "select * from datatable where isspecial = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, special);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Special Data From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
