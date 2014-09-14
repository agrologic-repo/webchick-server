<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.CellinkState" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    User editUser = (User) request.getAttribute("edituser");
    Collection<Cellink> cellinks = editUser.getCellinks();
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>User Info</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <style type="text/css">
        tr.unselected {
            background: white;
            color: black;
        }

        tr.selected {
            background: orange;
            color: white;
        }
    </style>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>

    <script type="text/javascript">
        function confrimRemove() {
            return confirm("This action will remove cellink from database\n Do you want to continue?");
        }
        function addCellink(userId) {
            redirect("./add-cellink.jsp?userId=" + userId);
            return false;
        }
        function removeCellink(userId, cellinkId) {
            if (confirm("This action will remove cellink from database\n Do you want to continue ?") == true) {
                redirect("./removecellink.html?userId=" + userId + "&cellinkId=" + cellinkId);
            }
        }
        function filterCellinks() {
            var status = document.formFilterCellinks.Status_Filter.selectedIndex
            redirect("./all-cellinks.html?userId=<%=editUser.getId()%>&status=" + status);
            return false;
        }
        function filterUsers() {
            var userId = document.formFilterUsers.User_Filter.value;
            if (userId == 0) {
                redirect("./all-cellinks.html");
            } else {
                redirect("./all-cellinks.html?userId=" + userId);
            }
            return false;
        }

    </script>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <h1><%=session.getAttribute("user.page.info.title")%>
        </h1>
        <table style="border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;"
               width="100%">
            <tr>
                <td>
                    <h2><%=session.getAttribute("user.page.info.title")%>
                    </h2>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <img src="resources/images/user1.png"/>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <a href="./edituserrequest.html?userId=<%=editUser.getId()%>">Edit user</a>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <table class="table-list" border=1 width="100%" cellpadding="2" cellspacing="1">
                        <input type="hidden" class=leftTitles name="Nuserid" value="<%=editUser.getId()%>"/>
                        <tbody>
                        <tr>
                            <td style="font-weight:bold"><%=session.getAttribute("user.login")%>
                            </td>
                            <td bgcolor="#F3F3F3"><%=editUser.getLogin()%>
                            </td>
                            <td style="font-weight:bold"><%=session.getAttribute("user.password")%>
                            </td>
                            <td bgcolor="#F3F3F3"><%=editUser.getPassword()%>
                            </td>
                        </tr>
                        <tr>
                            <td style="font-weight:bold"><%=session.getAttribute("user.name")%>
                            </td>
                            <td bgcolor="#F3F3F3"><%=editUser.getFirstName()%>&nbsp;<%=editUser.getLastName()%>
                            </td>
                            <td style="font-weight:bold"><%=session.getAttribute("user.role")%>
                            </td>
                            <td bgcolor="#F3F3F3">
                                <%if (editUser.getRole() == UserRole.ADMIN) {%>
                                <%=session.getAttribute("user.role.admin")%>
                                <%} else {%>
                                <%=session.getAttribute("user.role.regular")%>
                                <%}%>
                            </td>
                        </tr>
                        <tr>
                            <td style="font-weight:bold"><%=session.getAttribute("user.phone")%>
                            </td>
                            <td bgcolor="#F3F3F3"><%=editUser.getPhone()%>
                            </td>
                            <td style="font-weight:bold"><%=session.getAttribute("user.email")%>
                            </td>
                            <td bgcolor="#F3F3F3"><a href="mailto:<%=editUser.getEmail()%>"><%=editUser.getEmail()%>
                            </a></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <%@ include file="messages.jsp" %>
                </td>
            </tr>
            <tr>
                <td width="100%" colspan="5"><h2>cellink list</h2></td>
            </tr>
            <tr>
                <td>
                    <button onclick="addCellink(<%=editUser.getId()%>);"><%=session.getAttribute("button.add.cellink")%>
                    </button>
                </td>
            </tr>
            <tr>
                <td>
                    <p><b><%=cellinks.size()%>
                    </b>&nbsp;<%=session.getAttribute("label.records")%>
                    </p>

                    <form id="formFarms" name="formFarms">
                        <table class="table-list" border=1 width="100%">
                            <%if (cellinks.size() != 0) {%>
                            <thead>
                            <tr>
                                <th align="center" width="50px"><%=session.getAttribute("table.col.cellink.id")%>
                                </th>
                                <th align="center" width="150px"><%=session.getAttribute("table.col.cellink.name")%>
                                </th>

                                <%if (user.getRole() == UserRole.ADMIN) {%>
                                <th align="center" width="100px">
                                    <%=session.getAttribute("table.col.cellink.password")%>
                                </th>
                                <%}%>
                                <th align="center" width="120px"><%=session.getAttribute("table.col.cellink.sim")%>
                                </th>
                                <th align="center" width="80px"><%=session.getAttribute("table.col.cellink.status")%>
                                </th>
                                <th align="center" width="480px" colspan="5">
                                    <span><%=session.getAttribute("table.col.cellink.action")%></span></th>
                            </tr>
                            </thead>
                            <tbody>
                            <%for (Cellink cellink : cellinks) {%>
                            <tr onmouseover="this.style.background='#CEDEF4'" onmouseout="this.style.background='white'"
                                title="Click for details">
                                <td align="center"><%=cellink.getId()%>
                                </td>
                                <td align="center" style="padding-left:10px;"><a
                                        href='./cellink-setting.html?userId=<%=editUser.getId()%>&cellinkId=<%=cellink.getId()%>'><%=cellink.getName()%>
                                </td>
                                <%if (user.getRole() == UserRole.ADMIN) {%>
                                <td align="center" style="padding-left:10px;"
                                    onclick="redirect('./cellink-setting.html?userId=<%=editUser.getId()%>&cellinkId=<%=cellink.getId()%>')"><%=cellink.getPassword()%>
                                </td>
                                <%}%>
                                <td align="center" style="padding-left:10px;"><%=cellink.getSimNumber()%>
                                </td>
                                <%
                                    if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE
                                            || cellink.getCellinkState().getValue() == CellinkState.STATE_START
                                            || cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {
                                %>
                                <td align="center"
                                    bgcolor="<%=CellinkState.getCellinkStateBGColor(cellink.getState())%>"
                                    style="cursor: pointer" title="<%=session.getAttribute("button.connect.cellink")%>"
                                    onclick="redirect('./rmctrl-main-screen.html?userId=<%=editUser.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getName() %>&screenId=1')">
                                    <%=session.getAttribute("cellink.state." + CellinkState.intToState(CellinkState.STATE_ONLINE))%>
                                </td>
                                <%} else {%>
                                <td align="center"
                                    bgcolor="<%=CellinkState.getCellinkStateBGColor(cellink.getState())%>">
                                    <%=session.getAttribute("cellink.state." + CellinkState.intToState(CellinkState.STATE_OFFLINE))%>
                                </td>
                                <%}%>
                                <td align="center" valign="middle">
                                    <a href="rmctrl-main-screen.html?userId=<%=editUser.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getName() %>&screenId=1">
                                        <img src="resources/images/display.png" style="cursor: pointer" border="0"
                                             hspace="5"/>
                                        <%=session.getAttribute("button.connect")%>
                                    </a>

                                </td>
                                <td align="center" valign="middle">
                                    <a href="./cellink-setting.html?userId=<%=editUser.getId()%>&cellinkId=<%=cellink.getId()%>">
                                        <img src="resources/images/info.gif" style="cursor: pointer" border="0"
                                             hspace="5"/>
                                        <%=session.getAttribute("button.info")%>
                                    </a>
                                </td>
                                <td align="center" valign="middle">
                                    <a href="./editcellinkrequest.html?userId=<%= editUser.getId()%>&cellinkId=<%=cellink.getId()%>">
                                        <img src="resources/images/edit.gif" style="cursor: pointer" border="0"
                                             hspace="5"/>
                                        <%=session.getAttribute("button.edit")%>
                                    </a>
                                </td>
                                <td align="center" valign="middle">
                                    <a href="javascript:removeCellink(<%=editUser.getId()%>,<%=cellink.getId()%>);">
                                        <img src="resources/images/close.png" style="cursor: pointer" border="0"
                                             hspace="5"/>
                                        <%=session.getAttribute("button.delete")%>
                                    </a>
                                </td>
                            </tr>
                            <%}%>
                            </tbody>
                            <%}%>
                        </table>
                    </form>
                </td>
            </tr>
            <tr>
                <td>
                    <%if (user.getRole() == UserRole.USER) {%>
                    <button id="btnCancel" name="btnCancel"
                            onclick='return back("./my-farms.html?userId=<%=user.getId() %>");'>
                        <%=session.getAttribute("button.back") %>
                    </button>
                    <%} else {%>
                    <button id="btnCancel" name="btnCancel"
                            onclick='return back("./overview.html?userId=<%=user.getId() %>");'>
                        <%=session.getAttribute("button.back") %>
                    </button>
                    <%}%>
                </td>
            </tr>
        </table>
    </table>
</div>
</body>
</html>