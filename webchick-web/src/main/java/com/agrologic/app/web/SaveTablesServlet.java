
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.impl.TableDaoImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class SaveTablesServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(SaveTablesServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                Long screenId = Long.parseLong(request.getParameter("screenId"));
                String showTableMapStr = request.getParameter("showTableMap");
                String posTableMapStr = request.getParameter("posTableMap");
                String[] showTablePairs = showTableMapStr.split(";");
                Map<Long, String> showTableMap = new HashMap<Long, String>();

                for (String s : showTablePairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long tableId = Long.parseLong(st.nextToken());
                    String show = st.nextToken();

                    showTableMap.put(tableId, show);
                }

                String[] posTablePairs = posTableMapStr.split(";");
                Map<Long, Integer> posTableMap = new HashMap<Long, Integer>();

                for (String s : posTablePairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long tableId = Long.parseLong(st.nextToken());
                    Integer pos = Integer.parseInt(st.nextToken());
                    posTableMap.put(tableId, pos);
                }

                try {
                    TableDao tableDao = new TableDaoImpl();
                    tableDao.saveChanges(showTableMap, posTableMap, screenId, programId);
                    request.getSession().setAttribute("message", "Tables successfully saved !");
                    request.getSession().setAttribute("error", false);
                    request.getRequestDispatcher("./all-tables.html?screenId=" + screenId).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating program !");
                    request.getSession().setAttribute("message", "Error occurs while saving tables !");
                    request.getSession().setAttribute("error", true);
                    request.getRequestDispatcher("./all-tables.html?screenId=" + screenId).forward(request, response);
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



