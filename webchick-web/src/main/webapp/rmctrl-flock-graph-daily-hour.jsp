<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="com.agrologic.app.model.Data" %>
<%@ page import="com.agrologic.app.dao.FlockDao" %>
<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
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
    <title><%=session.getAttribute("history.graph.page.title")%></title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/test.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>

    <script type="text/javascript" src="resources/javascript/general.js">;</script>
</head>

<body>

<input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
<input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>

<div class="drop-down-show-hide" style="width: 100%;">

    <% if(dORh){//daily %>

    <div>
        <center>
            <button class="button" onclick='return back("./rmctrl-flock-graph-daily-hour.jsp?cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&graphType=hour");'>
                <%=session.getAttribute("graph.to.hour.graphs")%>
            </button>
        </center>
        <br>
    </div>

        <table id="daily-history-menu" border="0" cellpadding="0" cellspacing="0" border="1"style="border: 0px solid #0084e1;" width="100%">

            <% for (Data data : dataList){ %> <!--get data with value-->

                   <tr class="row-menu ui-widget ui-corner-all">
                       <td class=lefttd>
                           <a href="./rmctrl-flock-graph.jsp?flockId=<%=flockId%>&dataId=<%=data.getId()%>&graphType=<%=graphType%>" c class="link-menu" target="rightPage">
                              <%-- <h3><%=data.getLabel()%></h3>--%>
                                   <h3><%=data.getUnicodeLabel()%></h3>
                           </a>
                       </td>
                   </tr>

            <% } %>

        </table>

    <%  } else  { // hour %>

    <div>
        <center>
            <button class="button" onclick='return back("./rmctrl-flock-graph-daily-hour.jsp?cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&graphType=daily");'>
                <%=session.getAttribute("graph.to.daily.graphs")%>
            </button>
        </center>
        <br>
    </div>

        <table id="daily-history-menu" border="0" cellpadding="0" cellspacing="0" border="1"style="border: 0px solid #0084e1;" width="100%">

            <% for (HistoryHour hh : hourHistory){ %>

                   <tr class="row-menu ui-widget ui-corner-all">
                        <td class=lefttd>
                            <a href="./rmctrl-flock-graph.jsp?flockId=<%=flockId%>&label=<%=hh.get_label()%>&graphType=<%=graphType%>&format=<%=hh.get_format()%>&dnum=<%=hh.get_d_num()%>&cellinkId=<%=cellinkId%>&growDay=<%=grDay%>" target="rightPage" class="link-menu">
                                <h3><%=hh.get_label()%></h3>
                            </a>
                        </td>
                   </tr>

            <% } %>

        </table>

    <%  } %>
</div>

</body>

</html>

