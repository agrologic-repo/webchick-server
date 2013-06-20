<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.CellinkDto" %>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>

<%
    UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    CellinkDto cellink = (CellinkDto) request.getSession().getAttribute("cellink");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title><%=cellink.getName()%>
    </title>
    <LINK REL="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Tm.">
    <link rel="stylesheet" type="text/css" href="css/rmtstyle.css"/>
</head>
<body>
<br>

<h2>Connected to <%=cellink.getName()%>
</h2>
<hr>
<br>

<h3><a href="rmctrl-main-screen-ajax.jsp?cellinkId=<%=cellink.getId()%>&screenId=1" target="body" class="leftlink"><img
        src="img/ComputerScreen.gif" style="border:0px;height:32px;width:32px;"> Screens </a></h3>
Control your farm as if you were sitting in front of it.
<br>

<h3><a href="rmctrl-underconstruction.jsp" target="body" class="leftlink"><img src="img/configure.png"
                                                                               style="border:0px;height:32px;width:32px;">
    Preferences</a></h3>
<!--a href="rmctrl-preferences.jsp" target="rightPage" class="leftlink"><img src="img/configure.png" style="border:0px;height:32px;width:32px;"> Preferences</a-->
Set preferences to specifically meet your needs.
<br>

<h3><a href="rmctrl-underconstruction.jsp" target="body" class="leftlink"><img src="img/support.png"
                                                                               style="border:0px;height:32px;width:32px;">
    Help</a></h3>
All the resources you need to take full advantage of Remote Cellink.
<hr>
</body>
</html>
