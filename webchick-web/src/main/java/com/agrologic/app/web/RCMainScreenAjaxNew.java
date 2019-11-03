package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class RCMainScreenAjaxNew extends AbstractServlet {
    private HashMap<Long,Boolean> alarmMap;

    public RCMainScreenAjaxNew() {
        super();
//        alarmMap = new HashMap<Long, Boolean>();
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        alarmMap = new HashMap<Long, Boolean>();// to fix bug in alarms

        try {

            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long userId = Long.parseLong(request.getParameter("userId"));
                long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
                long screenId = 1;
                long langId = getInSessionLanguageId(request);

                try {
                    CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                    Cellink cellink = cellinkDao.getById(cellinkId);

                    if (cellink.getState() == CellinkState.STATE_ONLINE) {
                        cellink.setState(CellinkState.STATE_START);
                        cellink.setTime(new Timestamp(System.currentTimeMillis()));
                        cellinkDao.update(cellink);
                    } else {
                        cellink.setTime(new Timestamp(System.currentTimeMillis()));
                        cellinkDao.update(cellink);
                    }


                    // get all controllers connected to cellink
                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                    Collection<Controller> controllers = controllerDao.getActiveCellinkControllers(cellinkId);
                    request.getSession().setAttribute("controllers", controllers);

                    // check if controllerdata table have any data
                    out.println("<status=0 />");

                    final ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);

                    // get program relays
                    final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);

                    // get program alarms
                    final ProgramAlarmDao programAlarmDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramAlarmDao.class);

                    // get program system states
                    final ProgramSystemStateDao programSystemStateDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramSystemStateDao.class);

                    // get program screens
                    final ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    // get program screen tables
                    final TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                    final DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    int column = 0;

                    out.println("<table border='0' cellPadding='1' cellSpacing='1'>");

                    for (Controller controller : controllers) {

                        // get assigned to controller program
                        Program program = programDao.getById(controller.getProgramId());
                        controller.setProgram(program);

                        program.setProgramRelays(programRelayDao.getAllProgramRelays(program.getId(), langId));
                        program.setProgramAlarms(programAlarmDao.getAllProgramAlarms(program.getId(), langId));
                        program.setProgramSystemStates(programSystemStateDao.getAllProgramSystemStates(program.getId(), langId));

                        Long nextScreenID = screenDao.getSecondScreenAfterMain(program.getId());
                        Screen screen = screenDao.getById(program.getId(), screenId, langId);
                        program.addScreen(screen);

                        if (screen != null && nextScreenID != null) {
                            Collection<Table> tables = tableDao.getScreenTables(program.getId(), screen.getId(), langId, false);
                        screen.setTables(tables);

                        for (Table table : tables) {
                            if ((column % Controller.COLUMN_NUMBERS) == 0) {
                                out.println("<tr>");
                            }

                            // set clock
                            Data setClock = dataDao.getSetClockByController(controller.getId());
                            if ((setClock == null)) {
                                setClock = dataDao.getById(1309L);
                                setClock.setValue(0L);
                            }
                            controller.setSetClock(setClock);

                            // set controller data of main screen
                            Collection<Data> controllerDataList = dataDao.getOnlineTableDataList(controller.getId(), program.getId(), screen.getId(), table.getId(), langId);


                            table.setDataList(controllerDataList);

                            out.println("<td valign='top'  style='min-width:200px;'>");
                            out.println("<table class=\"table-screens\" border=\"1\" borderColor=\"#848C96\"");
                            out.println("onmouseover=\"this.style.borderColor='orange';this.style.borderWidth='0.1cm';this.style.borderStyle='solid';\"");
                            out.println("onmouseout=\"this.style.borderColor='';this.style.borderWidth='';this.style.borderStyle='';\">");
                            out.println("<thead>");
                            out.println("<th colspan='2' title=\"" + request.getSession().getAttribute("label.program.version") + " - " + controller.getProgram().getName() + "\">");

                            User user = (User) request.getSession().getAttribute("user");
                            if (user.getRole() == UserRole.ADMIN) {
                                out.println("<img src=\"resources/images/clear.gif\" border=0 onMouseOver=\"this.style.cursor='pointer'\" "
                                        + " click to delete controller data "
                                        + " onclick=\"window.document.location=\'./clear-controller-data.html?userId="
                                        + +userId + "&cellinkId="
                                        + cellinkId + "&controllerId="
                                        + controller.getId() + "\'\"/>");
                            }

                            out.println("<img src=\"resources/images/house.png\" border=0 hspace=5\">");

                            if (nextScreenID != null) {
                                out.println("<a href='./rmctrl-controller-screens-ajax.html?userId=" + userId + "&cellinkId=" + cellinkId + "&screenId=" + nextScreenID + "&controllerId=" + controller.getId() + "'>");
                            } else {
                                out.println("<a href='./rmctrl-main-screen.html?userId=" + userId + "&cellinkId=" + cellinkId + "'>");
                            }

                            if (isAlarmOnController(controllerDataList) == true) {
                                out.println("<img src=\"resources/images/alarm.gif\" border=0 hspace=5 title=\"Alarm in " + controller.getTitle() + " \">");
                            }

                            out.println(controller.getTitle());
                            out.println("</a>");
                            out.println("<br>");
                            out.println("<span class='last-updated-label'>");

                            if (controllerDataList.size() > 0) {
                                if (controller.getSetClock().getValue() == -1) {
                                    out.println(request.getSession().getAttribute("page.loading"));
                                    out.println("<img src='resources/images/loading2.gif'>");
                                } else {
                                    try {
                                        String setclock = controller.getSetClock().getFormattedValue();
                                        out.println(request.getSession().getAttribute("label.controller.update.time") + " - ");
                                        out.println(setclock);
                                    } catch (Exception ex) {
                                        out.println(request.getSession().getAttribute("label.controller.not.available"));
                                    }
                                }
                            } else {
                                out.println(request.getSession().getAttribute("page.loading"));
                                out.println("<img src='resources/images/loading2.gif'>");
                            }

                            out.println("</span>");
                            out.println("</th>");
                            out.println("</thead>");
                            out.println("<tbody>");

                            for (Data data : controllerDataList) {
                                createDataTable(controller, data, request, out, user);
                            }

                            out.println("<tr>" +
                                    "<td class='label' nowrap>" +
                                    "Click to send</td>" +
                                    "<td>" +
                                    "<center><input type=\"submit\" value=\"Send Data\"/></center>" +
                                    "</td>" +
                                    "</tr>");

                            out.println("</tbody>");
                            out.println("</table>");
                            out.println("</td>");
                            column++;

                            if ((column % Controller.COLUMN_NUMBERS) == 0) {
                                out.println("</tr>");
                            }
                        }
                    }
                }
                    out.println("</table>");
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

    private boolean isAlarmOnController(Collection<Data> onScreenData) {

        for(Map.Entry<Long,Boolean> entry:alarmMap.entrySet()) {
            if(entry.getKey() == 3154 || entry.getKey() == 3170 || entry.getKey() == 3708) {
                entry.setValue(false);
            }
        }

        for (Data d : onScreenData) {
            if (d.isAlarmOn()) {
                alarmMap.put(d.getId(), true);
            } else {
                alarmMap.put(d.getId(), false);
            }
        }

        boolean result = false;
        for(Map.Entry<Long,Boolean> entry:alarmMap.entrySet()) {
            if(entry.getValue()== true) {
                result = true;
            }
        }
        return result;
    }

    private Boolean isDigitIsAlarmOnController(Integer digit, Data data) {
        Boolean result = false;
        Integer value = data.getValue().intValue();

        while ((value / 10) > 0) {
            if (digit.equals(value % 10)) {
                result = true;
            }
            value = value / 10;
        }
        if (digit.equals(value % 10)) {
            result = true;
        }

        return result;
    }

    private void createDataTable(Controller controller, Data data, HttpServletRequest request, PrintWriter out, User user) {
        if (!data.isStatus()) {// status 0
            out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
            out.println("<td class='label' nowrap>");
            out.println(data.getUnicodeLabel());
            out.println("</td>");
            out.println("<td class='value'>");
            if (!data.isReadonly()) {
                request.getSession().setAttribute("userId", request.getParameter("userId"));
                request.getSession().setAttribute("cellinkId", request.getParameter("cellinkId"));
                out.println("<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' dir='ltr' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'"
                        + data.getFormat()
                        + "')\" size='6' style='height:14pt;color:green;background:lightgoldenrodyellow;font-size:10pt;font-weight: bold; vertical-align: middle;' value="
                        + data.getFormattedValue() + ">");
            } else {
                out.println(
                        "<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6'"
                                + " style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
                                + data.getFormattedValue() + "'>");
            }
            out.println("</td>");
            out.println("</tr>");
        } else {
            int special = data.getSpecial();
            switch (special) {
                case Data.STATUS:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<td class='value'>");
                    out.println(
                            "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6' "
                                    + "style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
                                    + data.getFormattedValue() + "'>");
                    out.println("</td>");
                    out.println("</tr>");
                    break;

                case Data.ALARM:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<td class='value'>");
                    out.println("<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0'" +
                            " size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; " +
                            " vertical-align: middle;border:0;' value='" + data.getFormattedValue() + "'>");
                    out.println("<img src='resources/images/help.gif' onClick=\"openPopup('" + controller.getId() + data.getId() + "',event);\">");
                    out.println("<div id=" + controller.getId() + data.getId() + " class=\"popup\" style=\"display:none;\">");
                    out.println(data.getUnicodeLabel());
                    out.println("<p class=\"cancel\" onclick=\"closePopup();\">X</p>");
                    out.println("</br>");
                    List<ProgramAlarm> alarms = controller.getProgram().getProgramAlarmsByData(data.getId());
                    StringBuilder toolTip = new StringBuilder();
                    for (ProgramAlarm a : alarms) {
                        if (isDigitIsAlarmOnController(a.getDigitNumber(), data)) {
                            toolTip.append("<p class='alarm-on'>").append(a.getDigitNumber()).append(" - ").append(a.getText()).append("</p>");
                        } else {
                            toolTip.append("<p class='alarm-off'>").append(a.getDigitNumber()).append(" - ").append(a.getText()).append("</p>");
                        }
                    }
                    out.println(toolTip.toString());
                    out.println("</div>");
                    out.println("</td>");
                    out.println("</tr>");

                    break;

                case Data.RELAY:
                    List<ProgramRelay> programRelays = controller.getProgram().getProgramRelays();
                    List<ProgramRelay> relayList = getProgramRelaysByRelayType(programRelays, data.getId());

                    if (relayList.size() > 0) {

                        for (ProgramRelay relay : relayList) {
                            if (relay.getRelayNumber() != 0) {
                                out.println(
                                        "<tr class='' onmouseover=\"this.className='selected'\" "
                                                + "onmouseout=\"this.className=''\">");

                                out.println("<td class=\"label\" nowrap>");
                                out.println(relay.getUnicodeText());
                                out.println("</td>");
                                out.println("<td class='' align=\"center\" nowrap colspan=2 style=\"height:14pt;color:green;font-size:10pt;font-weight: bold;\" onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");

                                if (data.getValue() == -1) {
                                    relay.setOff();
                                } else {
                                    relay.init(data.getValue());
                                }

                                if (relay.getText().startsWith("Fan") || relay.getText().startsWith("Mixer")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/fan-on.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/fan-off.gif'>");
                                    }
                                } else if (relay.getText().startsWith("Light")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/light-on.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/light-off.png'>");
                                    }
                                } else if (relay.getText().contains("Cool")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/coolon.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/cooloff.gif'>");
                                    }
                                } else if (relay.getText().contains("Heater")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/heateron.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/heateroff.gif'>");
                                    }
                                } else if (relay.getText().contains("Feed")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/aougeron.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/aougeroff.gif'>");
                                    }
                                } else if (relay.getText().contains("Water")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/wateron.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/wateroff.gif'>");
                                    }
                                } else if (relay.getText().contains("Ignition")) {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/sparkon.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/sparkoff.gif'>");
                                    }
                                } else {
                                    if (relay.isOn()) {
                                        out.println("<img src='resources/images/relayon.gif'>");
                                    } else {
                                        out.println("<img src='resources/images/relayoff.png'>");
                                    }
                                }
                                out.println("</td>");
                                out.println("</tr>");
                            }
                        }
                    }

                    break;

                case Data.SYSTEM_STATE:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    ProgramSystemState programSystemState = controller.getProgram().getSystemStateByNumber(data.getValue());
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<td class='value' align='center' nowrap "
                            + "style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
                    out.println(programSystemState.getText());
                    out.println("</td>");
                    out.println("</tr >");
                    break;

                case Data.STATUS_STRINGS:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    List <ProgramSystemState> listOfStates = controller.getProgram().getProgramSystemStateByData(data.getId());
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<td class='value' align='center' nowrap "
                             + "style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
                    out.println(listOfStates.get(data.getValue().intValue()).getText());
                    out.println("</td>");
                    out.println("</tr >");
                    break;

                default:
                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
                    out.println("<td class='label' nowrap>");
                    out.println(data.getUnicodeLabel());
                    out.println("</td>");
                    out.println("<td class='value'>");

                    if (!data.isReadonly()) {
                        out.println("<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' dir='ltr' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'"
                                + data.getFormat()
                                + "')\" size='6' style='height:14pt;color:green;background:lightgoldenrodyellow;font-size:10pt;font-weight: bold; vertical-align: middle;' value="
                                + data.getFormattedValue() + ">");
                    } else {
                        out.println(
                                "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='8'"
                                        + " style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
                                        + data.getFormattedValue() + "'>");
                    }

                    out.println("</td>");
                    out.println("</tr>");
                    break;
            }
        }
    }

