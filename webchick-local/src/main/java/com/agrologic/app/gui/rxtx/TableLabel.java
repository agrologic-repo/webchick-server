package com.agrologic.app.gui.rxtx;

import javax.swing.*;
import java.awt.*;

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
