/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.model.DataFormat;
import com.agrologic.app.util.DataFormatUtil;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class FlockGUIUtil {

    public static void keyPressedHandler(JTextField textField, KeyEvent event) {
        String string = DataFormatUtil.fixDecPoint(textField.getText(), DataFormat.DEC_0);
        textField.setText(string);
        char c = event.getKeyChar();
        // Get the typed character
        // Don't ignore backspace or delete
        textField.requestFocus();
    }

    public static void keyPressedHandler(JTextField textField, KeyEvent event, int format) {
        String string = DataFormatUtil.fixDecPoint(textField.getText(), format);
        textField.setText(string);
        char c = event.getKeyChar();
        // Get the typed character
        // Don't ignore backspace or delete
        textField.requestFocus();
    }

    public static void keyTypedHandler(JTextField textField, KeyEvent event) {
        char c = event.getKeyChar();        // Get the typed character
        // Don't ignore backspace or delete
        if ((c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_DELETE)) {
            // If the key was not a number then discard it
            // (this is a sloppy way to check)
            if (!(Character.isDigit(c))) {
                String text = textField.getText();
                if (DataFormatUtil.isDelimiterChar(c)) {
                    if (DataFormatUtil.isDelimiterExist(text)) {
                        event.consume();    // Ignore this key
                    }
                } else {
                    event.consume();        // Ignore this key
                }
            }
        }
    }
}
