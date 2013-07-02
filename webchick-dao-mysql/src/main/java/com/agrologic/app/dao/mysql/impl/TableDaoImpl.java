package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.mappers.TableUtil;
import com.agrologic.app.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TableDaoImpl implements TableDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(TableDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TableDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("screentable");
        this.dao = dao;
    }

    @Override
    public void insert(Table table) throws SQLException {
        String sqlQuery = "insert into screentable values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, table.getScreenId());
            prepstmt.setLong(3, table.getProgramId());
            prepstmt.setString(4, table.getTitle());
            prepstmt.setString(5, table.getDisplay());
            prepstmt.setInt(6, table.getPosition());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_INSERT_TABLE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(Table table) throws SQLException {
        String sqlQuery = "update screentable set Title=? , Position=? ,ScreenID=? where TableID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, table.getTitle());
            prepstmt.setInt(2, table.getPosition());
            prepstmt.setLong(3, table.getScreenId());
            prepstmt.setLong(4, table.getId());
            prepstmt.setLong(5, table.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_UPDATE_TABLE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long programId, Long screenId, Long tableId) throws SQLException {
        String sqlQuery = "delete from screentable where TableID=? and ScreenID=? and ProgramID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, tableId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_DELETE_TABLE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void insert(Collection<Table> tableList) throws SQLException {
        if ((tableList == null) || tableList.isEmpty()) {
            return;
        }

        String sqlQuery = "insert into screentable values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();

            // turn off autocommit
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            for (Table table : tableList) {
                prepstmt.setLong(1, table.getId());
                prepstmt.setLong(2, table.getScreenId());
                prepstmt.setLong(3, table.getProgramId());
                prepstmt.setString(4, table.getTitle());
                prepstmt.setString(5, table.getDisplay());
                prepstmt.setInt(6, table.getPosition());
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
                    throw new SQLException("Transaction is being rolled back");
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void insertTranslation(Collection<Table> tableList) throws SQLException {
        String sqlQuery = "insert into tablebylanguage values (?,?,?) ";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            prepstmt = con.prepareStatement(sqlQuery);
            for (Table table : tableList) {
                prepstmt.setLong(1, table.getId());
                prepstmt.setLong(2, table.getLangId());
                prepstmt.setString(3, table.getUnicodeTitle());
                try {
                    prepstmt.executeUpdate();
                } catch (Exception e) {
                    // if duplicate raw id
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertExsitTable(Table table) throws SQLException {
        String sqlQuery = "insert into screentable values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertDefaultTables(Long oldProgramId, Long newProgramId) throws SQLException {
//        String sqlQuery = "insert into screentable (TableID, ScreenID,ProgramID, Title, DisplayOnScreen, Position ) " +
//                        "(select TableID, ScreenID, ?, Title, DisplayOnScreen, Position " +
//                        "from screentable where ProgramID=?)";
        String sqlQuery = "insert into screentable (TableID, ScreenID,ProgramID, Title, DisplayOnScreen, Position ) " +
                "(select TableID, ScreenID, ?, Title, DisplayOnScreen, Position from screentable where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, oldProgramId);
            prepstmt.setLong(2, newProgramId);

            int result = prepstmt.executeUpdate();

            System.out.println("Number effected rows  : " + result);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Default ScreenTable To DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTableTranslation(Long tableId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into tablebylanguage values (?,?,?)on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, tableId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Insert Translation To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void moveTable(Table table, Long oldScreenId) throws SQLException {
        String sqlQuery = "update screentable set Title=? , Position=? ,ScreenID=? "
                + "where TableID=? and ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long screenId, Long programId) throws SQLException {
        String sqlQuery =
                "update screentable set DisplayOnScreen=?, Position=? where TableID=? and ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
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
            dao.closeConnection(con);
        }
    }

    @Override
    public Table getById(Long programId, Long screenId, Long tableId) throws SQLException {
        return getById(programId, screenId, tableId, 1L /*default language id is english*/);
    }

    @Override
    public Table getById(Long programId, Long screenId, Long tableId, Long langId) throws SQLException {
        return getTableById(programId, screenId, tableId, langId);
    }

    @Override
    public Table getTableById(Long programId, Long screenId, Long tableId, Long langId) throws SQLException {
        String sqlQuery = "select * from screentable "
                + "left join tablebylanguage on tablebylanguage.tableid=screentable.tableid "
                + "and tablebylanguage.langid=? "
                + "where screentable.programid=? and screentable.screenid=? and screentable.tableid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, screenId);
            prepstmt.setLong(4, tableId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return TableUtil.makeTable(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_RETRIEVE_TABLE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Table> getAll() throws SQLException {
        String sqlQuery = "select * from screentable";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return TableUtil.makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Table> getAllScreenTables(Long programId, Long screenId, String display) throws SQLException {
        String sqlQuery = "select * from screentable order by Position";

        if (!programId.equals("-1")) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.ProgramID=" + programId + " order by ScreenID ";
        }

        if (!screenId.equals("-1")) {
            sqlQuery = "select * from (" + sqlQuery + ") as a where a.ScreenID=" + screenId + " order by ScreenID ";
        }

        if (display.equals("")) {
            sqlQuery = "select * from (" + sqlQuery + ") as b where b.DisplayOnScreen='" + display + "'";
        }

        sqlQuery = "select * from (" + sqlQuery + ") as b order by Position";

        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return TableUtil.makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Table> getAllScreenTables(Long programId, Long screenId, Long langId) throws SQLException {
        return getAllScreenTables(programId, screenId, langId, SHOW_ALL);
    }

    @Override
    public Collection<Table> getAllScreenTables(Long programId, Long screenId, Long langId, int showCondition)
            throws SQLException {
        String sqlQuery = "select * from screentable st "
                + "left join tablebylanguage tl on tl.tableid=st.tableid and tl.langid=? "
                + "where st.programid=? and st.screenid=?";

        switch (showCondition) {
            default:
            case SHOW_ALL:
                sqlQuery = sqlQuery.concat(" order by st.Position");
                break;
            case SHOW_UNCHECKED:
                sqlQuery = sqlQuery.concat(" and DisplayOnScreen='no' order by st.Position");
                break;
            case SHOW_CHECKED:
                sqlQuery = sqlQuery.concat(" and DisplayOnScreen='yes' order by st.Position");
                break;
        }

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, screenId);
            ResultSet rs = prepstmt.executeQuery();
            return TableUtil.makeTableList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_TABLE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Table> getScreenTables(Long programId, Long screenId, Long langId, boolean showAll) throws SQLException {
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
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            prepstmt.setLong(3, screenId);

            ResultSet rs = prepstmt.executeQuery();

            return TableUtil.makeTableList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve ScreenTable From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Table> getAllWithTranslation() throws SQLException {
        String sqlQuery = "SELECT * FROM SCREENTABLE S INNER "
                + "JOIN TABLEBYLANGUAGE T ON S.TABLEID=T.TABLEID ";// GROUP BY S.TABLEID, T.LANGID ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return TableUtil.makeTableList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_TABLE, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}
