/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.model.*;
import com.agrologic.app.util.Windows;

import javax.swing.*;
import java.util.List;

/**
 * @author Administrator
 */
public class Manager extends JFrame {

    private Long currFlockId = (long) 1;
    private Flock currFlock;
    private FlockManagerService flockService;

    /**
     * Creates new form Manager
     */
    public Manager() {
        Windows.setWindowsLAF(this);
        initComponents();
        this.flockService = new FlockManagerService();
        setCurrFlock();
        viewFlock();
    }

    public Manager(FlockManagerService flockService, Long flockId) {
        Windows.setWindowsLAF(this);
        initComponents();
        this.flockService = flockService;
        this.currFlockId = flockId;
        setCurrFlock();
        viewFlock();
    }

    public void viewFlock() {
        try {
            sumAddGas();
            sumAddFuel();
            sumAddFeed();
            sumAddSpread();
            sumAddMedicine();
            sumAddLabor();
            sumAddTransaction();
            sumAddDistribute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Controller controller = flockService.getControllerById(currFlock.getControllerId());
        txtArea.setText("" + controller.getArea());

        //Energy
        txtGasStartAmount.setText("" + currFlock.getGasBegin());
        txtGasLeftAmount.setText("" + currFlock.getGasEnd());
        txtGasTotalAmount.setText("" + currFlock.calcTotalQuantityGas());
        txtGasStartPrice.setText("" + currFlock.getCostGas());
        txtGasLeftPrice.setText("" + currFlock.getCostGasEnd());
        txtGasTotalPrice.setText("" + currFlock.calcTotalGasCost());

        txtFuelStartAmount.setText("" + currFlock.getFuelBegin());
        txtFuelLeftAmount.setText("" + currFlock.getFuelEnd());
        txtFuelTotalAmount.setText("" + currFlock.calcTotalQuantityFuel());
        txtFuelStartPrice.setText("" + currFlock.getCostFuel());
        txtFuelLeftPrice.setText("" + currFlock.getCostFuelEnd());
        txtFuelTotalPrice.setText("" + currFlock.calcTotalFuelCost());

        // Chick quantity
        txtMaleQuant.setText("" + currFlock.getQuantityMale());
        txtFemaleQuant.setText("" + currFlock.getQuantityFemale());
        txtMaleCost.setText("" + currFlock.getCostChickMale());
        txtFemaleCost.setText("" + currFlock.getCostChickFemale());
        txtTotalQuant.setText("" + currFlock.getQuantityChicks());
        txtTotalCost.setText("" + currFlock.calcTotalChicksCost());

        // Meter data
        txtStartElect.setText("" + currFlock.getElectBegin());
        txtEndElect.setText("" + currFlock.getElectEnd());
        txtElectPrice.setText("" + currFlock.getCostElect());
        txtTotalElect.setText("" + currFlock.getQuantityElect());
        txtTotalElectSum.setText("" + currFlock.calcTotalElectCost());

        txtStartWater.setText("" + currFlock.getWaterBegin());
        txtEndWater.setText("" + currFlock.getWaterEnd());
        txtWaterPrice.setText("" + currFlock.getCostWater());
        txtTotalWater.setText("" + currFlock.getQuantityWater());
        txtTotalWaterSum.setText("" + currFlock.calcTotalWaterCost());

        // Spread
        txtTotalSum.setText("" + currFlock.getTotalSpread());
        txtTotalSpread.setText("" + currFlock.getSpreadAdd());

        // Feed
        txtTotalFeed.setText("" + currFlock.getFeedAdd());
        txtTotalExpances.setText("" + currFlock.getTotalFeed());

        // Medicine
        txtTotalMedicine.setText("" + currFlock.getTotalMedic());

        // Labor
        txtTotalLabor.setText("" + currFlock.getTotalLabor());

        // Summary
        txtChicks.setText("" + currFlock.getTotalChicks());
        txtElectricity.setText("" + currFlock.getTotalElect());
        txtWater.setText("" + currFlock.getTotalWater());
        txtDisel.setText("" + currFlock.getTotalFuel());
        txtGas.setText("" + currFlock.getTotalGas());
        txtSpread.setText("" + currFlock.getTotalSpread());
        txtFeed.setText("" + currFlock.getTotalFeed());
        txtMedicine.setText("" + currFlock.getTotalMedic());
        txtLabor.setText("" + currFlock.getTotalLabor());
        txtTransaction.setText("" + currFlock.getExpenses());

        txtTotalRevenuesTransact.setText("" + currFlock.getRevenues());
        txtTotalExpenses.setText("" + currFlock.getExpenses());
        txtRevenuesTotal.setText("" + currFlock.calcTotalRevenues());
        txtExpansesTotal.setText("" + currFlock.calcTotalExpenses());
        txtCostPerKG.setText("" + currFlock.calcTotCostPerKGBirds());
        txtFeedConversation.setText("");
        txtTotal.setText("");

        txtTotSaughtBirds.setText("");
        txtTotSaughtMale.setText("");
        txtTotSaughtFemale.setText("");
        txtTotalWeight.setText("");
        txtTotalRevenues.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel46 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        pnlHousesParameter = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtArea = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        btnSaveHouseParam = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnlEnergy = new javax.swing.JPanel();
        txtGasStartAmount = new javax.swing.JTextField();
        txtGasLeftAmount = new javax.swing.JTextField();
        txtGasTotalAmount = new javax.swing.JTextField();
        txtGasStartPrice = new javax.swing.JTextField();
        txtGasLeftPrice = new javax.swing.JTextField();
        txtGasTotalPrice = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtFuelStartPrice = new javax.swing.JTextField();
        txtFuelTotalAmount = new javax.swing.JTextField();
        txtFuelLeftAmount = new javax.swing.JTextField();
        txtFuelStartAmount = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFuelTotalPrice = new javax.swing.JTextField();
        txtFuelLeftPrice = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnSaveEnergy = new javax.swing.JButton();
        btnAddGas = new javax.swing.JButton();
        btnAddFuel = new javax.swing.JButton();
        pnlChickQuantity = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtMaleQuant = new javax.swing.JTextField();
        txtMaleCost = new javax.swing.JTextField();
        txtFemaleQuant = new javax.swing.JTextField();
        txtFemaleCost = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTotalQuant = new javax.swing.JTextField();
        txtTotalCost = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btnSaveChickQuant = new javax.swing.JButton();
        pnlMeterData = new javax.swing.JPanel();
        txtStartElect = new javax.swing.JTextField();
        txtEndElect = new javax.swing.JTextField();
        txtElectPrice = new javax.swing.JTextField();
        txtTotalElect = new javax.swing.JTextField();
        txtTotalElectSum = new javax.swing.JTextField();
        txtStartWater = new javax.swing.JTextField();
        txtEndWater = new javax.swing.JTextField();
        txtWaterPrice = new javax.swing.JTextField();
        txtTotalWater = new javax.swing.JTextField();
        txtTotalWaterSum = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnSaveMeterData = new javax.swing.JButton();
        pnlInformation = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtHathery = new javax.swing.JTextField();
        txtBreder = new javax.swing.JTextField();
        btnSaveInfo = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        pnlTotalDistribution = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtTotSaughtMale = new javax.swing.JTextField();
        txtTotSaughtFemale = new javax.swing.JTextField();
        txtTotSaughtBirds = new javax.swing.JTextField();
        txtTotalWeight = new javax.swing.JTextField();
        txtTotalRevenues = new javax.swing.JTextField();
        btnDistribution = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        pnlSpread = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtTotalSpread = new javax.swing.JTextField();
        txtTotalSum = new javax.swing.JTextField();
        btnSpread = new javax.swing.JButton();
        pnlFeed = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtTotalFeed = new javax.swing.JTextField();
        txtTotalExpances = new javax.swing.JTextField();
        txtTotalFeedConsump = new javax.swing.JTextField();
        btnFeed = new javax.swing.JButton();
        pnlMedicine = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtTotalMedicine = new javax.swing.JTextField();
        btnMedicine = new javax.swing.JButton();
        pnlLabor = new javax.swing.JPanel();
        btnLabor = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        txtTotalLabor = new javax.swing.JTextField();
        pnlTransaction = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtTotalExpenses = new javax.swing.JTextField();
        txtTotalRevenuesTransact = new javax.swing.JTextField();
        btnTransaction = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        pnlSummary = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        txtChicks = new javax.swing.JTextField();
        txtElectricity = new javax.swing.JTextField();
        txtWater = new javax.swing.JTextField();
        txtDisel = new javax.swing.JTextField();
        txtGas = new javax.swing.JTextField();
        txtSpread = new javax.swing.JTextField();
        txtTransaction = new javax.swing.JTextField();
        txtLabor = new javax.swing.JTextField();
        txtMedicine = new javax.swing.JTextField();
        txtFeed = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtRevenuesTotal = new javax.swing.JTextField();
        txtExpansesTotal = new javax.swing.JTextField();
        txtCostPerKG = new javax.swing.JTextField();
        txtFeedConversation = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();

        jLabel46.setText("jLabel46");

        setTitle("Manager ");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnlHousesParameter.setBorder(javax.swing.BorderFactory.createTitledBorder("Houses parameters"));

        jLabel1.setText("Area");

        jLabel2.setText("Currency");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Dollar", "Sheqel"}));

        btnSaveHouseParam.setText("Save");
        btnSaveHouseParam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveHouseParamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHousesParameterLayout = new javax.swing.GroupLayout(pnlHousesParameter);
        pnlHousesParameter.setLayout(pnlHousesParameterLayout);
        pnlHousesParameterLayout.setHorizontalGroup(
                pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHousesParameterLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlHousesParameterLayout.createSequentialGroup()
                                                .addGroup(pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                                                .addGap(34, 34, 34)
                                                .addGroup(pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtArea)
                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnSaveHouseParam, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        pnlHousesParameterLayout.setVerticalGroup(
                pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHousesParameterLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(txtArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlHousesParameterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addComponent(btnSaveHouseParam)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(307, 307, 307)
                                .addComponent(pnlHousesParameter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(277, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(195, 195, 195)
                                .addComponent(pnlHousesParameter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(282, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Houses ", jPanel1);

        pnlEnergy.setBorder(javax.swing.BorderFactory.createTitledBorder("Energy "));
        pnlEnergy.setToolTipText("");
        pnlEnergy.setLayout(null);

        txtGasStartAmount.setMaximumSize(null);
        txtGasStartAmount.setMinimumSize(null);
        txtGasStartAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGasStartAmountKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGasStartAmountKeyTyped(evt);
            }
        });
        pnlEnergy.add(txtGasStartAmount);
        txtGasStartAmount.setBounds(70, 120, 59, 20);

        txtGasLeftAmount.setMaximumSize(null);
        txtGasLeftAmount.setMinimumSize(null);
        txtGasLeftAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGasLeftAmountKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGasLeftAmountKeyTyped(evt);
            }
        });
        pnlEnergy.add(txtGasLeftAmount);
        txtGasLeftAmount.setBounds(70, 140, 59, 20);

        txtGasTotalAmount.setEditable(false);
        txtGasTotalAmount.setMaximumSize(null);
        txtGasTotalAmount.setMinimumSize(null);
        pnlEnergy.add(txtGasTotalAmount);
        txtGasTotalAmount.setBounds(70, 160, 59, 20);

        txtGasStartPrice.setMaximumSize(null);
        txtGasStartPrice.setMinimumSize(null);
        txtGasStartPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGasStartPriceKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGasStartPriceKeyTyped(evt);
            }
        });
        pnlEnergy.add(txtGasStartPrice);
        txtGasStartPrice.setBounds(130, 120, 60, 20);

        txtGasLeftPrice.setMaximumSize(null);
        txtGasLeftPrice.setMinimumSize(null);
        txtGasLeftPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGasLeftPriceKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGasLeftPriceKeyTyped(evt);
            }
        });
        pnlEnergy.add(txtGasLeftPrice);
        txtGasLeftPrice.setBounds(130, 140, 60, 20);

        txtGasTotalPrice.setEditable(false);
        txtGasTotalPrice.setMaximumSize(null);
        txtGasTotalPrice.setMinimumSize(null);
        pnlEnergy.add(txtGasTotalPrice);
        txtGasTotalPrice.setBounds(130, 160, 60, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Amount");
        pnlEnergy.add(jLabel3);
        jLabel3.setBounds(70, 100, 60, 22);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Price");
        pnlEnergy.add(jLabel4);
        jLabel4.setBounds(129, 100, 60, 22);

        jLabel5.setText("Start");
        pnlEnergy.add(jLabel5);
        jLabel5.setBounds(30, 120, 40, 14);

        jLabel6.setText("Left");
        pnlEnergy.add(jLabel6);
        jLabel6.setBounds(30, 140, 40, 14);

        jLabel7.setText("Total");
        pnlEnergy.add(jLabel7);
        jLabel7.setBounds(30, 160, 40, 14);

        txtFuelStartPrice.setMaximumSize(null);
        txtFuelStartPrice.setMinimumSize(null);
        txtFuelStartPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFuelStartPriceKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFuelStartPriceKeyTyped(evt);
            }
        });
        pnlEnergy.add(txtFuelStartPrice);
        txtFuelStartPrice.setBounds(261, 120, 59, 20);

        txtFuelTotalAmount.setEditable(false);
        txtFuelTotalAmount.setMaximumSize(null);
        txtFuelTotalAmount.setMinimumSize(null);
        pnlEnergy.add(txtFuelTotalAmount);
        txtFuelTotalAmount.setBounds(200, 160, 60, 20);

        txtFuelLeftAmount.setMaximumSize(null);
        txtFuelLeftAmount.setMinimumSize(null);
        pnlEnergy.add(txtFuelLeftAmount);
        txtFuelLeftAmount.setBounds(200, 140, 60, 20);

        txtFuelStartAmount.setMaximumSize(null);
        txtFuelStartAmount.setMinimumSize(null);
        pnlEnergy.add(txtFuelStartAmount);
        txtFuelStartAmount.setBounds(200, 120, 60, 20);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Price");
        pnlEnergy.add(jLabel8);
        jLabel8.setBounds(260, 100, 60, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Amount");
        pnlEnergy.add(jLabel9);
        jLabel9.setBounds(200, 102, 60, 20);

        txtFuelTotalPrice.setEditable(false);
        txtFuelTotalPrice.setMaximumSize(null);
        txtFuelTotalPrice.setMinimumSize(null);
        pnlEnergy.add(txtFuelTotalPrice);
        txtFuelTotalPrice.setBounds(261, 160, 59, 20);

        txtFuelLeftPrice.setMaximumSize(null);
        txtFuelLeftPrice.setMinimumSize(null);
        txtFuelLeftPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFuelLeftPriceKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFuelLeftPriceKeyTyped(evt);
            }
        });
        pnlEnergy.add(txtFuelLeftPrice);
        txtFuelLeftPrice.setBounds(261, 140, 59, 20);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Gas");
        pnlEnergy.add(jLabel10);
        jLabel10.setBounds(60, 80, 124, 20);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Fuel");
        pnlEnergy.add(jLabel11);
        jLabel11.setBounds(200, 80, 125, 20);

        btnSaveEnergy.setText("Save");
        btnSaveEnergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveEnergyActionPerformed(evt);
            }
        });
        pnlEnergy.add(btnSaveEnergy);
        btnSaveEnergy.setBounds(30, 220, 90, 23);

        btnAddGas.setText("Add Gas");
        btnAddGas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGasActionPerformed(evt);
            }
        });
        pnlEnergy.add(btnAddGas);
        btnAddGas.setBounds(90, 50, 73, 23);

        btnAddFuel.setText("Add Fuel");
        btnAddFuel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFuelActionPerformed(evt);
            }
        });
        pnlEnergy.add(btnAddFuel);
        btnAddFuel.setBounds(230, 50, 75, 23);

        pnlChickQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder("Chick quantity"));
        pnlChickQuantity.setLayout(null);

        jLabel12.setText("Male");
        pnlChickQuantity.add(jLabel12);
        jLabel12.setBounds(30, 110, 90, 20);

        jLabel13.setText("Total");
        pnlChickQuantity.add(jLabel13);
        jLabel13.setBounds(30, 150, 90, 20);

        txtMaleQuant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaleQuantKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMaleQuantKeyTyped(evt);
            }
        });
        pnlChickQuantity.add(txtMaleQuant);
        txtMaleQuant.setBounds(120, 110, 90, 20);

        txtMaleCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaleCostKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMaleCostKeyTyped(evt);
            }
        });
        pnlChickQuantity.add(txtMaleCost);
        txtMaleCost.setBounds(220, 110, 90, 20);

        txtFemaleQuant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFemaleQuantKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFemaleQuantKeyTyped(evt);
            }
        });
        pnlChickQuantity.add(txtFemaleQuant);
        txtFemaleQuant.setBounds(120, 130, 90, 20);

        txtFemaleCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFemaleCostKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFemaleCostKeyTyped(evt);
            }
        });
        pnlChickQuantity.add(txtFemaleCost);
        txtFemaleCost.setBounds(220, 130, 90, 20);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Quantity");
        pnlChickQuantity.add(jLabel14);
        jLabel14.setBounds(120, 90, 90, 20);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Cost");
        pnlChickQuantity.add(jLabel15);
        jLabel15.setBounds(220, 90, 90, 20);

        txtTotalQuant.setEditable(false);
        pnlChickQuantity.add(txtTotalQuant);
        txtTotalQuant.setBounds(120, 150, 90, 20);

        txtTotalCost.setEditable(false);
        pnlChickQuantity.add(txtTotalCost);
        txtTotalCost.setBounds(220, 150, 90, 20);

        jLabel16.setText("Female");
        pnlChickQuantity.add(jLabel16);
        jLabel16.setBounds(30, 130, 90, 20);

        btnSaveChickQuant.setText("Save");
        btnSaveChickQuant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChickQuantActionPerformed(evt);
            }
        });
        pnlChickQuantity.add(btnSaveChickQuant);
        btnSaveChickQuant.setBounds(30, 220, 80, 23);

        pnlMeterData.setBorder(javax.swing.BorderFactory.createTitledBorder("Meter data "));
        pnlMeterData.setLayout(null);

        txtStartElect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStartElectKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStartElectKeyTyped(evt);
            }
        });
        pnlMeterData.add(txtStartElect);
        txtStartElect.setBounds(20, 70, 70, 20);

        txtEndElect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEndElectKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEndElectKeyTyped(evt);
            }
        });
        pnlMeterData.add(txtEndElect);
        txtEndElect.setBounds(100, 70, 65, 20);

        txtElectPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtElectPriceKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtElectPriceKeyTyped(evt);
            }
        });
        pnlMeterData.add(txtElectPrice);
        txtElectPrice.setBounds(170, 70, 65, 20);

        txtTotalElect.setEditable(false);
        pnlMeterData.add(txtTotalElect);
        txtTotalElect.setBounds(100, 95, 65, 20);

        txtTotalElectSum.setEditable(false);
        pnlMeterData.add(txtTotalElectSum);
        txtTotalElectSum.setBounds(100, 120, 65, 20);

        txtStartWater.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStartWaterKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStartWaterKeyTyped(evt);
            }
        });
        pnlMeterData.add(txtStartWater);
        txtStartWater.setBounds(260, 70, 70, 20);

        txtEndWater.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEndWaterKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEndWaterKeyTyped(evt);
            }
        });
        pnlMeterData.add(txtEndWater);
        txtEndWater.setBounds(340, 70, 65, 20);

        txtWaterPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWaterPriceKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtWaterPriceKeyTyped(evt);
            }
        });
        pnlMeterData.add(txtWaterPrice);
        txtWaterPrice.setBounds(410, 70, 70, 20);

        txtTotalWater.setEditable(false);
        pnlMeterData.add(txtTotalWater);
        txtTotalWater.setBounds(340, 95, 65, 20);

        txtTotalWaterSum.setEditable(false);
        pnlMeterData.add(txtTotalWaterSum);
        txtTotalWaterSum.setBounds(340, 120, 65, 20);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Start");
        pnlMeterData.add(jLabel17);
        jLabel17.setBounds(20, 50, 70, 20);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("End");
        pnlMeterData.add(jLabel18);
        jLabel18.setBounds(100, 50, 60, 20);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Price");
        pnlMeterData.add(jLabel19);
        jLabel19.setBounds(170, 50, 60, 20);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Start");
        pnlMeterData.add(jLabel20);
        jLabel20.setBounds(260, 50, 60, 20);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("End");
        pnlMeterData.add(jLabel21);
        jLabel21.setBounds(340, 50, 60, 20);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Price");
        pnlMeterData.add(jLabel22);
        jLabel22.setBounds(410, 50, 70, 20);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Total Electricity");
        pnlMeterData.add(jLabel23);
        jLabel23.setBounds(10, 95, 80, 20);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Total Sum");
        pnlMeterData.add(jLabel24);
        jLabel24.setBounds(10, 120, 80, 20);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Total Water");
        pnlMeterData.add(jLabel25);
        jLabel25.setBounds(240, 95, 90, 20);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Total Sum");
        pnlMeterData.add(jLabel26);
        jLabel26.setBounds(240, 120, 90, 20);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Electricity");
        pnlMeterData.add(jLabel27);
        jLabel27.setBounds(20, 30, 210, 20);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Water");
        pnlMeterData.add(jLabel28);
        jLabel28.setBounds(260, 30, 220, 20);

        btnSaveMeterData.setText("Save");
        btnSaveMeterData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveMeterDataActionPerformed(evt);
            }
        });
        pnlMeterData.add(btnSaveMeterData);
        btnSaveMeterData.setBounds(20, 160, 90, 23);

        pnlInformation.setBorder(javax.swing.BorderFactory.createTitledBorder("Information"));

        jLabel29.setText("Hathery ");

        jLabel30.setText("Breder  ");

        btnSaveInfo.setText("Save");
        btnSaveInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInformationLayout = new javax.swing.GroupLayout(pnlInformation);
        pnlInformation.setLayout(pnlInformationLayout);
        pnlInformationLayout.setHorizontalGroup(
                pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlInformationLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlInformationLayout.createSequentialGroup()
                                                .addGroup(pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel29)
                                                        .addComponent(jLabel30))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtHathery)
                                                        .addComponent(txtBreder, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnSaveInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(67, Short.MAX_VALUE))
        );
        pnlInformationLayout.setVerticalGroup(
                pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlInformationLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel29)
                                        .addComponent(txtHathery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel30)
                                        .addComponent(txtBreder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnSaveInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pnlMeterData, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pnlInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pnlEnergy, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(pnlChickQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 58, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlEnergy, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                        .addComponent(pnlChickQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnlMeterData, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(138, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Begin&End", jPanel2);

        pnlTotalDistribution.setBorder(javax.swing.BorderFactory.createTitledBorder("Total Distribution"));
        pnlTotalDistribution.setLayout(null);

        jLabel40.setText("Total Slaughter Male");
        pnlTotalDistribution.add(jLabel40);
        jLabel40.setBounds(20, 70, 98, 14);

        jLabel41.setText("Total Slaughter Female");
        pnlTotalDistribution.add(jLabel41);
        jLabel41.setBounds(20, 100, 110, 14);

        jLabel42.setText("Total Slaughter Birds");
        pnlTotalDistribution.add(jLabel42);
        jLabel42.setBounds(20, 130, 99, 14);

        jLabel43.setText("Total Weight");
        pnlTotalDistribution.add(jLabel43);
        jLabel43.setBounds(20, 160, 61, 14);

        jLabel44.setText("Total Revenues");
        pnlTotalDistribution.add(jLabel44);
        jLabel44.setBounds(20, 190, 75, 14);
        pnlTotalDistribution.add(txtTotSaughtMale);
        txtTotSaughtMale.setBounds(150, 70, 150, 20);
        pnlTotalDistribution.add(txtTotSaughtFemale);
        txtTotSaughtFemale.setBounds(150, 100, 150, 20);
        pnlTotalDistribution.add(txtTotSaughtBirds);
        txtTotSaughtBirds.setBounds(150, 130, 150, 20);
        pnlTotalDistribution.add(txtTotalWeight);
        txtTotalWeight.setBounds(150, 160, 150, 20);
        pnlTotalDistribution.add(txtTotalRevenues);
        txtTotalRevenues.setBounds(150, 190, 150, 20);

        btnDistribution.setText("Add");
        btnDistribution.setMaximumSize(new java.awt.Dimension(60, 25));
        btnDistribution.setMinimumSize(new java.awt.Dimension(60, 25));
        btnDistribution.setPreferredSize(new java.awt.Dimension(60, 25));
        btnDistribution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDistributionActionPerformed(evt);
            }
        });
        pnlTotalDistribution.add(btnDistribution);
        btnDistribution.setBounds(20, 30, 80, 25);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(pnlTotalDistribution, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(484, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(pnlTotalDistribution, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(356, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Distribute", jPanel7);

        pnlSpread.setBorder(javax.swing.BorderFactory.createTitledBorder("Spread"));
        pnlSpread.setLayout(null);

        jLabel31.setText("Total Spread ");
        pnlSpread.add(jLabel31);
        jLabel31.setBounds(50, 70, 70, 14);

        jLabel32.setText("Total Sum");
        pnlSpread.add(jLabel32);
        jLabel32.setBounds(50, 100, 70, 14);
        pnlSpread.add(txtTotalSpread);
        txtTotalSpread.setBounds(130, 70, 120, 20);
        pnlSpread.add(txtTotalSum);
        txtTotalSum.setBounds(130, 100, 120, 20);

        btnSpread.setText("Add");
        btnSpread.setPreferredSize(new java.awt.Dimension(60, 25));
        btnSpread.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpreadActionPerformed(evt);
            }
        });
        pnlSpread.add(btnSpread);
        btnSpread.setBounds(20, 20, 90, 25);

        pnlFeed.setBorder(javax.swing.BorderFactory.createTitledBorder("Feed "));
        pnlFeed.setLayout(null);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Total Feed");
        pnlFeed.add(jLabel33);
        jLabel33.setBounds(71, 58, 102, 14);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Total Expances");
        pnlFeed.add(jLabel34);
        jLabel34.setBounds(71, 78, 102, 14);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("<html>Total Feed Consumption<p> (from controller) </html>");
        pnlFeed.add(jLabel35);
        jLabel35.setBounds(60, 98, 116, 40);
        pnlFeed.add(txtTotalFeed);
        txtTotalFeed.setBounds(190, 50, 130, 20);
        pnlFeed.add(txtTotalExpances);
        txtTotalExpances.setBounds(190, 75, 130, 20);
        pnlFeed.add(txtTotalFeedConsump);
        txtTotalFeedConsump.setBounds(190, 100, 130, 20);

        btnFeed.setText("Add");
        btnFeed.setPreferredSize(new java.awt.Dimension(60, 25));
        btnFeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFeedActionPerformed(evt);
            }
        });
        pnlFeed.add(btnFeed);
        btnFeed.setBounds(20, 20, 90, 25);

        pnlMedicine.setBorder(javax.swing.BorderFactory.createTitledBorder("Medicine "));

        jLabel39.setText("Total");

        btnMedicine.setText("Add");
        btnMedicine.setPreferredSize(new java.awt.Dimension(60, 25));
        btnMedicine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMedicineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMedicineLayout = new javax.swing.GroupLayout(pnlMedicine);
        pnlMedicine.setLayout(pnlMedicineLayout);
        pnlMedicineLayout.setHorizontalGroup(
                pnlMedicineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMedicineLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlMedicineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnMedicine, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalMedicine, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMedicineLayout.setVerticalGroup(
                pnlMedicineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMedicineLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnMedicine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(pnlMedicineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel39)
                                        .addComponent(txtTotalMedicine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnlLabor.setBorder(javax.swing.BorderFactory.createTitledBorder("Labor "));

        btnLabor.setText("Add");
        btnLabor.setPreferredSize(new java.awt.Dimension(60, 25));
        btnLabor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaborActionPerformed(evt);
            }
        });

        jLabel36.setText("Total");

        javax.swing.GroupLayout pnlLaborLayout = new javax.swing.GroupLayout(pnlLabor);
        pnlLabor.setLayout(pnlLaborLayout);
        pnlLaborLayout.setHorizontalGroup(
                pnlLaborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLaborLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlLaborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnLabor, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlLaborLayout.createSequentialGroup()
                                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtTotalLabor, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlLaborLayout.setVerticalGroup(
                pnlLaborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLaborLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(btnLabor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(pnlLaborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel36)
                                        .addComponent(txtTotalLabor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(29, Short.MAX_VALUE))
        );

        pnlTransaction.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction "));

        jLabel37.setText("Total Expenses");

        jLabel38.setText("Total Revenues        ");

        btnTransaction.setText("Add");
        btnTransaction.setPreferredSize(new java.awt.Dimension(60, 25));
        btnTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTransactionLayout = new javax.swing.GroupLayout(pnlTransaction);
        pnlTransaction.setLayout(pnlTransactionLayout);
        pnlTransactionLayout.setHorizontalGroup(
                pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTransactionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlTransactionLayout.createSequentialGroup()
                                                .addGroup(pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(txtTotalExpenses, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(32, 32, 32)
                                                .addGroup(pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtTotalRevenuesTransact, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(52, Short.MAX_VALUE))
        );
        pnlTransactionLayout.setVerticalGroup(
                pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTransactionLayout.createSequentialGroup()
                                .addContainerGap(33, Short.MAX_VALUE)
                                .addComponent(btnTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel37)
                                        .addComponent(jLabel38))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTotalExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTotalRevenuesTransact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlSpread, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pnlLabor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pnlMedicine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlFeed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pnlTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(178, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlSpread, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                        .addComponent(pnlFeed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnlLabor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlMedicine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(193, 193, 193))
        );

        jTabbedPane1.addTab("Expanses", jPanel3);

        pnlSummary.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary"));
        pnlSummary.setLayout(null);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Chicks ");
        pnlSummary.add(jLabel45);
        jLabel45.setBounds(31, 49, 60, 14);
        pnlSummary.add(txtChicks);
        txtChicks.setBounds(109, 46, 110, 20);
        pnlSummary.add(txtElectricity);
        txtElectricity.setBounds(109, 72, 110, 20);
        pnlSummary.add(txtWater);
        txtWater.setBounds(109, 98, 110, 20);
        pnlSummary.add(txtDisel);
        txtDisel.setBounds(109, 124, 110, 20);
        pnlSummary.add(txtGas);
        txtGas.setBounds(109, 150, 110, 20);
        pnlSummary.add(txtSpread);
        txtSpread.setBounds(317, 46, 110, 20);
        pnlSummary.add(txtTransaction);
        txtTransaction.setBounds(317, 150, 110, 20);
        pnlSummary.add(txtLabor);
        txtLabor.setBounds(317, 124, 110, 20);
        pnlSummary.add(txtMedicine);
        txtMedicine.setBounds(317, 98, 110, 20);
        pnlSummary.add(txtFeed);
        txtFeed.setBounds(317, 72, 110, 20);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Electricity ");
        pnlSummary.add(jLabel47);
        jLabel47.setBounds(31, 75, 60, 14);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Water ");
        pnlSummary.add(jLabel48);
        jLabel48.setBounds(31, 101, 60, 14);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Disel ");
        pnlSummary.add(jLabel49);
        jLabel49.setBounds(31, 127, 60, 14);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Gas ");
        pnlSummary.add(jLabel50);
        jLabel50.setBounds(31, 153, 60, 14);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Labor ");
        pnlSummary.add(jLabel51);
        jLabel51.setBounds(237, 127, 62, 14);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("Medicine ");
        pnlSummary.add(jLabel52);
        jLabel52.setBounds(237, 101, 62, 14);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Spread ");
        pnlSummary.add(jLabel53);
        jLabel53.setBounds(237, 49, 62, 14);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Transaction ");
        pnlSummary.add(jLabel54);
        jLabel54.setBounds(237, 153, 62, 14);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("Feed ");
        pnlSummary.add(jLabel55);
        jLabel55.setBounds(237, 76, 62, 14);
        pnlSummary.add(jSeparator1);
        jSeparator1.setBounds(31, 188, 560, 10);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Total Revenues");
        pnlSummary.add(jLabel56);
        jLabel56.setBounds(31, 231, 76, 14);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Total Expenses");
        pnlSummary.add(jLabel57);
        jLabel57.setBounds(31, 255, 76, 14);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("Cost per kg");
        pnlSummary.add(jLabel58);
        jLabel58.setBounds(31, 281, 76, 14);

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel59.setText("Feed conversion");
        pnlSummary.add(jLabel59);
        jLabel59.setBounds(17, 307, 90, 14);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel60.setText("Total ");
        pnlSummary.add(jLabel60);
        jLabel60.setBounds(40, 330, 71, 14);
        pnlSummary.add(txtRevenuesTotal);
        txtRevenuesTotal.setBounds(117, 228, 190, 20);
        pnlSummary.add(txtExpansesTotal);
        txtExpansesTotal.setBounds(117, 252, 190, 20);
        pnlSummary.add(txtCostPerKG);
        txtCostPerKG.setBounds(117, 278, 190, 20);
        pnlSummary.add(txtFeedConversation);
        txtFeedConversation.setBounds(117, 304, 190, 20);
        pnlSummary.add(txtTotal);
        txtTotal.setBounds(117, 330, 190, 20);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(193, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(118, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Summary", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setCurrFlock() {
        List<Flock> flocks = flockService.getFlocks();
        for (Flock f : flocks) {
            if (f.getFlockId().equals(currFlockId)) {
                currFlock = f;
            }
        }
    }

    private void btnSaveHouseParamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveHouseParamActionPerformed
        setCurrFlock();
        Integer area = Integer.parseInt(txtArea.getText());
        Long controllerId = currFlock.getControllerId();
        flockService.saveControllerArea(controllerId, area);

    }//GEN-LAST:event_btnSaveHouseParamActionPerformed

    private void btnSaveEnergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveEnergyActionPerformed
        if (!energyFormValid()) {
            JOptionPane.showMessageDialog(Manager.this, "Field cannot be empty ");
            return;
        }

        int startAmountGas = Integer.parseInt(txtGasStartAmount.getText());
        currFlock.setGasBegin(startAmountGas);
        int leftAmountGas = Integer.parseInt(txtGasLeftAmount.getText());
        currFlock.setGasEnd(leftAmountGas);
        float costGas = Float.parseFloat(txtGasStartPrice.getText());
        currFlock.setCostGas(costGas);
        float costGasEnd = Float.parseFloat(txtGasLeftPrice.getText());
        currFlock.setCostGasEnd(costGasEnd);

        int startAmountFuel = Integer.parseInt(txtFuelStartAmount.getText());
        currFlock.setFuelBegin(startAmountFuel);
        int leftAmountFuel = Integer.parseInt(txtFuelLeftAmount.getText());
        currFlock.setFuelEnd(leftAmountFuel);
        float costFuel = Float.parseFloat(txtFuelStartPrice.getText());
        currFlock.setCostFuel(costFuel);
        float costFuelEnd = Float.parseFloat(txtFuelLeftPrice.getText());
        currFlock.setCostFuelEnd(costFuelEnd);

        flockService.saveFlockDetail(currFlock);
        setCurrFlock();
        viewFlock();
    }//GEN-LAST:event_btnSaveEnergyActionPerformed

    private void btnSaveChickQuantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChickQuantActionPerformed
        setCurrFlock();
        if (!chickQuantFormValid()) {
            JOptionPane.showMessageDialog(Manager.this, "Field cannot be empty ");
            return;
        }

        int quantMale = Integer.parseInt(txtMaleQuant.getText());
        currFlock.setQuantityMale(quantMale);
        int quantFemale = Integer.parseInt(txtFemaleQuant.getText());
        currFlock.setQuantityFemale(quantFemale);

        float costChickMale = Float.parseFloat(txtMaleCost.getText());
        currFlock.setCostChickMale(costChickMale);

        float costChickFemale = Float.parseFloat(txtFemaleCost.getText());
        currFlock.setCostChickFemale(costChickFemale);
        currFlock.setTotalChicks(currFlock.calcTotalChicksCost());
        flockService.saveFlockDetail(currFlock);
        setCurrFlock();
        viewFlock();
    }//GEN-LAST:event_btnSaveChickQuantActionPerformed

    private void btnSaveInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveInfoActionPerformed
        setCurrFlock();
        flockService.saveFlockDetail(currFlock);
    }//GEN-LAST:event_btnSaveInfoActionPerformed

    private void btnSaveMeterDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveMeterDataActionPerformed
        setCurrFlock();
        if (!meterDataFormValid()) {
            JOptionPane.showMessageDialog(Manager.this, "Field cannot be empty ");
            return;
        }

        int startElect = Integer.parseInt(txtStartElect.getText());
        currFlock.setElectBegin(startElect);

        int endElect = Integer.parseInt(txtEndElect.getText());
        currFlock.setElectEnd(endElect);

        float electPrice = Float.parseFloat(txtElectPrice.getText());
        currFlock.setCostElect(electPrice);
        currFlock.setQuantityElect(endElect - startElect);
        currFlock.setTotalElect(currFlock.calcTotalElectCost());

        int startWater = Integer.parseInt(txtStartWater.getText());
        currFlock.setWaterBegin(startWater);
        int endWater = Integer.parseInt(txtEndWater.getText());
        currFlock.setWaterEnd(endWater);
        float waterPrice = Float.parseFloat(txtWaterPrice.getText());
        currFlock.setCostWater(waterPrice);
        currFlock.setQuantityWater(endWater - startWater);
        currFlock.setTotalWater(currFlock.calcTotalWaterCost());
        flockService.saveFlockDetail(currFlock);
        setCurrFlock();
        viewFlock();
    }//GEN-LAST:event_btnSaveMeterDataActionPerformed

    private void btnAddFuelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFuelActionPerformed
        AddFuelDialog addFuelDlg = new AddFuelDialog(currFlockId, this, true);
        addFuelDlg.setVisible(true);
    }//GEN-LAST:event_btnAddFuelActionPerformed

    private void btnAddGasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGasActionPerformed
        AddGasDialog addgasDlg = new AddGasDialog(currFlockId, this, true);
        addgasDlg.setVisible(true);
    }//GEN-LAST:event_btnAddGasActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        viewFlock();
    }//GEN-LAST:event_formWindowActivated

    private void btnSpreadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpreadActionPerformed
        AddSpreadDialog addSpreadDlg = new AddSpreadDialog(currFlockId, this, true);
        addSpreadDlg.setVisible(true);
    }//GEN-LAST:event_btnSpreadActionPerformed

    private void btnFeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFeedActionPerformed
        AddFeedDialog addFeedDlg = new AddFeedDialog(currFlockId, this, true);
        addFeedDlg.setVisible(true);
    }//GEN-LAST:event_btnFeedActionPerformed

    private void btnLaborActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaborActionPerformed
        AddLaborDialog laborDialog = new AddLaborDialog(currFlockId, this, true);
        laborDialog.setVisible(true);
    }//GEN-LAST:event_btnLaborActionPerformed

    private void btnTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionActionPerformed
        AddTransactionDialog transactionDialog = new AddTransactionDialog(currFlockId, this, true);
        transactionDialog.setVisible(true);
    }//GEN-LAST:event_btnTransactionActionPerformed

    private void btnMedicineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMedicineActionPerformed
        AddMedicineDialog medicineDialog = new AddMedicineDialog(currFlockId, this, true);
        medicineDialog.setVisible(true);
    }//GEN-LAST:event_btnMedicineActionPerformed

    private void btnDistributionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDistributionActionPerformed
        AddDistributeDialog distributeDialog = new AddDistributeDialog(currFlockId, this, true);
        distributeDialog.setVisible(true);
    }//GEN-LAST:event_btnDistributionActionPerformed

    private void txtGasStartPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasStartPriceKeyTyped
        FlockGUIUtil.keyTypedHandler(txtGasStartPrice, evt);
    }//GEN-LAST:event_txtGasStartPriceKeyTyped

    private void txtGasLeftPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasLeftPriceKeyTyped
        FlockGUIUtil.keyTypedHandler(txtGasLeftPrice, evt);
    }//GEN-LAST:event_txtGasLeftPriceKeyTyped

    private void txtGasStartPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasStartPriceKeyReleased
        FlockGUIUtil.keyPressedHandler(txtGasStartPrice, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtGasStartPriceKeyReleased

    private void txtGasLeftPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasLeftPriceKeyReleased
        FlockGUIUtil.keyPressedHandler(txtGasLeftPrice, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtGasLeftPriceKeyReleased

    private void txtFuelStartPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuelStartPriceKeyTyped
        FlockGUIUtil.keyTypedHandler(txtFuelStartPrice, evt);
    }//GEN-LAST:event_txtFuelStartPriceKeyTyped

    private void txtFuelStartPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuelStartPriceKeyReleased
        FlockGUIUtil.keyPressedHandler(txtFuelStartPrice, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtFuelStartPriceKeyReleased

    private void txtFuelLeftPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuelLeftPriceKeyTyped
        FlockGUIUtil.keyTypedHandler(txtFuelLeftPrice, evt);
    }//GEN-LAST:event_txtFuelLeftPriceKeyTyped

    private void txtFuelLeftPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuelLeftPriceKeyReleased
        FlockGUIUtil.keyPressedHandler(txtFuelLeftPrice, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtFuelLeftPriceKeyReleased

    private void txtMaleCostKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaleCostKeyTyped
        FlockGUIUtil.keyTypedHandler(txtMaleCost, evt);
    }//GEN-LAST:event_txtMaleCostKeyTyped

    private void txtFemaleCostKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFemaleCostKeyTyped
        FlockGUIUtil.keyTypedHandler(txtFemaleCost, evt);
    }//GEN-LAST:event_txtFemaleCostKeyTyped

    private void txtMaleCostKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaleCostKeyReleased
        FlockGUIUtil.keyPressedHandler(txtMaleCost, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtMaleCostKeyReleased

    private void txtFemaleCostKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFemaleCostKeyReleased
        FlockGUIUtil.keyPressedHandler(txtFemaleCost, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtFemaleCostKeyReleased

    private void txtMaleQuantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaleQuantKeyTyped
        FlockGUIUtil.keyTypedHandler(txtMaleQuant, evt);
    }//GEN-LAST:event_txtMaleQuantKeyTyped

    private void txtMaleQuantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaleQuantKeyReleased
        FlockGUIUtil.keyPressedHandler(txtMaleQuant, evt, DataFormat.DEC_4);
    }//GEN-LAST:event_txtMaleQuantKeyReleased

    private void txtFemaleQuantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFemaleQuantKeyTyped
        FlockGUIUtil.keyTypedHandler(txtFemaleQuant, evt);
    }//GEN-LAST:event_txtFemaleQuantKeyTyped

    private void txtFemaleQuantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFemaleQuantKeyReleased
        FlockGUIUtil.keyPressedHandler(txtFemaleQuant, evt, DataFormat.DEC_4);
    }//GEN-LAST:event_txtFemaleQuantKeyReleased

    private void txtGasStartAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasStartAmountKeyTyped
        FlockGUIUtil.keyTypedHandler(txtGasStartAmount, evt);
    }//GEN-LAST:event_txtGasStartAmountKeyTyped

    private void txtGasStartAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasStartAmountKeyReleased
        FlockGUIUtil.keyPressedHandler(txtGasStartAmount, evt);
    }//GEN-LAST:event_txtGasStartAmountKeyReleased

    private void txtGasLeftAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasLeftAmountKeyTyped
        FlockGUIUtil.keyTypedHandler(txtGasLeftAmount, evt);
    }//GEN-LAST:event_txtGasLeftAmountKeyTyped

    private void txtGasLeftAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGasLeftAmountKeyReleased
        FlockGUIUtil.keyPressedHandler(txtGasLeftAmount, evt);
    }//GEN-LAST:event_txtGasLeftAmountKeyReleased

    private void txtElectPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtElectPriceKeyTyped
        FlockGUIUtil.keyTypedHandler(txtElectPrice, evt);
    }//GEN-LAST:event_txtElectPriceKeyTyped

    private void txtElectPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtElectPriceKeyReleased
        FlockGUIUtil.keyPressedHandler(txtElectPrice, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtElectPriceKeyReleased

    private void txtWaterPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWaterPriceKeyTyped
        FlockGUIUtil.keyTypedHandler(txtWaterPrice, evt);
    }//GEN-LAST:event_txtWaterPriceKeyTyped

    private void txtWaterPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWaterPriceKeyReleased
        FlockGUIUtil.keyPressedHandler(txtWaterPrice, evt, DataFormat.DEC_2);
    }//GEN-LAST:event_txtWaterPriceKeyReleased

    private void txtStartElectKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStartElectKeyTyped
        FlockGUIUtil.keyTypedHandler(txtStartElect, evt);
    }//GEN-LAST:event_txtStartElectKeyTyped

    private void txtStartElectKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStartElectKeyReleased
        FlockGUIUtil.keyPressedHandler(txtStartElect, evt);
    }//GEN-LAST:event_txtStartElectKeyReleased

    private void txtEndElectKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEndElectKeyTyped
        FlockGUIUtil.keyTypedHandler(txtEndElect, evt);
    }//GEN-LAST:event_txtEndElectKeyTyped

    private void txtEndElectKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEndElectKeyReleased
        FlockGUIUtil.keyPressedHandler(txtEndElect, evt);
    }//GEN-LAST:event_txtEndElectKeyReleased

    private void txtStartWaterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStartWaterKeyTyped
        FlockGUIUtil.keyTypedHandler(txtStartWater, evt);
    }//GEN-LAST:event_txtStartWaterKeyTyped

    private void txtStartWaterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStartWaterKeyReleased
        FlockGUIUtil.keyPressedHandler(txtStartWater, evt);
    }//GEN-LAST:event_txtStartWaterKeyReleased

    private void txtEndWaterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEndWaterKeyTyped
        FlockGUIUtil.keyTypedHandler(txtEndWater, evt);
    }//GEN-LAST:event_txtEndWaterKeyTyped

    private void txtEndWaterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEndWaterKeyReleased
        FlockGUIUtil.keyPressedHandler(txtEndWater, evt);
    }//GEN-LAST:event_txtEndWaterKeyReleased

    public void sumAddGas() {
        List<Gas> gasList = flockService.getAllGas(currFlock.getFlockId());
        int gasAmount = 0;
        float gasTotalCost = 0;
        for (Gas g : gasList) {
            gasAmount += g.getAmount();
            gasTotalCost += g.getTotal();
        }
        currFlock.setGasAdd(gasAmount);
        currFlock.setTotalGas(gasTotalCost);
        flockService.saveFlockDetail(currFlock);
    }

    public void sumAddFeed() {
        List<Feed> feedList = flockService.getAllFeed(currFlock.getFlockId());
        int feedAmount = 0;
        float feedTotalCost = 0;
        for (Feed f : feedList) {
            feedAmount += f.getAmount();
            feedTotalCost += f.getTotal();
        }
        currFlock.setFeedAdd(feedAmount);
        currFlock.setTotalFeed(feedTotalCost);
        flockService.saveFlockDetail(currFlock);

    }

    public void sumAddFuel() {
        List<Fuel> fuelList = flockService.getAllFuel(currFlock.getFlockId());
        int fuelAmount = 0;
        float fuelTotalCost = 0;
        for (Fuel f : fuelList) {
            fuelAmount += f.getAmount();
            fuelTotalCost += f.getTotal();
        }
        currFlock.setFuelAdd(fuelAmount);
        currFlock.setTotalFuel(fuelTotalCost);
        flockService.saveFlockDetail(currFlock);
    }

    public void sumAddSpread() {
        List<Spread> spreadList = flockService.getAllSpread(currFlock.getFlockId());
        int spreadAmount = 0;
        float spreadTotalCost = 0;
        for (Spread s : spreadList) {
            spreadAmount += s.getAmount();
            spreadTotalCost += s.getTotal();
        }
        currFlock.setSpreadAdd(spreadAmount);
        currFlock.setTotalSpread(spreadTotalCost);
        flockService.saveFlockDetail(currFlock);
    }

    public void sumAddMedicine() {
        List<Medicine> spreadList = flockService.getAllMedicine(currFlock.getFlockId());
        float totalMedic = 0;
        for (Medicine m : spreadList) {
            totalMedic += m.getTotal();
        }
        currFlock.setTotalMedic(totalMedic);
        flockService.saveFlockDetail(currFlock);
    }

    private void sumAddLabor() {
        List<Labor> spreadList = flockService.getAllLabor(currFlock.getFlockId());
        float totalSalary = 0;
        for (Labor m : spreadList) {
            totalSalary += m.getSalary();
        }
        currFlock.setTotalLabor(totalSalary);
        flockService.saveFlockDetail(currFlock);
    }

    private void sumAddTransaction() {
        float exp = 0;
        float rev = 0;
        List<Transaction> transactList = flockService.getAllTransaction(currFlockId);
        for (Transaction t : transactList) {
            exp += t.getExpenses();
            rev += t.getRevenues();
        }
        currFlock.setExpenses(exp);
        currFlock.setRevenues(rev);
        flockService.saveFlockDetail(currFlock);
    }

    private void sumAddDistribute() {
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
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Manager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFuel;
    private javax.swing.JButton btnAddGas;
    private javax.swing.JButton btnDistribution;
    private javax.swing.JButton btnFeed;
    private javax.swing.JButton btnLabor;
    private javax.swing.JButton btnMedicine;
    private javax.swing.JButton btnSaveChickQuant;
    private javax.swing.JButton btnSaveEnergy;
    private javax.swing.JButton btnSaveHouseParam;
    private javax.swing.JButton btnSaveInfo;
    private javax.swing.JButton btnSaveMeterData;
    private javax.swing.JButton btnSpread;
    private javax.swing.JButton btnTransaction;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel pnlChickQuantity;
    private javax.swing.JPanel pnlEnergy;
    private javax.swing.JPanel pnlFeed;
    private javax.swing.JPanel pnlHousesParameter;
    private javax.swing.JPanel pnlInformation;
    private javax.swing.JPanel pnlLabor;
    private javax.swing.JPanel pnlMedicine;
    private javax.swing.JPanel pnlMeterData;
    private javax.swing.JPanel pnlSpread;
    private javax.swing.JPanel pnlSummary;
    private javax.swing.JPanel pnlTotalDistribution;
    private javax.swing.JPanel pnlTransaction;
    private javax.swing.JTextField txtArea;
    private javax.swing.JTextField txtBreder;
    private javax.swing.JTextField txtChicks;
    private javax.swing.JTextField txtCostPerKG;
    private javax.swing.JTextField txtDisel;
    private javax.swing.JTextField txtElectPrice;
    private javax.swing.JTextField txtElectricity;
    private javax.swing.JTextField txtEndElect;
    private javax.swing.JTextField txtEndWater;
    private javax.swing.JTextField txtExpansesTotal;
    private javax.swing.JTextField txtFeed;
    private javax.swing.JTextField txtFeedConversation;
    private javax.swing.JTextField txtFemaleCost;
    private javax.swing.JTextField txtFemaleQuant;
    private javax.swing.JTextField txtFuelLeftAmount;
    private javax.swing.JTextField txtFuelLeftPrice;
    private javax.swing.JTextField txtFuelStartAmount;
    private javax.swing.JTextField txtFuelStartPrice;
    private javax.swing.JTextField txtFuelTotalAmount;
    private javax.swing.JTextField txtFuelTotalPrice;
    private javax.swing.JTextField txtGas;
    private javax.swing.JTextField txtGasLeftAmount;
    private javax.swing.JTextField txtGasLeftPrice;
    private javax.swing.JTextField txtGasStartAmount;
    private javax.swing.JTextField txtGasStartPrice;
    private javax.swing.JTextField txtGasTotalAmount;
    private javax.swing.JTextField txtGasTotalPrice;
    private javax.swing.JTextField txtHathery;
    private javax.swing.JTextField txtLabor;
    private javax.swing.JTextField txtMaleCost;
    private javax.swing.JTextField txtMaleQuant;
    private javax.swing.JTextField txtMedicine;
    private javax.swing.JTextField txtRevenuesTotal;
    private javax.swing.JTextField txtSpread;
    private javax.swing.JTextField txtStartElect;
    private javax.swing.JTextField txtStartWater;
    private javax.swing.JTextField txtTotSaughtBirds;
    private javax.swing.JTextField txtTotSaughtFemale;
    private javax.swing.JTextField txtTotSaughtMale;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalCost;
    private javax.swing.JTextField txtTotalElect;
    private javax.swing.JTextField txtTotalElectSum;
    private javax.swing.JTextField txtTotalExpances;
    private javax.swing.JTextField txtTotalExpenses;
    private javax.swing.JTextField txtTotalFeed;
    private javax.swing.JTextField txtTotalFeedConsump;
    private javax.swing.JTextField txtTotalLabor;
    private javax.swing.JTextField txtTotalMedicine;
    private javax.swing.JTextField txtTotalQuant;
    private javax.swing.JTextField txtTotalRevenues;
    private javax.swing.JTextField txtTotalRevenuesTransact;
    private javax.swing.JTextField txtTotalSpread;
    private javax.swing.JTextField txtTotalSum;
    private javax.swing.JTextField txtTotalWater;
    private javax.swing.JTextField txtTotalWaterSum;
    private javax.swing.JTextField txtTotalWeight;
    private javax.swing.JTextField txtTransaction;
    private javax.swing.JTextField txtWater;
    private javax.swing.JTextField txtWaterPrice;
    // End of variables declaration//GEN-END:variables

    private boolean energyFormValid() {
        if (txtGasStartAmount.getText().length() == 0
                || txtGasLeftAmount.getText().length() == 0
                || txtGasStartPrice.getText().length() == 0
                || txtGasLeftPrice.getText().length() == 0
                || txtFuelStartAmount.getText().length() == 0
                || txtFuelLeftAmount.getText().length() == 0
                || txtFuelStartPrice.getText().length() == 0
                || txtFuelLeftPrice.getText().length() == 0) {
            return false;
        }
        return true;
    }

    private boolean meterDataFormValid() {
        if (txtStartElect.getText().length() == 0
                || txtEndElect.getText().length() == 0
                || txtElectPrice.getText().length() == 0
                || txtStartWater.getText().length() == 0
                || txtEndWater.getText().length() == 0
                || txtWaterPrice.getText().length() == 0) {
            return false;

        }
        return true;
    }

    private boolean chickQuantFormValid() {
        if (txtMaleQuant.getText().length() == 0
                || txtFemaleQuant.getText().length() == 0
                || txtMaleCost.getText().length() == 0
                || txtFemaleCost.getText().length() == 0) {
            return false;
        }
        return true;
    }
}
