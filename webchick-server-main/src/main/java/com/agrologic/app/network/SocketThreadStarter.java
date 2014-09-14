package com.agrologic.app.network;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkCriteria;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Monitors the status of all units (cellink) by polling and set session state starting to start communication .
 * When cellink state equals to #CellinkState.STATE_START,  it means that the server already open session and putted
 * into ClientSession map . Thus by changing #SocketThread to #NetworkState.STATE_STARTING it start running.
 */
public class SocketThreadStarter implements Runnable {
    private final ServerThread serverThread;
    private final Logger logger = LoggerFactory.getLogger(SocketThreadStarter.class);
    private CellinkDao cellinkDao;
    private CellinkCriteria criteria;


    public SocketThreadStarter(ServerThread serverThread) {
        this.serverThread = serverThread;
        this.cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        this.criteria = new CellinkCriteria();
        this.criteria.setRole(UserRole.ADMIN.getValue());
        this.criteria.setState(CellinkState.STATE_START);
    }

    @Override
    public void run() {
        ClientSessions clientSessions = serverThread.getClientSessions();
        List<Cellink> cellinks = null;
        try {
            cellinks = (List<Cellink>) cellinkDao.getAll(criteria);
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        }
        for (Cellink cellink : cellinks) {

            int state = cellink.getState();
            if (state == CellinkState.STATE_START) {
                SocketThread socketThread = clientSessions.getSessions().get(cellink.getId());
                if (socketThread != null) {
                    socketThread.setThreadState(NetworkState.STATE_STARTING);
                    logger.info("Session with cellink ID {} started communication ", cellink.getId());
                } else {
                    cellink.setState(CellinkState.STATE_OFFLINE);
                    logger.info("Session state with cellink ID {} was set offline ", cellink.getId());
                }
            }
        }
    }
}