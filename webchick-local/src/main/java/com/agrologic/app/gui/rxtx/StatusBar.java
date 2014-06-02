package com.agrologic.app.gui.rxtx;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.config.Protocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

/**
 * Created by Valery on 11/05/14.
 */
public class StatusBar extends JPanel {
    private Timer timee;
    private JLabel welcomedate;
    private JLabel lblNetworkProtocolTitle;
    private JLabel lblNetworkProtocol;
    private JLabel lblReceiveMsg;
    private JLabel lblSendMsg;
    private JLabel lblProgress;
    private JLabel lblControllerStatus;
    private JPanel statusPanel;

    public StatusBar(ComponentOrientation componentOrientation) {
        super(new BorderLayout());
        setComponentOrientation(componentOrientation);

        Configuration configuration = new Configuration();
        Integer num = Integer.valueOf(configuration.getProtocol());

        lblNetworkProtocolTitle = createLabel();
        lblNetworkProtocol = new JLabel(Protocol.get(num).getText());
        lblNetworkProtocol.setComponentOrientation(getComponentOrientation());
        lblNetworkProtocol.setToolTipText("Network Protocol : " + lblNetworkProtocol.getText());
        lblNetworkProtocol.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        welcomedate = createLabel();
        lblSendMsg = createLabel();
        lblReceiveMsg = createLabel();
        lblProgress = createLabel();
        lblControllerStatus = createLabel();

        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setComponentOrientation(getComponentOrientation());
        statusPanel.add(lblSendMsg, BorderLayout.LINE_START);
        statusPanel.add(lblReceiveMsg, BorderLayout.CENTER);
        statusPanel.add(lblProgress, BorderLayout.LINE_END);

        add(lblNetworkProtocol, BorderLayout.LINE_START);
        add(statusPanel, BorderLayout.CENTER);
        add(lblControllerStatus, BorderLayout.LINE_END);


        //display date time to status bar
        timee = new javax.swing.Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.Date now = new java.util.Date();
                String ss = DateFormat.getDateTimeInstance().format(now);
                welcomedate.setText(ss);
                welcomedate.setToolTipText("Welcome, Today is " + ss);

            }
        });
        timee.start();
    }

    public JLabel createLabel() {
        JLabel label = new JLabel();
        label.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        label.setComponentOrientation(getComponentOrientation());
        return label;
    }

    public void setSendMsg(String sendMsg) {
        lblSendMsg.setText(sendMsg);
    }

    public void setReceiveMsg(String receiveMsg) {
        int size = receiveMsg.length() > 50 ? 50 : receiveMsg.length();
        lblReceiveMsg.setText(receiveMsg.substring(0, size));
    }

    public void setProgress(String progress) {
        lblProgress.setText(progress);
    }

    public void setControllerStatus(String contrStatus) {
        lblControllerStatus.setText(contrStatus);
    }
}
