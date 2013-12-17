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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Graphs24HourPanel extends JPanel {

    private int width = 800;
    private int height = 550;
    private long currControllerId;
    private ControllerDao controllerDao;
    private DataDao dataDao;
    private List<ChartPanel> chartPanels;
    private Timer timerDB;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Creates new form Graphs24HourPanel
     */
    public Graphs24HourPanel(Long controllerId) {
        try {
            initComponents();
            controllerDao = DbImplDecider.use(DaoType.DERBY).getDao(ControllerDao.class);
            dataDao = DbImplDecider.use(DaoType.DERBY).getDao(DataDao.class);
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

        setSize(width, (height + 10) * 2);
    }

    /**
     * @param controllerId
     * @throws SQLException
     */
    public void createGraph(Long controllerId) throws SQLException {
        currControllerId = controllerId;
        String values = controllerDao.getControllerGraph(controllerId);
        Controller controller = controllerDao.getById(controllerId);
        Data setClock = dataDao.getSetClockByController(controllerId);
        if (controller.getName().contains("616")) {
            if (values != null) {
                AbstractGraph graph;
                Locale locale = getCurrentLocale();
                graph = new Graph24InputTemp(GraphType.IN_OUT_TEMP_HUMID, values, Long.valueOf("0"), locale);
                createAndAddChartPanel(graph);
            }
        } else {
            if (values != null && values.length() >= AbstractGraph.LENGHT) {
                AbstractGraph graph;
                Locale locale = getCurrentLocale();
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
    }

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
        ChartPanel chartpanel = new ChartPanel(graph.createChart(), width, height, width, height, width, height,
                false, true, true, true, true, true);
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
