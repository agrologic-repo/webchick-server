package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.RelayDao;
import com.agrologic.app.model.Relay;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AddRelayFormServlet extends HttpServlet {

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

        final Logger logger = Logger.getLogger(AddRelayFormServlet.class);
        PrintWriter out = response.getWriter();

        try {
            Long relayId = Long.parseLong(request.getParameter("NrelayId"));
            String relayText = request.getParameter("NrelayText");
            Long translateLang = Long.parseLong(request.getParameter("translateLang"));
            Relay relay = new Relay();

            relay.setId(relayId);
            relay.setText(relayText);

            RelayDao relayDao = DbImplDecider.use(DaoType.MYSQL).getDao(RelayDao.class);

            try {
                relayDao.insert(relay);
                logger.info("Relay " + relay.getText() + "  successfully added");
                relayDao.insertTranslation(relay.getId(), translateLang, relay.getText());
                request.getSession().setAttribute(
                        "message", "Relay <b style=\"color:gray\"> " + relay.getText() + " </b> successfully  added");
                request.getSession().setAttribute("error", false);
            } catch (SQLException ex) {
                logger.error(ex.getMessage() + relay.getText(), ex);
                request.getSession().setAttribute(
                        "message", "Error occurs during adding relay <b style=\"color:gray\">" + relay.getText() + "</b>");
                request.getSession().setAttribute("error", true);
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



