
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.util;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DataUtil {

    public static Data makeData(ResultSet rs) throws SQLException {
        Data data = new Data();
        data.setId(rs.getLong("DataID"));

        try {
            data.setType(rs.getLong("Type"));
            data.setStatus(rs.getBoolean("Status"));
            data.setReadonly(rs.getBoolean("ReadOnly"));
            data.setTitle(rs.getString("Title"));
            data.setFormat(rs.getInt("Format"));
            data.setLabel(rs.getString("Label"));
            data.setSpecial(rs.getInt("IsSpecial"));
            data.setIsRelay(rs.getBoolean("IsRelay"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // if position doesn't occur in result set ignore
        try { data.setPosition(rs.getInt("Position")); } catch (SQLException ex) {}

        // if position doesn't occur in result set ignore
        try { data.setDisplay(rs.getString("DisplayOnTable")); } catch (SQLException ex) {}

        try {
            data.setLangId(rs.getLong("LangID"));
        } catch (SQLException ex) {
            // by default language  id is english
            data.setLangId((long) 1);
        }

        // if value doesn't occur in result set ignore
        try {
            data.setValue(rs.getLong("Value"));
        } catch (SQLException ex) {
        }

        // if unicode doesn't occur in result set ignore
        try {
            if (rs.getString("UnicodeLabel") != null) {
                data.setUnicodeLabel(rs.getString("UnicodeLabel"));
//                data.setLabel(rs.getString("UnicodeLabel"));
            } else {
                data.setUnicodeLabel(rs.getString("Label"));
            }
        } catch (SQLException ex) {
            data.setUnicodeLabel(rs.getString("Label"));
        } catch (Exception ex) {
            data.setUnicodeLabel(rs.getString("Label"));
        }
        try {
            String sl = rs.getString("SpecialLabel");
            if (sl != null) {
                data.setUnicodeLabel(rs.getString("SpecialLabel"));
            }
        } catch (SQLException ex) { /*
             * ignore
             */

        }
        return data;
    }

    public static Data makeDataValue(ResultSet rs) throws SQLException {
        Data data = new Data();

        data.setId(rs.getLong("DataID"));

        // if value doesn't occur in result set ignore
        try {
            data.setValue(rs.getLong("Value"));
        } catch (SQLException ex) {
            /**
             * ignore
             */
        }

        return data;
    }

    public static Collection<Data> makeDataList(ResultSet rs) throws SQLException {
        List<Data> datas = new ArrayList<Data>();

        while (rs.next()) {
            datas.add(makeData(rs));
        }

        return datas;
    }

    public static Collection<Data> makeDataValueList(ResultSet rs) throws SQLException {
        List<Data> datas = new ArrayList<Data>();

        while (rs.next()) {
            datas.add(makeDataValue(rs));
        }

        return datas;
    }

    public static Collection<Data> getUniqueElements(Collection<Data> dataItems) {
        Set<Data> uniqueDataItems = new HashSet<Data>();

        for (Data data : dataItems) {
            uniqueDataItems.add(data);
        }

        return uniqueDataItems;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
