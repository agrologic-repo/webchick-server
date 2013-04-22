/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

import help.examples.LoadingFrame;
import javax.swing.SwingWorker;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class SimulatedActivity extends SwingWorker<Void, Integer> {
    private LoadingFrame loading;
    /**
     * Constructs the simulated activity that increments a counter from 0 to a given target.
     *
     * @param t the target value of the counter.
     */
    public SimulatedActivity(LoadingFrame l) {
        loading = l;
    }

    protected Void doInBackground() throws Exception {
        loading.setVisible(true);
        return null;
    }

    protected void process() {

    }

    protected void done() {

    }
}
