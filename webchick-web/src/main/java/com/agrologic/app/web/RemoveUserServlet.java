package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class RemoveUserServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RemoveUserServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");
        } else {
            Long userId = Long.parseLong(request.getParameter("userId"));
            UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);

            try {
                User user = userDao.getById(userId);

                userDao.remove(user.getId());
                logger.info("User " + user + "successfully removed !");
                request.getSession().setAttribute("message",
                        "User with ID " + user.getId() + " and login " + user.getLogin() + " successfully  removed !");
                request.getSession().setAttribute("error", false);
                request.getRequestDispatcher("./all-users.html").forward(request, response);
            } catch (SQLException ex) {
                logger.error("Error occurs while removing cellink !");
                request.getSession().setAttribute("message", "Error occurs while removing user with ID  " + userId);
                request.getSession().setAttribute("error", true);
                request.getRequestDispatcher("./all-users.html").forward(request, response);
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



