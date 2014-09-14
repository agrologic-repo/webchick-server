package com.agrologic.app.web;

import com.agrologic.app.graph.daily.EmptyGraph;
import com.agrologic.app.graph.daily.FeedWaterConsumpTemp;
import com.agrologic.app.graph.daily.PerHourReportGraph;
import com.agrologic.app.model.history.DayParam;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import org.jfree.chart.ChartUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;

public class Graph24HourFWServlet extends AbstractServlet {
    private Locale locale = Locale.ENGLISH;

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
        response.setContentType("text/html;charset=UTF-8");

        OutputStream out = response.getOutputStream();
        long flockId = Long.parseLong(request.getParameter("flockId"));
        long langId = getInSessionLanguageId(request);

        DayParam growDayParam = new DayParam(request.getParameter("growDay"));

        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Collection<String> historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjects(flockId,
                    growDayParam.getGrowDay(), langId);
            StringBuilder chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
                chainedHistoryValues.append(historyValues);
            }
            Long resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            PerHourReportGraph perHourReportGraph = new FeedWaterConsumpTemp(chainedHistoryValues.toString(), resetTime
                    , locale);

            ChartUtilities.writeChartAsPNG(out, perHourReportGraph.createChart(), 800, 600);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            EmptyGraph graph = new EmptyGraph();

            ChartUtilities.writeChartAsPNG(out, graph.getChart(), 600, 300);
            out.flush();
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



