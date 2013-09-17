
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Data;
import com.agrologic.app.table.TableOfHistoryCreator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

public class TableOfAvgWeight extends HttpServlet {
    final Logger logger = Logger.getLogger(TableOfAvgWeight.class);

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
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long userId = Long.parseLong(request.getParameter("userId"));
                long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
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
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data1 = dataDao.getById(Long.valueOf(2933), Long.valueOf(1));
                    Map<Integer, Data> interestData1 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data1);
                    Data data2 = dataDao.getById(Long.valueOf(2934), Long.valueOf(1));
                    Map<Integer, Data> interestData2 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data2);
                    Data data3 = dataDao.getById(Long.valueOf(2935), Long.valueOf(1));
                    Map<Integer, Data> interestData3 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data3);
                    Data data4 = dataDao.getById(Long.valueOf(2936), Long.valueOf(1));
                    Map<Integer, Data> interestData4 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data4);

                    out.println("<h2>Average Weight [KG] </h2>");
                    out.println(
                            "<table class=table-list cellpadding=1 cellspacing=1 border=1 style=behavior:url(tablehl.htc) url(sort.htc);>");
                    out.println("<tr>");
                    out.println("<th style=\"font-size: small\">Grow Day</th>");
                    out.println("<th style=\"font-size: small\">" + data1.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data2.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data3.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data4.getLabel() + "[KG]</th>");
                    out.println("</tr>");

                    Iterator iter = historyByGrowDay.keySet().iterator();

                    while (iter.hasNext()) {
                        Integer growDay = (Integer) iter.next();
                        Data d1 = interestData1.get(growDay);
                        Data d2 = interestData2.get(growDay);
                        Data d3 = interestData3.get(growDay);
                        Data d4 = interestData4.get(growDay);

                        if ((d1 == null) || (d2 == null) || (d3 == null) || (d4 == null)) {

                            // skip
                            continue;
                        } else {
                            out.println("<tr>");
                            out.println("<td align=center>" + growDay + "</td>");
                            out.println("<td align=center>" + d1.getFormattedValue() + "</td>");
                            out.println("<td align=center>" + d2.getFormattedValue() + "</td>");
                            out.println("<td align=center>" + d3.getFormattedValue() + "</td>");
                            out.println("<td align=center>" + d4.getFormattedValue() + "</td>");
                            out.println("</tr>");
                        }
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



