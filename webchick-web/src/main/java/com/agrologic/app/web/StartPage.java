package com.agrologic.app.web;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StartPage extends AbstractServlet {


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
            String access = request.getParameter("access");

            if (access.equals("regular")) {
                request.setAttribute("access", access);

                CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                Cellink cellink = cellinkDao.getActualCellink();
                logger.info("Current cellink {} ", cellink);

                UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                User user = userDao.getById(cellink.getUserId());

                logger.info("Current user  {} ", user);
                request.setAttribute("user", user);
                request.getRequestDispatcher("./rmctrl-main-screen.html?userId=" + cellink.getUserId()
                        + "&cellinkId=" + cellink.getId()).forward(request, response);
            } else {
                response.sendRedirect("./login.jsp");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            request.getRequestDispatcher("./errorPage.jsp").forward(request, response);
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



