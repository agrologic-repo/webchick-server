/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.i18n.LocaleManager;
import com.agrologic.app.model.Flock;
import com.agrologic.app.util.Windows;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author Administrator
 */
public class FlockManager extends JFrame {

//    public static final String FLOCK_ALREADY_EXIST = "Flock exist , select another controller ";
    private DateField d1;
    private DateField d2;
    private ResourceBundle bundle; // NOI18N //08/01/2018

    enum FlockStatus {
        Open, Close
    };

    private EnumComboBoxModel<FlockStatus> statusModel = new EnumComboBoxModel<FlockStatus>(FlockStatus.class);
    private Flock selectedFlock;
    private FlockGraphs flockGraphs;
    private Manager manager;
    private FlockManagerService flockService;
    private boolean enableEdit = false;
    private boolean newFlock = false;
    private static final Logger logger = Logger.getLogger(FlockManager.class);

    /**
     * Creates new form flock manager
     */
    public FlockManager() {
        initComponents();
        bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // NOI18N //08/01/2018
        logger.info("Inside FlockManager Constructor ");
//        setTitle("Flock Manager"); // 08/01/2017
        setTitle(bundle.getString("flock.manager"));
        Windows.setWindowsLAF(this);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        this.d1 = CalendarFactory.createDateField();
        d1.setDateFormat(df);
        this.d2 = CalendarFactory.createDateField();
        d2.setDateFormat(df);
        jPanel2.add(d1, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 114, 180, -1));
        jPanel2.add(d2, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 140, 180, -1));
        tblFlocks.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();
                    // get the row index that contains that coordinate
                    int rowNumber = tblFlocks.rowAtPoint(p);
                    // get the ListSelectionModel of the JTable
                    ListSelectionModel model = tblFlocks.getSelectionModel();
                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    model.setSelectionInterval(rowNumber, rowNumber);
                    newFlock = false;
                    Flock flock = new Flock();
                    flock.setFlockId((Long) tblFlocks.getModel().getValueAt(rowNumber, 0));
                    flock.setFlockName((String) tblFlocks.getModel().getValueAt(rowNumber, 1));
                    flock.setControllerName((String) tblFlocks.getModel().getValueAt(rowNumber, 2));
                    flock.setStatus((String) tblFlocks.getModel().getValueAt(rowNumber, 3));
                    flock.setStartDate((String) tblFlocks.getModel().getValueAt(rowNumber, 4));
                    flock.setEndDate((String) tblFlocks.getModel().getValueAt(rowNumber, 5));
                    setSelectedFlock(flock);
                }
                if (e.getClickCount() == 2) {
                    flockGraphs = new FlockGraphs();
                    flockGraphs.showDialog(selectedFlock.getFlockId());
                }
            }
        });

        flockService = new FlockManagerService();
        logger.info("Start load data");
        loadData();
        disableEditing();
    }

    public void loadData() {
        // load houses to houses list
        loadHouses();

        // load flocks to flock table
        loadFlocks();
    }

    private void loadHouses() {
        cmbHouse.addItem("");
        Map<String, Long> houses = flockService.getControllerMap();
        Set<Entry<String, Long>> entries = houses.entrySet();
        for (Map.Entry entry : entries) {
            cmbHouse.addItem("<html>" + entry.getKey() + "</html>");
        }
    }

    private void loadFlocks() {
        List<Flock> flocks = flockService.getFlocks();
        for (Flock flock : flocks) {
            String house = "";
            for (Entry entry : flockService.getControllerMap().entrySet()) {
                if (entry.getValue().equals(flock.getControllerId())) {
                    house = "<html>" + entry.getKey() + "</html>";
                }
            }

            ((DefaultTableModel) tblFlocks.getModel()).insertRow(0, new Object[]{
                    flock.getFlockId(),
                    flock.getFlockName(),
                    house,
                    flock.getStatus(),
                    flock.getStartTime(),
                    flock.getEndTime()});
        }
    }

    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        cmbHouse.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        d1.setShowToday(true);
        d2.setShowToday(true);
    }

    private void prepareForEditing() {
        txtID.setEnabled(enableEdit);
        txtName.setEnabled(enableEdit);
        cmbHouse.setEnabled(enableEdit);
        cmbStatus.setEnabled(enableEdit);
        d1.setEnabled(enableEdit);
        d2.setEnabled(enableEdit);
        btnSave.setEnabled(enableEdit);
        btnRemove.setEnabled(enableEdit);
        btnShowGraph.setEnabled(enableEdit);
        btnManage.setEnabled(enableEdit);
    }

    private void prepareForSaving() {
        txtID.setEnabled(enableEdit);
        txtName.setEnabled(enableEdit);
        cmbHouse.setEnabled(enableEdit);
        cmbStatus.setEnabled(enableEdit);
        d1.setEnabled(enableEdit);
        d2.setEnabled(enableEdit);
        btnSave.setEnabled(enableEdit);
        btnRemove.setEnabled(!enableEdit);
        btnShowGraph.setEnabled(!enableEdit);
        btnManage.setEnabled(!enableEdit);
    }

    private void disableEditing() {
        txtID.setEnabled(false);
        txtName.setEnabled(false);
        cmbHouse.setEnabled(false);
        cmbStatus.setEnabled(false);
        d1.setEnabled(false);
        d2.setEnabled(false);
        btnSave.setEnabled(false);
        btnRemove.setEnabled(false);
        btnShowGraph.setEnabled(false);
        btnManage.setEnabled(false);
    }

    private void setSelectedFlock(Flock flock) {
        enableEdit = true;
        selectedFlock = null;
        if (flock == null) {
            prepareForEditing();
            return;
        }
        selectedFlock = flock;
        clearFields();
        prepareForEditing();
        try {
            txtID.setText("" + selectedFlock.getFlockId());
            txtName.setText(selectedFlock.getFlockName());
            cmbHouse.setSelectedItem(selectedFlock.getControllerName());
//            if (selectedFlock.getStatus().equals("Open")) { // 08/01/2018
            if (selectedFlock.getStatus().equals("Open")) { // 08/01/2018
//                cmbStatus.setSelectedItem(FlockStatus.Open); // 08/01/2018
                cmbStatus.setSelectedItem(bundle.getString("flock.manager.status.open"));// 08/01/2018
            } else {
//                cmbStatus.setSelectedItem(FlockStatus.Close);// 08/01/2017
                cmbStatus.setSelectedItem("flock.manager.status.close"); // 08/01/2017
            }
            DateFormat shortDf = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
            Date date = shortDf.parse(selectedFlock.getStartTime());
            d1.setValue(date);
            d2.setValue(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateForm() {
        if (txtID.getText().equals("")
                || txtName.getText().equals("")
                || cmbHouse.getSelectedIndex() == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bundle = ResourceBundle.getBundle(LocaleManager.UI_RESOURCE); // NOI18N //08/01/2018
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFlocks = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnShowGraph = new javax.swing.JButton();
        btnManage = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblHouse = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbHouse = new javax.swing.JComboBox();
        cmbStatus = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 650));

//        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Select flock from list ")); // 08/01/2018
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("flock.manager.select.from.list"))); // 08/01/2018

        tblFlocks.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
//                new String[]{
//                        "ID", "Name", "House", "Status", "Start Date", "End Date" // 08/01/2018
//                }
                new String[]{
                        bundle.getString("flock.manager.id"), bundle.getString("flock.manager.name"), bundle.getString("flock.manager.house"), bundle.getString("flock.manager.status"), bundle.getString("flock.manager.start.date"), bundle.getString("flock.manager.end.date")
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblFlocks);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                                .addContainerGap())
        );

