<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="errorPage.jsp" %>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <style>
        div, input, textarea {
            font-family: Tahoma;
            font-size: 8pt;
        }

        input.std {
            width: 200px;
        }

        div.frame {
            padding-left: 70px;
            color: green;
        }
    </style>
</head>
<body>
<%--
<div class="frame">
<jsp:useBean id="mailer" class="com.agrologic.web.MailerBean">
    <jsp:setProperty name="mailer" property="*"/>
    <%-- mailer.sendMail();
</jsp:useBean>
Email has been sent successfully.
</div>
--%>
</body>
</html>