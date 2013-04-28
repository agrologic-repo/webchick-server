
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.dao.impl.AlarmDaoImpl;
import com.agrologic.app.model.AlarmDto;

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
public class RemoveAlarmServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RemoveAlarmServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long translateLang = Long.parseLong(request.getParameter("translateLang"));
                Long alarmId       = Long.parseLong(request.getParameter("alarmId"));

                try {
                    AlarmDao alarmDao = new AlarmDaoImpl();
                    AlarmDto  alarm    = alarmDao.getById(alarmId);

                    alarmDao.remove(alarm.getId());
                    logger.info("Alarm " + alarm + " successfully removed!");
                    request.getSession().setAttribute("message",
                                                      "Alarm <b style=\"color:gray\"> " + alarm.getText()
                                                      + " </b> successfully  removed !");
                    request.getSession().setAttribute("error", false);
                    request.getRequestDispatcher("./all-alarms.html?translateLang=" + translateLang).forward(request,
                                                 response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs during removing alarm" + ex.getMessage());
                    request.getSession().setAttribute("message", "Error occurs during removing alarm !");
                    request.getSession().setAttribute("error", true);
                    request.getRequestDispatcher("./all-alarms.html").forward(request, response);
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



