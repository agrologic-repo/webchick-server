/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.model.Data;
import com.agrologic.app.util.DataUtil;

/**
 *
 * @author JanL
 */
public class DataDaoImpl implements DataDao {

    protected DaoFactory dao;

    public DataDaoImpl() {
        this(DaoType.MYSQL);
    }

    public DataDaoImpl(DaoType daoType) {
        dao = DaoFactory.getDaoFactory(daoType);
    }

    @Override
    public void insert(Data data) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(final Collection<Data> dataList) throws SQLException {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // there is duplicate data elements in dataList we need only unique elements
        Collection<Data> uniqueDataList = DataUtil.getUniqueElements(dataList);

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

    @Override
    public void update(Data data) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateRelays() throws SQLException {

        String sqlQuery = "update datatable set isrelay=? where isRelay=? and label not like '%Relay%'";
        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, 0);
            prepstmt.setInt(2, 1);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("SQLException: " + e.getMessage());
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long dataId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCount() throws SQLException {
        String sqlQuery = "select count(DataID) from datatable as n";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Count Data from DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public int getCountTranslation() throws SQLException {
        String sqlQuery = "select count(DataID) from databylanguage as n";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Count Data ");
        } finally {
            stmt.close();
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
            throw new SQLException("Cannot retreive changed controller data");
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
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
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

    @Override
    public Collection<Data> getAllWithTranslation() throws SQLException {
        String sqlQuery = " select * from datatable "
                + "left join databylanguage "
                + "on datatable.dataid=databylanguage.dataid "
                + "order by datatable.dataid ";
//        String sqlQuery = " select * from datatable "
//                + "left join databylanguage "
//                + "on datatable.dataid=databylanguage.dataid and databylanguage.langid=2 "
//                + "order by datatable.dataid ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
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
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
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
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getControllerRelays(Long controllerId) throws SQLException {
        String sqlQuery = "select * from datatable as d inner join"
                + "(select DataID, value from controllerdata where ControllerID=? )"
                + " as cd on d.DataID=cd.DataID and d.IsSpecial=1";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getOnlineTableDataList(Long programId, Long controllerId, Long tableId, Long langId)
            throws SQLException {
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
                + "left join  databylanguage "
                + "on databylanguage.DataID = tabledata.DataID and databylanguage.LangID=? ) "
                + "as dt "
                + " left join "
                + " (select * from specialdatalabels where ProgramID=? and LangID=?)"
                + " as sdl on sdl.DataID=cd.DataID "
                + " where cd.DataID=dt.DataID and dt.DisplayOnTable='yes' and dt.TableID= ? and cd.controllerid=? "
                + "order by dt.position";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, langId);
            prepstmt.setLong(3, programId);
            prepstmt.setLong(4, langId);
            prepstmt.setLong(5, tableId);
            prepstmt.setLong(6, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Data> getOnlineTableDataList(Long controllerId, Long programId, Long screenId, Long tableId,
            Long langId) throws SQLException {
        String sqlQuery = "select * from datatable "
                + "left join controllerdata cd on cd.dataid=datatable.dataid and cd.controllerid=? and cd.value=-1 "
                + "left join databylanguage on databylanguage.dataid=datatable.dataid and databylanguage.langid=? "
                + "where datatable.dataid in (select tabledata.dataid from tabledata  where tabledata.programid=? "
                + "and tabledata.screenid=? and tabledata.tableid=?)";// and tabledata.displayontable='yes'

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            prepstmt.setLong(2, langId);
            prepstmt.setLong(3, programId);
            prepstmt.setLong(4, screenId);
            prepstmt.setLong(5, tableId);
            ResultSet rs = prepstmt.executeQuery();
            return DataUtil.makeDataList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
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
            throw new SQLException(CANNOT_RETREIVE_DATA, e);
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
    public void uncheckNotUsedDataOnAllScreens(Long programId) throws SQLException {
        String sqlQuery = "update tabledata set displayontable='no' where programid=? and dataid not in "
                //         + " (select dataid from controllerdata where controllerid=57)";
                + " (select dataid from controllerdata where controllerid=103)";

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
