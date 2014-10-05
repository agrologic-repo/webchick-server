<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<html dir="<%=session.getAttribute("dir")%>">

<head>
    <link rel="StyleSheet" type="text/css" href="${pageContext.request.contextPath}/resources/style/admincontent.css"/>
    <decorator:head/>
</head>
<body>
    <decorator:body/>
</body>
</html>