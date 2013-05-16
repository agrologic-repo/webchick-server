/*
 * SettingDialog.java
 *
 * Created on Jul 6, 2009, 1:20:58 PM
 */
package com.agrologic.app.gui;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.config.Protocol;
import com.agrologic.app.util.SpringUtilities;
import com.agrologic.app.util.Windows;
import gnu.io.CommPortIdentifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Server setting dialog is an observer object on ServerPreferences object.
 *
 * @author Valery Manakhimov
 */
public class ConfigurationDialog extends JDialog implements Observer {

    private static final long serialVersionUID = 1L;
    private final Configuration configuration;
    private boolean result = false;
    public static final int CANCEL = 0;
    public static final int OK = 1;

    public ConfigurationDialog() {
        this(null, true);
    }

    /**
     * Creates new form SettingDialog
     */
    public ConfigurationDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        Windows.setWindowsLAF(ConfigurationDialog.this);
        Windows.centerOnScreen(ConfigurationDialog.this);
        this.configuration = new Configuration();
        configuration.addObserver(ConfigurationDialog.this);
        // create and add action listener for database settings
        ActionListener showDataBaseSetting = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (showDataBaseDialog() == true) {
                    btnApply.setEnabled(true);
                    btnOk.setEnabled(true);
                    configuration.setDBConfig(
                            txtDBDriver.getText(),
                            txtDBUrl.getText(),
                            txtDBUser.getText(),
                            pwdDBPassword.getText());
                }
            }
        };
        btnDBSetting.addActionListener(showDataBaseSetting);

        // create and add action listener for ip and port
        ActionListener showNetworkSetting = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (showNetworkDialog() == true) {
                    btnApply.setEnabled(true);
                    btnOk.setEnabled(true);
                    Integer port = Integer.parseInt(txtPort.getText());
                    configuration.setNetworkConfig(txtIP.getText(), port);
                }
            }
        };
        btnTCPServer.addActionListener(showNetworkSetting);
        reloadForm();
    }

    /**
     * Reload all setting fields to this dialog.
     */
    public void reloadForm() {
        txtPort.setText(configuration.getPort().toString());
        txtIP.setText(configuration.getIp());
        txtCOMPort.setText(configuration.getComPort());
        spnSot.setValue(Integer.valueOf(configuration.getSotDelay()));
        spnEot.setValue(Integer.valueOf(configuration.getEotDelay()));
        spnNext.setValue(Integer.valueOf(configuration.getNextDelay()));
        spnErrFaults.setValue(Integer.valueOf(configuration.getMaxErrors()));
        txtDBDriver.setText(configuration.getDbDriver());
        txtDBUrl.setText(configuration.getDbUrl());
        txtDBUser.setText(configuration.getDbUser());
        pwdDBPassword.setText(configuration.getDbPassword());
        spnMinute.setValue(configuration.getKeepalive());

        if (configuration.runOnWindowsStart() == true) {
            chboxStartupWithWindows.setSelected(true);
        } else {
            chboxStartupWithWindows.setSelected(false);
        }
        txtUserID.setText(configuration.getUserId());
        txtCellinkID.setText(configuration.getCellinkId());

        String protocol = configuration.getProtocol();
        cmbBaud.setSelectedItem(protocol);

        String lang = configuration.getLanguage();
        cmbLanguage.setSelectedItem(lang);

        this.validate();
    }

    /**
     * Show database setting
     *
     * @return true when changes was made, otherwise false
     */
    public boolean showDataBaseDialog() {
        JLabel[] lbl = {new JLabel("Driver :", SwingConstants.TRAILING),
                new JLabel("URL :", SwingConstants.TRAILING),
                new JLabel("Username :", SwingConstants.TRAILING),
                new JLabel("Password :", SwingConstants.TRAILING)};
        int numPairs = lbl.length;

        JTextField[] txt = {new JTextField(25), new JTextField(25), new JTextField(25), new JTextField(25)};
        txt[0].setText(txtDBDriver.getText());
        txt[1].setText(txtDBUrl.getText());
        txt[2].setText(txtDBUser.getText());
        txt[3].setText(pwdDBPassword.getText());
        //Create and populate the panel.
        JPanel panel = new JPanel(new SpringLayout());
        for (int i = 0; i < numPairs; i++) {
            panel.add(lbl[i]);
            panel.add(txt[i]);
        }
        //Lay out the panel.
        SpringUtilities.makeCompactGrid(panel,
                numPairs, 2, //rows, cols
                6, 6, //initX, initY
                6, 6);       //xPad, yPad

        Object[] options = {"Save", "Cancel"};
        //JOptionPane.showConfirmDialog(null, panel);
        int answer = JOptionPane.showOptionDialog(panel,
                panel,
                "Data Base Setting",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[1]); //default button title
        if (answer == JOptionPane.YES_OPTION) {
            txtDBDriver.setText(txt[0].getText());
            txtDBUrl.setText(txt[1].getText());
            txtDBUser.setText(txt[2].getText());
            pwdDBPassword.setText(txt[3].getText());


            JOptionPane.showMessageDialog(panel, "DataBase setting successfully saved ! \n"
                    + txtDBDriver.getText() + "\n"
                    + txtDBUrl.getText() + "\n"
                    + txtDBUser.getText() + "\n"
                    + pwdDBPassword.getText());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Show network setting
     *
     * @return true when changes was made, otherwise false
     */
    public boolean showNetworkDialog() {
        JLabel[] lbl = {new JLabel("Server ip :", SwingConstants.TRAILING),
                new JLabel("Server port :", SwingConstants.TRAILING)};
        int numPairs = lbl.length;

        JTextField[] txt = {new JTextField(25), new JTextField(25), new JTextField(25), new JTextField(25)};
        txt[0].setText(txtIP.getText());
        txt[1].setText(txtPort.getText());
        //Create and populate the panel.
        JPanel panel = new JPanel(new SpringLayout());
        for (int i = 0; i < numPairs; i++) {
            panel.add(lbl[i]);
            panel.add(txt[i]);
        }
        //Lay out the panel.
        SpringUtilities.makeCompactGrid(panel,
                numPairs, 2, //rows, cols
                6, 6, //initX, initY
                6, 6);       //xPad, yPad

        Object[] options = {"Save", "Cancel"};
        //JOptionPane.showConfirmDialog(null, panel);
        int answer = JOptionPane.showOptionDialog(panel,
                panel,
                "Network Setting",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[1]); //default button title
        if (answer == JOptionPane.YES_OPTION) {
            txtIP.setText(txt[0].getText());
            txtPort.setText(txt[1].getText());

            JOptionPane.showMessageDialog(panel, "Network setting successfully saved ! \n"
                    + txtIP.getText() + "\n"
                    + txtPort.getText() + "\n");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reload all dialog fields when data changed in ServerPreferenses
     *
     * @param o   the ServerPreferenses object
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        reloadForm();
    }

    public boolean showDialog() {
        configuration.reInitPreferences();
        btnApply.setEnabled(false);
        btnOk.setEnabled(false);
        super.show();
        return result;
    }

    public String showSelectComportDialog() {
        String[] comPortTitles;
        // get available comports
        Enumeration<CommPortIdentifier> enm = CommPortIdentifier.getPortIdentifiers();
        List<String> comPortTitlesVector = new ArrayList<String>();
        while (enm.hasMoreElements()) {
            String comPortName = enm.nextElement().getName();
            if (comPortName.indexOf("COM") != -1) {
                comPortTitlesVector.add(comPortName);
            }
        }
        // Create comport titles array for input dialog
        int len = comPortTitlesVector.size();
        comPortTitles = new String[len];
        for (int i = 0; i < len; i++) {
            comPortTitles[i] = comPortTitlesVector.get(i);
        }

        String choosed = (String) JOptionPane.showInputDialog(null,
                "Select communication port",
                "Select communication port", JOptionPane.QUESTION_MESSAGE,
                null, comPortTitles, "Select COM");
        return choosed;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        pnlServerStartup = new javax.swing.JPanel();
        chboxStartupWithWindows = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        cmbLanguage = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        spnSot = new javax.swing.JSpinner();
        spnEot = new javax.swing.JSpinner();
        spnNext = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        txtCellinkID = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtUserID = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        spnErrFaults = new javax.swing.JSpinner();
        pnlServerConfig1 = new javax.swing.JPanel();
        txtCOMPort = new javax.swing.JTextField();
        btnComport = new javax.swing.JButton();
        lblIP = new javax.swing.JLabel();
        txtIP = new javax.swing.JLabel();
        btnTCPServer = new javax.swing.JButton();
        lblPort = new javax.swing.JLabel();
        txtPort = new javax.swing.JLabel();
        rdoComport = new javax.swing.JRadioButton();
        rdoTCPServer = new javax.swing.JRadioButton();
        pnlDBConfig = new javax.swing.JPanel();
        lblURL = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        btnDBSetting = new javax.swing.JButton();
        pwdDBPassword = new javax.swing.JPasswordField();
        lblDriver = new javax.swing.JLabel();
        txtDBDriver = new javax.swing.JLabel();
        txtDBUrl = new javax.swing.JLabel();
        txtDBUser = new javax.swing.JLabel();
        lblKeepAlive = new javax.swing.JLabel();
        lblMinute = new javax.swing.JLabel();
        spnMinute = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        lblPCComm = new javax.swing.JLabel();
        cmbBaud = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuration Dialog");
        setResizable(false);

        pnlServerStartup.setBorder(javax.swing.BorderFactory.createTitledBorder(" Startup"));
        pnlServerStartup.setLayout(null);

        chboxStartupWithWindows.setText("Start Server with Windows");
        chboxStartupWithWindows.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCheckBox chbox = (JCheckBox) evt.getSource();
                if (chbox.isSelected() == false) {
                    configuration.setStartup(false);
                } else {
                    configuration.setStartup(true);
                }
                btnApply.setEnabled(true);
                btnOk.setEnabled(true);
            }
        });
        pnlServerStartup.add(chboxStartupWithWindows);
        chboxStartupWithWindows.setBounds(30, 60, 160, 23);

        jLabel1.setText("Choose Language");
        pnlServerStartup.add(jLabel1);
        jLabel1.setBounds(30, 30, 100, 20);

        cmbLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"English", "Hebrew", "Chinese", "Russian", "French"}));
        cmbLanguage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbLanguageItemStateChanged(evt);
            }
        });
        pnlServerStartup.add(cmbLanguage);
        cmbLanguage.setBounds(140, 30, 100, 20);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = false;
                setVisible(false);
            }
        });

        btnOk.setText("OK");

        btnApply.setText("Apply");
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                configuration.saveUpdatePreferences();
                btnApply.setEnabled(false);
                result = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Synchronization"));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("SOT Delay (ms)");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("EOT Delay (ms)");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("NEXT Delay (ms)");

        spnSot.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(100)));
        spnSot.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnSotStateChanged(evt);
            }
        });

        spnEot.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(100)));
        spnEot.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnEotStateChanged(evt);
            }
        });

        spnNext.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(100)));
        spnNext.setMaximumSize(new java.awt.Dimension(30, 20));
        spnNext.setMinimumSize(new java.awt.Dimension(30, 20));
        spnNext.setPreferredSize(new java.awt.Dimension(30, 20));
        spnNext.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnNextStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(spnNext, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                        .addComponent(spnSot)
                                        .addComponent(spnEot))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{jLabel10, jLabel11, jLabel9});

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(spnSot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spnEot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Set IDs"));

        txtCellinkID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCellinkIDFocusLost(evt);
            }
        });
        txtCellinkID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCellinkIDKeyReleased(evt);
            }
        });

        jLabel15.setText("Farm ID");

        jLabel16.setText("User ID");

        txtUserID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUserIDFocusLost(evt);
            }
        });
        txtUserID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserIDKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtUserID, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                        .addComponent(txtCellinkID))
                                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtCellinkID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Real Time"));

        jLabel17.setText("No of Faults");

        spnErrFaults.setModel(new javax.swing.SpinnerNumberModel());
        spnErrFaults.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnErrFaultsStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnErrFaults, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spnErrFaults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlServerConfig1.setBorder(javax.swing.BorderFactory.createTitledBorder("Communication"));
        pnlServerConfig1.setLayout(null);

        txtCOMPort.setEditable(false);
        txtCOMPort.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtCOMPort.setText("COM1");
        pnlServerConfig1.add(txtCOMPort);
        txtCOMPort.setBounds(140, 50, 40, 20);

        btnComport.setText("Select COM");
        btnComport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String comport = showSelectComportDialog();
                if (comport != null) {
                    txtCOMPort.setText(comport);
                    configuration.setComPort(comport);
                    btnApply.setEnabled(true);
                    btnOk.setEnabled(true);
                }
            }
        });
        pnlServerConfig1.add(btnComport);
        btnComport.setBounds(30, 50, 90, 23);

        lblIP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblIP.setText("IP :");
        pnlServerConfig1.add(lblIP);
        lblIP.setBounds(40, 140, 35, 20);

        txtIP.setText("123.123.123.123");
        pnlServerConfig1.add(txtIP);
        txtIP.setBounds(80, 140, 100, 20);

        btnTCPServer.setText("Change");
        btnTCPServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // serverSetting.show();
            }
        });
        pnlServerConfig1.add(btnTCPServer);
        btnTCPServer.setBounds(260, 140, 80, 25);

        lblPort.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPort.setText("Port :");
        pnlServerConfig1.add(lblPort);
        lblPort.setBounds(40, 160, 35, 20);

        txtPort.setText("12345");
        pnlServerConfig1.add(txtPort);
        txtPort.setBounds(80, 160, 100, 20);

        buttonGroup1.add(rdoComport);
        rdoComport.setText("Comunication Port");
        rdoComport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoComportActionPerformed(evt);
            }
        });
        pnlServerConfig1.add(rdoComport);
        rdoComport.setBounds(10, 20, 120, 23);

        buttonGroup1.add(rdoTCPServer);
        rdoTCPServer.setText("TCP Server");
        rdoTCPServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTCPServerActionPerformed(evt);
            }
        });
        pnlServerConfig1.add(rdoTCPServer);
        rdoTCPServer.setBounds(10, 120, 100, 23);

        pnlDBConfig.setBorder(javax.swing.BorderFactory.createTitledBorder("Database"));
        pnlDBConfig.setLayout(null);

        lblURL.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblURL.setText("URL :");
        pnlDBConfig.add(lblURL);
        lblURL.setBounds(15, 40, 50, 20);

        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUser.setText("User :");
        pnlDBConfig.add(lblUser);
        lblUser.setBounds(15, 60, 50, 20);

        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPassword.setText("Password :");
        pnlDBConfig.add(lblPassword);
        lblPassword.setBounds(15, 80, 60, 20);

        btnDBSetting.setText("Change");
        btnDBSetting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //dbSetting.show();
            }
        });
        pnlDBConfig.add(btnDBSetting);
        btnDBSetting.setBounds(240, 70, 80, 25);

        pwdDBPassword.setEditable(false);
        pwdDBPassword.setText("valery");
        pwdDBPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        pnlDBConfig.add(pwdDBPassword);
        pwdDBPassword.setBounds(80, 80, 120, 20);

        lblDriver.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDriver.setText("Driver :");
        pnlDBConfig.add(lblDriver);
        lblDriver.setBounds(15, 20, 40, 20);

        txtDBDriver.setText("driver");
        pnlDBConfig.add(txtDBDriver);
        txtDBDriver.setBounds(80, 20, 240, 20);

        txtDBUrl.setText("database");
        pnlDBConfig.add(txtDBUrl);
        txtDBUrl.setBounds(80, 40, 240, 20);

        txtDBUser.setText("username");
        pnlDBConfig.add(txtDBUser);
        txtDBUser.setBounds(80, 60, 120, 20);

        pnlServerConfig1.add(pnlDBConfig);
        pnlDBConfig.setBounds(20, 210, 350, 110);

        lblKeepAlive.setText("  Keep alive timeout for cellink");
        pnlServerConfig1.add(lblKeepAlive);
        lblKeepAlive.setBounds(30, 190, 150, 17);

        lblMinute.setText("minutes");
        pnlServerConfig1.add(lblMinute);
        lblMinute.setBounds(260, 180, 43, 17);

        spnMinute.setModel(new javax.swing.SpinnerNumberModel(1, 1, 3, 1));
        spnMinute.setMinimumSize(new java.awt.Dimension(30, 20));
        spnMinute.setPreferredSize(new java.awt.Dimension(30, 20));
        spnMinute.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                Integer value = (Integer) ((JSpinner) evt.getSource()).getValue();
                if (value >= 1 && value <= 3) {
                    configuration.setKeepalive(value);
                } else {
                    JOptionPane.showMessageDialog(null, "Timeout must be between 1 and 3 minute!",
                            "Server Setting",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                Integer value = (Integer) ((JSpinner) evt.getSource()).getValue();
                if (value >= 1 && value <= 3) {
                    configuration.setKeepalive(value);
                } else {
                    JOptionPane.showMessageDialog(null, "Timeout must be between 1 and 3 minute!",
                            "Server Setting",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        spnMinute.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                Integer value = (Integer) ((JSpinner) evt.getSource()).getValue();
                configuration.setKeepalive(value);
            }
        });
        pnlServerConfig1.add(spnMinute);
        spnMinute.setBounds(200, 190, 50, 20);
        pnlServerConfig1.add(jSeparator1);
        jSeparator1.setBounds(20, 110, 350, 10);

        lblPCComm.setText("Text 2400 bps");
        pnlServerConfig1.add(lblPCComm);
        lblPCComm.setBounds(40, 80, 78, 20);

        cmbBaud.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"0", "1", "2", "3"}));
        cmbBaud.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBaudItemStateChanged(evt);
            }
        });
        pnlServerConfig1.add(cmbBaud);
        cmbBaud.setBounds(140, 80, 40, 20);

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.saveUpdatePreferences();
                result = true;
                setVisible(false);
                //System.exit(0);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 62, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlServerConfig1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(pnlServerStartup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlServerConfig1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlServerStartup, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnOk)
                                        .addComponent(btnCancel)
                                        .addComponent(btnApply))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void spnSotStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnSotStateChanged
        configuration.setSotDelay(spnSot.getValue().toString());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_spnSotStateChanged

    private void spnEotStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnEotStateChanged
        configuration.setEotDelay((String) spnEot.getValue().toString());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_spnEotStateChanged

    private void spnNextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnNextStateChanged
        configuration.setNextDelay((String) spnNext.getValue().toString());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_spnNextStateChanged

    private void txtCellinkIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCellinkIDFocusLost
        Long cellinkId = (txtCellinkID.getText().equals("") == true ? 0 : Long.parseLong(txtCellinkID.getText()));
        configuration.setCellinkId(txtCellinkID.getText());
    }//GEN-LAST:event_txtCellinkIDFocusLost

    private void txtCellinkIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCellinkIDKeyReleased
        configuration.setCellinkId(txtCellinkID.getText());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_txtCellinkIDKeyReleased

    private void txtUserIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUserIDFocusLost
        Long userId = (txtUserID.getText().equals("") == true ? 0 : Long.parseLong(txtUserID.getText()));
    }//GEN-LAST:event_txtUserIDFocusLost

    private void txtUserIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserIDKeyReleased
        configuration.setUserId(txtUserID.getText());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_txtUserIDKeyReleased

    private void spnErrFaultsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnErrFaultsStateChanged
        configuration.setMaxError((String) spnErrFaults.getValue().toString());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_spnErrFaultsStateChanged

    private void cmbBaudItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBaudItemStateChanged
        String baud = (String) evt.getItem();
        //configuration.setPCCom(baud);
        configuration.setProtocol(baud);
        Integer num = Integer.valueOf(baud);
        lblPCComm.setText(Protocol.get(num).getText());
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_cmbBaudItemStateChanged

    private void rdoComportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoComportActionPerformed
        boolean enabled = true;
        cmbBaud.setEnabled(enabled);
        btnComport.setEnabled(enabled);
        lblPCComm.setEnabled(enabled);
        txtCOMPort.setEnabled(enabled);
        lblIP.setEnabled(!enabled);
        lblPort.setEnabled(!enabled);
        txtIP.setEnabled(!enabled);
        txtPort.setEnabled(!enabled);
        btnTCPServer.setEnabled(!enabled);
        lblKeepAlive.setEnabled(!enabled);
        spnMinute.setEnabled(!enabled);
        lblMinute.setEnabled(!enabled);
        pnlDBConfig.setEnabled(!enabled);
        lblDriver.setEnabled(!enabled);
        lblURL.setEnabled(!enabled);
        lblUser.setEnabled(!enabled);
        lblPassword.setEnabled(!enabled);
        txtDBDriver.setEnabled(!enabled);
        txtDBUrl.setEnabled(!enabled);
        txtDBUser.setEnabled(!enabled);
        pwdDBPassword.setEnabled(!enabled);
        btnDBSetting.setEnabled(!enabled);
    }//GEN-LAST:event_rdoComportActionPerformed

    private void rdoTCPServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTCPServerActionPerformed
        boolean enabled = true;
        cmbBaud.setEnabled(!enabled);
        btnComport.setEnabled(!enabled);
        lblPCComm.setEnabled(false);
        txtCOMPort.setEnabled(!enabled);
        lblIP.setEnabled(enabled);
        lblPort.setEnabled(enabled);
        txtIP.setEnabled(enabled);
        txtPort.setEnabled(enabled);
        btnTCPServer.setEnabled(enabled);
        lblKeepAlive.setEnabled(enabled);
        spnMinute.setEnabled(enabled);
        lblMinute.setEnabled(enabled);
        pnlDBConfig.setEnabled(enabled);
        lblDriver.setEnabled(enabled);
        lblURL.setEnabled(enabled);
        lblUser.setEnabled(enabled);
        lblPassword.setEnabled(enabled);
        txtDBDriver.setEnabled(enabled);
        txtDBUrl.setEnabled(enabled);
        txtDBUser.setEnabled(enabled);
        pwdDBPassword.setEnabled(enabled);
        btnDBSetting.setEnabled(enabled);
    }//GEN-LAST:event_rdoTCPServerActionPerformed

    private void cmbLanguageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbLanguageItemStateChanged
        String lang = (String) evt.getItem();
        configuration.setLanguage(lang);
        btnApply.setEnabled(true);
        btnOk.setEnabled(true);
    }//GEN-LAST:event_cmbLanguageItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnComport;
    private javax.swing.JButton btnDBSetting;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnTCPServer;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chboxStartupWithWindows;
    private javax.swing.JComboBox cmbBaud;
    private javax.swing.JComboBox cmbLanguage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDriver;
    private javax.swing.JLabel lblIP;
    private javax.swing.JLabel lblKeepAlive;
    private javax.swing.JLabel lblMinute;
    private javax.swing.JLabel lblPCComm;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblURL;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlDBConfig;
    private javax.swing.JPanel pnlServerConfig1;
    private javax.swing.JPanel pnlServerStartup;
    private javax.swing.JPasswordField pwdDBPassword;
    private javax.swing.JRadioButton rdoComport;
    private javax.swing.JRadioButton rdoTCPServer;
    private javax.swing.JSpinner spnEot;
    private javax.swing.JSpinner spnErrFaults;
    private javax.swing.JSpinner spnMinute;
    private javax.swing.JSpinner spnNext;
    private javax.swing.JSpinner spnSot;
    private javax.swing.JTextField txtCOMPort;
    private javax.swing.JTextField txtCellinkID;
    private javax.swing.JLabel txtDBDriver;
    private javax.swing.JLabel txtDBUrl;
    private javax.swing.JLabel txtDBUser;
    private javax.swing.JLabel txtIP;
    private javax.swing.JLabel txtPort;
    private javax.swing.JTextField txtUserID;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
//        new ConfigurationDialog(null, true, null);
    }
}