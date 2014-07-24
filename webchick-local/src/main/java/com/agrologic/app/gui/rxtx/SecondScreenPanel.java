package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.i18n.LocaleManager;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SecondScreenPanel extends JPanel implements ScreenPanelUI {
    private static Logger logger = LoggerFactory.getLogger(SecondScreenPanel.class);
    private final DatabaseManager dbManager;
    private ApplicationLocal parent;
    private JLabel lblTitle;
    private JButton button;
    private JPanel topPanel;
    private JPanel mainContentPanel;
    private JTabbedPane tabsPane;
    private JScrollPane firstScrollPane;
    private JScrollPane secondScrollPane;
    private MainScreenPanel mainScreenPanel;
    private Controller controller;
    private Program program;
    private List<ProgramAlarm> programAlarms;
    private List<ProgramRelay> programRelays;
    private List<ProgramSystemState> programSystemStates;
    private Runnable task;
    private ScheduledExecutorService executor;
    private ScheduledFuture<?> future;
    private TreeMap<Screen, TreeMap<Table, List<DataController>>> screenTableDataMap;

    public SecondScreenPanel(final DatabaseManager dbManager, final Controller controller,
                             final ComponentOrientation componentOrientation) {
        super(new BorderLayout());
        this.dbManager = dbManager;
        this.controller = controller;
        this.setComponentOrientation(componentOrientation);
        this.button = createBackButton();
        this.lblTitle = createHouseNameLabel();
        this.topPanel = new JPanel(new BorderLayout());
        this.topPanel.setComponentOrientation(getComponentOrientation());
        this.topPanel.add(button, BorderLayout.LINE_START);
        this.topPanel.add(lblTitle, BorderLayout.CENTER);
        this.tabsPane = new JTabbedPane();
        this.tabsPane.setComponentOrientation(getComponentOrientation());
        this.mainContentPanel = new JPanel(new BorderLayout());
        this.mainContentPanel.add(tabsPane);
        this.mainContentPanel.setComponentOrientation(getComponentOrientation());

        this.loadAndInitControllerData();
        this.initScreenComponents();

        add(topPanel, BorderLayout.PAGE_START);
        add(mainContentPanel, BorderLayout.CENTER);
        setComponentOrientation(getComponentOrientation());

        task = new DataUpdator(dbManager.getDatabaseGeneralService());
        startTimerThread();
    }

    /**
     * Return button that shows the main screen
     */
    private JButton createBackButton() {
        ResourceBundle bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // NOI18N

        JButton buttonTemp = new JButton(bundle.getString("button.back.to.main"));
        if (getComponentOrientation().equals(ApplicationLocal.orientationLTR)) {
            buttonTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backltr.png"))); // NOI18N
        } else {
            buttonTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backrtl.png"))); // NOI18N
        }
        buttonTemp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tabsPane.removeAll();
                mainContentPanel.remove(tabsPane);
                remove(topPanel);
                remove(mainContentPanel);
                showFirstScrollPane();
                stopTimerThread();
                mainScreenPanel.destroySecondScreen();

                repaint();
                invalidate();
            }
        });
        buttonTemp.setComponentOrientation(getComponentOrientation());
        return buttonTemp;
    }

    /**
     * @return
     */
    private JLabel createHouseNameLabel() {
        JLabel label = new JLabel("<html>" + controller.getTitle() + "</html>");
        label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label.setForeground(new java.awt.Color(0, 0, 255));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setLabelFor(lblTitle);
        label.setAlignmentX(0.5F);
        return label;
    }

    /**
     *
     */
    private void showFirstScrollPane() {
        secondScrollPane.setVisible(false);
        secondScrollPane.setViewport(null);
        secondScrollPane = null;
        firstScrollPane.setVisible(true);

//        for (Component c : parent.getContentPane().getComponents()) {
//            if (c instanceof SecondScreenPanel) {
//
//            }
//        }
        parent.getContentPane().add(firstScrollPane);
        parent.validate();
        parent.repaint();
        parent.revalidate();
        parent.repaint();
    }

    /**
     * @param firstScrollPane
     */
    public void setFirstScrollPane(JScrollPane firstScrollPane) {
        this.firstScrollPane = firstScrollPane;
    }

    public JScrollPane getSecondScrollPane() {
        return secondScrollPane;
    }

    public void setSecondScrollPane(JScrollPane secondScrollPane) {
        this.secondScrollPane = secondScrollPane;
    }

    /**
     * @param parent
     */
    public void setParent(ApplicationLocal parent) {
        this.parent = parent;
    }

    public void setMainScreenPanel(MainScreenPanel mainScreenPanel) {
        this.mainScreenPanel = mainScreenPanel;
    }

    /**
     * Start data updater task
     */
    public void startTimerThread() {
        executor = Executors.newSingleThreadScheduledExecutor();
        future = executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Stop data updater task
     */
    public void stopTimerThread() {
        future.cancel(true);
        while (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     *
     */
    public void loadAndInitControllerData() {
        logger.info("Initialization second screen ");
        program = controller.getProgram();
        DatabaseAccessor dbaccess = dbManager.getDatabaseGeneralService();
        try {
            programRelays = dbaccess.getProgramRelayDao().getAllProgramRelays(program.getId(), dbManager.getDatabaseLoader().getLangId());
            program.setProgramRelays(programRelays);
            programAlarms = dbaccess.getProgramAlarmDao().getAllProgramAlarms(program.getId(), dbManager.getDatabaseLoader().getLangId());
            program.setProgramAlarms(programAlarms);
            programSystemStates = dbaccess.getProgramSystemStateDao().getAllProgramSystemStates(program.getId(), dbManager.getDatabaseLoader().getLangId());


        } catch (SQLException ex) {
            logger.debug("Loading data error .", ex);
        }

        screenTableDataMap = new TreeMap<Screen, TreeMap<Table, List<DataController>>>();

        Program program = controller.getProgram();

        for (Screen screen : program.getScreens()) {
            if (!skipScreen(screen)) {
                logger.debug("screen title : {} ", screen);
                Collection<Table> tableList = screen.getTables();
                if (tableList != null) {
                    TreeMap<Table, List<DataController>> tableDataMap = new TreeMap<Table, List<DataController>>();
                    try {
                        for (Table table : tableList) {
                            List<DataController> dataControllerList = new ArrayList<DataController>();
                            for (Data d : table.getDataList()) {
                                DataController newData = new DataController(d);
                                if (program.getProgramRelays() == null) {
                                    try {
                                        List<ProgramRelay> programRelays = dbaccess.getProgramRelayDao().getAllProgramRelays(program.getId(), dbManager.getDatabaseLoader().getLangId());
                                        program.setProgramRelays(programRelays);
                                        List<ProgramAlarm> programAlarms = dbaccess.getProgramAlarmDao().getAllProgramAlarms(program.getId(), dbManager.getDatabaseLoader().getLangId());
                                        program.setProgramAlarms(programAlarms);
                                    } catch (SQLException ex) {
                                        logger.debug("Error during initialization data components ", ex);
                                    }
                                }
                                if (newData.getIsRelay()) {
                                    try {
                                        newData.setProgramRelays(program.getProgramRelays());
                                    } catch (Exception e) {
                                        logger.debug("Cannot set program relays ", e);
                                    }
                                }
                                if (newData.isSystemState()) {
                                    try {
                                        newData.setProgramSystemStates(program.getProgramSystemStates());
                                    } catch (Exception e) {
                                        logger.debug("Cannot set program system state ", e);
                                    }
                                }
                                dataControllerList.add(newData);
                            }
                            tableDataMap.put(table, dataControllerList);
                        }
                    } catch (NullPointerException e) {
                        logger.info("NPE", e);
                        e.printStackTrace();
                    }
                    screenTableDataMap.put(screen, tableDataMap);
                }
            }
        }
    }

    public void initScreenComponents() {
        ListIterator<Screen> listIterator = removeUnusedScreen();
        moveBackwards(listIterator);

        while (listIterator.hasNext()) {
            Screen screen = listIterator.next();
            JPanel screenPanel = new JPanel(new ModifiedFlowLayout(FlowLayout.CENTER));
            screenPanel.setComponentOrientation(getComponentOrientation());
            if (screen.getTitle().equals("Graphs")) {
                logger.info("Initialization screen with graphs {} ", screen);
                JPanel graphPanel = new JPanel(new FlowLayout());
                graphPanel.add(new Graphs24HourPanel(controller.getId()));
                screenPanel.add(graphPanel);
                tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", screenPanel);
            } else if (screen.getTitle().equals("Action Set Buttons")) {
                logger.info("Initialization screen with graphs {} ", screen);
                JPanel tableContentPanel = new JPanel(new FlowLayout());
                List<ProgramActionSet> programActionSets = (List<ProgramActionSet>) program.getProgramActionSet();
                for (ProgramActionSet pas : programActionSets) {
                    DataComponent component = ComponentFactory.createActionButtonComponent(pas, getComponentOrientation(), controller,
                            dbManager.getDatabaseGeneralService());
                    tableContentPanel.add(component.getComponent());
                }
                tableContentPanel.setComponentOrientation(getComponentOrientation());
                screenPanel.add(tableContentPanel);
                tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", screenPanel);
            } else {
                logger.info("Initialization screen without graphs {} ", screen);
                TreeMap<Table, List<DataController>> tableDataMap = screenTableDataMap.get(screen);
                try {
                    Iterator<Table> iterator = tableDataMap.keySet().iterator();
                    int rows = tableDataMap.size() / 4;
                    rows += (tableDataMap.size() % 4) > 0 ? 1 : 0;
                    int cols = 4;
                    JPanel tableContentPanel = new JPanel(new GridLayout(rows, cols));
                    tableContentPanel.setComponentOrientation(getComponentOrientation());
                    DataComponentPanel[] dataComponentPanels = new DataComponentPanel[tableDataMap.size()];
                    int counter = 0;
                    while (iterator.hasNext()) {
                        Table table = iterator.next();
                        List<DataController> dataControllerList = tableDataMap.get(table);
                        if (counter == 0) {
                            dataComponentPanels[counter] = new DataComponentPanel(controller, program, programAlarms,
                                    programRelays, programSystemStates, dbManager.getDatabaseGeneralService(),
                                    getComponentOrientation(), table, dataControllerList);
                        } else {
                            dataComponentPanels[counter] = new DataComponentPanel(controller, program, programAlarms,
                                    programRelays, programSystemStates, dbManager.getDatabaseGeneralService(),
                                    getComponentOrientation(), table, dataControllerList,
                                    dataComponentPanels[counter - 1].getPrevComponent());
                        }

                        dataComponentPanels[counter].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                        tableContentPanel.add(dataComponentPanels[counter]);
                        counter++;
                    }
                    screenPanel.add(tableContentPanel);

                    for (DataComponentPanel dataComponentPanel : dataComponentPanels) {
                        dataComponentPanel.fillEmptyComponents();
                    }
                    dataComponentPanels[0].resetMaxComponentCounter();

                    tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", screenPanel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Move iterator to beginning of list.
     *
     * @param listIter the list iterator
     */
    public void moveBackwards(ListIterator<Screen> listIter) {
        while (listIter.hasPrevious()) {
            listIter.previous();
        }
    }

    /**
     * Create ListIterator with screens that used in second screen panel
     *
     * @return listIterator that referenced on list of screen objects for second screen panel.
     */
    public ListIterator<Screen> removeUnusedScreen() {
        ListIterator<Screen> listIterator = controller.getProgram().getScreens().listIterator();
        while (listIterator.hasNext()) {
            if (skipScreen(listIterator.next())) {
                listIterator.remove();
            }
        }
        return listIterator;
    }

    /**
     * Return true if give screen have to skip
     *
     * @param screen the screen to check if it should skip
     * @return true if screen have to skip otherwise false
     */
    private boolean skipScreen(Screen screen) {
        if (screen == null) {
            return false;
        }
        if (screen.getTitle().equals("Main") || screen.getTitle().equals("Action Buttons")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    private class DataUpdator implements Runnable {

        private DatabaseAccessor dbaccessor;
        private Map<Long, Long> dataList;

        DataUpdator(DatabaseAccessor dbaccessor) {
            this.dbaccessor = dbaccessor;
        }

        @Override
        public void run() {
            try {
                try {
                    dataList = dbaccessor.getDataDao().getUpdatedControllerDataValues(controller.getId());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                if (screenTableDataMap == null) {
                    return;
                }

                Iterator<Screen> screenIterator = screenTableDataMap.keySet().iterator();
                while (screenIterator.hasNext()) {
                    Screen screen = screenIterator.next();
                    TreeMap<Table, List<DataController>> tableDataMap = screenTableDataMap.get(screen);
                    Iterator<Table> tableIterator = tableDataMap.keySet().iterator();
                    while (tableIterator.hasNext()) {
                        Table table = tableIterator.next();
                        List<DataController> dataControllerList = tableDataMap.get(table);
                        for (DataController dataController : dataControllerList) {
                            Set<Map.Entry<Long, Long>> entrySet = dataList.entrySet();
                            for (Map.Entry<Long, Long> entry : entrySet) {
                                if (dataController.getId().equals(entry.getKey())) {
                                    dataController.setValue(entry.getValue());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}