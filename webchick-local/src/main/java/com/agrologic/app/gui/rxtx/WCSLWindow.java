/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.mysql.impl.MySqlDaoFactory;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.except.ObjectDoesNotExist;
import com.agrologic.app.except.SerialPortControlFailure;
import com.agrologic.app.gui.ConfigurationDialog;
import com.agrologic.app.gui.WCSWindow;
import com.agrologic.app.gui.flock.DesignScreen;
import com.agrologic.app.gui.flock.FlockManager;
import com.agrologic.app.model.Controller;
import com.agrologic.app.network.rxtx.NetworkState;
import com.agrologic.app.network.rxtx.SocketThread;
import com.agrologic.app.util.*;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class WCSLWindow extends JFrame implements PropertyChangeListener {

    private static final String ERROR_OPENING_COM_PORT = "An error occured while trying to access the com port."
            + "\n Please select a different  port."
            + "\n Click on the OK button to open configuration dialog .";
    private static final String USER_CANNOT_FOUND = "The User ID and Farm ID could not be found. \n"
            + "Click on the OK button to open the Configuration window and manually enter them there.";
    private static final long serialVersionUID = 1L;

    enum ServerState {

        START, STOP
    };
    WCSLWindow.ServerState currentState = WCSLWindow.ServerState.STOP;
    private DatabaseManager dbManager;
    private ConfigurationDialog configDialog;
    private Configuration configuration;
    private Task task;
    private ProgressMonitor progressMonitor;
    private JFileChooser chooser;
    private File currDir;
    private Process process;
    private SocketThread networkThread;
    private FlockManager fmw;
    private MainScreenPanel[] mainScreenPanels;
    private SecondScreenPanel[] secondscreens;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private long langId;
    public static Logger logger = Logger.getLogger(WCSLWindow.class);

    /**
     * Creates new form WCSL
     */
    public WCSLWindow() {
        initComponents();
        try {
            Windows.setWindowsLAF(this);
            Windows.centerOnScreen(this);
            configuration = new Configuration();
            setTitle("WebchickLocal " + configuration.getVersion());
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            final WindowListener closeWindow = new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            };
            addWindowListener(closeWindow);
            dbManager = new DatabaseManager(DaoType.DERBY);
            dbManager.getDatabaseGeneralService().setDatabaseDir(PropertyFileUtil.getProgramPath());
            dbManager.doLoadTableData();
            try {
                networkThread = new SocketThread(WCSLWindow.this, dbManager);
            } catch (SerialPortControlFailure e) {
                logger.info(e.getMessage(), e);
                throw new Throwable(ERROR_OPENING_COM_PORT + e.getMessage() + "\n");
            }
            logger.info("Create controller screens");
            createControllersScreens();
            logger.info("Fineshed creating controller screens");
            Thread comThread = new Thread(networkThread);
            logger.info("Start running communication thread");
            comThread.start();
        } catch (Throwable t) {
            t.printStackTrace();
            if (t.getMessage() == null) {
                JOptionPane.showMessageDialog(WCSLWindow.this, USER_CANNOT_FOUND, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(WCSLWindow.this, ERROR_OPENING_COM_PORT, "Error", JOptionPane.ERROR_MESSAGE);
            }
            openConfiguration();
            System.exit(0);
        }
    }

    /**
     * Calculate maximum dimension
     *
     * @param size the size
     * @param colsOnScreen the number of column
     * @return dimension the calculated dimension
     */
    public Dimension calcMaximumDimens(int size, int colsOnScreen) {
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
     * @param dimens the dimension
     * @param size the size
     * @param colsOnScreen the number of main screen panel column
     */
    public void setMainScreenLocation(Dimension dimens, int size, int colsOnScreen) {
        for (int i = 0; i < size; i++) {
            int difX = i % colsOnScreen;
            int difY = i / colsOnScreen;
            int x = dimens.width * difX;
            int y = dimens.height * difY;
            mainScreenPanels[i].setLocation(x, y);
        }
    }

    /**
     * Show setting dialog window .
     */
    public void openSetting() {
//        settingView = new SettingDialog(this, true);
//        settingView.addListener(this);
//        settingView.show();
    }

    /**
     * Show log file in note pad program.
     */
    public void openLogfile() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final Thread runner = new Thread() {

            @Override
            public void run() {
                String fileName = "logs\\system.log";
                try {
                    Runtime.getRuntime().exec("notepad.exe " + fileName);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error opening " + fileName,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };
        runner.start();
    }

    private String getPath() {
        String drive = System.getProperty("user.dir");
        String[] pp = drive.split("\\\\");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            sb.append(pp[i]).append("\\");
        }
        return sb.toString();

    }

    private String getMySQLPath() {
        String mySqlPath = getPath();
        String version = ((MySqlDaoFactory) DbImplDecider.getDaoFactory(DaoType.MYSQL)).getMySQLVersion();
        mySqlPath = mySqlPath + "MySql\\MySql Server " + version + "\\bin";
        return mySqlPath;
    }

    /**
     * Create main screen for each controller
     */
    private synchronized void createControllersScreens() {
        try {
            Long cellinkId = Long.parseLong(configuration.getCellinkId());
            logger.info("Get controllers from database");

            List<Controller> controllers =
                    dbManager.getDatabaseLoader().getUser().getCellinkById(cellinkId).getControllers();
            int size = controllers.size();

            logger.info("Start creating  main screens ");
            JPanel mainScreenPanelHolder = new JPanel(null);
            mainScreenPanels = new MainScreenPanel[size];
            for (int i = 0; i < size; i++) {
                mainScreenPanels[i] = new MainScreenPanel(dbManager, controllers.get(i));
                mainScreenPanelHolder.add(mainScreenPanels[i]);
            }

            int colsOnScreen = ScreenUI.COL_NUMBERS;
            Dimension dimens = calcMaximumDimens(size, colsOnScreen);
            setMainScreenLocation(dimens, size, colsOnScreen);
            for (int i = 0; i < mainScreenPanels.length; i++) {
                for (int j = 0; j < mainScreenPanels.length; j++) {
                    if (i != j) {
                        mainScreenPanels[i].addMainScreen(mainScreenPanels[j]);
                    }
                }
            }

            dimens.width *= ((size / colsOnScreen) > 0 ? 1 : 0)
                    * colsOnScreen + ((size / colsOnScreen) > 0 ? 0 : (size % colsOnScreen));
            dimens.height *= ((size / colsOnScreen) + ((size % colsOnScreen) > 0 ? 1 : 0));
            mainScreenPanelHolder.setPreferredSize(dimens);
            mainScreenPanelHolder.setSize(dimens);

            logger.info("Start creating  second screen panel ");
            secondscreens = new SecondScreenPanel[controllers.size()];

            for (int i = 0; i < controllers.size(); i++) {
                ObjectSizeFetcher.getObjectSize();
                logger.info("Second screen panel of controller : " + controllers.get(i));
                secondscreens[i] = new SecondScreenPanel(dbManager, controllers.get(i));
                mainScreenPanels[i].addSecondPanel(secondscreens[i]);
                mainScreenPanelHolder.add(secondscreens[i]);
                secondscreens[i].setMainScreenPanel(mainScreenPanels[i]);
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

            JSeparator statusSeparator = new JSeparator();
            Dimension screenResolut = Windows.screenResolution();
            statusSeparator.setBounds(0, screenResolut.height - 105, screenResolut.width, 30);
            JLabel statusLabel = new JLabel("Started");
            statusLabel.setBounds(0, 0, 250, 20);
            statusSeparator.add(statusLabel);
            networkThread.setStatusLabel(statusLabel);

            getContentPane().add(statusSeparator);
            setSize(screenResolut.width, screenResolut.height);
            Windows.centerOnScreen(this);
            logger.info("Fineshed creating main screens and second screens panels");
        } catch (ObjectDoesNotExist ex) {
            ex.printStackTrace();
        }
    }

    private Rectangle calcMaxBoundsMainScreens() {
        Rectangle maxBounds = new Rectangle(0, 0, 0, 0);
        for (MainScreenPanel msp : mainScreenPanels) {
            Rectangle b = msp.getBounds();
            if (b.width > maxBounds.width) {
                maxBounds.width = b.width;
            }
            if (b.height > maxBounds.height) {
                maxBounds.height = b.height;
            }
        }
        return maxBounds;
    }

    /**
     * Stop running program with conformation dialog.
     */
    public void exit() {
        String title = "Close Program Confirmation";
        String message = "Do you really want to close program ?";
        int result = JOptionPane.showConfirmDialog(WCSLWindow.this, message, title, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if ((networkThread != null) && (networkThread.getNetworkState() != NetworkState.STATE_STOP)) {
                networkThread.setThreadState(NetworkState.STATE_STOP);
            }
            System.exit(0);
        }
    }

    public void showErrorMsg(String title, String msg) {
        JOptionPane.showMessageDialog(null, msg,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("MessagesBundle_en_US"); // NOI18N
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

        mnuConfig1.setText(bundle.getString("menu.tools.conig")); // NOI18N
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
        progressMonitor = new ProgressMonitor(WCSLWindow.this, "Installing AG USB driver", "", 0, 100);
        progressMonitor.setProgress(0);
        task = new Task();
        task.addPropertyChangeListener(WCSLWindow.this);
        task.execute();
    }//GEN-LAST:event_mnuSetupDriverActionPerformed

    private void mnuManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManageActionPerformed
        logger.info("Show flock manager");
        fmw = new FlockManager();
        fmw.setVisible(true);
    }//GEN-LAST:event_mnuManageActionPerformed

    private void mnuDesignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDesignActionPerformed
        DesignScreen ds = new DesignScreen(WCSLWindow.this, true);
        ds.setVisible(true);

    }//GEN-LAST:event_mnuDesignActionPerformed

    public void openConfiguration() {
        boolean result = false;
        try {
            if (configDialog == null) {
                configDialog = new ConfigurationDialog(this, true);
                result = configDialog.showDialog();
            } else {
                result = configDialog.showDialog();
            }
            if (result == true) {
                LocalUtil.restartApplicationss();
            }
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openJARFile() {
        try {
            String path = new File(".").getCanonicalPath();
            path = path + "\\lib\\WebchickConfig.jar";
            File file = new File(path);
            URL url = file.toURL();
            URL[] urls = new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls);
            Class clazz = cl.loadClass("webchickconfig.ui.ConfigurationDialog");
            ConfigurationDialog cfg = (ConfigurationDialog) clazz.newInstance();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
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
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see
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
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new WCSLWindow().setVisible(true);
                logger.info("Display on screen");
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
                LocalUtil.sleep(100);
                Process p = Runtime.getRuntime().exec("CDM20814_Setup.exe");
                while (progress < 100 && !isCancelled()) {
                    //Sleep for up to one second.
                    LocalUtil.sleep(random.nextInt(100));
                    //Make random progress.
                    p.waitFor();
                    System.out.println(p.exitValue());
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
