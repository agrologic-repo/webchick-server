
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.*;
import com.agrologic.app.dao.impl.*;
import com.agrologic.app.model.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class RCMainScreenAjax extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static int count = 0;

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
        response.setContentType("text/xml;charset=UTF-8");

        PrintWriter out = response.getWriter();
        final Logger logger = Logger.getLogger(RCMainScreenAjax.class);

        try {
            long userId = Long.parseLong(request.getParameter("userId"));
            long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            long screenId = 1;
            String lang = (String) request.getSession().getAttribute("lang");

            if ((lang == null) || lang.equals("")) {
                lang = "en";
            }

//          Map<Long, Long> nextScrIdsByCntrl = new HashMap<Long, Long>();

            try {
                CellinkDao cellinkDao = new CellinkDaoImpl();
                CellinkDto cellink = cellinkDao.getById(cellinkId);

                if (cellink.getState() == CellinkState.STATE_ONLINE) {
                    cellink.setState(CellinkState.STATE_START);
                    cellinkDao.update(cellink);
                }

                // cellink.setTime(new Timestamp(System.currentTimeMillis()));
                cellinkDao.update(cellink);

                LanguageDao languageDao = new LanguageDaoImpl();
                long langId = languageDao.getLanguageId(lang);    // get language id

                // get all controllers connected to cellink
                ControllerDao controllerDao = new ControllerDaoImpl();
                List<ControllerDto> controllers = controllerDao.getAllActiveByCellinkId(cellinkId);

                request.getSession().setAttribute("controllers", controllers);

                // check if controllerdata table have any data
                if (!controllerDao.isDataReady(userId)) {
                    out.println("<status=1 />");
                    out.println("<table border='0' cellPadding='1' cellSpacing='1'>");

                    int column = 0;

                    for (ControllerDto controller : controllers) {
                        if ((column % ControllerDto.COLUMN_NUMBERS) == 0) {
                            out.println("<tr>");
                        }

                        out.println("<td valign='top'  style='min-width:200px;'>");
                        out.println("<table class=\"table-screens\" border=\"1\" borderColor=\"#848C96\"");
                        out.println(
                                "onmouseover=\"this.style.borderColor='orange';this.style.borderWidth='0.1cm';this.style.borderStyle='solid';\"");
                        out.println(
                                "onmouseout=\"this.style.borderColor='';this.style.borderWidth='';this.style.borderStyle='';\">");
                        out.println("<thead>");
                        out.println("<th colspan='2'>");
                        out.println("<img src=\"img/house.png\" border=0 hspace=5\">");
                        out.println("<br>");
                        out.println(controller.getTitle());
                        out.println("<p>");
                        out.println("<span style='font-size:12; color: tomato;'>");
                        out.println(request.getSession().getAttribute("page.loading"));
                        out.println("<img src='img/loading2.gif'>");
                        out.println("</span>");
                        out.println("</th>");
                        out.println("</thead>");
                        out.println("<tbody>");
                        out.println("</tbody>");
                        out.println("</table>");
                        column++;
                        out.println("</td>");

                        if ((column % ControllerDto.COLUMN_NUMBERS) == 0) {
                            out.println("</tr>");
                        }
                    }

                    out.println("</table>");
                    count++;
                } else {
                    out.println("<status=0 />");

                    final ProgramDao programDao = new ProgramDaoImpl();

                    // get program relays
                    final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);

                    // get program alarms
                    final ProgramAlarmDao programAlarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramAlarmDao.class);

                    // get program system states
                    final ProgramSystemStateDao programSystemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramSystemStateDao.class);

                    // get program screens
                    final ScreenDao screenDao = new ScreenDaoImpl();

                    // get program screen tables
                    final TableDao tableDao = new TableDaoImpl();
                    final DataDao dataDao = new DataDaoImpl();
                    int column = 0;

                    out.println("<table border='0' cellPadding='1' cellSpacing='1'>");

                    for (ControllerDto controller : controllers) {

                        // get asigned to controller program
                        ProgramDto program = programDao.getById(controller.getProgramId());

                        controller.setProgram(program);
                        program.setProgramRelays(programRelayDao.getAllProgramRelays(program.getId(), langId));
                        program.setProgramAlarms(programAlarmDao.getAllProgramAlarms(program.getId(), langId));
                        program.setProgramSystemStates(programSystemStateDao.getAllProgramSystemStates(program.getId(),
                                langId));

                        Long nextScreenID = screenDao.getSecondScreenAfterMain(program.getId());

                        // nextScrIdsByCntrl.put(controller.getId(), nsid);
                        ScreenDto screen = screenDao.getById(program.getId(), screenId, langId);

                        program.addScreen(screen);

                        List<TableDto> tables = tableDao.getScreenTables(program.getId(), screen.getId(), langId,
                                false);

                        screen.setTables(tables);

                        for (TableDto table : tables) {
                            if ((column % ControllerDto.COLUMN_NUMBERS) == 0) {
                                out.println("<tr>");
                            }

                            DataDto setClock = dataDao.getSetClockByController(controller.getId());

                            controller.setSetClock(setClock);

                            DataDto setDate = dataDao.getSetDateByController(controller.getId());

                            controller.setSetDate(setDate);

                            List<DataDto> list = dataDao.getOnlineTableDataList(program.getId(), controller.getId(),
                                    screen.getId(), table.getId(), langId);

                            // List<DataDto> list = dataDao.getOnlineTableDataList(program.getId(), controller.getId(), table.getId(), langId);
                            table.setDataList(list);
                            out.println("<td valign='top'  style='min-width:200px;'>");
                            out.println("<table class=\"table-screens\" border=\"1\" borderColor=\"#848C96\"");
                            out.println(
                                    "onmouseover=\"this.style.borderColor='orange';this.style.borderWidth='0.1cm';this.style.borderStyle='solid';\"");
                            out.println(
                                    "onmouseout=\"this.style.borderColor='';this.style.borderWidth='';this.style.borderStyle='';\">");
                            out.println("<thead>");
                            out.println("<th colspan='2' title=\""
                                    + request.getSession().getAttribute("label.program.version") + " - "
                                    + controller.getProgram().getName() + "\">");
                            out.println("<img src=\"img/house.png\" border=0 hspace=5\">");
                            out.println("<a href='./rmctrl-controller-screens-ajax.jsp?userId=" + userId
                                    + "&cellinkId=" + cellinkId + "&screenId=" + nextScreenID + "&controllerId="
                                    + controller.getId() + "&doResetTimeout=true'>");

                            if (isAlarmOnController(list) == true) {
                                out.println("<img src=\"img/alarm.gif\" border=0 hspace=5 title=\"Alarm in "
                                        + controller.getTitle() + " \">");
                            }

                            out.println(controller.getTitle());
                            out.println("</a>");
                            out.println("<br>");
                            out.println("<span style='font-size:12; color: tomato;'>");

                            if (list.size() > 0) {
                                try {
                                    String setclock = controller.getSetClock().getFormatedValue();

                                    out.println(request.getSession().getAttribute("label.controller.update.time")
                                            + " - ");
                                    out.println(setclock);
                                } catch (Exception ex) {
                                    out.println(request.getSession().getAttribute("label.controller.not.available"));
                                }
                            } else {
                                out.println("");
                                out.println(request.getSession().getAttribute("page.loading"));
                            }

                            out.println("</span>");
                            out.println("</th>");
                            out.println("</thead>");
                            out.println("<tbody>");

                            for (DataDto data : list) {
                                createDataTable(controller, screen, data, request, out);
                            }

                            out.println("</tbody>");
                            out.println("</table>");
                            column++;
                            out.println("</td>");

                            if ((column % ControllerDto.COLUMN_NUMBERS) == 0) {
                                out.println("</tr>");
                            }
                        }
                    }

                    out.println("</table>");
                }
            } catch (SQLException e) {
                logger.error("Database Error  : ", e);
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>");
                out.println(request.getSession().getAttribute("communication.error"));
                out.println("</td>");
                out.println("</tr>");
                out.println("</table>");
            }
        } finally {
            out.close();
        }
    }

    private List<ProgramRelay> getProgramRelaysByRelayType(List<ProgramRelay> dataRelays, Long relayType) {
        List<ProgramRelay> relayList = new ArrayList<ProgramRelay>();

        for (ProgramRelay pr : dataRelays) {
            if (pr.getDataId().equals(relayType)) {
                relayList.add(pr);
            }
        }

        return relayList;
    }

    private boolean isAlarmOnController(List<DataDto> onScreenData) {
        for (DataDto d : onScreenData) {
            if (d.getSpecial() == 1) {
                int mask = 0x0001;
                int val = (d.getValue().intValue());
                int on = val & mask;

                if (on == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    private void createDataTable(ControllerDto controller, ScreenDto screen, DataDto data, HttpServletRequest request,
                                 PrintWriter out) {
        if (!data.isStatus()) {
            out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
            out.println("<td class='label' nowrap>");
            out.println(data.getUnicodeLabel());
            out.println("</td>");
            out.println("<td class='value'>");

            if (!data.isReadonly()) {
                out.println(
                        "<input type='text' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeypress=\"keyPress(event, this, "
                                + controller.getId() + "," + data.getId()
                                + " );\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'"
                                + data.getFormat()
                                + "')\" size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;' value="
                                + data.getFormatedValue() + ">");
            } else {
                out.println(data.getFormatedValue());
            }

            out.println("</td>");
            out.println("</tr>");
        } else {
            int special = data.getSpecial();

            switch (special) {
                case DataDto.STATUS:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<td class='value'>");
                    out.println(data.getFormatedValue());
                    out.println("</td>");
                    out.println("</tr>");

                    break;

                case DataDto.ALARM:
                    List<ProgramAlarm> alarms = controller.getProgram().getProgramAlarmsByData(data.getId());
                    StringBuilder toolTip = new StringBuilder();

                    for (ProgramAlarm a : alarms) {
                        toolTip.append("<p>").append(a.getDigitNumber()).append(" - ").append(a.getText()).append("</p>");
                    }

                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<div id='helpBox" + controller.getId() + data.getId() + "'");
                    out.println(" style=\"background-color:#F0EFFF; ");
                    out.println("color: #0000FF; ");
                    out.println("overflow: hidden; ");
                    out.println("z-index:999; ");
                    out.println("position:absolute; ");
                    out.println("margin:auto; ");
                    out.println("width:250px; ");
                    out.println("height:250px; ");
                    out.println("padding-left:0px;");
                    out.println("border: 2px solid black;");
                    out.println("display:none;");
                    out.println("font-weight:bold;");
                    out.println("font-size:12px;\">");
                    out.println("<div style='padding:1px;margin: 0px 0px 0px 0px;'>");
                    out.println(data.getUnicodeLabel() + "&nbsp;&nbsp;&nbsp;");
                    out.println("<a style='padding-right:1px; cursor:hand;' href='javascript:HideHelp(\""
                            + controller.getId() + data.getId() + "\")'>");
                    out.println(request.getSession().getAttribute("button.close"));
                    out.println("X");
                    out.println("</a>");
                    out.println("</div>");
                    out.println("<div id='helpBoxInner" + controller.getId() + data.getId() + "'");
                    out.println("style=\"position: relative;");
                    out.println("color:Black;");
                    out.println("background-color:#ffffff;");
                    out.println("left: 0px;");
                    out.println("top: 0px;");
                    out.println("overflow: auto;");
                    out.println("width:250px;");
                    out.println("height:338px;");
                    out.println("border: 0px;");
                    out.println("padding: 5px;");
                    out.println("margin: 0px;");
                    out.println("font-size:11x;\">");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("<td class='value'>");
                    out.println(data.getFormatedValue());

//              out.println("<input type='text' readonly size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;' value=" + data.getFormatedValue() + ">");
                    out.println(
                            "<span class=\"formHelpLink\" style=\"color : #0000FF;font-weight: bold;font-size:1px;padding:0px 0px 0px 1px;margin:0px;cursor:help;\"");
                    out.println("valign='middle' align='left' onclick=\"ShowHelp(event, '" + toolTip.toString()
                            + "' , HLP_SHOW_POS_MOUSE ,200,350,'" + controller.getId() + data.getId() + "')\">");
                    out.println("<img src='img/help1.gif'>");
                    out.println("</span>");
                    out.println("</td>");
                    out.println("</tr>");

                    break;

                case DataDto.RELAY:
                    List<ProgramRelay> programRelays = controller.getProgram().getProgramRelays();
                    List<ProgramRelay> relayList = getProgramRelaysByRelayType(programRelays, data.getId());

                    if (relayList.size() > 0) {
                        for (ProgramRelay relay : relayList) {
                            if (relay.getRelayNumber() != 0) {
                                out.println(
                                        "<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                                out.println("<td class=\"label\" nowrap>");
                                out.println(relay.getUnicodeText());
                                out.println("</td>");
                                out.println(
                                        "<td class='' align=\"center\" nowrap colspan=2 style=\"height:14pt;color:green;font-size:10pt;font-weight: bold;\" onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                                relay.init(data.getValue());

                                if (relay.getText().startsWith("Fan") || relay.getText().startsWith("Mixer")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/fan-on.gif'>");
                                    } else {
                                        out.println("<img src='img/fan-off.gif'>");
                                    }
                                } else if (relay.getText().startsWith("Light")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/light-on.gif'>");
                                    } else {
                                        out.println("<img src='img/light-off.png'>");
                                    }
                                } else if (relay.getText().contains("Cool")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/coolon.gif'>");
                                    } else {
                                        out.println("<img src='img/cooloff.gif'>");
                                    }
                                } else if (relay.getText().contains("Heater")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/heateron.gif'>");
                                    } else {
                                        out.println("<img src='img/heateroff.gif'>");
                                    }
                                } else if (relay.getText().contains("Feed")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/aougeron.gif'>");
                                    } else {
                                        out.println("<img src='img/aougeroff.gif'>");
                                    }
                                } else if (relay.getText().contains("Water")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/wateron.gif'>");
                                    } else {
                                        out.println("<img src='img/wateroff.gif'>");
                                    }
                                } else if (relay.getText().contains("Ignition")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/sparkon.gif'>");
                                    } else {
                                        out.println("<img src='img/sparkoff.gif'>");
                                    }
                                } else {
                                    if (relay.isOn()) {
                                        out.println("<img src='img/relayon.gif'>");
                                    } else {
                                        out.println("<img src='img/relayoff.png'>");
                                    }
                                }

                                out.println("</td>");
                                out.println("</tr>");
                            }
                        }
                    }

                    break;

                case DataDto.SYSTEM_STATE:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");

                    ProgramSystemState programSystemState =
                            controller.getProgram().getSystemStateByNumber(data.getValue());

                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println(
                            "<td class='value' align='center' nowrap style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
                    out.println(programSystemState.getText());
                    out.println("</td>");
                    out.println("</tr >");

                    break;
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



