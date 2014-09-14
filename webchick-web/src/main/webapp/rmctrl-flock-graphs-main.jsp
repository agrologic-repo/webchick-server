<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Locale currLocal = (Locale) session.getAttribute("currLocale");
%>
<html>
<head>
    <style type="text/css">
        .menuFarme {
            border-right: 0px solid #1B213B;
            border-style: 1px solid #1B213B;
            border-collapse: collapse;
        "
        }

        frame {
            border: 0px solid #1B213B;;
        }
    </style>
</head>
<frameset rows="13%, * " frameborder="yes" border="2">
    <frame src="rmctrl-flock-graphs-top.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&currLocal=<%=currLocal%>"
           name="top" target="_blank">
    <frameset cols="13.5%,*">
        <frame class="menuFarme"
               src="./rmctrl-flock-graphs-left.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&currLocal=<%=currLocal%>"
               name="leftPage">
        <frame src="dashboard.html" name="rightPage">
    </frameset>
</frameset>
</html>