/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.wizard;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.gui.ConfigurationDialog;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Language;
import com.agrologic.app.model.User;
import com.agrologic.app.util.Windows;

import javax.swing.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQL2DerbyMegrator extends javax.swing.JFrame {

    private final static String CLEAR_TEXT = "";
    private final static String NO_CELLINKS = "<html>User exist . But no cellinks found for this user </html>";
    private final static String CHOOSE_CELLINK = "<html>User exist . Please select farm from list </html>";
    private final static String USER_NOT_EXIST = "<html>User does not exist </html>";
    private final static String EMPTY_FIELD = "<html>The user id feild cannot be empty</html>";
    private JFileChooser fileChooser;

    /**
     * Creates new form MySQL2DerbyMigrator
     */
    public MySQL2DerbyMegrator() {
        initComponents();
        initLanguage();
        Windows.centerOnScreen(this);
        Windows.setWindowsLAF(this);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        cmbFarms = new javax.swing.JComboBox();
        btnFind = new javax.swing.JButton();
        lblFindUserStatus = new javax.swing.JLabel();
        btnSettings = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtDatabaseDir = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        prgBarCreateStatus = new javax.swing.JProgressBar();
        lblCreatingStatus = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbLanguages = new javax.swing.JComboBox();
        btnCreate = new javax.swing.JButton();

        jButton2.setText("jButton2");

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Create Database Ver.0.1");
        setMinimumSize(new java.awt.Dimension(440, 510));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("User and Farm id's"));
        jPanel1.setToolTipText("");
        jPanel1.setMaximumSize(new java.awt.Dimension(405, 130));
        jPanel1.setMinimumSize(new java.awt.Dimension(405, 130));
        jPanel1.setPreferredSize(new java.awt.Dimension(405, 130));

        jLabel1.setText("User ID");
        jLabel1.setMaximumSize(new java.awt.Dimension(35, 10));
        jLabel1.setMinimumSize(new java.awt.Dimension(35, 10));
        jLabel1.setPreferredSize(new java.awt.Dimension(35, 10));

        jLabel2.setText("Farm list");

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnSettings.setText("Settings");
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserId)
                            .addComponent(cmbFarms, 0, 111, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnFind, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(btnSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lblFindUserStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFarms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSettings))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFindUserStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Database path"));

        jLabel3.setText("Choose database directory ");

        btnBrowse.setText("Browse...");
        btnBrowse.setMaximumSize(new java.awt.Dimension(80, 25));
        btnBrowse.setMinimumSize(new java.awt.Dimension(80, 25));
        btnBrowse.setPreferredSize(new java.awt.Dimension(80, 25));
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDatabaseDir)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDatabaseDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Creating status"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblCreatingStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(prgBarCreateStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(lblCreatingStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prgBarCreateStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose language"));

        jLabel5.setText("Language ");

        cmbLanguages.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbLanguagesItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(cmbLanguages, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbLanguages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCreate.setText("Create");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCreate)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        lblFindUserStatus.setText(CLEAR_TEXT);

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    cmbFarms.removeAllItems();
                    if (txtUserId.getText().length() == 0) {
                        lblFindUserStatus.setText(EMPTY_FIELD);
                        return;
                    }
                    Long userId = Long.parseLong(txtUserId.getText());
                    UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                    User user = userDao.getById(userId);
                    if (user.getValidate() == true) {
                        CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                        List<Cellink> cellinks = (List<Cellink>) cellinkDao.getAllUserCellinks(userId);
                        if (cellinks.size() == 0) {
                            lblFindUserStatus.setText(NO_CELLINKS);
                        } else {
                            lblFindUserStatus.setText(CHOOSE_CELLINK);
                        }
                        List<CellinkEntry> entries = new ArrayList<CellinkEntry>();
                        entries.add(new CellinkEntry(Long.valueOf(-1), ""));
                        for (Cellink c : cellinks) {
                            entries.add(new CellinkEntry(c.getId(), c.getName()));
                        }

                        for (CellinkEntry ce : entries) {
                            cmbFarms.addItem(ce);
                        }
                    } else {
                        lblFindUserStatus.setText(USER_NOT_EXIST);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtDatabaseDir.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        if (((JButton) evt.getSource()).getText().equals("Close")) {
            System.exit(0);
            return;
        }

        if(!validateFeilds()) {
            return;
        }

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    Configuration conf = new Configuration();
                    conf.setLanguage(((LanguageEntry) cmbLanguages.getSelectedItem()).getLang());
                    String path = txtDatabaseDir.getText();
                    delete(new File(path + "\\agrodb"));
                    System.setProperty("derby.system.home", path);
                    DatabaseManager dbMgr = new DatabaseManager();
                    String userId = txtUserId.getText();
                    String cellinkId = ((CellinkEntry) cmbFarms.getSelectedItem()).getId().toString();
                    prgBarCreateStatus.setIndeterminate(true);
                    btnCreate.setEnabled(false);
                    lblCreatingStatus.setText("Connecting to server and loading data from the remote server ... ");
                    dbMgr.doLoadTableData(userId, cellinkId);
                    lblCreatingStatus.setText("Creating embedded database ...  ");
                    dbMgr.runCreateTablesTask();
                    lblCreatingStatus.setText("Insert data to the embedded database ...");
                    dbMgr.runInsertLoadedData();
                    dbMgr.finish();
                    prgBarCreateStatus.setIndeterminate(false);
                    prgBarCreateStatus.setVisible(false);
                    lblCreatingStatus.setText("Done !");
                    btnCreate.setEnabled(true);
                    btnCreate.setText("Close");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void cmbLanguagesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbLanguagesItemStateChanged
        String lang = ((LanguageEntry) evt.getItem()).getLang();
        Configuration configuration = new Configuration();
        configuration.setLanguage(lang);
        configuration.saveUpdatePreferences();
    }//GEN-LAST:event_cmbLanguagesItemStateChanged

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        ConfigurationDialog configDialog = new ConfigurationDialog(this, true);
        configDialog.showDialog();
    }//GEN-LAST:event_btnSettingsActionPerformed

    public void delete(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            System.err.println("Failed to delete file: " + f);
        }
    }

    public void initLanguage() {
        LanguageDao langDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
        try {
            List<Language> languages = (List<Language>) langDao.geAll();
            List<LanguageEntry> entries = new ArrayList<LanguageEntry>();
            for (Language c : languages) {
                entries.add(new LanguageEntry(c.getId(), c.getLanguage()));
            }

            for (LanguageEntry le : entries) {
                cmbLanguages.addItem(le);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateFeilds() {
        if(txtUserId.getText().length() == 0) {
            return false;
        }

        if(cmbFarms.getSelectedIndex() == 0) {
            return false;
        }

        if(txtDatabaseDir.getText().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     *
     */
    class CellinkEntry {

        Long id;
        String cellink;

        public CellinkEntry(Long id, String cellink) {
            this.id = id;
            this.cellink = cellink;
        }

        public String getCellink() {
            return cellink;
        }

        public void setCellink(String cellink) {
            this.cellink = cellink;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return cellink;
        }
    }

    /**
     *
     */
    class LanguageEntry {

        Long id;
        String lang;

        public LanguageEntry(Long id, String lang) {
            this.id = id;
            this.lang = lang;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        @Override
        public String toString() {
            return lang;
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
            java.util.logging.Logger.getLogger(MySQL2DerbyMegrator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MySQL2DerbyMegrator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MySQL2DerbyMegrator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MySQL2DerbyMegrator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MySQL2DerbyMegrator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnSettings;
    private javax.swing.JComboBox cmbFarms;
    private javax.swing.JComboBox cmbLanguages;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblCreatingStatus;
    private javax.swing.JLabel lblFindUserStatus;
    private javax.swing.JProgressBar prgBarCreateStatus;
    private javax.swing.JTextField txtDatabaseDir;
    private javax.swing.JTextField txtUserId;
    // End of variables declaration//GEN-END:variables
}
