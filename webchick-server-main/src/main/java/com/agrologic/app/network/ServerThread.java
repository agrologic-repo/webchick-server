package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.util.ApplicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);
    private static ClientSessions clientSessions;
    private final CellinkDao cellinkDao;
    private ScheduledExecutorService executor;
    private ServerActivityStates currentState;
    private ServerSocket server;

    public ServerThread(ServerUI serverFacade) {
        cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.currentState = ServerActivityStates.IDLE;
        this.clientSessions = new ClientSessions(serverFacade, cellinkDao);
    }

    /**
     * Opening ServerSocket listener
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
            logger.info("ServerSocket opened on " + server.getLocalSocketAddress());
            return true;
        } catch (BindException ex) {
            logger.error("Error opening server.", ex);
            return false;
        } catch (IOException ex) {
            logger.error("Error opening server.", ex);
            return false;
        }
    }

    @Override
    public void run() {
        ShutdownHook shutdownHook = new ShutdownHook(clientSessions.getSessions());
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        boolean running = true;

        LOOP:
        while (running) {
            switch (getServerState()) {
                default:
                case IDLE:
                    ApplicationUtil.sleep(100);
                    break;

                case START:
                    logger.info("Start Server, please wait...");
                    if (startServer() == true) {
                        setServerActivityState(ServerActivityStates.RUNNING);
                        logger.info("Running");
                    } else {
                        setServerActivityState(ServerActivityStates.ERROR);
                        logger.error("Failed");
                    }

                    break;

                case RUNNING:
                    try {
                        //start client session
                        Socket socket = server.accept();
                        SocketThread newThread = clientSessions.createSessionWithClient(socket);
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
                break LOOP;
            }
        }
    }

    /**
     * Close ServerSocket
     */
    public void shutdownServer() {

        // change cellink state to offline
        Logger log = LoggerFactory.getLogger(ServerThread.class);

        if ((server != null) && !server.isClosed()) {
            try {
                if (cellinkDao != null) {
                    log.debug("Changing cellinks state to offline state...");
                    clientSessions.closeAllSessions();
                }
                logger.info("Close ServerSocket.");
                server.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        Iterator<Entry<Long, SocketThread>> entries = clientSessions.getSessions().entrySet().iterator();

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
                logger.debug(e.getMessage(), e);
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }
}



