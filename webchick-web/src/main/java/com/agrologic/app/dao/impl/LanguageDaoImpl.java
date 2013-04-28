
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.model.LanguageDto;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: LanguageDaoImpl <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class LanguageDaoImpl extends ConnectorDao implements LanguageDao {
    private LanguageDto makeLang(ResultSet rs) throws SQLException {
        LanguageDto lang = new LanguageDto();

        lang.setId(rs.getLong("ID"));
        lang.setLanguage(rs.getString("Lang"));
        lang.setShortLang(rs.getString("Short"));

        return lang;
    }

    private List<LanguageDto> makeLangList(ResultSet rs) throws SQLException {
        List<LanguageDto> langList = new ArrayList<LanguageDto>();

        while (rs.next()) {
            langList.add(makeLang(rs));
        }

        return langList;
    }

    @Override
    public Long getLanguageId(String l) throws SQLException {
        String     sqlQuery = "select id from languages where short like '%" + l + "%'";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getLong("ID");
            } else {
                return Long.valueOf(1);
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Language ID From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<LanguageDto> geAll() throws SQLException {
        String     sqlQuery = "select * from languages";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeLangList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Languages From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public LanguageDto getById(Long langId) throws SQLException {
        String            sqlQuery = "select * from languages where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeLang(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Language ID From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



