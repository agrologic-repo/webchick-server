
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.mappers.DataFormatUtil;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.rxtx.DataChangeEvent;
import com.agrologic.app.model.rxtx.DataChangeListener;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

public class DataTextField extends JTextField implements DataChangeListener {
    private volatile boolean locked = false;
    private long controllerId;
    private Data data;
    private DatabaseAccessor dbaccess;

    public DataTextField(String text, long cid, Data d, DatabaseAccessor dbaccess) {
        super(text);
        setListneners();
        this.controllerId = cid;
        this.data = d;
        this.dbaccess = dbaccess;
    }

    private void setListneners() {
        super.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof DataTextField) {
                    locked = true;
                    DataTextField textField = (DataTextField) e.getSource();
                    textField.selectAll();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() instanceof DataTextField) {
                    DataTextField oldTextField = (DataTextField) e.getSource();
                    oldTextField.setSelectionStart(0);
                    oldTextField.setSelectionEnd(0);
                    locked = false;
                }
            }
        });
        super.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();        // Get the typed character
                // Don't ignore backspace or delete
                if ((c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_DELETE)) {

                    // If the key was not a number then discard it
                    // (this is a sloppy way to check)
                    if (!(Character.isDigit(c))) {
                        DataTextField txt = (DataTextField) e.getSource();
                        if (DataFormatUtil.isDelimiterChar(c)) {
                            if (DataFormatUtil.isDelimiterExist(txt.getText())) {
                                e.consume();    // Ignore this key
                            }
                        } else {
                            e.consume();        // Ignore this key
                        }
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                DataTextField txt = (DataTextField) e.getSource();
                String string = DataFormatUtil.fixDecPoint(txt.getText(), data.getFormat());

                txt.setText(string);

                char c = e.getKeyChar();    // Get the typed character

                // Don't ignore backspace or delete
                if (c == KeyEvent.VK_ENTER) {
                    string = DataFormatUtil.clearDelimiter(string);

                    long value = Long.parseLong(string);

                    try {
                        data.setValueToChange(value);
                        value = data.getValue();
                        dbaccess.getControllerDao().sendNewDataValueToController(controllerId, data.getId(), value);
                        dbaccess.getControllerDao().saveNewDataValueOnController(controllerId, data.getId(), value);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
//                    txt.setBackground(Color.ORANGE);
                    txt.getParent().requestFocus();
                }
            }
        });
    }

    @Override
    public void dataChanged(DataChangeEvent event) {
        if (!locked) {
            super.setText(event.getNewString());
            super.revalidate();
            super.repaint();
            super.invalidate();
        }
    }
}
