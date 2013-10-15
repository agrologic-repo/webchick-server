package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.DataGraphCreator;
import com.agrologic.app.graph.daily.Graph24Empty;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.service.FlockHistoryService;
import org.jfree.chart.ChartUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphHeatOnTimeServlet extends AbstractServlet {

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

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                GrowDayRangeParam growDayRangeParam
                        = new GrowDayRangeParam(request.getParameter("fromDay"), request.getParameter("toDay"));

                try {
                    FlockHistoryService flockHistoryService = new FlockHistoryService();
                    Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockHistoryWithinRange(flockId, growDayRangeParam);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data1 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_1_TIME_ON.getId());

                    List<String> axisTitles = new ArrayList<String>();
                    axisTitles.add(data1.getLabel());

                    List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

                    Data data2 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_2_TIME_ON.getId());

                    axisTitles.add(data2.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

                    Data data3 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_3_TIME_ON.getId());

                    axisTitles.add(data3.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

                    Data data4 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_4_TIME_ON.getId());

                    axisTitles.add(data4.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));

                    Data data5 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_5_TIME_ON.getId());

                    axisTitles.add(data5.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data5));

                    Data data6 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_6_TIME_ON.getId());

                    axisTitles.add(data6.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data6));

                    String title = "Heat On Time";
                    String xAxisLabel = "Grow Day[Day]";
                    String yAxisLabel = "Time[Minute]";
                    HistoryGraph heatontimeGraph = new HistoryGraph();
                    heatontimeGraph.setDataHistoryList(dataHistoryList);
                    heatontimeGraph.createChart(title, xAxisLabel, yAxisLabel);
                    ChartUtilities.writeChartAsPNG(out, heatontimeGraph.getChart(), 800, 600);
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    logger.error("Unknown error. ", ex);
                    Graph24Empty graph = new Graph24Empty(GraphType.BLANK, "");
                    ChartUtilities.writeChartAsPNG(out, graph.getChart(), 600, 300);
                    out.flush();
                    out.close();
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



