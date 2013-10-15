package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AddUserFormServlet extends AbstractServlet {

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
        }

        String login = request.getParameter("Nusername");
        String password = request.getParameter("Npassword");
        String firstName = request.getParameter("Nfname");
        String lastName = request.getParameter("Nlname");
        String phone = request.getParameter("Nphone");
        String email = request.getParameter("Nemail");
        String role = request.getParameter("Nrole");
        String newCompanyList = request.getParameter("Ncompanylist");
        String newCompany = request.getParameter("Ncompany");
        String newCompanyCheckBox = request.getParameter("newCompany");
        User user = new User();

        user.setLogin(login);

        String encpsswd = com.agrologic.app.utils.Base64.encode(password);

        user.setPassword(encpsswd);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(UserRole.get(getUserRole(role)));

        if ("ON".equals(newCompanyCheckBox)) {
            user.setCompany(newCompany);
        } else {
            user.setCompany(newCompanyList);
        }

        try {
            UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);

            userDao.insert(user);
            logger.info("user " + user + " successfully added !");
            request.setAttribute("message", "User successfully added !");
            request.setAttribute("error", false);
            request.getRequestDispatcher("./all-users.html").forward(request, response);
        } catch (SQLException ex) {

            // error page
            logger.error("Error occurs while adding user.");
            request.setAttribute("message", "Error occurs while adding user.");
            request.setAttribute("error", true);
            request.getRequestDispatcher("./all-users.html").forward(request, response);
        } finally {
            out.close();
        }
    }

    private Integer getUserRole(String srole) {
        Integer role;

        try {
            role = Integer.parseInt(srole);
        } catch (NumberFormatException ex) {
            role = 0;
        }

        return role;
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



