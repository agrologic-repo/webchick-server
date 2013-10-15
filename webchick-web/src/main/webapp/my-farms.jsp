<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.CellinkState" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    request.setAttribute("role", user.getRole());
    Collection<Cellink> cellinks = (Collection<Cellink>) request.getAttribute("cellinks");
%>

<%! int countCellinksByState(Collection<Cellink> cellinks, int state) {
    int count = 0;
    for (Cellink cellink : cellinks) {
        if (cellink.getState() == state) {
            count++;
        }
    }
    return count;
}
%>
<%! Collection<Cellink> getCellinksByState(Collection<Cellink> cellinks, int state) {
    if (state == -1) {
        return cellinks;
    }
    Collection<Cellink> cellinkList = new ArrayList<Cellink>();
    for (Cellink cellink : cellinks) {
        if (cellink.getState() == state) {
            cellinkList.add(cellink);
        }
    }
    return cellinkList;
}
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("myfarms.page.title") %>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function disconnectCellink(cellinkId) {
            if (!confirm("Are you sure ?")) {
                return;
            }
            window.location.replace("./disconnect-cellink.html?userId=<%=user.getId()%>&cellinkId=" + cellinkId);
        }
    </script>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td colspan="2" width="100%">
                <h1><%=session.getAttribute("myfarms.page.header") %>
                </h1>

                <h2><%=session.getAttribute("myfarms.page.sub.header")%>
                </h2>
            </td>
        </tr>
        <%for (Cellink cellink : cellinks) {%>
        <tr>
            <td width="100%">
                <form id="formFarms" name="formFarms" style="display:inline">
                    <table id=""
                           style="border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;"
                           width="100%">
                        <tr>
                            <td colspan="2">
                                <%if (cellink.isOnline()) {%>
                                <table id="farms-online" border=0 cellpadding="10" cellspacing="10">
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <img class="link" src="resources/images/chicken_house1.jpg"
                                                 onclick="javascript:window.location.href='./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>' "/>
                                        </td>
                                        <td align="center">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h1><%=cellink.getName()%>
                                                        </h1>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <a class="button"
                                                           href="./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>&role=<%=user.getRole() %>"><%=session.getAttribute("button.show.houses") %>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table class="table-list-small" border="0">
                                                <tr>
                                                    <td>
                                                        <hr/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><%=session.getAttribute("label.cellink.version") %> <%=cellink.getVersion()%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="left"><%=session.getAttribute("label.cellink.status.online") %>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table class="table-list">
                                                            <tr>
                                                                <%if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                                                                <td>
                                                                    <img src="resources/images/disconnect.png"
                                                                         style="cursor: pointer" border="0" hspace="5"/>
                                                                    <a href="#"
                                                                       onclick="disconnectCellink(<%=cellink.getId() %>)">
                                                                        <%=session.getAttribute("button.disconnect.cellink")%>
                                                                    </a>
                                                                </td>
                                                                <%}%>
                                                                <td>
                                                                    <img src="resources/images/setting.png"
                                                                         style="cursor: pointer" border="0" hspace="5"/>
                                                                    <a href="./cellink-setting.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                                                        <%=session.getAttribute("button.setting")%>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <img src="resources/images/refresh.gif" style="cursor: pointer"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./my-farms.html?userId=<%=cellink.getUserId()%>">
                                                                        <%=session.getAttribute("button.refresh")%>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <%} else {%>
                                <table id="farms-offline" border=0 cellpadding="10" cellspacing="10">
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <img class="link" src="resources/images/chicken_house1.jpg"/>
                                        </td>
                                        <td align="center">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h1><%=cellink.getName()%>
                                                        </h1>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table class="table-list-small" border="0">
                                                <tr>
                                                    <td>
                                                        <hr/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><%=session.getAttribute("label.cellink.version") %> <%=cellink.getVersion()%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><%=session.getAttribute("label.cellink.offline.since") %>
                                                        <%=cellink.getFormatedTime() %>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table class="table-list">
                                                            <tr>
                                                                <td>
                                                                    <img src="resources/images/setting.png"
                                                                         style="cursor: pointer"
                                                                         title="<%=session.getAttribute("button.connect.cellink")%>"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./cellink-setting.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>"><%=session.getAttribute("button.setting")%>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <img src="resources/images/refresh.gif" style="cursor: pointer"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./my-farms.html?userId=<%=cellink.getUserId()%>"><%=session.getAttribute("button.refresh")%>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>

                                <%}%>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <hr>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        <%}%>

    </table>
</div>

</body>
</html>