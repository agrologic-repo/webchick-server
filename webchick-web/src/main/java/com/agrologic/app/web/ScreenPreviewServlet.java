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

public class ScreenPreviewServlet extends AbstractServlet {

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
                long programId;

                try {
                    programId = Long.parseLong(request.getParameter("programId"));
                } catch (NumberFormatException ex) {
                    programId = 1;        // default program
                }

                long screenId;

                try {
                    screenId = Long.parseLong(request.getParameter("screenId"));
                } catch (NumberFormatException ex) {
                    screenId = 1;         // default program
                }

                long translateLang;

                try {
                    translateLang = Long.parseLong(request.getParameter("screenLangId"));
                } catch (NumberFormatException ex) {
                    translateLang = 1;    // default program
                }

                try {
                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    List<Program> programs = (List<Program>) programDao.getAll();
                    Program program = programDao.getById(programId);
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    List<Screen> screens = (List<Screen>) screenDao.getAllScreensByProgramAndLang(programId,
                            translateLang, false);

                    program.setScreens(screens);

                    TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

                    for (Screen s : screens) {
                        if (s.getId().equals(screenId)) {
                            Collection<Table> tables = tableDao.getScreenTables(s.getProgramId(), s.getId(),
                                    translateLang, false);

                            for (Table table : tables) {
                                List<Data> dataList = (List<Data>) dataDao.getTableDataList(s.getProgramId(), s.getId(),
                                        table.getId(), translateLang, "yes");
                                table.setDataList(dataList);
                            }

                            s.setTables(tables);

                            break;
                        }
                    }

                    final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);
                    List<ProgramRelay> programRelays = programRelayDao.getAllProgramRelays(program.getId());

                    program.setProgramRelays(programRelays);

                    List<Data> dataRelays = (List<Data>) dataDao.getRelays();
                    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                    Collection<Language> langList = languageDao.geAll();

                    logger.info("retrieve program data relay!");
                    request.setAttribute("dataRelays", dataRelays);
                    request.setAttribute("program", program);
                    request.setAttribute("languages", langList);
                    request.getRequestDispatcher("./screen-preview.jsp?programId=" + programId + "&screenId="
                            + screenId + "&screenLangId=" + translateLang).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.error("Database error", ex);
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



