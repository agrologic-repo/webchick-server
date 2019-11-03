package com.agrologic.app.gui;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.exception.RestartApplicationException;
import com.agrologic.app.exception.StartProgramException;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.network.*;
import com.agrologic.app.util.ApplicationUtil;
import com.agrologic.app.util.Clock;
import com.agrologic.app.util.ProgramInstanceLocker;
import com.agrologic.app.util.Windows;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class Application extends JFrame implements Observer, ServerUI {

    private static final long serialVersionUID = 1L;
    private ConfigurationDialog configDialog;
    private Configuration configuration;
    private ServerThread serverSocketThread;
    private Thread serverThread;
    private ServerInfo serverInfo;
    private Clock clock;
    private CellinkTable cellinkTable;
    private final Logger logger = Logger.getLogger(Application.class);
    private static final String SOCKET_ALREADY_IN_USE = "Error opening socket \n Host or port already in use !";
    private static final String CANNOT_CREATE_LOCK_FILE = "Can't create Lock File.\nAccess is denied !";

    /**
     * Creates new form ServerWindow
     *
     * @throws StartProgramException
     */
    public Application() throws StartProgramException {
        logger.info("Start Server 1");
        try {
            ProgramInstanceLocker.lockFile();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), CANNOT_CREATE_LOCK_FILE, JOptionPane.ERROR_MESSAGE);
            logger.error("Cannot create lock file . Check security configuration in OS ");
            logger.info("Program exit");
            System.exit(0);
        }

        logger.info("Start Server 2");
        initComponents();

        JScrollPane cellinkTableScrolPane = new JScrollPane();
        cellinkTable = new CellinkTable();
        cellinkTableScrolPane.setViewportView(cellinkTable);
        paneServer.addTab("Cellink List", cellinkTableScrolPane);

        Windows.setWindowsLAF(Application.this);
        Windows.centerOnScreen(Application.this);
        Windows.setIconImage(Application.this, "/images/server.png");

        logger.info("Start Server 3");

        // start running network communication
        toolBar.setStartActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                logger.info("Start server...");
                startNetwork();
            }
        });

        // stop running network communication
        toolBar.setStopActionListener( new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopNetwork();
            }
        });

        // create show log file action listener
        toolBar.setLogActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showLogFile();
            }
        });


        toolBar.setSettingsActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openConfiguration();
            }
        });

        toolBar.setExitActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exitProgram();
            }
        });


        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        WindowListener closeWindow = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        };
        addWindowListener(closeWindow);

        hookIntoLogging();

        this.configuration = new Configuration();
        this.setTitle(this.getTitle() + " " + configuration.getVersion());

        this.serverInfo = new ServerInfo(this);
        this.serverInfo.addObserver(this);

        this.clock = new Clock();
        this.clock.addObserver(serverInfo);

        initServerSocketThread();
    }

    public final void initServerSocketThread() {
        this.serverSocketThread = new ServerThread(this);
        this.serverSocketThread.addObserver(this);
        this.serverSocketThread.addObserver(serverInfo);
        this.serverThread = new Thread(this.serverSocketThread, "ServerThread");
        if (configuration.runOnWindowsStart() == Boolean.TRUE) {
            logger.info("init server socket 2.1");
            this.serverSocketThread.setServerActivityState(ServerActivityStates.START);
            serverThread.start();
            clock.start();
            if (serverSocketThread.getServerState() == ServerActivityStates.ERROR) {
                JOptionPane.showMessageDialog(Application.this, SOCKET_ALREADY_IN_USE,
                        "Error", JOptionPane.ERROR_MESSAGE);
                serverSocketThread.setServerActivityState(ServerActivityStates.STOPPED);
            } else {
                toolBar.setStartedEnabled();
                cellinkTable.startMonitoring();
            }
        } else {
            this.serverSocketThread.setServerActivityState(ServerActivityStates.IDLE);
            this.toolBar.doStopClick();
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
                    logger.info("Start server activity...");
                    serverSocketThread.setServerActivityState(ServerActivityStates.START);
                    if (!serverThread.isAlive()) {
                        serverThread.start();
                    }
                    if (!clock.isRunning()) {
                        clock.start();
                    }

                } finally {
                    if (serverSocketThread.getServerState() == ServerActivityStates.ERROR) {
                        JOptionPane.showMessageDialog(Application.this,
                                SOCKET_ALREADY_IN_USE, "Error", JOptionPane.ERROR_MESSAGE);
                        serverSocketThread.setServerActivityState(ServerActivityStates.STOPPED);
                    } else {
                        toolBar.setStartedEnabled();
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

                    toolBar.setStoppedEnabled();

                    serverSocketThread.setServerActivityState(ServerActivityStates.STOPPING);
                    serverSocketThread.shutdownServer();
                    cellinkTable.stopMonitoring();
                    if (clock.isRunning()) {
                        clock.stop();
                    }
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
        int result = JOptionPane.showConfirmDialog(Application.this, message, title, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            toolBar.doStopClick();
            if (serverThread != null && serverThread.isAlive()) {
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
                    setEnableToolBarButtons(false);
                } else if (state == ServerActivityStates.STOPPED) {
                    setEnableToolBarButtons(true);
                }
            }
        }
        pnlServer.setServerInfo(serverInfo);
    }

    private void setEnableToolBarButtons(boolean enable) {
        toolBar.setStoppedEnabled();
    }

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
                ApplicationUtil.restartApplication(Application.class);
            }
        } catch (RestartApplicationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
    * content of this method is always regenerated by the Form Editor.
            */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlServer = new ServerStatusPanel();
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

        toolBar = new ToolBar();

        pnlServer.setBorder(javax.swing.BorderFactory.createTitledBorder("Server Info"));

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
                                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        Map<Long, SocketThread> threads = serverSocketThread.getClientSessions().getSessions();
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
                JOptionPane.showMessageDialog(Application.this, sb.toString());
            } else {
                for (SocketThread nt : deadThreads) {
                    threads.remove(nt.getCellink().getId());
                    nt.getCommControl().close();
                    sb.append("Session with cellink id ").append(nt.getCellink().getId()).append(" closed\n ");
                }
                JOptionPane.showMessageDialog(Application.this, sb.toString());
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
                    new Application().setVisible(true);
                } catch (StartProgramException e) {
                    logger.error(e);
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ToolBar toolBar;
    private javax.swing.JButton btnCloseUnused;
    private javax.swing.JButton btnShowOpendSockets;
    private ServerStatusPanel pnlServer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane paneServer;
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