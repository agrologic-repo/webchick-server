
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.dao.impl.SystemStateDaoImpl;
import com.agrologic.app.model.SystemStateDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class EditSystemStateFormServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        final Logger logger = Logger.getLogger(AddSystemStateFormServlet.class);
        PrintWriter  out    = response.getWriter();

        try {
            Long            systemstateId   = Long.parseLong(request.getParameter("NsystemstateId"));
            String          systemstateText = request.getParameter("Ntext");
            Long            translateLang   = Long.parseLong(request.getParameter("translateLang"));
            SystemStateDao systemstateDao  = new SystemStateDaoImpl();

            try {
                SystemStateDto systemstate = systemstateDao.getById(systemstateId);

                systemstate.setText(systemstateText);
                systemstateDao.update(systemstate);
                logger.info("System State " + systemstate.getText() + "  successfully updated");
                systemstateDao.insertTranslation(systemstate.getId(), translateLang, systemstate.getText());
                request.getSession().setAttribute("message",
                                                  "System State <b style=\"color:gray\"> " + systemstate.getText()
                                                  + " </b> successfully  updated");
                request.getSession().setAttribute("error", false);
            } catch (SQLException ex) {
                logger.error("Error occurs during editiing system state ", ex);
                request.getSession().setAttribute("message", "Error occurs during editing system state ");
                request.getSession().setAttribute("error", true);
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



