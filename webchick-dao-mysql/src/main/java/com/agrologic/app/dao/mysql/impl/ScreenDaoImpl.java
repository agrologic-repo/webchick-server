package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.dao.mappers.ScreenUtil;
import com.agrologic.app.model.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ScreenDaoImpl implements ScreenDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ScreenDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("programs");
        this.dao = dao;
    }

    @Override
    public void insert(Screen screen) throws SQLException {
        String sqlQuery = "insert into screens values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, screen.getProgramId());
            prepstmt.setString(3, screen.getTitle());
            prepstmt.setString(4, screen.getDisplay());
            prepstmt.setInt(5, screen.getPosition());
            prepstmt.setString(6, screen.getDescript());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_RETRIEVE_SCREENS_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(Screen screen) throws SQLException {
        String sqlQuery = "update screens set Title=?,Position=?,Descript=? where ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, screen.getTitle());
            prepstmt.setInt(2, screen.getPosition());
            prepstmt.setString(3, screen.getDescript());
            prepstmt.setLong(4, screen.getId());
            prepstmt.setLong(5, screen.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_UPDATE_SCREEN_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long programId, Long screenId) throws SQLException {
        String sqlQuery = "delete from screens where ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screenId);
            prepstmt.setLong(2, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_DELETE_SCREEN_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void insert(Collection<Screen> screenList) throws SQLException {
        if ((screenList == null) || screenList.isEmpty()) {
            return;
        }

        String sqlQuery = "insert into screens values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();

            // turn off autocommit
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            for (Screen screen : screenList) {
                prepstmt.setLong(1, screen.getId());
                prepstmt.setLong(2, screen.getProgramId());
                prepstmt.setString(3, screen.getTitle());
                prepstmt.setString(4, screen.getDisplay());
                prepstmt.setInt(5, screen.getPosition());
                prepstmt.setString(6, screen.getDescript());
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
                    throw new SQLException(TRANSACTION_ROLLED_BACK);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void insertTranslation(Collection<Screen> screenList, Long langId) throws SQLException {
        String sqlQuery = "insert into screenbylanguage values (?,?,?) ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();

            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;

            prepstmt = con.prepareStatement(sqlQuery);

            for (Screen screen : screenList) {
                prepstmt = con.prepareStatement(sqlQuery);
                prepstmt.setLong(1, screen.getId());
                prepstmt.setLong(2, langId);
                prepstmt.setString(3, screen.getUnicodeTitle() == null ? "" : screen.getUnicodeTitle());
                prepstmt.addBatch();

                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch();    // Execute every 200 items.
                    System.out.print(".");
                }

                i++;
            }

            System.out.print("");
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
                    throw new SQLException(TRANSACTION_ROLLED_BACK);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Screen getById(Long programId, Long screenId) throws SQLException {
        String sqlQuery = "select * from screens where programID=? and ScreenID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return ScreenUtil.makeScreen(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_SCREENS_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Screen getById(Long programId, Long screenId, Long langId) throws SQLException {
        String sqlQuery = "select * from screens "
                + "inner join screenbylanguage on screenbylanguage.screenid=screens.screenid "
                + "and screenbylanguage.langid=? and screens.ScreenID=? "
                + "and screens.ProgramID=? and screens.DisplayOnPage='yes' order by screens.Position";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return ScreenUtil.makeScreen(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_RETRIEVE_SCREENS_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Screen getById(Long programId, Long screenId, Long langId, boolean showAll) throws SQLException {
        String sqlQuery = "select * from screens as s1 join "
                + "(select UnicodeTitle,ScreenID from screenbylanguage where LangID = ?) "
                + "as s2 where s1.ScreenID=? and s2.ScreenID=s1.ScreenID and ProgramID=? ";

        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnPage='yes'");
        }

        sqlQuery = sqlQuery.concat(" order by Position");

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return ScreenUtil.makeScreen(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Screen> getAllByProgramId(Long programId) throws SQLException {
        String sqlQuery = "select * from screens where ProgramID=? order by Position";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return ScreenUtil.makeScreenList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Screens From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Screen> getAllScreensByProgramAndLang(Long programId, Long langId, boolean showAll) throws SQLException {
        String sqlQuery = "select * from screens "
                + "left join screenbylanguage on screenbylanguage.screenid = screens.screenid "
                + "and screenbylanguage.langid=? " + "where programid=? ";

        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnPage='yes'");
        }

        sqlQuery = sqlQuery.concat(" order by Position");

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return ScreenUtil.makeScreenList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertDefaultScreens(Long newProgramId, Long oldProgramId) throws SQLException {
        String sqlQuery = "insert into screens (ScreenID,ProgramID,Title,DisplayOnPage, Position) "
                + "(select ScreenID, ?,Title,DisplayOnPage,Position from  screens where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, newProgramId);
            prepstmt.setLong(2, oldProgramId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_RETRIEVE_SCREENS_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Long screenId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into screenbylanguage values (?,?,?) on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screenId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Translation To DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertExistScreen(Screen screen) throws SQLException {
        String sqlQuery = "insert into screens values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screen.getId());
            prepstmt.setLong(2, screen.getProgramId());
            prepstmt.setString(3, screen.getTitle());
            prepstmt.setString(4, screen.getDisplay());
            prepstmt.setInt(5, screen.getPosition());
            prepstmt.setString(6, screen.getDescript());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert ScreenDto To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId) throws SQLException {
        String sqlQuery = "update screens set DisplayOnPage=?, Position=? where ScreenID=? and ProgramID=?";
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

            throw new SQLException("Cannot Retrieve Screens From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public int getNextScreenPosByProgramId(Long programId) throws SQLException {
        String sqlQuery = "SELECT max(Position) as NextPos from screens where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("NextPos");
            } else {
                return 1;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Screens From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Long getSecondScreenAfterMain(Long programId) throws SQLException {
        String sqlQuery =
                "select screenid from screens as screnid where programid=? and DisplayOnpage='yes' and screenid > 1 and position > 1 limit 1";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("screenid");
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Screen Id From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Screen> getAll() throws SQLException {
        String sqlQuery = "select * from screens";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return ScreenUtil.makeScreenList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException(CANNOT_RETRIEVE_SCREENS_MESSAGE, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Screen> getAllProgramScreens(final Long programId) throws SQLException {
        return getAllProgramScreens(programId, (long) 1);
    }

    @Override
    public Collection<Screen> getAllProgramScreens(Long programId, Long langId) throws SQLException {
        String sqlQuery = "select * from screens s "
                + "left join screenbylanguage sl on s.screenid= sl.screenid and sl.langid=? "
                + "where s.ProgramID=? and s.DisplayOnPage='yes' order by s.Position";

        PreparedStatement prepstmt = null;
        Connection con = null;
        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            ResultSet rs = prepstmt.executeQuery();
            return ScreenUtil.makeScreenList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException(CANNOT_RETRIEVE_SCREENS_MESSAGE, e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
