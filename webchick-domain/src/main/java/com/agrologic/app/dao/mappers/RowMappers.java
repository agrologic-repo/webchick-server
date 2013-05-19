package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.Alarm;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMappers {
    public static AlarmMapper alarm() {
        return new AlarmMapper();
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
    }
}
