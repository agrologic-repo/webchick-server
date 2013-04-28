
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.model.ScreenDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author JanL
 */
public class ScreenDaoImpl extends ConnectorDao implements ScreenDao {
    private static final boolean DEBUG = true;

    private ScreenDto makeScreen(final ResultSet rs) throws SQLException {
        ScreenDto screen = new ScreenDto();

        screen.setId(rs.getLong("ScreenID"));
        screen.setTitle(rs.getString("Title"));
        screen.setProgramId(rs.getLong("ProgramID"));
        screen.setDisplay(rs.getString("DisplayOnPage"));
        screen.setPosition(rs.getInt("Position"));
        screen.setDescript(rs.getString("Descript"));

        // if unicode doesn't occur in result set ignore
        try {
            screen.setUnicodeTitle(rs.getString("UnicodeTitle"));

            if (screen.getUnicodeTitle().equals("")) {
                screen.setUnicodeTitle(rs.getString("Title"));
            }
        } catch (SQLException e) {
            screen.setUnicodeTitle(rs.getString("Title"));
        }

        return screen;
    }

    private List<ScreenDto> makeScreenList(ResultSet rs) throws SQLException {
        List<ScreenDto> screens = new ArrayList<ScreenDto>();

        while (rs.next()) {
            screens.add(makeScreen(rs));
        }

        return screens;
    }

    @Override
    public void insert(ScreenDto screen) throws SQLException {
        String            sqlQuery = "insert into screens values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, screen.getProgramId());
            prepstmt.setString(3, screen.getTitle());
            prepstmt.setString(4, screen.getDisplay());
            prepstmt.setInt(5, screen.getPosition());
            prepstmt.setString(6, screen.getDescript());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert ScreenDto To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(ScreenDto screen) throws SQLException {
        String            sqlQuery = "update screens set Title=?,Position=?,Descript=? where ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, screen.getTitle());
            prepstmt.setInt(2, screen.getPosition());
            prepstmt.setString(3, screen.getDescript());
            prepstmt.setLong(4, screen.getId());
            prepstmt.setLong(5, screen.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Update ScreenDto In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long programId, Long screenId) throws SQLException {
        String            sqlQuery = "delete from screens where ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screenId);
            prepstmt.setLong(2, programId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Delete ScreenDto From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertExistScreen(ScreenDto screen) throws SQLException {
        String            sqlQuery = "insert into screens values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screen.getId());
            prepstmt.setLong(2, screen.getProgramId());
            prepstmt.setString(3, screen.getTitle());
            prepstmt.setString(4, screen.getDisplay());
            prepstmt.setInt(5, screen.getPosition());
            prepstmt.setString(6, screen.getDescript());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert ScreenDto To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertExistActionSetScrean(ScreenDto screen) throws SQLException {
        String            sqlQuery = "insert into programactionset values (?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screen.getId());
            prepstmt.setLong(2, screen.getProgramId());
            prepstmt.setString(3, screen.getTitle());
            prepstmt.setString(4, screen.getDisplay());
            prepstmt.setInt(5, screen.getPosition());
            prepstmt.setString(6, screen.getDescript());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Action Set Button To The DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public ScreenDto getById(Long programId, Long screenId) throws SQLException {
        String            sqlQuery = "select * from screens where programid=? and screenid=? ";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, screenId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeScreen(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }

        // return getById(programId,screenId, Long.valueOf(1));
    }

    @Override
    public ScreenDto getById(Long programId, Long screenId, Long langId) throws SQLException {
        String sqlQuery = "select * from screens as s1 join "
                          + "(select UnicodeTitle,ScreenID from screenbylanguage where LangID = ?)"
                          + " as s2 where s1.ScreenID=? and s2.ScreenID=s1.ScreenID and ProgramID=? "
                          + "and DisplayOnPage='yes' order by Position ";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeScreen(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public ScreenDto getById(Long programId, Long screenId, Long langId, boolean showAll) throws SQLException {
        String sqlQuery = "select * from screens as s1 join "
                          + "(select UnicodeTitle,ScreenID from screenbylanguage where LangID = ?) "
                          + "as s2 where s1.ScreenID=? and s2.ScreenID=s1.ScreenID and ProgramID=? ";

        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnPage='yes'");
        }

        sqlQuery = sqlQuery.concat(" order by Position");

        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, screenId);
            prepstmt.setLong(3, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeScreen(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public int getNextScreenPosByProgramId(Long programId) throws SQLException {
        String            sqlQuery = "SELECT max(Position)  as NextPos from screens where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("NextPos");
            } else {
                return 1;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Screens From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public Long getSecondScreenAfterMain(Long programId) throws SQLException {
        String sqlQuery =
            "select screenid from screens as screnid where programid=? and DisplayOnpage='yes' and screenid > 1 and position > 1 limit 1";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("screenid");
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Screen Id From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertDefaultScreens(Long programId, Long selectedProgramId) throws SQLException {
        String sqlQuery =
            "insert into screens (ScreenID,ProgramID,Title,DisplayOnPage,Position,Descript) "
            + "(select ScreenID, ?, Title, DisplayOnPage, Position, Descript from  screens where ProgramID=?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            prepstmt.setLong(2, selectedProgramId);

            int result = prepstmt.executeUpdate();

            System.out.println("Number effected rows  : " + result);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Screens To DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Long screenId, Long langId, String translation) throws SQLException {
        String sqlQuery =
            "insert into screenbylanguage values (?,?,?) on duplicate key update UnicodeTitle=values(UnicodeTitle)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, screenId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Translation To DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void saveScreens(Map<Long, String> screens, Long programId) throws SQLException {
        String            sqlQuery = "update screens set DisplayOnPage=? where ProgramID=? and ScreenID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> keys = screens.keySet();

            for (Long screenId : keys) {
                final String show = screens.get(screenId);

                prepstmt.setString(1, show);
                prepstmt.setLong(2, programId);
                prepstmt.setLong(3, screenId);
                prepstmt.addBatch();
            }

            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Save Screens In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ScreenDto> getAllByProgramId(Long programId) throws SQLException {
        String            sqlQuery = "select * from screens where ProgramID=? order by Position";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeScreenList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Screens From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId)
            throws SQLException {
        String            sqlQuery = "update screens set DisplayOnPage=?, Position=? where ScreenID=? and ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            Set<Long> keys = showMap.keySet();

            for (Long screenId : keys) {
                final String show = showMap.get(screenId);
                Integer      pos  = positionMap.get(screenId);

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
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Screens From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ScreenDto> getAllScreensByProgramAndLang(Long programId, Long langId, boolean showAll)
            throws SQLException {
        String sqlQuery = "select * from screens "
                          + "left join screenbylanguage on screenbylanguage.screenid = screens.screenid "
                          + "and screenbylanguage.langid=? " + "where programid=? ";

        if (!showAll) {
            sqlQuery = sqlQuery.concat(" and DisplayOnPage='yes'");
        }

        sqlQuery = sqlQuery.concat(" order by Position");

        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);

            ResultSet rs = prepstmt.executeQuery();

            return makeScreenList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



