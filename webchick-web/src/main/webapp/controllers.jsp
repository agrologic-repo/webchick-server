<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.User" %>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Collection<User> users = (Collection<User>) request.getAttribute("users");
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

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Controllers</title>


    <link rel="StyleSheet" type="text/css" href="resources/custom/style/menubar.css">
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css">
    <script type="text/javascript" src="resources/custom/javascript/general.js">;</script>
    <script type="text/javascript">
        function addController(userId, cellinkId) {
            redirect("./add-controller.jsp?userId=" + userId + "&cellinkId=" + cellinkId);
            return false;
        }
        function removeController(userId, cellinkId, controllerId) {
            if (confirm("Are you sure ?") == true) {
                redirect("./removecontroller.html?controllerId=" + controllerId)
            }
        }
        function filterUsers() {
            redirect("./all-controllers.jsp?" + "userId=" + document.formFilterUsers.User_Filter.value);
            return false;
        }
        function filterFarms() {
            redirect("./all-controllers.html?userId=<%=selectedUser.getId()%>&cellinkId=" + document.formFilterFarms.Cellink_Filter.value);
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
                    <img src="resources/custom/images/plus1.gif"/><%=session.getAttribute("button.add.controller")%>
                </button>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="3">
                <jsp:include page="messages.jsp"/>
            </td>
        </tr>
        <tr>
            <td colSpan=2 width="600">
                <p>

                <h2><%=controllers.size()%>&nbsp;<%=session.getAttribute("label.records")%>
                </h2>

                <form id="formControllers" name="formControllers">
                    <table class="table-list" width="600px" bgColor=white border=1 borderColor=black
                           >
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
                                <img src="resources/custom/images/edit.gif" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.edit.controller")%>" border="0"
                                     onclick="redirect('edit-controller.jsp?userId=<%=selectedUser.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>')">
                                <img src="resources/custom/images/delete123.gif" style="cursor: pointer"
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