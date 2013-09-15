<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.Controller" %>

<jsp:directive.page import="com.agrologic.app.model.Program"/>
<jsp:directive.page import="com.agrologic.app.web.CellinkState"/>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="java.util.Collection"/>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    User editUser = (User) request.getSession().getAttribute("edituser");
    Long userId = Long.parseLong(request.getParameter("userId"));
    Collection<Cellink> cellinks = editUser.getCellinks();
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Cellink cellink = findCellinkToEdit(cellinks, cellinkId);
    Collection<Controller> controllers = cellink.getControllers();
%>


<%! User findUserToEdit(Collection<User> users, Long userId) {
    for (User u : users) {
        if (u.getId().equals(userId)) {
            return u;
        }
    }
    return null;
}
%>

<%! Cellink findCellinkToEdit(Collection<Cellink> cellinks, Long cellinkId) {
    for (Cellink c : cellinks) {
        if (c.getId().equals(cellinkId)) {
            return c;
        }
    }
    return null;
}
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
<html dir="<%=(String)request.getSession().getAttribute("dir")%>" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"
      lang="en">
<head>
    <title>Edit Cellink</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <style>
        tr.unselected {
            background: white;
            color: black;
        }

        tr.selected {
            background: orange;
            color: white;
        }
    </style>

    <script type="text/javascript">
        function addController(userId, cellinkId) {
            window.document.location.replace("./add-controller.jsp?userId=" + userId + "&cellinkId=" + cellinkId);
            return false;
        }
        function confirmRemove() {
            return confirm("This action will remove controller from database\nDo you want to continue?");
        }
        function removeController(userId, cellinkId, controllerId) {
            if (confirm("This action will remove controller from database\nDo you want to continue ?") == true) {
                window.document.location.replace("./removecontroller.html?userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + controllerId)
            }
        }
        function validate() {
            if (document.editForm.Ncellinkname.value == "") {
                alert('Enter user name');
                document.editForm.Ncellinkame.focus();
                return false;
            } else if (document.editForm.Npassword.value == "") {
                alert('Enter password');
                document.editForm.Npassword.focus();
                return false;
            } else {
                document.editForm.submit();
            }
        }
        function back(link) {
            window.document.location.replace(link);
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
        <tr>
            <td valign="top" height="648px" width="100%">
                <form id="formControllers" name="formControllers">
                    <table style="border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;"
                           width="100%">
                        <h1><%=session.getAttribute("label.cellink.setting.title")%>
                        </h1>
                        <table style="border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;"
                               width="100%">
                            <tr>
                                <td>
                                    <h2><%=session.getAttribute("label.cellink.setting.title")%>
                                    </h2>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <img border="0" src="img/cellink1.jpg"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="100%">
                                    <table class="table-list" border=1 width="100%" cellpadding="2" cellspacing="1">
                                        <tbody>
                                        <tr>
                                            <td style="font-weight:bold">Name</td>
                                            <td bgcolor="#F3F3F3"><%=cellink.getName() %>
                                            </td>
                                            <td style="font-weight:bold">Password</td>
                                            <td bgcolor="#F3F3F3"><%=cellink.getPassword() %>
                                            </td>
                                            <td style="font-weight:bold">User ID</td>
                                            <td bgcolor="#F3F3F3"><%=cellink.getUserId() %>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="font-weight:bold">Keep Alive Time</td>
                                            <td bgcolor="#F3F3F3"><%=cellink.getTime() %>
                                            </td>
                                            <td style="font-weight:bold">IP and Port</td>
                                            <td bgcolor="#F3F3F3"><%=cellink.getIp() %> : <%=cellink.getPort() %>
                                            </td>
                                            <td style="font-weight:bold">SIM Number</td>
                                            <td bgcolor="#F3F3F3"><input type="text" name="simnumber"
                                                                         value=" <%=cellink.getSimNumber() %>"/></td>
                                        </tr>
                                        <tr>
                                            <td style="font-weight:bold">Version</td>
                                            <td bgcolor="#F3F3F3"><%=cellink.getVersion() %>
                                            </td>
                                            <td style="font-weight:bold">Type</td>
                                            <td bgcolor="#F3F3F3">
                                                <select id="cellinkType" name="cellinkType">
                                                    <option value="" selected></option>
                                                    <option value="WEB">WEB</option>
                                                    <option value="PC">PC</option>
                                                    <option value="PC&WEB">PC&WEB</option>
                                                    <option value="VIRTUAL">VIRTUAL</option>
                                                </select>
                                            </td>
                                            <td style="font-weight:bold">State</td>
                                            <td align="center"
                                                bgcolor="<%=CellinkState.getCellinkStateBGColor(cellink.getState()) %>"
                                                style="font-weight: bold;color:<%=CellinkState.getCellinkStateColor(cellink.getState()) %>"><%=cellink.getCellinkState()%>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3">
                                    <%@include file="messages.jsp" %>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" width="100%"><h2>controller list</h2></td>
                            </tr>
                            <tr>
                                <td>
                                    <input class="button" type="button"
                                           value="<%=session.getAttribute("button.add.controller")%>"
                                           onclick="addController(<%=editUser.getId() %>,<%=cellink.getId()%>);"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <p><b><%=controllers.size()%>
                                    </b>&nbsp;<%=session.getAttribute("label.records")%>
                                    </p>
                                    <table class="table-list" border=1 style="behavior:url(tablehl.htc) url(sort.htc);"
                                           width="100%">
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
                                            <th align="center" width="440px" nowrap colspan="2">
                                                <span><%=session.getAttribute("table.col.controller.action")%></span>
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <%for (Controller controller : controllers) {%>
                                        <tr onmouseover="this.style.background='#CEDEF4'"
                                            onmouseout="this.style.background='white'" title="Click for details">
                                            <td align="center"><a
                                                    href="./editcontrollerrequest.html?userId=<%=editUser.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>"><%=controller.getTitle()%>
                                            </a></td>
                                            <td align="center"><%=controller.getName()%>
                                            </td>
                                            <td align="center"><%=controller.getNetName()%>
                                            </td>
                                            <td align="center"><a
                                                    href="./all-screens.html?programId=<%=controller.getProgramId() %>"><%=((Program) controller.getProgram()).getName() %>
                                            </a></td>
                                            <td align="center">
                                                <a href="./editcontrollerrequest.html?userId=<%=editUser.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>">
                                                    <img src="img/edit.gif" style="cursor: pointer" border="0"
                                                         hspace="5"/><%=session.getAttribute("button.edit")%>
                                                </a>
                                            </td>
                                            <td align="center">
                                                <a href="javascript:removeController('<%=editUser.getId()%>','<%=cellinkId%>','<%=controller.getId()%>');"
                                                   onclick="return confirmRemove();">
                                                    <img src="img/close.png" style="cursor: pointer" border="0"
                                                         hspace="5"/><%=session.getAttribute("button.delete")%>
                                                </a>
                                            </td>
                                        </tr>
                                        <%}%>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <button id="btnBack" name="btnBack"
                                            onclick='return back("./userinfo.html?userId=<%=editUser.getId() %>");'><%=session.getAttribute("button.back") %>
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </table>
                </form>
            </td>
        </tr>
        <!--Copyright area-->
    </table>
</div>

</body>
</html>