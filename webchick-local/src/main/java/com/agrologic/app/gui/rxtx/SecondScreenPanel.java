package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;
import com.agrologic.app.util.Windows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SecondScreenPanel extends JPanel implements ScreenUI {

    private static final int SCROLL_UNIT_INCREMENT = 32;
    private MainScreenPanel mainScreenPanel;
    private final DatabaseManager dbManager;
    private Controller controller;
    private Runnable task;
    private ScheduledExecutorService executor;
    private Dimension dim;
    private TreeMap<Screen, TreeMap<Table, List<DataController>>> screenTableDataMap;
    private static Logger logger = LoggerFactory.getLogger(SecondScreenPanel.class);

    public SecondScreenPanel(final DatabaseManager dbManager, final Controller controller) {
        initComponents();
        setVisible(false);
        this.dbManager = dbManager;
        this.controller = controller;
        this.lblTitle.setText("<html>" + controller.getTitle() + "</html>");
        this.dim = Windows.screenResolution();

        task = new Runnable() {

            DatabaseAccessor dbaccessor = dbManager.getDatabaseGeneralService();
            private Map<Long, Long> dataList = null;

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
                            List<DataController> dataFacadeList = tableDataMap.get(table);
                            for (DataController df : dataFacadeList) {
                                Set<Map.Entry<Long, Long>> entrySet = dataList.entrySet();
                                for (Map.Entry<Long, Long> entry : entrySet) {
                                    if (df.getId().equals(entry.getKey())) {
                                        df.setValue(entry.getValue());
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


    }

    public void startTimerThread() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    public void stopTimerThread() {
        executor.shutdown();
    }

    public void initLoadedControllerData() {
        logger.info("Initialization second screen ");

        screenTableDataMap = new TreeMap<Screen, TreeMap<Table, List<DataController>>>();
        Program program = controller.getProgram();
        for (Screen screen : program.getScreens()) {
            if (!skipScreen(screen)) {
                Collection<Table> tableList = screen.getTables();
                if (tableList != null) {
                    TreeMap<Table, List<DataController>> tableDataMap = new TreeMap<Table, List<DataController>>();
                    try {
                        for (Table table : tableList) {
                            List<DataController> dataControllerList = new ArrayList<DataController>();
                            initDataComponents(dbManager.getDatabaseGeneralService(), program, dataControllerList,
                                    table.getDataList());
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

    private void initDataComponents(DatabaseAccessor dbaccess, Program program, List<DataController> dataControllerList,
                                    Collection<Data> dataList) {
        for (Data d : dataList) {
            DataController newData = new DataController(d);
            if (program.getProgramRelays() == null) {
                try {
                    List<ProgramRelay> programRelays = dbaccess.getProgramRelayDao()
                            .getAllProgramRelays(program.getId(), dbManager.getDatabaseLoader().getLangId());
                    program.setProgramRelays(programRelays);
                    List<ProgramAlarm> programAlarms = dbaccess.getProgramAlarmDao()
                            .getAllProgramAlarms(program.getId(), dbManager.getDatabaseLoader().getLangId());
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
    }

    public void initScreenComponents() {

        ListIterator<Screen> listIter = removeUnusedScreen();
        moveBackwards(listIter);
        while (listIter.hasNext()) {
            Screen screen = listIter.next();
            JPanel screenPanel = new JPanel(null);
            if (screen.getTitle().equals("Graphs")) {
                logger.info("Initialization screen with graphs {} ", screen);
                screenPanel = new Graphs24HourPanel(controller.getId());
                int w = screenPanel.getWidth();
                int h = screenPanel.getHeight();
                screenPanel.setPreferredSize(new Dimension(w, h));
                addScreenPanelToTabsPane(screen, screenPanel);
            } else {
                logger.info("Initialization screen without graphs {} ", screen);
                TreeMap<Table, List<DataController>> tableDataMap = screenTableDataMap.get(screen);
                try {
                    int tableCount = 0;
                    int x = 0;
                    int y = 0;
                    int maxHeight = 0;
                    int maxWidth = 0;
                    Iterator<Table> iterator = tableDataMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        Table table = iterator.next();
                        List<DataController> dataControllerList = tableDataMap.get(table);
                        JPanel tablePanel = initTablePanel(table, dataControllerList, x, y);
                        tablePanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                        screenPanel.add(tablePanel);
                        Rectangle rect = tablePanel.getBounds();
                        if (maxHeight < rect.height) {
                            maxHeight = rect.height;
                        }
                        if (maxWidth < rect.width) {
                            maxWidth = rect.width;
                        }
                        tableCount++;
                        if (tableCount % OTHER_SCREEN_COL_NUMBERS == 0) {
                            y += maxHeight;
                            x = 0;
                            maxHeight = 0;
                            maxWidth = 0;
                        } else {
                            x += rect.getSize().width;
                        }
                    }
                    int height = y + maxHeight;
                    int width = getMaxLength(screenPanel.getComponents()) * 4;
                    screenPanel.setPreferredSize(new Dimension(width, height));
                    addScreenPanelToTabsPane(screen, screenPanel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        setSize(dim.width, dim.height - 110);
    }

    /**
     * Add screen panel that was created to tabs pane with scroll pane .
     *
     * @param screen      the screen with data
     * @param screenPanel the screen panel
     */
    private void addScreenPanelToTabsPane(Screen screen, JPanel screenPanel) {
        JScrollPane scrollPane = new JScrollPane(screenPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
        tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", scrollPane);
        tabsPane.setSize(dim.width - 10, dim.height - 160);
    }

    private JPanel initTablePanel(final Table table, List<DataController> dataList, int x, int y) {
        JPanel pnlTitle = new JPanel(null);
        JLabel tableTitle = new TableLabel("<html>" + table.getUnicodeTitle() + "</html>");
        pnlTitle.add(tableTitle);
        pnlTitle.setBackground(Color.LIGHT_GRAY);

        DataPanel dataPanel = new DataPanel(dataList, controller, dbManager.getDatabaseGeneralService());
        dataPanel.setProgramAlarms(controller.getProgram().getProgramAlarms());
        dataPanel.setProgramRelays(controller.getProgram().getProgramRelays());
        dataPanel.initComponents();
        int width = dataPanel.getWidth();
        int height = 20;

        tableTitle.setBounds(DataComponent.X_OFFSET, DataComponent.Y_OFFSET, width - 5, height);
        pnlTitle.setBounds(DataComponent.X_OFFSET, DataComponent.Y_OFFSET, width, height + 5);

        JPanel tablePanel = new JPanel(null);
        tablePanel.add(pnlTitle);
        tablePanel.add(dataPanel);
        tablePanel.setBounds(x, y, dataPanel.getWidth() + 5, dataPanel.getHeight() + pnlTitle.getHeight() + 10);
        return tablePanel;
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

    public void setMainScreenPanel(MainScreenPanel mainScreenPanel) {
        this.mainScreenPanel = mainScreenPanel;
    }

    private boolean skipScreen(Screen screen) {
        if (screen == null) {
            return false;
        }
        if (screen.getTitle().equals("Main")
                //                || screen.getTitle().equals("Graphs")
                || screen.getTitle().equals("Action Buttons")) {
            return true;
        } else {
            return false;
        }
    }

    private int getMaxLength(Component[] components) {
        if (components == null || components.length == 0) {
            return -1;
        }

        int maxLength = 0;
        for (Component component : components) {
            if (component instanceof JPanel) {
                int width = component.getBounds().width;
                if (maxLength < width) {
                    maxLength = width;
                }
            }
        }
        return maxLength;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        tabsPane = new javax.swing.JTabbedPane();
        btnHouseTitle = new javax.swing.JButton();

        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 0, 255));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setLabelFor(lblTitle);
        lblTitle.setAlignmentX(0.5F);
        add(lblTitle);
        lblTitle.setBounds(83, 0, 210, 33);
        add(tabsPane);
        tabsPane.setBounds(0, 45, 300, 380);

        btnHouseTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.png"))); // NOI18N
        btnHouseTitle.setText("BACK");
        btnHouseTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showMainScreenPanel(evt);
            }
        });
        add(btnHouseTitle);
        btnHouseTitle.setBounds(0, 0, 79, 33);
    }// </editor-fold>//GEN-END:initComponents

    private void showMainScreenPanel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showMainScreenPanel
        removeLoadedControllerData();
        stopTimerThread();
        mainScreenPanel.showMainScreen();
    }//GEN-LAST:event_showMainScreenPanel

    public void removeLoadedControllerData() {
        screenTableDataMap = null;
        Component[] comps = tabsPane.getComponents();
        for (Component c : comps) {
            tabsPane.remove(c);
        }
        System.gc();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHouseTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTabbedPane tabsPane;
    // End of variables declaration//GEN-END:variables
}