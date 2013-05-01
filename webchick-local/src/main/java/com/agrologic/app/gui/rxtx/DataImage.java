package com.agrologic.app.gui.rxtx;

import com.agrologic.app.model.rxtx.DataChangeEvent;
import com.agrologic.app.model.rxtx.DataChangeListener;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DataImage extends JLabel implements DataChangeListener {
    private static Map<Type, ImageIcon> imageIconMap;

    static {
        imageIconMap = new HashMap();
        imageIconMap.put(Type.FAN_ON, imageIcon("/resources/fan-on.gif"));
        imageIconMap.put(Type.FAN_OFF, imageIcon("/resources/fan-off.gif"));
        imageIconMap.put(Type.COOL_ON, imageIcon("/resources/coolon.gif"));
        imageIconMap.put(Type.COOL_OFF, imageIcon("/resources/cooloff.gif"));
        imageIconMap.put(Type.LIGHT_ON, imageIcon("/resources/lighton.gif"));
        imageIconMap.put(Type.LIGHT_OFF, imageIcon("/resources/lightoff.png"));
        imageIconMap.put(Type.AOUGER_ON, imageIcon("/resources/aougeron.gif"));
        imageIconMap.put(Type.AOUGER_OFF, imageIcon("/resources/aougeroff.gif"));
        imageIconMap.put(Type.WATER_ON, imageIcon("/resources/wateron.gif"));
        imageIconMap.put(Type.WATER_OFF, imageIcon("/resources/wateroff.gif"));
        imageIconMap.put(Type.RELAY_ON, imageIcon("/resources/relayon.gif"));
        imageIconMap.put(Type.RELAY_OFF, imageIcon("/resources/relayoff.png"));
        imageIconMap.put(Type.HEATER_ON, imageIcon("/resources/heateron.gif"));
        imageIconMap.put(Type.HEATER_OFF, imageIcon("/resources/heateroff.gif"));
        imageIconMap.put(Type.SPARK_ON, imageIcon("/resources/sparkon.gif"));
        imageIconMap.put(Type.SPARK_OFF, imageIcon("/resources/sparkoff.gif"));
    }

    private Integer bitNumber;
    private Type currType;

    public enum Type {
        FAN_ON, FAN_OFF, COOL_OFF, COOL_ON, LIGHT_ON, LIGHT_OFF, AOUGER_ON, AOUGER_OFF, WATER_ON, WATER_OFF, RELAY_ON,
        RELAY_OFF, HEATER_ON, HEATER_OFF, SPARK_ON, SPARK_OFF
    }

    public DataImage(Type imageType) {
        super();
        currType = imageType;
        setSize(new Dimension(16, 35));
        setImageIcon();
    }

    private static ImageIcon imageIcon(String path) {
        URL resource = DataImage.class.getResource(path);

        if (resource == null) {

            // logger.error("Resource "+path+" does not exist");
            return new ImageIcon();
        }

        return new ImageIcon(resource);
    }

    private void setImageIcon() {
        ImageIcon icon = null;

        icon = imageIconMap.get(currType);
        setIcon(icon);
    }

    @Override
    public void dataChanged(DataChangeEvent event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }

        if (bitNumber.equals(event.getBitNumber())) {
            this.currType = event.getType();
            setImageIcon();
            revalidate();
            repaint();
            invalidate();
        }
    }

    public void setBitNumber(Integer bitNumber) {
        this.bitNumber = bitNumber;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
