package com.agrologic.app.gui;

import com.agrologic.app.util.ApplicationUtil;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Valery
 * Date: 10/31/13
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ToolBar extends JToolBar {
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnSettings;
    private JButton btnLogFile;
    private JButton btnExit;
    private JButton btnWizardDB;


    public ToolBar() {
        super(SwingConstants.HORIZONTAL);
        setRollover(true);
        setFloatable(false);
        setToolTipText("Tool Bar");
        setBorderPainted(false);
        setMaximumSize(new java.awt.Dimension(250, 25));
        setMinimumSize(new java.awt.Dimension(250, 25));
        setPreferredSize(new java.awt.Dimension(250, 25));
        // create buttons
        btnStart = createButton("Start", ApplicationUtil.getIcon("play.png"));
        add(btnStart);
        btnStop = createButton("Stop", ApplicationUtil.getIcon("stop.png"));
        add(btnStop);
        btnSettings = createButton("Settings", ApplicationUtil.getIcon("settings.png"));
        add(btnSettings);
        btnLogFile = createButton("Log", ApplicationUtil.getIcon("note.gif"));
        add(btnLogFile);
        btnExit = createButton("Exit", ApplicationUtil.getIcon("logout.png"));
        add(btnExit);
        btnWizardDB = createButton("Database Wizard", ApplicationUtil.getIcon("database.png"));
        add(btnWizardDB);

    }

    /**
     * Create toolbar button
     *
     * @param toolTip the tool tip string
     * @param icon    the icon name
     * @return button the created button
     */
    private JButton createButton(String toolTip, ImageIcon icon) {
        JButton button = new JButton();
        button.setIcon(icon); // NOI18N
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setMaximumSize(new java.awt.Dimension(32, 32));
        button.setMinimumSize(new java.awt.Dimension(32, 32));
        button.setPreferredSize(new java.awt.Dimension(32, 32));
        button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        return button;
    }

    public void setStartActionListener(ActionListener actionListener) {

        btnStart.addActionListener(actionListener);
    }

    public void setStopActionListener(ActionListener actionListener) {
        btnStop.addActionListener(actionListener);
    }

    public void setSettingsActionListener(ActionListener actionListener) {
        btnSettings.addActionListener(actionListener);
    }

    public void setLogActionListener(ActionListener actionListener) {
        btnLogFile.addActionListener(actionListener);
    }

    public void setExitActionListener(ActionListener actionListener) {
        btnExit.addActionListener(actionListener);
    }

    public void doStopClick() {
        btnStop.doClick();
    }

    public void setStartedEnabled() {
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }

    public void setStoppedEnabled() {
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

    }
}