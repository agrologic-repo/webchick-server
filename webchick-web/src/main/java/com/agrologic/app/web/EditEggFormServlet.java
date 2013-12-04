package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.EggDao;
import com.agrologic.app.model.Eggs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *
 */
public class EditEggFormServlet extends AbstractServlet {


    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException if a servlet-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        Long flockId = Long.parseLong(request.getParameter("flockId"));
        Integer day = Integer.parseInt(request.getParameter("day"));
        Integer numOfBirds = Integer.parseInt(request.getParameter("numOfBirds"));
        Integer eggQuantity = Integer.parseInt(request.getParameter("eggQuantity"));
        Integer softShelled = Integer.parseInt(request.getParameter("softShelled"));
        Integer cracked = Integer.parseInt(request.getParameter("cracked"));

        Eggs eggs = new Eggs();
        eggs.setFlockId(flockId);
        eggs.setDay(day);
        eggs.setNumOfBirds(numOfBirds);
        eggs.setEggQuantity(eggQuantity);
        eggs.setSoftShelled(softShelled);
        eggs.setCracked(cracked);

        EggDao eggDao = DbImplDecider.use(DaoType.MYSQL).getDao(EggDao.class);
        try {
            eggDao.update(eggs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException if a servlet-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
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
     * @throws javax.servlet.ServletException if a servlet-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
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
