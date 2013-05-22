
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.util;


/**
 * Title: Windows.java <br> Description: <br> Copyright: Copyright © 2010 <br> Company: Agro Logic Ltd. ®<br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1.1 <br>
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Windows {

    /**
     * Center a Window, Frame, JFrame, Dialog, etc..
     *
     * @param window
     */
    public static void centerOnScreen(Window window) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        window.setLocation((d.width - window.getSize().width) / 2, (d.height - window.getSize().height) / 2);
    }

    public static void setWindowsLAF(Window window) {
        try {
            String laf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(window);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Set icon in left corner of form
     */
    public static void setIconImage(Window window, String iconName) {
        try {
            window.setIconImage(ImageIO.read(window.getClass().getResource(iconName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dimension screenResolution() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}



