/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.flock;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Locale;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class CalendarCellEditor extends AbstractCellEditor
        implements TableCellEditor,
        ActionListener {

    private DateField d1;

    public CalendarCellEditor() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        this.d1 = CalendarFactory.createDateField();
        d1.setDateFormat(df);
    }

    @Override
    public Object getCellEditorValue() {
        return d1.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
