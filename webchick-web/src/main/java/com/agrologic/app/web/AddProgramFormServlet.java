package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Program;
import com.agrologic.app.util.DateLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProgramFormServlet extends AbstractServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");
        } else {
            String name = request.getParameter("Nname");
            Long programId = Long.parseLong(request.getParameter("Nprogramid"));
            Long selectedProgramId = Long.parseLong(request.getParameter("Selectedprogramid"));
            String relays = request.getParameter("relays");
            String alarms = request.getParameter("alarms");
            String systemstates = request.getParameter("systemstates");
            String specialdata = request.getParameter("specialdata");

            try {
                Program newProgram = new Program();
                newProgram.setId(programId);
                newProgram.setName(name);
                newProgram.setCreatedDate(DateLocal.currentDate());
                newProgram.setModifiedDate(DateLocal.currentDate());

                // Here we insert new table to database
                ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);

                programDao.insert(newProgram);
                logger.info("Program " + newProgram + "successfully added !");
                request.setAttribute("message", "program successfully added !");

                // Here we execute query for inserting screens of new program
                ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                screenDao.insertDefaultScreens(newProgram.getId(), selectedProgramId);
                logger.info("New screens successfully added !");

                // Get screens of inserted program. Screens of new programs have
                // same ID's , so we need same screens but new program id in screen .
                TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                tableDao.insertDefaultTables(newProgram.getId(), selectedProgramId);

                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                dataDao.insertDataList(newProgram.getId(), selectedProgramId);

                if ((relays != null) && "ON".equals(relays.toUpperCase())) {
                    RelayDao relayDao = DbImplDecider.use(DaoType.MYSQL).getDao(RelayDao.class);
                    relayDao.copyRelays(newProgram.getId(), selectedProgramId);
                }

                if ((alarms != null) && "ON".equals(alarms.toUpperCase())) {
                    AlarmDao alarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(AlarmDao.class);
                    alarmDao.copyAlarms(newProgram.getId(), selectedProgramId);
                }

                if ((systemstates != null) && "ON".equals(systemstates.toUpperCase())) {
                    SystemStateDao systemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(SystemStateDao.class);
                    systemStateDao.copySystemStates(newProgram.getId(), selectedProgramId);
                }

                if ((specialdata != null) && "ON".equals(specialdata.toUpperCase())) {
                    dataDao.copySpecialData(newProgram.getId(), selectedProgramId);
                }

                // ----------------------------------------------------------
                request.setAttribute("error", false);
                request.setAttribute("message", "Program successfully added !");
                request.getRequestDispatcher("./all-programs.html").forward(request, response);
            } catch (Exception ex) {

                // error page
                logger.error("Error occurs while adding program !");
                request.setAttribute("message", "Error occurs while adding program !");
                request.setAttribute("error", true);
                request.getRequestDispatcher("./all-programs.html").forward(request, response);
            }
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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



