package com.agrologic.app.gui.rxtx;

import com.agrologic.app.util.Windows;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {

    private JProgressBar progressBar = new JProgressBar(0, 500);

    public LoadingDialog(ApplicationLocal app) {
        super(app,"Progress Dialog");
        add(BorderLayout.CENTER, progressBar);
        add(BorderLayout.NORTH, new JLabel("Progress..."));
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(300, 75);
        Windows.centerOnScreen(this);
        setVisible(true);

    }

    public int getProgressValue() {
        return progressBar.getValue();
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }
}
