/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class StatusPanel extends javax.swing.JSeparator {

    private javax.swing.JLabel lblHouse;
    private javax.swing.JLabel lblRecvMsg;
    private javax.swing.JLabel lblSendMsg;
    private javax.swing.JLabel lblProgress;
    private javax.swing.JLabel lblControllerStatus;

    public StatusPanel() {
        initComponents();
    }

    private void initComponents() {

        lblHouse = new javax.swing.JLabel();
        lblSendMsg = new javax.swing.JLabel();
        lblRecvMsg = new javax.swing.JLabel();
        lblProgress = new javax.swing.JLabel();
        lblControllerStatus = new javax.swing.JLabel();

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

        lblRecvMsg.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblRecvMsg);
        lblRecvMsg.setBounds(222, 2, 150, 17);

        lblProgress.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblProgress);
        lblProgress.setBounds(372, 2, 50, 17);

        lblControllerStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(lblControllerStatus);
        lblControllerStatus.setBounds(422, 2, 450, 17);
    }

    public void setHouse(String house) {
        lblHouse.setText(house);
    }

    public void setSendMsg(String sendMsg) {
        lblSendMsg.setText(sendMsg);
    }

    public void setRecvMsg(String recvMsg) {
        lblRecvMsg.setText(recvMsg);
    }

    public void setProgress(String progress) {
        lblProgress.setText(progress);
    }

    public void setControllerStatus(String contrStatus) {
        lblControllerStatus.setText(contrStatus);
    }

    public String getProgress() {
        return lblProgress.getText();
    }

    public String getControllerStatus() {
        return lblControllerStatus.getText();
    }
}
