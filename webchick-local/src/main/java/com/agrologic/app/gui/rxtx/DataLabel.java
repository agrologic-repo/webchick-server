package com.agrologic.app.gui.rxtx;

import com.agrologic.app.model.rxtx.DataChangeEvent;
import com.agrologic.app.model.rxtx.DataChangeListener;

import javax.swing.*;

public class DataLabel extends JLabel implements DataChangeListener {

    public DataLabel(String text) {
        super(text);
    }

    @Override
    public void dataChanged(DataChangeEvent event) {
        setText("<html>" + event.getNewString() + "</html>");
    }

    public void setText(Long value) {
        String svalue = value.toString();
        setText(svalue);
        revalidate();
        repaint();
        invalidate();
    }
}