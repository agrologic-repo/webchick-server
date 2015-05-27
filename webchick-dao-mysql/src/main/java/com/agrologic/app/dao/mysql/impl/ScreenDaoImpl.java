package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Screen;
import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ScreenDaoImpl implements ScreenDao {
    protected final Logger logger = LoggerFactory.getLogger(ScreenDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ScreenDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("screens");
    }

    @Override
    public void insert(Screen screen) throws SQLException {
        logger.debug("Inserting screen with title [{}]", screen.getTitle());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("programid", screen.getProgramId());
        valuesToInsert.put("title", screen.getTitle());
        valuesToInsert.put("displayonpage", screen.getDisplay());
        valuesToInsert.put("position", screen.getPosition());
        valuesToInsert.put("descript", screen.getDescript());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Screen screen) throws SQLException {
        logger.debug("Update screen with id [{}]", screen.getId());
        jdbcTemplate.update("update screens set Title=?,Position=?,Descript=? "
                + "where ScreenID=? and ProgramID=?",
                new Object[]{screen.getTitle(), screen.getPosition(),
                        screen.getDescript(), screen.getId(),
                        screen.getProgramId()});
    }

    @Override
    public void remove(Long programId, Long screenId) throws SQLException {
        Validate.notNull(programId, "Program Id can not be null");
        Validate.notNull(screenId, "Screen Id can not be null");
        logger.debug("Delete screen with id [{}]", screenId);
        jdbcTemplate.update("delete from screens where ScreenID=? and ProgramID=?", new Object[]{screenId, programId});
    }

    @Override
    public void insert(final Collection<Screen> screens) throws SQLException {

        logger.debug("Insert collection of screens ");
        final String sql = "insert into screens values (?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Screen screen = Lists.newArrayList(screens).get(i);
                ps.setLong(1, screen.getId());
                ps.setLong(2, screen.getProgramId());
                ps.setString(3, screen.getTitle());
                ps.setString(4, screen.getDisplay());
                ps.setInt(5, screen.getPosition());
                ps.setString(6, screen.getDescript());
            }

            @Override
            public int getBatchSize() {
                return screens.size();
            }
        });
    }

    public void insertTranslation(final Collection<Screen> screens, final Long langId) throws SQLException {
        logger.debug("Insert collection translation of screens ");
        final String sql = "insert into screenbylanguage values (?,?,?) " +
                "on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Screen screen = Lists.newArrayList(screens).get(i);
                ps.setLong(1, screen.getId());
                ps.setLong(2, langId);
                ps.setString(3, screen.getUnicodeTitle() == null ? "" : screen.getUnicodeTitle());
            }

            @Override
            public int getBatchSize() {
                return screens.size();
            }
        });
    }

    @Override
    public Screen getById(Long programId, Long screenId) throws SQLException {
        logger.debug("Get screen with id [{}]", screenId);
        String sqlQuery = "select * from screens where programID=? and ScreenID=?";
        List<Screen> screens = jdbcTemplate.query(sqlQuery, new Object[]{programId, screenId}, RowMappers.screen());
        if (screens.isEmpty()) {
            return null;
        }
        return screens.get(0);
    }

    @Override
    public Screen getById(Long programId, Long screenId, Long langId) throws SQLException {
        logger.debug("Get screen with id [{}]", screenId);
        String sqlQuery = "select * from ( " +
                "select * from screens " +
                "where ScreenID=? " +
                "and screens.ProgramID=? " +
                "and screens.DisplayOnPage='yes') as screens " +
                "left join screenbylanguage on screenbylanguage.screenid=screens.screenid " +
                "and screenbylanguage.langid=? order by screens.Position";
        List<Screen> screens = jdbcTemplate.query(sqlQuery,
                new Object[]{screenId, programId, langId}, RowMappers.screen());
        if (screens.isEmpty()) {
            return null;
        }
        return screens.get(0);
    }

    @Override
    public Screen getById(Long programId, Long screenId, Long langId, boolean showAll) throws SQLException {
        logger.debug("Get screen with id [{}]", screenId);
        String sqlQuery = "select * from screens as s1 join "
                + "(select UnicodeTitle,ScreenID from screenbylanguage "
                + "where LangID = ?) as s2 where s1.ScreenID=? "
                + "and s2.ScreenID=s1.ScreenID and ProgramID=? ";
        List<Screen> screens = jdbcTemplate.query(sqlQuery,
                new Object[]{langId, screenId, programId, showAll},
                RowMappers.screen());
        if (screens.isEmpty()) {
            return null;
        }
        return screens.get(0);
    }

    @Override
    public Collection<Screen> getAllByProgramId(Long programId) throws SQLException {
        logger.debug("Get screen with id [{}]", programId);
        String sqlQuery = "select * from screens where ProgramID=? order by Position";
        return jdbcTemplate.query(sqlQuery, new Object[]{programId}, RowMappers.screen());
    }

    @Override
    public Collection<Screen> getAllScreensByProgramAndLang(Long programId, Long langId, boolean displayOnPage)
            throws SQLException {
        logger.debug("Get screen with id [{}]", programId);
        String sqlQuery = "select * from screens left join screenbylanguage "
                + "on screenbylanguage.screenid = screens.screenid "
                + "and screenbylanguage.langid=? "
                + "where programid=?"
                + " and displayonpage like ? "
                + " order by position ";
        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId, displayOnPage == false ? "%yes%" : "%%"}, RowMappers.screen());
    }

    @Override
    public void insertDefaultScreens(Long newProgramId, Long oldProgramId) throws SQLException {
        logger.debug("Insert screens ");
        final String sqlQuery = "insert into screens (ScreenID, ProgramID, Title, DisplayOnPage, Position, Descript) "
                + "(select ScreenID, ?, Title, DisplayOnPage, Position, Descript from screens where ProgramID=?)";
        jdbcTemplate.update(sqlQuery, new Object[]{newProgramId, oldProgramId});
    }

    @Override
    public void insertTranslation(Long screenId, Long langId, String translation) throws SQLException {
        logger.debug("Insert screen translation ");
        final String sqlQuery = "insert into screenbylanguage values (?,?,?) " +
                "on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        jdbcTemplate.update(sqlQuery, new Object[]{screenId, langId, translation});
    }

    @Override
    public void insertExistScreen(Screen screen) throws SQLException {
        logger.debug("Inserting screen with title [{}]", screen.getTitle());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("screenid", screen.getId());
        valuesToInsert.put("programid", screen.getProgramId());
        valuesToInsert.put("title", screen.getTitle());
        valuesToInsert.put("displayonpage", screen.getDisplay());
        valuesToInsert.put("position", screen.getPosition());
        valuesToInsert.put("descript", screen.getDescript());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void uncheckNotUsedScreenInProgram(Long programId) throws SQLException {
        String sql = "update screens set displayonpage='no' where programid=? and Title<>'Graphs' and screenid not in\n" +
                "(select distinct(screenid) from screentable as st where programid=? and displayonscreen='yes' );";
        jdbcTemplate.update(sql, programId, programId);
    }

    @Override
    public void saveChanges(final Map<Long, String> showMap, final Map<Long, Integer> positionMap, final Long programId) throws SQLException {
        logger.debug("Save changes of screen position and show ");
        final String sql = "update screens set DisplayOnPage=?, Position=? where ScreenID=? and ProgramID=?";
        final List<Long> screenIds = new ArrayList(showMap.size());
        final List<String> showFlags = new ArrayList(showMap.size());
        final List<Integer> positions = new ArrayList(showMap.size());
        Set<Long> keys = showMap.keySet();
        for (Long screenId : keys) {
            screenIds.add(screenId);
            showFlags.add(showMap.get(screenId));
            positions.add(positionMap.get(screenId));
        }

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, showFlags.get(i));
                ps.setInt(2, positions.get(i));
                ps.setLong(3, screenIds.get(i));
                ps.setLong(4, programId);
            }

            @Override
            public int getBatchSize() {
                return showMap.size();
            }
        });
    }

    @Override
    public void moveUp(Long programId, Long screenId, Integer position) throws SQLException {
        String sqlPrevScreen = "select * from screens where programid=? and position=?";

        List<Screen> result = jdbcTemplate.query(sqlPrevScreen, new Object[]{programId, position - 1},
                RowMappers.screen());
        if (result.size() > 0) {
            Screen prevPosScreen = result.get(0);
            String sqlChangePosPrevScreen = "update screens set position=? where programid=? and screenid=?";
            jdbcTemplate.update(sqlChangePosPrevScreen, new Object[]{position, programId, prevPosScreen.getId()});

            String sqlChangePosActualScreen = "update screens set position=? where programid=? and screenid=?";
            jdbcTemplate.update(sqlChangePosActualScreen, new Object[]{position - 1, programId, screenId});
        }
    }

    @Override
    public void moveDown(Long programId, Long screenId, Integer position) throws SQLException {
        String sqlPrevScreen = "select * from screens where programid=? and position=?";

        List<Screen> result = jdbcTemplate.query(sqlPrevScreen, new Object[]{programId, position + 1},
                RowMappers.screen());
        if (result.size() > 0) {
            Screen prevPosScreen = result.get(0);

            String sqlChangePosPrevScreen = "update screens set position=? where programid=? and screenid=?";
            jdbcTemplate.update(sqlChangePosPrevScreen, new Object[]{position, programId, prevPosScreen.getId()});

            String sqlChangePosActualScreen = "update screens set position=? where programid=? and screenid=?";
            jdbcTemplate.update(sqlChangePosActualScreen, new Object[]{position + 1, programId, screenId});
        }

    }

    @Override
    public int getNextScreenPosByProgramId(final Long programId) throws SQLException {
        logger.debug("Calculate position for new screen ");
        String sqlQuery = "select max(Position) as NextPos from screens where ProgramID=?";
        return jdbcTemplate.queryForInt(sqlQuery, new Object[]{programId});
    }

    @Override
    public Long getSecondScreenAfterMain(final Long programId) throws SQLException {
        logger.debug("Calculate second screen index ");
        String sqlQuery = "select screenid from screens as screnid where programid=? and DisplayOnpage='yes' " +
                "and screenid > 1 and position > 1 limit 1";
        return jdbcTemplate.queryForLong(sqlQuery, new Object[]{programId});
    }

    @Override
    public Collection<Screen> getAll() throws SQLException {
        logger.debug("Get all screens ");
        String sqlQuery = "select * from screens";
        return jdbcTemplate.query(sqlQuery, RowMappers.screen());
    }

    @Override
    public Collection<Screen> getAllProgramScreens(final Long programId) throws SQLException {
        return getAllProgramScreens(programId, (long) 1);
    }

    @Override
    public Collection<Screen> getAllProgramScreens(final Long programId, final Long langId) throws SQLException {
        logger.debug("Get screens by program with id [{}]", programId);
        String sqlQuery = "select * from screens s "
                + "left join screenbylanguage sl on s.screenid= sl.screenid and sl.langid=? "
                + "where s.ProgramID=? and s.DisplayOnPage='yes' order by s.Position";
        return jdbcTemplate.query(sqlQuery, new Object[]{langId, programId}, RowMappers.screen());
    }
}
