<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.CellinkDto"/>
<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>
<jsp:directive.page import="com.agrologic.app.web.CellinkState"/>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    UserDto editUser = (UserDto) request.getSession().getAttribute("edituser");
    Long userId = Long.parseLong(request.getParameter("userId"));
    List<CellinkDto> cellinks = editUser.getCellinks();
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    CellinkDto cellink = findCellinkToEdit(cellinks, cellinkId);
    List<ControllerDto> controllers = cellink.getControllers();
%>
<%! UserDto findUserToEdit(List<UserDto> users, Long userId) {
    for (UserDto u : users) {
        if (u.getId().equals(userId)) {
            return u;
        }
    }
    return null;
}
%>

<%! CellinkDto findCellinkToEdit(List<CellinkDto> cellinks, Long cellinkId) {
    for (CellinkDto c : cellinks) {
        if (c.getId().equals(cellinkId)) {
            return c;
        }
    }
    return null;
}
%>
<%! public UserDto getChoosedUser(List<UserDto> users, Long userId) {
    for (UserDto u : users) {
        if (u.getId().equals(userId)) {
            return u;
        }
    }
    return null;
}
%>

<%! public CellinkDto getChoosedCellink(List<CellinkDto> cellinks, Long cellinkId) {
    for (CellinkDto c : cellinks) {
        if (c.getId().equals(cellinkId)) {
            return c;
        }
    }
    return null;
}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"
      lang="en">
<head>
    <title><%=session.getAttribute("label.cellink.setting.title")%>
    </title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <script type="text/javascript" language="javascript" src="js/general.js"></script>
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
        function activate(userId, cellinkId, controllerId, active) {
            window.document.location.replace("./activatecontroller.html?userId=" + userId
                    + "&cellinkId=" + cellinkId
                    + "&controllerId=" + controllerId
                    + "&active=" + active
                    + "&url=cellink-setting.html");
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
            <td style="width: 100%">
                <form id="formControllers" name="formControllers">
                    <table style="border: 0px; border-style: solid; border-collapse: collapse;" width="100%">
                        <tr>
                            <td>
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
                                        <td>
                                            <table class="table-list" border="1" width="100%" cellpadding="2"
                                                   cellspacing="1">
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
                                                    <td bgcolor="#F3F3F3"><%=cellink.getIp() %>
                                                        : <%=cellink.getPort() %>
                                                    </td>
                                                    <td style="font-weight:bold">SIM Number</td>
                                                    <td bgcolor="#F3F3F3"><input type="text" name="simnumber"
                                                                                 value=" <%=cellink.getSimNumber() %>"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight:bold">Version</td>
                                                    <td bgcolor="#F3F3F3"><%=cellink.getVersion() %>
                                                    </td>
                                                    <td style="font-weight:bold">Type</td>
                                                    <td bgcolor="#F3F3F3"><%=cellink.getType() %>
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
                                        <td align="center" colspan="2">
                                            <%@include file="messages.jsp" %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" width="100%">
                                            <h2><%=session.getAttribute("table.caption.controller")%>
                                            </h2></td>
                                    </tr>
                                    <tr>
                                        <td width="100%">
                                            <input class="button" type="button"
                                                   value="<%=session.getAttribute("button.add.controller")%>"
                                                   onclick="addController(<%=editUser.getId() %>,<%=cellink.getId()%>);"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">
                                            <b><%=controllers.size()%>
                                            </b>&nbsp;<%=session.getAttribute("label.records")%>
                                            <table class="table-list" border=1
                                                   style="behavior:url(tablehl.htc) url(sort.htc);">
                                                <thead>
                                                <tr>
                                                    <th align="center"
                                                        nowrap><%=session.getAttribute("table.col.controller.title")%>
                                                    </th>
                                                    <th align="center"
                                                        nowrap><%=session.getAttribute("table.col.controller.name")%>
                                                    </th>
                                                    <th align="center"
                                                        nowrap><%=session.getAttribute("table.col.controller.netname")%>
                                                    </th>
                                                    <th align="center"
                                                        nowrap><%=session.getAttribute("table.col.controller.progversion")%>
                                                    </th>
                                                    <th align="center"
                                                        nowrap><%=session.getAttribute("table.col.controller.active")%>
                                                    </th>
                                                    <th align="center" nowrap colspan="2">
                                                        <span><%=session.getAttribute("table.col.controller.action")%></span>
                                                    </th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <% int rowCount = 0;%>
                                                <%for (ControllerDto controller : controllers) {%>
                                                <% if ((rowCount % 2) == 0) {%>
                                                <tr class="odd" onMouseOver="changeOdd(this);"
                                                    onmouseout="changeOdd(this)">
                                                        <%} else {%>
                                                <tr class="even" onMouseOver="changeEven(this);"
                                                    onmouseout="changeEven(this)">
                                                    <%}%>
                                                    <td align="center"><a
                                                            href="./editcontrollerrequest.html?userId=<%=editUser.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>"><%=controller.getTitle()%>
                                                    </a></td>
                                                    <td align="center"><%=controller.getName()%>
                                                    </td>
                                                    <td align="center"><%=controller.getNetName()%>
                                                    </td>
                                                    <td align="center">
                                                        <%if (user.getRole() != UserRole.REGULAR) {%>
                                                        <a href="./all-screens.html?programId=<%=controller.getProgramId() %>">
                                                            <%=((ProgramDto) controller.getProgram()).getName() %>
                                                        </a>
                                                        <%} else {%>
                                                        <%=((ProgramDto) controller.getProgram()).getName() %>
                                                        <%}%>
                                                    </td>
                                                    <td align="center">
                                                        <%if (controller.isActive()) {%>
                                                        <input type="checkbox" name="Nactive" id="Nactive" checked
                                                               onclick="activate(<%=userId%>, <%=cellinkId%>, <%=controller.getId() %>, 'OFF')"/>
                                                        <%} else {%>
                                                        <input type="checkbox" name="Nactive" id="Nactive"
                                                               onclick="activate(<%=userId%>, <%=cellinkId%>, <%=controller.getId() %>, 'ON')"/>
                                                        <%}%>
                                                    </td>
                                                    <td align="center">
                                                        <img src="img/edit-9.png" style="cursor: pointer" border="0"
                                                             hspace="5"/>
                                                        <a href="./editcontrollerrequest.html?userId=<%=editUser.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>">
                                                            <%=session.getAttribute("button.edit")%>
                                                        </a>
                                                    </td>
                                                    <% if (user.getRole() != UserRole.REGULAR) {%>
                                                    <td align="center">
                                                        <img src="img/close.png" style="cursor: pointer" border="0"
                                                             hspace="5"/>
                                                        <a href="javascript:removeController('<%=editUser.getId()%>','<%=cellinkId%>','<%=controller.getId()%>');">
                                                            <%=session.getAttribute("button.delete")%>
                                                        </a>
                                                    </td>
                                                    <%}%>
                                                </tr>
                                                <%rowCount++;%>
                                                <%}%>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <%if (user.getRole() == UserRole.REGULAR) {%>
                                            <button name="btnCancel" type="button"
                                                    onclick='return back("./my-farms.html?userId=<%=editUser.getId() %>");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <%} else {%>
                                            <button name="btnCancel" type="button"
                                                    onclick='return back("./overview.html?userId=<%=editUser.getId() %>");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <%}%>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
