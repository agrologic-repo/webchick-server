<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ include file="language.jsp" %>

<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.Program" %>
<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.io.PrintWriter" %>

<%  User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long screenId = Long.parseLong((String) request.getParameter("screenId"));
    Controller controller = (Controller) request.getAttribute("controller");
    Program program = controller.getProgram();
    Collection<Screen> screens = program.getScreens();
    Integer newConnectionTimeout = (Integer) request.getAttribute("newConnectionTimeout");

    Locale oldLocal = (Locale) session.getAttribute("oldLocale");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmctrl-controller-graphs.jsp?lang=" + lang + "&userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + controllerId + "&screenId=" + screenId);
    }

    String graphURLTH = "";
    String filenameth = GenerateGraph.generateChartTempHum(controllerId, session, new PrintWriter(out), currLocal);
    if (filenameth.contains("public_error")) {
        graphURLTH = "img\\public_nodata_500x300.png";
    } else {
        graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
    }

    String filenamewft = GenerateGraph.generateChartWaterFeedTemp(controllerId, session, new PrintWriter(out), currLocal);
    String graphURLWFT = "";
    if (filenameth.contains("public_error")) {
        graphURLWFT = "img\\public_nodata_500x300.png";
    } else {
        graphURLWFT = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamewft;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("all.screen.page.title")%></title>

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
    <link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/tabstyle.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/progressbar.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
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
            window.location.replace('<a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true');
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
<!-- style="padding-top:0pt;padding-left:20pt;"-->
<!-- style="padding: 0 0 0 0px; color:maroon; text-transform:capitalize;" -->
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
            <a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                <img src="resources/images/refresh.gif" style="cursor: pointer" border="0"/>
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
                                <% } %>
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
                                       href="./rmctrl-main-screen-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId() %>&screenId=<%=MAIN_SCREEN%>&doResetTimeout=true"
                                       id="<%=screen.getId()%>"
                                       onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%>
                                    </a>
                                </td>
                                <% } else if (screen.getTitle().equals("Graphs")) {%>
                                <td nowrap>
                                    <a class="<%=cssClass%>"
                                       href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true"
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
                                       href="./rmctrl-controller-screens-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true"
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
            </table>

            <table cellPadding="2" cellSpacing="2" align="center">
                <tr>
                    <td valign="top" colspan="8">
                        <table cellSpacing=1 cellPadding=1 border=0 width="100%">
                            <%--
                            <tr>
                            <td>
                            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 95%">
                                <legend>Hide Series</legend>
                                <table class="table-list-small">
                                    <tr>
                                        <td>
                                            Check series you want to see
                                        </td>

                                        <td>
                                            <input type="checkbox">Inside</input>
                                        </td>
                                        <td>
                                            <input type="checkbox">Outside</input>
                                        </td>
                                        <td>
                                            <input type="checkbox">Humidity</input>
                                        </td>
                                        <td>
                                            <button type="button" onclick="">Submit</button>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                            </td>
                            </tr>
                            --%>
                            <tr>
                                <td align="center">
                                    <img src="<%=graphURLTH%>" width=800 height=600 border=0
                                         usemap="#<%=filenameth%>">
                                </td>
                            </tr>
                            <%--
                            <tr>
                            <td>
                            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 95%">
                                <legend>Hide Series</legend>
                                <table class="table-list-small">
                                    <tr>
                                        <td>
                                            Check series you want to see
                                        </td>

                                        <td>
                                            <input type="checkbox">Water</input>
                                        </td>
                                        <td>
                                            <input type="checkbox">Feed</input>
                                        </td>
                                        <td>
                                            <input type="checkbox">Temperature</input>
                                        </td>
                                        <td>
                                            <button type="button" onclick="">Submit</button>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                            </td>
                            </tr>
                            --%>
                            <tr>
                                <td align="center">
                                    <img src="<%= graphURLWFT %>" width=800 height=600 border=0
                                         usemap="#<%= filenamewft %>">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td align="center" valign="top">
                        <a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                            <img src="resources/images/refresh.gif" style="cursor: pointer" border="0"/>
                            &nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;
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
