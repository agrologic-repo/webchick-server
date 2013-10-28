<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.User" %>

<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>

    <title>Under Construction</title>

    <link rel="stylesheet" type="text/css" href="resources/style/rmtstyle.css"/>
</head>
<body>
<br>

<h2>Under Construction</h2>
<hr>
<h3>Requested page is currently under construction.</h3>
<br><br>
<img src="resources/images/under_construction.jpg">

<p>Please check back soon!</p>
<hr>
</body>
</html>