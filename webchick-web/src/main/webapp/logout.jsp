<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <script language="JavaScript">
        function redirect() {
            window.location = "login.jsp";
        }
    </script>
</head>
<body onload="redirect()">
</body>
</html>