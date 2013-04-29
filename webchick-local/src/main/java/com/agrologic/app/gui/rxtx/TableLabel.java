
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.*;
import java.awt.*;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class TableLabel extends JLabel {
    public static final int fontSize = 15;
    public static final int horizAlign = SwingConstants.CENTER;
    public static final Color forgrdColor = Color.BLUE;
    public static final int fontStyle = Font.BOLD;

    public TableLabel(String text) {
        super(text, horizAlign);
        setForeground(forgrdColor);
        setFont(new Font(getFont().getFontName(), fontStyle, fontSize));
    }
}


//~ Formatted by Jindent --- http://www.jindent.com