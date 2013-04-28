
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.dao.impl.SystemStateDaoImpl;
import com.agrologic.app.model.LanguageDto;
import com.agrologic.app.model.SystemStateDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class ListSystemStates extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(ProgramDetailsServet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                try {
                    long translateLang;

                    try {
                        translateLang = Long.parseLong(request.getParameter("translateLang"));
                    } catch (NumberFormatException ex) {
                        translateLang = 1;    // default program
                    }

                    LanguageDao      langDao   = new LanguageDaoImpl();
                    List<LanguageDto> languages = langDao.geAll();

                    logger.info("retreive system state names!");
                    request.getSession().setAttribute("languages", languages);

                    SystemStateDao      systemStateDao   = new SystemStateDaoImpl();
                    List<SystemStateDto> systemStateNames = systemStateDao.getAll(translateLang);

                    logger.info("retreive system state names!");
                    request.getSession().setAttribute("systemstateNames", systemStateNames);
                    response.sendRedirect("./all-systemstates.jsp?translateLang=" + translateLang);
                } catch (SQLException ex) {
                    logger.info("Error occurs while retreive program systemStates!", ex);
                    request.getSession().setAttribute("message", "Error occurs while retreive program systemStates!");
                    request.getRequestDispatcher("./all-programs.html").forward(request, response);
                }
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>
}



