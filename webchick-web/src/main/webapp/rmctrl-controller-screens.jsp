<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.model.ScreenDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>
<jsp:directive.page import="com.agrologic.app.model.TableDto"/>
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
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    List<ControllerDto> controllers = (List<ControllerDto>) request.getSession().getAttribute("controllers");
    ControllerDto controller = getController(controllers, controllerId);
    ProgramDto program = controller.getProgram();
    List<ScreenDto> screens = program.getScreens();
    Integer newConnectionTimeout = (Integer) request.getSession().getAttribute("newConnectionTimeout");

    Locale oldLocal = (Locale) session.getAttribute("oldLocale");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmtctrl-screens.html?lang="+ lang + "&userId=" + userId + "&cellinkId=" + cellinkId + "&screenId=" + screenId+"&controllerId=" + controllerId);
    }
%>
<%! ControllerDto getController(List<ControllerDto> controllers, Long controllerId) {
        for (ControllerDto c : controllers) {
            if (c.getId().equals(controllerId)) {
                return c;
            }
        }
        return null;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String) request.getSession().getAttribute("dir")%>">
    <head>
        <title><%=session.getAttribute("all.screen.page.title")%></title>
        <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Ltd." type="image/x-icon" />
        <style type="text/css">
            div.tableHolder {
                OVERFLOW:auto;
                WIDTH: 800px;
                HEIGHT: 600px;
                POSITION:relative;
            }
            thead td {
                Z-INDEX: 20;
                POSITION: relative;
                TOP: expression(this.offsetParent.scrollTop-2);
                HEIGHT: 20px;
                TEXT-ALIGN: center
            }
            tfoot td {
                Z-INDEX: 20;
                POSITION: relative;
                TOP: expression(this.offsetParent.clientHeight - this.offsetParent.scrollHeight + this.offsetParent.scrollTop); HEIGHT: 20px;
                TEXT-ALIGN: left;
                text-wrap: suppress;
            }
        </style>
        <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tld."/>
        <link rel="stylesheet" type="text/css" href="css/rmtstyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/tabstyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/progressbar.css"/>
        <script type="text/javascript">
            /**logout*/
            function doLogout() {
                window.location="logout.html";
            }
            /** refresh the page for loading updated data */
            function refresh() {
                window.document.location.replace("./rmtctrl-screens.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>");
            }
            var refreshIntervalId = setInterval("refresh()",55000);
            /** get connection timeout and set disconnect timer */
            var time = <%=newConnectionTimeout%> ;
            //var timeoutId = setTimeout("disconnectTimer()", time);
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
                window.location.replace('./rmtctrl-screens.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true');
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

                if (curWidth < 210) {
                    table.style.display = "block";
                    slider.style.width = curWidth + 1 + "px";
                    message.innerHTML="<%=session.getAttribute("label.disconnetion.progress")%> " +(parseInt((220-curWidth)/10)-1) + " <%=session.getAttribute("label.seconds")%>";
                    TIMER = setTimeout(disconnectTimer, 100);
                } else {
                    table.style.display = "none";
                    alert("timeout " + time + " TIMER" + TIMER)
                    doLogout();
                }
            }
        </script>
    </head>
    <body onload="resetTimer();">
        <table width="100%" >
            <tr>
                <td align="center">
                    <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 95%">
                        <table border="0" cellPadding=1 cellSpacing=1 width="100%">
                            <tr>
                                <td>
                                    <table align="center">
                                        <tr>
                                            <td align="center" valign="top">
                                                <h2><%=controller.getTitle()%></h2>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <%@include file="toplang.jsp"%>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <table id="tblProgress" align="center" style="display:none;">
                                        <tr>
                                            <td align="left">
                                                <div id="divMessage" style="text-align:center;font-size:medium"></div>
                                                <div id="divSliderBG"><img src="Images/Transparent.gif" height="1" width="1"/></div>
                                                <div id="divSlider"><img src="Images/Transparent.gif" height="1" width="1"/></div>
                                                <input id="btnStop" align="center" type="button" value="<%=session.getAttribute("button.stay.online")%>" onclick="stopTimer();"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 95%">
                        <a href="./rmtctrl-screens.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                            <img src="img/refresh.gif" style="cursor: pointer" border="0" hspace="5"/>
                            <%=session.getAttribute("button.refresh")%>
                        </a>
                        <table style="font-size:90%;" width="100%" border="0">
                            <tr>
                                <td align="center" valign="top">
                                    <table border="0"  width="100%"  id="topnav">
                                        <tr>
                                            <%int col = 0;
                                                final long MAIN_SCREEN = 1;%>
                                            <%for (ScreenDto screen : screens) {%>
                                            <% if ((col % 8) == 0) {%>
                                        </tr>
                                        <tr>
                                            <% }%>
                                            <% col++;%>
                                            <% String cssClass = "";%>
                                            <% if (screen.getId() == screenId) {%>
                                                <% cssClass = "active";%>
                                            <% } else {%>
                                                <% cssClass = "";%>
                                            <% }%>
                                                <% if (screen.getId() == MAIN_SCREEN) {%>
                                                    <td nowrap align="center">
                                                        <a class="<%=cssClass%>" href="./rmctrl-main-screen-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&screenId=<%=MAIN_SCREEN%>&doResetTimeout=true" id="<%=screen.getId()%>" onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                                    </td>
                                                <% } else if (screen.getTitle().equals("Graphs")) {%>
                                                    <td nowrap align="center">
                                                        <a class="<%=cssClass%>"href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" id="<%=screen.getId()%>" onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                                    </td>
                                                <% } else if (screen.getTitle().equals("Action Set Buttons")) {%>
                                                    <td nowrap>
                                                        <a class="<%=cssClass%>" href="./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" id="<%=screen.getId()%>"  onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                                    </td>
                                                <% } else {%>
                                                    <td nowrap align="center">
                                                        <a class="<%=cssClass%>" href="./rmtctrl-screens.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" id="<%=screen.getId()%>" onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                                    </td>
                                                <%}%>
                                            <%}%>

                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>

                        <table cellPadding="2" cellSpacing="2" align="center">
                            <%
                                int column = 0;
                                ScreenDto currScreen = program.getScreenById(screenId);
                                List<TableDto> tables = currScreen.getTables();
                                if (tables.size() > 0) {%>
                            <%for (TableDto table : tables) {
                                    Long tableId = table.getId();
                                    if ((column % ScreenDto.COLUMN_NUMBERS) == 0) {%>
                            <tr>
                                <%}%>
                                <td valign="top" colspan="8">
                                    <table class="table-list" width="100%" align=left border="1" borderColor="#848C96" onmouseover="this.style.borderColor='orange';this.style.borderWidth='0.1cm';this.style.borderStyle='solid';" onmouseout="this.style.borderColor='';this.style.borderWidth='';this.style.borderStyle='';">
                                        <thead>
                                            <th class="housesHeader" nowrap colspan="2">
                                                <%=table.getUnicodeTitle()%>
                                            </th>
                                        </thead>
                                        <tbody>
                                            <%  for (DataDto data : table.getDataList()) {
                                                    if (data.isStatus()) {
                                                        int special = data.getSpecial();
                                                        switch (special) {
                                                            case 0:%>
                                                            <!--HOUSE ALARMS-->
                                                            <tr class="unselected" onmouseover="this.className='selected'" onmouseout="this.className='unselected'">
                                                                <td class="tableHeaders" nowrap><%=data.getUnicodeLabel()%></td>
                                                                <td class="tableValues" nowrap><%=data.getFormatedValue()%></td>
                                                            </tr>
                                                        <% break;
                                                        default:%>
                                                            <tr class="unselected" onmouseover="this.className='selected'" onmouseout="this.className='unselected'">
                                                                <td class="tableHeaders" nowrap><%=data.getUnicodeLabel()%></td>
                                                                <td class="tableValues" nowrap><%=data.getFormatedValue()%></td>
                                                            </tr>
                                                        <% break;
                                                        }%>
                                                    <%} else {%>
                                                        <tr class="unselected" onmouseover="this.className='selected'" onmouseout="this.className='unselected'">
                                                            <td class="tableHeaders" nowrap onmouseover="this.className='tdselected'" onmouseout="this.className='tableHeaders'"><%=data.getUnicodeLabel()%></td>
                                                            <td class="tableValues" style="text-decoration: underline" onclick='window.open("rmctrl-edit-value.jsp?controllerId=<%=controller.getId()%>&screenId=<%=screenId%>&tableId=<%=tableId%>&dataId=<%=data.getId()%>","mywindow","status=no,width=200,height=200,left=350,top=400,screenX=100,screenY=100")'>
                                                                <a title='Click to edit' ><%=data.getFormatedValue()%></a>
                                                            </td>
                                                        </tr>
                                                    <%}
                                                }%>
                                        </tbody>
                                    </table>
                                </td>
                                <%column++;%>
                                <%if ((column % ScreenDto.COLUMN_NUMBERS) == 0) {%>
                            </tr>
                            <%}%>
                            <%}%>
                            <%}%>
                        </table>
                        <table width="100%">
                            <tr>
                                <td align="center">
                                    <a href="./rmtctrl-screens.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                                        <img src="img/refresh.gif" style="cursor: pointer" border="0"/>&nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td></tr></table>
    </body>
</html>
