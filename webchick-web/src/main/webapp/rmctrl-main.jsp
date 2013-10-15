<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Cellink cellink = (Cellink) request.getAttribute("cellink");
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Cellink remote control - <%=cellink.getName()%>
    </title>
    <script language="JavaScript">
        if (window.name == "body" && window != parent) parent.location.replace(window.location);
    </script>



    <link rel="shortcut icon" href="favicon.ico">
</head>
<frameset cols="180px,*">
    <frame name="menuFrame" height="100%" id="menuFrame"
           src="rmctrl-menu.jsp?userId=<%=user.getId()%>&cellinkId=<%=cellink.getId()%>" scrolling="no" border="0"
           frameborder="0" framespacing="0" marginheight="0" marginwidth="0"/>
    <frame name="body" width="100%" height="100%"
           src="rmctrl-main-screen-ajax.jsp?userId=<%=user.getId()%>&cellinkId=<%=cellink.getId()%>&screenId=1"
           border="0" frameborder="0" framespacing="0" marginwidth="0" marginheight="0" scrolling="auto"/>
    <!--body style="margin: 0 0 0 0px">
        <table cellpadding=0 cellspacing=0 style="width: 100%; height: 100%">
        <tr>
            <td style="width: 12%; height: 100%; overflow: hidden; border-right: 1px solid #7182C8">
                <iframe name="menuFrame" src="rmctrl-menu.jsp?userId=<%--=user.getId()%>&cellinkId=<%=cellink.getId()--%>" marginheight="0" marginwidth="0" scrolling="no" frameborder="0" framespacing="0" border="0" id="menuFrame" style="width: 160px; height: 100%;"></iframe>
            <!--/td>
            <td style="width: 85%;  height: 100%;">
                <iframe width="100%" height="100%" name="body" src="rmctrl-content.jsp?cellinkId=<%--=cellink.getId()--%>" frameborder="0" framespacing="0" border="0" marginwidth="0" marginheight="0"  scrolling="auto" ></iframe>
            <!--/td>
        </tr>
        </table>
    </body-->
</frameset>
</html>
