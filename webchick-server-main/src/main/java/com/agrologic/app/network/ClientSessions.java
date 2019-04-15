package com.agrologic.app.network;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controls sessions between our server and clients.
 */
public class ClientSessions {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Long, SocketThread> sessions = new ConcurrentHashMap<Long, SocketThread>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final CellinkDao cellinkDao;
    private final ServerUI server;

    public ClientSessions(ServerUI server, CellinkDao cellinkDao) {
        this.server = server;
        this.cellinkDao = cellinkDao;
    }

    public synchronized SocketThread createSessionWithClient(Socket socket, int counterMRP) throws IOException {
        SocketThread newThread = new SocketThread(this, socket, counterMRP);
        newThread.setServerFacade(server);
        threadPool.execute(newThread);
        return newThread;
    }

    public synchronized void closeDuplicateSession(SocketThread session) {
        SocketThread duplicateSession = sessions.get(session.getCellink().getId());
        if (duplicateSession != null && !session.getCommControl().equals(duplicateSession.getCommControl())) {
            logger.info("Closing duplicating session [{}]", session.getCellink().getId());
            duplicateSession.stopRunning();//TODO: cover with tests
        }
    }

    public synchronized void closeAllSessions() throws SQLException {
        logger.info("Closing and stopping all sessions");
        for (Map.Entry<Long, SocketThread> sessionEntry : sessions.entrySet()) {
            SocketThread session = sessionEntry.getValue();
            session.stopRunning();
            Cellink cellink = session.getCellink();
            if (cellink.getCellinkState().getValue() != CellinkState.STATE_OFFLINE) {
                cellink.setState(CellinkState.STATE_OFFLINE);
                cellinkDao.update(cellink);
            }
        }
    }

    public synchronized Map<Long, SocketThread> getSessions() {
        return sessions;
    }

    public synchronized void openSession(Long id, SocketThread socketThread) {
        sessions.put(id, socketThread);
    }

    public synchronized void closeSession(Long id) {
        sessions.remove(id);
    }
}
