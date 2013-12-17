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
    private JButton start;
    private JButton stop;
    private JButton settings;
    private JButton log;
    private JButton exit;
    private JButton dbwizard;


    private ToolBar() {
        super(SwingConstants.HORIZONTAL);
        setRollover(true);
        setFloatable(false);
        setToolTipText("Tool Bar");
        setBorderPainted(false);
        setMaximumSize(new java.awt.Dimension(250, 25));
        setMinimumSize(new java.awt.Dimension(250, 25));
        setPreferredSize(new java.awt.Dimension(250, 25));
        start = createButton("Start", ApplicationUtil.getIcon("play.png"));
        stop = createButton("Stop", ApplicationUtil.getIcon("stop.png"));
        settings = createButton("Settings", ApplicationUtil.getIcon("settings.png"));
        log = createButton("Log", ApplicationUtil.getIcon("note.png"));
        exit = createButton("Exit", ApplicationUtil.getIcon("logout.png"));
        dbwizard = createButton("Database Wizard", ApplicationUtil.getIcon("database.png"));

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

        start.addActionListener(actionListener);
    }

    public void setStopActionListener(ActionListener actionListener) {
        stop.addActionListener(actionListener);
    }

    public void setSettingsActionListener(ActionListener actionListener) {
        settings.addActionListener(actionListener);
    }

    public void setLogActionListener(ActionListener actionListener) {
        settings.addActionListener(actionListener);
    }


}