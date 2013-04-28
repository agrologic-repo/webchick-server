
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.RelayDao;
import com.agrologic.app.dao.impl.RelayDaoImpl;
import com.agrologic.app.model.RelayDto;

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
public class RemoveRelayServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RemoveRelayServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long translateLang = Long.parseLong(request.getParameter("translateLang"));
                Long relayId       = Long.parseLong(request.getParameter("relayId"));

                try {
                    RelayDao relayDao = new RelayDaoImpl();
                    RelayDto  relay    = relayDao.getById(relayId);

                    relayDao.remove(relay.getId());
                    logger.info("Relay " + relay + " successfully removed!");
                    request.getSession().setAttribute("message",
                                                      "Relay <b style=\"color:gray\"> " + relay.getText()
                                                      + " </b> successfully  removed !");
                    request.getSession().setAttribute("error", false);
                    request.getRequestDispatcher("./all-relays.html?translateLang=" + translateLang).forward(request,
                                                 response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs during removing relay" + ex.getMessage());
                    request.getSession().setAttribute("message", "Error occurs during removing relay !");
                    request.getSession().setAttribute("error", true);
                    request.getRequestDispatcher("./all-relays.html").forward(request, response);
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



