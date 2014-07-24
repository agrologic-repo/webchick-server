package com.agrologic.app.gui.rxtx;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.model.ProgramActionSet;
import com.agrologic.app.model.rxtx.DataChangeEvent;
import com.agrologic.app.model.rxtx.DataChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Valery on 4/29/14.
 */
public class ActionButton extends JButton implements DataChangeListener {
    private final ProgramActionSet programActionSet;
    private final DatabaseAccessor dbaccess;
    private long controllerId;

    public ActionButton(String text, long cid, ProgramActionSet pas, DatabaseAccessor dba) {
        super("<html>" + text + "</hmtl>");
        this.controllerId = cid;
        this.programActionSet = pas;
        this.dbaccess = dba;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dbaccess.getControllerDao().sendNewDataValueToController(controllerId, programActionSet.getDataId(),
                            programActionSet.getValueId());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public void dataChanged(DataChangeEvent event) {
        // ignore
    }
}
