<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.*" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

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
    Long screenId = Long.parseLong((String) request.getParameter("screenId"));
    Controller controller = (Controller) request.getSession().getAttribute("controller");
    Program program = controller.getProgram();
    Collection<Screen> screens = program.getScreens();
    Integer newConnectionTimeout = (Integer) request.getSession().getAttribute("newConnectionTimeout");

    Collection<ActionSet> actionsets = (Collection<ActionSet>) request.getSession().getAttribute("actionset");
    Locale oldLocal = (Locale) session.getAttribute("oldLocale");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmtctrl-actionset.html?lang=" + lang + "&userId=" + userId + "&cellinkId=" + cellinkId + "&screenId=" + screenId + "&controllerId=" + controllerId);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html dir="<%=(String) request.getSession().getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("all.screen.page.title")%>
    </title>
    <link rel="shortcut icon" HREF="img/favicon5.ico" TITLE="AgroLogic Ltd." type="image/x-icon"/>
    <style type="text/css">
        div.tableHolder {
            OVERFLOW: auto;
            WIDTH: 800px;
            HEIGHT: 600px;
            POSITION: relative;
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
            TOP: expression(this.offsetParent.clientHeight - this.offsetParent.scrollHeight + this.offsetParent.scrollTop);
            HEIGHT: 20px;
            TEXT-ALIGN: left;
            text-wrap: suppress;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="css/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="css/tabstyle.css"/>
    <link rel="stylesheet" type="text/css" href="css/progressbar.css"/>
    <script type="text/javascript">
        /**logout*/
        function doLogout() {
            window.location = "logout.html";
        }
        /** refresh the page for loading updated data */
        function refresh() {
            window.document.location.replace("./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>");
        }
        var refreshIntervalId = setInterval("refresh()", 55000);
        /** get connection timeout and set disconnect timer */
        var time = <%=newConnectionTimeout%>;
        //var timeoutId = setTimeout("disconnectTimer()", time);
        var TIMER, TIMES_UP, Slider;
        function resetTimer() {
            TIMES_UP = true;
            var slider = document.getElementById("divSlider");
            slider.style.width = "0px";
            clearTimeout(TIMER);
        }
        function stopTimer() {
            var table = document.getElementById("tblProgress");
            table.style.display = "none";
            resetTimer();
            window.location.replace('<a href="./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true');
        }
        function disconnectTimer() {
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
                message.innerHTML = "<%=session.getAttribute("label.disconnetion.progress")%> " + (parseInt((220 - curWidth) / 10) - 1) + " <%=session.getAttribute("label.seconds")%>";
                TIMER = setTimeout(disconnectTimer, 100);
            } else {
                table.style.display = "none";
                doLogout();
            }
        }
        function buttonPress(cid, did, val) {
            // look for window.event in case event isn't passed in
            document.actionForm.action = "./change-actionset-value.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=" + cid + "&screenId=<%=screenId%>&dataId=" + did + "&Nvalue=" + val;
            document.actionForm.method = "POST";
            document.actionForm.submit();
            refresh();
        }
    </script>
</head>
<body>
<table width="100%">
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 95%">
                <table border="0" cellPadding=1 cellSpacing=1 width="100%">
                    <tr>
                        <td>
                            <table align="center">
                                <tr>
                                    <td align="center" valign="top">
                                        <h2><%=controller.getTitle()%>
                                        </h2>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%@include file="toplang.jsp" %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table id="tblProgress" align="center" style="display:none;">
                                <tr>
                                    <td align="left">
                                        <div id="divMessage" style="text-align:center;font-size:medium"></div>
                                        <div id="divSliderBG"><img src="Images/Transparent.gif" height="1" width="1"/>
                                        </div>
                                        <div id="divSlider"><img src="Images/Transparent.gif" height="1" width="1"/>
                                        </div>
                                        <input id="btnStop" align="center" type="button"
                                               value="<%=session.getAttribute("button.stay.online")%>"
                                               onclick="stopTimer();"/>
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
                <a href="./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&programId=<%=controller.getProgramId()%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                    <img src="img/refresh.gif" style="cursor: pointer" border="0"/>
                    &nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;
                </a>
                <table style="font-size:90%;" width="100%" border="0">
                    <tr>
                        <td valign="top">
                            <table border="0" width="100%" id="topnav">
                                <tr>
                                    <%
                                        int col = 0;
                                        final long MAIN_SCREEN = 1;
                                    %>
                                    <%for (Screen screen : screens) {%>
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
                                    <td nowrap>
                                        <a class="<%=cssClass%>"
                                           href="./rmctrl-main-screen-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&screenId=<%=MAIN_SCREEN%>&doResetTimeout=true"
                                           id="<%=screen.getId()%>"
                                           onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                                        </a>
                                    </td>
                                    <% } else if (screen.getTitle().equals("Graphs")) {%>
                                    <td nowrap>
                                        <a class="<%=cssClass%>"
                                           href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true"
                                           id="<%=screen.getId()%>"
                                           onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                                        </a>
                                    </td>
                                    <% } else if (screen.getTitle().equals("Action Set Buttons")) {%>
                                    <td nowrap>
                                        <a class="<%=cssClass%>"
                                           href="./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true"
                                           id="<%=screen.getId()%>"
                                           onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                                        </a>
                                    </td>
                                    <% } else {%>
                                    <td nowrap>
                                        <a class="<%=cssClass%>"
                                           href="./rmctrl-controller-screens-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true"
                                           id="<%=screen.getId()%>"
                                           onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                                        </a>
                                    </td>
                                    <%}%>
                                    <%}%>

                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form name="actionForm">
                                <table border="0" cellPadding="2" cellSpacing="2" align="center">
                                    <% int column = 0;%>
                                    <%
                                        for (ActionSet asd : actionsets) {
                                            if ((column % 5) == 0) {
                                    %>
                                    <tr>
                                        <%}%>
                                        <td align="center">
                                            <button class="actionbutton" type="sumbit"
                                                    onclick="buttonPress(<%=controllerId %>,<%=asd.getDataId() %>, <%=asd.getValueId() %>)"><%=asd.getUnicodeLabel()%>
                                            </button>
                                        </td>
                                        <%column++;%>
                                        <%if ((column % 5) == 0) {%>
                                    </tr>
                                    <%}%>
                                    <%}%>
                                </table>
                            </form>
                        </td>
                    </tr>
                </table>
                <table width="100%">
                    <tr>
                        <td align="center">
                            <a href="./rmtctrl-screens.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                                <img src="img/refresh.gif" style="cursor: pointer"
                                     border="0"/>&nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;
                            </a>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
</table>
</body>
</html>
