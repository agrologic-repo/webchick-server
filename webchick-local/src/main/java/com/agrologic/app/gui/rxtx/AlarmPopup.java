package com.agrologic.app.gui.rxtx;

import com.agrologic.app.model.ProgramAlarm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

/**
 * Created by Valery on 19/05/14.
 */
public class AlarmPopup extends JPopupMenu {
    private DefaultListModel<String> model = new DefaultListModel<String>();

    public AlarmPopup(ComponentOrientation currentOrientation, List<ProgramAlarm> programAlarms) {
        super();
        this.setComponentOrientation(currentOrientation);
        for (ProgramAlarm pa : programAlarms) {
            model.addElement("<html>" + pa.getDigitNumber() + " - " + pa.getText() + "</html>");
        }
        JList list = new JList(model);
        list.setComponentOrientation(getComponentOrientation());
        setLayout(new BorderLayout());
        add(list);

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                setVisible(false);
            }
        });
    }
}
