<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.DataDto" %>
<%@ page import="com.agrologic.app.model.UserDto" %>
<%@ page import="java.util.List" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
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
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    List<DataDto> historyData = (List<DataDto>) request.getSession().getAttribute("historyData");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>">
<head>
    <title>Flock History</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <script type="text/javascript" src="js/util.js"></script>
    <script type="text/javascript">
        function back(link) {
            window.document.location.replace(link);
            return false;
        }
    </script>
</head>
<body>
<input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
<input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>
<table width="100%">
<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%">
            <table width="80%">
                <tr>
                    <td>
                        <%@include file="toplang.jsp" %>
                    </td>
                    <td align="center">
                        <h1 style="text-align: center;">Flock History</h1>
                    </td>
                    <td>
                        <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1&doResetTimeout=true">
                            <img src="img/display.png" style="cursor: pointer" border="0"/>
                            &nbsp;<%=session.getAttribute("button.screens")%>&nbsp;
                        </a>
                        <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                            <img src="img/chicken-icon.png" style="cursor: pointer" border="0"/>
                            <%=session.getAttribute("main.screen.page.flocks")%>
                        </a>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
</tr>
<tr>
<td align="center">
<fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%">
    <table border="0" width="1024px" cellpadding="0" cellspacing="0" style="padding:1px;">
        <tr>
            <td>
                <h2> history graphs by grow day </h2>
            </td>
            <td>
                <h2> history 24 hour graphs by grow day </h2>
            </td>
        </tr>
        <tr>
            <td>
                <table class="table-list">
                    <thead>
                    <th width="200px" align="center" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">Title
                    </th>
                    <th align="center" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">Graph
                    </th>
                    <th align="center" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">Table
                    </th>
                    <th align="center" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">Export
                    </th>
                    </thead>
                    <tbody>
                    <tr class="even">
                        <td>
                            <a href="./rmctrl-flockwaterfeed-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                Feed and Water Consumption
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockwaterfeed-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph1.png" border="0" hspace="10"
                                     onclick="window.location='./rmctrl-flockwaterfeed-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>'">
                                </img>
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockfeedwater-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/table.gif" border="0" hspace="10"
                                     onclick="window.location='./rmctrl-flockfeedwater-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>'">
                                </img>
                            </a>
                        </td>
                        <td>
                            <a href="./ExpToExcelFeedWater?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/excel.gif" border="0" hspace="10"
                                     onclick="window.location='./ExpToExcelFeedWater?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>'">
                                </img>
                            </a>
                        </td>
                    </tr>
                    <tr class="odd">
                        <td>
                            <a href="./rmctrl-flockmortality-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                Mortality
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockmortality-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph2.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockmortality-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/table.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./ExpToExcelMortality?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/excel.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                    </tr>
                    <tr class="even">
                        <td>
                            <a href="./rmctrl-flockminmaxhum-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                Minimum and Maximum Temperature and Humidity
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockminmaxhum-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph3.png" border="0" title="Outside Temperature"
                                     hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockminmaxtemphum-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/table.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./ExpToExcelMinMaxHum?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/excel.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                    </tr>
                    <tr class="odd">
                        <td>
                            <a href="./rmctrl-flockavgweight-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                Average Weight
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockavgweight-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph4.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockavgweight-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/table.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./ExpToExcelAvgWeight?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/excel.gif" border="0" title="Inside Temperature"
                                     hspace="10"/>
                            </a>
                        </td>
                    </tr>
                    <tr class="even">
                        <td>
                            <a href="./rmctrl-flockheatontime-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                Heat ON Time
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockheatontime-graphs.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph5.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./rmctrl-flockheatontime-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/table.gif" border="0" hspace="10"/>
                            </a>
                        </td>
                        <td>
                            <a href="./ExpToExcelHeatOnTime?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/excel.gif" border="0" title="Inside Temperature"
                                     hspace="10"/>
                            </a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
            <td valign="top">
                <table class="table-list">
                    <th align="center" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">Title
                    </th>
                    <th align="center" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">Graph
                    </th>
                    <tr>
                        <td>In\Out Temperature and Humidity</td>
                        <td>
                            <a href="./rmctrl-flock24graph-ioh.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph1.png" border="0" hspace="10"/>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td>Water and Feed Consumption</td>
                        <td>
                            <a href="./rmctrl-flock24graph-wfc.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                               style="font-size: medium">
                                <img src="img/graph1.png" border="0" hspace="10"/>
                            </a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</fieldset>
</td>
</tr>
</table>
</body>
</html>