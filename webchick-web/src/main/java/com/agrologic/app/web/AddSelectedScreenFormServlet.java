package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.ActionSetDaoImpl;
import com.agrologic.app.model.*;
import com.google.common.collect.Lists;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class AddSelectedScreenFormServlet extends AbstractServlet {

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
                Long programId = Long.parseLong(request.getParameter("programId"));
                Long selectedProgramId = Long.parseLong(request.getParameter("selectedProgramId"));
                Long selectedScreenId = Long.parseLong(request.getParameter("selectedScreenId"));

                try {
                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(selectedProgramId);
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    int nextScreenPos = screenDao.getNextScreenPosByProgramId(programId);

                    Screen screen = screenDao.getById(program.getId(), selectedScreenId);
                    screen.setProgramId(programId);
                    screen.setPosition(nextScreenPos);
                    screen.setDisplay("yes");
                    screenDao.insertExistScreen(screen);

                    if (screen.getTitle().equals("Action Set Buttons")) {
                        ActionSetDao actionSetDao = DbImplDecider.use(DaoType.MYSQL).getDao(ActionSetDaoImpl.class);
                        Collection<ActionSet> actionsetList = actionSetDao.getAll(program.getId());
                        actionSetDao.insertActionSetList(Lists.newArrayList(actionsetList), programId);

                    } else {
                        TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                        Collection<Table> screenTables = tableDao.getScreenTables(selectedProgramId, selectedScreenId, true);

                        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

                        for (Table t : screenTables) {
                            t.setProgramId(programId);
                            tableDao.insert(t);
                            List<Data> tableData = (List<Data>) dataDao.getTableDataList(selectedProgramId, selectedScreenId,
                                    t.getId(), null);

                            for (Data d : tableData) {
                                dataDao.insertDataToTable(programId, screen.getId(), t.getId(), d.getId(), "yes",
                                        d.getPosition());
                            }
                        }
                    }

                    logger.info("New screen and screens data successfully added !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request,
                            response);
                } catch (Exception e) {
                    logger.error("Error occurs while adding screen !", e);
                    request.setAttribute("message", "Error occurs during adding screen!");
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



