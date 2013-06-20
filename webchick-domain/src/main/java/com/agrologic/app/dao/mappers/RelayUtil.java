package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.ProgramRelay;
import com.agrologic.app.model.Relay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelayUtil {

    public static Relay makeRelay(ResultSet rs) throws SQLException {
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
            relay.setUnicodeText(rs.getString("Name"));
        }

        return relay;
    }

    public static List<Relay> makeRelayList(ResultSet rs) throws SQLException {
        List<Relay> relays = new ArrayList<Relay>();

        while (rs.next()) {
            relays.add(makeRelay(rs));
        }

        return relays;
    }

    public static ProgramRelay makeProgramRelay(ResultSet rs) throws SQLException {
        ProgramRelay programRelay = new ProgramRelay();

        programRelay.setDataId(rs.getLong("DataID"));
        programRelay.setBitNumber(rs.getInt("BitNumber"));
        programRelay.setText(rs.getString("Text"));
        programRelay.setProgramId(rs.getLong("ProgramID"));
        programRelay.setRelayNumber(rs.getInt("RelayNumber"));
        programRelay.setRelayTextId(rs.getLong("RelayTextID"));

        try {
            programRelay.setUnicodeText(rs.getString("UnicodeText"));
        } catch (SQLException ex) {    /*
             * ignore
             */
            programRelay.setUnicodeText(rs.getString("Text"));
        }

        return programRelay;
    }

    public static List<ProgramRelay> makeProgramRelayList(ResultSet rs) throws SQLException {
        List<ProgramRelay> programRelays = new ArrayList<ProgramRelay>();

        while (rs.next()) {
            programRelays.add(makeProgramRelay(rs));
        }

        return programRelays;
    }

//    public static Util<Relay> getUniqueElements(Util<Relay> relayItems) {
//        Set<Relay> uniqueAlarmItems = new HashSet<Relay>();
//
//        for (Relay relay : relayItems) {
//            uniqueAlarmItems.add(relay);
//        }
//
//        return uniqueAlarmItems;
//    }
}


