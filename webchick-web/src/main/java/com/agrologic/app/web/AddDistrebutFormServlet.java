
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DistribDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.DistribDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.DistribDto;
import com.agrologic.app.model.FlockDto;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * @author JanL
 */
public class AddDistrebutFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(AddDistrebutFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            Long controllerId = Long.parseLong(request.getParameter("controllerId"));
            Long flockId = Long.parseLong(request.getParameter("flockId"));
            String date = request.getParameter("startDate");
            String price = request.getParameter("price");
            String numberAccount = request.getParameter("numberAccount");
            String total = request.getParameter("total");
            String account = request.getParameter("account");
            String target = request.getParameter("target");
            String sex = request.getParameter("sex");
            String quantbirds = request.getParameter("quantbirds");
            String quantAKg = request.getParameter("QuantAKg");
            String quantBKg = request.getParameter("QuantBKg");
            String quantCKg = request.getParameter("QuantCKg");
            String priceA = request.getParameter("PriceA");
            String priceB = request.getParameter("PriceB");
            String priceC = request.getParameter("PriceC");
            String quantA = request.getParameter("QuantA");
            String quantB = request.getParameter("QuantB");
            String quantC = request.getParameter("QuantC");
            String veterinKg = request.getParameter("VeterinKg");
            String veterin = request.getParameter("Veterin");
            String anotherKg = request.getParameter("anotherKg");
            String another = request.getParameter("anotherKg");

            try {
                DistribDao gasDao = new DistribDaoImpl();
                DistribDto disrib = new DistribDto();
                disrib.setTotal(Float.parseFloat(total));
                gasDao.insert(disrib);

                FlockDao flockDao = new FlockDaoImpl();
                FlockDto flock = flockDao.getById(flockId);
                flockDao.update(flock);
                logger.info("Distrib added successfully to the database");
                ControllerDao controllerDao = new ControllerDaoImpl();
                List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellinkId);
                for (ControllerDto controller : controllers) {
                    List<FlockDto> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }
                request.getSession().setAttribute("controllers", controllers);
                request.getRequestDispatcher("./rmctrl-add-gas.jsp?celinkId=" + cellinkId + "&controllerId="
                        + controllerId + "&flockId=" + flockId).forward(request, response);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
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



