package com.agrologic.app.network;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
@Ignore
public class ClientSessionsTest {
    private CellinkDao cellinkDao = mock(CellinkDao.class);

    @Test
    public void closeDuplicateSessionShouldDoNothingIfNoDuplicate() {
        ClientSessions sut = createSut(sessionWithId(1L));
        sut.closeDuplicateSession(sessionWithId(2L));
        assertEquals(1, sut.getSessions().size());
    }

    @Test
    public void closeAllSessionsStopsRunningThread() throws Exception {
        SocketThread givenSession = sessionWithState(CellinkState.STATE_ONLINE);
        ClientSessions sut = createSut(givenSession);
        sut.closeAllSessions();

        assertTrue(givenSession.isStopThread());
    }

    @Test
    public void closeAllSessionsIsNoOpIfSessionAlreadyStopped() throws Exception {
        SocketThread givenSession = sessionWithState(CellinkState.STATE_ONLINE);
        givenSession.stopRunning();
        ClientSessions sut = createSut(givenSession);
        sut.closeAllSessions();

        assertTrue(givenSession.isStopThread());
    }

    @Test
    public void closeAllSessionsSavesChangesToDb() throws Exception {
        SocketThread givenSession = sessionWithState(CellinkState.STATE_ONLINE);
        ClientSessions sut = createSut(givenSession);
        sut.closeAllSessions();

        assertEquals(CellinkState.STATE_OFFLINE, givenSession.getCellink().getState());
        verify(cellinkDao).update(any(Cellink.class));
    }

    @Test
    public void closeAllSessionsShouldNotSaveIfStateWasOffline() throws Exception {
        ClientSessions sut = createSut(sessionWithState(CellinkState.STATE_OFFLINE));
        sut.closeAllSessions();
        verify(cellinkDao, never()).update(any(Cellink.class));
    }

    @Test
    public void closeAllSessionsDoesNothingIfNoSessions() throws Exception {
        createSut().closeAllSessions();
    }

    private ClientSessions createSut(SocketThread... sessions) {
        ClientSessions clientSessions = new ClientSessions(null, null, cellinkDao);
        for (SocketThread session : sessions) {
            clientSessions.getSessions().put(session.getCellink().getId(), session);
        }
        return clientSessions;
    }

    private SocketThread sessionWithId(Long id) {
        Cellink cellink = new Cellink();
        cellink.setId(id);
        return new SocketThread(cellink);
    }

    private SocketThread sessionWithState(Integer state) {
        Cellink cellink = new Cellink();
        cellink.setId(1L);
        cellink.setState(state);
        return new SocketThread(cellink);
    }

    private SocketThread session(Long id, Integer state) {
        Cellink cellink = new Cellink();
        cellink.setId(id);
        cellink.setState(state);

        return new SocketThread(cellink);
    }
}
