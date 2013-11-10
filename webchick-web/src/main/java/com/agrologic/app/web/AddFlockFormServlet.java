package com.agrologic.app.web;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;


public class AddFlockFormServlet extends AbstractServlet {


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
            Long userId = Long.parseLong(request.getParameter("userId"));
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            Long controllerId = Long.parseLong(request.getParameter("house_Filter"));
            String flockName = request.getParameter("flockName");
            String status = request.getParameter("status_Filter");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            Flock flock = new Flock();
            flock.setFlockName(flockName);
            flock.setControllerId(controllerId);
            flock.setStatus(status);
            flock.setStartDate(startDate);
            flock.setEndDate(endDate);

            try {
                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Controller controller = controllerDao.getById(controllerId);

                flock.setProgramId(controller.getProgramId());

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);

                flockDao.insert(flock);
                setInfoMessage(request,
                        MessageFormat.format(getDefaultMessages(request).getString("message.success.add"),
                                new Object[]{controller.getId()}),
                        MessageFormat.format(getMessages(request).getString("message.success.add"),
                                new Object[]{controller.getId()}));
                response.sendRedirect("./flocks.html?userId=" + userId + "&cellinkId=" + cellinkId);
            } catch (Exception ex) {
                setInfoMessage(request,
                        MessageFormat.format(getDefaultMessages(request).getString("message.error.add"),
                                new Object[]{flock.getFlockName()}),
                        MessageFormat.format(getMessages(request).getString("message.error.add"),
                                new Object[]{flock.getFlockName()}));
                request.getRequestDispatcher("./flocks.html?userId=" + userId + "&cellinkId="
                        + cellinkId).forward(request, response);
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



