
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.model.DataFormat;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TableFlockHistory24 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String outfile;

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
        final Logger logger = Logger.getLogger(TableFlockHistory24.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            Locale locale = Locale.ENGLISH;

            try {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                String test = request.getParameter("flockId");
                System.out.println(test);

                int fromDay = -1;
                int toDay = -1;
                StringBuilder range = new StringBuilder();

                try {
                    fromDay = Integer.parseInt(request.getParameter("fromDay"));
                    toDay = Integer.parseInt(request.getParameter("toDay"));

                    if ((fromDay != -1) && (toDay != -1)) {
                        range.append("( From ").append(fromDay).append(" to ").append(toDay).append(" grow day )");
                    }
                } catch (Exception ex) {
                    fromDay = -1;
                    toDay = -1;
                }

                int growDay = 1;

                try {
                    growDay = Integer.parseInt(request.getParameter("growDay"));
                } catch (Exception ex) {
                    growDay = 1;
                }

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Long resetTime = new Long(flockDao.getResetTime(flockId, growDay));

                if (resetTime != null) {
                    resetTime = DataFormat.convertToTimeFormat(resetTime);

                    if (resetTime == (long) -1) {
                        resetTime = (long) 0;
                    }
                } else {
                    resetTime = Long.valueOf("0");
                }

                String values1 = flockDao.getHistory24(flockId, growDay, "D18");
                String values2 = flockDao.getHistory24(flockId, growDay, "D19");
                String values3 = flockDao.getHistory24(flockId, growDay, "D20");
                String values4 = flockDao.getHistory24(flockId, growDay, "D21");
                String values5 = flockDao.getHistory24(flockId, growDay, "D72");

                values1 = getDefaultValuesIfEmpty(values1);
                values2 = getDefaultValuesIfEmpty(values2);
                values3 = getDefaultValuesIfEmpty(values3);
                values4 = getDefaultValuesIfEmpty(values4);
                values5 = getDefaultValuesIfEmpty(values5);

                String title1 = flockDao.getDNHistory24("D18");
                String title2 = flockDao.getDNHistory24("D19");
                String title3 = flockDao.getDNHistory24("D20");
                String title4 = flockDao.getDNHistory24("D21");
                String title5 = flockDao.getDNHistory24("D72");
                Map<Integer, String> history24ByHour = parseHistory24(resetTime, values1);
                List<List<String>> history24Data = new ArrayList<List<String>>();

                history24Data.add((DataForExcelCreator.createDataList(history24ByHour.keySet())));
                history24ByHour = parseHistory24(resetTime, values1);
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_1));
                history24ByHour = parseHistory24(resetTime, values2);
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_1));
                history24ByHour = parseHistory24(resetTime, values3);
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.HUMIDITY));
                history24ByHour = parseHistory24(resetTime, values4);
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_4));
                history24ByHour = parseHistory24(resetTime, values5);
                history24Data.add(DataForExcelCreator.createDataHistory24List(history24ByHour, DataFormat.DEC_4));

                List<String> columnTitles = new ArrayList<String>();

                columnTitles.add("Grow day " + growDay + "\n Hour(24)");
                columnTitles.add(title1);
                columnTitles.add(title2);
                columnTitles.add(title3);
                columnTitles.add(title4);
                columnTitles.add(title5);
                out.println("<p>");
                out.println(
                        "<table class=table-list cellpadding=1 cellspacing=1 border=1 style=behavior:url(tablehl.htc) url(sort.htc);>");
                out.println("<tr>");

                for (String title : columnTitles) {
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
                            logger.error(e);
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

    public String getDefaultValuesIfEmpty(String values) {
        if (values.equals("-1 ") || values.equals("")) {
            values = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
        }

        return values;
    }

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



