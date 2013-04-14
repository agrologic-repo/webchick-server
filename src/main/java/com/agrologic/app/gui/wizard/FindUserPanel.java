
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.wizard;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.UserDao;

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.User;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;

import java.net.URL;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;

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
public class FindUserPanel extends JPanel {

    public static final int FINISH_FIND = 100;
    private static final long serialVersionUID = 1L;
    private static final boolean USER_EXIST = true;
    private int findProccessProgress = 0;
    private boolean userExist = false;
    FindStates state = FindStates.USER_NOT_EXIST;
    private Configuration configuration;
    private CellinkDao cellinkDao;
    private volatile long cellinkId;
    private JComboBox cellinkIdComboBox;
    private HashMap<String, Long> cellinkMap;
    private JPanel contentPanel;
    private JLabel errorMessageLabel;
    private JButton findButton;
    private JComboBox languageComboBox;
    private JLabel chooseLangLabel;
    private ImageIcon icon;
    private JLabel iconLabel;
    private JLabel insertUserLabel;
    private JPanel jPanel1;
    private JLabel pressNextLabel;
    private JLabel selectCellinkLabel;
    private UserDao userDao;
    private long userId;
    private JTextField userIdText;
    private JLabel welcomeTitle;

    enum FindStates {

        USER_NOT_EXIST, CELLINK_CHOOSED, CELLINK_NOT_CHOOSED, NO_CELLINKS,
    };

    public FindUserPanel() {
        configuration = new Configuration();
        userDao = DaoFactory.getDaoFactory(DaoType.MYSQL).getUserDao();
        cellinkDao = DaoFactory.getDaoFactory(DaoType.MYSQL).getCellinkDao();
        cellinkMap = new HashMap<String, Long>();
        iconLabel = new JLabel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
        icon = getImageIcon();
        setLayout(new BorderLayout());

        if (icon != null) {
            iconLabel.setIcon(icon);
        }

        iconLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        add(iconLabel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.EAST);
        userIdText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (((e.getKeyChar() >= '0') && (e.getKeyChar() <= '9'))
                        || (e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    userIdText.setEditable(true);
                    errorMessageLabel.setText("");
                } else {
                    userIdText.setEditable(false);
                    errorMessageLabel.setForeground(Color.red);
                    errorMessageLabel.setText("Enter only numeric digits(0-9)");
                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                userIdText.setEditable(true);

                if (userIdText.getText().equals("")) {
                    findButton.setEnabled(false);
                } else {
                    findButton.setEnabled(true);
                }
            }
        });
        invalidate();
        findButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                findProccessProgress = 0;

                try {
                    userId = Long.parseLong(userIdText.getText());

                    User user = userDao.getById(userId);

                    if (user.getValidate() == USER_EXIST) {
                        userId = user.getId();
                        cellinkIdComboBox.removeAllItems();
                        cellinkIdComboBox.addItem("");
                        cellinkMap.put("", (long) -1);

                        List<Cellink> cellinks = (List<Cellink>) cellinkDao.getAllUserCellinks(userId);
                        for (Cellink cellink : cellinks) {
                            cellinkIdComboBox.addItem(cellink.getName());
                            cellinkMap.put(cellink.getName(), cellink.getId());
                        }

                        if (cellinks.isEmpty()) {
                            setFindState(FindStates.NO_CELLINKS);
                            updateMessageLabel();
                        } else {
                            setFindState(FindStates.CELLINK_NOT_CHOOSED);
                            updateMessageLabel();
                        }
                    } else {
                        setFindState(FindStates.USER_NOT_EXIST);
                        updateMessageLabel();
                    }
                } catch (SQLException ex) {
                    errorMessageLabel.setText("Database error occurded .");
                }

