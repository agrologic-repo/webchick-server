<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    String cellinkId = request.getParameter("cellinkId");
    String flockId = request.getParameter("flockId");
    Locale currLocale = (Locale) session.getAttribute("currLocale");
    String pageDir = "leftPage";
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%></title>
    <style type="text/css">
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
            box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
            width: 100%;
        }
    </style>

</head>

<body>
<div>
    <!--daily-->
    <form action="./rmctrl-flock-graph-daily-hour.jsp" target="<%=pageDir%>">
        <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
        <input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>
        <input id="graphType" type="hidden" name="graphType" value="daily"/>
        <input id="currLocale" type="hidden" name="currLocale" value="<%=currLocale%>"/>
        <button class="button" id="daily">
            <%=session.getAttribute("graph.to.daily.graphs")%>
        </button>
    </form>
    <!--hour-->
    <form action="./rmctrl-flock-graph-daily-hour.jsp" target="<%=pageDir%>">
        <input id="cellinkIdd" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
        <input id="flockIdd" type="hidden" name="flockId" value="<%=flockId%>"/>
        <input id="graphTypee" type="hidden" name="graphType" value="hour"/>
        <input id="currLocalee" type="hidden" name="currLocale" value="<%=currLocale%>"/>
        <!--gr_day-->
        <button class="button">
            <%=session.getAttribute("graph.to.hour.graphs")%>
        </button>
    </form>
</div>
<script>
    $("document").ready(function () {
        $("#daily")[0].click();
    });
</script>
</body>

</html>