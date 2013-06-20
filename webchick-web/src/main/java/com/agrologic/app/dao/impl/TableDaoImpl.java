
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;

import com.agrologic.app.dao.TableDao;
import com.agrologic.app.model.TableDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableDaoImpl extends ConnectorDao implements TableDao {
    private TableDto makeTable(ResultSet rs) throws SQLException {
        TableDto table = new TableDto();

        table.setId(rs.getLong("TableID"));
        table.setTitle(rs.getString("Title"));
        table.setScreenId(rs.getLong("ScreenID"));
        table.setProgramId(rs.getLong("ProgramID"));
        table.setDisplay(rs.getString("DisplayOnScreen"));
        table.setPosition(rs.getInt("Position"));

        // if unicode doesn't occur in result set ignore
        try {
            table.setUnicodeTitle(rs.getString("UnicodeTitle"));

            if (table.getUnicodeTitle().equals("")) {
                table.setUnicodeTitle(rs.getString("Title"));
            }
        } catch (SQLException ex) {    /* ignore */
            table.setUnicodeTitle(rs.getString("Title"));
        }

        return table;
    }

    private List<TableDto> makeTableList(ResultSet rs) throws SQLException {
        List<TableDto> screens = new ArrayList<TableDto>();

        while (rs.next()) {
            screens.add(makeTable(rs));
        }

        return screens;
    }

    @Override
    public void insert(TableDto table) throws SQLException {
        String sqlQuery = "insert into screentable values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, table.getScreenId());
            prepstmt.setLong(3, table.getProgramId());
            prepstmt.setString(4, table.getTitle());
            prepstmt.setString(5, table.getDisplay());
            prepstmt.setInt(6, table.getPosition());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Insert ScreenTable To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(TableDto table) throws SQLException {
        String sqlQuery = "update screentable set Title=? , Position=? ,ScreenID=? where TableID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, table.getTitle());
            prepstmt.setInt(2, table.getPosition());
            prepstmt.setLong(3, table.getScreenId());
            prepstmt.setLong(4, table.getId());
            prepstmt.setLong(5, table.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Update ScreenTable In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long programId, Long screenId, Long tableId) throws SQLException {
        String sqlQuery = "delete from screentable where TableID=? and ScreenID=? and ProgramID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, tableId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Delete ScreenTable From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertExsitTable(TableDto table) throws SQLException {
        String sqlQuery = "insert into screentable values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, table.getId());
            prepstmt.setLong(2, table.getScreenId());
            prepstmt.setLong(3, table.getProgramId());
            prepstmt.setString(4, table.getTitle());
            prepstmt.setString(5, table.getDisplay());
            prepstmt.setInt(6, table.getPosition());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());
            e.printStackTrace();

            throw new SQLException("Cannot Insert ScreenTable To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public TableDto getById(Long programId, Long screenId, Long tableId) throws SQLException {
        String sqlQuery = "select * from screentable where TableID=? and ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, tableId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeTable(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TableDto> getAll() throws SQLException {
        String sqlQuery = "select * from screentable";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TableDto> getAllScreenTables(String screenIdList) throws SQLException {
        String sqlQuery = "select * from screentable where screenid in (" + screenIdList
                + ") order by ScreenId,Position";
        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TableDto> getAllScreenTables(Long programId, Long screenId, String display) throws SQLException {
        String sqlQuery = "select * from screentable order by Position";

        if (programId != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.ProgramID=" + programId + " order by ScreenID ";
        }

        if (screenId != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.ScreenID=" + screenId + " order by ScreenID ";
        }

        if (display != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.DisplayOnScreen='" + display + "'";
        }

        sqlQuery = "select * from (" + sqlQuery + ") as b order by Position";

        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TableDto> getAllScreenTables(Long programId, Long screenId, Long langId, String display)
            throws SQLException {
        String sqlQuery = "select * from screentable order by Position";

        if (programId != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.ProgramID=" + programId + " order by ScreenID ";
        }

        if (screenId != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.ScreenID=" + screenId + " order by ScreenID ";
        }

        if (display != null) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.DisplayOnScreen='" + display + "'";
        }

        sqlQuery = "select * from ( " + sqlQuery
                + " ) as t left join (select UnicodeTitle,TableID from tablebylanguage where LangID =" + langId
                + ")" + " as tl  where t.TableID=tl.TableID and DisplayOnScreen='yes' order by Position ";

        Statement stmt = null;
        Connection con = null;

        try {
            con = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<TableDto> getScreenTables(Long programId, Long screenId, Long langId, boolean showAll)
            throws SQLException {
        String sqlQuery = "select * from screentable"
                + " left join tablebylanguage on tablebylanguage.tableid=screentable.tableid"
                + " and tablebylanguage.langid=?" + " where programid=? and screenid=?";

        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnScreen='yes'");
        }

        sqlQuery = sqlQuery.concat(" order by Position");

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, screenId);

            ResultSet rs = prepstmt.executeQuery();

            return makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertDefaultTables(Long programId, Long selectedProgramId) throws SQLException {
        String sqlQuery =
                "insert into screentable (TableID, ScreenID,ProgramID, Title, DisplayOnScreen, Position ) "
                        + "(select TableID, ScreenID, ?, Title, DisplayOnScreen, Position from screentable where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, selectedProgramId);

            int result = prepstmt.executeUpdate();

            System.out.println("Number effected rows  : " + result);
        } catch (SQLException e) {
            throw new SQLException("Cannot Insert Default ScreenTable To DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertTableTranslation(Long tableId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into tablebylanguage values (?,?,?)on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, tableId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Insert Translation To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void moveTable(TableDto table, Long oldScreenId) throws SQLException {
        String sqlQuery = "update screentable set Title=? , Position=? ,ScreenID=? "
                + "where TableID=? and ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, table.getTitle());
            prepstmt.setInt(2, table.getPosition());
            prepstmt.setLong(3, table.getScreenId());
            prepstmt.setLong(4, table.getId());
            prepstmt.setLong(5, oldScreenId);
            prepstmt.setLong(6, table.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Update ScreenTable In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long screenId, Long programId)
            throws SQLException {
        String sqlQuery =
                "update screentable set DisplayOnScreen=?, Position=? where TableID=? and ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> keys = showMap.keySet();

            for (Long tableId : keys) {
                final String show = showMap.get(tableId);
                Integer pos = positionMap.get(tableId);

                prepstmt.setString(1, show);
                prepstmt.setInt(2, pos);
                prepstmt.setLong(3, tableId);
                prepstmt.setLong(4, screenId);
                prepstmt.setLong(5, programId);
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Save Changes In ScreenTable DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



