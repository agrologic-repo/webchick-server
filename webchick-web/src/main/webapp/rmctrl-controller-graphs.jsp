<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ include file="language.jsp" %>

<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.agrologic.app.model.*" %>

<%
    User user;
    Long userId;
    Long cellinkId;
    Long controllerId;
    Long screenId;
    Controller controller;
    Program program;
    Collection<Screen> screens;
    Integer newConnectionTimeout;
    Locale oldLocal;
    Locale currLocal;
    String graphURLTH;
    String filenameth;
    String filenamewft;
    String graphURLWFT;
    String filenamefwpb;
    String graphURLFWPB;
    String filenamef2w2pb;
    String graphURLF2W2PB;
    String filenamewspb;
    String graphURLWSPB;

    user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    userId = Long.parseLong(request.getParameter("userId"));

    if(user.getUserRole().equals(UserRole.USER)) {
        try {
            if (!user.getId().equals(userId)) {
                response.sendRedirect("./index.htm");
            }
        } catch (Exception e) {
        }
    }

    cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    controllerId = Long.parseLong(request.getParameter("controllerId"));
    screenId = Long.parseLong(request.getParameter("screenId"));
    controller = (Controller) request.getAttribute("controller");
    program = controller.getProgram();
    screens = program.getScreens();
    newConnectionTimeout = (Integer) request.getAttribute("newConnectionTimeout");

    oldLocal = (Locale) session.getAttribute("oldLocale");
    currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmtctrl-graph.html?lang=" + lang + "&userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + controllerId + "&screenId=" + screenId);
    }

        filenameth = GenerateGraph.generateChartTempHum(controllerId, session, new PrintWriter(out), currLocal);
        if (filenameth.contains("public_error")) {
            graphURLTH = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
        } else {
            graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
        }

        filenamewft = GenerateGraph.generateChartWaterFeedTemp(controllerId, session, new PrintWriter(out), currLocal);
        if (filenameth.contains("public_error")) {
            graphURLWFT = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
        } else {
            graphURLWFT = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamewft;
        }

        filenamefwpb = GenerateGraph.generateChartFeedWaterPerBird24(controllerId, session, new PrintWriter(out), currLocal);
        if (filenameth.contains("public_error")) {
            graphURLFWPB = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
        } else {
            graphURLFWPB = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamefwpb;
        }

        filenamef2w2pb = GenerateGraph.generateChartFeed2Water2_24(controllerId, session, new PrintWriter(out), currLocal);
        if (filenameth.contains("public_error")) {
            graphURLF2W2PB = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
        } else {
            graphURLF2W2PB = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamef2w2pb;
        }

        filenamewspb = GenerateGraph.generateChartWaterSum_24(controllerId, session, new PrintWriter(out), currLocal);
        if (filenameth.contains("public_error")) {
            graphURLWSPB = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
        } else {
            graphURLWSPB = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamewspb;
        }

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("all.screen.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/tabstyle.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <%--<script type="text/javascript" src="resources/javascript/util.js">;</script>--%>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
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
                               href="./rmctrl-main-screen.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId() %>&screenId=<%=MAIN_SCREEN%>"
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
                               href="./rmctrl-controller-screens-ajax.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>"
                               id="<%=screen.getId()%>"
                               onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                            </a>
                        </td>
                        <%}%>
                        <%}%>
                    </tr>
                </table>
                <div id="accordion-graph">
                    <h3><%=session.getAttribute("graph.inside.outside.humidity.title")%></h3>
                    <div><img src="<%=graphURLTH%>" width=800 height=400 border=0 usemap="#<%=filenameth%>"></div>
                    <h3><%=session.getAttribute("graph.feed.water.title")%></h3>
                    <div><img src="<%= graphURLWFT %>" width=800 height=400 border=0 usemap="#<%= filenamewft%>"></div>
                    <h3><%=session.getAttribute("graph.feed.water.per.bird.title")%></h3>
                    <div><img src="<%= graphURLFWPB %>" width=800 height=400 border=0 usemap="#<%= filenamefwpb%>"></div>
                    <h3><%=session.getAttribute("graph.feed2.water2.title")%></h3>
                    <div><img src="<%= graphURLF2W2PB %>" width=800 height=400 border=0 usemap="#<%= filenamef2w2pb%>"></div>
                    <h3><%=session.getAttribute("graph.water.sum.title")%></h3>
                    <div><img src="<%= graphURLWSPB %>" width=800 height=400 border=0 usemap="#<%= filenamewspb%>"></div>
                </div>
            </fieldset>
        </td>
    </tr>
</table>
</body>
</html>
