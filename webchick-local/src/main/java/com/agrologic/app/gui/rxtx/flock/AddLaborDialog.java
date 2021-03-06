/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.model.Labor;
import com.agrologic.app.model.Worker;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Administrator
 */
public class AddLaborDialog extends JDialog {

    private Configuration conf;
    private Labor currLabor;
    private Long flockId;
    private DateField d1;
    private FlockManagerService flockService;

    class WorkerEntry {

        Long id;
        String name;
        Float hourCost;

        public WorkerEntry() {
        }

        public Float getHourCost() {
            return hourCost;
        }

        public void setHourCost(Float hourCost) {
            this.hourCost = hourCost;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WorkerEntry other = (WorkerEntry) obj;
            if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Creates new form AddSpreadDialog
     */
    public AddLaborDialog(Frame parent, boolean modal) {
        this(Long.valueOf(1), parent, modal);
    }

    public AddLaborDialog(Long flockId, Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
        conf = new Configuration();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        this.d1 = CalendarFactory.createDateField();
        d1.setDateFormat(df);
        getContentPane().add(d1);
        d1.setBounds(170, 226, 100, 20);
        flockService = new FlockManagerService();
        this.flockId = flockId;
        loadLabor(flockId);
        tblDataTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();
                    // get the row index that contains that coordinate
                    int rowNumber = tblDataTable.rowAtPoint(p);
                    // get the ListSelectionModel of the JTable
                    ListSelectionModel model = tblDataTable.getSelectionModel();
                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    model.setSelectionInterval(rowNumber, rowNumber);
                    Long id = (Long) tblDataTable.getModel().getValueAt(rowNumber, 0);
                    currLabor = flockService.getLaborById(id);
                    setForm(currLabor);
                    btnAdd.setEnabled(false);
                    btnRemove.setEnabled(true);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDataTable = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtHour = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSalary = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        cmbWorkerNames = new javax.swing.JComboBox();
        btnAddFeadType = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Labor");
        setMinimumSize(new java.awt.Dimension(650, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(null);

        tblDataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Date", "Hour", "Salary"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblDataTable);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 11, 624, 170);

        btnAdd.setText("Add ");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd);
        btnAdd.setBounds(435, 195, 105, 23);

        jLabel2.setText("Name");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 198, 140, 20);

        jLabel3.setText("Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 226, 140, 20);

        jLabel4.setText("Hour");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 252, 140, 20);

        txtHour.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHourKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHourKeyTyped(evt);
            }
        });
        getContentPane().add(txtHour);
        txtHour.setBounds(170, 250, 220, 20);

        jLabel6.setText("Salary");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(20, 280, 140, 20);

