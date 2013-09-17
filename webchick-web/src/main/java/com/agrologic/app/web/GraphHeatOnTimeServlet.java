
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.graph.DataGraphCreator;
import com.agrologic.app.graph.daily.Graph24Empty;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.model.Data;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphHeatOnTimeServlet extends HttpServlet {


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
        final Logger logger = Logger.getLogger(GraphAvgWeightServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        OutputStream out = response.getOutputStream();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                int fromDay = -1;
                int toDay = -1;
                StringBuilder range = new StringBuilder();

                try {
                    fromDay = Integer.parseInt(request.getParameter("fromDay"));
                    toDay = Integer.parseInt(request.getParameter("toDay"));

                    if ((fromDay != -1) && (toDay != -1)) {
                        range.append("( From ").append(fromDay).append(" to ").append(toDay).append(" grow day .)");
                    }
                } catch (Exception ex) {
                    fromDay = -1;
                    toDay = -1;
                }

                try {
                    FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                    Map<Integer, String> historyByGrowDay = flockDao.getAllHistoryByFlock(flockId, fromDay, toDay);
                    List<Map<Integer, Data>> dataHistroryList = new ArrayList<Map<Integer, Data>>();
                    List<String> axisTitles = new ArrayList<String>();
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data1 = dataDao.getById(Long.valueOf(1303));

                    axisTitles.add(data1.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

                    Data data2 = dataDao.getById(Long.valueOf(1304));

                    axisTitles.add(data2.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

                    Data data3 = dataDao.getById(Long.valueOf(1305));

                    axisTitles.add(data3.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

                    Data data4 = dataDao.getById(Long.valueOf(1306));

                    axisTitles.add(data4.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));

                    Data data5 = dataDao.getById(Long.valueOf(1307));

                    axisTitles.add(data5.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data5));

                    Data data6 = dataDao.getById(Long.valueOf(1308));

                    axisTitles.add(data6.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data6));

                    String title = "Heat On Time";
                    String xAxisLabel = "Grow Day[Day]";
                    String yAxisLabel = "Time[Minute]";
                    HistoryGraph heatontimeGraph = new HistoryGraph();
                    heatontimeGraph.setDataHistoryList(dataHistroryList);
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



