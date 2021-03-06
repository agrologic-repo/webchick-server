package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.Screen;
import com.agrologic.app.util.DateLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class EditScreenFormServlet extends AbstractServlet {

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
                Long programId = Long.parseLong(request.getParameter("programid"));
                Long screenId = Long.parseLong(request.getParameter("screenid"));
                String newTitle = request.getParameter("Ntitle");
                Integer newPosition = Integer.parseInt(request.getParameter("Nposition"));
                String newDescript = request.getParameter("Ndescript");

                try {
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    Screen screen = screenDao.getById(programId, screenId);

                    screen.setTitle(newTitle);
                    screen.setPosition(newPosition);
                    screen.setDescript(newDescript);
                    screenDao.update(screen);
                    logger.info("Screen " + screen + "successfully updated !");

                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);
                    String modified = DateLocal.currentDate();

                    program.setModifiedDate(modified);
                    programDao.update(program);
                    request.setAttribute("message", "Screen successfully updated !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-screens.html?programId="
                            + screen.getProgramId()).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating screen !" + ex.getMessage());
                    request.setAttribute("message", "Error occurs while updating screen !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request,
                            response);
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



