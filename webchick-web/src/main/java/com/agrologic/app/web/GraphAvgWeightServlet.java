
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.DataGraphCreator;
import com.agrologic.app.graph.daily.EmptyGraph;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.history.FromDayToDay;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import org.jfree.chart.ChartUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphAvgWeightServlet extends AbstractServlet {
    public static String title = "Average Weight";
    public static String xAxisLabel = "Grow Day[Day]";
    public static String yAxisLabel = "Weight[KG]";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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

        try {
            long flockId = Long.parseLong(request.getParameter("flockId"));
            long langId = getInSessionLanguageId(request);
            FromDayToDay growDayRangeParam = new FromDayToDay(request.getParameter("fromDay"),
                    request.getParameter("toDay"));

            try {

                FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
                Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId,
                        growDayRangeParam);

                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

                Data data1 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_1_ID.getId());
                dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

                Data data2 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_2_ID.getId());
                dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

                Data data3 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_3_ID.getId());
                dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

                Data data4 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_4_ID.getId());
                dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));


                List<String> axisTitles = new ArrayList<String>();
                axisTitles.add(data1.getLabel());
                axisTitles.add(data2.getLabel());
                axisTitles.add(data3.getLabel());
                axisTitles.add(data4.getLabel());

                HistoryGraph averageWeightGraph = new HistoryGraph();
                averageWeightGraph.setDataHistoryList(dataHistoryList);
                averageWeightGraph.createChart(title, xAxisLabel, yAxisLabel);

                request.setAttribute("fromDay", growDayRangeParam.getFromDay());
                request.setAttribute("toDay", growDayRangeParam.getToDay());
                ChartUtilities.writeChartAsPNG(out, averageWeightGraph.getChart(), 800, 600);
                out.flush();
                out.close();
            } catch (Exception ex) {
                logger.error("Unknown error. ", ex);
                EmptyGraph graph = new EmptyGraph();
                ChartUtilities.writeChartAsPNG(out, graph.getChart(), 600, 300);
                out.flush();
                out.close();
            }
        } finally {
            out.close();
        }
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



