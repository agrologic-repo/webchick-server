<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.Flock" %>
<%@ page import="java.util.Collection" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Controller editController = (Controller) request.getAttribute("controller");
    Flock flock = getFlock(editController.getFlocks(), flockId);
    session.setAttribute("flock", flock);
%>
<%! private Flock getFlock(Collection<Flock> flocks, Long flockId) {
    for (Flock f : flocks) {
        if (f.getFlockId().equals(flockId)) {
            return f;
        }
    }
    return null;
}
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("flock.page.title")%>
    </title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <script src="resources/javascript/jquery.js"></script>
    <script src="resources/javascript/jquery-ui.js"></script>
    <script>
        $(function () {
            var currentTabIndex = "0";
            $("#tabs").tabs({
                active: localStorage.getItem('tab-index'),
                activate: function (e, ui) {
                    currentTabIndex = ui.newTab.index().toString();
                    localStorage.setItem('tab-index', currentTabIndex);
                },
                beforeLoad: function (event, ui) {
                    ui.jqXHR.error(function () {
                        ui.panel.html('<img src="resources/images/loading2.gif" width="24" height="24" style="vertical-align:middle;"> Loading...');
                    });
                }
            });
        });
    </script>
</head>
<body>

<table width="100%">
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <table width="85%" border="0">
                    <tr>
                        <td width="20%">
                            <%@include file="toplang.jsp" %>
                        </td>
                        <td align="center">
                            <h1 style="text-align: center;"><%=session.getAttribute("flock.page.title")%>
                            </h1>
                        </td>
                        <td width="20%">
                            <a href="rmctrl-main-screen.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1">
                            <img src="resources/images/display.png" style="cursor: pointer" border="0"/>
                                &nbsp;<%=session.getAttribute("button.screens")%>&nbsp;
                            </a>
                            <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                                <img src="resources/images/chicken-icon.png" style="cursor: pointer" border="0"/>
                                <%=session.getAttribute("main.screen.page.flocks")%>
                            </a>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td align="center" width="85%">
            <div id="tabs">
                <ul>
                    <li><a href="#tabs-1"><%=session.getAttribute("manage.page.tab.houses")%>
                    </a></li>
                    <li><a href="#tabs-2"><%=session.getAttribute("manage.page.tab.begin")%>
                    </a></li>
                    <li><a href="#tabs-3"><%=session.getAttribute("manage.page.tab.expenses")%>
                    </a></li>
                    <li><a href="#tabs-4"><%=session.getAttribute("manage.page.tab.distribute")%>
                    </a></li>
                    <%if (editController.getHouseType().equals("Layer")) {%>
                    <li><a href="#tabs-5"><%=session.getAttribute("manage.page.tab.eggmanager")%>
                    </a></li>
                    <li><a href="#tabs-6"><%=session.getAttribute("manage.page.tab.summary")%>
                    </a></li>
                    <%} else {%>
                    <li><a href="#tabs-5"><%=session.getAttribute("manage.page.tab.summary")%>
                    </a></li>
                    <%}%>
                </ul>
                <div id="tabs-1">
                    <jsp:include page="flock-houses.jsp"/>
                </div>

                <div id="tabs-2">
                    <jsp:include page="flock-begin.jsp"/>
                </div>
                <div id="tabs-3">
                    <jsp:include page="flock-expanses.jsp"/>
                </div>
                <div id="tabs-4">
                    <jsp:include page="flock-distibute.jsp"/>
                </div>
                <%if (editController.getHouseType().equals("Layer")) {%>
                <div id="tabs-5">
                    <jsp:include page="flock-eggs.jsp"/>
                </div>
                <div id="tabs-6">
                    <jsp:include page="flock-summary.jsp"/>
                </div>
                <%} else {%>
                <div id="tabs-5">
                    <jsp:include page="flock-summary.jsp"/>
                </div>
                <%}%>
            </div>
        </td>
    </tr>
</table>
</body>
</html>

