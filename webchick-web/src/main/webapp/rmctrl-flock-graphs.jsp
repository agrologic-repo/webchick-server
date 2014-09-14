<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="com.agrologic.app.model.Flock" %>
<%@ page import="java.io.PrintWriter" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Flock flock = (Flock) request.getAttribute("flock");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    Integer fromDay = -1;
    Integer toDay = -1;
    try {
        fromDay = Integer.parseInt(request.getParameter("fromDay"));
        if (fromDay == null) {
            fromDay = -1;
        }
        toDay = Integer.parseInt(request.getParameter("toDay"));
        if (toDay == null) {
            toDay = -1;
        }
    } catch (Exception ex) {
    }
    Integer growDay = 1;
    try {
        growDay = Integer.parseInt(request.getParameter("growDay"));
        if (growDay == null) {
            growDay = 1;
        }
    } catch (Exception ex) {
        growDay = 1;

    }

    //////////////////////////////////////////////////Graph 24 hour/////////////////////////////////////////////////////
    String filenameth = (String) session.getAttribute("filenameth-flockid=" + flockId + "&growday=" + growDay);
    if (filenameth == null) {
        filenameth = GenerateGraph.generateChartFlockTempHum(flockId, growDay.toString(), session,
                new PrintWriter(out), currLocal);
        session.setAttribute("filenameth-flockid=" + flockId + "&growday=" + growDay, filenameth);
    }

    String graphURLTH;
    if (filenameth.contains("public_error")) {
        graphURLTH = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
    }
    String filenamewft = (String) session.getAttribute("filenamewft");
    if (filenamewft == null) {
        filenamewft = GenerateGraph.generateChartFlockWaterFeedTemp(flockId, growDay.toString(), session, new PrintWriter(out), currLocal);
        session.setAttribute("filenamewft", filenamewft);
    }

    String graphURLWFT;
    if (filenamewft.contains("public_error")) {
        graphURLWFT = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLWFT = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamewft;
    }
    //////////////////////////////////////////////////Graph Daily///////////////////////////////////////////////////////

    String filenamefw = (String) session.getAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay);
    if (filenamefw == null) {
        filenamefw = GenerateGraph.generateChartFlockWaterFeed(flockId, fromDay.toString(), toDay.toString(),
                session, new PrintWriter(out), currLocal);
    }

    String graphURLWF;
    if (filenamefw.contains("public_error")) {
        graphURLWF = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLWF = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamefw;
    }

    String filenameaw = (String) session.getAttribute("filenameaw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay);
    if (filenameaw == null) {
        filenameaw = GenerateGraph.generateChartFlockAverageWeight(flockId, fromDay.toString(), toDay.toString(),
                session, new PrintWriter(out), currLocal);
        session.setAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamefw);
    }

    String graphURLAW;
    if (filenameaw.contains("public_error")) {
        graphURLAW = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLAW = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameaw;
    }

    String filenamehot = (String) session.getAttribute("filenameaw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay);
    if (filenamehot == null) {
        filenamehot = GenerateGraph.generateChartFlockHeatOnTime(flockId, fromDay.toString(), toDay.toString(),
                session, new PrintWriter(out), currLocal);
        session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
    }

    String graphURLHOT;
    if (filenamehot.contains("public_error")) {
        graphURLHOT = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLHOT = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamehot;
    }


    String filenamem = (String) session.getAttribute("filenamem-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay);
    if (filenamem == null) {
        filenamem = GenerateGraph.generateChartFlockMortality(flockId, fromDay.toString(), toDay.toString(),
                session, new PrintWriter(out), currLocal);
        session.setAttribute("filenamem-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamem);
    }

    String graphURLM;
    if (filenamem.contains("public_error")) {
        graphURLM = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLM = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamem;
    }


    String filenamemmh = (String) session.getAttribute("filenamemmh-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay);
    if (filenamemmh == null) {
        filenamemmh = GenerateGraph.generateChartFlockMinMaxTemperatureHumidity(flockId, fromDay.toString(), toDay.toString(),
                session, new PrintWriter(out), currLocal);
        session.setAttribute("filenamemmh-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamemmh);
    }
    String graphURLMMH;
    if (filenamefw.contains("public_error")) {
        graphURLMMH = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLMMH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamemmh;
    }
%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
</head>
<body>
<div>
    <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
    <input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>
</div>

<table width="100%">
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <table width="85%">
                    <tr>
                        <td width="65%">
                            <h1 style="text-align: center;"><%=session.getAttribute("history.graph.page.title")%>
                                <%=flock.getFlockName()%>
                            </h1>

                        </td>
                        <td width="20%">
                            <a href="rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1">
                                <img src="resources/images/display.png" style="cursor: pointer" hspace="5"
                                     border="0"/><%=session.getAttribute("button.screens")%>
                            </a>
                            <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                                <img src="resources/images/chicken-icon.png" style="cursor: pointer" hspace="5"
                                     border="0"/><%=session.getAttribute("main.screen.page.flocks")%>
                            </a>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <table border="0" width="85%" cellpadding="0" cellspacing="0" style="padding:1px;">
                    <tr>
                        <td>
                            <h2><%=session.getAttribute("history.graph.page.growday.graph.title")%>
                            </h2>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                                <form id="flock-graph" name="flock-graph" action="./rmctrl-flock-graphs.html">
                                    <input type="hidden" name="flockId" value="<%=flockId%>"/>
                                    <input type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
                                    <input type="hidden" name="userId" value="<%=userId%>"/>
                                    <table class="table-list-small">
                                        <tr>
                                            <td>
                                                <%--<%=session.getAttribute("label.growday")%> : <input type="text" size="5"--%>
                                                <%--name="growDay"--%>
                                                <%--value="<%=growDay%>"/>--%>

                                                <%if (fromDay == -1 || toDay == -1) {%>
                                                <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                                 name="fromDay"/>
                                                <%=session.getAttribute("label.to")%> : <input type="text" size="5"
                                                                                               name="toDay"/>
                                                <%} else {%>
                                                <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                                 name="fromDay"
                                                                                                 value="<%=fromDay%>"/>
                                                <%=session.getAttribute("label.to")%> :
                                                <input type="text" size="5" name="toDay" value="<%=toDay%>"/>
                                                <%}%>
                                                <input type="submit" value="<%=session.getAttribute("button.go")%>"/>
                                                <input type="button" id="btnClear" name="btnClear"
                                                       value="<%=session.getAttribute("button.clear")%>"/>
                                            </td>
                                            <td>
                                                <a href="./exptoexcelhistory.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                                                   onclick="window.location.href.replace('./exptoexcelhistory.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>')">
                                                    <img src="resources/images/excel.gif" style="cursor: pointer"
                                                         hspace="5"
                                                         border="0"/><%=session.getAttribute("button.export")%>
                                                </a>
                                                <a title="Table" style="cursor: pointer"
                                                   onclick="window.open('./rmctrl-flockhistory-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                                    <img src="resources/images/table.gif" style="cursor: pointer"
                                                         hspace="5"
                                                         border="0"/><%=session.getAttribute("button.table")%>
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </fieldset>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="accordion-daily-graph">
                                <h3><%=session.getAttribute("history.graph.page.panel.fwt.label")%>
                                </h3>
                                <div>
                                    <%
                                        //                                    String graphURLTH;
//                                    String filenameth = (String)session.getAttribute("filenameth-flockid="+flockId+"&growday="+growDay);
                                        if (filenameth == null) {
                                            filenameth = GenerateGraph.generateChartFlockTempHum(flockId, growDay.toString(), session,
                                                    new PrintWriter(out), currLocal);
                                            session.setAttribute("filenameth-flockid=" + flockId + "&growday=" + growDay, filenameth);
                                        }

                                        if (filenameth.contains("public_error")) {
                                            graphURLTH = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
                                        } else {
                                            graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
                                        }
                                    %>
                                    <img src="<%=graphURLWF%>" width=800 height=600 border=0 usemap="#<%=filenamefw%>">
                                </div>
                                <h3><%=session.getAttribute("history.graph.page.panel.aw.label")%>
                                </h3>

                                <div>
                                    <img src="<%=graphURLAW%>" width=800 height=600 border=0 usemap="#<%=filenameaw%>">
                                </div>
                                <h3><%=session.getAttribute("history.graph.page.panel.max.label")%>
                                </h3>

                                <div>
                                    <img src="<%=graphURLMMH%>" width=800 height=600 border=0
                                         usemap="#<%=filenamemmh%>">
                                </div>
                                <h3><%=session.getAttribute("history.graph.page.panel.mor.label")%>
                                </h3>

                                <div>
                                    <img src="<%=graphURLM%>" width=800 height=600 border=0 usemap="#<%=filenamem%>">
                                </div>
                                <h3><%=session.getAttribute("history.graph.page.panel.hon.label")%>
                                </h3>

                                <div>
                                    <img src="<%=graphURLHOT%>" width=800 height=600 border=0
                                         usemap="#<%=filenamehot%>">
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
                <table border="0" width="85%" cellpadding="0" cellspacing="0" style="padding:1px;">
                    <tr>
                        <td>
                            <h2><%=session.getAttribute("history.graph.page.24hour.graph.title")%>
                            </h2>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                                <form action="./rmctrl-flock-graphs.html">
                                    <input type="hidden" name="flockId" value="<%=flockId%>"/>
                                    <input type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
                                    <input type="hidden" name="userId" value="<%=userId%>"/>
                                    <table class="table-list-small">
                                        <tr>
                                            <td>
                                                <%=session.getAttribute("label.growday")%> : <input type="text" size="5"
                                                                                                    name="growDay"
                                                                                                    value="<%=growDay%>"/>
                                                <input type="submit" value="<%=session.getAttribute("button.go")%>"/>
                                            </td>
                                            <td>
                                            </td>
                                            <td>
                                                <img src="resources/images/excel.gif" style="cursor: pointer" hspace="5"
                                                     border="0"/>
                                                <a href="./exptoexcelhistory24.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>">
                                                    <%=session.getAttribute("button.export")%>
                                                </a>
                                                <img src="resources/images/table.gif" style="cursor: pointer" hspace="5"
                                                     border="0"/>
                                                <a onclick="window.open('./rmctrl-flockhistory24-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                                    <%=session.getAttribute("button.table")%>
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </fieldset>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="accordion-hourly-graph">
                                <h3><%=session.getAttribute("history.graph.page.panel.ioh24.label")%>
                                </h3>

                                <div>
                                    <img src="<%=graphURLTH%>" width=800 height=600 border=0
                                         usemap="#<%=filenameth%>">
                                </div>
                                <h3><%=session.getAttribute("history.graph.page.panel.fwt24.label")%>
                                </h3>

                                <div>
                                    <img src="<%= graphURLWFT %>" width=800 height=600 border=0
                                         usemap="#<%= filenamewft %>">
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
</table>
</body>
</html>