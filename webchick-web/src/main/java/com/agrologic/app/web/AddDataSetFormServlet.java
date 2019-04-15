package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Data;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AddDataSetFormServlet extends AbstractServlet {
    private DataDao dataDao;
    private long endDataId;
    private int pos;
    private long programid;
    private long screenid;
    private String show;
    private long startDataId;
    private long tableid;

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
            programid = Long.parseLong(request.getParameter("programId"));
            screenid = Long.parseLong(request.getParameter("screenId"));
            tableid = Long.parseLong(request.getParameter("tableId"));
            startDataId = Long.parseLong(request.getParameter("startdataId"));
            endDataId = Long.parseLong(request.getParameter("enddataId"));
            show = request.getParameter("show");
            pos = Integer.parseInt(request.getParameter("startpos"));
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    int type = (int) startDataId;        // type of value (like 4096)

                    if ((type & 0xC000) != 0xC000) {
                        startDataId = (type & 0xFFF);    // remove type to get an index 4096&0xFFF -> 0
                    } else {
                        startDataId = (type & 0xFFFF);
                    }
//                    if (startDataId < 0){
//                        startDataId = startDataId + 65536;
//                    }
//
//                    if (endDataId < 0){
//                        endDataId = endDataId + 65536;
//                    }
                    type = (int) endDataId;              // type of value (like 4096)

                    if ((type & 0xC000) != 0xC000) {
                        endDataId = (type & 0xFFF);      // remove type to get an index 4096&0xFFF -> 0
                    } else {
                        endDataId = (type & 0xFFFF);
                    }

                    if (startDataId > endDataId) {
                        return;
                    }

                    // stop counter to prevent add over data
                    long stopCount = endDataId - startDataId;

                      while ((startDataId <= endDataId) || (stopCount >= 0)) {

                        Data data = null;

                        try {
                            data = dataDao.getById(startDataId);
                        } catch (SQLException ex) {
                            data = new Data();
                            data.setFormat(0);
                        } catch (EmptyResultDataAccessException ex) {
                            startDataId++;
                            stopCount--;
                            continue;
                        }

                        try {
                            dataDao.insertDataToTable(programid, screenid, tableid, startDataId, show, pos);
                        } catch (SQLException ex) {
                            for (Throwable e : ex) {
                                if (e instanceof SQLException) {
                                    e.printStackTrace(System.err);
                                    logger.error("Error during inserting data : ", e);
                                    Throwable t = ex.getCause();

                                    while (t != null) {
                                        t = t.getCause();
                                    }
                                }
                            }
                        }

                        startDataId++;
                        stopCount--;
                        pos++;

                    }
                }
            });

            thread.start();
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
}



