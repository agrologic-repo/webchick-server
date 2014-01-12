/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.management.TableContentCreator;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.history.FromDayToDayParam;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

public class ViewTableEggCountHistory extends AbstractServlet {

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

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                FromDayToDayParam growDayRangeParam
                        = new FromDayToDayParam(request.getParameter("fromDay"), request.getParameter("toDay"));
                try {
                    FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
                    Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockHistoryWithinRange(flockId, growDayRangeParam);

                    List<String> columnTitles = new ArrayList<String>();
                    List<Map<Integer, Data>> historyDataList = createHistoryByGrowDay(columnTitles, historyByGrowDay);
                    out.println("<p>");
                    out.println("<table class=table-list cellpadding=1 cellspacing=1 border=1>");
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
                                logger.error(e.getMessage(), e);
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
     * Create list of management data by grow day .
     *
     * @param columnTitles     the list with column titles
     * @param historyByGrowDay all management by grow day map.
     * @return historyDataForTable the list of management data by grow day.
     * @throws UnsupportedOperationException
     */
    private static List<Map<Integer, Data>> createHistoryByGrowDay(List<String> columnTitles,
                                                                   Map<Integer, String> historyByGrowDay)
            throws UnsupportedOperationException {
        List<Map<Integer, Data>> historyDataForTable = new ArrayList<Map<Integer, Data>>();
        Map<Integer, Data> tempList = new TreeMap<Integer, Data>();
        List<Long> choosedList = new ArrayList<Long>();
        choosedList.add((long) 1465);
        choosedList.add((long) 1467);
        choosedList.add((long) 1469);
        choosedList.add((long) 1471);
        choosedList.add((long) 1473);
        choosedList.add((long) 1475);
        choosedList.add((long) 2600);
        choosedList.add((long) 2615);
        choosedList.add((long) 2630);
        choosedList.add((long) 2645);
        choosedList.add((long) 2660);
        choosedList.add((long) 2675);

        try {
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data = dataDao.getById(Long.valueOf(800), (long) 1);
            columnTitles.add(data.getLabel());
            tempList = TableContentCreator.createGrowDayList(historyByGrowDay, data);
            historyDataForTable.add(tempList);
            for (Long dataId : choosedList) {
                data = dataDao.getById(dataId, (long) 1);
                tempList = TableContentCreator.createEggCountHistDataByGrowDay(historyByGrowDay, data);
                if (!tempList.isEmpty()) {
                    columnTitles.add(data.getLabel());
                    historyDataForTable.add(tempList);
                }
            }
        } catch (SQLException e) {

        }
        return historyDataForTable;
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
    }// </editor-fold>
}

