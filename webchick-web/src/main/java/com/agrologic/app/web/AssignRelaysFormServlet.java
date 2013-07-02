
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramRelayDao;
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
 * @author JanL
 */
public class AssignRelaysFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AssignRelaysFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                String dataRelayMapParam = request.getParameter("datamap");
                SortedMap<Integer, String> bitsTextMap = new TreeMap<Integer, String>();
                SortedMap<Long, Map<Integer, String>> dataRelayMap = new TreeMap<Long, Map<Integer, String>>();
                int bitCount = 0,
                        maxNumBits = 16;
                long dataId = 0;
                StringTokenizer bitsTextToken = new StringTokenizer(dataRelayMapParam, ";");

                while (bitsTextToken.hasMoreTokens()) {
                    StringTokenizer bitsToken = new StringTokenizer(bitsTextToken.nextToken(), ",");

                    if ((bitCount % maxNumBits) == 0) {
                        String token = bitsToken.nextToken();

                        dataId = Long.parseLong(token);
                    }

                    int bit = Integer.parseInt(bitsToken.nextToken().trim());
                    String text = bitsToken.nextToken();

                    bitsTextMap.put(bit, text);
                    bitCount++;

                    if ((bitCount % maxNumBits) == 0) {
                        dataRelayMap.put(dataId, bitsTextMap);
                        bitsTextMap = new TreeMap<Integer, String>();
                    }
                }

                try {
                    final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);
                    programRelayDao.assignRelaysToGivenProgram(programId, dataRelayMap);
                    logger.info("Relays successfuly added!");
                    request.getRequestDispatcher("./program-relays.html?programId=" + programId).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.info("Error occurs while adding relays!");
                    request.getRequestDispatcher("./program-relays.html").forward(request, response);
                }

                System.out.println();
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