                findProccessProgress = FINISH_FIND;
            }
        });
        findButton.setEnabled(false);
        cellinkIdComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cmb = (JComboBox) e.getSource();
                if (cmb.getItemCount() > 0) {
                    String item = (String) cmb.getSelectedItem();
                    if (item.equals("")) {
                        cellinkId = -1;
                        setFindState(FindStates.CELLINK_NOT_CHOOSED);
                        updateMessageLabel();
                    } else {
                        setFindState(FindStates.CELLINK_CHOOSED);
                        updateMessageLabel();
                    }
                }
            }
        });
    }

    public void addTextChangedActionListener(KeyListener l) {
        userIdText.addKeyListener(l);
    }

    public void addFindUserActionListener(ActionListener l) {
        findButton.addActionListener(l);
    }

    public void addSelectCellinkActionListener(ActionListener l) {
        cellinkIdComboBox.addActionListener(l);
    }

    private JPanel getContentPanel() {
        JPanel contentPanel1 = new JPanel();
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel();
        welcomeTitle = new JLabel();
        insertUserLabel = new JLabel();
        userIdText = new JTextField();
        findButton = new JButton();
        pressNextLabel = new JLabel();
        errorMessageLabel = new JLabel();

        jPanel1.setLayout(new java.awt.GridBagLayout());
        welcomeTitle.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));    // NOI18N
        welcomeTitle.setText("Welcome to the Database Creating Wizard Dialog");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipadx = 119;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 0, 20);

        jPanel1.add(welcomeTitle, gridBagConstraints);
        insertUserLabel.setHorizontalAlignment(SwingConstants.LEFT);
        insertUserLabel.setText("Please Insert User ID :");
        insertUserLabel.setMaximumSize(new java.awt.Dimension(100, 15));
        insertUserLabel.setMinimumSize(new java.awt.Dimension(100, 15));
        insertUserLabel.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 20, 0, 0);
        jPanel1.add(insertUserLabel, gridBagConstraints);
        userIdText.setHorizontalAlignment(JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 4, 0, 0);
        userIdText.setPreferredSize(new Dimension(20, 20));
        jPanel1.add(userIdText, gridBagConstraints);
        findButton.setText("Find");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 8, 0, 0);
        jPanel1.add(findButton, gridBagConstraints);
        selectCellinkLabel = new JLabel("Select Cellink ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 240;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        jPanel1.add(selectCellinkLabel, gridBagConstraints);

        selectCellinkLabel.setVisible(false);
        cellinkIdComboBox = new JComboBox();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 0);
        cellinkIdComboBox.setPreferredSize(new Dimension(20, 20));
        jPanel1.add(cellinkIdComboBox, gridBagConstraints);
        cellinkIdComboBox.setVisible(false);

        pressNextLabel.setText("Press the 'Next' button to create database...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 136;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(150, 20, 15, 0);
        jPanel1.add(pressNextLabel, gridBagConstraints);

        errorMessageLabel.setForeground(java.awt.Color.red);
        errorMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        errorMessageLabel.setText("");
        errorMessageLabel.setPreferredSize(new Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 241;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        jPanel1.add(errorMessageLabel, gridBagConstraints);

        chooseLangLabel = new JLabel("Language : ");
        chooseLangLabel.setVisible(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 10;
        chooseLangLabel.setPreferredSize(new Dimension(80, 20));
        jPanel1.add(chooseLangLabel, gridBagConstraints);

        languageComboBox = new JComboBox();
        languageComboBox.setModel(new DefaultComboBoxModel(
                new String[]{"English", "Hebrew", "Chinese", "Russian", "French", "German"}));
        languageComboBox.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                String lang = (String) evt.getItem();
                configuration.setLanguage(lang);
                configuration.saveUpdatePreferences();
            }
        });
        String lang = configuration.getLanguage();
        languageComboBox.setSelectedItem(lang);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 90;
        languageComboBox.setPreferredSize(new Dimension(20, 20));

        jPanel1.add(languageComboBox, gridBagConstraints);

        contentPanel1.add(jPanel1, BorderLayout.CENTER);

        return contentPanel1;
    }

    private ImageIcon getImageIcon() {
        return new ImageIcon((URL) getResource("derby.jpg"));
    }

    private Object getResource(String key) {
        URL url = null;
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

    public boolean isTextEmpty() {
        return userIdText.getText().equals("");
    }

    public String getUserId() {
        return userIdText.getText();
    }

    public String getCellinkId() {
        String item = (String) cellinkIdComboBox.getSelectedItem();
        return cellinkMap.get(item).toString();
    }

    public synchronized int getFindProccessProgress() {
        return findProccessProgress;
    }

    public boolean isUserExist() {
        return userExist;
    }

    public boolean cellinkSelected() {
        String item = (String) cellinkIdComboBox.getSelectedItem();
        long value = cellinkMap.get(item);
        return value != -1;
    }

    public void setFindState(FindStates state) {
        this.state = state;
    }

    private void updateMessageLabel() {
        switch (state) {
            default:
            case NO_CELLINKS:
                userExist = true;
                setEnabledCellinkList(userExist);
                errorMessageLabel.setForeground(Color.red);
                errorMessageLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
                errorMessageLabel.setText("<html>User exist . But no cellinks found for this user .<br \\>"
                        + "Please check User ID.</html>");
                break;

            case USER_NOT_EXIST:
                userExist = false;
                setEnabledCellinkList(userExist);
                errorMessageLabel.setForeground(Color.red);
                errorMessageLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
                errorMessageLabel.setText("User does not exist ");
                break;

            case CELLINK_NOT_CHOOSED:
                userExist = true;
                setEnabledCellinkList(userExist);
                errorMessageLabel.setForeground(Color.blue);
                errorMessageLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
                errorMessageLabel.setText("User exist . Please select cellink from list ");
                break;

            case CELLINK_CHOOSED:
                userExist = true;
                setEnabledCellinkList(userExist);
                errorMessageLabel.setForeground(Color.blue);
                errorMessageLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
                errorMessageLabel.setText("Now you can create database ");
                break;
        }
    }

    private void setEnabledCellinkList(boolean result) {
        selectCellinkLabel.setVisible(result);
        cellinkIdComboBox.setVisible(result);
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
