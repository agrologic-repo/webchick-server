package com.agrologic.app.network;

import com.agrologic.app.gui.ServerUI;
import com.agrologic.app.util.Clock;

import java.util.Observable;
import java.util.Observer;

public class ServerInfo extends Observable implements Observer {
    private boolean on;
    private int onlineCellinks;
    private ServerUI serverFacade;
    private String time;
    private int totalCellinks;

    public ServerInfo(ServerUI serverFacade) {
        this.serverFacade = serverFacade;
        onlineCellinks = serverFacade.getCellinkTable().onlineCellinks();
        totalCellinks = serverFacade.getCellinkTable().totalCellinks();
    }

    public boolean isOn() {
        return on;
    }

    public void setOn() {
        this.on = true;
    }

    public void setOff() {
        this.on = false;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        setChanged();
        notifyObservers(time);
    }

    public String getServerStatus() {
        return (isOn())
                ? "Running"
                : "Stopped";
    }

    public synchronized Integer getTotalCellinks() {
        return totalCellinks;
    }

    public synchronized Integer getActiveCellinks() {
        return onlineCellinks;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Clock) {
            setTime(((Clock) o).getTime());
            synchronized (this) {
                totalCellinks = serverFacade.getCellinkTable().totalCellinks();
                onlineCellinks = serverFacade.getCellinkTable().onlineCellinks();
            }
        }

        if (arg instanceof ServerActivityStates) {
            ServerActivityStates state = (ServerActivityStates) arg;

            if (state == ServerActivityStates.START) {
                setOn();
            } else if (state == ServerActivityStates.STOPPING) {
                setOff();
                setChanged();
                notifyObservers(this);
            }
        }
    }
}



