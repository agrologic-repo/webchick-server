package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.model.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RCControllerScreenAjax extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        /**
         * Logger for this class and subclasses
         */
        final Logger logger = Logger.getLogger(RCScreensServlet.class);

        response.setContentType("text/xml;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            long userId = Long.parseLong(request.getParameter("userId"));
            long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            long controllerId = Long.parseLong(request.getParameter("controllerId"));
            long screenId = Long.parseLong(request.getParameter("screenId"));
            String lang = (String) request.getSession().getAttribute("lang");

            if ((lang == null) || lang.equals("")) {
                lang = "en";
            }

            try {
                CellinkDao cellinkDao = new CellinkDaoImpl();
                CellinkDto cellink = cellinkDao.getById(cellinkId);

                if (cellink.getState() == CellinkState.STATE_ONLINE) {
                    cellink.setState(CellinkState.STATE_START);
                    cellinkDao.update(cellink);
                }

                cellinkDao.update(cellink);

                LanguageDao languageDao = new LanguageDaoImpl();
                long langId = languageDao.getLanguageId(lang);
                ControllerDao controllerDao = new ControllerDaoImpl();
                List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellinkId);
                ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);
                ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                ;
                TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

                for (ControllerDto controller : controllers) {
                    if (controller.getId() == controllerId) {
                        Program program = programDao.getById(controller.getProgramId());
                        List<ProgramRelay> programRelays = programRelayDao.getAllProgramRelays(program.getId(), langId);
                        program.setProgramRelays(programRelays);

                        List<Screen> screens = (List<Screen>) screenDao.getAllScreensByProgramAndLang(program.getId(),
                                langId, false);
                        for (Screen screen : screens) {
                            if (screen.getId() == screenId) {
                                Collection<Table> tables = tableDao.getScreenTables(program.getId(), screen.getId(),
                                        langId, false);

                                for (Table table : tables) {
                                    Collection<Data> dataList = dataDao.getOnlineTableDataList(controller.getId(), program.getId(),
                                            screen.getId(), table.getId(), langId);

                                    table.setDataList(dataList);
                                }

                                screen.setTables(tables);
                            }
                        }

                        program.setScreens(screens);
                        controller.setProgram(program);
                    } else {
                        continue;
                    }
                }

                ControllerDto controller = findController(controllerId, controllers);

                out.println();
                out.println("<table style=\"font-size:90%;\" width=\"100%\" border=\"0\">");
                out.println("<tr>");
                out.println("<td align=center valign=top>");
                out.println("<table border=0  width=100%  id=topnav>");
                out.println("<tr>");

                int col = 0;
                final long MAIN_SCREEN = 1;

                for (Screen screen : controller.getProgram().getScreens()) {
                    if ((col % 8) == 0) {
                        out.println("</tr>");
                        out.println("<tr>");
                    }

                    col++;

                    String cssClass = "";

                    if (screen.getId() == screenId) {
                        cssClass = "active";
                    } else {
                        cssClass = "";
                    }

                    if (screen.getId() == MAIN_SCREEN) {
                        out.println("<td nowrap align=center>");
                        out.println("<a class='" + cssClass + "' href='./rmctrl-main-screen-ajax.jsp?lang=" + lang
                                + "&userId=" + userId + "&cellinkId=" + controller.getCellinkId() + "&screenId="
                                + MAIN_SCREEN + "&doResetTimeout=true' id=" + screen.getId() + " >");
                        out.println(screen.getUnicodeTitle());
                        out.println("</a>");
                        out.println("</td>");
                    } else if (screen.getTitle().equals("Graphs")) {
                        out.println("<td nowrap align=center>");
                        out.println("<a class='" + cssClass + "' href='./rmtctrl-graph.html?lang=" + lang + "&userId="
                                + userId + "&cellinkId=" + controller.getCellinkId() + "&controllerId="
                                + controller.getId() + "&programId=" + controller.getProgram().getId()
                                + "&screenId=" + screen.getId() + "&doResetTimeout=true' id=" + screen.getId()
                                + " onclick='document.body.style.cursor=wait'>");
                        out.println(screen.getUnicodeTitle());
                        out.println("</a>");
                        out.println("</td>");
                    } else if (screen.getTitle().equals("Action Set Buttons")) {
                        out.println("<td nowrap>");
                        out.println("<a class='" + cssClass + "' href='./rmtctrl-actionset.html?lang=" + lang
                                + "&userId=" + userId + "&cellinkId=" + controller.getCellinkId()
                                + "&controllerId=" + controller.getId() + "&programId="
                                + controller.getProgram().getId() + "&screenId=" + screen.getId()
                                + "&doResetTimeout=true' id=" + screen.getId()
                                + " onclick='document.body.style.cursor=wait'>");
                        out.println(screen.getUnicodeTitle());
                        out.println("</a>");
                        out.println("</td>");
                    } else {
                        out.println("<td nowrap align=\"center\">");
                        out.println("<a class='" + cssClass + "' href='./rmctrl-controller-screens-ajax.jsp?lang="
                                + lang + "&userId=" + userId + "&cellinkId=" + controller.getCellinkId()
                                + "&controllerId=" + controller.getId() + "&programId="
                                + controller.getProgram().getId() + "&screenId=" + screen.getId()
                                + "&doResetTimeout=true' onclick='document.body.style.cursor=wait'>");
                        out.println(screen.getUnicodeTitle());
                        out.println("</a>");
                        out.println("</td>");
                    }
                }

                out.println("</tr>");
                out.println("</table>");
                out.println("</td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<table cellPadding=2 cellSpacing=2 align=center>");

                int column = 0;
                Screen currScreen = controller.getProgram().getScreenById(screenId);
                Collection<Table> tables = currScreen.getTables();

                if (tables.size() > 0) {
                    for (Table table : tables) {
                        Long tableId = table.getId();

                        if ((column % Screen.COLUMN_NUMBERS) == 0) {
                            out.println("<tr>");
                        }

                        out.println("<td valign=top colspan=8>");
                        out.println("<table class=\"table-screens\" border=\"1\" borderColor=\"#848C96\"");
                        out.println(
                                "onmouseover=\"this.style.borderColor='orange';this.style.borderWidth='0.1cm';this.style.borderStyle='solid';\"");
                        out.println(
                                "onmouseout=\"this.style.borderColor='';this.style.borderWidth='';this.style.borderStyle='';\">");
                        out.println("<thead>");
                        out.println("<th nowrap colspan=2>");
                        out.println(table.getUnicodeTitle());
                        out.println("</th>");
                        out.println("</thead>");
                        out.println("<tbody>");

                        for (Data data : table.getDataList()) {
                            if (data.isStatus()) {
                                int special = data.getSpecial();

                                switch (special) {
                                    case 0:
                                        out.println(
                                                "<tr class=unselected onmouseover=this.className='selected' onmouseout=this.className='unselected'>");
                                        out.println("<td class='label'>");
                                        out.println(data.getUnicodeLabel());
                                        out.println("</td>");
                                        out.println("<td class='value' nowrap>");
                                        out.println(
                                                "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
                                                        + data.getFormattedValue() + "'>");
                                        out.println("</td>");
                                        out.println("</tr>");

                                        break;

                                    default:
                                        out.println(
                                                "<tr class=unselected onmouseover=this.className='selected' onmouseout=this.className='unselected'>");
                                        out.println("<td class='label' nowrap>");
                                        out.println(data.getUnicodeLabel());
                                        out.println("</td>");
                                        out.println("<td class='value' nowrap>");
                                        out.println(
                                                "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
                                                        + data.getFormattedValue() + "'>");
                                        out.println("</td>");
                                        out.println("</tr>");

                                        break;
                                }
                            } else {
                                out.println(
                                        "<tr class=unselected onmouseover=this.className='selected' onmouseout=this.className='unselected'>");
                                out.println(
                                        "<td class='label' nowrap onmouseover=this.className='tdselected' onmouseout=this.className='label'>");
                                out.println(data.getUnicodeLabel());
                                out.println("</td>");
                                out.println("<td class='value'>");

                                if (!data.isReadonly()) {
                                    out.println(
                                            "<input type='text' dir='ltr' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeypress=\"keyPress(event, this, "
                                                    + controller.getId() + "," + currScreen.getId() + "," + data.getId()
                                                    + " );\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'"
                                                    + data.getFormat()
                                                    + "')\" size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;' value="
                                                    + data.getFormattedValue() + ">");
                                } else {
                                    out.println(
                                            "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
                                                    + data.getFormattedValue() + "'>");
                                }

                                out.println("</td>");
                                out.println("</tr>");
                            }
                        }

                        out.println("</tbody>");
                        out.println("</table>");
                        out.println("</td>");
                        column++;

                        if ((column % Screen.COLUMN_NUMBERS) == 0) {
                            out.println("</tr>");
                        }
                    }
                }

                out.println("</table>");
            } catch (SQLException ex) {

                // error page
                logger.info("retrieve program data relay!", ex);
                request.getRequestDispatcher("./rmctrl-controller-screens.jsp?userId" + userId + "&cellinkId="
                        + cellinkId + "&screenId=" + screenId).forward(request, response);
            } catch (Exception ex) {

                // error page
                logger.info("retrieve program data relay!", ex);
                request.getRequestDispatcher("./rmctrl-controller-screens.jsp?userId" + userId + "&cellinkId="
                        + cellinkId + "&screenId=" + screenId).forward(request, response);
            }
        } finally {
            out.close();
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

    private ControllerDto findController(long controllerId, List<ControllerDto> controllers) {
        Iterator iter = controllers.iterator();

        while (iter.hasNext()) {
            ControllerDto c = (ControllerDto) iter.next();

            if (c.getId() == controllerId) {
                return c;
            }
        }

        return null;
    }
}