//        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Add new flock")); // 08/01/2018
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("flock.manager.add.new.flock"))); // 08/01/2018

//        btnNew.setText("New "); // 08/01/2018
        btnNew.setText(bundle.getString("flock.manager.button.new")); // 08/01/2018
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

//        btnSave.setText("Save"); // 08/01/2018
        btnSave.setText(bundle.getString("flock.manager.button.save")); // 08/01/2018
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

//        btnRemove.setText("Remove"); // 08/01/2018
        btnRemove.setText(bundle.getString("flock.manager.button.remove")); // 08/01/2018
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

//        btnShowGraph.setText("Show Graph"); // 08/01/2018
        btnShowGraph.setText(bundle.getString("flock.manager.button.show.graph")); // 08/01/2018
        btnShowGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowGraphActionPerformed(evt);
            }
        });

//        btnManage.setText("Manage"); // 08/01/2018
        btnManage.setText(bundle.getString("flock.manager.manage"));
        btnManage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

//        jLabel1.setText("ID"); // 08/01/2018
        jLabel1.setText(bundle.getString("flock.manager.id")); // 08/01/2018
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 12, 65, 20));

        txtID.setEditable(false);
        jPanel2.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 12, 180, -1));

//        jLabel2.setText("Name"); // 08/01/2018
        jLabel2.setText(bundle.getString("flock.manager.name")); // 08/01/2018
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 38, 71, 20));
        jPanel2.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 38, 180, -1));

//        lblHouse.setText("House"); // 08/01/2018
        lblHouse.setText(bundle.getString("flock.manager.house")); // 08/01/2018
        jPanel2.add(lblHouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 64, 65, 20));

