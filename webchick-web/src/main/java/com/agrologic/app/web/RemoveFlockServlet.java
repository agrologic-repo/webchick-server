
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Flock;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

//~--- JDK imports ------------------------------------------------------------

/**
 * Title: RemoveFlockServlet.java <br>
 * Decription: <br>
 * Copyright:   Copyright 2010 <br>
 * Company:     Agro Logic Ltd.<br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1.1 <br>
 */
public class RemoveFlockServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RemoveFlockServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        Long userId = Long.parseLong(request.getParameter("userId"));
        Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
        Long flockId = Long.parseLong(request.getParameter("flockId"));

        try {
            FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
            Flock flock = flockDao.getById(flockId);

            flockDao.remove(flockId);
            logger.info("Flock  " + flock + "successfully removed !");
            request.getSession().setAttribute("message", "Flock successfully  removed !");
            request.getSession().setAttribute("error", false);
            request.getRequestDispatcher("./flocks.html?userId=" + userId + "&cellinkId=" + cellinkId).forward(request,
                    response);
        } catch (SQLException ex) {

            // error page
            logger.error("Error occurs during removing flock !");
            request.getSession().setAttribute("message", "Error occurs during removing flock !");
            request.getSession().setAttribute("error", true);
            request.getRequestDispatcher("./flocks.html?userId=" + userId + "&cellinkId=" + cellinkId).forward(request,
                    response);
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



