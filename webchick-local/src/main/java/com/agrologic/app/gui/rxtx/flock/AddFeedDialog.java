/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.model.Feed;
import com.agrologic.app.model.FeedType;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Administrator
 */
public class AddFeedDialog extends JDialog {

    private Configuration conf;
    private Feed currFeed;
    private Long flockId;
    private DateField d1;
    private FlockManagerService flockService;

    class FeedTypeEntry {

        Long id;
        String type;
        Float price;

        public FeedTypeEntry() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return type;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FeedTypeEntry other = (FeedTypeEntry) obj;
            if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
                return false;
            }
            if (this.price != other.price && (this.price == null || !this.price.equals(other.price))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
            hash = 97 * hash + (this.price != null ? this.price.hashCode() : 0);
            return hash;
        }
    }

    /**
     * Creates new form AddSpreadDialog
     */
    public AddFeedDialog(Frame parent, boolean modal) {
        this(Long.valueOf(1), parent, modal);
    }

    public AddFeedDialog(Long flockId, Frame owner, boolean modal) {
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
        loadFeed(flockId);
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
                    currFeed = flockService.getFeedById(id);
                    setForm(currFeed);
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
        jLabel5 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        txtAccountNumber = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        cmbFeedType = new javax.swing.JComboBox();
        btnAddFeadType = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Feed");
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
                "ID", "Amount", "Date", "Type", "Account Number", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        jLabel2.setText("Amount");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 198, 140, 20);

        jLabel3.setText("Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 226, 140, 20);

        jLabel4.setText("Fee Type");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 252, 140, 20);

        jLabel5.setText("Account Number");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 278, 140, 20);

        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
        });
        getContentPane().add(txtAmount);
        txtAmount.setBounds(170, 198, 220, 20);

        txtAccountNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAccountNumberKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAccountNumberKeyTyped(evt);
            }
        });
        getContentPane().add(txtAccountNumber);
        txtAccountNumber.setBounds(170, 278, 220, 20);

        jLabel6.setText("Total");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(20, 304, 140, 20);

        txtTotal.setEditable(false);
        txtTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                calcTotal(evt);
            }
        });
        getContentPane().add(txtTotal);
        txtTotal.setBounds(170, 304, 220, 20);

        btnRemove.setText("Delete");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemove);
        btnRemove.setBounds(435, 220, 105, 23);

        cmbFeedType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        getContentPane().add(cmbFeedType);
        cmbFeedType.setBounds(170, 250, 220, 20);

        btnAddFeadType.setText("Add Feed");
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
        Feed feed = getFromFields();
        flockService.addFeed(feed);
        loadFeed(flockId);
        clearForm();
    }//GEN-LAST:event_btnAddActionPerformed

    private void calcTotal() {
        if (!validateForm()) {
            return;
        }

        String samount = txtAmount.getText();
        int amount = Integer.parseInt(samount);
        float price = ((FeedTypeEntry) cmbFeedType.getSelectedItem()).getPrice();
        float total = (float) (amount * price);
        txtTotal.setText("" + total);
    }

    private void calcTotal(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_calcTotal
        if (!validateForm()) {
            return;
        }

        String samount = txtAmount.getText();
        int amount = Integer.parseInt(samount);
        float price = ((FeedTypeEntry) cmbFeedType.getSelectedItem()).getPrice();
        float total = (float) (amount * price);
        txtTotal.setText("" + total);
    }//GEN-LAST:event_calcTotal

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if (currFeed == null) {
            return;
        }
        flockService.removeFeed(currFeed.getId());
        loadFeed(flockId);
        clearForm();
        btnAdd.setEnabled(true);
        btnRemove.setEnabled(false);
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddFeadTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFeadTypeActionPerformed
        AddFeedTypeDialog addFeedDlg = new AddFeedTypeDialog((JFrame) this.getParent(), true);
        addFeedDlg.setVisible(true);
    }//GEN-LAST:event_btnAddFeadTypeActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        loadFeed(flockId);
    }//GEN-LAST:event_formWindowActivated

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        FlockGUIUtil.keyPressedHandler(txtAmount, evt);
        calcTotal();
    }//GEN-LAST:event_txtAmountKeyReleased

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
        FlockGUIUtil.keyTypedHandler(txtAmount, evt);

    }//GEN-LAST:event_txtAmountKeyTyped

    private void txtAccountNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountNumberKeyTyped
        FlockGUIUtil.keyPressedHandler(txtAccountNumber, evt);
    }//GEN-LAST:event_txtAccountNumberKeyTyped

    private void txtAccountNumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountNumberKeyReleased
        FlockGUIUtil.keyPressedHandler(txtAccountNumber, evt);
        calcTotal();
    }//GEN-LAST:event_txtAccountNumberKeyReleased

    private void loadFeed(Long flockId) {
        ((DefaultTableModel) tblDataTable.getModel()).getDataVector().removeAllElements();
        ((ListSelectionModel) tblDataTable.getSelectionModel()).setSelectionInterval(-1, -1);
        tblDataTable.revalidate();
        List<Feed> fuelList = flockService.getAllFeed(flockId);
        for (Feed g : fuelList) {
            ((DefaultTableModel) tblDataTable.getModel()).insertRow(0, new Object[]{
                        g.getId(),
                        g.getAmount(),
                        g.getDate(),
                        g.getType(),
                        g.getNumberAccount(),
                        g.getTotal()});
        }

        Long cellinkId = Long.parseLong(conf.getCellinkId());
        List<FeedType> feedTypeList = flockService.getAllFeedType(cellinkId);
        List<FeedTypeEntry> feedTypeEntryList = new ArrayList<FeedTypeEntry>();
        cmbFeedType.removeAllItems();
        cmbFeedType.addItem("");
        for (FeedType f : feedTypeList) {
            FeedTypeEntry fte = new FeedTypeEntry();
            fte.setId(f.getId());
            fte.setPrice(f.getPrice());
            fte.setType(f.getFeedType());
            feedTypeEntryList.add(fte);
            cmbFeedType.addItem(fte);
        }
    }

    private Feed getFromFields() {
        Long flockId = Long.valueOf(1);
        String amount = txtAmount.getText();
        String date = d1.getFormattedTextField().getText();
        String accountNumber = txtAccountNumber.getText();
        String total = txtTotal.getText();
        Long type = ((FeedTypeEntry) cmbFeedType.getSelectedItem()).getId();

        currFeed = new Feed();
        currFeed.setType(type);
        currFeed.setAmount(Integer.parseInt(amount));
        currFeed.setDate(date);
        currFeed.setFlockId(flockId);
        currFeed.setNumberAccount(Integer.parseInt(accountNumber));
        currFeed.setTotal(Float.parseFloat(total));
        return currFeed;
    }

    private boolean validateForm() {
        if (txtAmount.getText().equals("")
                || txtAccountNumber.getText().equals("")
                || cmbFeedType.getSelectedIndex() == 0) {
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtAmount.setText("");
        txtAccountNumber.setText("");
        txtTotal.setText("");
    }

    private void setForm(Feed feed) {
        try {
            txtAmount.setText("" + feed.getAmount());
            DateFormat shortDf = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
            Date date = shortDf.parse(feed.getDate());
            d1.setValue(date);
            txtAccountNumber.setText("" + feed.getNumberAccount());
            txtTotal.setText("" + feed.getTotal());
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
            java.util.logging.Logger.getLogger(AddFeedDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddFeedDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddFeedDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddFeedDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AddFeedDialog dialog = new AddFeedDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cmbFeedType;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDataTable;
    private javax.swing.JTextField txtAccountNumber;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}