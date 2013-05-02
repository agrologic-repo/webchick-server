package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

/**
 * Title: ServerSocketThread <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class ServerThread extends Observable implements Runnable {
    public static final int MAX_NUM_SOCKET = 512;
    private final Logger logger = Logger.getLogger(ServerThread.class);
    private final CellinkDao cellinkDao;
    private final ClientSessions clientSessions;
    private ServerActivityStates currentState;
    private ServerSocket server;
    private Configuration configuration;
    private Map<Long, SocketThread> threads;

    public ServerThread(Configuration serverPref, ServerUI serverFacade) {
        cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.configuration = serverPref;
        this.currentState = ServerActivityStates.IDLE;
        this.clientSessions = new ClientSessions(configuration, serverFacade, cellinkDao);
        this.threads = clientSessions.getSessions();
    }

    @Override
    public void run() {
        ShutdownHook shutdownHook = new ShutdownHook(threads);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        MonitorThread monitor = new MonitorThread();
        monitor.start();
        boolean running = true;

        LOOP:
        while (running) {
            switch (getServerState()) {
                default:
                case IDLE:
                    try {
                        wait(100);
                    } catch (Exception ex) {

                    }

                    break;

                case START:
                    logger.info("Start Server, please wait...");

                    if (startServer() == true) {
                        setServerActivityState(ServerActivityStates.RUNNING);
                        logger.info("Running");
                    } else {
                        setServerActivityState(ServerActivityStates.ERROR);
                        logger.fatal("Failed");
                    }

                    break;

                case RUNNING:
                    try {
                        Socket socket = server.accept();//start client session
                        SocketThread newThread = clientSessions.createSessionWithClient(socket);
                        // notify observers
                        setChanged();
                        notifyObservers(newThread);
                    } catch (SocketException ex) {
                        logger.info("stop server, close server socket.");
                    } catch (IOException ex) {
                        logger.fatal("Error occurs while accepting new connection.", ex);
                    } catch (Error err) {
                        err.printStackTrace();
                    }

                    break;

                case STOPPING:
                    shutdownServer();
                    setServerActivityState(ServerActivityStates.STOPPED);

                    break;

                case ERROR:
                    try {
                        wait(100);
                    } catch (Exception ex) {
                        // ignore

                    }

                    break;

                case STOPPED:
                    logger.info("Server is closed.");
                    setServerActivityState(ServerActivityStates.IDLE);

                    break;

                case EXIT:
                    logger.info("Shutdown program.");
                    shutdownServer();
                    running = false;
                    break;
            }

            if (!running) {
                monitor.interrupt();

                break LOOP;
            }
        }
    }

    /**
     * Opening ServerSocket listener.
     *
     * @return true if opened successful, false otherwise
     */
    private boolean startServer() {
        try {
            InetAddress ia = InetAddress.getByName(configuration.getIp());
            Integer port = configuration.getPort();
            logger.info("Try to open server on port : " + port);
            server = new ServerSocket(port, MAX_NUM_SOCKET, ia);
            logger.info("ServerSocket opened on " + server.getLocalSocketAddress());
            return true;
        } catch (BindException ex) {
            logger.error("Error opening server.");
            logger.fatal(ex);

            return false;
        } catch (IOException ex) {
            logger.error("Error opening server.", ex);

            return false;
        }
    }

    /**
     * Close ServerSocket
     */
    public void shutdownServer() {

        // change cellink state to offline
        Logger log = Logger.getLogger(ServerThread.class);

        if ((server != null) && !server.isClosed()) {
            try {
                if (cellinkDao != null) {
                    log.debug("Changing cellinks state to offline state...");
                    clientSessions.closeAllSessions();
                }
                log.debug("Cellinks state are offline now!");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                logger.info("Trying to close ServerSocket.");
                server.close();
            } catch (IOException ex) {
                logger.error("Error closing ServerSocket. ", ex);
            }
        }

        threads.clear();
    }

    /**
     * Notify about changed server state
     *
     * @param serverState the server state
     */
    public void setServerActivityState(ServerActivityStates serverState) {
        currentState = serverState;
        setChanged();
        notifyObservers(serverState);
    }

    /**
     * Notify about new connection.
     *
     * @param worker
     */
    public void newConnection(SocketThread worker) {
        setChanged();
        notifyObservers(worker);
    }

    /**
     * @return ServerActivityStates the server activity state
     */
    public ServerActivityStates getServerState() {
        return currentState;
    }

    /**
     * Release and delete file lock.
     */
    public static void setCellinkOffline(Map<Long, SocketThread> threads) {

        // change cellink state to offline
        Logger log = Logger.getLogger(ServerThread.class);
        CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);

        try {
            if (cellinkDao != null) {
                log.debug("Changing cellinks state to offline state...");

                Collection<Cellink> cellinks = cellinkDao.getAll();
                Iterator<SocketThread> iter = threads.values().iterator();

                while (iter.hasNext()) {
                    SocketThread thread = iter.next();
                    Cellink c = thread.getCellink();
                    CellinkState state = c.getCellinkState();

                    switch (state.getValue()) {
                        case CellinkState.STATE_START:
                            c.setState(CellinkState.STATE_STOP);
                            threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                            threads.remove(c.getId());

                            break;

                        case CellinkState.STATE_RUNNING:
                            c.setState(CellinkState.STATE_STOP);
                            threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                            threads.remove(c.getId());

                            break;

                        case CellinkState.STATE_ONLINE:
                            c.setState(CellinkState.STATE_STOP);
                            threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                            threads.remove(c.getId());

                            break;

                        case CellinkState.STATE_RESTART:
                            c.setState(CellinkState.STATE_STOP);
                            threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                            threads.remove(c.getId());

                            break;

                        case CellinkState.STATE_UNKNOWN:
                            c.setState(CellinkState.STATE_STOP);
                            threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                            threads.remove(c.getId());

                            break;

                        case CellinkState.STATE_STOP:
                            c.setState(CellinkState.STATE_STOP);
                            threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                            threads.remove(c.getId());

                            break;

                        default:
                            break;
                    }

                    cellinkDao.update(c);
                }

                for (Cellink c : cellinks) {
                    if (c.getCellinkState().getValue() != CellinkState.STATE_OFFLINE) {
                        c.setState(CellinkState.STATE_OFFLINE);
                        cellinkDao.update(c);
                    }

                    log.debug("Cellink " + c + " offline now ");
                }

                log.debug("Cellinks state are offline now!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showThreadList() {
        Iterator<Entry<Long, SocketThread>> entries = threads.entrySet().iterator();

        if (entries.hasNext() == false) {
            logger.debug("The thread list is empty !");

            return;
        }

        while (entries.hasNext()) {
            Entry<Long, SocketThread> entry = entries.next();
            Long cid = entry.getKey();
            SocketThread snt = entry.getValue();

            logger.debug("The cellink id : " + cid + " the socket opened : "
                    + snt.getComControl().getSocket().toString());
        }
    }

    public void clearThreadList() {
        Iterator<Entry<Long, SocketThread>> entries = threads.entrySet().iterator();

        while (entries.hasNext()) {
            Entry<Long, SocketThread> entry = entries.next();
            Long cid = entry.getKey();
            SocketThread snt = entry.getValue();
            NetworkState state = snt.getThreadState();

            switch (state) {
                case STATE_ABORT:
                    break;
            }

            logger.debug("The cellink id : " + cid + " the socket opened : "
                    + snt.getComControl().getSocket().toString());
        }
    }

    public Map<Long, SocketThread> getThreadList() {
        return threads;
    }

    class MonitorThread extends Thread {

        @Override
        public void run() {
            int size = threads.size();

            while (!Thread.currentThread().interrupted()) {
                if (getServerState() == ServerActivityStates.RUNNING) {
                    if (size != threads.size()) {
                        logger.debug("Current thread in system =  : " + threads.size());
                        size = threads.size();
                    }
                }
            }
        }
    }

    @SuppressWarnings("PackageVisibleInnerClass")
    static class ShutdownHook extends Thread {

        Map<Long, SocketThread> threads;

        ShutdownHook(Map<Long, SocketThread> threads) {
            this.threads = threads;
        }

        @Override
        public void run() {
            setCellinkOffline(threads);
        }
    }
}



