
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class DisconnectCellinks extends HttpServlet {

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
            CellinkDao cellinkDao = new CellinkDaoImpl();
            Long userId = Long.parseLong(request.getParameter("userId"));
            String cellinkIds = request.getParameter("cellinkIds");
            String[] cellinkIdsStrings = null;

            cellinkIdsStrings = cellinkIds.split("and");

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < cellinkIdsStrings.length; i++) {
                try {
                    long cellinkId = Long.parseLong(cellinkIdsStrings[i]);

                    cellinkDao.disconnectStarted(cellinkId);
                    cellinkDao.disconnect(cellinkId);
                    sb.append(cellinkId).append(",");
                } catch (SQLException ex) {
                    Logger.getLogger(DisconnectCellinks.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(DisconnectCellinks.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            String result = sb.toString();

            result = result.substring(0, result.length() - 1);
            request.getSession().setAttribute("message", "Cellink(s) with ID " + result + " successfully disconnected");
            request.getSession().setAttribute("error", false);
            request.getRequestDispatcher("./overview.html?userId=" + userId).forward(request, response);
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



