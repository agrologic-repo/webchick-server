
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.CellinkState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Title: CellinkTableCellRenderer <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD.
 * <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class CellinkStateCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(new Font("Tahoma-Bold", Font.PLAIN, 12));

        if (value == null) {
            return null;
        }

        CellinkState state = (CellinkState) value;

        if (!isSelected) {
            switch (state.getValue()) {
            case CellinkState.STATE_ONLINE :
                setBackground(Color.cyan);

                break;

            case CellinkState.STATE_RUNNING :
                setBackground(Color.green);

                break;

            case CellinkState.STATE_START :
                setBackground(Color.orange);

                break;

            case CellinkState.STATE_STOP :
                setBackground(Color.red);

                break;

            case CellinkState.STATE_OFFLINE :
                setBackground(Color.lightGray);

                break;

            case CellinkState.STATE_UNKNOWN :
            default :
                setBackground(Color.white);

                break;
            }
        }

        return this;
    }
}



