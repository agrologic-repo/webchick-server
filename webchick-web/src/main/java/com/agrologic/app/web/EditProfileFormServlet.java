package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import com.agrologic.app.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class EditProfileFormServlet extends AbstractServlet {

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

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");
        } else {
            User currUser = (User) request.getSession().getAttribute("user");

            String password = request.getParameter("Npassword");
            String firstName = request.getParameter("Nfname");
            String lastName = request.getParameter("Nlname");
            String phoneNumber = request.getParameter("Nphone");
            String email = request.getParameter("Nemail");
            String encpsswd = Base64.encode(password);

            User user = currUser;
            user.setPassword(encpsswd);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phoneNumber);
            user.setEmail(email);
            String forwardLink = "./my-profile.jsp?userId=" + user.getId();

            UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);

            try {
                userDao.update(user);
                logger.info("User " + user + " successfully updated !");
                request.setAttribute("message", "User successfully updated !");
                request.setAttribute("error", false);
                request.getRequestDispatcher(forwardLink).forward(request, response);
            } catch (SQLException ex) {

                // error page
                logger.error("Error occurs while updating user !");
                request.setAttribute("message", "Error occurs while updating user !");
                request.setAttribute("error", true);
                request.getRequestDispatcher(forwardLink).forward(request, response);
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



