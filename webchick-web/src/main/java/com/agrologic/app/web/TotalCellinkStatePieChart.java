
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Cellink;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Collection;

public class TotalCellinkStatePieChart extends AbstractServlet {

    private static final long serialVersionUID = 1177210028733322431L;
    private DefaultPieDataset dataset = new DefaultPieDataset();
    // private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private JFreeChart jfc;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        setValues(cellinkDao);

        // jfc = ChartFactory.createPieChart("Cellinks Grouped By States", dataset, true, true, false);
        jfc = ChartFactory.createPieChart("", dataset, false, false, false);

        // jfc = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.HORIZONTAL, false, false, false);//PieChart("", dataset, false, false, false);
        // jfc.setTitle(new TextTitle("Cellinks Grouped By States", new Font("SansSerif", Font.PLAIN, 13)));
//      CategoryPlot cp = jfc.getCategoryPlot();
//      cp.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
//      cp.setRangePannable(true);
//      cp.setRangeZeroBaselineVisible(true);
//      NumberAxis localNumberAxis = (NumberAxis)cp.getRangeAxis();
//      localNumberAxis.setUpperMargin(0.25D);
//      BarRenderer localBarRenderer = (BarRenderer)cp.getRenderer();
//      localBarRenderer.setBaseItemLabelsVisible(true);
//      localBarRenderer.setItemLabelAnchorOffset(7.0D);

        PiePlot pp = (PiePlot) jfc.getPlot();

        pp.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        pp.setBackgroundPaint(Color.WHITE);

//      pp.setSectionOutlinesVisible(true);
        pp.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        pp.setNoDataMessage("No data");
        pp.setCircular(false);
        pp.setSectionPaint("Offline", Color.RED);
        pp.setSectionPaint("Running", Color.BLUE);
        pp.setSectionPaint("Online", Color.GREEN);
        pp.setSimpleLabels(true);
        pp.setInteriorGap(0.0D);
        jfc.setBackgroundPaint(Color.WHITE);
        pp.setLabelGap(0.15);

        // disable caching
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        OutputStream out = response.getOutputStream();

        ChartUtilities.writeChartAsPNG(out, jfc, 100, 100);
        out.flush();
        out.close();
    }

    private void setValues(CellinkDao cellinkDao) {
        int online = 0;
        int offline = 0;
        int running = 0;
        try {
            Collection<Cellink> cellinks = cellinkDao.getAll();
            for (Cellink cellink : cellinks) {
                switch (cellink.getState()) {
                    case CellinkState.STATE_ONLINE:
                        online++;
                        break;
                    case CellinkState.STATE_UNKNOWN:
                    case CellinkState.STATE_START:
                    case CellinkState.STATE_STOP:
                    case CellinkState.STATE_OFFLINE:
                        offline++;
                        break;
                    case CellinkState.STATE_RUNNING:
                        running++;
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataset.setValue("Offline", new Integer(offline));
        dataset.setValue("Running", new Integer(running));
        dataset.setValue("Online", new Integer(online));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>
}
