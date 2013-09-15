<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.Controller" %>

<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="java.util.Collection"/>

<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Collection<User> users = (Collection<User>) request.getSession().getAttribute("users");
    Long userId = Long.parseLong(request.getParameter("userId"));
    User selectedUser = getChoosedUser(users, userId);

    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Cellink cellink = getChoosedCellink(selectedUser.getCellinks(), cellinkId);

    Collection<Controller> controllers = cellink.getControllers();
%>
<%! public User getChoosedUser(Collection<User> users, Long userId) {
    for (User u : users) {
        if (u.getId().equals(userId)) {
            return u;
        }
    }
    return null;
}
%>

<%! public Cellink getChoosedCellink(Collection<Cellink> cellinks, Long cellinkId) {
    for (Cellink c : cellinks) {
        if (c.getId().equals(cellinkId)) {
            return c;
        }
    }
    return null;
}
%>

<%! public Collection<Controller> filterCellinkControllers(Collection<Controller> controllers, Long cellinkId) {
    if (cellinkId == null || cellinkId == 0) {
        return controllers;
    }
    Collection<Controller> filteredControllers = new ArrayList<Controller>();
    for (Controller c : controllers) {
        if (c.getCellinkId().equals(cellinkId)) {
            filteredControllers.add(c);
        }
    }
    return filteredControllers;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>">
<head>
    <title>Controllers</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="SHORTCUT ICON" HREF="img/favicon5.ico">
    <link rel="StyleSheet" type="text/css" href="css/menubar.css">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
    <script type="text/javascript">
        function addController(userId, cellinkId) {
            window.document.location.replace("./add-controller.jsp?userId=" + userId + "&cellinkId=" + cellinkId);
            return false;
        }
        function removeController(userId, cellinkId, controllerId) {
            if (confirm("Are you sure ?") == true) {
                window.document.location.replace("./removecontroller.html?controllerId=" + controllerId)
            }
        }
        function filterUsers() {
            window.document.location.replace("./all-controllers.jsp?" + "userId=" + document.formFilterUsers.User_Filter.value);
            return false;
        }
        function filterFarms() {
            window.document.location.replace("./all-controllers.html?userId=<%=selectedUser.getId()%>&cellinkId=" + document.formFilterFarms.Cellink_Filter.value);
            return false;
        }
    </script>
</head>
<body>
<div class="shell">
    <%@include file="usermenuontop.jsp" %>
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td width="200">
                <h1><%=session.getAttribute("label.controllers") %>
                </h1>
            </td>
            <td width="390" align="right">
                <button id="btnAdd" name="btnAdd" onclick="addController(<%=userId%>,<%=cellinkId%>);">
                    <img src="img/plus1.gif"/><%=session.getAttribute("button.add.controller")%>
                </button>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="3">
                <%@include file="messages.jsp" %>
            </td>
        </tr>
        <tr>
            <td colSpan=2 width="600">
                <p>

                <h2><%=controllers.size()%>&nbsp;<%=session.getAttribute("label.records")%>
                </h2>

                <form id="formControllers" name="formControllers">
                    <table class="table-list" width="600px" bgColor=white border=1 borderColor=black
                           style="behavior:url(tablehl.htc) url(sort.htc);">
                        <thead>
                        <tr>
                            <th align="center" width="120px"
                                nowrap><%=session.getAttribute("table.col.controller.title")%>
                            </th>
                            <th align="center" width="100px"
                                nowrap><%=session.getAttribute("table.col.controller.name")%>
                            </th>
                            <th align="center" width="120px"
                                nowrap><%=session.getAttribute("table.col.controller.netname")%>
                            </th>
                            <th align="center" width="200px"
                                nowrap><%=session.getAttribute("table.col.controller.progversion")%>
                            </th>
                            <th align="center" width="100px" nowrap>
                                <span><%=session.getAttribute("table.col.controller.action")%></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <% int rows = 0;
                            for (Controller controller : controllers) {%>
                        <%if ((rows % 2) == 0) {%>
                        <tr class="even">
                                <%} else {%>
                        <tr class="odd">
                            <%}%>
                            <td align="center"><%=controller.getTitle()%>
                            </td>
                            <td align="center"><%=controller.getName()%>
                            </td>
                            <td width="" align="center"><%=controller.getNetName()%>
                            </td>
                            <td width="" align="center"><%=controller.getProgramId() %>
                            </td>
                            <td width="" align="center">
                                <img src="img/edit.gif" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.edit.controller")%>" border="0"
                                     onclick="window.document.location='edit-controller.jsp?userId=<%=selectedUser.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>'">
                                <img src="img/delete123.gif" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.delete.controller")%>" border="0"
                                     onclick="javascript:removeController('<%=controller.getId()%>')">
                            </td>
                        </tr>
                        <% rows++;%>
                        <%}%>
                        </tbody>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>

</body>
</html>