/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;
import com.agrologic.app.util.Windows;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 *
 * @author Administrator
 */
public class SecondScreenPanel extends JPanel implements ScreenUI {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final int SCROLL_UNIT_INCREMENT = 32;
    private MainScreenPanel mainScreenPanel;
    private DatabaseManager dbManager;
    private Controller controller;
    private TreeMap<Screen, TreeMap<Table, List<DataController>>> screenTableDataMap;
    private Timer timerDB;

    /**
     * Creates new form SecondPanel
     */
    public SecondScreenPanel(DatabaseManager dbManager, Controller controller) {
        initComponents();
        setVisible(false);
        this.dbManager = dbManager;
        this.controller = controller;
        this.lblTitle.setText("<html>" + controller.getTitle() + "</html>");
    }

    public void startTimerThread() {
        timerDB = new javax.swing.Timer(REFRESH_RATE, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeUpdate();
            }
        });
        timerDB.start();
    }

    public void stopTimerThread() {
        timerDB.stop();
        timerDB = null;
    }

    public void removeLoadedControllerData() {
        screenTableDataMap = null;
        Component[] comps = tabsPane.getComponents();
        for (Component c : comps) {
            tabsPane.remove(c);
        }
        System.gc();
    }

    public void initLoadedControllerData() {
        DatabaseAccessor dbaccess = dbManager.getDatabaseGeneralService();
        if(screenTableDataMap != null) {
            return;
        }
        screenTableDataMap = new TreeMap<Screen, TreeMap<Table, List<DataController>>>();

        Program program = controller.getProgram();
        for (Screen screen : program.getScreens()) {
            if (!skipScreen(screen)) {
                List<Table> tableList = screen.getTables();
                if (tableList != null) {
                    TreeMap<Table, List<DataController>> tableDataMap = new TreeMap<Table, List<DataController>>();
                    try {
                        for (Table table : tableList) {
                            List<DataController> dataControllerList = new ArrayList<DataController>();
                            List<Data> dataList = table.getDataList();
                            for (Data d : dataList) {
                                DataController newData = new DataController(d);
                                if (program.getProgramRelays() == null) {
                                    try {
                                        List<ProgramRelay> programRelays = (List<ProgramRelay>) dbaccess.getRelayDao().getSelectedProgramRelays(program.getId(), dbManager.getDatabaseLoader().getLangId());
                                        program.setProgramRelays(programRelays);
                                        List<ProgramAlarm> programAlarms = (List<ProgramAlarm>) dbaccess.getAlarmDao().getSelectedProgramAlarms(program.getId(), dbManager.getDatabaseLoader().getLangId());
                                        program.setProgramAlarms(programAlarms);
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                if (newData.getIsRelay()) {
                                    try {
                                        newData.setProgramRelays(program.getProgramRelays());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (newData.isSystemState()) {
                                    try {
                                        newData.setProgramSystemStates(program.getProgramSystemStates());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                dataControllerList.add(newData);
                            }
                            tableDataMap.put(table, dataControllerList);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    screenTableDataMap.put(screen, tableDataMap);
                }
            }
        }
    }

    public void initScreenComponents() {
        int heightt = 0;
        int widthh = 0;
        ListIterator<Screen> listIter = removeUnusedScreen();
        moveBackwards(listIter);
        while (listIter.hasNext()) {
            Screen screen = listIter.next();
            int tableCount = 0;
            int x = 0;
            int y = 0;
            int maxHeight = 0;
            int maxWidth = 0;
            JPanel screenPanel = new JPanel(null);
            if (screen.getTitle().equals("Graphs")) {
                screenPanel = new Graphs24HourPanel(controller.getId());
                screenPanel.setPreferredSize(new Dimension(screenPanel.getWidth(), screenPanel.getHeight()));
                JScrollPane scrollPane = new JScrollPane(screenPanel);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
                tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", scrollPane);
                Dimension dim = Windows.screenResolution();
                tabsPane.setSize(dim.width - 10, dim.height - 140);
            } else {
                TreeMap<Table, List<DataController>> tableDataMap = screenTableDataMap.get(screen);
                try {
                    Iterator<Table> iterTable = tableDataMap.keySet().iterator();
                    while (iterTable.hasNext()) {
                        Table table = iterTable.next();
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
                        if (tableCount % COL_NUMBERS == 0) {
                            y += maxHeight;
                            x = 0;
                            maxHeight = 0;
                            maxWidth = 0;
                        } else {
                            x += rect.getSize().width;
                        }
                    }
                    heightt = y + maxHeight;
                    widthh = getMaxLength(screenPanel.getComponents()) * 4;
                    screenPanel.setPreferredSize(new Dimension(widthh, heightt));
                    JScrollPane scrollPane = new JScrollPane(screenPanel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
                    tabsPane.add("<html>" + screen.getUnicodeTitle() + "</html>", scrollPane);
                    Dimension dim = Windows.screenResolution();
                    tabsPane.setSize(dim.width - 10, dim.height - 160);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Dimension dim = Windows.screenResolution();
        setSize(dim.width, dim.height - 110);
    }

    public void serialize() {
        try {
            //use buffering
            OutputStream file = new FileOutputStream("screen.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(screenTableDataMap);
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deserialize() {
        //deserialize the quarks.ser file
        //note the use of abstract base class references

        try {
            //use buffering
            InputStream file = new FileInputStream("screen.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                //deserialize the List
                screenTableDataMap = (TreeMap<Screen, TreeMap<Table, List<DataController>>>) input.readObject();
            } finally {
                input.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
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
     * @return listIter that referenced on list of screen objects for second screen panel.
     */
    public ListIterator<Screen> removeUnusedScreen() {
        ListIterator<Screen> listIter = controller.getProgram().getScreens().listIterator();
        while (listIter.hasNext()) {
            if (skipScreen(listIter.next())) {
                listIter.remove();
            }
        }
        return listIter;
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

    private JPanel initTablePanel(final Table table, List<DataController> dataList, int x, int y) {
        JLabel tableTitle = new TableLabel("<html>" + table.getUnicodeTitle() + "</html>");
        JPanel pnlTitle = new JPanel(null);
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
     * Check if some of text box fields is in focus .
     *
     * @return true if text box field is in focus , otherwise false;
     */
    private boolean isTextBoxInFocus(int index) {
        JScrollPane pane = (JScrollPane) tabsPane.getComponent(index);
        JPanel screenPanel = (JPanel) pane.getViewport().getView();
        if (screenPanel.getComponentCount() == 0) {
            return false;
        }

        JPanel panel = (JPanel) screenPanel.getComponent(0);
        for (Component c : panel.getComponents()) {
            if (c instanceof DataPanel) {
                Component[] components = ((DataPanel) c).getComponents();
                for (int i = 0; i < components.length; i++) {
                    if (components[i] instanceof JTextField) {
                        JTextField tf = (JTextField) components[i];
                        synchronized (tf) {
                            if (tf.isFocusOwner()) {
                                System.out.println("Component in focus value : " + tf.getText());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getMaxLength(Component[] components) {
        if (components == null || components.length == 0) {
            return -1;
        }

        int maxLength = 0;
        for (Component c : components) {
            if (c instanceof JPanel) {
                int width = ((JPanel) c).getBounds().width;
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

        btnHouseTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/back.png"))); // NOI18N
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHouseTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTabbedPane tabsPane;
    // End of variables declaration//GEN-END:variables

    public synchronized void executeUpdate() {
        Runnable task = new SwingWorker() {

            DatabaseAccessor dbaccess = dbManager.getDatabaseGeneralService();
            List<Data> dl = null;

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    dl = (List<Data>) dbaccess.getDataDao()
                            .getControllerData(controller.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                //this in the other hand will always be executed on the EDT.
                //This has to be done in the EDT because currently JTableBinding
                //is not smart enough to realize that the notification comes in another
                //thread and do a SwingUtilities.invokeLater. So we are force to execute this
                // in the EDT. Seee http://markmail.org/thread/6ehh76zt27qc5fis and
                // https://beansbinding.dev.java.net/issues/show_bug.cgi?id=60
                if(screenTableDataMap == null) {
                    return;
                }

                Iterator<Screen> iterScreen = screenTableDataMap.keySet().iterator();
                while (iterScreen.hasNext()) {
                    Screen screen = iterScreen.next();
                    TreeMap<Table, List<DataController>> tableDataMap = screenTableDataMap.get(screen);
                    Iterator<Table> iterTable = tableDataMap.keySet().iterator();
                    while (iterTable.hasNext()) {
                        Table table = iterTable.next();
                        List<DataController> dataFacadeList = tableDataMap.get(table);
                        for (DataController df : dataFacadeList) {
                            Iterator<Data> iter = dl.iterator();
                            while (iter.hasNext()) {
                                Data data = iter.next();
                                if (df.getId().equals(data.getId())) {
                                    data.setFormat(df.getFormat());
                                    df.setValue(data.getValueToView());
                                }
                            }
                        }
                    }
                }
            }
        };
        executorService.execute(task);
    }
}