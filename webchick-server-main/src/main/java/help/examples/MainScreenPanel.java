
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package help.examples;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.util.Windows;



import java.awt.*;

import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.BorderUIResource;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class MainScreenPanel extends JPanel {
    public static final int SIZE = 14;
    private int             cols = 4;
    private int             rows = (SIZE / 4) + (((SIZE % 4) > 0)
            ? 1
            : 0);
    private JPanel          panelHolder;
    private JScrollPane     scrollPane;

    public MainScreenPanel() {
        super(null);
        initComponents();
    }

    public void initComponents() {
        Dimension dim = Windows.screenResolution();

        panelHolder = new JPanel(new GridLayout(rows, cols));

        for (int i = 0; i < SIZE; i++) {
            JPanel panel = new JPanel(null);

            panel.setBorder(BorderUIResource.getBlackLineBorderUIResource());

            JButton button = new JButton("House " + i);

            button.setBounds(0, 0, ((dim.width - 60) / 4), 30);
            panel.add(button);
            panelHolder.add(panel);
        }

        panelHolder.setPreferredSize(new Dimension(150 * 4, 800 * 4));
        scrollPane = new JScrollPane(panelHolder);
        scrollPane.setAutoscrolls(true);
        scrollPane.setBounds(0, 0, dim.width - 35, dim.height - 145);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        add(scrollPane);
    }
}



