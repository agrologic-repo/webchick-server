
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.MedicineDao;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.dao.impl.MedicineDaoImpl;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.FlockDto;
import com.agrologic.app.model.MedicineDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JanL
 */
public class RemoveMedicineServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(SaveBeginEndForm.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Long cellinkId  = Long.parseLong(request.getParameter("cellinkId"));
            Long flockId    = Long.parseLong(request.getParameter("flockId"));
            Long medicineId = Long.parseLong(request.getParameter("medicineId"));

            try {
                MedicineDao medicineDao = new MedicineDaoImpl();
                MedicineDto  medicine    = medicineDao.getById(medicineId);

                if (medicine == null) {
                    logger.info("Medicine " + medicineId + " can't be removed");
                    request.getRequestDispatcher("./rmctrl-add-medicine.jsp?celinkId=" + cellinkId + "&flockId="
                                                 + flockId).forward(request, response);
                } else {
                    medicineDao.remove(medicine.getId());
                    logger.info("Medicine removed successfully from the datebase");

                    List<MedicineDto> medicines     = medicineDao.getAllByFlockId(flockId);
                    float             totalMedicine = 0;

                    for (MedicineDto m : medicines) {
                        totalMedicine += m.getTotal();
                    }

                    FlockDao flockDao = new FlockDaoImpl();
                    FlockDto  flock    = flockDao.getById(flockId);

                    flock.setTotalMedic(totalMedicine);
                    flockDao.update(flock);

                    ControllerDao      controllerDao = new ControllerDaoImpl();
                    List<ControllerDto> controllers   = controllerDao.getAllByCellinkId(cellinkId);

                    for (ControllerDto controller : controllers) {
                        List<FlockDto> flocks = flockDao.getAllFlocksByController(controller.getId());

                        controller.setFlocks(flocks);
                    }

                    request.getSession().setAttribute("controllers", controllers);
                    request.getRequestDispatcher("./rmctrl-add-medicine.jsp?celinkId=" + cellinkId + "&flockId="
                                                 + flockId).forward(request, response);
                }
            } catch (SQLException ex) {
                logger.info("Error occurs durring removing medicine");
                request.getRequestDispatcher("./rmctrl-add-medicine.jsp?celinkId=" + cellinkId + "&flockId="
                                             + flockId).forward(request, response);
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>
}



