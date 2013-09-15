package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Table;
import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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
        logger.debug("Creating table with title [{}]", table.getTitle());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("screenid", table.getScreenId());
        valuesToInsert.put("programid", table.getProgramId());
        valuesToInsert.put("title", table.getTitle());
        valuesToInsert.put("displayonscreen", table.getDisplay());
        valuesToInsert.put("position", table.getPosition());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Table table) throws SQLException {
        logger.debug("Update table with id [{}]", table.getId());
        jdbcTemplate.update("update screens set Title=?,Position=?,Descript=? "
                + "where ScreenID=? and ProgramID=?",
                new Object[]{table.getTitle(), table.getPosition(), table.getScreenId(), table.getId(),
                        table.getProgramId()});
    }

    @Override
    public void remove(Long programId, Long screenId, Long tableId) throws SQLException {
        Validate.notNull(programId, "Program Id can not be null");
        Validate.notNull(screenId, "Screen Id can not be null");
        logger.debug("Delete table with id [{}]", tableId);
        jdbcTemplate.update("delete from screentable where TableID=? and ScreenID=? and ProgramID=? ",
                new Object[]{tableId, screenId, programId});
    }

    public void insert(final Collection<Table> tables) throws SQLException {
        logger.debug("Insert collection of tables ");
        final String sql = "insert into screentable values (?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Table table = Lists.newArrayList(tables).get(i);
                ps.setLong(1, table.getId());
                ps.setLong(2, table.getScreenId());
                ps.setLong(3, table.getProgramId());
                ps.setString(4, table.getTitle());
                ps.setString(5, table.getDisplay());
                ps.setInt(6, table.getPosition());
            }

            @Override
            public int getBatchSize() {
                return tables.size();
            }
        });
    }

    @Override
    public void insertDefaultTables(Long oldProgramId, Long newProgramId) throws SQLException {
        logger.debug("Inserting tables from one program to another ");
        String sql = "insert into screentable (TableID, ScreenID,ProgramID, Title, DisplayOnScreen, Position ) " +
                "(select TableID, ScreenID, ?, Title, DisplayOnScreen, Position from screentable where ProgramID=?)";
        jdbcTemplate.update(sql, new Object[]{oldProgramId, newProgramId});
    }

    @Override
    public void insertTableTranslation(Long tableId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into tablebylanguage values (?,?,?) on duplicate key update UnicodeTitle=values(UnicodeTitle)";
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
    public void insertTranslation(final Collection<Table> tables) throws SQLException {
        logger.debug("Insert collection translation of tables ");
        final String sql = "insert into tablebylanguage values (?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Table table = Lists.newArrayList(tables).get(i);
                ps.setLong(1, table.getId());
                ps.setLong(2, table.getLangId());
                ps.setString(3, table.getUnicodeTitle() == null ? "" : table.getUnicodeTitle());
            }

            @Override
            public int getBatchSize() {
                return tables.size();
            }
        });
    }

    @Override
    public void moveTable(Table table, Long oldScreenId) throws SQLException {
        logger.debug("Move table from one screen to another ");
        String sqlQuery = "update screentable set Title=? , Position=? ,ScreenID=? "
                + "where TableID=? and ScreenID=? and ProgramID=?";

        jdbcTemplate.update(sqlQuery, new Object[]{table.getTitle(), table.getPosition(), table.getScreenId(),
                table.getId(), oldScreenId, table.getProgramId()});
    }

    @Override
    public void saveChanges(final Map<Long, String> showMap, final Map<Long, Integer> positionMap, final Long screenId,
                            final Long programId) throws SQLException {

        logger.debug("Save changes of table position and show ");
        final String sql = "update screentable set DisplayOnScreen=?, Position=? where TableID=? and ScreenID=? " +
                "and ProgramID=?";
        final List<Long> tableIds = new ArrayList(showMap.size());
        final List<String> showFlags = new ArrayList(showMap.size());
        final List<Integer> positions = new ArrayList(showMap.size());
        Set<Long> keys = showMap.keySet();
        for (Long tableId : keys) {
            tableIds.add(tableId);
            showFlags.add(showMap.get(tableId));
            positions.add(positionMap.get(tableId));
        }

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, showFlags.get(i));
                ps.setInt(2, positions.get(i));
                ps.setLong(3, tableIds.get(i));
                ps.setLong(4, screenId);
                ps.setLong(5, programId);
            }

            @Override
            public int getBatchSize() {
                return showMap.size();
            }
        });
    }

    @Override
    public Table getById(Long programId, Long screenId, Long tableId) throws SQLException {
        return getById(programId, screenId, tableId, 1L /*default language id is english*/);
    }

    @Override
    public Table getById(Long programId, Long screenId, Long tableId, Long langId) throws SQLException {
        logger.debug("Get tables by program with id [{}]", programId);
        String sqlQuery = "select * from screentable "
                + "left join tablebylanguage on tablebylanguage.tableid=screentable.tableid "
                + "and tablebylanguage.langid=? "
                + "where screentable.programid=? and screentable.screenid=? and screentable.tableid=?";
        List<Table> tables = jdbcTemplate.query(sqlQuery, new Object[]{langId, programId, screenId, tableId},
                RowMappers.table());
        return tables.get(0);
    }

    @Override
    public Collection<Table> getScreenTables(Long programId, Long screenId, Boolean showAll) throws SQLException {
        logger.debug("Get tables by program with id [{}]", programId);
        String sqlQuery = "select * from screentable where programid=? and screenid =? ";
        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnScreen='yes'");
        }
        sqlQuery = sqlQuery.concat(" order by Position");
        return jdbcTemplate.query(sqlQuery, new Object[]{programId, screenId}, RowMappers.table());
    }

    @Override
    public Collection<Table> getScreenTables(Long programId, Long screenId, Long langId, Boolean showAll)
            throws SQLException {
        String sqlQuery = "select * from screentable"
                + " left join tablebylanguage on tablebylanguage.tableid=screentable.tableid"
                + " and tablebylanguage.langid=? where programid=? and screenid=?";
        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnScreen='yes'");
        }
        sqlQuery = sqlQuery.concat(" order by Position");
        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId, screenId}, RowMappers.table());
    }

    @Override
    public Collection<Table> getAllWithTranslation() throws SQLException {
        logger.debug("Get all tables with translation title ");
        String sqlQuery = "SELECT * FROM SCREENTABLE S INNER "
                + "JOIN TABLEBYLANGUAGE T ON S.TABLEID=T.TABLEID ";
        return jdbcTemplate.query(sqlQuery, RowMappers.table());
    }

    @Override
    public Collection<Table> getAll() throws SQLException {
        String sqlQuery = "select * from screentable";
        return jdbcTemplate.query(sqlQuery, RowMappers.table());
    }
}
