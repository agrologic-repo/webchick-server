<%@ page import="com.agrologic.app.model.Flock" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ include file="language.jsp" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Flock flock = (Flock) request.getAttribute("flock");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
</head>
<body>
<div>
    <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
    <input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>
</div>
<table width="100%">
    <tr>
        <td align="center" width="100%">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <table width="100%" border="0">
                    <tr>
                        <td wdith="90%" align="center">
                            <h1 style="text-align: center;"><%=session.getAttribute("history.graph.page.title")%>
                            </h1>

                            <h2 style="text-align: center;"><%=flockId%>

                        </td>
                        <td width="10%">
                            <table>
                                <tr>
                                    <td>
                                        <a href="./rmctrl-main-screen.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1">
                                            <img src="resources/images/display.png" style="cursor: pointer" hspace="5"
                                                 border="0" name="top"
                                                 target="_blank"/><%=session.getAttribute("button.screens")%>
                                        </a>
                                        <a href="./flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                                            <img src="resources/images/chicken-icon.png" style="cursor: pointer"
                                                 hspace="5"
                                                 border="0" name="top"
                                                 target="_blank"/><%=session.getAttribute("main.screen.page.flocks")%>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
</table>
</body>
</html>