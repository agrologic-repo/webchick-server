
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramAlarmDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class AssignAlarmsFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AssignAlarmsFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                String dataAlarmMapParam = request.getParameter("datamap");
                SortedMap<Integer, String> digitsTextMap = new TreeMap<Integer, String>();
                SortedMap<Long, Map<Integer, String>> dataAlarmMap = new TreeMap<Long, Map<Integer, String>>();
                int digitCount = 0,
                        maxNumDigits = 10;
                long dataId = 0;
                StringTokenizer bitsTextToken = new StringTokenizer(dataAlarmMapParam, ";");

                while (bitsTextToken.hasMoreTokens()) {
                    StringTokenizer bitsToken = new StringTokenizer(bitsTextToken.nextToken(), ",");

                    if ((digitCount % maxNumDigits) == 0) {
                        String token = bitsToken.nextToken();

                        dataId = Long.parseLong(token);
                    }

                    int digit = Integer.parseInt(bitsToken.nextToken().trim());
                    String text = bitsToken.nextToken();
                    digitsTextMap.put(digit, text);
                    digitCount++;

                    if ((digitCount % maxNumDigits) == 0) {
                        dataAlarmMap.put(dataId, digitsTextMap);
                        digitsTextMap = new TreeMap<Integer, String>();
                    }
                }

                try {
                    ProgramAlarmDao programAlarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramAlarmDao.class);
                    programAlarmDao.assignAlarmsToGivenProgram(programId, dataAlarmMap);
                    logger.info("Alarms successfuly added!");
                    request.getRequestDispatcher("./program-alarms.html?programId=" + programId).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.info("Error occurs while retrieve adding alarms!");
                    request.getRequestDispatcher("./program-alarms.html").forward(request, response);
                }
            }
        } finally {
            out.close();
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



