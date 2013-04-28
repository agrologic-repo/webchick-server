<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.model.ScreenDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>
<jsp:directive.page import="com.agrologic.app.model.TableDto"/>

<%@page import = "com.agrologic.graph.GenerateGraph"%>
<%@page import = "org.jfree.chart.demo.servlet.WebChart" %>
<%@page import = "org.jfree.chart.demo.servlet.GraphDataSet" %>
<%@page import = "java.io.PrintWriter" %>
<%@page import = "java.util.ArrayList" %>
<%@page import = "java.util.Iterator" %>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long screenId = Long.parseLong((String)request.getParameter("screenId"));
    ControllerDto controller =(ControllerDto)request.getSession().getAttribute("controller");
    ProgramDto program = controller.getProgram();
    List<ScreenDto> screens = program.getScreens();
    Integer newConnectionTimeout =(Integer)request.getSession().getAttribute("newConnectionTimeout");

    Locale currLocale = (Locale)session.getAttribute("currLocale");

    String graphURLTH = "";
    String filenameth = GenerateGraph.generateChartTempHum(controllerId, session, new PrintWriter(out), currLocale);
    if(filenameth.contains("public_error")) {
        graphURLTH = "img\\public_nodata_500x300.png";
    } else {
        graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
    }

    String filenamewft = GenerateGraph.generateChartWaterFeedTemp(controllerId, session, new PrintWriter(out), currLocale);
    String graphURLWFT = "";
    if(filenameth.contains("public_error")) {
        graphURLWFT = "img\\public_nodata_500x300.png";
    } else {
        graphURLWFT = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamewft;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>">
    <head>
        <title><%=session.getAttribute("all.screen.page.title")%></title>
        <link rel="shortcut icon" HREF="img/favicon5.ico" TITLE="AgroLogic Ltd." type="image/x-icon" />
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
        <link rel="stylesheet" type="text/css" href="css/admincontent.css"/>
        <link rel="stylesheet" type="text/css" href="css/tabstyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/progressbar.css"/>
        <script type="text/javascript">
            /**logout*/
            function doLogout()
            {
                window.location="logout.html";
            }
            /** refresh the page for loading updated data */
            function refresh() {
                 window.document.location.replace("./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>");
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
                window.location.replace('<a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true');
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
            <a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                <img src="img/refresh.gif" style="cursor: pointer" border="0"/>
                &nbsp;<%=session.getAttribute("button.refresh")%>&nbsp;
            </a>
            <table style="font-size:90%;" width="100%" border="0">
            <tr>
                <td valign="top">
                    <table border="0"  width="100%"  id="topnav">
                        <tr>
                            <%int col = 0;
                            final long MAIN_SCREEN = 1;%>
                            <%for(ScreenDto screen : screens) {%>
                                <% if ( (col % 8) == 0 ) {%>
                                    </tr><tr>
                                <% } %>
                                <% col++;%>
                                <% String cssClass =""; %>
                                <% if(screen.getId() == screenId ) {%>
                                    <% cssClass = "active";%>
                                <% } else {%>
                                    <% cssClass = "";%>
                                <% }%>

                                <% if (screen.getId() == MAIN_SCREEN ) {%>
                                    <td nowrap>
                                        <a class="<%=cssClass%>" href="./rmctrl-main-screen-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId() %>&screenId=<%=MAIN_SCREEN%>&doResetTimeout=true" id="<%=screen.getId()%>" onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                    </td>
                                <% } else if (screen.getTitle().equals("Graphs")) {%>
                                    <td nowrap>
                                        <a class="<%=cssClass%>" href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" id="<%=screen.getId()%>" onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                    </td>
                                <% } else if (screen.getTitle().equals("Action Set Buttons")) {%>
                                        <td nowrap>
                                            <a class="<%=cssClass%>" href="./rmtctrl-actionset.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId()%>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" id="<%=screen.getId()%>"  onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
                                        </td>
                                <% } else {%>
                                    <td nowrap>
                                        <a class="<%=cssClass%>" href="./rmctrl-controller-screens-ajax.jsp?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=controller.getCellinkId()%>&programId=<%=controller.getProgramId() %>&screenId=<%=screen.getId()%>&controllerId=<%=controller.getId()%>&doResetTimeout=true" id="<%=screen.getId()%>" onclick='document.body.style.cursor = "wait"'><%=screen.getUnicodeTitle()%></a>
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
                                <img src="<%=graphURLTH%>" width=800 height=600 border=0 usemap="#<%=filenameth%>">
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
                                <img src="<%= graphURLWFT %>" width=800 height=600 border=0 usemap="#<%= filenamewft %>">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center" valign="top">
                <a href="./rmtctrl-graph.html?lang=<%=lang%>&userId=<%=userId%>&cellinkId=<%=cellinkId%>&programId=<%=controller.getProgramId() %>&screenId=<%=screenId%>&controllerId=<%=controller.getId()%>&doResetTimeout=true">
                <img src="img/refresh.gif" style="cursor: pointer" border="0"/>
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