        txtSalary.setEditable(false);
        txtSalary.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                calcTotal(evt);
            }
        });
        getContentPane().add(txtSalary);
        txtSalary.setBounds(170, 280, 220, 20);

        btnRemove.setText("Delete");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemove);
        btnRemove.setBounds(435, 220, 105, 23);

        cmbWorkerNames.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        getContentPane().add(cmbWorkerNames);
        cmbWorkerNames.setBounds(170, 200, 220, 20);

        btnAddFeadType.setText("Add Worker");
        btnAddFeadType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFeadTypeActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddFeadType);
        btnAddFeadType.setBounds(435, 245, 105, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (!validateForm()) {
            return;
        }

        Labor labor = getFromFields();
        flockService.addLabor(labor);
        loadLabor(flockId);
        clearForm();

    }//GEN-LAST:event_btnAddActionPerformed

    private void calcTotal() {
        if (!validateForm()) {
            return;
        }
        String shours = txtHour.getText();
        int hours = Integer.parseInt(shours);
        float hourCost = ((WorkerEntry) cmbWorkerNames.getSelectedItem()).getHourCost();
        float salary = (float) (hours * hourCost);
        txtSalary.setText("" + salary);
    }

    private void calcTotal(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_calcTotal
        if (!validateForm()) {
            return;
        }

        String shours = txtHour.getText();
        int hours = Integer.parseInt(shours);
        float hourCost = ((WorkerEntry) cmbWorkerNames.getSelectedItem()).getHourCost();
        float salary = (float) (hours * hourCost);
        txtSalary.setText("" + salary);
    }//GEN-LAST:event_calcTotal

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if (currLabor == null) {
            return;
        }
        flockService.removeLabor(currLabor.getId());
        loadLabor(flockId);
        clearForm();
        btnAdd.setEnabled(true);
        btnRemove.setEnabled(false);
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddFeadTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFeadTypeActionPerformed
        AddWorkerDialog addLaborDlg = new AddWorkerDialog((JFrame) this.getParent(), true);
        addLaborDlg.setVisible(true);
    }//GEN-LAST:event_btnAddFeadTypeActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        loadLabor(flockId);
    }//GEN-LAST:event_formWindowActivated

    private void txtHourKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHourKeyReleased
        FlockGUIUtil.keyPressedHandler(txtHour, evt);
        calcTotal();
    }//GEN-LAST:event_txtHourKeyReleased

    private void txtHourKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHourKeyTyped
        FlockGUIUtil.keyTypedHandler(txtHour, evt);
    }//GEN-LAST:event_txtHourKeyTyped

    private void loadLabor(Long flockId) {
        ((DefaultTableModel) tblDataTable.getModel()).getDataVector().removeAllElements();
        ((ListSelectionModel) tblDataTable.getSelectionModel()).setSelectionInterval(-1, -1);
        tblDataTable.revalidate();
        List<Labor> laborList = flockService.getAllLabor(flockId);
        for (Labor l : laborList) {
            ((DefaultTableModel) tblDataTable.getModel()).insertRow(0, new Object[]{
                        l.getId(),
                        ((Worker) flockService.getWorkerById(l.getWorkerId())).getName(),
                        l.getDate(),
                        l.getHours(),
                        l.getSalary()});
        }
        Long cellinkId = Long.parseLong(conf.getCellinkId());
        List<Worker> workerList = flockService.getAllWorker(cellinkId);
        cmbWorkerNames.removeAllItems();
        cmbWorkerNames.addItem("");
        for (Worker f : workerList) {
            WorkerEntry we = new WorkerEntry();
            we.setId(f.getId());
            we.setHourCost(f.getHourCost());
            we.setName(f.getName());
            cmbWorkerNames.addItem(we);
        }
    }

    private Labor getFromFields() {
        Long flockId = Long.valueOf(1);
        String hour = txtHour.getText();
        String date = d1.getFormattedTextField().getText();
        String salary = txtSalary.getText();
        Long id = ((WorkerEntry) cmbWorkerNames.getSelectedItem()).getId();
        currLabor = new Labor();
        currLabor.setFlockId(flockId);
        currLabor.setHours(Integer.parseInt(hour));
        currLabor.setDate(date);
        currLabor.setFlockId(flockId);
        currLabor.setWorkerId(id);
        currLabor.setSalary(Float.parseFloat(salary));
        return currLabor;
    }

    private boolean validateForm() {
        if (txtHour.getText().equals("")
                || cmbWorkerNames.getSelectedIndex() == 0) {
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtHour.setText("");
        txtSalary.setText("");
    }

    private void setForm(Labor labor) {
        try {
            txtHour.setText("" + labor.getHours());
            DateFormat shortDf = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
            Date date = shortDf.parse(labor.getDate());
            d1.setValue(date);
            txtSalary.setText("" + labor.getSalary());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddLaborDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddLaborDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddLaborDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddLaborDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AddLaborDialog dialog = new AddLaborDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddFeadType;
    private javax.swing.JButton btnRemove;
    private javax.swing.JComboBox cmbWorkerNames;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDataTable;
    private javax.swing.JTextField txtHour;
    private javax.swing.JTextField txtSalary;
    // End of variables declaration//GEN-END:variables
}
