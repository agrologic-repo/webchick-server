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

/**
 * Created by Valery on 4/29/14.
 */
public class DataPasswordField extends JPasswordField implements DataChangeListener {
    private volatile boolean locked = false;
    private long controllerId;
    private Data data;
    private DatabaseAccessor dbaccess;
    private DataPasswordField nextTextField;

    public DataPasswordField(String text, long cid, Data data, DatabaseAccessor dbaccess) {
        super(text);
        setListeners();
        this.controllerId = cid;
        this.data = data;
        this.dbaccess = dbaccess;
    }

    public DataPasswordField getNextTextField() {
        return nextTextField;
    }

    public void setNextTextField(DataPasswordField nextTextField) {
        this.nextTextField = nextTextField;
    }

    private void setListeners() {
        super.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof DataPasswordField) {
                    locked = true;
                    DataPasswordField textField = (DataPasswordField) e.getSource();
                    textField.selectAll();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() instanceof DataPasswordField) {
                    DataPasswordField oldTextField = (DataPasswordField) e.getSource();
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
                        DataPasswordField txt = (DataPasswordField) e.getSource();
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
                DataPasswordField txt = (DataPasswordField) e.getSource();
                String string = DataFormatUtil.fixDecPoint(txt.getText(), data.getFormat());
                txt.setText(string);
                char c = e.getKeyChar();    // Get the typed character

                // Don't ignore backspace or delete
                if (c == KeyEvent.VK_ENTER) {
                    string = DataFormatUtil.clearDelimiter(string);
                    long value = Long.parseLong(string);
                    try {
                        data.setValueFromUI(value);
                        value = data.getValue();
                        dbaccess.getControllerDao().sendNewDataValueToController(controllerId, data.getId(), value);
                        dbaccess.getControllerDao().saveNewDataValueOnController(controllerId, data.getId(), value);
                        if (getNextTextField() != null) {
                            getNextTextField().requestFocus();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
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
