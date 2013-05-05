package com.agrologic.app.util;

import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.ProgramAlarm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AlarmUtil {

    public static Alarm makeAlarm(ResultSet rs) throws SQLException {
        Alarm alarm = new Alarm();

        alarm.setId(rs.getLong("ID"));
        alarm.setText(rs.getString("Name"));

        try {
            alarm.setLangId(rs.getLong("LangID"));
        } catch (SQLException ex) {
            // by default language  id is english
            alarm.setLangId((long) 1);
        }

        try {
            alarm.setUnicodeText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {
            alarm.setUnicodeText(rs.getString("Name"));
        }

        return alarm;
    }

    public static Collection<Alarm> makeAlarmList(ResultSet rs) throws SQLException {
        List<Alarm> alarms = new ArrayList<Alarm>();

        while (rs.next()) {
            alarms.add(makeAlarm(rs));
        }

        return alarms;
    }

    public static ProgramAlarm makeProgramAlarm(ResultSet rs) throws SQLException {
        ProgramAlarm programAlarm = new ProgramAlarm();

        programAlarm.setDataId(rs.getLong("DataID"));
        programAlarm.setDigitNumber(rs.getInt("DigitNumber"));
        programAlarm.setText(rs.getString("Text"));
        programAlarm.setProgramId(rs.getLong("ProgramID"));

        try {
            programAlarm.setText(rs.getString("UnicodeName"));
        } catch (SQLException ex) {
            /*
             * ignore
             */
            programAlarm.setText(rs.getString("Text"));
        }

        try {
            programAlarm.setAlarmTextId(rs.getLong("AlarmTextID"));
        } catch (SQLException ex) { /*
             * ignore
             */

        }

        return programAlarm;
    }

    public static List<ProgramAlarm> makeProgramAlarmList(ResultSet rs) throws SQLException {
        List<ProgramAlarm> programAlarms = new ArrayList<ProgramAlarm>();

        while (rs.next()) {
            programAlarms.add(makeProgramAlarm(rs));
        }

        return programAlarms;
    }

    public static Collection<Alarm> getUniqueElements(Collection<Alarm> alarmItems) {
        Set<Alarm> uniqueAlarmItems = new HashSet<Alarm>();

        for (Alarm alarm : alarmItems) {
            uniqueAlarmItems.add(alarm);
        }

        return uniqueAlarmItems;
    }
}


