package com.agrologic.app.gui.rxtx;

import com.agrologic.app.model.ProgramAlarm;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProgramAlarmPopup extends JDialog {
    private JButton button;
    private JPanel contentPanel;
    private Point location;
    private List<ProgramAlarm> programAlarms;

    /**
     * @param location
     * @param pa
     */
    public ProgramAlarmPopup(Point location, List<ProgramAlarm> pa) {
        super();
        this.location = location;
        this.programAlarms = pa;
        initComponents();
    }

    private void initComponents() {
        setUndecorated(true);
        setLayout(new BorderLayout());

        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new LineBorder(Color.blue, 1));

        GridBagConstraints c = new GridBagConstraints();
        int x = 0;
        int y = 0;
        JLabel alarm = new JLabel("Alarms", JLabel.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = x++;
        c.gridy = 0;
        c.weightx = 0.5;
        contentPanel.add(alarm, c);

        button = createCloseButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c.gridx = x++;
        c.gridy = y++;
        c.weightx = 0.0;
        contentPanel.add(button, c);

        JLabel label;
        for (ProgramAlarm pa : programAlarms) {
            label = new JLabel("<html>" + pa.getDigitNumber() + " - " + pa.getText() + "</html>");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridx = 0;
            c.gridy = y++;
            c.insets = new Insets(1, 2, 2, 1);  // padding
            c.gridwidth = 2;
            contentPanel.add(label, c);
        }

        int width = 50;
        int height = (programAlarms.size() + 1) * 20;
        if (location == null) {
            contentPanel.setBounds(40, 80, width * 5, height);
        } else {
            contentPanel.setBounds(location.x, location.y, width * 5, height);
        }
        setBounds(contentPanel.getBounds());
        add(contentPanel);
    }

    /**
     *
     *
     * @return
     */
    private static JButton createCloseButton() {
        final JButton button = new JButton();
        button.setIcon(new javax.swing.ImageIcon(button.getClass().getResource("/images/close.png")));
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setOpaque(false);
        return button;
    }
}
