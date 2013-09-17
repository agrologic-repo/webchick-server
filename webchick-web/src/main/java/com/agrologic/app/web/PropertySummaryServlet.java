
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;

public class PropertySummaryServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(ListUserCellinksServlet.class);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                User user = (User) request.getSession().getAttribute("user");
                try {
                    if (user.getRole() == UserRole.ADMIN) {
                        UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                        CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                        ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                        Collection<User> users = userDao.getAll();
                        for (User u : users) {
                            Collection<Cellink> cellinks = cellinkDao.getAllUserCellinks(u.getId());
                            for (Cellink c : cellinks) {
                                Collection<Controller> controllers = controllerDao.getAllByCellink(c.getId());
                                c.setControllers(controllers);
                            }
                            u.setCellinks(cellinks);
                        }
                        request.getSession().setAttribute("users", users);
                        request.getRequestDispatcher("./propertysummary.jsp").forward(request, response);
                    } else if (user.getRole() == UserRole.USER) {
                        logger.info("access denied for user " + user);
                        request.getRequestDispatcher("./access-denied.jsp").forward(request, response);
                    }
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