//        jLabel3.setText("Start Date"); // 08/01/2018
        jLabel3.setText(bundle.getString("flock.manager.start.date")); // 08/01/2018
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 116, 65, 20));

//        jLabel4.setText("End Date"); // 08/01/2018
        jLabel4.setText(bundle.getString("flock.manager.end.date")); // 08/01/2018
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 142, 65, 20));

//        jLabel5.setText("Status"); // 08/01/2018
        jLabel5.setText(bundle.getString("flock.manager.status")); // 08/01/2018
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 90, 65, 20));
        jPanel2.add(cmbHouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 64, 180, -1));

        jPanel2.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 90, 180, -1));
        cmbStatus.setModel(statusModel);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnShowGraph)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnManage, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(93, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(43, 43, 43))))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnShowGraph)
                                        .addComponent(btnManage))
                                .addGap(29, 29, 29)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableEdit = true;
        newFlock = true;
        setSelectedFlock(null);
        prepareForSaving();
        clearFields();
        txtID.setText("" + flockService.generateFlockId());
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!validateForm()) {
            JOptionPane.showMessageDialog(FlockManager.this,
                    bundle.getString("flock.manager.fields.can.not.be.empty"),
                    bundle.getString("flock.manager.error"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Flock flock = new Flock();
        Long flockId = flockService.generateFlockId();
        if (selectedFlock != null) {
            flock = selectedFlock;
        } else {
            flock.setFlockId(flockId);
        }

        flock.setFlockName(txtName.getText());
        String houseName = (String) cmbHouse.getSelectedItem();
        houseName = houseName.replace("<html>", "");
        houseName = houseName.replace("</html>", "");

        Long controlId = flockService.getControllerMap().get(houseName);
        flock.setControllerId(controlId);
        flock.setStartDate(d1.getFormattedTextField().getText());
        if (cmbStatus.getSelectedItem() == FlockStatus.Open) {
//            flock.setStatus(FlockStatus.Open.toString()); // 08/01/2018
            flock.setStatus(bundle.getString("flock.manager.status.open")); // 08/01/2018
        } else {
//            flock.setStatus(FlockStatus.Close.toString()); // 08/01/2018
            flock.setStatus(bundle.getString("flock.manager.status.close")); // 08/01/2018
            flock.setEndDate(d2.getFormattedTextField().getText());
        }
        if (newFlock) {
            boolean flockExist = false;
            List<Flock> openFlocks = flockService.getOpenFlocks();
            for (Flock f : openFlocks) {
                if (f.getControllerId().equals(flock.getControllerId())) {
                    flockExist = true;
                }
            }
            if (flockExist) {
//                JOptionPane.showMessageDialog(FlockManager.this, FLOCK_ALREADY_EXIST, "Error", JOptionPane.ERROR_MESSAGE); // 08/01/2018
                JOptionPane.showMessageDialog(FlockManager.this, bundle.getString("flock.manager.flock.already.exist"), bundle.getString("flock.manager.error"), JOptionPane.ERROR_MESSAGE); // 08/01/2018
            } else {
                flockService.addFlock(flock);
            }
            newFlock = false;
            clearFields();
        } else {
            flockService.saveFlock(flock);
            clearFields();
        }

        //deletes ALL the rows
        ((DefaultTableModel) tblFlocks.getModel()).getDataVector().removeAllElements();
        //repaints the table and notify all listeners (only once!)
        ((DefaultTableModel) tblFlocks.getModel()).fireTableDataChanged();
        cmbHouse.removeAllItems();
        loadData();
        disableEditing();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if (selectedFlock == null) {
            return;
        }
        flockService.removeFlock(selectedFlock);
        setSelectedFlock(null);
        //deletes ALL the rows
        ((DefaultTableModel) tblFlocks.getModel()).getDataVector().removeAllElements();
        //repaints the table and notify all listeners (only once!)
        ((DefaultTableModel) tblFlocks.getModel()).fireTableDataChanged();
        cmbHouse.removeAllItems();
        loadData();
        clearFields();
        disableEditing();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnShowGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowGraphActionPerformed
        flockGraphs = new FlockGraphs();
        flockGraphs.showDialog(selectedFlock.getFlockId());
    }//GEN-LAST:event_btnShowGraphActionPerformed

    private void btnManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageActionPerformed
        if (manager == null) {
            manager = new Manager(flockService, selectedFlock.getFlockId());
        }
        manager.setVisible(true);

    }//GEN-LAST:event_btnManageActionPerformed

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
            java.util.logging.Logger.getLogger(FlockManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FlockManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FlockManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FlockManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FlockManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnManage;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnShowGraph;
    private javax.swing.JComboBox cmbHouse;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHouse;
    private javax.swing.JTable tblFlocks;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
