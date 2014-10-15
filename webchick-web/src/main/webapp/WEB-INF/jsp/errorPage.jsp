<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title></title>
    <style>
        body, p {
            font-family: Tahoma;
            font-size: 10pt;
            padding-left: 30px;
        }

        pre {
            font-size: 8pt;
        }
    </style>

</head>
<body>
<font><h1>Exception Event Occurred</h1>

    <h3><p>We're sorry, an eccurred processing your request.</p>

        <p><font color="red">You got a <%=exception.toString() %>
            <%exception.printStackTrace(); %></font></p></h3>
</font>
</body>
</html>