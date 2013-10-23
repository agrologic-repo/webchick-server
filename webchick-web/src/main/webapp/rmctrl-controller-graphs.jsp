<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ include file="language.jsp" %>

<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.Program" %>
<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.io.PrintWriter" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    Controller controller = (Controller) request.getAttribute("controller");
    Program program = controller.getProgram();
    Collection<Screen> screens = program.getScreens();
    Integer newConnectionTimeout = (Integer) request.getAttribute("newConnectionTimeout");

    Locale oldLocal = (Locale) session.getAttribute("oldLocale");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmtctrl-graph.html?lang=" + lang + "&userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + controllerId + "&screenId=" + screenId);
    }

    String graphURLTH;
    String filenameth = GenerateGraph.generateChartTempHum(controllerId, session, new PrintWriter(out), currLocal);
    if (filenameth.contains("public_error")) {
        graphURLTH = request.getContextPath() + "/resources/custom/images/public_nodata_500x300.png";
    } else {
        graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
    }

    String filenamewft = GenerateGraph.generateChartWaterFeedTemp(controllerId, session, new PrintWriter(out), currLocal);
    String graphURLWFT;
    if (filenameth.contains("public_error")) {
        graphURLWFT = request.getContextPath() + "/resources/custom/images/public_nodata_500x300.png";
    } else {
        graphURLWFT = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamewft;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("all.screen.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/custom/style/tabstyle.css"/>
    <link rel="stylesheet" type="text/css" href="resources/custom/style/jquery-ui.css"/>

    <%--<script type="text/javascript" src="resources/custom/javascript/util.js">;</script>--%>
    <script type="text/javascript" src="resources/custom/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/custom/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/custom/javascript/jquery-ui.js">;</script>
    <script>
        $(function () {
            $("#accordion-graph").accordion({
                collapsible: true
            });
        });
    </script>
    <script type="text/javascript">
        /**logout*/
        function doLogout() {
            window.location = "logout.html";
        }
        /** refresh the page for loading updated data */
        function refresh() {
            redirect("./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>");
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
            window.location.replace('<a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>');
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
    </script>
</head>
<body>
<table width="100%">
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 95%">
                <table border="0" cellPadding=1 cellSpacing=1 width="100%">
                    <tr>
                        <td align="center">
                            <h2><%=controller.getTitle()%>
                            </h2>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%@include file="toplang.jsp" %>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 95%">
                <table border="0" width="100%" id="topnav">
                    <tr>
                        <% int col = 0; %>
                        <% final long MAIN_SCREEN = 1; %>
                        <% for (Screen screen : screens) {%>
                        <% if ((col % 8) == 0) {%>
                    </tr>
                    <tr>
                        <%}%>
                        <% col++;%>
                        <% String cssClass = ""; %>
                        <% if (screen.getId() == screenId) {%>
                        <% cssClass = "active";%>
                        <% } else {%>
                        <% cssClass = "";%>
                        <% }%>
                        <% if (screen.getId() == MAIN_SCREEN) {%>
                        <td nowrap>
                            <a class="<%=cssClass%>"
                               href="./rmctrl-main-screen-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId() %>&screenId=<%=MAIN_SCREEN%>"
                               id="<%=screen.getId()%>"
                               onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                            </a>
                        </td>
                        <% } else if (screen.getTitle().equals("Graphs")) {%>
                        <td nowrap>
                            <a class="<%=cssClass%>"
                               href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>"
                               id="<%=screen.getId()%>"
                               onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                            </a>
                        </td>
                        <% } else if (screen.getTitle().equals("Action Set Buttons")) {%>
                        <td nowrap>
                            <a class="<%=cssClass%>"
                               href="./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>"
                               id="<%=screen.getId()%>"
                               onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                            </a>
                        </td>
                        <% } else {%>
                        <td nowrap>
                            <a class="<%=cssClass%>"
                               href="rmctrl-controller-screens-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>"
                               id="<%=screen.getId()%>"
                               onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                            </a>
                        </td>
                        <%}%>
                        <%}%>
                    </tr>
                </table>


                <div id="accordion-graph">
                    <h3><%=session.getAttribute("graph.ioh.title")%>
                    </h3>

                    <div>
                        <img src="<%=graphURLTH%>" width=800 height=600 border=0
                             usemap="#<%=filenameth%>">
                    </div>
                    <h3><%=session.getAttribute("graph.fw.title")%>
                    </h3>

                    <div>
                        <img src="<%= graphURLWFT %>" width=800 height=600 border=0
                             usemap="#<%= filenamewft %>">
                    </div>
                </div>
            </fieldset>
        </td>
    </tr>
</table>
</body>
</html>
