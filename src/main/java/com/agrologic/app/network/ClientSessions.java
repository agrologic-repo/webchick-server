package com.agrologic.app.network;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSessions {
    private final Configuration configuration;
    private final Map<Long, SocketThread> sessions = new ConcurrentHashMap<Long, SocketThread>();
    private final CellinkDao cellinkDao;
    private final ServerUI serverFacade;

    public ClientSessions(Configuration configuration, ServerUI serverFacade, CellinkDao cellinkDao) {
        this.configuration = configuration;
        this.serverFacade = serverFacade;
        this.cellinkDao = cellinkDao;
    }

    public SocketThread createSessionWithClient(Socket socket) throws IOException {
        SocketThread newThread = new SocketThread(sessions, socket, configuration);
        newThread.setServreFacade(serverFacade);
        newThread.setName("SocketThread-" + socket.getInetAddress());
        newThread.start();
        return newThread;
    }

    public void closeAllSessions() throws SQLException {
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

    public Map<Long, SocketThread> getSessions() {
        return sessions;
    }
}
