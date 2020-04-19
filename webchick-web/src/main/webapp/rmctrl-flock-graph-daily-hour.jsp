<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.FlockDao" %>
<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="com.agrologic.app.model.Data" %>
<%@ page import="com.agrologic.app.model.HistoryHour" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ page errorPage="histerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    String cellinkId = request.getParameter("cellinkId");
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    String graphType = request.getParameter("graphType");
    Locale currLocale = (Locale) session.getAttribute("currLocale");

    List <Data> dataList = null;
    Collection<HistoryHour> hourHistory = null;
    boolean dORh = true;
    String grDay = null;

    if (graphType.equals("daily")){
//        data_list = GenerateGraph.getHistDataList(flock_id);
        dataList = GenerateGraph.getHistDataList(flockId, currLocale);
    } else {
        grDay = request.getParameter("growDay");
        FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
        if(grDay == null){
            grDay = flockDao.getUpdatedGrowDayHistory24MinWithData(flockId).toString();
            if (grDay.equals("0")){
                grDay = "1";
            }
        }
        hourHistory = flockDao.get_history_hour(flockId, Long.parseLong(grDay));
//        hour_history = GenerateGraph.get_labels_and_format(hour_history);
        hourHistory = GenerateGraph.get_labels_and_format(hourHistory, currLocale);
        dORh = false;
    }
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">

<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <style type="text/css">
        label {
            display: block;
            margin: 20px 0 0 0;
        }

        select {
            width: 100%;
        }

        table {
            font: normal 15px tahoma;
            margin: 0px;
        }

        .link-menu {
            margin: 0px;
            color: #000000;
            float: left;
            text-decoration: none;
            cursor: pointer;
            display: block;
            width: 100%;
            height: 100%;
        }

        .row-menu {
            margin: 0px;
            background-color: #aaaaaa;
            border-bottom: 1px solid #ffffff;
            text-decoration: none;
            text-align: left;
            display: block;
            cursor: default;
            line-height: 30px;

        }

        .row-menu:hover {
            background-color: #e6e6e6;
        }

        .selected {
            background-color: #e6e6e6;
        }

        .button {
            background-color: #aaaaaa;
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            -webkit-transition-duration: 0.4s; /* Safari */
            transition-duration: 0.4s;
            border-radius: 12px;
            box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
            width: 100%;
        }
    </style>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
</head>

<body onload="downLoad()">
<div id="loadingDiv" class="loadingClass" style="width: 100%">
    <table width="100%">
        <tr>
            <td align="center"><p><strong><em><spring:message code="graph.please.wait.while.page.loading"/>
            </em></strong></p></td>
        </tr>
    </table>
</div>
<input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
<input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>

<div class="drop-down-show-hide" style="width: 100%;">
    <% if (dORh) {//daily %>
        <div>
            <center>
                <button class="button"
                        onclick='return back("./rmctrl-flock-graph-daily-hour.jsp?cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&graphType=hour");'>
                    <%=session.getAttribute("graph.to.hour.graphs")%>
                </button>
            </center>
            <br>
        </div>
        <table id="daily-history-menu" border="0" cellpadding="0" cellspacing="0" border="1" style="border: 0px solid #0084e1;" width="100%">
        <% for (Data data : dataList) { %> <!--get data with value-->
            <tr class="row-menu ui-widget ui-corner-all">
                <td>
                    <a href="./rmctrl-flock-graph.jsp?flockId=<%=flockId%>&dataId=<%=data.getId()%>&graphType=<%=graphType%>"
                       c class="link-menu" target="rightPage">
                        <h3><%=data.getUnicodeLabel()%>
                        </h3>
                    </a>
                </td>
            </tr>
    <% } %>
        </table>
    <% } else { // hour %>
        <div>
            <center>
                <button class="button" onclick='return back("./rmctrl-flock-graph-daily-hour.jsp?cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&graphType=daily");'>
                    <%=session.getAttribute("graph.to.daily.graphs")%>
                </button>
            </center>
            <br>
        </div>
        <table id="daily-history-menu" border="0" cellpadding="0" cellspacing="0" border="1" style="border: 0px solid #0084e1;" width="100%">
            <% for (HistoryHour hh : hourHistory) { %>
            <tr class="row-menu ui-widget ui-corner-all">
                <td>
                    <a href="./rmctrl-flock-graph.jsp?flockId=<%=flockId%>&label=<%=hh.get_label()%>&graphType=<%=graphType%>&format=<%=hh.get_format()%>&dnum=<%=hh.get_d_num()%>&cellinkId=<%=cellinkId%>&growDay=<%=grDay%>"
                       target="rightPage" class="link-menu">
                        <h3><%=hh.get_label()%>
                        </h3>
                    </a>
                </td>
            </tr>
            <% } %>
        </table>
    <% } %>
</div>
<script>
    $('#daily-history-menu tr').on('click', function () {
        $('#daily-history-menu tr').removeClass('selected');
        $(this).toggleClass('selected');
        if ($(this).data('href') !== undefined) {
            document.location = $(this).data('href');
        }
    });

    $('#hourly-history-menu tr').on('click', function () {
        $('#hourly-history-menu tr').removeClass('selected');
        $(this).toggleClass('selected');
        if ($(this).data('href') !== undefined) {
            document.location = $(this).data('href');
        }
    });
</script>
<script>
    function downLoad() {
        if (document.all) {
            document.all["loadingDiv"].style.visibility = "hidden";
        } else if (document.getElementById) {
            document.getElementById("loadingDiv").style.visibility = 'hidden';
        }
    }
</script>
</body>

</html>