//    private void createDataTable(Controller controller, Data data, HttpServletRequest request, PrintWriter out, User user) {
//        if (!data.isStatus()) {
//            out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//            out.println("<td class='label' nowrap>");
//            out.println(data.getUnicodeLabel());
//            out.println("</td>");
//            out.println("<td class='value'>");
//            if (!data.isReadonly()) {
//                if (user.getRole() == UserRole.READONLYUSER) {
//                    out.println("<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6'"
//                                    + " style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
//                                    + data.getFormattedValue() + "'>");
//                } else {
//                    request.getSession().setAttribute("userId", request.getParameter("userId"));
//                    request.getSession().setAttribute("cellinkId", request.getParameter("cellinkId"));
//                    out.println("<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' dir='ltr' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'"
//                            + data.getFormat()
//                            + "')\" size='6' style='height:14pt;color:green;background:lightgoldenrodyellow;font-size:10pt;font-weight: bold; vertical-align: middle;' value="
//                            + data.getFormattedValue() + ">");
//                }
//            } else {
//                out.println(
//                        "<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6'"
//                                + " style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
//                                + data.getFormattedValue() + "'>");
//            }
//            out.println("</td>");
//            out.println("</tr>");
//        } else {
//            int special = data.getSpecial();
//            switch (special) {
//                case Data.STATUS:
//                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//                    out.println("<td class='label' nowrap>");
//                    out.println(data.getUnicodeLabel());
//                    out.println("</td>");
//                    out.println("<td class='value'>");
//                    out.println(
//                            "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6' "
//                                    + "style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
//                                    + data.getFormattedValue() + "'>");
//                    out.println("</td>");
//                    out.println("</tr>");
//
//                    break;
//
//                case Data.ALARM:
//                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//                    out.println("<td class='label' nowrap>");
//                    out.println(data.getUnicodeLabel());
//                    out.println("</td>");
//                    out.println("<td class='value'>");
//                    out.println("<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0'" +
//                            " size='6' style='height:14pt;color:green;font-size:10pt;font-weight: bold; " +
//                            " vertical-align: middle;border:0;' value='" + data.getFormattedValue() + "'>");
//                    out.println("<img src='resources/images/help.gif' onClick=\"openPopup('" + controller.getId() + data.getId() + "',event);\">");
//                    out.println("<div id=" + controller.getId() + data.getId() + " class=\"popup\" style=\"display:none;\">");
//                    out.println(data.getUnicodeLabel());
//                    out.println("<p class=\"cancel\" onclick=\"closePopup();\">X</p>");
//                    out.println("</br>");
//                    List<ProgramAlarm> alarms = controller.getProgram().getProgramAlarmsByData(data.getId());
//                    StringBuilder toolTip = new StringBuilder();
//                    for (ProgramAlarm a : alarms) {
//                        if (isDigitIsAlarmOnController(a.getDigitNumber(), data)) {
//                            toolTip.append("<p class='alarm-on'>").append(a.getDigitNumber()).append(" - ").append(a.getText()).append("</p>");
//                        } else {
//                            toolTip.append("<p class='alarm-off'>").append(a.getDigitNumber()).append(" - ").append(a.getText()).append("</p>");
//                        }
//                    }
//                    out.println(toolTip.toString());
//                    out.println("</div>");
//                    out.println("</td>");
//                    out.println("</tr>");
//
//                    break;
//
//                case Data.RELAY:
//                    List<ProgramRelay> programRelays = controller.getProgram().getProgramRelays();
//                    List<ProgramRelay> relayList = getProgramRelaysByRelayType(programRelays, data.getId());
//
//                    if (relayList.size() > 0) {
//
//                        for (ProgramRelay relay : relayList) {
//                            if (relay.getRelayNumber() != 0) {
//                                out.println(
//                                        "<tr class='' onmouseover=\"this.className='selected'\" "
//                                                + "onmouseout=\"this.className=''\">");
//
//                                out.println("<td class=\"label\" nowrap>");
//                                out.println(relay.getUnicodeText());
//                                out.println("</td>");
//                                out.println("<td class='' align=\"center\" nowrap colspan=2 style=\"height:14pt;color:green;font-size:10pt;font-weight: bold;\" onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//
//                                if (data.getValue() == -1) {
//                                    relay.setOff();
//                                } else {
//                                    relay.init(data.getValue());
//                                }
//
//                                if (relay.getText().startsWith("Fan") || relay.getText().startsWith("Mixer")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/fan-on.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/fan-off.gif'>");
//                                    }
//                                } else if (relay.getText().startsWith("Light")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/light-on.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/light-off.png'>");
//                                    }
//                                } else if (relay.getText().contains("Cool")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/coolon.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/cooloff.gif'>");
//                                    }
//                                } else if (relay.getText().contains("Heater")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/heateron.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/heateroff.gif'>");
//                                    }
//                                } else if (relay.getText().contains("Feed")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/aougeron.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/aougeroff.gif'>");
//                                    }
//                                } else if (relay.getText().contains("Water")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/wateron.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/wateroff.gif'>");
//                                    }
//                                } else if (relay.getText().contains("Ignition")) {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/sparkon.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/sparkoff.gif'>");
//                                    }
//                                } else {
//                                    if (relay.isOn()) {
//                                        out.println("<img src='resources/images/relayon.gif'>");
//                                    } else {
//                                        out.println("<img src='resources/images/relayoff.png'>");
//                                    }
//                                }
//                                out.println("</td>");
//                                out.println("</tr>");
//                            }
//                        }
//                    }
//
//                    break;
//
//                case Data.SYSTEM_STATE:
//                    switch (data.getId().intValue()){
//                        case 3001:
//                            out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
////                        ProgramSystemState programSystemState = controller.getProgram().getSystemStateByNumber(data.getValue());
//                            List <ProgramSystemState> listProgramSystemState = controller.getProgram().getProgramSystemStateByData(data.getId());
//                            out.println("<td class='label' nowrap>");
//                            out.println(data.getUnicodeLabel());
//                            out.println("</td>");
//                            out.println("<td class='value' align='center' nowrap "
//                                    + "style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
////                        out.println(programSystemState.getText());
//                            out.println(listProgramSystemState.get(data.getValue().intValue()).getText());
//                            out.println("</td>");
//                            out.println("</tr >");
//                            break;
//                        default:
//                            out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//                            ProgramSystemState programSystemState = controller.getProgram().getSystemStateByNumber(data.getValue());
//                            out.println("<td class='label' nowrap>");
//                            out.println(data.getUnicodeLabel());
//                            out.println("</td>");
//                            out.println("<td class='value' align='center' nowrap "
//                                    + "style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
//                            out.println(programSystemState.getText());
//                            out.println("</td>");
//                            out.println("</tr >");
//                            break;
//
//                    }
////                    if (data.getId() == 3001){
////                        out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//////                        ProgramSystemState programSystemState = controller.getProgram().getSystemStateByNumber(data.getValue());
////                        List <ProgramSystemState> listProgramSystemState = controller.getProgram().getProgramSystemStateByData(data.getId());
////                        out.println("<td class='label' nowrap>");
////                        out.println(data.getUnicodeLabel());
////                        out.println("</td>");
////                        out.println("<td class='value' align='center' nowrap "
////                                + "style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
//////                        out.println(programSystemState.getText());
////                        out.println(listProgramSystemState.get(data.getValue().intValue()).getText());
////                        out.println("</td>");
////                        out.println("</tr >");
////                    } else {
////                        out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
////                        ProgramSystemState programSystemState = controller.getProgram().getSystemStateByNumber(data.getValue());
////                        out.println("<td class='label' nowrap>");
////                        out.println(data.getUnicodeLabel());
////                        out.println("</td>");
////                        out.println("<td class='value' align='center' nowrap "
////                                + "style='height:14pt;color:green;font-size:10pt;font-weight: bold;' colspan=2>");
////                        out.println(programSystemState.getText());
////                        out.println("</td>");
////                        out.println("</tr >");
////                    }
//                    break;
//                default:
//                    out.println("<tr class='' onmouseover=\"this.className='selected'\" onmouseout=\"this.className=''\">");
//                    out.println("<td class='label' nowrap>");
//                    out.println(data.getUnicodeLabel());
//                    out.println("</td>");
//                    out.println("<td class='value'>");
//
//                    if (!data.isReadonly()) {
//                        if (user.getRole() == UserRole.READONLYUSER) {
//                            out.println("<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='6'"
//                                    + " style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
//                                    + data.getFormattedValue() + "'>");
//                        } else {
////                            out.println(
////                                    "<input type='text' name='test' dir='ltr' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'" + data.getFormat()
////                                            + "')\" size='8' style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;' value=" + data.getFormattedValue() + ">");
//                            out.println("<input name='"+controller.getId()+",1,"+data.getId()+"'type='text' dir='ltr' onFocus=\"blockAjax()\" onBlur=\"unblockAjax()\" onkeydown=\"return keyDown(this)\" onkeyup=\"return checkField(event,this,'"
//                                    + data.getFormat()
//                                    + "')\" size='6' style='height:14pt;color:green;background:lightgoldenrodyellow;font-size:10pt;font-weight: bold; vertical-align: middle;' value="
//                                    + data.getFormattedValue() + ">");
//                        }
//
//                    } else {
//                        out.println(
//                                "<input type='text' dir='ltr' onfocus='this.blur()' readonly='readonly' border='0' size='8'"
//                                        + " style='height:14pt;color:green;font-size:10pt;font-weight: bold; vertical-align: middle;border:0;' value='"
//                                        + data.getFormattedValue() + "'>");
//                    }
//
//                    out.println("</td>");
//                    out.println("</tr>");
//                    break;
//            }
//        }
//    }

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



