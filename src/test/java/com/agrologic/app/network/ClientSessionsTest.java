package com.agrologic.app.network;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ClientSessionsTest {
    private CellinkDao cellinkDao = mock(CellinkDao.class);
    private ClientSessions sut = new ClientSessions(null, null, cellinkDao);

    @Test
    public void closeAllSessionsStopsRunningThread() throws Exception {
        SocketThread givenSession = sessionWithState(CellinkState.STATE_ONLINE);
        sut.getSessions().put(1L, givenSession);
        sut.closeAllSessions();

        assertTrue(givenSession.isStopThread());
    }

    @Test
    public void closeAllSessionsIsNoOpIfSessionAlreadyStopped() throws Exception {
        SocketThread givenSession = sessionWithState(CellinkState.STATE_ONLINE);
        givenSession.stopRunning();
        sut.getSessions().put(1L, givenSession);
        sut.closeAllSessions();

        assertTrue(givenSession.isStopThread());
    }

    @Test
    public void closeAllSessionsSavesChangesToDb() throws Exception {
        SocketThread givenSession = sessionWithState(CellinkState.STATE_ONLINE);
        sut.getSessions().put(1L, givenSession);
        sut.closeAllSessions();

        assertEquals(CellinkState.STATE_OFFLINE, givenSession.getCellink().getState());
        verify(cellinkDao).update(any(Cellink.class));
    }

    @Test
    public void closeAllSessionsShouldNotSaveIfStateWasOffline() throws Exception {
        sut.getSessions().put(1L, sessionWithState(CellinkState.STATE_OFFLINE));
        sut.closeAllSessions();
        verify(cellinkDao, never()).update(any(Cellink.class));
    }

    @Test
    public void closeAllSessionsDoesNothingIfNoSessions() throws Exception {
        sut.closeAllSessions();
    }

    private SocketThread sessionWithState(Integer state) {
        Cellink cellink = new Cellink();
        cellink.setId(1L);
        cellink.setState(state);
        return new SocketThread(cellink);
    }
}
