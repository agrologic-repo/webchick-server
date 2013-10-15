package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.ProgramRelay;
import com.agrologic.app.model.Relay;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ProgramRelaysServlet extends AbstractServlet {


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

                    /**
                     */
                    long translateLang;

                    try {
                        translateLang = Long.parseLong(request.getParameter("translateLang"));
                    } catch (NumberFormatException ex) {
                        translateLang = 1;    // default program
                    }

                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);
                    logger.info("get program !");

                    String[] empty = new String[0];
                    ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);
                    List<ProgramRelay> programRelays = programRelayDao.getAllProgramRelays(program.getId(), empty);
                    program.setProgramRelays(programRelays);
                    logger.info("retrieve program relays!");
                    request.setAttribute("program", program);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    List<Data> dataRelays = (List<Data>) dataDao.getRelays();
                    logger.info("retrieve program data relay!");
                    request.setAttribute("dataRelays", dataRelays);

                    RelayDao relayDao = DbImplDecider.use(DaoType.MYSQL).getDao(RelayDao.class);
                    List<Relay> relayNames = (List<Relay>) relayDao.getAll(translateLang);
                    request.setAttribute("relayNames", relayNames);
                    request.getRequestDispatcher("./assign-relays.jsp?translateLang=" + translateLang).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.info("Error occurs while retrieve program relays!");
                    request.setAttribute("message", "Error occurs while retrieve program relays!");
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



