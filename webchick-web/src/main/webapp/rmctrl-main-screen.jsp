<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.model.ScreenDto"/>
<jsp:directive.page import="com.agrologic.app.model.TableDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramAlarm"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramSystemStateDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramRelayDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>

<%  UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);
    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    List<ControllerDto> controllers = (List<ControllerDto>) request.getSession().getAttribute("controllers");
    List<DataDto> dataRelays = (List<DataDto>) request.getSession().getAttribute("dataRelays");
    Integer newConnectionTimeout = (Integer) request.getSession().getAttribute("newConnectionTimeout");

    HashMap<Long, Long> nextScrIdsByCntrl = (HashMap<Long, Long>) request.getSession().getAttribute("nextScrIdsByCntrl");

    String newlang = (String) request.getSession().getAttribute("lang");
    Locale oldLocal = (Locale) session.getAttribute("oldLocale");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmctrl-main-screen-ajax.jsp?userId=" + userId + "&cellinkId=" + cellinkId + "&screenId=1&doResetTimeout=true");
    }
%>
<%! boolean isAlarmOnController(List<DataDto> onScreenData) {
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
%>
<%! List<ProgramRelayDto> getProgramRelaysByRelayType(List<ProgramRelayDto> dataRelays, Long relayType) {
        List<ProgramRelayDto> relayList = new ArrayList<ProgramRelayDto>();
        for (ProgramRelayDto pr : dataRelays) {
            if (pr.getDataId().equals(relayType)) {
                relayList.add(pr);
            }
        }
        return relayList;
    }
%>
<%! DataDto getSetClock(List<DataDto> onScreenData) {
        for (DataDto data : onScreenData) {
            if (data.getId().equals(1309) && data.getValue() != null) {
                return data;
            }
        }
        return null;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html dir="<%=(String) request.getSession().getAttribute("dir")%>">
    <head>
        <title><%=session.getAttribute("main.screen.page.title")%></title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <link rel="shortcut icon" HREF="img/favicon5.ico" TITLE="AgroLogic Ltd." type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="css/rmtstyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/progressbar.css"/>
        <script type="text/javascript">
            function getCookie(name)
            {
                var start = document.cookie.indexOf( name + "=" );
                var len = start + name.length + 1;
                if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
                    return null;
                }
                if ( start == -1 )
                    return null;
                var end = document.cookie.indexOf( ";", len );
                if ( end == -1 )
                    end = document.cookie.length;
                return unescape( document.cookie.substring( len, end ) );
            }
            function doload()
            {
                var scrollTop = getCookie ("scrollTop");
                var scrollLeft = getCookie("scrollLeft");

                if (!isNaN(scrollTop))
                {
                    document.body.scrollTop = scrollTop;
                    document.documentElement.scrollTop = scrollTop;

                }
                if (!isNaN(scrollLeft))
                {
                    document.body.scrollLeft = scrollLeft;
                    document.documentElement.scrollLeft = scrollLeft;
                }

                Delete_Cookie("scrollTop");
                Delete_Cookie("scrollLeft");
            }
            function Refresh() {
                document.cookie = 'scrollTop=' + f_scrollTop();
                document.cookie = 'scrollLeft=' + f_scrollLeft();
                document.location.reload(true);
            }
            //Setting the cookie for vertical position
            function f_scrollTop() {
                return f_filterResults (
                window.pageYOffset ? window.pageYOffset : 0,
                document.documentElement ? document.documentElement.scrollTop : 0,
                document.body ? document.body.scrollTop : 0
            );
            }
            function f_filterResults(n_win, n_docel, n_body) {
                var n_result = n_win ? n_win : 0;
                if (n_docel && (!n_result || (n_result > n_docel)))
                    n_result = n_docel;
                return n_body && (!n_result || (n_result > n_body)) ? n_body : n_result;
            }
            //Setting the cookie for horizontal position
            function f_scrollLeft() {
                return f_filterResults (
                window.pageXOffset ? window.pageXOffset : 0,
                document.documentElement ? document.documentElement.scrollLeft : 0,
                document.body ? document.body.scrollLeft : 0
            );
            }
            function Delete_Cookie(name)
            {
                document.cookie = name + "=" + ";expires=Thu, 01-Jan-1970 00:00:01 GMT";

            }
            //window.onload=doload;
            /**logout*/
            function doLogout()
            {

                window.location="logout.html";
            }
            /** refresh the page for loading updated data */
            function refresh() {
                window.document.location.replace("./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=1%>");
            }
            var refreshIntervalId = setInterval("refresh()",55000);
            /** get connection timeout and set disconnect timer */
            var time = <%=newConnectionTimeout%> ;
            var timeoutId = setTimeout("disconnectTimer()", time);
            var TIMER, TIMES_UP, Slider;
            function resetTimer(){
                TIMES_UP = true;
                var slider = document.getElementById("divSlider");
                slider.style.width = "0px";
                clearTimeout(TIMER);
            }
            function stopTimer(){
                var table = document.getElementById("tblProgress");
                table.style.display = "none";
                resetTimer();
                window.location.replace('./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=1%>&doResetTimeout=true');
            }
            function disconnectTimer(){
                // clear refresh during display disconection
                // progress bar
                clearInterval(refreshIntervalId);
                TIMES_UP = false;
                var table = document.getElementById("tblProgress");
                var message = document.getElementById("divMessage");
                var slider = document.getElementById("divSlider");
                var curWidth = parseInt(slider.style.width);

                if (curWidth < 420) {
                    table.style.display = "block";
                    slider.style.width = curWidth + 1 + "px";
                    message.innerHTML="<%=session.getAttribute("label.disconnetion.progress")%> " +(parseInt((440-curWidth)/10)-1) + " <%=session.getAttribute("label.seconds")%>";
                    TIMER = setTimeout(disconnectTimer, 100);
                } else {
                    table.style.display = "none";
                    doLogout();
                }
            }
        </script>
        <script type="text/javascript" src="js/fhelp.js"></script>
        <script type="text/javascript" src="js/fglobal.js"></script>
    </head>
    <body onload="resetTimer();">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 95%">
                <table>
                    <tr>
                        <td>
                            <%@include file="toplang.jsp"%>
                        </td>
                        <td>
                            <h3 style="text-align: center;">
                                <%=session.getAttribute("main.screen.page.head")%>
                            </h3>
                            <!-- logout table -->
                            <table id="tblProgress" align="center" style="display:none;">
                                <tr>
                                    <td>
                                        <div id="divMessage" style="text-align:center;font-size:medium"></div>
                                        <div id="divSliderBG"><img src="Images/Transparent.gif" height="1" width="1"></div>
                                        <div id="divSlider"><img src="Images/Transparent.gif" height="1" width="1"></div>
                                        <input id="btnStop" align="center" type="button" value="<%=session.getValue("button.stay.online")%>" onclick="stopTimer();">
                                    </td>
                                </tr>
                            </table>
                            <h4><%=session.getAttribute("main.screen.page.comment")%></h4>
                        </td>
                        <td valign="bottom">
                            <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
                            <a href="<%=request.getContextPath()%>/userinfo.html?userId=<%=userId%>">
                                <img src="img/cellinks.png" style="cursor: pointer" border="0"/>
                                &nbsp;<%=session.getAttribute("button.cellinks")%>&nbsp;
                            </a>
                            <%} else {%>
                            <a href="<%=request.getContextPath()%>/overview.html?userId=<%=userId%>">
                                <img src="img/cellinks.png" style="cursor: pointer" border="0"/>
                                &nbsp;<%=session.getAttribute("button.cellinks")%>&nbsp;
                            </a>
                            <%}%>
                            <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                                <img src="img/chicken-icon.png" style="cursor: pointer" title="<%=session.getAttribute("button.connect.cellink")%>" border="0"/>
                                &nbsp;<%=session.getAttribute("main.screen.page.flocks")%>&nbsp;</a>
                            <a href="logout.html"><img src="img/logout.gif" style="cursor: pointer" border="0"/>
                                &nbsp;<%=session.getAttribute("label.logout")%>&nbsp;</a>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 95%">
                <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=1%>&doResetTimeout=true">
                    <img src="img/refresh.gif" style="cursor: pointer" border="0"/>&nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;</a>
                <table border="0">
                    <tr>
                        <td valign="top">
                            <form id="frm" style="display:inline">
                                <input type="hidden" name="state" value=""></input>
                                <table border="0">
                                    <tbody>
                                        <%
                            int column = 0;
                            for (ControllerDto controller : controllers) {%>
                                        <%if ((column % ControllerDto.COLUMN_NUMBERS) == 0) {%>
                                        <tr>
                                            <%}%>
                                            <td valign="top">
                                                <% List<TableDto> tables = controller.getSellectedScreenTables(screenId);
                                                    if (tables.size() > 0) {
                                                        List<DataDto> onScreenData = tables.get(0).getDataList();
                                                Long tableId = tables.get(0).getId();%>
                                                <table class="table-list" width="100%" border="1" borderColor="#848C96" onmouseover="this.style.borderColor='orange';this.style.borderWidth='0.1cm';this.style.borderStyle='solid';" onmouseout="this.style.borderColor='';this.style.borderWidth='';this.style.borderStyle='';">
                                                    <thead>
                                                        <%if (isAlarmOnController(onScreenData) == true) {%>
                                                        <th class="housesHeader" colspan="3" title="<%=session.getAttribute("label.program.version")%> - <%=controller.getProgram().getName()%>">
                                                            <img src="img/alarm.gif" title="Alarm in <%=controller.getTitle()%>">
                                                                <%} else {%>
                                                                <th class="housesHeader" colspan="3" title="<%=session.getAttribute("label.program.version")%> - <%=controller.getProgram().getName()%>">
                                                                    <%}%>
                                                                    <a href="./rmtctrl-screens.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=nextScrIdsByCntrl.get(controller.getId())%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" onclick='document.body.style.cursor = "wait"'><%=controller.getTitle()%>&nbsp;</a>
                                                                    <% DataDto setClock = controller.getSetClock();%>
                                                                    <%if (setClock.getValue() != null) {%>
                                                                    <div style="font-size:small; color: tomato; ">
                                                                        <%=session.getAttribute("label.controller.update.time")%> [ <%=controller.getSetClock().getFormatedValue()%>]</font>
                                                                    </div>
                                                                    <%}%>
                                                                </th>
                                                                </thead>
                                                                <tbody>
                                                                    <%    for (DataDto data : onScreenData) {
                                                                            if (data.isStatus()) {
                                                                                int special = data.getSpecial();
                                                                                switch (special) {
                                                case 0:%>
                                                                    <!--HOUSE REGULAR-->
                                                                    <tr class="unselected" onmouseover="this.className='selected'" onmouseout="this.className='unselected'">
                                                                        <td class="tableHeaders" nowrap><%=data.getUnicodeLabel()%></td>
                                                                        <td align="center" nowrap colspan="2">
                                                                            <%=data.getFormatedValue()%>
                                                                        </td>
                                                                    </tr>
                                                                    <%break;
                                                    case DataDto.RELAY:%>
                                                                    <!--RELAYS-->
                                                                    <% List<ProgramRelayDto> programRelays = controller.getProgram().getProgramRelays();%>
                                                                    <% List<ProgramRelayDto> relayList = getProgramRelaysByRelayType(programRelays, data.getId());%>
                                                                    <% if (relayList.size() > 0) {%>
                                                                    <%for (ProgramRelayDto relay : relayList) {%>
                                                                    <%  if (relay.getRelayNumber() != 0) {%>
                                                                    <tr>
                                                                        <td class="tableHeaders" nowrap><%=relay.getUnicodeText()%></td>
                                                                        <td align="center" nowrap colspan="2" class="" onmouseover="this.className='selected'" onmouseout="this.className=''">
                                                                            <% relay.init(data.getValue());%>
                                                                            <% if (relay.getText().startsWith("Fan") || relay.getText().startsWith("Mixer")) {%>
                                                                            <% if (relay.isOn()) {%>
                                                                            <img src="img/fan-on.gif">
                                                                                <%} else {%>
                                                                                <img src="img/fan-off.gif">
                                                                                    <%}%>
                                                                                    <%} else if (relay.getText().startsWith("Light")) {%>
                                                                                    <% if (relay.isOn()) {%>
                                                                                    <img src="img/light-on.gif">
                                                                                        <%} else {%>
                                                                                        <img src="img/light-off.png">
                                                                                            <%}%>
                                                                                            <%} else if (relay.getText().contains("Cool")) {%>
                                                                                            <% if (relay.isOn()) {%>
                                                                                            <img src="img/coolon.gif">
                                                                                                <%} else {%>
                                                                                                <img src="img/cooloff.gif">
                                                                                                    <%}%>
                                                                                                    <%} else if (relay.getText().contains("Heater")) {%>
                                                                                                    <% if (relay.isOn()) {%>
                                                                                                    <img src="img/heateron.gif">
                                                                                                        <%} else {%>
                                                                                                        <img src="img/heateroff.gif">
                                                                                                            <%}%>
                                                                                                            <%} else if (relay.getText().contains("Feed")) {%>
                                                                                                            <% if (relay.isOn()) {%>
                                                                                                            <img src="img/aougeron.gif">
                                                                                                                <%} else {%>
                                                                                                                <img src="img/aougeroff.gif">
                                                                                                                    <%}%>
                                                                                                                    <%} else if (relay.getText().contains("Water")) {%>
                                                                                                                    <% if (relay.isOn()) {%>
                                                                                                                    <img src="img/wateron.gif">
                                                                                                                        <%} else {%>
                                                                                                                        <img src="img/wateroff.gif">
                                                                                                                            <%}%>
                                                                                                                            <%} else if (relay.getText().contains("Ignition")) {%>
                                                                                                                            <% if (relay.isOn()) {%>
                                                                                                                            <img src="img/sparkon.gif">
                                                                                                                                <%} else {%>
                                                                                                                                <img src="img/sparkoff.gif">
                                                                                                                                    <%}%>
                                                                                                                                    <%} else {%>
                                                                                                                                    <% if (relay.isOn()) {%>
                                                                                                                                    <b style="color: blue"><%=session.getAttribute("relay.on")%></b>
                                                                                                                                    <%} else {%>
                                                                                                                                    <b style="color: red"><%=session.getAttribute("relay.off")%></b>
                                                                                                                                    <%}%>
                                                                                                                                    <%}%>
                                                                                                                                    </td>
                                                                                                                                    </tr>
                                                                                                                                    <%}%>
                                                                                                                                    <%}%>
                                                                                                                                    <%}%>
                                                                                                                                    <%break;
                                                    case DataDto.ALARM:%>
                                                                                                                                    <!--ALARMS-->
                                                                                                                                    <% List<ProgramAlarm> alarms = controller.getProgram().getProgramAlarmsByData(data.getId());%>
                                                                                                                                    <% StringBuilder toolTip = new StringBuilder();%>
                                                                                                                                    <% for (ProgramAlarm a : alarms) {%>
                                                                                                                                    <%toolTip.append("<p>" + a.getDigitNumber() + " - " + a.getText() + "</p>");%>
                                                                                                                                    <%}%>
                                                                                                                                    <tr class="unselected" onmouseover="this.className='selected'" onmouseout="this.className='unselected'">
                                                                                                                                        <td class="tableHeaders" nowrap><%=data.getUnicodeLabel()%></td>
                                                                                                                                        <div id="helpBox<%=controller.getId()%><%=data.getId()%>"
                                                                                                                                             style="background-color:#F0EFFF;
                                                                                                                                             color: #0000FF;
                                                                                                                                             overflow: hidden;
                                                                                                                                             z-index:999;
                                                                                                                                             position:absolute;
                                                                                                                                             margin:auto;
                                                                                                                                             width:250px;
                                                                                                                                             height:250px;
                                                                                                                                             padding-left:0px;
                                                                                                                                             border: 2px solid black;
                                                                                                                                             display:none;
                                                                                                                                             font-weight:bold;
                                                                                                                                             font-size:12px;">
                                                                                                                                            <div style="padding:1px;margin: 0px 0px 0px 0px;">
                                                                                                                                                <%=data.getUnicodeLabel()%>&nbsp;&nbsp;&nbsp;
                                                                                                                                                <div>
                                                                                                                                                    <a style="padding-right:1px; text-decoration:underline;font-weight:bold; font-size:15px;  " href='javascript:HideHelp("<%=controller.getId()%><%=data.getId()%>")'>X</a>
                                                                                                                                                </div>
                                                                                                                                            </div>
                                                                                                                                            <div id="helpBoxInner<%=controller.getId()%><%=data.getId()%>"
                                                                                                                                                 style="position: relative;
                                                                                                                                                 color:Black;
                                                                                                                                                 background-color:#ffffff;
                                                                                                                                                 left: 0px;
                                                                                                                                                 top: 0px;
                                                                                                                                                 overflow: auto;
                                                                                                                                                 width:250px;
                                                                                                                                                 height:338px;
                                                                                                                                                 border: 0px;
                                                                                                                                                 padding: 5px;
                                                                                                                                                 margin: 0px;
                                                                                                                                                 font-size:11x;">
                                                                                                                                            </div>
                                                                                                                                        </div>
                                                                                                                                        <td><%=data.getValue()%></td>
                                                                                                                                        <td>
                                                                                                                                            <span class="formHelpLink" style="color : #0000FF;font-weight: bold;font-size:1px;padding:0px 0px 0px 1px;margin:0px;cursor:help;"
                                                                                                                                                  valign="middle" align="left" onclick="ShowHelp(event, '<%=toolTip.toString()%>', HLP_SHOW_POS_MOUSE ,200,350,'<%=controller.getId()%><%=data.getId()%>');">
                                                                                                                                                <img src="img/help1.gif" width="16" height="16" alt="info"/>
                                                                                                                                            </span>
                                                                                                                                        </td>
                                                                                                                                    </tr>
                                                                                                                                    <%break;
                                                    case DataDto.SYSTEM_STATE:%>
                                                                                                                                    <tr class="unselected" onmouseover="this.className='selected'" onmouseout="this.className='unselected'">
                                                                                                                                        <% ProgramSystemStateDto programSystemState = controller.getProgram().getSystemStateByNumber(data.getValue());%>
                                                                                                                                        <td class="tableHeaders" nowrap><%=data.getUnicodeLabel()%></td>
                                                                                                                                        <td align="center" nowrap colspan="2"><%=programSystemState.getText()%></td>
                                                                                                                                    </tr>
                                                                                                                                    <% break;
                                                    }%>
                                                                                                                                    <%} else {%>
                                                                                                                                    <tr class="unselected" onmouseover="this.className='selected'"  onmouseout="this.className='unselected'">
                                                                                                                                        <td class="tableHeaders"  nowrap onmouseover="this.className='tdselected'" onmouseout="this.className='tableHeaders'"><%=data.getUnicodeLabel()%></td>
                                                                                                                                        <td align="center" nowrap colspan="2" align="center" style="text-decoration: underline" onclick='window.open("rmctrl-edit-value.jsp?controllerId=<%=controller.getId()%>&screenId=<%=screenId%>&tableId=<%=tableId%>&dataId=<%=data.getId()%>",
                                                                                                                                            "mywindow","status=no,width=200,height=200,left=350,top=400,screenX=100,screenY=100")'>
                                                                                                                                            <a title='Click to edit' ><%=data.getFormatedValue()%></a>
                                                                                                                                        </td>
                                                                                                                                    </tr>
                                                                                                                                    <%}
                                            }%>
                                                                                                                                    <% column++;%>
                                                                                                                                    </tbody>
                                                                                                                                    </table>
                                                                                                                                    <%}%>
                                                                                                                                    </td>
                                                                                                                                    <%if ((column % ControllerDto.COLUMN_NUMBERS) == 0) {%>
                                                                                                                                    </tr>
                                                                                                                                    <%}%>
                                                                                                                                    <%}%>
                                                                                                                                    </tbody>
                                                                                                                                    </table>
                                                                                                                                    </form>
                                                                                                                                    </td>
                                                                                                                                    </tr>
                                                                                                                                    <tr>
                                                                                                                                        <td align="center">
                                                                                                                                            <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=1%>&doResetTimeout=true">
                                                                                                                                                <img src="img/refresh.gif" style="cursor: pointer" border="0"/>
                                                                                                                                                &nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;
                                                                                                                                            </a>
                                                                                                                                        </td>
                                                                                                                                    </tr>
                                                                                                                                    </table>
                                                                                                                                    </fieldset>
                                                                                                                                    </td>
    </body>
</html>
