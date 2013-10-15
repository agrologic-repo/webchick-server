package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.model.SystemState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class RemoveSystemStateServlet extends AbstractServlet {

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
                Long translateLang = Long.parseLong(request.getParameter("translateLang"));
                Long systemstateId = Long.parseLong(request.getParameter("systemstateId"));

                try {
                    SystemStateDao systemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(SystemStateDao.class);
                    SystemState systemstate = systemStateDao.getById(systemstateId);
                    systemStateDao.remove(systemstate.getId());
                    logger.info("SystemState " + systemstate + " successfully removed!");
                    request.setAttribute("message",
                            "SystemState <b style=\"color:gray\"> " + systemstate.getText()
                                    + " </b> successfully  removed !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-systemstates.html?translateLang="
                            + translateLang).forward(request, response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs during removing systemstate" + ex.getMessage());
                    request.setAttribute("message", "Error occurs during removing systemstate !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-systemstates.html").forward(request, response);
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



