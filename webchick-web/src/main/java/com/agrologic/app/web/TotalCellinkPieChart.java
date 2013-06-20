
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class TotalCellinkPieChart extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(TotalCellinkPieChart.class);

        response.setContentType("text/html;charset=UTF-8");

        OutputStream out = response.getOutputStream();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                try {
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    JFreeChart jfc;
                    CellinkDao cellinkDao = new CellinkDaoImpl();
                    final int offline = cellinkDao.getAll(CellinkState.STATE_OFFLINE).size();
                    final int running = cellinkDao.getAll(CellinkState.STATE_RUNNING).size();
                    final int online = cellinkDao.getAll(CellinkState.STATE_START).size()
                            + cellinkDao.getAll(CellinkState.STATE_ONLINE).size();

                    dataset = new DefaultPieDataset();
                    dataset.setValue("Online", new Integer(online));
                    dataset.setValue("Offline", new Integer(offline));
                    dataset.setValue("Running", new Integer(running));
                    jfc = ChartFactory.createPieChart3D("Total Cellinks Summary", dataset, true, false, false);

                    PiePlot pp = (PiePlot) jfc.getPlot();

                    pp.setSectionOutlinesVisible(false);
                    pp.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
                    pp.setCircular(false);
                    pp.setIgnoreNullValues(false);
                    pp.setIgnoreZeroValues(true);
                    pp.setInteriorGap(0.05D);
                    pp.setSimpleLabels(true);

                    // disable caching
                    response.setHeader("Cache-Control", "no-cache");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);
                    response.setContentType("image/png");
                    ChartUtilities.writeChartAsPNG(out, jfc, 300, 200);
                    out.flush();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                }
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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



