package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.ActionSetDaoImpl;
import com.agrologic.app.model.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ListScreenTables extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**
         * Logger for this class and subclasses
         */
        final Logger logger = Logger.getLogger(ListScreenTables.class);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                long programId = Long.parseLong(request.getParameter("programId"));
                long screenId = Long.parseLong(request.getParameter("screenId"));
                long translateLang;

                try {
                    translateLang = Long.parseLong(request.getParameter("translateLang"));
                } catch (NumberFormatException ex) {
                    translateLang = 1;    // default program
                }

                try {
                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);

                    List<Screen> screens = (List<Screen>) screenDao.getAllByProgramId(programId);
                    program.setScreens(screens);

                    Screen screen = screenDao.getById(programId, screenId);
                    if (screen.getTitle().equals("Action Set Buttons")) {
                        ActionSetDao actionsetDao = DbImplDecider.use(DaoType.MYSQL).getDao(ActionSetDaoImpl.class);
                        Collection<ActionSet> actionset = actionsetDao.getAll(programId, translateLang);

                        request.getSession().setAttribute("actionset", actionset);

                        LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                        Collection<Language> langList = languageDao.geAll();

                        request.getSession().setAttribute("languages", langList);
                        request.getSession().setAttribute("program", program);
                        request.getSession().setAttribute("screen", screen);
                        request.getRequestDispatcher("./all-actionset.jsp?screenId=" + screen.getId()
                                + "&translateLang=" + translateLang).forward(request, response);
                    } else {
                        TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                        Collection<Table> tables = tableDao.getScreenTables(screen.getProgramId(), screen.getId(),
                                translateLang, true);
                        screen.setTables(tables);

                        LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                        Collection<Language> langList = languageDao.geAll();

                        request.getSession().setAttribute("program", program);
                        request.getSession().setAttribute("screen", screen);
                        request.getSession().setAttribute("languages", langList);
                        request.getRequestDispatcher("./all-tables.jsp?translateLang="
                                + translateLang).forward(request, response);
                    }
                } catch (SQLException ex) {
                    logger.error("database error ! " + ex.getMessage());
                    request.setAttribute("errormessage", ex.getMessage());
                    request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
                }
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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



