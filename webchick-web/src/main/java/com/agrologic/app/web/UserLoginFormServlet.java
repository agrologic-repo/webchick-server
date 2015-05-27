package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;
import com.agrologic.app.service.UserManagerService;
import com.agrologic.app.service.impl.UserManagerServiceImpl;
import com.agrologic.app.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class UserLoginFormServlet extends AbstractServlet {
    public static final String MY_FARMS_URL = "/my-farms.html";
    public static final String OVERVIEW_URL = "/overview.html";
    private UserManagerService userManagerService;

    public UserLoginFormServlet() {
        super();
        userManagerService = new UserManagerServiceImpl(DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class));
    }

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
    }

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

        String name = request.getParameter("name");
        String pass = request.getParameter("password");
        String reme = request.getParameter("remember");

        PrintWriter out = response.getWriter();

        try {
            String encpsswd = Base64.encode(pass);
            User user = userManagerService.validate(name, encpsswd);

            user.setPassword(pass);
            logger.info("login user : " + name);

            if (user.getValidate() == false) {
                logger.error("username " + name + " and password: " + pass + " not found");
                logger.warn("User tried to bypass login. remote IP = " + request.getRemoteHost());
                request.setAttribute("errormessage", "incorrect user name and/or password");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                if ((reme != null) && "ON".equals(reme.toUpperCase())) {
                    setCookies(true, name, pass, response);
                } else {
                    setCookies(false, name, pass, response);
                }

                logger.info(user + " , successfully logged in system .");
                logger.warn("User tried to bypass login. remote IP = " + request.getRemoteHost());
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("access", "admin");
                logger.info(request.getServerName());

                switch (user.getRole()) {
                    case USER:
                        response.sendRedirect(getURLWithContextPath(request) + MY_FARMS_URL + "?userId=" + user.getId());
                        break;

                    case DISTRIBUTOR:
                        response.sendRedirect(getURLWithContextPath(request) + OVERVIEW_URL);
                        break;

                    case ADMIN:
                        response.sendRedirect(getURLWithContextPath(request) + OVERVIEW_URL);
                        break;

                    default:
                        user.setUserRole(UserRole.ADMIN);// TODO: test if role not defined need to delete in release
                        response.sendRedirect(getURLWithContextPath(request) + OVERVIEW_URL);
                        break;
                }
            }
        } catch (SQLException ex) {
            logger.error("Database error while loggin user!", ex);
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } finally {
            out.close();
        }
    }

    public void setCookies(boolean remember, String user, String pass, HttpServletResponse response) {
        if (remember == true) {
            Cookie rememberCookie = new Cookie("remember", "true");

            rememberCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(rememberCookie);

            Cookie userCookie = new Cookie("name", user);

            userCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(userCookie);

            Cookie passCookie = new Cookie("password", pass);

            passCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(passCookie);
        } else {
            Cookie cookie = new Cookie("remember", "false");

            cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(cookie);
        }
    }

//  <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

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



