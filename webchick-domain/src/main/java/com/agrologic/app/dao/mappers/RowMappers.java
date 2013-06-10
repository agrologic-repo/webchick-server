package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMappers {

    public static AlarmMapper alarm() {
        return new AlarmMapper();
    }

    public static RelayMapper relay() {
        return new RelayMapper();
    }

    public static SystemStateMapper systemstate() {
        return new SystemStateMapper();
    }

    public static ProgramAlarmMapper programAlarm() {
        return new ProgramAlarmMapper();
    }

    public static ProgramMapper program() {
        return new ProgramMapper();
    }

    private static class AlarmMapper implements RowMapper<Alarm> {
        @Override
        public Alarm mapRow(ResultSet rs, int rowNum) throws SQLException {
            Alarm alarm = new Alarm();
            alarm.setId(rs.getLong("ID"));
            alarm.setText(rs.getString("Name"));
            try {
                alarm.setLangId(rs.getLong("LangID"));
            } catch (SQLException ex) {
                // by default language id is english
                alarm.setLangId((long) 1);
            }

            try {
                alarm.setUnicodeText(rs.getString("UnicodeName"));
            } catch (SQLException ex) {
                // if unicode for this alarm does not exist
                // we take the text that was inserted for name
                alarm.setUnicodeText(alarm.getText());
            }
            return alarm;
        }
    }

    private static class RelayMapper implements RowMapper<Relay> {
        @Override
        public Relay mapRow(ResultSet rs, int rowNum) throws SQLException {
            Relay relay = new Relay();
            relay.setId(rs.getLong("ID"));
            relay.setText(rs.getString("Name"));
            try {
                relay.setLangId(rs.getLong("LangID"));
            } catch (SQLException ex) {
                // by default language  id is english
                relay.setLangId((long) 1);
            }

            try {
                relay.setUnicodeText(rs.getString("UnicodeText"));
            } catch (SQLException ex) {
                // if unicode for this relay does not exist
                // we take the text that was inserted for name
                relay.setUnicodeText(relay.getText());
            }

            return relay;
        }
    }

    private static class SystemStateMapper implements RowMapper<SystemState> {
        @Override
        public SystemState mapRow(ResultSet rs, int rowNum) throws SQLException {
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
                systemState.setUnicodeText(systemState.getText());
            }

            return systemState;
        }
    }

    private static class ProgramMapper implements RowMapper<Program> {
        @Override
        public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
            Program program = new Program();
            program.setId(rs.getLong("ProgramID"));
            program.setName(rs.getString("Name"));
            program.setCreatedDate(rs.getString("Created"));
            program.setModifiedDate(rs.getString("Modified"));
            return program;
        }
    }

    private static class ProgramAlarmMapper implements RowMapper<ProgramAlarm> {
        @Override
        public ProgramAlarm mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProgramAlarm programAlarm = new ProgramAlarm();
            programAlarm.setDataId(rs.getLong("DataID"));
            programAlarm.setDigitNumber(rs.getInt("DigitNumber"));
            programAlarm.setText(rs.getString("Text"));
            programAlarm.setProgramId(rs.getLong("ProgramID"));
            try {
                programAlarm.setText(rs.getString("UnicodeName"));
            } catch (SQLException ex) {    /* ignore */
                programAlarm.setText(programAlarm.getText());
            }
            return programAlarm;
        }
    }

}
