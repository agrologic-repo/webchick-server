<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.agrologic.app.model.Flock" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    Flock flock = (Flock) request.getAttribute("flock");
%>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <style type="text/css">
        .menuFarme {
            border-collapse: collapse;
        }

        frame {
            border: 1px solid #1B213B;;
        }
    </style>
</head>
<c:choose>
    <c:when test='${dir eq "ltr"}'>
        <frameset rows="13%, * " frameborder="yes" border="2">
            <frame src="rmctrl-flock-graphs-top.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&flockName=<%=flock.getFlockName()%>&currLocal=<%=currLocal%>" name="top" target="_blank">
            <frameset cols="13.5%, *">
                <frame class="menuFarme" src="./rmctrl-flock-graphs-left.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&currLocal=<%=currLocal%>" name="leftPage">
                <frame src="dashboard.jsp" name="rightPage">
            </frameset>
        </frameset>
    </c:when>
    <c:otherwise>
        <frameset rows="13%, * " frameborder="yes" border="2">
            <frame src="rmctrl-flock-graphs-top.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&flockName=<%=flock.getFlockName()%>&currLocal=<%=currLocal%>" name="top" target="_blank">
            <frameset cols="* , 13.5%">
                <frame class="menuFarme" src="./rmctrl-flock-graphs-left.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&currLocal=<%=currLocal%>" name="rightPage">
                <frame src="dashboard.jsp" name="leftPage">
            </frameset>
        </frameset>
    </c:otherwise>
</c:choose>
</html>