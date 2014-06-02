package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.rxtx.DataController;
import com.agrologic.app.util.Windows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainScreenPanel extends JPanel implements ScreenUI {

    public static final int BUTTON_HEIGHT = 30;
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
    private ScheduledExecutorService executor;
    private BufferedImage image;
    private static Logger logger = LoggerFactory.getLogger(MainScreenPanel.class);

    /**
     * Creates new form MainScreenPanel
     */
    public MainScreenPanel(final DatabaseManager dbManager, final Controller controller) {
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

        try {
            image = ImageIO.read(getClass().getResource("/images/alarm.gif"));
        } catch (IOException e) {
            logger.error("Cannot load icon alarm.gif");
        }

        initLoadedControllerData();
        initScreenComponents();

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

                    for (DataController df : dataControllerList) {
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

    @Override
    public void initLoadedControllerData() {
        long screenId = 1;  // main screen id
        long tableId = 1;  // main screen table id
        Collection<Data> dataList = controller.getProgram().getScreenById(screenId).getTableById(tableId).getDataList();
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
