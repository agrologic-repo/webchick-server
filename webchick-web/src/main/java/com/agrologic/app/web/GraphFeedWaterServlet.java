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

public class GraphFeedWaterServlet extends AbstractServlet {
    public static final String FEED_KG_AXIST_TITLE = "Feed[KG]";
    public static final String FEED_AND_WATER_CONSUMPTION_TITLE = "Feed And Water Consumption";
    public static final String GROW__DAY_AXIS_TITLE = "Grow Day[Day]";
    public static final String WATER_LITER_AXIS_TITLE = "Water[Liter]";

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
                FromDayToDay growDayRangeParam = new FromDayToDay(request.getParameter("fromDay"),
                        request.getParameter("toDay"));

                try {
                    FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
                    Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

                    List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data1 = dataDao.getById(PerGrowDayHistoryDataType.FEED_CONSUMPTION_ID.getId());

                    List<String> axisTitles = new ArrayList<String>();
                    axisTitles.add(data1.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

                    HistoryGraph waterFeedGraph = new HistoryGraph();
                    waterFeedGraph.setDataHistoryList(dataHistoryList);
                    waterFeedGraph.createChart(FEED_AND_WATER_CONSUMPTION_TITLE, GROW__DAY_AXIS_TITLE, FEED_KG_AXIST_TITLE);

                    Data data2 = dataDao.getById(PerGrowDayHistoryDataType.WATER_CONSUMPTION_ID.getId());
                    axisTitles.add(data2.getLabel());
                    dataHistoryList.clear();
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
                    waterFeedGraph.createAndAddSeriesCollection(dataHistoryList, WATER_LITER_AXIS_TITLE);
                    ChartUtilities.writeChartAsPNG(out, waterFeedGraph.getChart(), 800, 600);
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    logger.error("Unknown error. ", ex);
                    EmptyGraph graph = new EmptyGraph();
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



