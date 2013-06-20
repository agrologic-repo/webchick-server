
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.dao.impl.*;
import com.agrologic.app.model.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class ViewResultServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(ViewResultServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
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
                    ProgramDao programDao = new ProgramDaoImpl();
                    List<ProgramDto> programs = programDao.getAll();
                    ProgramDto program = programDao.getById(programId);
                    ScreenDao screenDao = new ScreenDaoImpl();
                    List<ScreenDto> screens = screenDao.getAllScreensByProgramAndLang(programId, translateLang, false);

                    program.setScreens(screens);

                    TableDao tableDao = new TableDaoImpl();
                    DataDao dataDao = new DataDaoImpl();

                    for (ScreenDto s : screens) {
                        if (s.getId().equals(screenId)) {
                            List<TableDto> tables = tableDao.getScreenTables(s.getProgramId(), s.getId(),
                                    translateLang, false);

                            for (TableDto table : tables) {
                                List<DataDto> dataList = dataDao.getTableDataList(s.getProgramId(), s.getId(),
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

                    List<DataDto> dataRelays = dataDao.getRelays();
                    LanguageDao languageDao = new LanguageDaoImpl();
                    List<LanguageDto> langList = languageDao.geAll();

                    logger.info("retrieve program data relay!");
                    request.getSession().setAttribute("dataRelays", dataRelays);
                    request.getSession().setAttribute("program", program);
                    request.getSession().setAttribute("programs", programs);
                    request.getSession().setAttribute("languages", langList);
                    request.getRequestDispatcher("./view-result.jsp?programId=" + programId + "&screenId=" + screenId
                            + "&screenLangId=" + translateLang).forward(request, response);
                } catch (SQLException ex) {

                    // error page
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



