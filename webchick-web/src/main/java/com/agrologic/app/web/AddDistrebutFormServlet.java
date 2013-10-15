package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.DistribDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Distrib;
import com.agrologic.app.model.Flock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;

public class AddDistrebutFormServlet extends AbstractServlet {


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
                DistribDao distribDao = DbImplDecider.use(DaoType.MYSQL).getDao(DistribDaoImpl.class);
                Distrib disrib = new Distrib();
                disrib.setTotal(Float.parseFloat(total));
                distribDao.insert(disrib);

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);
                flockDao.update(flock);
                logger.info("Distrib added successfully to the database");

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);
                for (Controller controller : controllers) {
                    Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }
                request.setAttribute("controllers", controllers);
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



