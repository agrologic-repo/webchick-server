
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.util;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Observable;

import javax.swing.Timer;

/**
 * Title: Clock <br> Decription: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class Clock extends Observable {
    boolean running;
    long    tick;
    Timer   timer;

    public Clock() {
        running = false;
        tick    = 0;
    }

    public void start() {
        tick  = 0;
        timer = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeTick();
                setChanged();
                notifyObservers(timer);
            }
        });
        timer.start();
        running = true;
    }

    public void stop() {
        running = false;
        timer.stop();
        timer = null;
    }

    public String getTime() {
        return this.toString();
    }

    public boolean isRunning() {
        return running;
    }

    private void timeTick() {
        tick++;
    }

    @Override
    public String toString() {
        long       tmp  = tick;
        final long hour = tmp / 3600;

        tmp = tmp % 3600;

        final long minute = tmp / 60;
        final long sec    = tmp % 60;

        return (((hour < 10)
                 ? "0"
                 : "") + hour + ":" + ((minute < 10)
                                       ? "0"
                                       : "") + minute + ":" + ((sec < 10)
                ? "0"
                : "") + sec);
    }
}



