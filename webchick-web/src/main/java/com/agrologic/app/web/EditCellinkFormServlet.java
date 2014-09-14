package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class EditCellinkFormServlet extends AbstractServlet {

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
                Long userId = Long.parseLong(request.getParameter("Nuserid"));
                Long cellinkId = Long.parseLong(request.getParameter("Ncellinkid"));
                String name = request.getParameter("Ncellinkname");
                String password = request.getParameter("Npassword");
                String simNumber = request.getParameter("Nsim");
                String type = request.getParameter("Ntype");

                try {
                    UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                    CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);

                    Cellink cellink = cellinkDao.getById(cellinkId);
                    cellink.setName(name);
                    cellink.setPassword(password);
                    cellink.setSimNumber(simNumber);
                    cellink.setType(type);
                    cellinkDao.update(cellink);
                    logger.info("Cellink " + cellink + "successfully updated !");
                    request.setAttribute("message", "Cellink successfully updated !");
                    request.setAttribute("error", false);

                    // ////////////////////////////////////////////////
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
                    request.setAttribute("users", users);
                    request.getRequestDispatcher("./userinfo.html?userId=" + userId).forward(request, response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs while updating cellink !");
                    request.setAttribute("message", "Error occurs while updating cellink !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./userinfo.html?userId=" + userId).forward(request, response);
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



