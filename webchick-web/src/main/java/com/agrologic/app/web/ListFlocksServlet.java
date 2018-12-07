package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class ListFlocksServlet extends AbstractServlet {


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                Long userId = Long.parseLong(request.getParameter("userId"));
                Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));

                try {
                    CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                    Cellink cellink = cellinkDao.getById(cellinkId);
                    request.setAttribute("cellink", cellink);

                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                    Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);
                    request.setAttribute("controllers", controllers);

                    FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                    for (Controller controller : controllers) {
                        Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                        // 01/03/2018
                        for (Flock f : flocks){
                            if (f != null && f.getStatus().toLowerCase().equals("Open".toLowerCase())) {
                                checkAndFixStartDateAndFlock(f, flockDao, controller);
                            }
                        }
                        // 01/03/2018
                        controller.setFlocks(flocks);
                    }

                    logger.info("retrieve user and user cellinks and all controllers of each cellink");
                    request.getRequestDispatcher("./rmctrl-controller-flocks.jsp?userId=" + userId + "&celinkId="
                            + cellinkId).forward(request, response);
                } catch (SQLException ex) {
                    request.getRequestDispatcher("./rmctrl-controller-flocks.jsp?userId=" + userId + "&celinkId="
                            + cellinkId).forward(request, response);
                }
            }
        } finally {
            out.close();
        }
    }

    // 01/03/2018
    private void checkAndFixStartDateAndFlock(Flock f, FlockDao flockDao, Controller controller) throws SQLException {
        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        Data onlineGrowDay = dataDao.getGrowDay(controller.getId());
        Integer updatedGrowDay = flockDao.getUpdatedGrowDayHistory(f.getFlockId());
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        Date currentDay = getCurrentDay();

        if (onlineGrowDay != null && onlineGrowDay.getValue() != -1 && updatedGrowDay != null){
            //fix flock status
            if(updatedGrowDay >= onlineGrowDay.getValue()){
                f.setStatus("Close");
                f.setEndDate(sdf.format(currentDay));
                flockDao.updateFlockStatus(f);
                flockDao.updateFlockEndDay(f);
                if (!(updatedGrowDay + 1 == onlineGrowDay.getValue()) && f.getStatus().toLowerCase().equals("Open".toLowerCase())){
                    //fix start date
                    try {
                        Date startDate = sdf.parse(f.getStartTime());
                        Date onlineGrowDayDate = addDays(startDate, onlineGrowDay.getValue().intValue());
                        if (currentDay.compareTo(onlineGrowDayDate) != 0) {
                            Date startDateToSet = addDays(currentDay, -((onlineGrowDay.getValue().intValue() - 1)));
                            f.setStartDate(sdf.format(startDateToSet));
                            flockDao.updateFlockStartDay(f);
                        }
                    } catch (ParseException e) {

                    }
                }
            } else if(onlineGrowDay.getValue() > 1){
                //fix start date
                try {
                    Date startDate = sdf.parse(f.getStartTime());
                    Date onlineGrowDayDate = addDays(startDate, onlineGrowDay.getValue().intValue());
                    if (currentDay.compareTo(onlineGrowDayDate) != 0) {
                        Date startDateToSet = addDays(currentDay, -((onlineGrowDay.getValue().intValue() - 1)));
                        f.setStartDate(sdf.format(startDateToSet));
                        flockDao.updateFlockStartDay(f);
                    }
                } catch (ParseException e) {

                }
            }
        }
    }
    // 01/03/2018

    private Date addDays(Date date, int days) {//01/03/2018
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days

        return cal.getTime();
    } //01/03/2018

    private Date getCurrentDay(){ //01/03/2018
        Date date;

        Calendar cal = Calendar.getInstance();

        return date = cal.getTime();
    } //01/03/2018

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
