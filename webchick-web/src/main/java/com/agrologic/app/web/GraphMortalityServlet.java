package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.LanguageDao;
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
import java.util.*;

public class GraphMortalityServlet extends AbstractServlet {

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

                    List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
                    List<String> axisTitles = new ArrayList<String>();
                    Locale currLocale = (Locale) request.getSession().getAttribute("currLocale");
                    String lang = currLocale.toString().substring(0, 2);
                    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                    long langId = languageDao.getLanguageId(lang);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data1 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY.getId());
                    axisTitles.add(data1.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

                    Data data2 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_MALE.getId());
                    axisTitles.add(data2.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

                    Data data3 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_FEMALE.getId());
                    axisTitles.add(data3.getLabel());
                    dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

                    HashMap<String, String> dictinary = createDictionary(currLocale);
                    String title = dictinary.get("graph.mrt.title");    // "Daily Mortality";
                    String xAxisTitle = dictinary.get("graph.mrt.axis.growday");    // "Grow Day[Day]";
                    String yAxisTitle = dictinary.get("graph.mrt.axis.birds");    // "Birds";
                    HistoryGraph mortalityGraph = new HistoryGraph();

                    mortalityGraph.setDataHistoryList(dataHistoryList);
                    mortalityGraph.createChart(title, xAxisTitle, yAxisTitle);
                    request.setAttribute("fromDay", growDayRangeParam.getFromDay());
                    request.setAttribute("toDay", growDayRangeParam.getToDay());
                    ChartUtilities.writeChartAsPNG(out, mortalityGraph.getChart(), 800, 600);
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Unknown error.", ex);
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

    protected HashMap<String, String> createDictionary(Locale locale) {
        HashMap<String, String> dictinary = new HashMap<String, String>();
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);

        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = e.nextElement();

            if (key.startsWith("graph")) {
                String value = bundle.getString(key);

                dictinary.put(key, value);
            }
        }

        return dictinary;
    }

    private String getFilePath(Locale locale) {
        String fileName = "GraphData_" + locale.toString() + ".properties";
        String realPath = this.getServletContext().getRealPath(".");

        realPath = realPath.substring(0, realPath.length() - 1);
        realPath = realPath.concat("WEB-INF\\classes\\").concat(fileName);

        return realPath;
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



