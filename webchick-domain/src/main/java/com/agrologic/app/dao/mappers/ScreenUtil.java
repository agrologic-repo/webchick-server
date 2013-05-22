package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.Screen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScreenUtil {

    public static Screen makeScreen(ResultSet rs) throws SQLException {
        Screen screen = new Screen();
        screen.setId(rs.getLong("ScreenID"));
        screen.setTitle(rs.getString("Title"));
        screen.setProgramId(rs.getLong("ProgramID"));
        screen.setDisplay(rs.getString("DisplayOnPage"));
        screen.setPosition(rs.getInt("Position"));
        screen.setDescript(rs.getString("Descript"));
        // if unicode doesn't occur in result set ignore
        try {
            screen.setLangId(rs.getLong("LangId"));
        } catch (SQLException e) {
            screen.setLangId((long) 1);
        }
        try {
            if ((rs.getString("UnicodeTitle") != null)) {
                screen.setUnicodeTitle(rs.getString("UnicodeTitle"));
            } else {
                screen.setUnicodeTitle(rs.getString("Title"));
            }
        } catch (SQLException e) {
            /* ignore */
            screen.setUnicodeTitle(rs.getString("Title"));
        }

        return screen;
    }

    public static Collection<Screen> makeScreenList(ResultSet rs) throws SQLException {
        List<Screen> screens = new ArrayList<Screen>();

        while (rs.next()) {
            screens.add(makeScreen(rs));
        }

        return screens;
    }
}


