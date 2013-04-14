/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app;

import com.agrologic.app.except.StartProgramException;
import com.agrologic.app.gui.WCSWindow;
import com.agrologic.app.gui.wizard.WizardRunner;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class Main {

    public static PorgamType runType;

    public static void main(String[] args) {
        runType = PorgamType.APPSERVER;

        boolean restart;
        do {
            restart = new Main().launch();
        } while (restart);
    }

    private static boolean launch() {
        boolean result = false;
        switch (runType) {
            case APPSERVER:
                Logger logger = Logger.getRootLogger();
                try {
                    logger.info("strat server");
                    new WCSWindow().setVisible(true);
                } catch (StartProgramException e) {
                    logger.error(e);
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
                result = true;
                break;
            case DERBYWIZARD:
                WizardRunner wr = new WizardRunner(null);
                try {
                    wr.call();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
        }
        return result;
    }
}
