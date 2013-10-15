package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Table;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class RemoveDataServlet extends AbstractServlet {

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
                Long tableId = Long.parseLong(request.getParameter("tableId"));
                Long dataId = Long.parseLong(request.getParameter("dataId"));

                try {

                    TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                    Table table = tableDao.getById(programId, screenId, tableId);
                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    Data data = dataDao.getById(dataId);

                    dataDao.removeDataFromTable(programId, screenId, table.getId(), data.getId());
                    logger.info("Data " + data + "successfully removed !");
                    request.setAttribute("message", "Data successfully  removed !");
                    dataDao.removeSpecialDataFromTable(programId, data.getId());
                    logger.info("Special Data " + data + "successfully removed !");
                    request.setAttribute("message", "Special Data successfully  removed !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-tabledata.html?screenId=" + table.getScreenId() + "&tableId="
                            + table.getId()).forward(request, response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs while removing controlller !" + ex.getMessage());
                    request.setAttribute("message", "Error occurs while removing user !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-tabledata.html").forward(request, response);
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



