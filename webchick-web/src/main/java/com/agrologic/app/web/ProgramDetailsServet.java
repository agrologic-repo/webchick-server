package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ProgramDetailsServet extends AbstractServlet {

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
        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");
        }

        Long programId = Long.parseLong(request.getParameter("programId"));

        try {
            ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
            Program program = programDao.getById(programId);

            logger.info("retrieve program details!");
            request.setAttribute("program", program);

            final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);
            List<ProgramRelay> programRelays = programRelayDao.getAllProgramRelays(program.getId());

            request.setAttribute("programRelays", programRelays);

            ProgramAlarmDao programAlarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramAlarmDao.class);
            List<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(program.getId());

            request.setAttribute("programAlarms", programAlarms);

            ProgramSystemStateDao programSystemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramSystemStateDao.class);
            List<ProgramSystemState> programSystemStates =
                    programSystemStateDao.getAllProgramSystemStates(programId);

            request.setAttribute("programSystemStates", programSystemStates);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Collection<Data> relayDataTypes = dataDao.getProgramDataRelays(program.getId());

            request.setAttribute("relayDataTypes", relayDataTypes);

            Collection<Data> alarmDataTypes = dataDao.getProgramDataAlarms(program.getId());

            request.setAttribute("alarmDataTypes", alarmDataTypes);

            Collection<Data> sysStateDataTypes = dataDao.getProgramDataSystemStates(program.getId());

            request.setAttribute("sysStateDataTypes", sysStateDataTypes);
            request.getRequestDispatcher("./program-details.jsp").forward(request, response);
        } catch (SQLException ex) {
            logger.info("Error occurs while retrieve controller details!");
            request.getRequestDispatcher("./all-programs.html").forward(request, response);
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



