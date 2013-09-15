
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class RemoveCellinkServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RemoveCellinkServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            Long userId = Long.parseLong(request.getParameter("userId"));
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));

            try {
                UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Cellink cellink = cellinkDao.getById(cellinkId);

                cellinkDao.remove(cellink.getId());
                logger.info("Cellink " + cellink + "successfully removed !");
                request.getSession().setAttribute("message",
                        "Cellink with id " + cellink.getId() + " and name " + cellink.getName() + " successfully  removed !");
                request.getSession().setAttribute("error", false);

                Collection<User> users = new ArrayList<User>();
                String paramRole = request.getParameter("role");

                if ((paramRole == null) || "3".equals(paramRole)) {
                    users = userDao.getAll();
                    paramRole = "3";
                } else {
                    int role = Integer.parseInt(paramRole);
                    users = userDao.getAllByRole(role);
                }

                for (User u : users) {
                    Collection<Cellink> cellinks = cellinkDao.getAllUserCellinks(u.getId());
                    for (Cellink c : cellinks) {
                        Collection<Controller> controllers = controllerDao.getAllByCellink(c.getId());
                        c.setControllers(controllers);
                    }
                    u.setCellinks(cellinks);
                }
                logger.info("retrieve all users ");
                request.getSession().setAttribute("users", users);
                request.getRequestDispatcher("./userinfo.html?userId=" + userId).forward(request, response);
            } catch (SQLException ex) {

                // error page
                logger.error("Error occurs while removing cellink !");
                request.getSession().setAttribute("message", "Error occurs while removing cellink with id " + cellinkId);
                request.getSession().setAttribute("error", true);
                request.getRequestDispatcher("./userinfo.html?userId=" + userId).forward(request, response);
            } finally {
                out.close();
            }
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



