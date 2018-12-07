<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Integer fromDay = -1;
    Integer toDay = -1;
    try {
        fromDay = Integer.parseInt(request.getParameter("fromDay"));
        if (fromDay == null) {
            fromDay = -1;
        }
        toDay = Integer.parseInt(request.getParameter("toDay"));
        if (toDay == null) {
            toDay = -1;
        }
    } catch (Exception ex) {

    }
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title></title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="resources/style/calendar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/multiopt.css"/>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
</head>
<body>

<div id="loading" style="display: none;"></div>
<table cellpadding="1" cellspacing="1" border="0" width="100%">
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 100%">
                <h1><%=session.getAttribute("history.table.page.title")%>
                </h1>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 100%">
                <h2></h2>
                <table border="0" width="100%">
                    <tr>
                        <td width="100%" align="center">
                            <form action="./rmctrl-flockhistory-table.jsp" style="display: inline-block">
                                <input type="hidden" name="flockId" value="<%=flockId%>">
                                <%=session.getAttribute("label.show.range")%>&nbsp;
                                <%if (fromDay == -1 || toDay == -1) {%>
                                <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                 name="fromDay"
                                                                                 style="display: inline;"/>
                                <%=session.getAttribute("label.to")%> : <input type="text" size="5"
                                                                               name="toDay"
                                                                               style="display: inline;"/>
                                <%} else {%>
                                <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                 name="fromDay"
                                                                                 value="<%=fromDay%>"
                                                                                 style="display: inline;"/>
                                <%=session.getAttribute("label.to")%> : <input type="text" size="5" name="toDay"
                                                                               value="<%=toDay%>"
                                                                               style="display: inline;"/>
                                <%}%>

                                <button id="btnGo" style="min-width: 80px">
                                    <%=session.getAttribute("button.apply")%>
                                </button>
                            </form>
                            <button id="btnClear" name="btnClear" style="min-width: 80px">
                                <%=session.getAttribute("button.clear")%>
                            </button>
                        </td>
                    </tr>
                </table>
                <br/>
                <jsp:include page="view-tablehistory.html"/>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td align="center">
            <p>
                <button id="btnClose" name="btnClose"
                        onclick='self.close()'><%=session.getAttribute("button.close")%>
                </button>
            </p>
        </td>
    </tr>
</table>
<script>
    $("#btnClear").click(function () {
        window.location.href = "./rmctrl-flockhistory-table.jsp?flockId=<%=flockId%>";
    });
</script>
</body>
</html>