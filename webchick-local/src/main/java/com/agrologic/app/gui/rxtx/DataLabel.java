
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.rxtx.DataChangeEvent;
import com.agrologic.app.model.rxtx.DataChangeListener;
import javax.swing.JLabel;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DataLabel extends JLabel implements DataChangeListener {

    public DataLabel(String text) {
        super(text);
    }

    @Override
    public void dataChanged(DataChangeEvent event) {
        setText("<html>" + event.getNewString() + "</html>");
    }

    public void setText(Long value) {
        String svalue = value.toString();
        setText(svalue);
        revalidate();
        repaint();
        invalidate();
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
