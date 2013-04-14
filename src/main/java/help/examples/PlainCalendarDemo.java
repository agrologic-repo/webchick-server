/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package help.examples;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
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
public class PlainCalendarDemo extends JDialog {

    DateField d1;
    DateField d2;
    private JButton bCalc;
    private JButton bExit;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JTextField tResult;

    public PlainCalendarDemo(Frame parent, boolean modal) {
        super(parent, modal);
        this.d1 = CalendarFactory.createDateField();
        this.d2 = CalendarFactory.createDateField();
        this.d2.setShowOkCancel(true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel jPanel1 = new JPanel();
        this.bExit = new JButton();
        JPanel jPanel2 = new JPanel();
        this.jLabel1 = new JLabel();
        JPanel jPanel3 = this.d1;
        this.jLabel3 = new JLabel();
        this.jLabel2 = new JLabel();
        JPanel jPanel4 = this.d2;
        this.jLabel4 = new JLabel();
        JSeparator jSeparator1 = new JSeparator();
        this.bCalc = new JButton();
        this.tResult = new JTextField();

        setDefaultCloseOperation(2);
        setTitle("Plain Calendar Demo");
        this.bExit.setText("Exit");
        this.bExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                PlainCalendarDemo.this.bExitActionPerformed(evt);
            }
        });
        jPanel1.add(this.bExit);

        getContentPane().add(jPanel1, "South");

        jPanel2.setLayout(new GridBagLayout());

        this.jLabel1.setText("1st Date");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        jPanel2.add(this.jLabel1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.weightx = 1.0D;
        jPanel2.add(jPanel3, gridBagConstraints);

        this.jLabel3.setText("(one click required)");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.fill = 2;
        jPanel2.add(this.jLabel3, gridBagConstraints);

        this.jLabel2.setText("2nd Date");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        jPanel2.add(this.jLabel2, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.weightx = 1.0D;
        jPanel2.add(jPanel4, gridBagConstraints);

        this.jLabel4.setText("(ok/cancel buttons provided)");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.fill = 2;
        jPanel2.add(this.jLabel4, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.weightx = 1.0D;
        jPanel2.add(jSeparator1, gridBagConstraints);

        this.bCalc.setText("Calc");
        this.bCalc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                PlainCalendarDemo.this.bCalcActionPerformed(evt);
            }
        });
        jPanel2.add(this.bCalc, new GridBagConstraints());

        this.tResult.setColumns(15);
        this.tResult.setEditable(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.weightx = 1.0D;
        jPanel2.add(this.tResult, gridBagConstraints);

        getContentPane().add(jPanel2, "Center");

        pack();
    }

    private void bCalcActionPerformed(ActionEvent evt) {
        Date c1 = (Date) this.d1.getValue();
        Date c2 = (Date) this.d2.getValue();
        if ((c1 != null) && (c2 != null)) {
            long t = c2.getTime() - c1.getTime();
            if (t < 0L) {
                t *= -1L;
            }
            int d = (int) (t / 86400000L);
            this.tResult.setText("Time Difference: " + d + " days");
        } else {
            this.tResult.setText("");
        }
    }

    private void bExitActionPerformed(ActionEvent evt) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        new PlainCalendarDemo(null, true).setVisible(true);
    }
}