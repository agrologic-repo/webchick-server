package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainScreenPanel extends JPanel implements ScreenUI {
    private static Logger logger = LoggerFactory.getLogger(MainScreenPanel.class);
    private static int maxComponentCounter = 0;
    private int componentCounter = 0;
    private JButton btnHouse;
    private GridBagConstraints gridBagConstraints;
    private DataComponent prevComponent;
    private JScrollPane firstScrollPane;
    private JPanel secondMainPanel;
    private JScrollPane secondScrollPane;
    private ApplicationLocal parent;
    private SecondScreenPanel secondScreenPanel;
    private ComponentOrientation currentOrientation;

    private Controller controller;
    private List<Data> controllerData;
    private List<ProgramAlarm> programAlarms;
    private List<ProgramRelay> programRelays;
    private List<ProgramSystemState> programSystemStates;
    private List<DataController> controllerDataList;

    private DatabaseManager dbManager;
    private ScheduledExecutorService executor;
    private BufferedImage image;

    /**
     * Creates new form MainScreenPanel
     */
    public MainScreenPanel(final DatabaseManager dbManager, final Controller controller,
                           ComponentOrientation orientation) {
        this.currentOrientation = orientation;
        this.dbManager = dbManager;
        this.controller = controller;
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.setComponentOrientation(currentOrientation);

        loadControllerData();
        loadAndInitControllerData();
        initScreenComponents();
        try {
            image = ImageIO.read(getClass().getResource("/images/alarm.gif"));
        } catch (IOException e) {
            logger.error("Cannot load icon alarm.gif");
        }

        Runnable task = new Runnable() {

            private DatabaseAccessor dbaccessor;
            private Map<Long, Long> dataList = null;

            @Override
            public void run() {
                try {
                    try {
                        dbaccessor = dbManager.getDatabaseGeneralService();
                        dataList = dbaccessor.getDataDao().getUpdatedControllerDataValues(controller.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (dataList == null) {
                        return;
                    }

                    for (DataController df : controllerDataList) {
                        Set<Map.Entry<Long, Long>> entrySet = dataList.entrySet();
                        for (Map.Entry<Long, Long> entry : entrySet) {
                            if (df.getId().equals(entry.getKey())) {
                                df.setValue(entry.getValue());
                                // check alarm data
                                if (entry.getKey().compareTo(Long.valueOf(3154)) == 0) {
                                    int val = (entry.getValue().intValue());
                                    if (val > 0) {
                                        try {
                                            btnHouse.setIcon(new ImageIcon(image));
                                        } catch (Exception e) {
                                            logger.error("Cannot load alarm.gif");
                                        }
                                    } else {
                                        btnHouse.setIcon(null);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        };

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    public void loadControllerData() {
        controllerData = (List<Data>) controller.getProgram().getScreenById(1L).getTableById(1L).getDataList();
        programAlarms = controller.getProgram().getProgramAlarms();
        programRelays = controller.getProgram().getProgramRelays();
        programSystemStates = controller.getProgram().getProgramSystemStates();
    }

    @Override
    public void loadAndInitControllerData() {
        controllerDataList = new ArrayList<DataController>();
        for (Data d : controllerData) {
            DataController newData = new DataController(d);
            if (newData.getIsRelay()) {
                newData.setProgramRelays(controller.getProgram().getProgramRelays());
            }
            if (newData.isSystemState()) {
                newData.setProgramSystemStates(controller.getProgram().getProgramSystemStates());
            }
            controllerDataList.add(newData);
        }
    }

    @Override
    public void initScreenComponents() {
        gridBagConstraints = createGridBagConstraint();
        btnHouse = new JButton("<html>" + controller.getTitle() + "</html>");
        btnHouse.setToolTipText("<html>" + controller.getProgram().getName() + "</html>");

        btnHouse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                firstScrollPane.setVisible(false);
                secondMainPanel = new JPanel(new BorderLayout());
                secondScreenPanel = new SecondScreenPanel(dbManager, controller, getComponentOrientation());
                secondScreenPanel.setFirstScrollPane(firstScrollPane);
                secondScreenPanel.setParent(parent);
                secondScreenPanel.setMainScreenPanel(MainScreenPanel.this);
                secondMainPanel.add(secondScreenPanel);
                secondScrollPane = new JScrollPane(secondMainPanel);
                secondScreenPanel.setSecondScrollPane(secondScrollPane);
                secondScrollPane.setAutoscrolls(true);
                secondScrollPane.getVerticalScrollBar().setUnitIncrement(32);
                int max = secondScrollPane.getHorizontalScrollBar().getMaximum();
                secondScrollPane.getHorizontalScrollBar().setValue(max);
                secondScreenPanel.startTimerThread();
                parent.getContentPane().add(secondScrollPane);
                parent.validate();
                parent.repaint();
            }
        });


        add(btnHouse, gridBagConstraints);
        gridBagConstraints.gridwidth = 1;

        try {
            for (int i = 0; i < controllerDataList.size(); i++) {
                DataController data = controllerDataList.get(i);
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
                                dataComponent = ComponentFactory.createRelayComponent(data, programRelay, getComponentOrientation());
                                componentCounter++;
                                gridBagConstraints.gridy++;
                                gridBagConstraints.gridx = 0;
                                add(dataComponent.getLabel(), gridBagConstraints);
                                gridBagConstraints.gridx = 1;
                                add(dataComponent.getComponent(), gridBagConstraints);
                            }
                        }
                    }
                } else if (data.isAlarm()) {
                    dataComponent = ComponentFactory.createAlarmComponent(data, programAlarms, getComponentOrientation());
                    componentCounter++;
                    gridBagConstraints.gridy++;
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getLabel(), gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    add(dataComponent.getComponent(), gridBagConstraints);
                } else if (data.isSystemState()) {
                    dataComponent = ComponentFactory.createSystemStateComponent(data, programSystemStates, getComponentOrientation());
                    componentCounter++;
                    gridBagConstraints.gridy++;
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getLabel(), gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    add(dataComponent.getComponent(), gridBagConstraints);
                } else {
                    dataComponent = ComponentFactory.createDataComponent(data, controller, dbManager.getDatabaseGeneralService(), getComponentOrientation());
                    componentCounter++;
                    gridBagConstraints.gridy++;
                    gridBagConstraints.gridx = 0;
                    add(dataComponent.getLabel(), gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    add(dataComponent.getComponent(), gridBagConstraints);
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

    /**
     *
     */
    public void fillEmptyComponents() {
        while (componentCounter < maxComponentCounter) {
            JTextField text = new JTextField("");
            text.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
            text.setEditable(false);
            text.setComponentOrientation(currentOrientation);
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            add(text, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(text, gridBagConstraints);
            componentCounter++;
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

    private void calculateMaxComponentCounter(int componentCounter) {
        if (maxComponentCounter < componentCounter) {
            maxComponentCounter = componentCounter;
        }
    }

    /**
     * @param firstScrollPane
     */
    public void setFirstScrollPane(JScrollPane firstScrollPane) {
        this.firstScrollPane = firstScrollPane;
    }

    public void destroySecondScreen() {
        this.secondScreenPanel = null;
        this.secondMainPanel = null;
        this.secondScrollPane = null;

    }

    /**
     * @param parent
     */
    public void setParent(ApplicationLocal parent) {
        this.parent = parent;
    }
}

