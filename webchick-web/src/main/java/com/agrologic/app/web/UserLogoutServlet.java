package com.agrologic.app.web;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;


public class UserLogoutServlet extends AbstractServlet {


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
        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");
        } else {
            User user = (User) request.getSession().getAttribute("user");
            CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);//DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);

            try {
                Collection<Cellink> cellinks = cellinkDao.getAllUserCellinks(user.getId());
                for (Cellink cellink : cellinks) {
                    if ((cellink.getState() == CellinkState.STATE_RUNNING)
                            || (cellink.getState() == CellinkState.STATE_START)) {
                        cellink.setState(CellinkState.STATE_STOP);
                        cellinkDao.update(cellink);
                    }
                }

                Cookie logout = new Cookie("logout", "true");
                logout.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(logout);
                logger.info(user + " , successfully logged out from system .");
                request.getSession().invalidate();    // clear session
                request.getRequestDispatcher("./logout.jsp").forward(request, response);
            } catch (SQLException ex) {
                logger.error("database error while user : " + user + " logout user!", ex);
            } finally {
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

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



