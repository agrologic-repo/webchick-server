package com.agrologic.app.util;

import com.agrologic.app.model.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ControllerUtil {

    /**
     * Help to create controller from result set
     *
     * @param rs a result set
     * @return controller and object that encapsulate a controller attributes
     * @throws java.sql.SQLException if failed to execute statement.
     */
    public static Controller makeController(ResultSet rs) throws SQLException {
        Controller controller = new Controller();

        controller.setId(rs.getLong("ControllerID"));
        controller.setNetName(rs.getString("NetName"));
        controller.setTitle(rs.getString("Title"));
        controller.setName(rs.getString("ControllerName"));
        controller.setArea(rs.getInt("Area"));
        controller.setCellinkId(rs.getLong("CellinkId"));
        controller.setProgramId(rs.getLong("ProgramId"));
        controller.setActive(rs.getBoolean("Active"));

        return controller;
    }

    /**
     * Help to create Collection of controller from result set
     *
     * @param rs a result set
     * @return controllers a Collection of controller objects
     * @throws java.sql.SQLException if failed to execute statement.
     */
    public static Collection<Controller> makeControllerList(ResultSet rs) throws SQLException {
        Collection<Controller> controllers = new ArrayList<Controller>();

        while (rs.next()) {
            controllers.add(makeController(rs));
        }

        return controllers;
    }
}


