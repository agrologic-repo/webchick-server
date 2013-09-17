
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.ProgramAlarm;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class ProgramAlarmsServlet extends HttpServlet {

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
                Long programId = Long.parseLong(request.getParameter("programId"));

                try {
                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);

                    logger.info("retrieve program!");

                    String[] empty = new String[0];
                    ProgramAlarmDao programAlarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramAlarmDao.class);
                    List<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(program.getId(), empty);

                    program.setProgramAlarms(programAlarms);
                    logger.info("retrieve program alarms!");
                    request.getSession().setAttribute("program", program);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    List<Data> dataAlarms = dataDao.getAlarms();

                    logger.info("retrieve program alarms datatypes!");
                    request.getSession().setAttribute("dataAlarms", dataAlarms);

                    AlarmDao alarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(AlarmDao.class);
                    List<Alarm> alarmNames = new ArrayList<Alarm>(alarmDao.getAll());

                    logger.info("retrieve relay names!");
                    request.getSession().setAttribute("alarmNames", alarmNames);
                    request.getRequestDispatcher("./assign-alarms.jsp").forward(request, response);

                    // request.getRequestDispatcher("./program-alarms.jsp").forward(request, response);
                } catch (SQLException ex) {
                    logger.info("Error occurs while retrieve program alarms!");
                    request.getSession().setAttribute("message", "Error occurs while retrieve program alarms!");
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



