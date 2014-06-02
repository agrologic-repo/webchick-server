package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.ProgramAlarm;
import com.agrologic.app.model.ProgramRelay;
import com.agrologic.app.model.ProgramSystemState;
import com.agrologic.app.model.rxtx.DataController;

import java.awt.*;
import java.util.List;

/**
 * Created by Valery on 5/4/14.
 */
public class ComponentFactory {

    public static DataComponent createAlarmComponent(DataController dataController, List<ProgramAlarm> programAlarms, ComponentOrientation componentOrientation) {
        return new DataComponent(dataController, programAlarms, componentOrientation);
    }

    public static DataComponent createRelayComponent(DataController dataController, ProgramRelay programRelay, ComponentOrientation componentOrientation) {
        return new DataComponent(dataController, programRelay, componentOrientation);
    }

    public static DataComponent createSystemStateComponent(DataController dataController, List<ProgramSystemState> programSystemStates, ComponentOrientation componentOrientation) {
        return new DataComponent(dataController, programSystemStates, "-----", componentOrientation);
    }

    public static DataComponent createDataComponent(DataController d, Controller c, DatabaseAccessor dba, ComponentOrientation componentOrientation) {
        return new DataComponent(d, c, dba, componentOrientation);
    }
}
