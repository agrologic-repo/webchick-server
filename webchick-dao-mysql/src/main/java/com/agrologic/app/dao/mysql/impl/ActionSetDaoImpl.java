package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.ActionSetDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.model.ActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActionSetDaoImpl implements ActionSetDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(ActionSetDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ActionSetDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("actionset");
        this.dao = dao;
    }

    private ActionSet makeActionSet(ResultSet rs) throws SQLException {
        ActionSet actionSet = new ActionSet();

        actionSet.setValueId(rs.getLong("ValueID"));
        actionSet.setDataId(rs.getLong("DataID"));
        actionSet.setLabel(rs.getString("Label"));

        try {
            actionSet.setUnicodeText(rs.getString("UnicodeText"));
        } catch (SQLException e) {
            actionSet.setUnicodeText(rs.getString("Label"));
        }

        try {
            actionSet.setScreenId(rs.getLong("ScreenId"));
        } catch (SQLException e) {

            // ignore
        }

        try {
            actionSet.setProgramId(rs.getLong("ProgramId"));
        } catch (SQLException e) {

            // ignore
        }

        try {
            actionSet.setPosition(rs.getInt("Position"));
        } catch (SQLException e) {

            // ignore
        }

        try {
            actionSet.setDisplayOnPage(rs.getString("DisplayOnPage"));
        } catch (SQLException e) {

            // ignore
        }

        return actionSet;
    }

    private List<ActionSet> makeActionSetList(ResultSet rs) throws SQLException {
        List<ActionSet> actionSets = new ArrayList<ActionSet>();

        while (rs.next()) {
            actionSets.add(makeActionSet(rs));
        }

        return actionSets;
    }

    @Override
    public ActionSet getById(Long valueId) throws SQLException {
        String sqlQuery = "select * from actionset where ValueID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, valueId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeActionSet(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve ActionSet From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(ActionSet ActionSet) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(ActionSet ActionSet) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(Long ActionSetId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertActionSetToTable(Long programId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateActionSetInTable(Long programId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeActionSetFromTable(Long programId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ActionSet> getAll() throws SQLException {
        String sqlQuery = "select * from actionset";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeActionSetList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve ActionSet From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<ActionSet> getAll(Long programId) throws SQLException {

        // call method with default language english
        return getAll(programId, Long.valueOf(1));
    }

    @Override
    public List<ActionSet> getAll(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from actionset as actset " + "left join programactionset as progactset "
                + "on actset.ValueID=progactset.ValueID left join actionsetbylanguage "
                + "on actionsetbylanguage.ValueID=actset.ValueID and actionsetbylanguage.langid=? "
                + "where programid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeActionSetList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve ProgramActionSet From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<ActionSet> getAllOnScreen(Long programId) throws SQLException {
        // call method with default language english
        return getAllOnScreen(programId, Long.valueOf(1));
    }

    @Override
    public List<ActionSet> getAllOnScreen(Long programId, Long langId) throws SQLException {
        String sqlQuery =
                "select * from actionset as actset "
                        + "left join programactionset as progactset on actset.ValueID=progactset.ValueID "
                        + "left join actionsetbylanguage on actionsetbylanguage.ValueID=actset.ValueID and actionsetbylanguage.langid=? "
                        + "where programid=? and actset.ValueID=progactset.ValueID and progactset.DisplayOnPage='yes'";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeActionSetList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve ProgramActionSet From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertActionSetList(List<ActionSet> actionsetList, Long programId) throws SQLException {
        String sqlQuery =
                "insert into programactionset (DataID, ValueID, ScreenID, ProgramID, Position, DisplayOnPage) VALUES (?,?,?,?,?,?) on duplicate key update ValueID=values(ValueID)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();

            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;

            prepstmt = con.prepareStatement(sqlQuery);

            for (ActionSet as : actionsetList) {
                prepstmt.setLong(1, as.getDataId());
                prepstmt.setLong(2, as.getValueId());
                prepstmt.setLong(3, as.getScreenId());
                prepstmt.setLong(4, programId);
                prepstmt.setInt(5, as.getPosition());
                prepstmt.setString(6, as.isDisplayOnPage());
                prepstmt.addBatch();

                if ((i + 1) % 100 == 0) {
                    prepstmt.executeBatch();    // Execute every 1000 items.
                }

                i++;
            }

            prepstmt.executeBatch();
            con.commit();

            // con.close();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException("Transaction is being rolled back", e);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId)
            throws SQLException {
        String sqlQuery = "update programactionset set DisplayOnPage=?, Position=? where ValueID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> keys = showMap.keySet();

            for (Long screenId : keys) {
                final String show = showMap.get(screenId);
                Integer pos = positionMap.get(screenId);

                prepstmt.setString(1, show);
                prepstmt.setInt(2, pos);
                prepstmt.setLong(3, screenId);
                prepstmt.setLong(4, programId);
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve ActionSet From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Long programId, Long screenId, Map<Long, String> showMap, Map<Long, Integer> posMap)
            throws SQLException {
        String sqlQuery = "update programactionset set DisplayOnPage=?, Position=? where ValueID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> keys = showMap.keySet();

            for (Long valueId : keys) {
                final String show = showMap.get(valueId);
                Integer pos = posMap.get(valueId);

                prepstmt.setString(1, show);
                prepstmt.setInt(2, pos);
                prepstmt.setLong(3, valueId);
                prepstmt.setLong(4, programId);
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve ActionSet From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertActionSetTranslation(Long valueId, Long langId, String translate) throws SQLException {
        String sqlQuery =
                "insert into actionsetbylanguage values (?,?,?) on duplicate key update UnicodeText=values(UnicodeText)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, valueId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translate);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Translation To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}



