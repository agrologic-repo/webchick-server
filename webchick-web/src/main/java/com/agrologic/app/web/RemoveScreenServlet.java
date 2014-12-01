package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Table;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;


public class RemoveScreenServlet extends AbstractServlet {

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
                Long screenId = Long.parseLong(request.getParameter("screenId"));

                try {
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

                    if (CheckDefaultProgram.isDefaultProgram(programId)) {
                        logger.error("Can't remove default program screen!");
                        request.setAttribute("message", "Can't remove default program screen!");
                        request.setAttribute("error", true);
                        request.getRequestDispatcher("./all-screens.html").forward(request, response);
                    } else {
                        Collection<Table> tables = tableDao.getScreenTables(programId, screenId, null);
                        for (Table t : tables) {
                            dataDao.removeDataFromTable(programId, screenId, t.getId());
                        }
                        screenDao.remove(programId, screenId);
                        logger.info("Screen " + screenId + " successfully removed !");
                        request.setAttribute("message", "Screen successfully  removed !");
                        request.setAttribute("error", false);
                        request.getRequestDispatcher("./all-screens.html").forward(request, response);
                    }
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs while removing screen !");
                    logger.error("Reasone : ", ex);
                    request.setAttribute("message", "Error occurs while removing screen !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-screens.html").forward(request, response);
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



