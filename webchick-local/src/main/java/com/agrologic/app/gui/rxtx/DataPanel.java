package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;

import javax.swing.*;
import java.util.List;

public class DataPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Controller controller;
    private List<DataController> dataList;
    private DatabaseAccessor dbaccess;
    private List<ProgramAlarm> programAlarms;
    private List<ProgramRelay> programRelays;
    private List<ProgramSystemState> programSystemStates;

    public DataPanel(List<DataController> dataList, Controller controller, DatabaseAccessor dbaccess) {
        super(null);
        this.controller = controller;
        this.dbaccess = dbaccess;
        this.dataList = dataList;
    }

    public void initComponents() {
        int maxWidth = getLongestTextLength(dataList);
        maxWidth = (maxWidth < DataComponent.WIDTH)
                ? DataComponent.WIDTH
                : maxWidth;

        int k = 0;
        for (int i = 0; i < dataList.size(); i++) {
            DataController data = dataList.get(i);
            if (data.getIsRelay() == true) {
                k = createRelayComponent(data, i, k, maxWidth);
            } else if (data.isAlarm()) {
                k = createAlarmComponent(data, i, k, maxWidth);
            } else if (data.isSystemState()) {
                k = createSystemStateComponent(data, i, k, maxWidth);
            } else {
                DataComponent dataComponent = new DataComponent(data, DataComponent.X_OFFSET,
                        DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + k),
                        DataComponent.X_OFFSET + maxWidth,
                        DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + k), controller,
                        dbaccess);
                add(dataComponent.getLabel());
                add(dataComponent.getComponent());
            }
        }

        setBounds(DataComponent.X_OFFSET, 30, (maxWidth + maxWidth / 2) - 5,
                DataComponent.HEIGHT * (dataList.size() + k) + 10);
    }

    private int createRelayComponent(DataController data, int i, int k, int maxWidth) {
        if (programRelays == null) {
            return 0;
        }

        int j = k;


        for (ProgramRelay programRelay : programRelays) {
            if (programRelay.getDataId().equals(data.getId())) {

                if (programRelay.getRelayNumber() == 0) {
                    if (controller.getName().equals("WOD")) {
                        if (data.getValue() == null || data.getValue() == -1) {
                            programRelay.setOff();
                        } else {
                            programRelay.init(data.getValueToUI());
                        }

                        DataComponent dataComponent = new DataComponent(data, DataComponent.X_OFFSET,
                                DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j),
                                DataComponent.X_OFFSET + maxWidth,
                                DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j),
                                programRelay);
                        j++;
                        add(dataComponent.getLabel());
                        add(dataComponent.getComponent());
                    } else {
                        continue;
                    }
                } else {
                    if (data.getValue() == null || data.getValue() == -1) {
                        programRelay.setOff();
                    } else {
                        programRelay.init(data.getValueToUI());
                    }

                    DataComponent dataComponent = new DataComponent(data, DataComponent.X_OFFSET,
                            DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j),
                            DataComponent.X_OFFSET + maxWidth,
                            DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j),
                            programRelay);
                    j++;
                    add(dataComponent.getLabel());
                    add(dataComponent.getComponent());
                }
            }
        }
        j--;
        return j;
    }

    private int createAlarmComponent(DataController data, int i, int k, int maxWidth) {
        int j = k;
        DataComponent dataComponent = new DataComponent(data, DataComponent.X_OFFSET,
                DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j),
                DataComponent.X_OFFSET + maxWidth,
                DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j), programAlarms);
        j++;
        add(dataComponent.getLabel());
        add(dataComponent.getComponent());
        j--;
        return j;
    }

    private int createSystemStateComponent(DataController data, int i, int k, int maxWidth) {
        int j = k;
        DataComponent dataComponent = new DataComponent(data, DataComponent.X_OFFSET,
                DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j),
                DataComponent.X_OFFSET + maxWidth - maxWidth,
                DataComponent.Y_OFFSET + DataComponent.HEIGHT * (i + j), programAlarms);
        j++;
        String text = "-----";
        if (data.getValue() == null) {
            data.setValue((long) 0);
        }

        for (ProgramSystemState pss : programSystemStates) {
            long v = data.getValue();
            long n = pss.getSystemStateNumber();
            if (v == n) {
                text = pss.getText();

            }
        }
        add(dataComponent.getLabel());
        JComponent jc = dataComponent.getComponent();
        ((DataLabel) jc).setText(text);
        jc.setSize(maxWidth * 2, jc.getSize().height);
        add(dataComponent.getComponent());
        j--;
        return j;
    }

    public void setProgramAlarms(List<ProgramAlarm> programAlarms) {
        this.programAlarms = programAlarms;
    }

    public void setProgramRelays(List<ProgramRelay> programRelays) {
        this.programRelays = programRelays;
    }

    public void setProgramSystemStates(List<ProgramSystemState> programSystemStates) {
        this.programSystemStates = programSystemStates;
    }

    /**
     * Find longest string in data object to layout components .
     *
     * @param dataList the list of data
     * @return length the length of longest string
     */
    private int getLongestTextLength(List<DataController> dataList) {
        int length = 0;
        for (Data d : dataList) {
            int len = d.getUnicodeLabel().length();
            if (len > length) {
                length = len;
            }
        }
        return length;
    }
}