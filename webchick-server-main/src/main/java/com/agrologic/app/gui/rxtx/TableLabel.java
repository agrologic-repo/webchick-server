
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

//~--- JDK imports ------------------------------------------------------------
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.Data;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
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
