<%--
    Document   : test1
    Created on : Sep 2, 2012, 8:57:08 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="windows-1255"%>
<%
    request.setAttribute("MyName", "Luk");
    response.sendRedirect("test2.jsp");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
