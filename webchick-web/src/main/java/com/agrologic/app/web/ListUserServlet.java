package com.agrologic.app.web;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;
import com.agrologic.app.service.CompanyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;

public class ListUserServlet extends AbstractServlet {

    private CompanyService companyService;

    public ListUserServlet() {
        companyService = new CompanyService();
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
                User user = (User) request.getSession().getAttribute("user");

                if (!companyService.isCompaniesInSession(request)) {
                    companyService.setCompaniesToSession(request);
                }

                try {
                    if ((user.getRole() == UserRole.USER) || (user.getRole() == UserRole.DISTRIBUTOR)) {
                        logger.info("access denied for user " + user);
                        request.getRequestDispatcher("./access-denied.jsp").forward(request, response);
                    } else {
                        UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                        String paramRole = request.getParameter("role");
                        Integer role = null;
                        if ((paramRole != null) && !"".equals(paramRole) && !"0".equals(paramRole)) {
                            role = Integer.parseInt(paramRole);
                        }
                        String company = request.getParameter("company");
                        if (company != null && (company.equals("All") || (company.equals("")))) {
                            company = null;
                        }
                        String index = request.getParameter("index");
                        if (index == null) {
                            index = "0";
                        }

                        String search = request.getParameter("searchText");
                        Collection<User> users = userDao.getAll(role, company, search, index);
                        int count = userDao.count(role, company, search);
                        setTableParameters(request, index, count);

                        CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                        for (User u : users) {
                            Collection<Cellink> cellinks = cellinkDao.getAllUserCellinks(u.getId());
                            u.setCellinks(cellinks);
                        }
                        logger.info("retrieve all users ");
                        request.setAttribute("users", users);
                        request.getRequestDispatcher("./all-users.jsp?role=" + paramRole).forward(request, response);
                    }
                } catch (SQLException ex) {
                    // error page
                    logger.error(getDefaultMessages(request).getString("message.error.retrieve.users"), ex);
                    request.setAttribute("message",  getMessages(request).getString("message.error.retrieve.users"));
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-users.jsp?role=0").forward(request, response);
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
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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



