package com.agrologic.app.web;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ActivateControllerServlet extends AbstractServlet {
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
                Long userId = Long.parseLong(request.getParameter("userId"));
                Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
                Long controllerId = Long.parseLong(request.getParameter("controllerId"));
                String active = request.getParameter("active");
                String url = request.getParameter("url");

                try {
                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                    Controller controller = controllerDao.getById(controllerId);
                    if ((active != null) && "ON".equals(active.toUpperCase())) {
                        setInfoMessage(request,
                                MessageFormat.format(getDefaultMessages(request).getString("message.info.controller.activated"),
                                        new Object[]{controller.getId()}),
                                MessageFormat.format(getMessages(request).getString("message.info.controller.activated"),
                                        new Object[]{controller.getId()}));
                        controller.setActive(true);
                    } else {
                        setInfoMessage(request,
                                MessageFormat.format(getDefaultMessages(request).getString("message.info.controller.deactivated"),
                                        new Object[]{controller.getId()}),
                                MessageFormat.format(getMessages(request).getString("message.info.controller.deactivated"),
                                        new Object[]{controller.getId()}));
                        controller.setActive(false);
                    }
                    controllerDao.update(controller);


                    if (url.equals("cellink-setting.html")) {
                        request.getRequestDispatcher("./cellink-setting.html?userId" + userId + "&cellinkId"
                                + cellinkId).forward(request, response);
                    } else {
                        request.getRequestDispatcher("./overview.html?userId" + userId + "&cellinkId"
                                + cellinkId).forward(request, response);
                    }
                } catch (SQLException ex) {
                    setErrorMessage(request,
                            MessageFormat.format(getMessages(request).getString("message.error.remove"),
                                    new Object[]{controllerId}),
                            MessageFormat.format(getMessages(request).getString("message.error.remove"),
                                    new Object[]{controllerId}), ex);

                    if (url.equals("cellink-setting.html")) {
                        request.getRequestDispatcher("./cellink-setting.html?userId" + userId + "&cellinkId"
                                + cellinkId).forward(request, response);
                    } else {
                        request.getRequestDispatcher("./overview.html?userId" + userId + "&cellinkId"
                                + cellinkId).forward(request, response);
                    }
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



