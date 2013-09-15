
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.graph.daily.Graph;
import com.agrologic.app.graph.daily.Graph24Empty;
import com.agrologic.app.graph.daily.Graph24FWI;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.Flock;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * @author JanL
 */
public class Graph24HourFWServlet extends HttpServlet {
    public Graph24HourFWServlet() {
        super();
    }

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
        final Logger logger = Logger.getLogger(Graph24HourFWServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        OutputStream out = response.getOutputStream();
        long flockId = Long.parseLong(request.getParameter("flockId"));
        int growDay;
        StringBuilder range = new StringBuilder();

        try {
            String gd = request.getParameter("growDay");
            growDay = Integer.parseInt(request.getParameter("growDay"));
            range.append("( Grow day ").append(growDay);
        } catch (Exception ex) {
            growDay = 1;
        }

        try {
            FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
            Flock flock = flockDao.getById(flockId);
            Long resetTime = new Long(flockDao.getResetTime(flockId, growDay));
            if (resetTime != null) {
                resetTime = DataFormat.convertToTimeFormat(resetTime);
            } else {
                resetTime = Long.valueOf("0");
            }

            String values1 = flockDao.getHistory24(flockId, growDay, "D18");
            String values2 = flockDao.getHistory24(flockId, growDay, "D19");
            String values3 = flockDao.getHistory24(flockId, growDay, "D20");
            String values4 = flockDao.getHistory24(flockId, growDay, "D21");
            String values5 = flockDao.getHistory24(flockId, growDay, "D72");

            values1 = getDefaultValuesIfEmpty(values1);
            values2 = getDefaultValuesIfEmpty(values2);
            values3 = getDefaultValuesIfEmpty(values3);
            values4 = getDefaultValuesIfEmpty(values4);
            values5 = getDefaultValuesIfEmpty(values5);

            StringBuilder sb = new StringBuilder();

            sb.append(values1).append(values2).append(values3).append(values4).append(values5);

            Locale locale = Locale.ENGLISH;
            Graph graph = new Graph24FWI(GraphType.IN_FEED_WATER, sb.toString(), resetTime, locale);

            ChartUtilities.writeChartAsPNG(out, graph.createChart(), 800, 600);
            out.flush();
            out.close();
        } catch (Exception ex) {
            Graph24Empty graph = new Graph24Empty(GraphType.BLANK, "");

            ChartUtilities.writeChartAsPNG(out, graph.getChart(), 600, 300);
            out.flush();
            out.close();
        }
    }

    public String getDefaultValuesIfEmpty(String values) {
        if (values.equals("-1 ") || values.equals("")) {
            values = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
        }

        return values;
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



