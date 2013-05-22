package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LanguageUtil {
    public static Language makeLang(ResultSet rs) throws SQLException {
        Language lang = new Language();

        lang.setId(rs.getLong("ID"));
        lang.setLanguage(rs.getString("Lang"));
        lang.setShortLang(rs.getString("Short"));

        return lang;
    }

    public static Collection<Language> makeLangList(ResultSet rs) throws SQLException {
        List<Language> langList = new ArrayList<Language>();

        while (rs.next()) {
            langList.add(makeLang(rs));
        }

        return langList;
    }
}


