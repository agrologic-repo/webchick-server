package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.CombinedXYGraph;
import com.agrologic.app.graph.daily.EmptyGraph;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class GraphMinMaxHumidityServlet extends AbstractServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * labels
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
                FromDayToDay growDayRangeParam
                        = new FromDayToDay(request.getParameter("fromDay"), request.getParameter("toDay"));
                try {

                    FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
                    Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_IN.getId());

                    Map<Integer, Data> interestData = createDataSet(historyByGrowDay, data);
                    data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_IN.getId());

                    Map<Integer, Data> interestData2 = createDataSet(historyByGrowDay, data);
                    data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_OUT.getId());

                    Map<Integer, Data> interestData3 = createDataSet(historyByGrowDay, data);
                    data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_OUT.getId());

                    Map<Integer, Data> interestData4 = createDataSet(historyByGrowDay, data);
                    CombinedXYGraph combGraph = new CombinedXYGraph();
                    combGraph.createFirstNextPlot("Maximum and Minimum Inside Temperature", "Grow Day[Day]",
                            "Temperature[C]", data, 0, interestData, interestData2);

                    combGraph.createNextPlot("Maximum and Minimum Outside Temperature", "Grow Day[Day]",
                            "Temperature[C]", data, 0, interestData3, interestData4);
                    data = dataDao.getById(PerGrowDayHistoryDataType.MAX_HUMIDITY.getId());

                    Map<Integer, Data> interestData5 = createDataSet(historyByGrowDay, data);
                    data = dataDao.getById(PerGrowDayHistoryDataType.MIN_HUMIDITY.getId());
                    Map<Integer, Data> interestData6 = createDataSet(historyByGrowDay, data);

                    combGraph.createNextPlot("Humidity", "Grow Day[Day]", "Humidity[%]", data, 1, interestData5,
                            interestData6);
                    combGraph.createChart("In\\Out Min\\Max Temperature And Humidity Graph", growDayRangeParam.toString());
                    request.setAttribute("fromDay", growDayRangeParam.getFromDay());
                    request.setAttribute("toDay", growDayRangeParam.getToDay());
                    ChartUtilities.writeChartAsPNG(out, combGraph.getChart(), 800, 800);
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
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
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

    /**
     * @param historyByGrowDay
     * @param data
     * @return
     */
    private Map<Integer, Data> createDataSet(final Map<Integer, String> historyByGrowDay, final Data data) {
        Map<Integer, Data> dataSet = new HashMap<Integer, Data>();
        Iterator iter = historyByGrowDay.keySet().iterator();

        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            String value = historyByGrowDay.get(key);
            StringTokenizer st = new StringTokenizer(value, " ");

            while (st.hasMoreElements()) {
                try {
                    String dataElem = (String) st.nextElement();
                    String valElem = (String) st.nextElement();
                    String dataType = data.getType().toString();

                    if (dataElem.equals(dataType) && (valElem.indexOf('-') == -1)) {
                        data.setValueFromUI(valElem);

                        Data cloned = (Data) data.clone();

                        dataSet.put(key, cloned);

                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        return dataSet;
    }
}



