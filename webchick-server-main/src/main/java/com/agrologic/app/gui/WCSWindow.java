/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ServerWindow.java
 *
 * Created on Jun 10, 2009, 8:52:36 AM
 * @version V0.0.12 <br> version pattern <release.database.core_code>
 */
package com.agrologic.app.gui;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.VersionDao;
import com.agrologic.app.except.JarFileWasNotFound;
import com.agrologic.app.except.StartProgramException;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.network.*;
import com.agrologic.app.util.Clock;
import com.agrologic.app.util.ProgramInstanceLocker;
import com.agrologic.app.util.RestartApplication;
import com.agrologic.app.util.Windows;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class WCSWindow extends JFrame implements Observer, ServerUI {

    private static final long serialVersionUID = 1L;
    private ConfigurationDialog configDialog;
    private Configuration configuration;
    private ServerThread serverSocketThread;
    private Thread serverThread;
    private ServerInfo serverInfo;
    private Clock clock;
    private JPopupMenu popupTableMenu;
    private JMenuItem startedState;
    private JMenuItem stoppedState;
    private CellinkTable cellinkTable;
    private final Logger logger = Logger.getLogger(WCSWindow.class);
    private static final String ERROR_OPENING_SOCKET_SERVER_ADDRESS_ALREADY_IN_USE = "Error opening socket \n" +
            "host or port already in use !";
    private static final String CANNOT_CREATE_LOCK_FILE = "Can't create Lock File.\nAccess is denied !";

    /**
     * Creates new form ServerWindow
     *
     * @throws StartProgramException
     */
    public WCSWindow() throws StartProgramException {

        try {
            ProgramInstanceLocker.lockFile();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), CANNOT_CREATE_LOCK_FILE, JOptionPane.ERROR_MESSAGE);
            logger.error("Cannot create lock file . Check security configuration in OS ");
            logger.info("Program exit");
            System.exit(0);
        }

        initComponents();

        JScrollPane cellinkTableScrolPane = new JScrollPane();
        cellinkTable = new CellinkTable();
        cellinkTableScrolPane.setViewportView(cellinkTable);
        paneServer.addTab("Cellink List", cellinkTableScrolPane);
        popupTableMenu = new JPopupMenu();
        startedState = new JMenuItem("Start");
        startedState.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = cellinkTable.getSelectedRow();
                cellinkTable.setState(row, CellinkState.STATE_START);
            }
        });

        popupTableMenu.add(startedState);
        stoppedState = new JMenuItem("Stop");
        stoppedState.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = cellinkTable.getSelectedRow();
                cellinkTable.setState(row, CellinkState.STATE_STOP);
            }
        });
        popupTableMenu.add(stoppedState);

        Windows.setWindowsLAF(WCSWindow.this);
        Windows.centerOnScreen(WCSWindow.this);
        Windows.setIconImage(WCSWindow.this, "/images/server.png");

        // start running network communication
        ActionListener start = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                startNetwork();
            }
        };
        btnStart.addActionListener(start);

        // stop running network communication
        ActionListener stop = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopNetwork();
            }
        };
        btnStop.addActionListener(stop);

        // create show log file action listener
        ActionListener showLogFile = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showLogFile();
            }
        };
        btnLogFile.addActionListener(showLogFile);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        WindowListener closeWindow = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        };
        addWindowListener(closeWindow);

        ActionListener closeProgram = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exitProgram();
            }
        };
        btnExit.addActionListener(closeProgram);

        hookIntoLogging();

        this.configuration = new Configuration();
        this.setTitle(this.getTitle() + " " + configuration.getVersion());
        this.serverInfo = new ServerInfo(this);
        this.serverInfo.addObserver(this);
        this.clock = new Clock();
        this.clock.addObserver(serverInfo);

        cellinkTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isMetaDown()) {
                    popupTableMenu.show(cellinkTable, e.getX(), e.getY());
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();
                    // get the row index that contains that coordinate
                    int rowNumber = cellinkTable.rowAtPoint(p);
                    // Get the ListSelectionModel of the JTable
                    ListSelectionModel model = cellinkTable.getSelectionModel();
                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    model.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });
        initServerSocketThread();

        try {
            String version = DbImplDecider.use(DaoType.MYSQL).getDao(VersionDao.class).getVersion();
        } catch (RuntimeException e) {
            logger.debug("Cannot get database version ", e);
            openConfiguration();
        }
    }

    public final void initServerSocketThread() {
        this.serverSocketThread = new ServerThread(configuration, this);
        this.serverSocketThread.addObserver(this);
        this.serverSocketThread.addObserver(serverInfo);

        if (configuration.runOnWindowsStart() == Boolean.TRUE) {
            this.serverSocketThread.setServerActivityState(ServerActivityStates.START);
            serverThread = new Thread(this.serverSocketThread, "ServerThread");
            serverThread.start();
            clock.start();
            if (serverSocketThread.getServerState() == ServerActivityStates.ERROR) {
                JOptionPane.showMessageDialog(WCSWindow.this, ERROR_OPENING_SOCKET_SERVER_ADDRESS_ALREADY_IN_USE, "Error", JOptionPane.ERROR_MESSAGE);
                serverSocketThread.setServerActivityState(ServerActivityStates.STOPPED);
            } else {
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                cellinkTable.startMonitoring();
            }
        } else {
            this.serverSocketThread.setServerActivityState(ServerActivityStates.IDLE);
            this.btnStop.doClick();
        }
    }

    private void hookIntoLogging() {
        TextPaneAppender tpa = new TextPaneAppender("ConsoleAppender");
        tpa.setTextPane(statusConsole);
        Logger.getLogger("com.agrologic.app.network.ServerThread").addAppender(tpa);

        TextPaneAppender tpaf = new TextPaneAppender("ConsoleAppender");
        tpaf.setTextPane(trafficLog);
        Logger.getLogger("com.agrologic.app.network.SocketThread").addAppender(tpaf);
    }

    /**
     * Start running network
     */
    private void startNetwork() {
        Thread runner = new Thread("StartThread") {

            @Override
            public void run() {
                try {
                    serverSocketThread.setServerActivityState(ServerActivityStates.START);
                    if (!serverThread.isAlive()) {
                        serverThread.start();
                    }

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//                    while (serverSocketThread.getServerState() != ServerActivityStates.RUNNING
//                            && serverSocketThread.getServerState() != ServerActivityStates.ERROR) {
//                    }
                    if (!clock.isRunning()) {
                        clock.start();
                    }

                } finally {
                    if (serverSocketThread.getServerState() == ServerActivityStates.ERROR) {
                        JOptionPane.showMessageDialog(WCSWindow.this, ERROR_OPENING_SOCKET_SERVER_ADDRESS_ALREADY_IN_USE, "Error", JOptionPane.ERROR_MESSAGE);
                        serverSocketThread.setServerActivityState(ServerActivityStates.STOPPED);
                    } else {
                        btnStart.setEnabled(false);
                        btnStop.setEnabled(true);
                        cellinkTable.startMonitoring();
                    }
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        runner.start();
    }

    /**
     * Stop running network
     */
    private void stopNetwork() {

        Thread runner = new Thread("ServerThread") {

            @Override
            public void run() {
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    //btnSetting.setEnabled(true);
                    serverSocketThread.setServerActivityState(ServerActivityStates.STOPPING);
                    serverSocketThread.shutdownServer();

                    while (serverSocketThread.getServerState() != ServerActivityStates.IDLE) {
                    }
                    if (clock.isRunning()) {
                        clock.stop();
                    }
                    cellinkTable.stopMonitoring();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        runner.start();
    }

    /**
     * Show log file in note pad.
     */
    private void showLogFile() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Thread runner = new Thread() {

            @Override
            public void run() {
                String fileName = "logs/system.log";
                try {
                    Runtime.getRuntime().exec("notepad.exe " + fileName);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error opening " + fileName,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };
        runner.start();
    }

    /**
     * Stop running program with conformation dialog.
     */
    private void exitProgram() {
        String title = "Close Program Confirmation";
        String message = "Do you really want to close program ?";
        int result = JOptionPane.showConfirmDialog(WCSWindow.this, message, title, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            btnStop.doClick();
            if (serverThread.isAlive()) {
                serverThread.interrupt();
            }

            if (serverSocketThread.getServerState() == ServerActivityStates.RUNNING) {
                serverSocketThread.setServerActivityState(ServerActivityStates.EXIT);
            }
            System.exit(0);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ServerThread) {
            if (arg instanceof ServerActivityStates) {
                ServerActivityStates state = (ServerActivityStates) arg;
                if (state == ServerActivityStates.START) {
                    enableToolBarButtons(false);
                } else if (state == ServerActivityStates.STOPPED) {
                    enableToolBarButtons(true);
                }
            }
        }
        setServerInfoPanel();
    }

    private void setServerInfoPanel() {
        lblOnOff.setText(serverInfo.getServerStatus());
        lblTimeOn.setText(serverInfo.getTime());
        lblTotalCellinks.setText(serverInfo.getTotalCellinks().toString());
        lblConnectedCellinks.setText(serverInfo.getActiveCellinks().toString());
    }

    private void enableToolBarButtons(boolean enable) {
        btnStart.setEnabled(enable);
        btnStop.setEnabled(!enable);
        //btnSetting.setEnabled(enable);
    }

    public void openConfiguration() {
        try {
            if (configDialog == null) {
                configDialog = new ConfigurationDialog(this, true);
                configDialog.show();
            } else {
                configDialog.show();
            }
            RestartApplication.restartUsingFileClassAndProcessBuilder();
        } catch (JarFileWasNotFound ex) {
            logger.error("Cannot restart application . URI does not correct.", ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ToolBar = new javax.swing.JToolBar();
        btnStart = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnSetting = new javax.swing.JButton();
        btnLogFile = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnWizardDB = new javax.swing.JButton();
        pnlServer = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblOnOff = new javax.swing.JLabel();
        lblTimeOn = new javax.swing.JLabel();
        lblConnectedCellinks = new javax.swing.JLabel();
        lblTotalCellinks = new javax.swing.JLabel();
        paneServer = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnShowOpendSockets = new javax.swing.JButton();
        btnCloseUnused = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        statusConsole = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trafficLog = new javax.swing.JTextPane();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Webchick Server");
        setMinimumSize(new java.awt.Dimension(692, 546));

        ToolBar.setFloatable(false);
        ToolBar.setToolTipText("Tool Bar");
        ToolBar.setBorderPainted(false);
        ToolBar.setMaximumSize(new java.awt.Dimension(250, 25));
        ToolBar.setMinimumSize(new java.awt.Dimension(250, 25));
        ToolBar.setPreferredSize(new java.awt.Dimension(250, 25));

        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png"))); // NOI18N
        btnStart.setToolTipText("Run");
        btnStart.setFocusable(false);
        btnStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStart.setMaximumSize(new java.awt.Dimension(32, 32));
        btnStart.setMinimumSize(new java.awt.Dimension(32, 32));
        btnStart.setPreferredSize(new java.awt.Dimension(32, 32));
        btnStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(btnStart);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stop.png"))); // NOI18N
        btnStop.setToolTipText("Stop");
        btnStop.setFocusable(false);
        btnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStop.setMaximumSize(new java.awt.Dimension(32, 32));
        btnStop.setMinimumSize(new java.awt.Dimension(32, 32));
        btnStop.setPreferredSize(new java.awt.Dimension(32, 32));
        btnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(btnStop);

        btnSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings.png"))); // NOI18N
        btnSetting.setToolTipText("Setting");
        btnSetting.setFocusable(false);
        btnSetting.setMaximumSize(new java.awt.Dimension(32, 32));
        btnSetting.setMinimumSize(new java.awt.Dimension(32, 32));
        btnSetting.setPreferredSize(new java.awt.Dimension(32, 32));
        btnSetting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openConfiguration();
            }
        });
        ToolBar.add(btnSetting);

        btnLogFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/note.gif"))); // NOI18N
        btnLogFile.setToolTipText("Log");
        btnLogFile.setFocusable(false);
        btnLogFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogFile.setMaximumSize(new java.awt.Dimension(32, 32));
        btnLogFile.setMinimumSize(new java.awt.Dimension(32, 32));
        btnLogFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(btnLogFile);

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logout.png"))); // NOI18N
        btnExit.setToolTipText("Exit");
        btnExit.setFocusable(false);
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setMaximumSize(new java.awt.Dimension(32, 32));
        btnExit.setMinimumSize(new java.awt.Dimension(32, 32));
        btnExit.setPreferredSize(new java.awt.Dimension(32, 32));
        btnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(btnExit);

        btnWizardDB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/database.png"))); // NOI18N
        btnWizardDB.setToolTipText(" Database Wizard");
        btnWizardDB.setFocusable(false);
        btnWizardDB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnWizardDB.setMaximumSize(new java.awt.Dimension(32, 32));
        btnWizardDB.setMinimumSize(new java.awt.Dimension(32, 32));
        btnWizardDB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(btnWizardDB);

        pnlServer.setBorder(javax.swing.BorderFactory.createTitledBorder("Server Info"));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("On/Off");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Time on");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Alive cellinks");

        jLabel6.setText("Total cellinks");

        lblOnOff.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblOnOff.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOnOff.setText("Stopped");

        lblTimeOn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTimeOn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTimeOn.setText("00:00:00");

        lblConnectedCellinks.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblConnectedCellinks.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblConnectedCellinks.setText("0");

        lblTotalCellinks.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalCellinks.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalCellinks.setText("0");

        javax.swing.GroupLayout pnlServerLayout = new javax.swing.GroupLayout(pnlServer);
        pnlServer.setLayout(pnlServerLayout);
        pnlServerLayout.setHorizontalGroup(
                pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlServerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblTotalCellinks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblOnOff, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTimeOn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblConnectedCellinks, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnlServerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{jLabel3, jLabel4, jLabel5, jLabel6});

        pnlServerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{lblConnectedCellinks, lblOnOff, lblTimeOn, lblTotalCellinks});

        pnlServerLayout.setVerticalGroup(
                pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlServerLayout.createSequentialGroup()
                                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlServerLayout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel6))
                                        .addGroup(pnlServerLayout.createSequentialGroup()
                                                .addComponent(lblOnOff)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTimeOn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblConnectedCellinks, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTotalCellinks)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlServerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{lblConnectedCellinks, lblOnOff, lblTimeOn, lblTotalCellinks});

        pnlServerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{jLabel3, jLabel4, jLabel5, jLabel6});

        btnShowOpendSockets.setText("Opened Socket List");
        btnShowOpendSockets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowOpendSocketsActionPerformed(evt);
            }
        });

        btnCloseUnused.setText("Close Unused Sockets");
        btnCloseUnused.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseUnusedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnShowOpendSockets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCloseUnused, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnShowOpendSockets)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCloseUnused)
                                .addContainerGap(315, Short.MAX_VALUE))
        );

        statusConsole.setEditable(false);
        jScrollPane3.setViewportView(statusConsole);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 642, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                                        .addGap(10, 10, 10)))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 380, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                                        .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        paneServer.addTab("Server Log", jPanel1);

        jScrollPane1.setViewportView(trafficLog);

        paneServer.addTab("Traffic Log", jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(ToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(paneServer)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(pnlServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(ToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(paneServer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowOpendSocketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowOpendSocketsActionPerformed
        serverSocketThread.showThreadList();
    }//GEN-LAST:event_btnShowOpendSocketsActionPerformed

    private void btnCloseUnusedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseUnusedActionPerformed
        Map<Long, SocketThread> threads = serverSocketThread.getThreadList();
        if (threads != null && !threads.isEmpty()) {
            List<SocketThread> deadThreads = new ArrayList<SocketThread>();
            Set<Entry<Long, SocketThread>> entries = threads.entrySet();
            for (Entry<Long, SocketThread> e : entries) {
                SocketThread st = e.getValue();
                if (st.getThreadState() == NetworkState.STATE_STOP) {
                    deadThreads.add(st);
                }
            }
            StringBuilder sb = new StringBuilder();
            if (deadThreads.size() == 0) {
                sb.append("No unused open sockets in system ");
                JOptionPane.showMessageDialog(WCSWindow.this, sb.toString());
            } else {
                for (SocketThread nt : deadThreads) {
                    threads.remove(nt.getCellink().getId());
                    nt.getCommControl().close();
                    sb.append("Session with cellink id ").append(nt.getCellink().getId()).append(" closed\n ");
                }
                JOptionPane.showMessageDialog(WCSWindow.this, sb.toString());
            }
        }
    }//GEN-LAST:event_btnCloseUnusedActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Logger logger = Logger.getRootLogger();
                try {
                    logger.info("start server");
                    new WCSWindow().setVisible(true);
                } catch (StartProgramException e) {
                    logger.error(e);
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnCloseUnused;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogFile;
    private javax.swing.JButton btnSetting;
    private javax.swing.JButton btnShowOpendSockets;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnWizardDB;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblConnectedCellinks;
    private javax.swing.JLabel lblOnOff;
    private javax.swing.JLabel lblTimeOn;
    private javax.swing.JLabel lblTotalCellinks;
    private javax.swing.JTabbedPane paneServer;
    private javax.swing.JPanel pnlServer;
    private javax.swing.JTextPane statusConsole;
    private javax.swing.JTextPane trafficLog;
    // End of variables declaration//GEN-END:variables

    @Override
    public CellinkTable getCellinkTable() {
        return cellinkTable;
    }

    @Override
    public Iterator<Cellink> iterator() {
        return cellinkTable.iterator();
    }
}