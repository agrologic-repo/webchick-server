
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.dao.impl.ScreenDaoImpl;
import com.agrologic.app.model.LanguageDto;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.model.ScreenDto;

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
 * @author JanL
 */
public class ListProgramScreens extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**
         * Logger for this class and subclasses
         */
        final Logger logger = Logger.getLogger(ListProgramScreens.class);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                String programIdParam = request.getParameter("programId");
                long programId = 1;
                if (programIdParam != null) {
                    programId = Long.parseLong(programIdParam);
                }

                long translateLang;
                try {
                    translateLang = Long.parseLong(request.getParameter("translateLang"));
                } catch (NumberFormatException ex) {
                    translateLang = 1;    // default program
                }

                try {
                    ProgramDao programDao = new ProgramDaoImpl();
                    ProgramDto program = programDao.getById(programId);
                    ScreenDao screenDao = new ScreenDaoImpl();
                    List<ScreenDto> screens = screenDao.getAllScreensByProgramAndLang(program.getId(), translateLang,
                            true);

                    program.setScreens(screens);

                    LanguageDao languageDao = new LanguageDaoImpl();
                    List<LanguageDto> langList = languageDao.geAll();

                    request.getSession().setAttribute("program", program);
                    request.getSession().setAttribute("languages", langList);
                    request.getRequestDispatcher("./all-screens.jsp?translateLang=" + translateLang).forward(request,
                            response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("database error ! " + ex.getMessage());
                    request.setAttribute("errormessage", ex.getMessage());
                    request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
                }
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>
}



