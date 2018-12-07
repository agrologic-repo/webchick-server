/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.i18n.LocaleManager;
import com.agrologic.app.model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Administrator
 */
public class AddTransactionDialog extends JDialog {

    private Configuration conf;
    private Long flockId;
    private Transaction currTransaction;
    private FlockManagerService flockService;
    private ResourceBundle bundle;

    /**
     * Creates new form AddSpreadDialog
     */
    public AddTransactionDialog(Frame parent, boolean modal) {
        this(Long.valueOf(1), parent, modal);
    }

    public AddTransactionDialog(Long flockId, Frame owner, boolean modal) {
        initComponents();
        bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // 09/01/2018
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        flockService = new FlockManagerService();
        conf = new Configuration();
        this.flockId = flockId;
        loadTransaction(flockId);
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
                    currTransaction = flockService.getTransactionById(id);
                    setForm(currTransaction);
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

        bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // 09/01/2018
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDataTable = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        txtRevenues = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtExpenses = new javax.swing.JTextField();

//        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { bundle.getString("add.transaction.dialog.item1"), bundle.getString("add.transaction.dialog.item2"), bundle.getString("add.transaction.dialog.item3"), bundle.getString("add.transaction.dialog.item4") }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//        setTitle("Add Transaction");
        setTitle(bundle.getString("add.transaction.dialog.add.transaction"));
        setMaximumSize(new java.awt.Dimension(390, 320));
        setMinimumSize(new java.awt.Dimension(390, 320));
        setPreferredSize(new java.awt.Dimension(390, 320));
        getContentPane().setLayout(null);

        tblDataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
//            new String [] {
//                "ID", "Name", "Expenses", "Revenues"
//            }
                new String [] {
                        bundle.getString("add.transaction.dialog.id"), bundle.getString("add.transaction.dialog.name"), bundle.getString("add.transaction.dialog.expenses"), bundle.getString("add.transaction.dialog.revenues")
                }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
        jScrollPane1.setBounds(10, 11, 360, 170);

//        btnAdd.setText("Add ");
        btnAdd.setText(bundle.getString("add.transaction.dialog.add"));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd);
        btnAdd.setBounds(260, 200, 105, 23);

//        jLabel2.setText("Name");
        jLabel2.setText(bundle.getString("add.transaction.dialog.name"));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 200, 65, 20);

//        jLabel4.setText("Revenues");
        jLabel4.setText(bundle.getString("add.transaction.dialog.revenues"));
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 250, 65, 20);
        getContentPane().add(txtName);
        txtName.setBounds(100, 200, 140, 20);

//        btnRemove.setText("Delete");
        btnRemove.setText(bundle.getString("add.transaction.dialog.delete"));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemove);
        btnRemove.setBounds(260, 230, 105, 23);
        getContentPane().add(txtRevenues);
        txtRevenues.setBounds(100, 250, 140, 20);

//        jLabel5.setText("Expenses");
        jLabel5.setText(bundle.getString("add.transaction.dialog.expenses"));
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 225, 65, 20);
        getContentPane().add(txtExpenses);
        txtExpenses.setBounds(100, 225, 140, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (!validateForm()) {
            return;
        }
        Transaction transaction = getFromFields();
        flockService.addTransaction(transaction);
        loadTransaction(flockId);
        clearForm();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if(currTransaction == null) {
            return;
        }
        flockService.removeTransaction(currTransaction.getId());
        loadTransaction(flockId);
        clearForm();
        btnAdd.setEnabled(true);
        btnRemove.setEnabled(false);
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void loadTransaction(Long cellinkId) {
        ((DefaultTableModel) tblDataTable.getModel()).getDataVector().removeAllElements();
        ((ListSelectionModel) tblDataTable.getSelectionModel()).setSelectionInterval(-1, -1);
        tblDataTable.revalidate();
        List<Transaction> transactionList = flockService.getAllTransaction(cellinkId);
        for (Transaction f : transactionList) {
            ((DefaultTableModel) tblDataTable.getModel()).insertRow(0, new Object[]{
                        f.getId(),
                        f.getName(),
                        f.getExpenses(),
                        f.getRevenues()});
        }
    }

    private Transaction getFromFields() {
        String name = txtName.getText();
        String srevenues = txtRevenues.getText();
        Float revenues = Float.parseFloat(srevenues);
        String sexpenses = txtExpenses.getText();
        Float expenses = Float.parseFloat(sexpenses);

        currTransaction = new Transaction();
        currTransaction.setFlockId(flockId);
        currTransaction.setName(name);
        currTransaction.setExpenses(expenses);
        currTransaction.setRevenues(revenues);
        return currTransaction;
    }

    private boolean validateForm() {
        if (txtName.getText().equals("")
            || txtRevenues.getText().equals("")
            || txtExpenses.getText().equals("")) {
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtName.setText("");
        txtRevenues.setText("");
        txtExpenses.setText("");
    }

    private void setForm(Transaction fuel) {
        txtName.setText("" + currTransaction.getName());
        txtExpenses.setText("" + currTransaction.getExpenses());
        txtRevenues.setText("" + currTransaction.getRevenues());
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
            java.util.logging.Logger.getLogger(AddTransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddTransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddTransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddTransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AddTransactionDialog dialog = new AddTransactionDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnRemove;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDataTable;
    private javax.swing.JTextField txtExpenses;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtRevenues;
    // End of variables declaration//GEN-END:variables
}
