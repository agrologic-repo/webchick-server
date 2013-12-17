package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.management.DataForTableCreator;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.service.FlockHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

public class ViewTableHistoryData extends AbstractServlet {
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

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {

                long flockId = Long.parseLong(request.getParameter("flockId"));
                GrowDayRangeParam growDayRangeParam
                        = new GrowDayRangeParam(request.getParameter("fromDay"), request.getParameter("toDay"));

                FlockHistoryService flockHistoryService = new FlockHistoryService();
                Map<Integer, String> historyByGrowDayMap
                        = flockHistoryService.getFlockHistoryWithinRange(flockId, growDayRangeParam);

                List<Map<Integer, Data>> historyDataList = createHistoryByGrowDay(historyByGrowDayMap);
                out.println("<p><table class=table-list cellpadding=1 cellspacing=1 border=1><tr>");
                out.println("<tr>");

                List<String> perHourHistoryDataTitles = flockHistoryService.getPerGrowDayHistoryDataTitles();
                for (String title : perHourHistoryDataTitles) {
                    out.println("<th style=\"font-size: small\">" + title + "</th>");
                }
                out.println("</tr>");

                Iterator<Integer> growDayIter = historyByGrowDayMap.keySet().iterator();
                while (growDayIter.hasNext()) {
                    Integer growDay = growDayIter.next();
                    Iterator<Map<Integer, Data>> historyIter = historyDataList.iterator();
                    out.println("<tr>");
                    while (historyIter.hasNext()) {
                        try {
                            Map<Integer, Data> interestData = historyIter.next();
                            Data data = interestData.get(growDay);
                            if (data == null) {
                                out.println("<td align=center>&nbsp;</td>");
                            } else if (data.getId() == 800) {
                                out.println("<td align=center>" + growDay + "</td>");
                            } else {
                                out.println("<td align=center>" + valueByType(data) + "</td>");
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    out.println("</tr>");
                }
                out.println("</table>");
            }
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        }
    }

    /**
     * Return formatted data value string .
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
     * Create list of management data by grow day .
     *
     * @param historyByGrowDay all management by grow day map.
     * @return historyDataForTable the list of management data by grow day.
     * @throws UnsupportedOperationException
     */
    private List<Map<Integer, Data>> createHistoryByGrowDay(Map<Integer, String> historyByGrowDay)
            throws UnsupportedOperationException {
        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        List<Map<Integer, Data>> historyDataForTable = new ArrayList<Map<Integer, Data>>();
        Map<Integer, Data> tempList = new HashMap<Integer, Data>();
        long langId = 1L;
        for (PerGrowDayHistoryDataType perGrowDayHistoryDataType : PerGrowDayHistoryDataType.values()) {
            try {
                Data data = dataDao.getById(perGrowDayHistoryDataType.getId(), langId);
                if (data.getId() == 800) {
                    historyDataForTable.add(DataForTableCreator.createGrowDayList(historyByGrowDay, data));
                } else {
                    tempList = DataForTableCreator.createHistDataByGrowDay(historyByGrowDay, data);
                }
                if (!tempList.isEmpty()) {
                    historyDataForTable.add(tempList);
                }
            } catch (SQLException e) {
                logger.error("Specified data {} was not found ", e);
            }
        }

        return historyDataForTable;
    }
}




