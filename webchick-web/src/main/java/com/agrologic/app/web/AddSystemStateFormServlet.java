package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.model.SystemState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AddSystemStateFormServlet extends AbstractServlet {

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
            Long systemStateId = Long.parseLong(request.getParameter("NsystemstateId"));
            String systemStateText = request.getParameter("NsystemstateText");
            Long translateLang = Long.parseLong(request.getParameter("translateLang"));

            SystemState systemState = new SystemState();
            systemState.setId(systemStateId);
            systemState.setText(systemStateText);

            SystemStateDao systemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(SystemStateDao.class);

            try {
                systemStateDao.insert(systemState);
                logger.info("System State " + systemState.getText() + "  successfully added");
                systemStateDao.insertTranslation(systemState.getId(), translateLang, systemState.getText());
                request.setAttribute("message",
                        "SystemState <b style=\"color:gray\"> " + systemState.getText()
                                + " </b> successfully  added");
                request.setAttribute("error", false);
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                request.setAttribute("message",
                        "Error occurs during adding systemState <b style=\"color:gray\">  "
                                + systemState.getText() + " </b> ");
                request.setAttribute("error", true);
            } catch (Exception ex) {
                logger.error("Error occurs during adding system state ", ex);
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



