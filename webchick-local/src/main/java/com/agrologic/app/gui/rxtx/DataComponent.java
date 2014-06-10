package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.*;
import com.agrologic.app.model.rxtx.DataController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class DataComponent {
    private JComponent component;
    private Controller controller;
    private DataController data;
    private DatabaseAccessor dbaccess;
    private JLabel label;
    private List<ProgramAlarm> programAlarms;
    private ProgramRelay relay;
    private ComponentOrientation componentOrientation;

    /**
     * Constructor to create alarm component
     *
     * @param dataController
     * @param pas
     */
    public DataComponent(DataController dataController, List<ProgramAlarm> pas, ComponentOrientation componentOrientation) {
        this.componentOrientation = componentOrientation;
        data = dataController;
        programAlarms = new ArrayList<ProgramAlarm>();

        for (ProgramAlarm pa : pas) {
            if (data.getId().equals(pa.getDataId())) {
                programAlarms.add(pa);
            }
        }
        label = new JLabel("<html>" + data.getUnicodeLabel() + "</html>");
        label.setOpaque(true);
        component = createComponent();
    }

    /**
     * Constructor to create system state component
     * s
     *
     * @param dataController
     * @param psss
     * @param defaultText
     */
    public DataComponent(DataController dataController, List<ProgramSystemState> psss, String defaultText, ComponentOrientation componentOrientation) {
        this.componentOrientation = componentOrientation;
        data = dataController;
        if (data.getValue() == null) {
            data.setValue((long) 0);
        }

        String text = defaultText;
        for (ProgramSystemState pss : psss) {
            long v = data.getValue();
            long n = pss.getSystemStateNumber();
            if (v == n) {
                text = pss.getText();
            }
        }
        label = new JLabel("<html>" + data.getUnicodeLabel() + "</html>");
        label.setOpaque(true);
        component = createComponent();
        ((DataLabel) component).setText(text);
    }

    /**
     * Constructor to create relay component
     *
     * @param dataController
     * @param programRelay
     */
    public DataComponent(DataController dataController, ProgramRelay programRelay, ComponentOrientation componentOrientation) {
        this.componentOrientation = componentOrientation;
        data = dataController;
        relay = programRelay;
        label = new JLabel("<html>" + relay.getUnicodeText() + "</html>");
        label.setOpaque(true);
        component = createComponent();
    }

    /**
     * Constructor to create data component
     *
     * @param d
     * @param c
     * @param dba
     */
    public DataComponent(DataController d, Controller c, DatabaseAccessor dba, ComponentOrientation componentOrientation) {
        this.componentOrientation = componentOrientation;
        controller = c;
        dbaccess = dba;
        data = d;
        label = new JLabel("<html>" + data.getUnicodeLabel() + "</html>");
        label.setOpaque(true);
        component = createComponent();
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
                if (data.isPassword()) {
                    returnCompon = createPasswordReadWriteComponent();
                    data.addDataChangeListener((DataPasswordField) returnCompon);
                } else {
                    returnCompon = createReadWriteComponent();
                    data.addDataChangeListener((DataTextField) returnCompon);
                }
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
        dataLabel.setPreferredSize(new Dimension(50, 20));
        dataLabel.setComponentOrientation(componentOrientation);
        data.addDataChangeListener(dataLabel);
        return dataLabel;
    }

    private JComponent createReadWriteComponent() {
        String formatedValue = "-1";
        if (data.getValue() != null) {
            formatedValue = DataFormat.formatToStringValue(data.getFormat(), data.getValueToUI());
        }

        final DataTextField dataText = new DataTextField(formatedValue, controller.getId(), data, dbaccess);
        data.addDataChangeListener(dataText);
        dataText.setPreferredSize(new Dimension(50, 20));
        if (componentOrientation == ApplicationLocal.orientationLTR) {
            dataText.setHorizontalAlignment(JTextField.LEFT);
        } else {
            dataText.setHorizontalAlignment(JTextField.RIGHT);
        }
        return dataText;
    }

    private JComponent createPasswordReadWriteComponent() {
        String formatedValue = "-1";
        if (data.getValue() != null) {
            formatedValue = DataFormat.formatToStringValue(data.getFormat(), data.getValueToUI());
        }

        final DataPasswordField dataPasswordField = new DataPasswordField(formatedValue, controller.getId(), data, dbaccess);
        data.addDataChangeListener(dataPasswordField);
        dataPasswordField.setPreferredSize(new Dimension(50, 20));
        dataPasswordField.setComponentOrientation(componentOrientation);
        return dataPasswordField;
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
        final JComponent jComponent = createReadOnlyComponent();
        jComponent.setForeground(Color.blue);
        ((JLabel) jComponent).setIcon(new ImageIcon(jComponent.getClass().getResource("/images/help.gif")));
        jComponent.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent arg0) {
                AlarmPopup popup = new AlarmPopup(componentOrientation, programAlarms);
                Dimension size = popup.getPreferredSize();
                int x = (jComponent.getWidth() - size.width) / 2;
                int y = jComponent.getHeight();
                popup.show(jComponent, x, y);
            }

            public void mouseEntered(MouseEvent arg0) {

            }

            public void mouseExited(MouseEvent arg0) {

            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {

            }
        });


        Font font = jComponent.getFont();
        jComponent.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        return jComponent;
    }
}
