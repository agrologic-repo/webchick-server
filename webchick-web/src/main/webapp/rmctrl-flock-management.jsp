<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.Flock" %>
<%@ page import="java.util.Collection" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Controller editController = (Controller) request.getAttribute("controller");
    Flock flock = getFlock(editController.getFlocks(), flockId);
    session.setAttribute("flock",flock);
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
<html>
<head>
<title><%=session.getAttribute("flock.page.title")%>></title>
<link rel="stylesheet" href="resources/style/jquery-ui.css" />
<script src="resources/javascript/jquery.js"></script>
<script src="resources/javascript/jquery-ui.js"></script>
<script>
    $(function() {
        $( "#tabs" ).tabs({
            beforeLoad: function( event, ui ) {
                ui.jqXHR.error(function() {
                    ui.panel.html(
                            "Couldn't load this tab. We'll try to fix this as soon as possible. " +
                                    "If this wouldn't be a demo." );
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
                        <a href="rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1">
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
            <li><a href="#tabs-1">Houses</a></li>
            <li><a href="#tabs-2">Begin</a></li>
            <li><a href="#tabs-3">Expanses</a></li>
            <li><a href="#tabs-4">Distribute</a></li>
            <li><a href="#tabs-5">Summary</a></li>
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
        <div id="tabs-5">
            <jsp:include page="flock-summary.jsp"/>
        </div>
    </div>
</td>
</tr>
</table>
</body>
</html>

