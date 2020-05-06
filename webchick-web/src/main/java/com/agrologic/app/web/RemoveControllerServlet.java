package com.agrologic.app.web;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class RemoveControllerServlet extends AbstractServlet {

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
            response.sendRedirect("/login.jsp");
        } else {
            User user = (User) request.getSession().getAttribute("user");
            String forwardLink = "./cellink-setting.html";

//            if (user.getRole() == UserRole.ADMIN) {
//                forwardLink = "./cellinkinfo.html";
//            } else {
//                forwardLink = "./cellink-setting.html";
//            }

            Long userId = Long.parseLong(request.getParameter("userId"));
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            Long controllerId = Long.parseLong(request.getParameter("controllerId"));

            try {
                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Controller controller = controllerDao.getById(controllerId);

                controllerDao.remove(controller.getId());
                logger.info("Controller " + controller + " successfully removed !");
                request.setAttribute("message",
                        "Controller with id " + controller.getId() + " and name " + controller.getName() +
                                " successfully  removed !");
                request.setAttribute("error", false);
                request.getRequestDispatcher(forwardLink + "?userId" + userId + "&cellinkId"
                        + cellinkId).forward(request, response);
            } catch (SQLException ex) {

                // error page
                logger.error("Error occurs while removing controller !");
                request.setAttribute("message", "Error occurs while removing controller with id  " +
                        controllerId);
                request.setAttribute("error", true);
                request.getRequestDispatcher(forwardLink + "?userId" + userId + "&cellinkId"
                        + cellinkId).forward(request, response);
                out.println("<script>window.top.location.href =" + request.getContextPath() + "/login.jsp" + "</script>");
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



