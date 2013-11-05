package com.agrologic.app.gui.rxtx;

import javax.swing.*;
import java.awt.*;

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

        setMaximumSize(new java.awt.Dimension(940, 25));
        setMinimumSize(new java.awt.Dimension(940, 25));
        setPreferredSize(new java.awt.Dimension(940, 25));
        setLayout(null);
        int x = 2;
        int y = 2;
        int w = 100;
        int h = 20;

        lblHouse.setFont(new Font("Arial", Font.BOLD, 13));
        lblHouse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblHouse);
        lblHouse.setBounds(x, y, w, h);

        x += w;
        w += 50;

        lblSendMsg.setFont(new Font("Arial", Font.BOLD, 13));
        lblSendMsg.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblSendMsg);
        lblSendMsg.setBounds(x, y, w, h);

        x += w;
        w += 200;

        lblReceiveMsg.setFont(new Font("Arial", Font.BOLD, 13));
        lblReceiveMsg.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblReceiveMsg);
        lblReceiveMsg.setBounds(x, y, w, h);

        x += w;
        w = 30;
        lblProgress.setFont(new Font("Arial", Font.BOLD, 13));
        lblProgress.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblProgress);
        lblProgress.setBounds(x, y, w, h);
        x += w;
        w = 500;

        lblControllerStatus.setFont(new Font("Arial", Font.BOLD, 13));
        lblControllerStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblControllerStatus);
        lblControllerStatus.setBounds(x, y, w, h);
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
