<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
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
    <LINK REL="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Tm.">
</head>
<body>
<font><h1>Exception Event Occurred</h1>

    <h3><p>We're sorry, an eccurred processing your request.</p>

        <p><font color="red">You got a <%=exception.toString() %>
            <%exception.printStackTrace(); %></font></p></h3>
</font>
</body>
</html>