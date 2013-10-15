package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.model.Language;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.Screen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ListProgramScreens extends AbstractServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                String programIdParam = request.getParameter("programId");
                long programId = 1;
                if (programIdParam != null) {
                    programId = Long.parseLong(programIdParam);
                }

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
                    List<Screen> screens = (List<Screen>)
                            screenDao.getAllScreensByProgramAndLang(
                                    program.getId(),
                                    translateLang,
                                    true);
                    program.setScreens(screens);

                    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                    Collection<Language> langList = languageDao.geAll();

                    request.setAttribute("program", program);
                    request.setAttribute("languages", langList);
                    request.getRequestDispatcher("./all-screens.jsp?translateLang=" + translateLang).forward(request,
                            response);
                } catch (SQLException ex) {

                    // error page
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



