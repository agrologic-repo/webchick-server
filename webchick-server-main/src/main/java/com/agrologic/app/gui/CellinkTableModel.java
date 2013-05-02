
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Title: CellinkTableModel <br> Description: <br> Copyright: Copyright (c) 2009
 * <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class CellinkTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES     = {
        "ID", "Name", "User", "IP Address", "Port", "Time", "Status", "Logging"
    };
    public static final int       COL_ID           = 0;
    public static final int       COL_IP_ADDRESS   = 3;
    public static final int       COL_NAME         = 1;
    public static final int       COL_PORT         = 4;
    public static final int       COL_STATUS       = 6;
    public static final int       COL_TIME         = 5;
    public static final int       COL_USER         = 2;
    public static final int       COL_WITH_LOGGING = 7;
    private static final long     serialVersionUID = 1L;
    private static final Class[]  COLUMN_CLASSES   = {
        Long.class, String.class, String.class, String.class, Integer.class, String.class, CellinkState.class,
        Boolean.class
    };
    private boolean       debug = true;
    private List<Cellink> cellinks;

    public CellinkTableModel() {
        this.cellinks = new ArrayList<Cellink>();
    }

    public CellinkTableModel(List<Cellink> cellinks) {
        this.cellinks = cellinks;
    }

    public void add(Cellink cellink) {
        cellinks.add(cellink);
    }

    public void addAll(Collection<Cellink> c) {
        cellinks.addAll(c);
    }

    public void remove(Cellink cellink) {
        cellinks.remove(cellink);
    }

    public void removeAll(Collection<Cellink> c) {
        cellinks.removeAll(c);
    }

    @Override
    public String getColumnName(int index) {
        return COLUMN_NAMES[index];
    }

    @Override
    public Class<?> getColumnClass(int index) {
        return COLUMN_CLASSES[index];
    }

    @Override
    public int getRowCount() {
        return cellinks.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_CLASSES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] cellinkObjects = cellinks.toArray();
        Cellink  cellink        = (Cellink) cellinkObjects[rowIndex];

        switch (columnIndex) {
        case COL_ID :
            return cellink.getId();

        case COL_NAME :
            return cellink.getName();

        case COL_USER :
            return cellink.getPassword();

        case COL_IP_ADDRESS :
            return cellink.getIp();

        case COL_PORT :
            return cellink.getPort();

        case COL_TIME :
            return cellink.getTime().toString();

        case COL_STATUS :
            return cellink.getCellinkState();

        case COL_WITH_LOGGING :
            return cellink.isWithLogging();

        default :
            throw new IllegalArgumentException("Illegal column index");
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {

        // let's say I wanted to make the whole
        // second row and the whole third column
        // uneditable, and the rest editable
        return (col == COL_WITH_LOGGING) || (col == COL_STATUS);
    }

    /**
     * Don't need to implement this method unless your table's data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        Object[] cellinkObjects = cellinks.toArray();
        Cellink  cellink        = (Cellink) cellinkObjects[row];

        switch (col) {
        case COL_ID :
            cellink.setId((Long) value);

            break;

        case COL_NAME :
            cellink.setName((String) value);

            break;

        case COL_USER :
            cellink.setPassword((String) value);

            break;

        case COL_IP_ADDRESS :
            cellink.setIp((String) value);

            break;

        case COL_PORT :
            cellink.setPort((Integer) value);

            break;

//      case COL_TIME:
//          cellink.setTime(new Timestamp).toString();
//          break;
        case COL_STATUS :
            cellink.getCellinkState();

            break;

        case COL_WITH_LOGGING :
            cellink.setWithLogging(((Boolean) value).booleanValue());

            break;

        default :
            throw new IllegalArgumentException("Illegal column index");
        }

        fireTableCellUpdated(row, col);
    }

    public int size() {
        return cellinks.size();
    }

    public List<Cellink> getData() {
        return cellinks;
    }

    /**
     * Add new cellink to current cellink list . Remove cellink from current
     * cellink list .
     *
     * @param retrievedCellinkList the retrieved from database cellink list .
     */
    public void addAndRemoveAbsent(List<Cellink> retrievedCellinkList) {
        List<Cellink> removableCellinks = new ArrayList<Cellink>();

        for (Cellink c : cellinks) {
            if (retrievedCellinkList.contains(c) == false) {
                removableCellinks.add(c);
            }
        }

        removeAll(removableCellinks);

        List<Cellink> addCellinks = new ArrayList<Cellink>();

        for (Cellink c : retrievedCellinkList) {
            if (cellinks.contains(c) == false) {
                addCellinks.add(c);
            }
        }

        addAll(addCellinks);
    }

    public void setReloadChanges(int length, List<Cellink> retrievedCellinkList) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                Cellink currCellink = cellinks.get(i);
                Cellink retrCellink = retrievedCellinkList.get(j);

                if (currCellink.equals(retrCellink)) {
                    if (currCellink.getState() != retrCellink.getState()) {
                        currCellink.setState(retrCellink.getState());
                    }
                }
            }
        }
    }
}



