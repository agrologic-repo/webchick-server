<%--
    Document   : unavailable
    Created on : Oct 3, 2011, 11:03:41 AM
    Author     : Administrator
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="disableCaching.jsp" %>

<%@ page isErrorPage = "true"%>
<%@ page import = "java.io.*"%>
<html>
<head>
<title>Server Application Unavailable</title>
<LINK REL="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Tm.">
<style type="text/css">
.errmsg
{
	margin-top:12;
	width:80%;
	background-color:#FFEEEE;
	border-width: 1;
	border-style: solid;
	border-color: red;
	padding-top:6;
	padding-bottom:6;
}
</style>
</head>
<body>
<h2>Server Application Unavailable</h2>
<div class="errmsg">
<b>Service Temporarily Unavailable</b>
<p>The server is temporarily unable to service your
request due to maintenance downtime or capacity
problems. Please try again later.</p>
</div></body>
</html>


