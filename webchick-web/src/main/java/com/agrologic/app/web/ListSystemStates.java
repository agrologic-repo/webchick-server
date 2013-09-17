package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.model.Language;
import com.agrologic.app.model.SystemState;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;

public class ListSystemStates extends HttpServlet {

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
        final Logger logger = Logger.getLogger(ProgramDetailsServet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                try {
                    long translateLang;

                    try {
                        translateLang = Long.parseLong(request.getParameter("translateLang"));
                    } catch (NumberFormatException ex) {
                        translateLang = 1;    // default program
                    }

                    LanguageDao langDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                    Collection<Language> languages = langDao.geAll();

                    logger.info("retrieve system state names!");
                    request.getSession().setAttribute("languages", languages);

                    SystemStateDao systemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(SystemStateDao.class);
                    Collection<SystemState> systemStateNames = systemStateDao.getAll(translateLang);

                    logger.info("retrieve system state names!");
                    request.getSession().setAttribute("systemstateNames", systemStateNames);
                    response.sendRedirect("./all-systemstates.jsp?translateLang=" + translateLang);
                } catch (SQLException ex) {
                    logger.info("Error occurs while retrieve program systemStates!", ex);
                    request.getSession().setAttribute("message", "Error occurs while retrieve program systemStates!");
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



