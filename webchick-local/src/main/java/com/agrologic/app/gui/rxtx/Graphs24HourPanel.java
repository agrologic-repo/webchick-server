/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.gui.graph.AbstractGraph;
import com.agrologic.app.gui.graph.Graph24FWI;
import com.agrologic.app.gui.graph.Graph24IOH;
import com.agrologic.app.gui.graph.GraphType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Administrator
 */
public class Graphs24HourPanel extends JPanel {

    private int width = 800;
    private int height = 600;
    private long currControllerId;
    private ControllerDao controllerDao;
    private DataDao dataDao;
    private List<ChartPanel> chartPanels;
    private Timer timerDB;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final static Map<String, Locale> LANGUGES = new HashMap<String, Locale>();

    static {
        LANGUGES.put("English", Locale.ENGLISH);
        LANGUGES.put("Hebrew", new Locale("iw", "IL"));
        LANGUGES.put("Russian", new Locale("ru", "RU"));
        LANGUGES.put("Chinese", new Locale("zh", "CN"));
        LANGUGES.put("French", new Locale("fr", "FR"));
        LANGUGES.put("Germany", Locale.GERMANY);
    }

    /**
     * Creates new form RealtimeGraphScreen
     */
    public Graphs24HourPanel(Long controllerId) {
        try {
            initComponents();
            controllerDao = DbImplDecider.getDaoFactory(DaoType.DERBY).getControllerDao();
            dataDao = DbImplDecider.getDaoFactory(DaoType.DERBY).getDataDao();
            chartPanels = new ArrayList<ChartPanel>();
            createGraph(controllerId);
            repaint();
            timerDB = new javax.swing.Timer(ScreenUI.REFRESH_RATE, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    executeUpdate();
                }
            });
            timerDB.start();
        } catch (SQLException ex) {
            Logger.getLogger(Graphs24HourPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createGraph(Long controllerId) throws SQLException {
        currControllerId = controllerId;
        String values = controllerDao.getControllerGraph(controllerId);
        Data setClock = dataDao.getSetClockByController(controllerId);
        if (values != null && values.length() >= AbstractGraph.LENGHT) {
            AbstractGraph graph = null;

            Locale locale = getLocal();

            if (setClock == null || setClock.getValue() == null) {
                graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, values, Long.valueOf("0"), locale);
                createAndAddChartPanel(graph);
                graph = new Graph24FWI(GraphType.IN_FEED_WATER, values, Long.valueOf("0"), locale);
                createAndAddChartPanel(graph);
            } else {
                long value = DataFormat.convertToTimeFormat(setClock.getValue());
                graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, values, value, locale);
                createAndAddChartPanel(graph);
                graph = new Graph24FWI(GraphType.IN_FEED_WATER, values, value, locale);
                createAndAddChartPanel(graph);
            }
        }
    }

    public Locale getLocal() {
        Configuration configuration = new Configuration();
        return LANGUGES.get(configuration.getLanguage());
    }

    /**
     * Creates chart panel and add to screen
     *
     * @param graph the created graph
     */
    private void createAndAddChartPanel(AbstractGraph graph) {
        ChartPanel chartpanel = null;
        chartpanel = new ChartPanel(graph.createChart());
        chartpanel.setSize(width, height);
        chartpanel.setVisible(true);
        add(chartpanel);
        chartPanels.add(chartpanel);
    }

    private boolean graphShouldBeUpdated() throws SQLException {
        final Timestamp updateTime = controllerDao.getUpdatedGraphTime(currControllerId);
        if (updateTime == null) {
            return true;
        }

        final long timeSinceUpdated = System.currentTimeMillis() - updateTime.getTime();
        if (timeSinceUpdated > (60 * 60 * 1000)) {
            return true;
        }

        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public synchronized void executeUpdate() {
        Runnable task = new SwingWorker() {

            String values;

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    values = controllerDao.getControllerGraph(currControllerId);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (graphShouldBeUpdated() == true) {
                        removeAll();
                        chartPanels.clear();
                        createGraph(currControllerId);
                    }
                } catch (SQLException ex) {
                }
            }
        };
        executorService.execute(task);
    }
}