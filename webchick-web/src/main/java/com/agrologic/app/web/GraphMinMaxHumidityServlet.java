
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.model.DataDto;
import com.agrologic.app.graph.CombinedXYGraph;
import com.agrologic.app.graph.daily.Graph24Empty;
import com.agrologic.app.graph.daily.GraphType;

import org.apache.log4j.Logger;

import org.jfree.chart.ChartUtilities;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GraphMinMaxHumidityServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(GraphMinMaxHumidityServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        OutputStream out = response.getOutputStream();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                long          flockId = Long.parseLong(request.getParameter("flockId"));
                int           fromDay = -1;
                int           toDay   = -1;
                StringBuilder range   = new StringBuilder();

                try {
                    fromDay = Integer.parseInt(request.getParameter("fromDay"));
                    toDay   = Integer.parseInt(request.getParameter("toDay"));

                    if ((fromDay != -1) && (toDay != -1)) {
                        range.append("( From ").append(fromDay).append(" to ").append(toDay).append(" grow day .)");
                    }
                } catch (Exception ex) {
                    fromDay = -1;
                    toDay   = -1;
                }

                try {
                    DataDao              dataDao          = new DataDaoImpl();
                    DataDto               data             = dataDao.getById(Long.valueOf(3002));
                    FlockDao             flockDao         = new FlockDaoImpl();
                    Map<Integer, String>  historyByGrowDay = flockDao.getAllHistoryByFlock(flockId, fromDay, toDay);
                    Map<Integer, DataDto> interestData     = createDataSet(historyByGrowDay, data);

                    data = dataDao.getById(Long.valueOf(3003));

                    Map<Integer, DataDto> interestData2 = createDataSet(historyByGrowDay, data);

                    data = dataDao.getById(Long.valueOf(3004));

                    Map<Integer, DataDto> interestData3 = createDataSet(historyByGrowDay, data);

                    data = dataDao.getById(Long.valueOf(3005));

                    Map<Integer, DataDto> interestData4 = createDataSet(historyByGrowDay, data);
                    CombinedXYGraph       combGraph     = new CombinedXYGraph();

                    combGraph.createFirstNextPlot("Maximum and Minimum Inside Temperature", "Grow Day[Day]",
                                                  "Temperature[C�]", data, 0, interestData,
                                                  interestData2 /* , interestData3,interestData4 */);
                    combGraph.createNextPlot("Maximum and Minimum Outside Temperature", "Grow Day[Day]",
                                             "Temperature[C�]", data, 0, interestData3, interestData4);
                    data = dataDao.getById(Long.valueOf(3006));

                    Map<Integer, DataDto> interestData5 = createDataSet(historyByGrowDay, data);

                    data = dataDao.getById(Long.valueOf(3007));

                    Map<Integer, DataDto> interestData6 = createDataSet(historyByGrowDay, data);

                    combGraph.createNextPlot("Humidity", "Grow Day[Day]", "Humidity[%]", data, 1, interestData5,
                                             interestData6);
                    combGraph.createChart("In\\Out Min\\Max Temperature And Humidity Graph", range.toString());
                    request.getSession().setAttribute("fromDay", fromDay);
                    request.getSession().setAttribute("toDay", toDay);
                    ChartUtilities.writeChartAsPNG(out, combGraph.getChart(), 800, 800);
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
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>

    /**
     *
     * @param historyByGrowDay
     * @param data
     * @return
     */
    private Map<Integer, DataDto> createDataSet(final Map<Integer, String> historyByGrowDay, final DataDto data) {
        Map<Integer, DataDto> dataSet = new HashMap<Integer, DataDto>();
        Iterator              iter    = historyByGrowDay.keySet().iterator();

        while (iter.hasNext()) {
            Integer         key   = (Integer) iter.next();
            String          value = historyByGrowDay.get(key);
            StringTokenizer st    = new StringTokenizer(value, " ");

            while (st.hasMoreElements()) {
                try {
                    String dataElem = (String) st.nextElement();
                    String valElem  = (String) st.nextElement();
                    String dataType = data.getType().toString();

                    if (dataElem.equals(dataType) && (valElem.indexOf('-') == -1)) {
                        data.setValueToChange(valElem);

                        DataDto cloned = (DataDto) data.clone();

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



