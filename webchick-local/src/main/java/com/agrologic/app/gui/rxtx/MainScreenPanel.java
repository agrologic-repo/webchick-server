package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.rxtx.DataController;
import com.agrologic.app.network.rxtx.SocketThread;
import com.agrologic.app.util.Windows;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainScreenPanel extends JPanel implements ScreenUI {

    public static final int BUTTON_HEIGHT = 30;
    public final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Dimension thePanelHolderSize = new Dimension();
    private DataPanel dataPanel;
    private JPanel holderPanel;
    private final JPanel me;
    private JScrollPane scrollPane;
    private SecondScreenPanel secondScreenPanel;
    private List<DataController> dataControllerList;
    private List<MainScreenPanel> otherMainScreens;
    private Controller controller;
    private DatabaseManager dbManager;
    private SocketThread nwt;
    private Timer timerDB;
    private static Logger logger = Logger.getLogger(MainScreenPanel.class);

    /**
     * Creates new form MainScreenPanell
     */
    public MainScreenPanel(DatabaseManager dbManager, Controller controller) {
        initComponents();
        this.dbManager = dbManager;
        this.me = this;
        this.controller = controller;
        btnHouse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((otherMainScreens != null) && (otherMainScreens.size() > 0)) {
                    for (MainScreenPanel msp : otherMainScreens) {
                        msp.setVisible(false);
                    }
                }
                me.setVisible(false);
                secondScreenPanel.initLoadedControllerData();
                secondScreenPanel.initScreenComponents();
                secondScreenPanel.startTimerThread();

                Rectangle rect = secondScreenPanel.getBounds();

                scrollPane.setBounds(rect.x, rect.y, rect.width, rect.height);
                holderPanel.setBounds(rect.x, rect.y, rect.width, rect.height);
                holderPanel.setPreferredSize(rect.getSize());
                setPreferredSize(secondScreenPanel.getSize());
                secondScreenPanel.setVisible(true);
            }
        });

        pnlHouse.setLayout(new BorderLayout(5, 5));
        pnlHouse.add(btnHouse);
        add(pnlHouse);

        initLoadedControllerData();
        initScreenComponents();

        timerDB = new javax.swing.Timer(ScreenUI.REFRESH_RATE, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                executeUpdate();
            }
        });
        timerDB.start();
    }

    @Override
    public void initLoadedControllerData() {
        long screenId = 1;  // main screen id
        long tableId = 1;  // main screen table id
        List<Data> dataList = controller.getProgram().getScreenById(screenId).getTableById(tableId).getDataList();
        dataControllerList = new ArrayList<DataController>();
        for (Data d : dataList) {
            DataController newData = new DataController(d);
            if (newData.getIsRelay()) {
                newData.setProgramRelays(controller.getProgram().getProgramRelays());
            }
            if (newData.isSystemState()) {
                newData.setProgramSystemStates(controller.getProgram().getProgramSystemStates());
            }
            dataControllerList.add(newData);
        }
    }

    @Override
    public void initScreenComponents() {
        btnHouse.setText("<html>" + controller.getTitle() + "</html>");
//        setUpdatedTime(btnHouse);
        btnHouse.setSize(btnHouse.getText().length() * 2, btnHouse.getHeight());
        btnHouse.setToolTipText("<html>" + controller.getProgram().getName() + "</html>");
        dataPanel = new DataPanel(dataControllerList, controller, dbManager.getDatabaseGeneralService());
        dataPanel.setProgramAlarms(controller.getProgram().getProgramAlarms());
        dataPanel.setProgramRelays(controller.getProgram().getProgramRelays());
        dataPanel.setProgramSystemStates(controller.getProgram().getProgramSystemStates());
        dataPanel.initComponents();

        int width = dataPanel.getWidth();
        int height = BUTTON_HEIGHT;
        btnHouse.setBounds(2, 2, width - 3, height - 3);
        pnlHouse.setBounds(2, 2, width, height);
        add(dataPanel);
        setBounds(0, 0, dataPanel.getWidth() + 5, dataPanel.getHeight() + pnlHouse.getHeight() + 5);
    }

    public void showMainScreen() {
        if ((otherMainScreens != null) && (otherMainScreens.size() > 0)) {
            for (MainScreenPanel msp : otherMainScreens) {
                msp.setVisible(true);
            }
        }

        me.setVisible(true);
        setBounds();
        secondScreenPanel.setVisible(false);
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void setHolderPanel(JPanel holderPanel) {
        this.holderPanel = holderPanel;
    }

    public void setHolderPanelSize(Dimension theSize) {
        thePanelHolderSize = theSize;
    }

    public void setBounds() {
        Dimension dimension = Windows.screenResolution();
        Rectangle rect = secondScreenPanel.getBounds();
        scrollPane.setBounds(rect.x, rect.y, dimension.width - 20, dimension.height - 110);
        holderPanel.setBounds(rect.x, rect.y, dimension.width - 20, dimension.height - 110);
        holderPanel.setPreferredSize(thePanelHolderSize);
    }

    public void addSecondPanel(SecondScreenPanel ssp) {
        this.secondScreenPanel = ssp;
    }

    public void addMainScreen(MainScreenPanel msp) {
        if (otherMainScreens == null) {
            otherMainScreens = new ArrayList<MainScreenPanel>();
        }
        otherMainScreens.add(msp);
    }

    @Override
    public void executeUpdate() {
        Runnable task = new SwingWorker() {

            private DatabaseAccessor dbaccessor;
            private List<Data> dl = null;

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    dbaccessor = dbManager.getDatabaseGeneralService();
                    dl = (List<Data>) dbaccessor.getDataDao().getControllerData(controller.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void done() {
                // this in the other hand will always be executed on the EDT.
                // This has to be done in the EDT because currently JTableBinding
                // is not smart enough to realize that the notification comes in another
                // thread and do a SwingUtilities.invokeLater. So we are force to execute this
                // in the EDT. Seee http://markmail.org/thread/6ehh76zt27qc5fis and
                // https://beansbinding.dev.java.net/issues/show_bug.cgi?id=60
                for (DataController df : dataControllerList) {
                    Iterator<Data> iter = dl.iterator();
                    while (iter.hasNext()) {
                        Data data = iter.next();
                        if (df.getId().equals(data.getId())) {
                            df.setValue(data.getValueToView());
                        }
                    }
                }
            }
        };
        executorService.execute(task);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHouse = new javax.swing.JPanel();
        btnHouse = new javax.swing.JButton();

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setLayout(null);

        btnHouse.setText("Loading Data...");
        btnHouse.setMaximumSize(new java.awt.Dimension(180, 30));
        btnHouse.setMinimumSize(new java.awt.Dimension(180, 30));
        btnHouse.setPreferredSize(new java.awt.Dimension(180, 30));
        btnHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHouseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHouseLayout = new javax.swing.GroupLayout(pnlHouse);
        pnlHouse.setLayout(pnlHouseLayout);
        pnlHouseLayout.setHorizontalGroup(
                pnlHouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnHouse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlHouseLayout.setVerticalGroup(
                pnlHouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnHouse, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        add(pnlHouse);
        pnlHouse.setBounds(11, 5, 180, 40);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHouseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHouseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHouse;
    private javax.swing.JPanel pnlHouse;
    // End of variables declaration//GEN-END:variables
}
