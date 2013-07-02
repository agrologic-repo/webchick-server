
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.dao.impl.ActionSetDaoImpl;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.model.*;
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

public class ListTableData extends HttpServlet {

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
                long tableId = Long.parseLong(request.getParameter("tableId"));
                long translateLang;

                try {
                    translateLang = Long.parseLong(request.getParameter("translateLang"));
                } catch (NumberFormatException ex) {
                    translateLang = 1;    // default program
                }

                try {
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    ;
                    Screen screen = screenDao.getById(programId, screenId);

                    if (screen.getTitle().equals("Action Set Buttons")) {
                        ActionSetDao actionsetDao = new ActionSetDaoImpl();
                        List<ActionSetDto> actionset = actionsetDao.getAll();
                        LanguageDao languageDao = new LanguageDaoImpl();
                        List<LanguageDto> langList = languageDao.geAll();

                        request.getSession().setAttribute("languages", langList);
                        request.getRequestDispatcher("./all-actionset.jsp?screenId=" + screen.getId()
                                + "&translateLang=" + translateLang).forward(request, response);
                    } else {
                        TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                        Table table = tableDao.getById(programId, screenId, tableId);
                        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                        List<Data> dataList = dataDao.getTableDataList(screen.getProgramId(), screen.getId(),
                                table.getId(), translateLang, null);

                        table.setDataList(dataList);

                        List<Table> tables = new ArrayList<Table>();

                        tables.add(table);
                        screen.setTables(tables);
                        request.getSession().setAttribute("screen", screen);

                        LanguageDao languageDao = new LanguageDaoImpl();
                        List<LanguageDto> langList = languageDao.geAll();

                        request.getSession().setAttribute("languages", langList);
                        request.getRequestDispatcher("./all-tabledata.jsp?tableId=" + tableId + "&translateLang="
                                + translateLang).forward(request, response);
                    }
                } catch (SQLException ex) {

                    // error page
                    logger.error("database error ! " + ex.getMessage());
                    request.setAttribute("errormessage", ex.getMessage());
                    request.getRequestDispatcher("./all-tabledata.jsp?tableId=" + tableId).forward(request, response);
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



