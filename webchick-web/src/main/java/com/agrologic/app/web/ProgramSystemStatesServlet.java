package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.ProgramSystemState;
import com.agrologic.app.model.SystemState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ProgramSystemStatesServlet extends AbstractServlet {

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

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));

                try {
                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);

                    logger.info("get program !");

                    String[] empty = new String[0];
                    ProgramSystemStateDao programSystemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramSystemStateDao.class);
                    List<ProgramSystemState> programSystemStates =
                            programSystemStateDao.getAllProgramSystemStates(programId, empty);

                    program.setProgramSystemStates(programSystemStates);
                    logger.info("retrieve program system states!");
                    request.setAttribute("program", program);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    List<Data> dataSystemStates = (List<Data>) dataDao.getSystemStates();

                    logger.info("retrieve program data system states!");
                    request.setAttribute("dataSystemStates", dataSystemStates);

                    SystemStateDao systemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(SystemStateDao.class);
                    Collection<SystemState> systemStateNames = systemStateDao.getAll();

                    logger.info("retrieve syste state names!");
                    request.setAttribute("systemStateNames", systemStateNames);
                    request.getRequestDispatcher("./assign-systemstates.jsp").forward(request, response);

                    // request.getRequestDispatcher("./program-systemstates.jsp").forward(request, response);
                } catch (SQLException ex) {
                    logger.info("Error occurs while retrieve program system states!");
                    request.setAttribute("message", "Error occurs while retrieve program system states!");
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



