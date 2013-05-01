
package com.agrologic.app.util;

import com.agrologic.app.model.Cellink;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CellinkUtil {

    /**
     * Return Cellink object created from result set .
     *
     * @param rs the result set
     * @return cellink the cellink object
     * @throws java.sql.SQLException if failed to execute statement.
     */
    public static Cellink makeCellink(ResultSet rs) throws SQLException {
        Cellink cellink = new Cellink();

        cellink.setId(rs.getLong("CellinkID"));
        cellink.setName(rs.getString("Name"));
        cellink.setPassword(rs.getString("Password"));
        cellink.setTime(rs.getTimestamp("Time"));
        cellink.setIp(rs.getString("IP"));
        cellink.setPort(rs.getInt("Port"));
        cellink.setState(rs.getInt("State"));
        cellink.setScreenId(rs.getLong("ScreenID"));
        cellink.setUserId(rs.getLong("UserID"));
        cellink.setActual(rs.getBoolean("Actual"));
        cellink.setVersion(rs.getString("Version"));
        cellink.setValidate(true);

        return cellink;
    }

    /**
     * Help method to create list of retrieved data .
     *
     * @param rs the result set
     * @return list of cellink
     * @throws java.sql.SQLException if failed to execute statement.
     */
    public static Collection<Cellink> makeCellinkList(ResultSet rs) throws SQLException {
        List<Cellink> cellinks = new ArrayList<Cellink>();

        while (rs.next()) {
            cellinks.add(makeCellink(rs));
        }

        return cellinks;
    }
}


