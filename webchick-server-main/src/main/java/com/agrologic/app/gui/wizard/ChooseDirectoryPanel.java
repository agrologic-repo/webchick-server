
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui.wizard;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.UserDao;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ChooseDirectoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton           browseButton;
    private JLabel            chooseDirecotryLabel;
    private JPanel            contentPanel;
    private JLabel            errorMessageLabel;
    private JFileChooser      fileChooser;
    private ImageIcon         icon;
    private JLabel            iconLabel;
    private JPanel            jPanel1;
    private JLabel            pressNextLabel;
    private JTextField txtDirPath;
    private UserDao userDao;
    private JLabel  welcomeTitle;

    public ChooseDirectoryPanel() {
        iconLabel    = new JLabel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        icon = getImageIcon();
        setLayout(new BorderLayout());

        if (icon != null) {
            iconLabel.setIcon(icon);
        }

        iconLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        add(iconLabel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.EAST);

//      contentPanel.setPreferredSize(new Dimension(300, 250));
//      setPreferredSize(new Dimension(300, 250));
    }

    private JPanel getContentPanel() {
        java.awt.GridBagConstraints gridBagConstraints;
        JPanel                      contentPanel1 = new JPanel();

        jPanel1              = new javax.swing.JPanel();
        welcomeTitle         = new javax.swing.JLabel();
        chooseDirecotryLabel = new javax.swing.JLabel();
        txtDirPath           = new javax.swing.JTextField();
        browseButton         = new javax.swing.JButton();
        pressNextLabel       = new javax.swing.JLabel();
        errorMessageLabel    = new javax.swing.JLabel();

        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();

        jPanel1Layout.columnWidths = new int[] { 0, 5, 0 };
        jPanel1Layout.rowHeights   = new int[] {
            0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0
        };
        jPanel1.setLayout(jPanel1Layout);
        welcomeTitle.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
        welcomeTitle.setText("Welcome to create database wizard dialog");
        gridBagConstraints           = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipadx     = 119;
        gridBagConstraints.ipady     = 10;
        gridBagConstraints.anchor    = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets    = new java.awt.Insets(1, 2, 0, 0);
        jPanel1.add(welcomeTitle, gridBagConstraints);
        chooseDirecotryLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chooseDirecotryLabel.setText("Choose database directory");
        chooseDirecotryLabel.setMaximumSize(new java.awt.Dimension(96, 15));
        chooseDirecotryLabel.setMinimumSize(new java.awt.Dimension(96, 15));
        chooseDirecotryLabel.setPreferredSize(new java.awt.Dimension(96, 15));
        gridBagConstraints        = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 2;
        gridBagConstraints.fill   = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx  = 184;
        gridBagConstraints.ipady  = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 1, 1);
        jPanel1.add(chooseDirecotryLabel, gridBagConstraints);
        txtDirPath.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDirPath.setText(getDefaultDatabaseDir());
        txtDirPath.setPreferredSize(new Dimension(150, 15));
        gridBagConstraints        = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 4;
        gridBagConstraints.fill   = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx  = 200;
        gridBagConstraints.ipady  = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(txtDirPath, gridBagConstraints);
        pressNextLabel.setText("Press the 'Next' button to create database...");
        gridBagConstraints        = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 10;
        gridBagConstraints.fill   = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx  = 135;
        gridBagConstraints.ipady  = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 0);
        jPanel1.add(pressNextLabel, gridBagConstraints);
        errorMessageLabel.setForeground(java.awt.Color.red);
        errorMessageLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        errorMessageLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints           = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx     = 280;
        gridBagConstraints.ipady     = 20;
        gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
        jPanel1.add(errorMessageLabel, gridBagConstraints);
        browseButton.setText("Browse");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButtonActionPerformed(e);
            }
        });
        gridBagConstraints        = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx  = 2;
        gridBagConstraints.gridy  = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(browseButton, gridBagConstraints);
        jPanel1.setBounds(0, 0, 460, 310);
        contentPanel1.add(jPanel1);

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

    public String getDefaultDatabaseDir() {
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir   = userHomeDir + "\\collection";

        return System.getProperty("derby.system.home", systemDir);
    }

    public String getDatabaseDir() {
        return txtDirPath.getText();
    }

    public void browseButtonActionPerformed(ActionEvent evt) {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(ChooseDirectoryPanel.this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + fileChooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + fileChooser.getSelectedFile());
            txtDirPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        } else {
            System.out.println("No Selection ");
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
