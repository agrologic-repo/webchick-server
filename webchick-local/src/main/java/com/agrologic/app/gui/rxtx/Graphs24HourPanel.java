package com.agrologic.app.gui.rxtx;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.gui.rxtx.graph.*;
import com.agrologic.app.i18n.LocaleManager;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Graphs24HourPanel extends JPanel {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Locale locale;
    private int width = 800;
    private int height = 550;
    private String datasetString;
    private Controller controller;
    private long currControllerId;
    private ControllerDao controllerDao;
    private DataDao dataDao;
    private List<ChartPanel> chartPanels;
    private AbstractGraph graph;

    /**
     * Creates new form Graphs24HourPanel
     */
    public Graphs24HourPanel(Long controllerId) {
        super(new BorderLayout(5, 5));
        try {
            initComponents();
            locale = getCurrentLocale();
            controllerDao = DbImplDecider.use(DaoType.DERBY).getDao(ControllerDao.class);
            dataDao = DbImplDecider.use(DaoType.DERBY).getDao(DataDao.class);
            chartPanels = new ArrayList<ChartPanel>();
            currControllerId = controllerId;
            loadDatasetString();
            createGraph();
            this.setLayout(new GridLayout(0,2));////
            repaint();

            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (graphShouldBeUpdated() == true) {
                            loadDatasetString();
                            Data setClock = dataDao.getSetClockByController(currControllerId);
                            long value = DataFormat.convertToTimeFormat(setClock.getValue());
                            graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, datasetString, value, locale);
                            ((ChartPanel) chartPanels.get(0)).setChart(graph.createChart());
                            graph = new Graph24FWI(GraphType.IN_FEED_WATER, datasetString, value, locale);
                            ((ChartPanel) chartPanels.get(1)).setChart(graph.createChart());
                            graph = new Graph24FWPB(GraphType.FEED_WATER_PER_BIRD, datasetString, value, locale);
                            ((ChartPanel) chartPanels.get(2)).setChart(graph.createChart());
                            graph = new Graph24F2W2(GraphType.FEED2_WATER2, datasetString, value, locale);
                            ((ChartPanel) chartPanels.get(3)).setChart(graph.createChart());
                            graph = new Graph24WS(GraphType.WATER_SUM, datasetString, value, locale);
                            ((ChartPanel) chartPanels.get(4)).setChart(graph.createChart());
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        setSize(width, (height + 10) * 2);
    }

    private void loadDatasetString() throws SQLException {
        datasetString = controllerDao.getControllerGraph(currControllerId);
        controller = controllerDao.getById(currControllerId);
        if (datasetString == null) {
            datasetString = "";
        }
    }

    public void createGraph() throws SQLException {
        Data setClock = dataDao.getSetClockByController(currControllerId);

        if (controller.getName().contains("616")) {
            if (datasetString != null) {
                graph = new Graph24InputTemp(GraphType.IN_OUT_TEMP_HUMID, datasetString, Long.valueOf("0"), locale);
                ChartPanel chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height,false, true, true, true, true, true);
                chartpanel.setVisible(true);
//                add(chartpanel, BorderLayout.PAGE_START);
                chartPanels.add(chartpanel);
            }
        } else {
            if (datasetString != null && datasetString.length() >= AbstractGraph.LENGHT) {
                if (setClock == null || setClock.getValue() == null) {
                    graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, datasetString, Long.valueOf("0"), locale);
                    ChartPanel chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height,false, true, true, true, true, true);
                    chartpanel.setVisible(true);
//                    add(chartpanel, BorderLayout.PAGE_START);
                    chartPanels.add(chartpanel);
                    graph = new Graph24FWI(GraphType.IN_FEED_WATER, datasetString, Long.valueOf("0"), locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
//                    add(chartpanel, BorderLayout.PAGE_END);
                    chartPanels.add(chartpanel);
                    graph = new Graph24FWPB(GraphType.FEED_WATER_PER_BIRD, datasetString, Long.valueOf("0"), locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                    graph = new Graph24F2W2(GraphType.FEED2_WATER2, datasetString, Long.valueOf("0"), locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                    graph = new Graph24WS(GraphType.WATER_SUM, datasetString, Long.valueOf("0"), locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                } else {
                    long value = DataFormat.convertToTimeFormat(setClock.getValue());
                    graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, datasetString, value, locale);
                    ChartPanel chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
//                    add(chartpanel, BorderLayout.PAGE_START);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                    graph = new Graph24FWI(GraphType.IN_FEED_WATER, datasetString, value, locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
//                    add(chartpanel, BorderLayout.PAGE_END);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                    graph = new Graph24FWPB(GraphType.FEED_WATER_PER_BIRD, datasetString, value, locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                    graph = new Graph24F2W2(GraphType.FEED2_WATER2, datasetString, value, locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                    graph = new Graph24WS(GraphType.WATER_SUM, datasetString, value, locale);
                    chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
                    chartpanel.setVisible(true);
                    add(chartpanel);
                    chartPanels.add(chartpanel);
                }
            }
        }
    }

    /**
     * Return current locale
     *
     * @return
     */
    public Locale getCurrentLocale() {
        Configuration configuration = new Configuration();
        LocaleManager localeManager = new LocaleManager();
        localeManager.setCurrentLanguage(configuration.getLanguage());
        return localeManager.getCurrentLocale();
    }

    /**
     * Creates chart panel and add to screen
     *
     * @param graph the created graph
     */
    private void createAndAddChartPanel(AbstractGraph graph) {
        ChartPanel chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height, false, true, true, true, true, true);
        chartpanel.setVisible(true);
        chartpanel.getComponent(0);
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
}
