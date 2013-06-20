/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;

import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.utils.DateLocal;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * @author Administrator
 */
public class UncheckUnusedDataServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /**
         * Logger for this class and subclasses
         */
        final Logger logger = Logger.getLogger(SaveScreensServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                Long controllerId = Long.valueOf(103);
                controllerId = Long.parseLong(request.getParameter("controllerId"));

                try {
                    DataDao dataDao = new DataDaoImpl();
                    dataDao.uncheckNotUsedDataOnAllScreens(programId, controllerId);
                    ProgramDao programDao = new ProgramDaoImpl();
                    ProgramDto program = programDao.getById(programId);
                    program.setModifiedDate(DateLocal.currentDate());
                    programDao.update(program);
                    request.getSession().setAttribute("message", "Data successfully saved !");
                    request.getSession().setAttribute("error", Boolean.FALSE);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating screen!");
                    request.getSession().setAttribute("message", "Error occurs while updating screen !");
                    request.getSession().setAttribute("error", Boolean.TRUE);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request, response);
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
     * Handles the HTTP
     * <code>POST</code> method.
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
    }// </editor-fold>
}
