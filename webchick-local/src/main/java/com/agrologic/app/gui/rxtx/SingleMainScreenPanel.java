package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Valery on 10/06/14
 */
public class SingleMainScreenPanel extends JPanel implements ScreenPanelUI {
    private static final int SCROLL_UNIT_INCREMENT = 32;
    private static Logger logger = LoggerFactory.getLogger(SecondScreenPanel.class);
    private static int maxComponentCounter = 0;
    private final DatabaseManager dbManager;
    private int componentCounter = 0;
    private ApplicationLocal parent;
    private JLabel lblTitle;
    private GridBagConstraints gridBagConstraints;
    private JButton button;
    private JPanel topPanel;
    private JPanel mainContentPanel;
    private JTabbedPane tabsPane;
    private JScrollPane firstScrollPane;
    private JScrollPane secondScrollPane;
    private MainScreenPanel mainScreenPanel;
    private Controller controller;
    private Program program;
    private java.util.List<ProgramAlarm> programAlarms;
    private java.util.List<ProgramRelay> programRelays;
    private java.util.List<ProgramSystemState> programSystemStates;
    private Runnable task;
    private ScheduledExecutorService executor;
    private TreeMap<Screen, TreeMap<Table, java.util.List<DataController>>> screenTableDataMap;

    public SingleMainScreenPanel(final DatabaseManager dbManager, final Controller controller,
                                 final ComponentOrientation componentOrientation) {
        super(new BorderLayout());
        this.dbManager = dbManager;
        this.controller = controller;
        this.setComponentOrientation(componentOrientation);
        this.lblTitle = createHouseNameLabel();
        this.topPanel = new JPanel(new BorderLayout());
        this.topPanel.setComponentOrientation(getComponentOrientation());
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
     * @param firstScrollPane
     */
    public void setFirstScrollPane(JScrollPane firstScrollPane) {
        this.firstScrollPane = firstScrollPane;
    }

    /**
     * @param parent
     */
    public void setParent(ApplicationLocal parent) {
        this.parent = parent;
    }

    /**
     * Start data updator task
     */
    public void startTimerThread() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Stop data updator
     */
    public void stopTimerThread() {
        executor.shutdown();
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

        screenTableDataMap = new TreeMap<Screen, TreeMap<Table, java.util.List<DataController>>>();

        Program program = controller.getProgram();

        for (Screen screen : program.getScreens()) {
            if (!skipScreen(screen)) {
                Collection<Table> tableList = screen.getTables();
                if (tableList != null) {
                    TreeMap<Table, java.util.List<DataController>> tableDataMap = new TreeMap<Table, java.util.List<DataController>>();
                    try {
                        for (Table table : tableList) {
                            java.util.List<DataController> dataControllerList = new ArrayList<DataController>();
                            for (Data d : table.getDataList()) {
                                DataController newData = new DataController(d);
                                if (program.getProgramRelays() == null) {
                                    try {
                                        java.util.List<ProgramRelay> programRelays = dbaccess.getProgramRelayDao().getAllProgramRelays(program.getId(), dbManager.getDatabaseLoader().getLangId());
                                        program.setProgramRelays(programRelays);
                                        java.util.List<ProgramAlarm> programAlarms = dbaccess.getProgramAlarmDao().getAllProgramAlarms(program.getId(), dbManager.getDatabaseLoader().getLangId());
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
            JPanel screenPanel = new JPanel();
            screenPanel.setComponentOrientation(getComponentOrientation());
            if (screen.getTitle().equals("Graphs")) {
                logger.info("Initialization screen with graphs {} ", screen);
                JPanel graphPanel = new JPanel(new FlowLayout());
                graphPanel.add(new Graphs24HourPanel(controller.getId()));
                screenPanel.add(graphPanel);
                tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", screenPanel);
            } else {
                logger.info("Initialization screen without graphs {} ", screen);
                TreeMap<Table, java.util.List<DataController>> tableDataMap = screenTableDataMap.get(screen);
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
                        java.util.List<DataController> dataControllerList = tableDataMap.get(table);
                        dataComponentPanels[counter] = new DataComponentPanel(controller, program, programAlarms,
                                programRelays, programSystemStates, dbManager.getDatabaseGeneralService(),
                                getComponentOrientation(), table, dataControllerList);
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
                    TreeMap<Table, java.util.List<DataController>> tableDataMap = screenTableDataMap.get(screen);
                    Iterator<Table> tableIterator = tableDataMap.keySet().iterator();
                    while (tableIterator.hasNext()) {
                        Table table = tableIterator.next();
                        java.util.List<DataController> dataControllerList = tableDataMap.get(table);
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
