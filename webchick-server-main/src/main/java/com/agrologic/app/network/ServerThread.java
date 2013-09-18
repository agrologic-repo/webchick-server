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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerThread extends Observable implements Runnable {
    public static final int SERVER_SOCKET_TIMEOUT = 5000;
    public static final int MAX_NUM_SOCKET = 512;
    private static final Logger logger = Logger.getLogger(ServerThread.class);
    private final CellinkDao cellinkDao;
    private static ClientSessions clientSessions;
    private ServerActivityStates currentState;
    private ServerSocket server;
    private ScheduledExecutorService executor;
    private Map<Long, SocketThread> threads;

    public ServerThread(ServerUI serverFacade) {
        cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.currentState = ServerActivityStates.IDLE;
        this.clientSessions = new ClientSessions(serverFacade, cellinkDao);
        this.threads = clientSessions.getSessions();

    }

    /**
     * Opening ServerSocket listener.
     *
     * @return true if opened successful, false otherwise
     */
    private boolean startServer() {
        try {
            Configuration configuration = new Configuration();
            InetAddress ia = InetAddress.getByName(configuration.getIp());
            Integer port = configuration.getPort();
            logger.info("Try to open server on port : " + port);
            server = new ServerSocket(port, MAX_NUM_SOCKET, ia);
            server.setSoTimeout(SERVER_SOCKET_TIMEOUT);

            executor = Executors.newScheduledThreadPool(2);
            executor.scheduleAtFixedRate(new SocketThreadStarter(this), 0L, 1L, TimeUnit.SECONDS);
            executor.scheduleAtFixedRate(new MonitorSocketThreadSize(), 0L, 1L, TimeUnit.SECONDS);

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

    @Override
    public void run() {
        ShutdownHook shutdownHook = new ShutdownHook(threads);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        boolean running = true;

        LOOP:
        while (running) {
            switch (getServerState()) {
                default:
                case IDLE:
                    try {
                        Thread.sleep(100);
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
                        SocketThread newThread = clientSessions
                                .createSessionWithClient(socket);
                        setChanged();
                        notifyObservers(newThread);
                    } catch (SocketException ex) {
                        logger.info("stop server, close server socket ");
                    } catch (SocketTimeoutException ex) {
                        logger.trace("socket timeout exception ", ex);
                    } catch (IOException ex) {
                        logger.trace("Error occurs while accepting new connection ", ex);
                    } catch (Exception ex) {
                        logger.trace("Unknown exception ", ex);
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
                    logger.info("Server is closed");
                    setServerActivityState(ServerActivityStates.IDLE);

                    break;

                case EXIT:
                    logger.info("Shutdown program");
                    shutdownServer();
                    running = false;
                    break;
            }

            if (!running) {
//                monitor.interrupt();

                break LOOP;
            }
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
        executor.shutdown();
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
     * @return ServerActivityStates the server activity state
     */
    public ServerActivityStates getServerState() {
        return currentState;
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
                    + snt.getCommControl().getSocket().toString());
        }
    }

    public Map<Long, SocketThread> getThreadList() {
        return threads;
    }

    public ClientSessions getClientSessions() {
        return clientSessions;
    }

    /**
     * Release and delete file lock.
     */
    @SuppressWarnings("PackageVisibleInnerClass")
    static class ShutdownHook extends Thread {

        Map<Long, SocketThread> threads;

        ShutdownHook(Map<Long, SocketThread> threads) {
            this.threads = threads;
        }

        @Override
        public void run() {
            CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);

            try {
                if (cellinkDao != null) {
                    logger.debug("Changing cellinks state to offline state...");
                    Collection<Cellink> cellinks = cellinkDao.getAll();
                    for (SocketThread thread : threads.values()) {
                        Cellink c = thread.getCellink();
                        c.setState(CellinkState.STATE_STOP);
                        cellinkDao.update(c);
                        threads.get(c.getId()).setThreadState(NetworkState.STATE_STOP);
                        threads.remove(c.getId());
                    }

                    for (Cellink c : cellinks) {
                        if (c.getCellinkState().getValue() != CellinkState.STATE_OFFLINE) {
                            c.setState(CellinkState.STATE_OFFLINE);
                            cellinkDao.update(c);
                        }
                        logger.debug("Cellink " + c + " offline now ");
                    }
                }
            } catch (SQLException e) {
                logger.debug(e);
            } catch (Exception e) {
                logger.debug(e);
            }
        }
    }

    class MonitorSocketThreadSize extends Thread {

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

}



