package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.LaborDaoImpl;
import com.agrologic.app.dao.mysql.impl.WorkerDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.Labor;
import com.agrologic.app.model.Worker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class AddLaborFormServlet extends AbstractServlet {


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
            String hours = request.getParameter("hours");
            String name = request.getParameter("workersids");

            try {
                Long workerId = Long.parseLong(name);
                WorkerDao workerDao = DbImplDecider.use(DaoType.MYSQL).getDao(WorkerDaoImpl.class);
                Worker worker = workerDao.getById(workerId);
                LaborDao laborDao = DbImplDecider.use(DaoType.MYSQL).getDao(LaborDaoImpl.class);
                Labor labor = new Labor();
                labor.setFlockId(flockId);
                labor.setWorkerId(worker.getId());
                labor.setDate(date);
                labor.setDefine(worker.getDefine());
                labor.setHours(Integer.parseInt(hours));

                float salary = Math.round(worker.getHourCost() * labor.getHours());

                labor.setSalary(salary);
                laborDao.insert(labor);
                logger.info("Labor added successfully to the database");

                List<Labor> labors = laborDao.getAllByFlockId(flockId);
                float totalLaborsSalary = 0;

                for (Labor l : labors) {
                    totalLaborsSalary += l.getSalary();
                }

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);

                flock.setTotalLabor(totalLaborsSalary);
                flockDao.update(flock);

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);

                for (Controller controller : controllers) {
                    Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }

                request.setAttribute("controllers", controllers);
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



