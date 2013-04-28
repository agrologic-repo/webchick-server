
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.LaborDao;
import com.agrologic.app.dao.WorkerDao;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.dao.impl.LaborDaoImpl;
import com.agrologic.app.dao.impl.WorkerDaoImpl;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.FlockDto;
import com.agrologic.app.model.LaborDto;
import com.agrologic.app.model.WorkerDto;

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
public class AddLaborFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        final Logger logger = Logger.getLogger(AddLaborFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Long   cellinkId    = Long.parseLong(request.getParameter("cellinkId"));
            Long   controllerId = Long.parseLong(request.getParameter("controllerId"));
            Long   flockId      = Long.parseLong(request.getParameter("flockId"));
            String date         = request.getParameter("startDate");
            String hours        = request.getParameter("hours");
            String name         = request.getParameter("workersids");

            try {
                Long       workerId  = Long.parseLong(name);
                WorkerDao workerDao = new WorkerDaoImpl();
                WorkerDto  worker    = workerDao.getById(workerId);
                LaborDao  laborDao  = new LaborDaoImpl();
                LaborDto   labor     = new LaborDto();

                labor.setFlockId(flockId);
                labor.setWorkerId(worker.getId());
                labor.setDate(date);
                labor.setDefine(worker.getDefine());
                labor.setHours(Integer.parseInt(hours));

                float salary = Math.round(worker.getHourCost() * labor.getHours());

                labor.setSalary(salary);
                laborDao.insert(labor);
                logger.info("Labor added successfully to the database");

                List<LaborDto> labors            = laborDao.getAllByFlockId(flockId);
                float          totalLaborsSalary = 0;

                for (LaborDto l : labors) {
                    totalLaborsSalary += l.getSalary();
                }

                FlockDao flockDao = new FlockDaoImpl();
                FlockDto  flock    = flockDao.getById(flockId);

                flock.setTotalLabor(totalLaborsSalary);
                flockDao.update(flock);

                ControllerDao      controllerDao = new ControllerDaoImpl();
                List<ControllerDto> controllers   = controllerDao.getAllByCellinkId(cellinkId);

                for (ControllerDto controller : controllers) {
                    List<FlockDto> flocks = flockDao.getAllFlocksByController(controller.getId());

                    controller.setFlocks(flocks);
                }

                request.getSession().setAttribute("controllers", controllers);
                request.getRequestDispatcher("./rmctrl-add-labor.jsp?celinkId=" + cellinkId + "&controllerId="
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



