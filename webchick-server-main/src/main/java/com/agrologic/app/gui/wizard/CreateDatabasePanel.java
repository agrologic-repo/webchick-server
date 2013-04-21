package com.agrologic.app.gui.wizard;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class CreateDatabasePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel            anotherBlankSpace;
    private JLabel            blankSpace;
    private JPanel            contentPanel;
    private ImageIcon         icon;
    private JLabel            iconLabel;
    private JLabel            jLabel1;
    private JPanel            jPanel1;
    private JLabel            progressDescription;
    private JProgressBar      progressSent;
    private JLabel            welcomeTitle;
    private JLabel            yetAnotherBlankSpace1;

    public CreateDatabasePanel() {
        super();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        contentPanel.getBounds().setLocation(0, 0);
        iconLabel = new JLabel();
        icon      = getImageIcon();
        setLayout(new BorderLayout());
        if (icon != null) {
            iconLabel.setIcon(icon);
        }
        iconLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        add(iconLabel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setProgressText(String s) {
        progressDescription.setText(s);
    }

    public void setProgressValue(int i) {
        progressSent.setValue(i);
    }

    private JPanel getContentPanel() {
        JPanel contentPanel1 = new JPanel();

        welcomeTitle = new JLabel();
        welcomeTitle.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
        welcomeTitle.setText("Start Creating Database ");
        welcomeTitle.setPreferredSize(new Dimension(250, 15));

        jPanel1             = new JPanel();
        progressSent        = new JProgressBar();
        progressDescription = new JLabel();
        jLabel1             = new JLabel();

        jPanel1.setLayout(new java.awt.BorderLayout());
        welcomeTitle.setText("Now we will create database...");
        jPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);
        jPanel1.setLayout(new java.awt.GridLayout(0, 1));
        blankSpace = new JLabel();
        jPanel1.add(blankSpace);
        progressSent.setStringPainted(true);
        jPanel1.add(progressSent);
        progressDescription.setFont(new java.awt.Font("MS Sans Serif", Font.PLAIN, 11));
        progressDescription.setText("Creating database...");
        jPanel1.add(progressDescription);
        anotherBlankSpace = new JLabel();
        jPanel1.add(anotherBlankSpace);
        yetAnotherBlankSpace1 = new JLabel();
        jPanel1.add(yetAnotherBlankSpace1);
        jLabel1.setText("After the creating is completed, the Finish buttons will enable below.");
        jPanel1.add(jLabel1, java.awt.BorderLayout.SOUTH);
        contentPanel1.add(jPanel1, BorderLayout.CENTER);
        return contentPanel1;
    }

    private ImageIcon getImageIcon() {
        return new ImageIcon((URL) getResource("derby.jpg"));
    }

    private Object getResource(String key) {
        URL    url  = null;
        String name = key;

        if (name != null) {
            try {
                Class c = Class.forName("com.agrologic.app.gui.wizard.WizardRunner");
                url = c.getResource(name);
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Unable to find Main class");
            }

            return url;
        } else {
            return null;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
