<%@ page import="com.agrologic.app.model.Flock" %>
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
%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <style type="text/css">
        table {
            border: 1px solid #1B213B;
            font: normal 15px tahoma;
            width: 200px;
            margin: 0px;
        }

        .leftlink {
            margin: 0px;
            color: white;
            float: left;
            text-decoration: none;
            line-height: 30px;
            cursor: pointer
        }

        .leftlinkh {
            margin: 0px;
            background: blue;
            color: black;
            text-decoration: none;
            text-align: left;
            width: 200px;
            display: block;
            cursor: default
        }

        a, a:active, a:visited {
            outline: none
        }
    </style>
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
<div> Choose Graph</div>
<select id="dropDown">
    <option></option>
    <option value="div1">Daily Graphs</option>
    <option value="div2">Hourly Graphs</option>
</select>

<div id="div1" class="drop-down-show-hide">
    <table border="0" cellpadding="0" cellspacing="0" border="1" style="border: 1px solid #1B213B;">
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-feed-water.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Feed & Water
                    </h3>
                </a>
            </td>
        </tr>
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-average-weight.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Average Weight
                    </h3>
                </a>
            </td>
        </tr>
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-minmaxhum.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Min & Max
                    </h3>
                </a>
            </td>
        </tr>
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-heaton-time.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Heaters On Time
                    </h3>
                </a>
            </td>
        </tr>
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-mortality.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Mortality
                    </h3>
                </a>
            </td>
        </tr>
    </table>
</div>
<div id="div2" class="drop-down-show-hide">
    <table border="0" cellpadding="0" cellspacing="0" border="1" style="border: 1px solid #1B213B;">
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-feed-water.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Feed & Water
                    </h3>
                </a>
            </td>
        </tr>
        <tr class=leftlinkh>
            <td class=lefttd>
                <a href="./rmctrl-flock-minmaxhum.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>&fromDay<%=fromDay%>&toDay=<%=toDay%>"
                   target="rightPage" class="leftlink">
                    <h3>
                        Temperature & Humidity
                    </h3>
                </a>
            </td>
        </tr>
    </table>
</div>
<script>
    $('.drop-down-show-hide').hide();
    $('#dropDown').change(function () {
        $('.drop-down-show-hide').hide();
        $('#' + this.value).show();
    });
    $("#dropDown").val("div1").change();
</script>
</body>
</html>