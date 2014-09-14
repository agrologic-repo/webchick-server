package com.agrologic.app.web;

import com.agrologic.app.model.Flock;
import com.agrologic.app.model.history.DayParam;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FlockGraphServlet extends AbstractServlet {
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
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            Long flockId = Long.parseLong(request.getParameter("flockId"));

            try {
                FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
                Flock flock = flockHistoryService.getFlock(flockId);
                request.setAttribute("flock", flock);

                DayParam growDayParam = new DayParam(request.getParameter("growDay"));
//                request.getRequestDispatcher("./rmctrl-flock-graphs.jsp?cellinkId=" + cellinkId + "&growDay="
//                        + growDayParam.getGrowDay()).forward(request, response);

                request.getRequestDispatcher("./rmctrl-flock-graphs-main.jsp?cellinkId=" + cellinkId + "&growDay="
                        + growDayParam.getGrowDay()).forward(request, response);

            } catch (Exception ex) {
                logger.trace("Fail save management setting", ex);
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



