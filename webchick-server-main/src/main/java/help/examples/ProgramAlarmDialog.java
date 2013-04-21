
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package help.examples;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ProgramAlarmDialog extends JDialog {
    int     x = -1;
    int     y = -1;
    JPanel  alarmsPanel;
    JButton button;
    JPanel  buttonPanel;
    JPanel  contentPanel;

    public ProgramAlarmDialog() {
        super();
        initComponents();
    }

    public ProgramAlarmDialog(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        initComponents();
    }

    private void initComponents() {
        setUndecorated(true);
        setLayout(new BorderLayout());
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new LineBorder(Color.blue, 1));
        button = createCloseButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(button);
        alarmsPanel = new JPanel(new GridLayout(10, 1));

        int width = 0;

        for (int i = 0; i < 10; i++) {
            JLabel label = new JLabel("    " + i + " - text");

            alarmsPanel.add(label);

            if (label.getText().length() > width) {
                width = label.getText().length();
            }
        }

        if ((x == -1) && (y == -1)) {
            setBounds(40, 80, width * 10, 200);
        } else {
            setBounds(x + 50, y + 40, width * 10, 200);
        }

        contentPanel.add(buttonPanel, BorderLayout.PAGE_START);
        contentPanel.add(alarmsPanel, BorderLayout.PAGE_END);
        add(contentPanel);
    }

    private static JButton createHelpButton() {
        final JButton button = new JButton();

        button.setIcon(new javax.swing.ImageIcon(button.getClass().getResource("/help/examples/help.gif")));
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

//      button.setOpaque(true);
//      button.setBorder(null);
        return button;
    }

    private static JButton createCloseButton() {
        final JButton button = new JButton();

        button.setIcon(new javax.swing.ImageIcon(button.getClass().getResource("/help/examples/close.png")));
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

//      button.setOpaque(true);
//      button.setBorder(null);
        return button;
    }

    public static void main(String[] args[]) {
        final JFrame frame = new JFrame("Test Frame");

        frame.setBounds(40, 80, 200, 200);
        frame.setLayout(new FlowLayout());

        final JButton button = createHelpButton();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point              point  = frame.getLocation();
                ProgramAlarmDialog dialog = new ProgramAlarmDialog();

                dialog.setVisible(true);
            }
        });
        frame.add(button);
        frame.setVisible(true);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
