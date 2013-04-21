
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.model.Table;
import com.agrologic.app.util.TableUtil;
import java.sql.*;
import java.util.Collection;

/**
 * Title: TableDaoImpl <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class TableDaoImpl implements TableDao {

    protected DaoFactory dao;

    public TableDaoImpl() {
        this(DaoType.MYSQL);
    }

    public TableDaoImpl(DaoType daoType) {
        this.dao = DaoFactory.getDaoFactory(daoType);
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

            throw new SQLException(CANNOT_RETREIEVE_TABLE, e);
        } finally {
            prepstmt.close();
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
            throw new SQLException(CANNOT_RETREIEVE_TABLE, e);
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
            throw new SQLException(CANNOT_RETREIEVE_TABLE, e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}