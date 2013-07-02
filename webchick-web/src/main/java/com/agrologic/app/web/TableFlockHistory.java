
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.table.TableOfHistoryCreator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class TableFlockHistory extends HttpServlet {
    private static final List<Long> historyDataIdList = new ArrayList<Long>();
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TableFlockHistory.class);

    static {
        historyDataIdList.add(Long.valueOf(800));
        historyDataIdList.add(Long.valueOf(1301));
        historyDataIdList.add(Long.valueOf(1302));
        historyDataIdList.add(Long.valueOf(2933));
        historyDataIdList.add(Long.valueOf(2934));
        historyDataIdList.add(Long.valueOf(2935));
        historyDataIdList.add(Long.valueOf(2936));
        historyDataIdList.add(Long.valueOf(1303));
        historyDataIdList.add(Long.valueOf(1304));
        historyDataIdList.add(Long.valueOf(1305));
        historyDataIdList.add(Long.valueOf(1306));
        historyDataIdList.add(Long.valueOf(1307));
        historyDataIdList.add(Long.valueOf(1308));
        historyDataIdList.add(Long.valueOf(3002));
        historyDataIdList.add(Long.valueOf(3003));
        historyDataIdList.add(Long.valueOf(3004));
        historyDataIdList.add(Long.valueOf(3005));
        historyDataIdList.add(Long.valueOf(3006));
        historyDataIdList.add(Long.valueOf(3007));
        historyDataIdList.add(Long.valueOf(3017));
        historyDataIdList.add(Long.valueOf(3033));
        historyDataIdList.add(Long.valueOf(3034));
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
        final Logger logger = Logger.getLogger(TableFlockHistory.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

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
                    FlockDao flockDao = new FlockDaoImpl();
                    Map<Integer, String> historyByGrowDay = flockDao.getAllHistoryByFlock(flockId, fromDay, toDay);
                    List<Map<Integer, Data>> historyDataList = new ArrayList<Map<Integer, Data>>();
                    List<String> columnTitles = new ArrayList<String>();

                    historyDataList = createHistoryByGrowDay(columnTitles, historyByGrowDay);
                    out.println("<p>");
                    out.println(
                            "<table class=table-list cellpadding=1 cellspacing=1 border=1 style=behavior:url(tablehl.htc) url(sort.htc);>");
                    out.println("<tr>");

                    for (String title : columnTitles) {
                        out.println("<th style=\"font-size: small\">" + title + "</th>");
                    }

                    out.println("</tr>");

                    Iterator<Integer> growdayIter = historyByGrowDay.keySet().iterator();

                    while (growdayIter.hasNext()) {
                        Integer growDay = growdayIter.next();
                        Iterator<Map<Integer, Data>> historyIter = historyDataList.iterator();

                        out.println("<tr>");

                        while (historyIter.hasNext()) {
                            try {
                                Map<Integer, Data> interestData = historyIter.next();
                                Data data = interestData.get(growDay);

                                if (data.getId() == 800) {
                                    out.println("<td align=center>" + growDay + "</td>");
                                } else {
                                    out.println("<td align=center>" + valueByType(data) + "</td>");
                                }
                            } catch (Exception e) {
                                out.println("<td align=center>&nbsp;</td>");
                                logger.error(e);
                            }
                        }

                        out.println("</tr>");
                    }

                    out.println("</table>");
                } catch (Exception ex) {
                    logger.error("Unknown error. ", ex);
                }
            }
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        }
    }

    /**
     * Return formated data value string .
     * If data type is Time than return time in minutes .
     *
     * @param data the data object
     * @return formated value string
     */
    private String valueByType(Data data) {
        Long value = data.getValue();

        if (DataFormat.TIME == data.getFormat()) {
            long h = value / 100;
            long m = value % 100;
            long t = h * 60 + m;

            return String.valueOf(t);
        }
        return data.getFormattedValue();
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
     * Create list of history data by grow day .
     *
     * @param columnTitles     the list with column titles
     * @param historyByGrowDay all history by grow day map.
     * @return historyDataForTable the list of history data by grow day.
     * @throws UnsupportedOperationException
     */
    private List<Map<Integer, Data>> createHistoryByGrowDay(List<String> columnTitles,
                                                            Map<Integer, String> historyByGrowDay)
            throws UnsupportedOperationException {
        List<Map<Integer, Data>> historyDataForTable = new ArrayList<Map<Integer, Data>>();
        Map<Integer, Data> tempList = new TreeMap<Integer, Data>();
        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

        for (Long dataId : historyDataIdList) {
            try {
                Data data = dataDao.getById(dataId, (long) 1);

                if (dataId == 800) {
                    columnTitles.add(data.getLabel());
                    tempList = TableOfHistoryCreator.createGrowDayList(historyByGrowDay, data);
                    historyDataForTable.add(tempList);
                } else {
                    tempList = TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data);

                    if (!tempList.isEmpty()) {
                        columnTitles.add(data.getLabel());
                        historyDataForTable.add(tempList);
                    }
                }
            } catch (SQLException e) {

            }
        }

        return historyDataForTable;
    }
}




