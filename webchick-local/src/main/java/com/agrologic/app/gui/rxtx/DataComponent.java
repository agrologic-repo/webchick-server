package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DataComponent {

    public static final int HEIGHT = 25;
    public static final int WIDTH = 150;
    public static final int X_OFFSET = 2;
    public static final int Y_OFFSET = 2;
    private Rectangle boundsComponent;
    private Rectangle boundsLabel;
    private JComponent component;
    private Controller controller;
    private DataController data;
    private DatabaseAccessor dbaccess;
    private JLabel label;
    private Point location;
    private List<ProgramAlarm> programAlarms;
    private ProgramRelay relay;

    public DataComponent(DataController d, int xCordL, int yCordL, int xCordC, int yCordC, List<ProgramAlarm> pas) {
        data = d;
        programAlarms = new ArrayList<ProgramAlarm>();

        for (ProgramAlarm pa : pas) {
            if (data.getId().equals(pa.getDataId())) {
                programAlarms.add(pa);
            }
        }

        boundsLabel = new Rectangle(xCordL, yCordL, xCordC - 10, HEIGHT);
        boundsComponent = new Rectangle(xCordC, yCordC, 60, HEIGHT);
        label = new JLabel("<html>" + data.getUnicodeLabel() + "</html>");
        label.setBounds(boundsLabel);
        label.setOpaque(true);
        component = createComponent();
        component.setBounds(boundsComponent);
    }

    public DataComponent(DataController d, int xCordL, int yCordL, int xCordC, int yCordC, ProgramRelay r) {
        data = d;
        relay = r;
        boundsLabel = new Rectangle(xCordL, yCordL, xCordC - 10, HEIGHT);
        boundsComponent = new Rectangle(xCordC, yCordC, 50, HEIGHT);
        label = new JLabel("<html>" + relay.getUnicodeText() + "</html>");
        label.setBounds(boundsLabel);
        label.setOpaque(true);
        component = createComponent();
        component.setBounds(boundsComponent);
    }

    public DataComponent(DataController d, int xCordL, int yCordL, int xCordC, int yCordC, Controller c,
                         DatabaseAccessor dba) {
        controller = c;
        dbaccess = dba;
        data = d;
        boundsLabel = new Rectangle(xCordL, yCordL, xCordC - 10, HEIGHT);
        boundsComponent = new Rectangle(xCordC, yCordC, 50, HEIGHT);
        label = new JLabel("<html>" + data.getUnicodeLabel() + "</html>");
        label.setBounds(boundsLabel);
        label.setOpaque(true);
        component = createComponent();
        component.setBounds(boundsComponent);
        component.setForeground(new Color(0, 100, 0));
        Font font = component.getFont();
        component.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
    }

    public JLabel getLabel() {
        return label;
    }

    public JComponent getComponent() {
        return component;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DataComponent other = (DataComponent) obj;

        if ((this.data != other.data) && ((this.data == null) || !this.data.equals(other.data))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 97 * hash + ((this.data != null)
                ? this.data.hashCode()
                : 0);

        return hash;
    }

    private JComponent createComponent() {
        JComponent returnCompon;

        if (!data.isStatus()) {
            if (!data.getReadonly()) {
                returnCompon = createReadWriteComponent();
                data.addDataChangeListener((DataTextField) returnCompon);
            } else {
                returnCompon = createReadOnlyComponent();
                data.addDataChangeListener((DataLabel) returnCompon);
            }
        } else {
            int special = data.getSpecial();
            switch (special) {
                case Data.STATUS:
                    returnCompon = createReadOnlyComponent();
                    data.addDataChangeListener((DataLabel) returnCompon);

                    break;

                case Data.ALARM:
                    returnCompon = createAlarmComponent();
                    data.addDataChangeListener((DataLabel) returnCompon);

                    JButton button = createShowAlramDialogButton(returnCompon.getLocation());
                    JComponent contentPanel = new JPanel(new FlowLayout());

                    contentPanel.add(returnCompon);
                    contentPanel.add(button);
                    returnCompon = contentPanel;

                    break;

                case Data.SYSTEM_STATE:
                    returnCompon = createReadOnlyComponent();
                    data.addDataChangeListener((DataLabel) returnCompon);

                    break;

                case Data.RELAY:
                    returnCompon = createRelayComponent();
                    data.addDataChangeListener((DataImage) returnCompon);

                    break;

                default:
                    returnCompon = createReadOnlyComponent();
                    data.addDataChangeListener((DataLabel) returnCompon);

                    break;
            }
        }

        return returnCompon;
    }

    private JComponent createReadOnlyComponent() {
        String formatedValue = "-1";

        if (data.getValue() != null) {
            formatedValue = DataFormat.formatToStringValue(data.getFormat(), data.getValueToUI());
        }

        final DataLabel dataLabel = new DataLabel(formatedValue);
        data.addDataChangeListener(dataLabel);
        return dataLabel;
    }

    private JComponent createReadWriteComponent() {
        String formatedValue = "-1";

        if (data.getValue() != null) {
            formatedValue = DataFormat.formatToStringValue(data.getFormat(), data.getValueToUI());
            // dataText.setText(formatedValue);
        }

        final DataTextField dataText = new DataTextField(formatedValue, controller.getId(), data, dbaccess);

        data.addDataChangeListener(dataText);
        dataText.setPreferredSize(new java.awt.Dimension(10, 20));

        return dataText;
    }

    private JComponent createRelayComponent() {
        JComponent returnCompon = null;

        if (relay.getText().contains("Fan") || relay.getText().contains("Mixer")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.FAN_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.FAN_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else if (relay.getText().contains("Light")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.LIGHT_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.LIGHT_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else if (relay.getText().contains("Cool")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.COOL_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.COOL_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else if (relay.getText().contains("Heater")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.HEATER_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.HEATER_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else if (relay.getText().contains("Feed")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.AOUGER_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.AOUGER_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else if (relay.getText().contains("Water")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.WATER_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.WATER_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else if (relay.getText().contains("Ignition")) {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.SPARK_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.SPARK_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        } else {
            if (relay.isOn()) {
                returnCompon = new DataImage(DataImage.Type.RELAY_ON);
            } else {
                returnCompon = new DataImage(DataImage.Type.RELAY_OFF);
            }

            ((DataImage) returnCompon).setBitNumber(relay.getBitNumber());
        }

        data.addDataChangeListener((DataImage) returnCompon);

        return returnCompon;
    }

    private JComponent createAlarmComponent() {
        JComponent jComponent = null;
        jComponent = createReadOnlyComponent();

        StringBuilder toolTipBuffer = new StringBuilder();
        toolTipBuffer.append("<html>");
        toolTipBuffer.append("<center>");
        toolTipBuffer.append("<font style='color: blue;'>");
        toolTipBuffer.append(data.getLabel());
        toolTipBuffer.append("</font><br />");
        toolTipBuffer.append("<font style='color: red;'>");
        for (ProgramAlarm pa : programAlarms) {
            toolTipBuffer.append(pa.getDigitNumber()).append(" - ").append(pa.getText()).append("<br />");
        }
        toolTipBuffer.append("</font>");
        toolTipBuffer.append("</center>");
        toolTipBuffer.append("</html>");
        jComponent.setToolTipText(toolTipBuffer.toString());
        jComponent.setForeground(Color.blue);
        Font font = jComponent.getFont();
        jComponent.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        return jComponent;
    }

    private JButton createShowAlramDialogButton(Point p) {
        final JButton button = createHelpButton();

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                int xPos = evt.getXOnScreen();
                int yPos = evt.getYOnScreen();

                location = new Point(xPos, yPos);
            }
        });
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ProgramAlarmPopup dialog = new ProgramAlarmPopup(location, programAlarms);
                dialog.setVisible(true);
            }
        });

        return button;
    }

    private static JButton createHelpButton() {
        final JButton button = new JButton();

        button.setIcon(new javax.swing.ImageIcon(button.getClass().getResource("/images/help.gif")));
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setOpaque(true);
        button.setBorder(null);

        return button;
    }
}
