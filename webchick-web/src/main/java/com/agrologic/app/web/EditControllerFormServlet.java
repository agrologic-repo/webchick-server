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

public class EditControllerFormServlet extends AbstractServlet {

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
        logger.error("Unauthorized access!");
        PrintWriter out = response.getWriter();
        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                String forwardLink = "./cellink-setting.html";
                Long userId = Long.parseLong(request.getParameter("userId"));
                Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
                Long controllerId = Long.parseLong(request.getParameter("controllerId"));

                String title = request.getParameter("title");
                String netName = request.getParameter("netname");
                Long programId = Long.parseLong(request.getParameter("programId"));
                String controllerType = request.getParameter("controllerType");
                String active = request.getParameter("active");

                try {
                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                    Controller controller = controllerDao.getById(controllerId);
                    controller.setNetName(netName);
                    controller.setTitle(title);
                    controller.setCellinkId(cellinkId);
                    controller.setProgramId(programId);
                    controller.setName(controllerType);
                    if ((active != null) && "ON".equals(active.toUpperCase())) {
                        controller.setActive(true);
                    } else {
                        controller.setActive(false);
                    }
                    controllerDao.update(controller);

                    logger.info("Controller " + controller + " successfully updated !");
                    request.setAttribute("message", "Controller with ID " + controller.getId() + " successfully updated !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher(forwardLink + "?userId" + userId + "&cellinkId"
                            + cellinkId).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating controller !");
                    request.setAttribute("message", "Error occurs while updating controller  .");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher(forwardLink + "?userId" + userId + "&cellinkId"
                            + cellinkId).forward(request, response);
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



