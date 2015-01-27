package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Valery on 18/05/14.
 */
public class DataComponentPanel extends JPanel {
    private static int maxComponentCounter = 0;
    private int componentCounter = 0;
    private Controller controller;
    private Table table;
    private DatabaseAccessor dbaccess;
    private GridBagConstraints gridBagConstraints;
    private DataComponent prevComponent;
    private List<ProgramAlarm> programAlarms;
    private List<ProgramRelay> programRelays;
    private List<ProgramSystemState> programSystemStates;
    private List<DataController> dataControllerList;

    /**
     * @param table
     * @param dataControllerList
     */
    public DataComponentPanel(Controller controller, Program program, List<ProgramAlarm> programAlarms,
                              List<ProgramRelay> programRelays, List<ProgramSystemState> programSystemStates,
                              DatabaseAccessor dbaccess, ComponentOrientation componentOrientation,
                              Table table, List<DataController> dataControllerList) {
        this(controller, program, programAlarms,
                programRelays, programSystemStates,
                dbaccess, componentOrientation,
                table, dataControllerList, null);
    }

    /**
     * @param table
     * @param dataControllerList
     */
    public DataComponentPanel(Controller controller, Program program, List<ProgramAlarm> programAlarms,
                              List<ProgramRelay> programRelays, List<ProgramSystemState> programSystemStates,
                              DatabaseAccessor dbaccess, ComponentOrientation componentOrientation,
                              Table table, List<DataController> dataControllerList, DataComponent prevComponent) {

        super(new GridBagLayout());
        this.setComponentOrientation(componentOrientation);
        this.prevComponent = prevComponent;
        this.controller = controller;
        this.dbaccess = dbaccess;
        this.programRelays = programRelays;
        this.programAlarms = programAlarms;
        this.programSystemStates = programSystemStates;
        this.controller = controller;
        this.table = table;
        this.dataControllerList = dataControllerList;
        intiComponents();
    }

    private void intiComponents() {
        setComponentOrientation(getComponentOrientation());
        gridBagConstraints = createGridBagConstraint();

        JLabel tableTitle = new TableLabel("<html>" + table.getUnicodeTitle() + "</html>");
        tableTitle.setOpaque(true);
        tableTitle.setBackground(Color.lightGray);
        add(tableTitle, gridBagConstraints);
        gridBagConstraints.gridwidth = 1;

        try {
            for (int i = 0; i < dataControllerList.size(); i++) {
                DataController data = dataControllerList.get(i);
                DataComponent dataComponent;
                if (data.getIsRelay() == true) {
                    for (ProgramRelay programRelay : programRelays) {
                        if (programRelay.getDataId().equals(data.getId())) {
                            // skip relay number equals 0
                            if (programRelay.getRelayNumber() != 0) {
                                if (data.getValue() == null || data.getValue() == -1) {
                                    programRelay.setOff();
                                } else {
                                    programRelay.init(data.getValueToUI());
                                }
                                dataComponent = ComponentFactory.createRelayComponent(data, getComponentOrientation(),
                                        programRelay);
                                gridBagConstraints.gridy++;
                                gridBagConstraints.gridx = 0;
                                add(dataComponent.getLabel(), gridBagConstraints);
                                gridBagConstraints.gridx = 1;
                                add(dataComponent.getComponent(), gridBagConstraints);
                                componentCounter++;
                            }
                        }
                    }
                } else if (data.isAlarm()) {
                    dataComponent = ComponentFactory.createAlarmComponent(data, getComponentOrientation(), programAlarms);
                    gridBagConstraints.gridy++;
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getLabel(), gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    add(dataComponent.getComponent(), gridBagConstraints);
                    componentCounter++;
                } else if (data.isSystemState()) {
                    dataComponent = ComponentFactory.createSystemStateComponent(data, getComponentOrientation(), programSystemStates);
                    gridBagConstraints.gridy++;
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getLabel(), gridBagConstraints);
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getComponent(), gridBagConstraints);
                    componentCounter++;
                } else {
                    dataComponent = ComponentFactory.createDataComponent(data, getComponentOrientation(), controller, dbaccess);
                    gridBagConstraints.gridy++;
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getLabel(), gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    gridBagConstraints.ipadx = 2;
                    add(dataComponent.getComponent(), gridBagConstraints);
                    componentCounter++;

                    if (prevComponent == null) {
                        prevComponent = dataComponent;
                    } else {
                        if (dataComponent.getComponent() instanceof DataTextField) {
                            if (prevComponent.getComponent() instanceof DataTextField) {
                                ((DataTextField) prevComponent.getComponent()).setNextTextField((DataTextField) dataComponent.getComponent());
                                prevComponent = dataComponent;
                            } else if (prevComponent.getComponent() instanceof DataPasswordField) {
                                ((DataPasswordField) prevComponent.getComponent()).setNextTextField((DataPasswordField) dataComponent.getComponent());
                                prevComponent = dataComponent;
                            } else {
                                prevComponent = dataComponent;
                            }
                        }
                        if (dataComponent.getComponent() instanceof DataPasswordField) {
                            if (prevComponent.getComponent() instanceof DataPasswordField) {
                                ((DataPasswordField) prevComponent.getComponent()).setNextTextField((DataPasswordField) dataComponent.getComponent());
                                prevComponent = dataComponent;
                            } else if (prevComponent.getComponent() instanceof DataTextField) {
                                ((DataTextField) prevComponent.getComponent()).setNextTextField((DataTextField) dataComponent.getComponent());
                                prevComponent = dataComponent;
                            } else {
                                prevComponent = dataComponent;
                            }
                        }
                    }

                }
            }
            calculateMaxComponentCounter(componentCounter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GridBagConstraints createGridBagConstraint() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.insets = new Insets(1, 2, 1, 2);  //top padding
        return c;
    }

    public void fillEmptyComponents() {
        while (componentCounter < maxComponentCounter) {
            JTextField text = new JTextField("");
            text.setOpaque(false);
            text.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            text.setEditable(false);
            text.setComponentOrientation(getComponentOrientation());
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            add(text, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(text, gridBagConstraints);
            componentCounter++;
        }
    }

    public void resetMaxComponentCounter() {
        maxComponentCounter = 0;
    }

    private void calculateMaxComponentCounter(int componentCounter) {
        if (maxComponentCounter < componentCounter) {
            maxComponentCounter = componentCounter;
        }
    }


    public DataComponent getPrevComponent() {
        return prevComponent;
    }
}
