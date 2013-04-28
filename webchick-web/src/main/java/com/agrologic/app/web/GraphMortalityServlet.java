
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.model.DataDto;
import com.agrologic.app.graph.DataGraphCreator;
import com.agrologic.app.graph.daily.Graph24Empty;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.graph.history.HistoryGraph;

import org.apache.log4j.Logger;

import org.jfree.chart.ChartUtilities;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GraphMortalityServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(GraphMortalityServlet.class);

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
                    FlockDao                   flockDao         = new FlockDaoImpl();
                    Map<Integer, String>        historyByGrowDay = flockDao.getAllHistoryByFlock(flockId, fromDay, toDay);
                    List<Map<Integer, DataDto>> dataHistroryList = new ArrayList<Map<Integer, DataDto>>();
                    List<String>                axisTitles       = new ArrayList<String>();
                    Locale                      currLocale       = (Locale) request.getSession().getAttribute("currLocale");
                    String                      lang             = currLocale.toString().substring(0, 2);
                    LanguageDao                languageDao      = new LanguageDaoImpl();
                    long                        langId           = languageDao.getLanguageId(lang);
                    DataDao                    dataDao          = new DataDaoImpl();
                    DataDto                     data1            = dataDao.getById(Long.valueOf(3017));

//                  String realPath = getFilePath(currLocale);
//                  String toJavaString = Unicode2ASCII.fromHTMLToJava(data1.getUnicodeLabel());
//                  PropertyFileUtil.setProperty(realPath, "", "label", toJavaString);
//                  ResourceBundle bundle = ResourceBundle.getBundle("GraphData",currLocale);
//                  data1.setUnicodeLabel(bundle.getString("label"));
                    axisTitles.add(data1.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

                    DataDto data2 = dataDao.getById(Long.valueOf(3033));

//                  toJavaString = Unicode2ASCII.fromHTMLToJava(data2.getUnicodeLabel());
//                  PropertyFileUtil.setProperty(realPath, "", "label", toJavaString);
//                  bundle = ResourceBundle.getBundle("GraphData", currLocale);
//                  data2.setUnicodeLabel(bundle.getString("label"));
                    axisTitles.add(data2.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

                    DataDto data3 = dataDao.getById(Long.valueOf(3034));

                    axisTitles.add(data3.getLabel());
                    dataHistroryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

                    HashMap<String, String> dictinary      = createDictionary(currLocale);
                    String                  title          = dictinary.get("graph.mrt.title");    // "Daily Mortality";
                    String                  xAxisTitle     = dictinary.get("graph.mrt.axis.growday");    // "Grow Day[Day]";
                    String                  yAxisTitle     = dictinary.get("graph.mrt.axis.birds");    // "Birds";
                    HistoryGraph            mortalityGraph = new HistoryGraph();

                    mortalityGraph.setDataHistoryList(dataHistroryList);
                    mortalityGraph.createChart(title, xAxisTitle, yAxisTitle);
                    request.getSession().setAttribute("fromDay", fromDay);
                    request.getSession().setAttribute("toDay", toDay);
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
        ResourceBundle          bundle    = ResourceBundle.getBundle("MessagesBundle", locale);

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
}



