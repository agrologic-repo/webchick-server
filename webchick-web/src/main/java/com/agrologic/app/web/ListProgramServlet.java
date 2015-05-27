package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ListProgramServlet extends AbstractServlet {

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
                try {
                    String searchText = request.getParameter("searchText");
                    if (searchText == null) {
                        searchText = "";
                    }

                    String index = request.getParameter("index");
                    if (index == null) {
                        index = "0";
                    }

                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    User user = (User) request.getSession().getAttribute("user");
                    UserRole userRole = user.getRole();

                    int count;
                    Collection<Program> programs = new ArrayList<Program>();
                    switch (userRole) {
                        case USER:
                            count = programDao.count(searchText);
                            setTableParameters(request, index, count);
                            break;

                        case ADMIN:
                            programs = programDao.getAll(searchText, index);
                            count = programDao.count(searchText);
                            setTableParameters(request, index, count);
                            break;

                        case DISTRIBUTOR:
                            programs = programDao.getAllByUserCompany(searchText, user.getCompany());
                            count = programs.size();
                            setTableParameters(request, index, count);
                            break;

                        default:
                            break;
                    }
                    request.setAttribute("programs", programs);
                    request.getRequestDispatcher("./all-programs.jsp?searchText=" + searchText).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("database error ! " + ex.getMessage());
                    request.setAttribute("errormessage", ex.getMessage());
                    request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
                }
            }
        } finally {
            out.close();
        }
    }

    private void setTableParameters(HttpServletRequest request, String index, int count) {
        if (index.equals("0")) {
            int from = 1;
            int to = 25;
            int of = count;

            request.setAttribute("from", from);
            request.setAttribute("to", to);
            request.setAttribute("of", of);
        } else {
            int from = Integer.parseInt(index);
            int to = from + ((count - from) > 25
                    ? 25
                    : (count - from));
            int of = count;

            request.setAttribute("from", from);
            request.setAttribute("to", to);
            request.setAttribute("of", of);
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



