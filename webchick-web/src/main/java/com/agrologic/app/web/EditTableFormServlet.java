package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.Table;
import com.agrologic.app.utils.DateLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class EditTableFormServlet extends AbstractServlet {

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
                Long programId = Long.parseLong(request.getParameter("programId"));
                Long oldScreenId = Long.parseLong(request.getParameter("screenId"));
                Long tableId = Long.parseLong(request.getParameter("tableId"));
                String newTitle = request.getParameter("Ntabletitle");
                Long newScreenId = Long.parseLong(request.getParameter("NscreenId"));
                Integer newPosition = Integer.parseInt(request.getParameter("Nposition"));
                TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                try {
                    logger.error(String.format(" programId %d  oldScreenId %d tableId %d", programId, oldScreenId,
                            tableId));
                    Table table = tableDao.getById(programId, oldScreenId, tableId);
                    if (table != null) {
                        table.setTitle(newTitle);
                        table.setPosition(newPosition);
                        table.setScreenId(newScreenId);
                        tableDao.moveTable(table, oldScreenId);
                    }

                    logger.info("Table " + table + " successfully updated !");
                    dataDao.moveData(newScreenId, programId, tableId);
                    logger.info("Data from table " + table + " successfully moved !");

                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);

                    program.setModifiedDate(DateLocal.currentDate());
                    programDao.update(program);
                    request.setAttribute("message", "Table successfully updated !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-tables.html?programId=" + programId + "&screenId="
                            + oldScreenId).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating table !" + "\n" + ex.getMessage());
                    ex.printStackTrace();
                    request.setAttribute("message", "Error occurs while updating table !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-tables.html?programId=" + programId + "&screenId="
                            + oldScreenId).forward(request, response);
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



