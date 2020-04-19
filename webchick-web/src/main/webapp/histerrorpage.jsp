<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page isErrorPage="true" %>

<script>
    function goBack() {
        window.history.back();
    }
</script>
<html dir="<%=session.getAttribute("dir")%>">

    <head>
          <title>There is no history</title>
          <link rel="shortcut icon" href="resources/images/favicon.ico">
    </head>
    <style>
        h2 {
            color: #3A8B41;
        }
    </style>

    <body>
          <h2> There is no history data. </h2>
          <button onclick="goBack()">Go Back</button>

    </body>
</html>
