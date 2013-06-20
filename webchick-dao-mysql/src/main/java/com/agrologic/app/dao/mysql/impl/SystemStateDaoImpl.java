package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.SystemState;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.*;

public class SystemStateDaoImpl implements SystemStateDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertTranslate;

    public SystemStateDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("systemstatenames");
        this.jdbcInsertTranslate = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsertTranslate.setTableName("systemstatebylanguage");
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(SystemState systemState) throws SQLException {
        logger.debug("Creating systemState with id [{}]", systemState.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("id", systemState.getId());
        valuesToInsert.put("name", systemState.getText());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(SystemState systemState) throws SQLException {
        logger.debug("Update systemState with id [{}]", systemState.getId());
        jdbcTemplate.update("update systemstatenames set Name=? where ID=?", new Object[]{systemState.getText(), systemState.getId()});
    }

    @Override
    public void remove(Long id) throws SQLException {
        Validate.notNull(id, "ID can not be null");
        logger.debug("Delete systemState name with id [{}]", id);
        jdbcTemplate.update("delete from systemStatenames where ID=?", new Object[]{id});

    }

    @Override
    public void insert(Collection<SystemState> systemStateList) throws SQLException {
        // there is duplicate SystemState elements in systemStateList we need only unique elements
        Collection<SystemState> systemStateCollection = Util.getUniqueElements(systemStateList);
        for (SystemState systemState : systemStateCollection) {
            insert(systemState);
        }
    }

    @Override
    public void insertTranslation(Long systemStateId, Long langId, String translation) throws SQLException {
        String sqlQuery = "insert into systemstatebylanguage values (?,?,?) on duplicate key" +
                " update UnicodeName=values(UnicodeName)";
        jdbcTemplate.update(sqlQuery, new Object[]{systemStateId, langId, translation});
    }

    @Override
    public void insertTranslation(Collection<SystemState> systemStates) throws SQLException {
        String sqlQuery = "insert into systemStatebylanguage values (?,?,?) ";
//      + "on duplicate key update UnicodeName=values(UnicodeName)";
        List<SystemState> systemStateList = new ArrayList(systemStates);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (SystemState systemState : systemStateList) {
            Object[] values = new Object[]{
                    systemState.getId(),
                    systemState.getLangId(),
                    systemState.getUnicodeText()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("insert into systemStatebylanguage values (?,?,?) ", batch);
    }

    @Override
    public SystemState getById(Long id) throws SQLException {
        String sqlQuery = "select * from systemStatenames where ID=?";
        List<SystemState> systemStates = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.systemState());
        if (systemStates.isEmpty()) {
            return null;
        }
        return systemStates.get(0);
    }

    @Override
    public Collection<SystemState> getAll() throws SQLException {
        logger.debug("Get all systemState names ");
        return jdbcTemplate.query("select * from systemstatenames order by ID,Name", RowMappers.systemState());
    }

    @Override
    public Collection<SystemState> getAll(Long langId) throws SQLException {
        String sqlQuery = "select s1.id, s1.name, s2.systemstateid, s2.langid, s2.unicodename from systemstatenames s1 "
                + "left join systemstatebylanguage s2 on s1.id=s2.systemstateid and langid=" + langId;
        logger.debug("Get all systemState names with given language id [{}] ", langId);
        return jdbcTemplate.query(sqlQuery, RowMappers.systemState());

    }

    @Override
    public Collection<SystemState> getAllWithTranslation() throws SQLException {
        String sqlQuery = "select * from systemstatenames "
                + "join systemstatebylanguage on systemstatenames.id=systemstatebylanguage.systemstateid "
                + "order by systemstatebylanguage.langid , systemstatebylanguage.systemstateid";
        logger.debug("Get all systemState names with translation to all languages ");
        return jdbcTemplate.query(sqlQuery, RowMappers.systemState());

    }

//    @Override
//    public Collection<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException {
//        String sqlQuery = "select * from programsysstates where ProgramID=? ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, programId);
//            return SystemStateUtil.makeProgramSystemStateList(prepstmt.executeQuery());
//        } catch (SQLException e) {
//            throw new SQLException("Cannot Retrieve ProgramSystemState From DataBase", e);
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

//    @Override
//    public Collection<ProgramSystemState> getSelectedProgramSystemStates(Long programId) throws SQLException {
//        String sqlQuery = "select * from programsysstates where ProgramID=? and TEXT not Like '%None%' "
//                + "and Text not Like '%Damy%' order by DataID, SystemStateNumber";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, programId);
//            return SystemStateUtil.makeProgramSystemStateList(prepstmt.executeQuery());
//        } catch (SQLException e) {
//            throw new SQLException("Cannot Retrieve ProgramSystemState From DataBase", e);
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }
//
//    @Override
//    public Collection<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException {
//        String sqlQuery =
//                "select * from programsysstates as pss "
//                        + " inner join systemstatebylanguage ssbl on "
//                        + " ssbl.SystemStateID=pss.systemstatetextid and ssbl.langid=? and pss.programid=? and pss.text not like '%None%' "
//                        + " and pss.Text not Like '%None%' and pss.Text not Like '%Damy%' order by pss.DataID,pss.Number ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, langId);
//            prepstmt.setLong(2, programId);
//            return SystemStateUtil.makeProgramSystemStateList(prepstmt.executeQuery());
//        } catch (SQLException e) {
//            throw new SQLException("Cannot Retrieve ProgramSystemState From DataBase", e);
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }
}