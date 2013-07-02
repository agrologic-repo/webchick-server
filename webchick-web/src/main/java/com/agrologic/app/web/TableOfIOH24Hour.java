
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
import java.util.Iterator;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class TableOfIOH24Hour extends HttpServlet {

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
        final Logger logger = Logger.getLogger(TableOfFeedWater.class);

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
                    long langId = 1;
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data1 = dataDao.getById(Long.valueOf(1301), langId);
                    Map<Integer, Data> interestData1 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data1);
                    Data data2 = dataDao.getById(Long.valueOf(1302), langId);
                    Map<Integer, Data> interestData2 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data2);
                    Data data3 = dataDao.getById(Long.valueOf(2933), langId);
                    Map<Integer, Data> interestData3 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data3);
                    Data data4 = dataDao.getById(Long.valueOf(2934), langId);
                    Map<Integer, Data> interestData4 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data4);
                    Data data5 = dataDao.getById(Long.valueOf(2935), langId);
                    Map<Integer, Data> interestData5 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data5);
                    Data data6 = dataDao.getById(Long.valueOf(2936), langId);
                    Map<Integer, Data> interestData6 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data6);
                    Data data7 = dataDao.getById(Long.valueOf(3002), langId);
                    Map<Integer, Data> interestData7 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data7);
                    Data data8 = dataDao.getById(Long.valueOf(3003), langId);
                    Map<Integer, Data> interestData8 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data8);
                    Data data9 = dataDao.getById(Long.valueOf(3004), langId);
                    Map<Integer, Data> interestData9 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data9);
                    Data data10 = dataDao.getById(Long.valueOf(3005), langId);
                    Map<Integer, Data> interestData10 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data10);
                    Data data11 = dataDao.getById(Long.valueOf(3006), langId);
                    Map<Integer, Data> interestData11 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data11);
                    Data data12 = dataDao.getById(Long.valueOf(3007), langId);
                    Map<Integer, Data> interestData12 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data12);
                    Data data13 = dataDao.getById(Long.valueOf(1303), langId);
                    Map<Integer, Data> interestData13 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data13);
                    Data data14 = dataDao.getById(Long.valueOf(1304), langId);
                    Map<Integer, Data> interestData14 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data14);
                    Data data15 = dataDao.getById(Long.valueOf(1305), langId);
                    Map<Integer, Data> interestData15 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data15);
                    Data data16 = dataDao.getById(Long.valueOf(3017), langId);
                    Map<Integer, Data> interestData16 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data16);
                    Data data17 = dataDao.getById(Long.valueOf(3033), langId);
                    Map<Integer, Data> interestData17 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data17);
                    Data data18 = dataDao.getById(Long.valueOf(3034), langId);
                    Map<Integer, Data> interestData18 =
                            TableOfHistoryCreator.createHistDataByGrowDay(historyByGrowDay, data18);

                    out.println("<p>");
                    out.println(
                            "<table class=table-list cellpadding=1 cellspacing=1 border=1 style=behavior:url(tablehl.htc) url(sort.htc);>");
                    out.println("<tr>");
                    out.println("<th style=\"font-size: small\">Grow Day</th>");
                    out.println("<th style=\"font-size: small\">" + data1.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data2.getLabel() + "[Lt]</th>");
                    out.println("<th style=\"font-size: small\">" + data3.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data4.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data5.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data6.getLabel() + "[KG]</th>");
                    out.println("<th style=\"font-size: small\">" + data7.getLabel() + "[C]</th>");
                    out.println("<th style=\"font-size: small\">" + data8.getLabel() + "[C]</th>");
                    out.println("<th style=\"font-size: small\">" + data9.getLabel() + "[C]</th>");
                    out.println("<th style=\"font-size: small\">" + data10.getLabel() + "[C]</th>");
                    out.println("<th style=\"font-size: small\">" + data11.getLabel() + "[%]</th>");
                    out.println("<th style=\"font-size: small\">" + data12.getLabel() + "[%]</th>");
                    out.println("<th style=\"font-size: small\">" + data13.getLabel() + "[minutes]</th>");
                    out.println("<th style=\"font-size: small\">" + data14.getLabel() + "[minutes]</th>");
                    out.println("<th style=\"font-size: small\">" + data15.getLabel() + "[minutes]</th>");
                    out.println("<th style=\"font-size: small\">" + data16.getLabel() + "[Birds]</th>");
                    out.println("<th style=\"font-size: small\">" + data17.getLabel() + "[Birds]</th>");
                    out.println("<th style=\"font-size: small\">" + data18.getLabel() + "[Birds]</th>");
                    out.println("</tr>");

                    Iterator iter = historyByGrowDay.keySet().iterator();

                    while (iter.hasNext()) {
                        Integer growDay = (Integer) iter.next();

                        data1 = interestData1.get(growDay);
                        data2 = interestData2.get(growDay);
                        data3 = interestData3.get(growDay);
                        data4 = interestData4.get(growDay);
                        data5 = interestData5.get(growDay);
                        data6 = interestData6.get(growDay);
                        data7 = interestData7.get(growDay);
                        data8 = interestData8.get(growDay);
                        data9 = interestData9.get(growDay);
                        data10 = interestData10.get(growDay);
                        data10 = interestData11.get(growDay);
                        data12 = interestData12.get(growDay);
                        data13 = interestData13.get(growDay);
                        data14 = interestData14.get(growDay);
                        data15 = interestData15.get(growDay);
                        data16 = interestData16.get(growDay);
                        data17 = interestData17.get(growDay);
                        data18 = interestData18.get(growDay);
                        out.println("<tr>");
                        out.println("<td align=center>" + growDay + "</td>");
                        out.println("<td align=center>" + valueByType(data1) + "</td>");
                        out.println("<td align=center>" + valueByType(data2) + "</td>");
                        out.println("<td align=center>" + valueByType(data3) + "</td>");
                        out.println("<td align=center>" + valueByType(data4) + "</td>");
                        out.println("<td align=center>" + valueByType(data5) + "</td>");
                        out.println("<td align=center>" + valueByType(data6) + "</td>");
                        out.println("<td align=center>" + valueByType(data7) + "</td>");
                        out.println("<td align=center>" + valueByType(data8) + "</td>");
                        out.println("<td align=center>" + valueByType(data9) + "</td>");
                        out.println("<td align=center>" + valueByType(data10) + "</td>");
                        out.println("<td align=center>" + valueByType(data11) + "</td>");
                        out.println("<td align=center>" + valueByType(data12) + "</td>");
                        out.println("<td align=center>" + valueByType(data13) + "</td>");
                        out.println("<td align=center>" + valueByType(data14) + "</td>");
                        out.println("<td align=center>" + valueByType(data15) + "</td>");
                        out.println("<td align=center>" + valueByType(data16) + "</td>");
                        out.println("<td align=center>" + valueByType(data17) + "</td>");
                        out.println("<td align=center>" + valueByType(data18) + "</td>");
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
}



