package com.agrologic.app.gui.rxtx;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.exception.DatabaseNotFound;
import com.agrologic.app.exception.RestartApplicationException;
import com.agrologic.app.exception.SerialPortControlFailure;
import com.agrologic.app.exception.WrongDatabaseException;
import com.agrologic.app.gui.ConfigurationDialog;
import com.agrologic.app.gui.rxtx.flock.DesignScreen;
import com.agrologic.app.gui.rxtx.flock.FlockManager;
import com.agrologic.app.i18n.LocaleManager;
import com.agrologic.app.model.Controller;
import com.agrologic.app.network.rxtx.NetworkState;
import com.agrologic.app.network.rxtx.SocketThread;
import com.agrologic.app.util.ApplicationUtil;
import com.agrologic.app.util.PropertyFileUtil;
import com.agrologic.app.util.Windows;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ApplicationLocal extends JFrame implements PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    private DatabaseManager dbManager;
    private ConfigurationDialog configDialog;
    private Configuration configuration;
    private Task task;
    private ProgressMonitor progressMonitor;
    private SocketThread networkThread;
    private MainScreenPanel[] mainScreenPanels;
    private ExecutorService threadPool = Executors.newFixedThreadPool(1);
    public static Logger logger = Logger.getLogger(ApplicationLocal.class);

    /**
     * Creates a new {@link ApplicationLocal}.
     */
    public ApplicationLocal() {
        initComponents();
        Windows.setWindowsLAF(this);
        Windows.centerOnScreen(this);
        final WindowListener closeWindow = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        };

        configuration = new Configuration();
        setTitle("WebchickLocal " + configuration.getVersion());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(closeWindow);
    }

    enum LoadState {
        CREATE_DAO, LOAD_DATA, CREATE_NETWORK, CREATE_SCREENS, FINISH
    }

    public void start() {
        final LoadingDialog loadingDialog = new LoadingDialog(ApplicationLocal.this);
        Thread loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                LoadState loadState = LoadState.CREATE_DAO;
                try {
                    for (int i = 25; i <= 500; i++) {
                        loadingDialog.setProgressValue(i);
                        if (loadingDialog.getProgressValue() == 500) {
                            loadingDialog.setVisible(false);
                            threadPool.submit(networkThread);
                            ApplicationLocal.this.setVisible(true);
                        }

                        ApplicationUtil.sleep(5);

                        switch (loadState) {
                            case CREATE_DAO:
                                logger.info("Creating derby database");
                                dbManager = new DatabaseManager(DaoType.DERBY);
                                dbManager.getDatabaseGeneralService().setDatabaseDir(PropertyFileUtil.getProgramPath());
                                loadingDialog.setProgressValue(150);
                                loadState = LoadState.LOAD_DATA;
                                i = 150;
                                break;

                            case LOAD_DATA:
                                logger.info("Loading data from database");
                                dbManager.doLoadTableData();
                                loadingDialog.setProgressValue(250);
                                i = 250;
                                loadState = LoadState.CREATE_NETWORK;
                                break;
                            case CREATE_NETWORK:
                                logger.info("Create network threads");
                                networkThread = new SocketThread(ApplicationLocal.this, dbManager);
                                loadingDialog.setProgressValue(350);
                                i = 350;
                                loadState = LoadState.CREATE_SCREENS;
                                break;
                            case CREATE_SCREENS:
                                logger.info("Creating controller screens ");
                                createControllersScreens();
                                i = 490;
                                loadState = LoadState.FINISH;
                                break;
                            default:
                                break;
                        }
                    }
                } catch (SerialPortControlFailure e) {
                    loadingDialog.setVisible(false);
                    logger.error(e);
                    showErrorMessage("Error", e.getMessage());
                    openConfiguration();
                    System.exit(0);
                } catch (WrongDatabaseException e) {

                    loadingDialog.setVisible(false);
                    logger.error(e);
                    showErrorMessage("Error", e.getMessage());
                    openConfiguration();
                    System.exit(0);
                } catch (DatabaseNotFound e) {
                    loadingDialog.setVisible(false);
                    logger.error(e);
                    showErrorMessage("Error", e.getMessage());
                    System.exit(0);
                } catch (Exception e) {
                    loadingDialog.setVisible(false);
                    logger.error(e);
                    showErrorMessage("Error", "");
                    System.exit(0);
                }
            }
        });
        loadingThread.start();
    }

    /**
     * Calculate maximum dimension
     *
     * @param size the size
     * @return dimension the calculated dimension
     */
    public Dimension calcMaximumDimension(int size) {
        Dimension dimension = new Dimension(0, 0);
        for (int i = 0; i < size; i++) {
            if (size == 1) {
                dimension.width = mainScreenPanels[i].getBounds().width;
                dimension.height = mainScreenPanels[i].getBounds().height;
            }

            if (dimension.width < mainScreenPanels[i].getBounds().width) {
                dimension.width = mainScreenPanels[i].getBounds().width;
            }
            if (dimension.height < mainScreenPanels[i].getBounds().height) {
                dimension.height = mainScreenPanels[i].getBounds().height;
            }
        }
        return dimension;
    }

    /**
     * Set main screen panel location according to dimension
     *
     * @param dimension    the dimension
     * @param size         the size
     * @param colsOnScreen the number of main screen panel column
     */
    public void setMainScreenLocation(Dimension dimension, int size, int colsOnScreen) {
        for (int i = 0; i < size; i++) {
            int difX = i % colsOnScreen;
            int difY = i / colsOnScreen;
            int x = dimension.width * difX;
            int y = dimension.height * difY;
            mainScreenPanels[i].setLocation(x, y);
        }
    }

    /**
     * Create main screen for each controller
     */
    private synchronized void createControllersScreens() {
        logger.info("Get loaded controllers");
        Collection<Controller> controllers =
                dbManager.getDatabaseLoader().getUser().getCellinks().iterator().next().getControllers();

        int size = controllers.size();
        logger.info("Start creating  main screens ");
        JPanel mainScreenPanelHolder = new JPanel(null);
        mainScreenPanels = new MainScreenPanel[size];
        for (int i = 0; i < size; i++) {
            mainScreenPanels[i] = new MainScreenPanel(dbManager, Lists.newArrayList(controllers).get(i));
            mainScreenPanelHolder.add(mainScreenPanels[i]);
        }

        int colsOnScreen = ScreenUI.MAIN_SCREEN_COL_NUMBERS;
        Dimension dimension = calcMaximumDimension(size);
        setMainScreenLocation(dimension, size, colsOnScreen);
        for (int i = 0; i < mainScreenPanels.length; i++) {
            for (int j = 0; j < mainScreenPanels.length; j++) {
                if (i != j) {
                    mainScreenPanels[i].addMainScreen(mainScreenPanels[j]);
                }
            }
        }

        dimension.width *= ((size / colsOnScreen) > 0 ? 1 : 0)
                * colsOnScreen + ((size / colsOnScreen) > 0 ? 0 : (size % colsOnScreen));
        dimension.height *= ((size / colsOnScreen) + ((size % colsOnScreen) > 0 ? 1 : 0));
        mainScreenPanelHolder.setPreferredSize(dimension);
        mainScreenPanelHolder.setSize(dimension);

        logger.info("Start creating  second screen panel ");
        SecondScreenPanel[] secondScreenPanels = new SecondScreenPanel[controllers.size()];

        for (int i = 0; i < controllers.size(); i++) {
            secondScreenPanels[i] = new SecondScreenPanel(dbManager, Lists.newArrayList(controllers).get(i));
            mainScreenPanels[i].addSecondPanel(secondScreenPanels[i]);
            mainScreenPanelHolder.add(secondScreenPanels[i]);
            secondScreenPanels[i].setMainScreenPanel(mainScreenPanels[i]);
        }
        logger.info("Finished creating  second screen panel ");

        JScrollPane scrollPane = new JScrollPane(mainScreenPanelHolder);
        scrollPane.setBorder(null);
        scrollPane.setAutoscrolls(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);

        logger.info("Set main screen to other main screens ");
        for (MainScreenPanel mainScreenPanel : mainScreenPanels) {
            mainScreenPanel.setScrollPane(scrollPane);
            mainScreenPanel.setHolderPanel(mainScreenPanelHolder);
            mainScreenPanel.setHolderPanelSize(mainScreenPanelHolder.getPreferredSize());
            mainScreenPanel.setBounds();
        }
        setLayout(null);
        getContentPane().add(scrollPane);

        Dimension screenResolution = Windows.screenResolution();
        StatusPanel statusPanel = new StatusPanel();
        statusPanel.setBounds(0, screenResolution.height - 105, screenResolution.width, 30);

        networkThread.setStatusPanel(statusPanel);
        getContentPane().add(statusPanel);// put at bottom of UI
        setSize(screenResolution.width, screenResolution.height);
        Windows.centerOnScreen(this);
    }

    /**
     * Stop running program with conformation dialog.
     */
    public void exit() {
        String title = "Close Program Confirmation";
        String message = "Do you really want to close program ?";
        int result = JOptionPane.showConfirmDialog(ApplicationLocal.this, message, title, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if ((networkThread != null) && (networkThread.getNetworkState() != NetworkState.STATE_STOP)) {
                networkThread.setThreadState(NetworkState.STATE_STOP);
            }
            System.exit(0);
        }
    }

    /**
     * Show error message
     *
     * @param title   the error message title
     * @param message the error message text
     */
    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(ApplicationLocal.this, message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuExit = new javax.swing.JMenuItem();
        mnuFlock = new javax.swing.JMenu();
        mnuManage = new javax.swing.JMenuItem();
        mnuTools = new javax.swing.JMenu();
        mnuConfig1 = new javax.swing.JMenuItem();
        mnuSetupDriver = new javax.swing.JMenuItem();
        mnuDesign = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ResourceBundle bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // NOI18N
        mnuFile.setText(bundle.getString("menu.file")); // NOI18N

        mnuExit.setText(bundle.getString("menu.file.exit")); // NOI18N
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuExit);

        jMenuBar1.add(mnuFile);

        mnuFlock.setText("Flock");

        mnuManage.setText("Manage");
        mnuManage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManageActionPerformed(evt);
            }
        });
        mnuFlock.add(mnuManage);

        jMenuBar1.add(mnuFlock);

        mnuTools.setText(bundle.getString("menu.tools")); // NOI18N

        mnuConfig1.setText(bundle.getString("menu.tools.config")); // NOI18N
        mnuConfig1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfig1ActionPerformed(evt);
            }
        });
        mnuTools.add(mnuConfig1);

        mnuSetupDriver.setText(bundle.getString("menu.tools.setup.driver")); // NOI18N
        mnuSetupDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSetupDriverActionPerformed(evt);
            }
        });
        mnuTools.add(mnuSetupDriver);

        mnuDesign.setText("Design");
        mnuDesign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDesignActionPerformed(evt);
            }
        });
        mnuTools.add(mnuDesign);

        jMenuBar1.add(mnuTools);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 757, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 544, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mnuExitActionPerformed

    private void mnuConfig1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConfig1ActionPerformed
        openConfiguration();
    }//GEN-LAST:event_mnuConfig1ActionPerformed

    private void mnuSetupDriverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSetupDriverActionPerformed
        progressMonitor = new ProgressMonitor(ApplicationLocal.this, "Installing AG USB driver", "", 0, 100);
        progressMonitor.setProgress(0);
        task = new Task();
        task.addPropertyChangeListener(ApplicationLocal.this);
        task.execute();
    }//GEN-LAST:event_mnuSetupDriverActionPerformed

    private void mnuManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManageActionPerformed
        FlockManager fmw = new FlockManager();
        fmw.setVisible(true);
    }//GEN-LAST:event_mnuManageActionPerformed

    private void mnuDesignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDesignActionPerformed
        DesignScreen ds = new DesignScreen(ApplicationLocal.this, true);
        ds.setVisible(true);

    }//GEN-LAST:event_mnuDesignActionPerformed

    public void openConfiguration() {
        boolean result;
        try {
            if (configDialog == null) {
                configDialog = new ConfigurationDialog(this, true);
                result = configDialog.showDialog();
            } else {
                result = configDialog.showDialog();
            }
            if (result == true) {
                ApplicationUtil.restartApplication(ApplicationLocal.class);
            }
        } catch (RestartApplicationException e) {
            showErrorMessage("Error", e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ApplicationLocal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ApplicationLocal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ApplicationLocal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ApplicationLocal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ApplicationLocal wcsl = new ApplicationLocal();
                wcsl.start();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem mnuConfig1;
    private javax.swing.JMenuItem mnuDesign;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuFlock;
    private javax.swing.JMenuItem mnuManage;
    private javax.swing.JMenuItem mnuSetupDriver;
    private javax.swing.JMenu mnuTools;
    // End of variables declaration//GEN-END:variables

    class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            setProgress(0);
            try {
                ApplicationUtil.sleep(100);
                Process p = Runtime.getRuntime().exec("CDM20814_Setup.exe");
                while (progress < 100 && !isCancelled()) {
                    //Sleep for up to one second.
                    ApplicationUtil.sleep(random.nextInt(100));
                    //Make random progress.
                    p.waitFor();
                    progress += random.nextInt(2);
                    setProgress(Math.min(progress, 100));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Invoked when task progress property changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);
            String message = String.format("Completed %d%%.\n", progress);
            progressMonitor.setNote(message);
            if (progressMonitor.isCanceled() || task.isDone()) {
                Toolkit.getDefaultToolkit().beep();
                if (progressMonitor.isCanceled()) {
                    task.cancel(true);
                }
            }
        }
    }
}
