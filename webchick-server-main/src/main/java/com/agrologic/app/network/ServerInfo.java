
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.network;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.gui.ServerUI;

import com.agrologic.app.util.Clock;

//~--- JDK imports ------------------------------------------------------------

import java.util.Observable;
import java.util.Observer;

/**
 * Title: ServerInfo <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class ServerInfo extends Observable implements Observer {
    private String  ip;
    private Integer keepalive;
    private boolean on;
    private int     onlineCellinks;
    private Integer port;
    private ServerUI serverFacade;
    private String   time;
    private int      totalCellinks;

    public ServerInfo() {

//      this.cellinkDao = new CellinkDaoImpl();
//      //this.workers = new ArrayList<SessionThread>();
//      try {
//          cellinkList = cellinkDao.getAll();
//      } catch (SQLException ex) {
//          cellinkList = new ArrayList<Cellink>();
//      }
    }

    public ServerInfo(ServerUI serverFacade) {
        this.serverFacade = serverFacade;
        onlineCellinks    = serverFacade.getCellinkTable().onlineCellnks();
        totalCellinks     = serverFacade.getCellinkTable().totalCellnks();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
                totalCellinks  = serverFacade.getCellinkTable().totalCellnks();
                onlineCellinks = serverFacade.getCellinkTable().onlineCellnks();
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


//~ Formatted by Jindent --- http://www.jindent.com
