package com.agrologic.app.util;


import com.agrologic.app.model.ProgramSystemState;
import com.agrologic.app.model.SystemState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SystemStateUtil {
    public static SystemState makeSystemState(ResultSet rs) throws SQLException {
        SystemState systemState = new SystemState();

        systemState.setId(rs.getLong("ID"));
        systemState.setText(rs.getString("Name"));

        try {
            systemState.setLangId(rs.getLong("LangID"));
        } catch (SQLException ex) {

            // by default language  id is english
            systemState.setLangId((long) 1);
        }

        try {
            systemState.setUnicodeText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {
            systemState.setUnicodeText(rs.getString("Name"));
        }

        return systemState;
    }

    public static Collection<SystemState> makeSystemStateList(ResultSet rs) throws SQLException {
        List<SystemState> systemStates = new ArrayList<SystemState>();

        while (rs.next()) {
            systemStates.add(makeSystemState(rs));
        }

        return systemStates;
    }

    public static ProgramSystemState makeProgramSystemState(ResultSet rs) throws SQLException {
        ProgramSystemState programSystemState = new ProgramSystemState();
        programSystemState.setDataId(rs.getLong("DataID"));
        programSystemState.setNumber(rs.getInt("Number"));
        programSystemState.setText(rs.getString("Text"));
        programSystemState.setProgramId(rs.getLong("ProgramID"));
        programSystemState.setSystemStateNumber(rs.getInt("SystemStateNumber"));
        try {
            programSystemState.setText(rs.getString("UnicodeName"));
        } catch (SQLException ex) { /* ignore */
        }
        try {
            programSystemState.setSystemStateTextId(rs.getLong("SystemStateTextID"));
        } catch (SQLException ex) { /* ignore */
        }
        return programSystemState;
    }

    public static List<ProgramSystemState> makeProgramSystemStateList(ResultSet rs) throws SQLException {
        List<ProgramSystemState> programAlarms = new ArrayList<ProgramSystemState>();

        while (rs.next()) {
            programAlarms.add(makeProgramSystemState(rs));
        }

        return programAlarms;
    }

    public static Collection<SystemState> getUniqueElements(Collection<SystemState> systemStateItems) {
        Set<SystemState> uniqueAlarmItems = new HashSet<SystemState>();

        for (SystemState systemState : systemStateItems) {
            uniqueAlarmItems.add(systemState);
        }

        return uniqueAlarmItems;
    }
}


