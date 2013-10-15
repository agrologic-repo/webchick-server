package com.agrologic.app.web;

import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.service.FlockHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class RepresentInTableTheHistory24Data extends AbstractServlet {

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

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");
        } else {

            try {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                GrowDayParam growDayParam = new GrowDayParam(request.getParameter("growDay"));
                FlockHistoryService flockHistoryService = new FlockHistoryService();
                List<String> historyValueList =
                        flockHistoryService.getFlockHistory24Hour(flockId, growDayParam.getGrowDay());

                StringBuilder chainedHistoryValues = new StringBuilder();
                for (String historyValues : historyValueList) {
                    chainedHistoryValues.append(historyValues);
                }
                Long resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
                Map<Integer, String> history24ByHour = parseHistory24(resetTime, historyValueList.get(0));
                List<List<String>> history24Data = new ArrayList<List<String>>();
                history24Data.add((DataForExcelCreator.createDataList(history24ByHour.keySet())));
                history24ByHour = parseHistory24(resetTime, historyValueList.get(0));
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_1));
                history24ByHour = parseHistory24(resetTime, historyValueList.get(1));
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_1));
                history24ByHour = parseHistory24(resetTime, historyValueList.get(2));
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.HUMIDITY));
                history24ByHour = parseHistory24(resetTime, historyValueList.get(3));
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_4));
                history24ByHour = parseHistory24(resetTime, historyValueList.get(4));
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_4));

                List<String> perHourHistoryDataTitles =
                        flockHistoryService.getPerHourHistoryDataTitles(growDayParam.getGrowDay());

                out.println("<p>");
                out.println("<table class=table-list cellpadding=1 cellspacing=1 border=1>");
                out.println("<tr>");

                for (String title : perHourHistoryDataTitles) {
                    out.println("<th style=\"font-size: small\">" + title + "</th>");
                }

                out.println("</tr>");

                Iterator hourIter = history24ByHour.keySet().iterator();

                while (hourIter.hasNext()) {
                    Integer hour = (Integer) hourIter.next();
                    Iterator<List<String>> historyIter = history24Data.iterator();
                    out.println("<tr>");
                    while (historyIter.hasNext()) {
                        try {
                            List<String> interestData = historyIter.next();
                            String data = interestData.get(hour);
                            out.println("<td align=center>" + data + "</td>");
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }

                    out.println("</tr>");
                }

                out.println("</table>");
            } catch (Exception e) {
                logger.error("Unknown error. ", e);
            }
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

    private Map<Integer, String> parseHistory24(long resetTime, String values) {
        String[] valueList = values.split(" ");
        Map<Integer, String> valuesMap = new TreeMap<Integer, String>();
        int j = (int) resetTime / 100;

        for (int i = 0; i < 24; i++) {
            if (j == 24) {
                j = 0;
            }

            valuesMap.put(j++, valueList[i]);
        }

        return valuesMap;
    }
}



