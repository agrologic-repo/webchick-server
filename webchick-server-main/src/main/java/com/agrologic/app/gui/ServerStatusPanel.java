package com.agrologic.app.gui;

import com.agrologic.app.network.ServerInfo;

import javax.swing.*;

/**
 * Created by Valery on 12/03/2015.
 */
public class ServerStatusPanel extends JPanel {
    private javax.swing.JLabel lblOnOffTitle;
    private javax.swing.JLabel lblOnOff;
    private javax.swing.JLabel lblTimeOnTitle;
    private javax.swing.JLabel lblTimeOn;
    private javax.swing.JLabel lblAliveCellinkTitle;
    private javax.swing.JLabel lblConnectedCellinks;
    private javax.swing.JLabel lblTotCellinkTitle;
    private javax.swing.JLabel lblTotalCellinks;

    public ServerStatusPanel() {
        super();
        setBorder(javax.swing.BorderFactory.createTitledBorder("Server Info"));
        lblOnOffTitle = createLabel("On/Off", SwingConstants.LEFT);
        lblTimeOnTitle = createLabel("Time on", SwingConstants.LEFT);
        lblAliveCellinkTitle = createLabel("Alive cellinks", SwingConstants.LEFT);
        lblTotCellinkTitle = createLabel("Total cellinks", SwingConstants.LEFT);
        lblOnOff = createLabel("Stopped", SwingConstants.RIGHT);
        lblTimeOn = createLabel("00:00:00", SwingConstants.RIGHT);
        lblConnectedCellinks = createLabel("0", SwingConstants.RIGHT);
        lblTotalCellinks = createLabel("0", SwingConstants.RIGHT);


        javax.swing.GroupLayout pnlServerLayout = new javax.swing.GroupLayout(this);
        setLayout(pnlServerLayout);
        pnlServerLayout.setHorizontalGroup(
                pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlServerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblOnOffTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblTimeOnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblTotCellinkTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblAliveCellinkTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblTotalCellinks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblOnOff, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTimeOn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblConnectedCellinks, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnlServerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{lblOnOffTitle, lblTimeOnTitle, lblAliveCellinkTitle, lblTotCellinkTitle});

        pnlServerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{lblConnectedCellinks, lblOnOff, lblTimeOn, lblTotalCellinks});

        pnlServerLayout.setVerticalGroup(
                pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlServerLayout.createSequentialGroup()
                                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlServerLayout.createSequentialGroup()
                                                .addComponent(lblOnOffTitle)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTimeOnTitle)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblAliveCellinkTitle)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTotCellinkTitle))
                                        .addGroup(pnlServerLayout.createSequentialGroup()
                                                .addComponent(lblOnOff)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTimeOn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblConnectedCellinks, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTotalCellinks)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlServerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{lblConnectedCellinks, lblOnOff, lblTimeOn, lblTotalCellinks});

        pnlServerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{lblOnOffTitle, lblTimeOnTitle, lblAliveCellinkTitle, lblTotCellinkTitle});

    }

    public JLabel createLabel(String text, int orientation) {
        JLabel label = new JLabel();
        label.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label.setHorizontalAlignment(orientation);
        label.setText(text);
        return label;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        lblOnOff.setText(serverInfo.getServerStatus());
        lblTimeOn.setText(serverInfo.getTime());
        lblTotalCellinks.setText(serverInfo.getTotalCellinks().toString());
        lblConnectedCellinks.setText(serverInfo.getActiveCellinks().toString());
    }
}
