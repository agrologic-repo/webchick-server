<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>

<%
    UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/strict.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Under Construction</title>
        <LINK REL="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Tm.">
        <link rel="stylesheet" type="text/css" href="css/rmtstyle.css"/>
    </head>
    <body>
        <br>
        <h2>Under Construction</h2>
        <hr>
        <h3>Requested page is currently under construction.</h3>
        <br><br>
        <img src="img/under_construction.jpg">
        <p>Please check back soon!</p>
        <hr>
    </body>
</html>