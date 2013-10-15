package com.agrologic.app.web;

import java.io.Serializable;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class TaskBean implements Runnable, Serializable {
    private int counter;
    private boolean running;
    private int sleep;
    private boolean started;
    private int sum;

    public TaskBean() {
        counter = 0;
        sum = 0;
        started = false;
        running = false;
        sleep = 100;
    }

    protected void work() {
        try {
            Thread.sleep(sleep);
            counter++;
            sum += counter;
        } catch (InterruptedException e) {
            setRunning(false);
        }
    }

    public synchronized int getPercent() {
        return counter;
    }

    public synchronized boolean isStarted() {
        return started;
    }

    public synchronized boolean isCompleted() {
        return counter == 100;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;

        if (running) {
            started = true;
        }
    }

    public synchronized Object getResult() {
        if (isCompleted()) {
            return new Integer(sum);
        } else {
            return null;
        }
    }

    public void run() {
        try {
            setRunning(true);

            while (isRunning() && !isCompleted()) {
                work();
            }
        } finally {
            setRunning(false);
        }
    }
}



