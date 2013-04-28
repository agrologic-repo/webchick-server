
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package help.examples;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.util.Windows;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ButtonPanel extends JPanel {
    static final Point point = new Point(10, 10);
    static final Point shift = new Point(35, 30);
    private JButton    mainScreenButton;
    private JButton    secdScreenButton;

    public ButtonPanel(ActionListener parent, LayoutManager layout) {
        super(layout);
        mainScreenButton = new JButton("Main Screen");
        add(mainScreenButton);
        mainScreenButton.addActionListener(parent);
        secdScreenButton = new JButton("card3");
        add(secdScreenButton);
        secdScreenButton.addActionListener(parent);

        Dimension dim = Windows.screenResolution();

//      setBounds(point.x, point.y, dim.width - shift.x, shift.y);
        setBounds(point.x, point.y, 1024 - shift.x, shift.y);
    }
}



