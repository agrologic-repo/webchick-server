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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFlockFormServlet extends AbstractServlet {


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
        request.setCharacterEncoding("UTF-8");

        if (!CheckUserInSession.isUserInSession(request)) {

            logger.error("Unauthorized access!");
            response.sendRedirect("./login.jsp");

        } else {

            Flock flock = new Flock();
            Long user_id = Long.parseLong(request.getParameter("userId"));
            Long cellink_id = Long.parseLong(request.getParameter("cellinkId"));

            try {

                String [] cont_ids = request.getParameterValues("house_filter");

                for (int i = 0; i < cont_ids.length; i++) {
                    Long controller_id = Long.parseLong(cont_ids[i]);
                    String flock_name = request.getParameter("flockName");
                    Date date = get_current_day();

                    SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
                    sdf.setLenient(false);
                    String start_date = sdf.format(date);


                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                    Controller controller = controllerDao.getById(controller_id);

                    if (controller.isActive()) {

                        if (flock_name == null || flock_name.equals("")) {
                            flock_name = controller.getTitle();
                        }

                        flock.setFlockName(flock_name);
                        flock.setControllerId(controller_id);
                        flock.setStatus("Open");
                        flock.setStartDate(start_date);
                        flock.setProgramId(controller.getProgramId());

                        FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);

                        if (!flockDao.open_flock_is_exist(controller_id)) {
                            flockDao.insert(flock);
                            setInfoMessage(request,
                                    MessageFormat.format(getDefaultMessages(request).getString("message.success.add"),
                                            new Object[]{controller.getId()}),
                                    MessageFormat.format(getMessages(request).getString("message.success.add"),
                                            new Object[]{controller.getId()}));
                        }
                    }
                }

                response.sendRedirect("./flocks.html?userId=" + user_id + "&cellinkId=" + cellink_id);

            } catch (Exception ex) {

                setInfoMessage(request,
                               MessageFormat.format(getDefaultMessages(request).getString("message.error.add"),
                               new Object[]{flock.getFlockName()}),
                               MessageFormat.format(getMessages(request).getString("message.error.add"),
                               new Object[]{flock.getFlockName()}));
                request.getRequestDispatcher("./flocks.html?userId=" + user_id + "&cellinkId=" + cellink_id).forward(request, response);

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private Date get_current_day(){
        Date date;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        date = cal.getTime();

        return date;
    }
}


//    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        request.setCharacterEncoding("UTF-8");
//
//        if (!CheckUserInSession.isUserInSession(request)) {
//            logger.error("Unauthorized access!");
//            response.sendRedirect("./login.jsp");
//        } else {
//            Long userId = Long.parseLong(request.getParameter("userId"));
//            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
//            Long controllerId = Long.parseLong(request.getParameter("house_Filter"));
//            String flockName = request.getParameter("flockName");
//            if (flockName == null){
//
//            }
//            String status = request.getParameter("status_Filter");
//            String startDate = request.getParameter("startDate");
//            String endDate = request.getParameter("endDate");
//
//
//            Flock flock = new Flock();
//            flock.setFlockName(flockName);
//            flock.setControllerId(controllerId);
//            flock.setStatus(status);
//            flock.setStartDate(startDate);
//            flock.setEndDate(endDate);
//
//            try {
//                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
//                Controller controller = controllerDao.getById(controllerId);
//
//                flock.setProgramId(controller.getProgramId());
//
//                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
//
//                flockDao.insert(flock);
//                setInfoMessage(request,
//                        MessageFormat.format(getDefaultMessages(request).getString("message.success.add"),
//                                new Object[]{controller.getId()}),
//                        MessageFormat.format(getMessages(request).getString("message.success.add"),
//                                new Object[]{controller.getId()}));
//                response.sendRedirect("./flocks.html?userId=" + userId + "&cellinkId=" + cellinkId);
//            } catch (Exception ex) {
//                setInfoMessage(request,
//                        MessageFormat.format(getDefaultMessages(request).getString("message.error.add"),
//                                new Object[]{flock.getFlockName()}),
//                        MessageFormat.format(getMessages(request).getString("message.error.add"),
//                                new Object[]{flock.getFlockName()}));
//                request.getRequestDispatcher("./flocks.html?userId=" + userId + "&cellinkId=" + cellinkId).forward(request, response);
//            } finally {
//                out.close();
//            }
//        }
//    }
