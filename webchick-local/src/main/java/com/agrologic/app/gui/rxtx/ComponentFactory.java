package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;

import java.awt.*;
import java.util.List;

/**
 * Created by Valery on 5/4/14.
 */
public class ComponentFactory {

    public static DataComponent createAlarmComponent(DataController dataController,
                                                     ComponentOrientation componentOrientation,
                                                     List<ProgramAlarm> programAlarms) {
        return new DataComponent(dataController, componentOrientation, programAlarms);
    }

    public static DataComponent createRelayComponent(DataController dataController, ComponentOrientation componentOrientation, ProgramRelay programRelay) {
        return new DataComponent(dataController, componentOrientation, programRelay);
    }

    public static DataComponent createSystemStateComponent(DataController dataController,
                                                           ComponentOrientation componentOrientation,
                                                           List<ProgramSystemState> programSystemStates) {
        return new DataComponent(dataController, componentOrientation, programSystemStates, "-----");
    }

    public static DataComponent createDataComponent(DataController dataController,
                                                    ComponentOrientation componentOrientation, Controller c,
                                                    DatabaseAccessor dba) {
        return new DataComponent(dataController, componentOrientation, c, dba);
    }

    public static DataComponent createActionButtonComponent(ProgramActionSet pas, ComponentOrientation componentOrientation,
                                                            Controller c, DatabaseAccessor dba) {
        return new DataComponent(pas, componentOrientation, c, dba);
    }

}
