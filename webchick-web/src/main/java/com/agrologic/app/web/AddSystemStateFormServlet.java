
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
public class AddSystemStateFormServlet extends HttpServlet {

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
            Long           systemStateId   = Long.parseLong(request.getParameter("NsystemstateId"));
            String         systemStateText = request.getParameter("NsystemstateText");
            Long           translateLang   = Long.parseLong(request.getParameter("translateLang"));
            SystemStateDto systemState     = new SystemStateDto();

            systemState.setId(systemStateId);
            systemState.setText(systemStateText);

            SystemStateDao systemStateDao = new SystemStateDaoImpl();

            try {
                systemStateDao.insert(systemState);
                logger.info("System State " + systemState.getText() + "  successfully added");
                systemStateDao.insertTranslation(systemState.getId(), translateLang, systemState.getText());
                request.getSession().setAttribute("message",
                                                  "SystemState <b style=\"color:gray\"> " + systemState.getText()
                                                  + " </b> successfully  added");
                request.getSession().setAttribute("error", false);
            } catch (SQLException ex) {
                logger.error("Error occurs during adding systemState " + systemState.getText(), ex);
                request.getSession().setAttribute("message",
                                                  "Error occurs during adding systemState <b style=\"color:gray\">  "
                                                  + systemState.getText() + " </b> ");
                request.getSession().setAttribute("error", true);
            } catch (Exception ex) {
                logger.error("Error occurs during adding system state ", ex);
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



