
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.LanguageDao;

import com.agrologic.app.model.Language;

import com.agrologic.app.util.LanguageUtil;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.Collection;

/**
 * Title: LanguageDaoImpl <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class LanguageDaoImpl implements LanguageDao {
    protected DaoFactory dao;

    public LanguageDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    public void insert(Language language) throws SQLException {
        String            sqlQuery = "insert into languages values (?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, language.getId());
            prepstmt.setString(2, language.getLanguage());
            prepstmt.setString(3, language.getShortLang());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Language To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void insert(Collection<Language> languageList) throws SQLException {
        String            sqlQuery = "insert into languages values (?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con = dao.getConnection();

            // turn off autocommit
            con.setAutoCommit(false);

            int i = 0;

            prepstmt = con.prepareStatement(sqlQuery);

            for (Language language : languageList) {
                prepstmt.setObject(1, language.getId());
                prepstmt.setString(2, language.getLanguage());
                prepstmt.setString(3, language.getShortLang());
                prepstmt.addBatch();

                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch();    // Execute every 200 items.
                    System.out.print(".");
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

                    throw new SQLException("Transaction is being rolled back");
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void update(Language language) throws SQLException {
        String            sqlQuery = "update languages set Lang=?, Short=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, language.getLanguage());
            prepstmt.setString(2, language.getShortLang());
            prepstmt.setObject(3, language.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Update Program In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void remove(Long langId) throws SQLException {
        String            sqlQuery = "delete from languages where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Delete Language From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Long getLanguageId(String l) throws SQLException {
        String     sqlQuery = "select id from languages where short like '%" + l + "%'";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getLong("ID");
            } else {
                return Long.valueOf(1);
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Language ID From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Language getById(Long langId) throws SQLException {
        String            sqlQuery = "select * from languages where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return LanguageUtil.makeLang(rs);
            } else {
                return new Language();
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Language ID From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Language> geAll() throws SQLException {
        String     sqlQuery = "select * from languages";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return LanguageUtil.makeLangList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Languages From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
