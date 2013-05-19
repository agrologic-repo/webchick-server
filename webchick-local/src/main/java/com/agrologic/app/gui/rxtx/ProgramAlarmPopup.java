package com.agrologic.app.gui.rxtx;

import com.agrologic.app.model.ProgramAlarm;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProgramAlarmPopup extends JDialog {
    private JPanel alarmsPanel;
    private JButton button;
    private JPanel buttonPanel;
    private JPanel contentPanel;
    private Point location;
    private List<ProgramAlarm> programAlarms;

    public ProgramAlarmPopup(Point location, List<ProgramAlarm> pa) {
        super();
        this.location = location;
        this.programAlarms = pa;
        initComponents();
    }

    private void initComponents() {
        setUndecorated(true);
        setLayout(new BorderLayout());
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new LineBorder(Color.blue, 1));
        button = createCloseButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(button);

        alarmsPanel = new JPanel(new GridLayout(programAlarms.size() + 1, 1));
        alarmsPanel.add(new JLabel("<html><p></p></html>"));

        for (ProgramAlarm pa : programAlarms) {
            JLabel label = new JLabel("<html>    " + pa.getDigitNumber() + " - " + pa.getText() + "</html>");
            alarmsPanel.add(label);
        }

        int width = 50;
        int height = (programAlarms.size() + 1) * 20;
        if (location == null) {
            setBounds(40, 80, width * 5, height);
        } else {
            setBounds(location.x, location.y, width * 5, height);
        }

        contentPanel.add(buttonPanel, BorderLayout.PAGE_START);
        contentPanel.add(alarmsPanel, BorderLayout.PAGE_END);
        add(contentPanel);
    }
//
//    private static JButton createHelpButton() {
//        final JButton button = new JButton();
//        button.setIcon(new javax.swing.ImageIcon(button.getClass().getResource("/help/examples/help.gif")));
//        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
//        button.setOpaque(false);
//        return button;
//    }

    private static JButton createCloseButton() {
        final JButton button = new JButton();
        button.setIcon(new javax.swing.ImageIcon(button.getClass().getResource("/icons/close.png")));
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setOpaque(false);
        return button;
    }
}
