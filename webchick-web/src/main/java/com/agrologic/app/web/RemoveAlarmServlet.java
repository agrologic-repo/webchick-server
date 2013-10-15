package com.agrologic.app.web;

import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Alarm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RemoveAlarmServlet extends AbstractServlet {

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
                Long translateLang = Long.parseLong(request.getParameter("translateLang"));
                Long alarmId = Long.parseLong(request.getParameter("alarmId"));
                try {
                    AlarmDao alarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(AlarmDao.class);
                    Alarm alarm = alarmDao.getById(alarmId);
                    alarmDao.remove(alarm);
                    logger.info("Alarm [{}] successfully removed!", alarm);
                    request.setAttribute("message", "Alarm <b style=\"color:gray\">" + alarm.getText() + "</b> successfully removed !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-alarms.html?translateLang=" + translateLang).forward(request, response);
                } catch (Exception ex) {
                    // error page
                    logger.error(ex.getMessage());
                    request.setAttribute("message", "Error occurs during removing alarm ");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-alarms.html").forward(request, response);
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



