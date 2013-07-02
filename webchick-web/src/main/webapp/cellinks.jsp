<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ page import="com.agrologic.app.model.CellinkDto" %>
<%@ page import="com.agrologic.app.web.CellinkState" %>
<%@ page import="java.util.Collection" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<UserDto> users = (Collection<UserDto>) request.getSession().getAttribute("users");
    String uid = request.getParameter("userId");
    Long userId = Long.parseLong(uid);
    UserDto selectedUser = getUserCellinks(users, userId);

    Collection<CellinkDto> cellinks = selectedUser.getCellinks();
%>
<%! UserDto getUserCellinks(Collection<UserDto> users, Long userId) {
    for (UserDto u : users) {
        if (u.getId() == userId) {
            return u;
        }
    }
    return null;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>">
<head>
    <title>Farms</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="shortcut icon" href="img/favicon5.ico">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
    <link rel="StyleSheet" type="text/css" href="css/menubar.css">
    <script type="text/javascript">
        function addCellink(userId) {
            window.document.location.replace("./add-cellink.jsp?userId=" + userId);
            return false;
        }
        function removeCellink(userId, cellinkId) {
            if (confirm("Are you sure ?") == true) {
                window.document.location.replace("./removecellink.html?userId=" + userId + "&cellinkId=" + cellinkId);
            }
        }
        function filterCellinks() {
            var status = document.formFilterCellinks.Status_Filter.selectedIndex
            window.document.location.replace("./all-cellinks.html?userId=<%=selectedUser.getId()%>&status=" + status);
            return false;
        }
        function filterUsers() {
            var userId = document.formFilterUsers.User_Filter.value;
            if (userId == 0) {
                window.document.location.replace("./all-cellinks.html");
            } else {
                window.document.location.replace("./all-cellinks.html?userId=" + userId);
            }
            return false;
        }
    </script>
</head>
<body>
<table width="1024px" cellpadding="0" cellspacing="0" style="padding:0px;" align="center">
    <tr>
        <td width="100%" colspan="2">
            <%@include file="usermenuontop.jsp" %>
        </td>
    </tr>
    <tr>
        <td valign="top" height="648px">
            <table border=0 cellPadding=1 cellSpacing=1 width="600">
                <tr>
                    <td style="font-size:x-small;font-weight:normal;" nowrap>
                        <a href="./main.jsp">Home</a>&nbsp;>&nbsp;
                        <a href="./all-users.html">Setup</a>&nbsp;>&nbsp;
                        <a href="./all-cellinks.jsp?userId=<%=selectedUser.getId() %>">User Info</a>
                    </td>
                </tr>
                <tr>
                    <td width="200">
                        <p>

                        <h1><%=session.getAttribute("label.cellinks") %>
                        </h1></p>
                    </td>
                    <td width="300" align="right">
                        <p>
                            <button onclick="addCellink(<%=selectedUser.getId()%>);"><img
                                    src="img/plus1.gif"><%=session.getAttribute("button.add.cellink")%>
                            </button>
                        </p>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="3">
                        <%@include file="messages.jsp" %>
                    </td>
                </tr>
                <tr>
                    <td colSpan=2 width="600px">
                        <p>

                        <h2><%=cellinks.size()%>&nbsp;<%=session.getAttribute("label.records") %>
                        </h2>

                        <form id="formFarms" name="formFarms">
                            <table class="table-list" width="600px" bgColor=white border=1 borderColor=black
                                   style="behavior:url(tablehl.htc) url(sort.htc);">
                                <%if (cellinks.size() != 0) {%>
                                <thead>
                                <tr>
                                    <th align="center" width="50px"><%=session.getAttribute("table.col.cellink.id")%>
                                    </th>
                                    <th align="center" width="150px"><%=session.getAttribute("table.col.cellink.name")%>
                                    </th>
                                    <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
                                    <th align="center"
                                        width="100px"><%=session.getAttribute("table.col.cellink.password")%>
                                    </th>
                                    <%}%>
                                    <th align="center"
                                        width="150px"><%=session.getAttribute("table.col.cellink.status")%>
                                    </th>
                                    <th align="center" width="150px">
                                        <span><%=session.getAttribute("table.col.cellink.action")%></span></th>
                                </tr>
                                </thead>
                                <tbody>
                                <%
                                    int rows = 0;
                                    for (CellinkDto cellink : cellinks) {
                                        if ((rows % 2) == 0) {%>
                                <tr class="even">
                                        <%} else {%>
                                <tr class="odd">
                                    <%}%>
                                    <td><%=cellink.getId()%>
                                    </td>
                                    <td align="left" style="padding-left:10px;"><%=cellink.getName()%>
                                    </td>
                                    <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
                                    <td><%=cellink.getPassword()%>
                                    </td>
                                    <%}%>
                                    <%if (CellinkState.OFFLINE != cellink.getCellinkState()) {%>
                                    <td align="center"
                                        bgcolor="<%=CellinkState.getCellinkStateColor(CellinkState.STATE_ONLINE) %>">
                                        <%=session.getAttribute("cellink.state." + CellinkState.intToState(CellinkState.STATE_ONLINE)) %>
                                    </td>
                                    <%} else {%>
                                    <td align="center"
                                        bgcolor="<%=CellinkState.getCellinkStateColor(CellinkState.STATE_OFFLINE) %>">
                                        <%=session.getAttribute("cellink.state." + CellinkState.intToState(CellinkState.STATE_OFFLINE)) %>
                                    </td>
                                    <%}%>
                                    <td width="auto" align="center">
                                        <img src="img/edit.gif" style="cursor: pointer"
                                             title="<%=session.getAttribute("button.edit.cellink")%>" border="0"
                                             onclick="window.document.location='edit-cellink.jsp?userId=<%= selectedUser.getId()%>&cellinkId=<%=cellink.getId()%>'">
                                        &nbsp;<img src="img/delete123.gif" style="cursor: pointer"
                                                   title="<%=session.getAttribute("button.delete.cellink")%>" border="0"
                                                   onclick="javascript:removeCellink('<%=selectedUser.getId()%>','<%=cellink.getId()%>');">
                                        &nbsp;<img src="img/chicken-icon.png" style="cursor: pointer"
                                                   title="<%=session.getAttribute("button.connect.cellink")%>"
                                                   border="0"
                                                   onclick="window.document.location='rmtctrl-main.html?cellinkId=<%=cellink.getId()%>'">
                                        &nbsp;<img src="img/info.gif" style="cursor: pointer"
                                                   title="<%=session.getAttribute("button.info.cellink")%>" border="0"
                                                   onclick="window.document.location='./cellinkinfo.jsp?userId=<%=selectedUser.getId()%>&cellinkId=<%=cellink.getId() %>'">
                                    </td>
                                </tr>
                                <% rows++;%>
                                <%}%>
                                </tbody>
                                <%}%>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>
</body>
</html>

