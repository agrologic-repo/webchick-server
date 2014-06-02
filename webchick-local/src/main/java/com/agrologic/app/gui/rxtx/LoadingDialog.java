package com.agrologic.app.gui.rxtx;

import com.agrologic.app.i18n.LocaleManager;
import com.agrologic.app.util.Windows;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class LoadingDialog extends JDialog {

    private JProgressBar progressBar = new JProgressBar(0, 500);


    public LoadingDialog(ApplicationLocal app) {
        super(app);
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        ResourceBundle bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // NOI18N
        setTitle(bundle.getString("dialog.loading.title"));
        add(BorderLayout.CENTER, progressBar);
        add(BorderLayout.NORTH, new JLabel(bundle.getString("dialog.loading.message")));
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
