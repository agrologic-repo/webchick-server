package com.agrologic.app.gui.rxtx;

import javax.swing.*;

public class StatusPanel extends JSeparator {

    private JLabel lblHouse;
    private JLabel lblReceiveMsg;
    private JLabel lblSendMsg;
    private JLabel lblProgress;
    private JLabel lblControllerStatus;

    public StatusPanel() {
        initComponents();
    }

    private void initComponents() {

        lblHouse = new JLabel();
        lblSendMsg = new JLabel();
        lblReceiveMsg = new JLabel();
        lblProgress = new JLabel();
        lblControllerStatus = new JLabel();

        setMaximumSize(new java.awt.Dimension(940, 20));
        setMinimumSize(new java.awt.Dimension(940, 20));
        setPreferredSize(new java.awt.Dimension(940, 20));
        setLayout(null);

        lblHouse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblHouse);
        lblHouse.setBounds(2, 2, 100, 17);

        lblSendMsg.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblSendMsg);
        lblSendMsg.setBounds(102, 2, 120, 17);

        lblReceiveMsg.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblReceiveMsg);
        lblReceiveMsg.setBounds(222, 2, 150, 17);

        lblProgress.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblProgress);
        lblProgress.setBounds(372, 2, 50, 17);

        lblControllerStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblControllerStatus);
        lblControllerStatus.setBounds(422, 2, 450, 17);
    }

    public void setSendMsg(String sendMsg) {
        lblSendMsg.setText(sendMsg);
    }

    public void setReceiveMsg(String receiveMsg) {
        lblReceiveMsg.setText(receiveMsg);
    }

    public void setProgress(String progress) {
        lblProgress.setText(progress);
    }

    public void setControllerStatus(String contrStatus) {
        lblControllerStatus.setText(contrStatus);
    }
}
