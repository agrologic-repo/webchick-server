<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<%-- this error page must be removed after moving all pages to WEB-INF\jsp --%>

<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title></title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
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