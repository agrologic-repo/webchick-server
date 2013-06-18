
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class EditAlarmFormServlet extends HttpServlet {

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

        final Logger logger = LoggerFactory.getLogger(AddAlarmFormServlet.class);
        PrintWriter out = response.getWriter();

        try {
            Long alarmId = Long.parseLong(request.getParameter("NalarmId"));
            String alarmText = request.getParameter("Ntext");
            Long translateLang = Long.parseLong(request.getParameter("translateLang"));
            AlarmDao alarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(AlarmDao.class);

            try {
                Alarm alarm = alarmDao.getById(alarmId);

                alarm.setText(alarmText);
                alarmDao.update(alarm);
                logger.info("Alarm [{}] successfully updated", alarm.getText());
                alarmDao.insertTranslation(alarm.getId(), translateLang, alarm.getText());
                request.getSession().setAttribute("message",
                        "Alarm <b style=\"color:gray\"> " + alarm.getText()
                                + " </b> successfully  updated");
                request.getSession().setAttribute("error", false);
            } catch (SQLException ex) {
                logger.error("Error occurs during editing alarm ", ex);
                request.getSession().setAttribute("message", "Error occurs during editing alarm ");
                request.getSession().setAttribute("error", true);
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



