<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Cellink editCellink = (Cellink) request.getAttribute("editCellink");
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("label.cellink.setting.title")%></title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function addController(userId, cellinkId) {
            redirect("./add-controller.jsp?userId=" + userId + "&cellinkId=" + cellinkId);
            return false;
        }
        function confirmRemove() {
            return confirm("This action will remove controller from database\nDo you want to continue?");
        }
        function removeController(userId, cellinkId, controllerId) {
            if (confirm("This action will remove controller from database\nDo you want to continue ?") == true) {
                redirect("./removecontroller.html?userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + controllerId)
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
        function activate(userId, cellinkId, controllerId, active) {
            redirect("./activatecontroller.html?userId=" + userId
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
                                            <img border="0" src="resources/images/cellink.jpg"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table class="table-list" border="1" width="100%" cellpadding="2"
                                                   cellspacing="1">
                                                <tbody>
                                                <tr>
                                                    <td style="font-weight:bold">Name</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getName() %>
                                                    </td>
                                                    <td style="font-weight:bold">Password</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getPassword() %>
                                                    </td>
                                                    <td style="font-weight:bold">User ID</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getUserId() %>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight:bold">Keep Alive Time</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getTime() %>
                                                    </td>
                                                    <td style="font-weight:bold">IP and Port</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getIp() %>
                                                        : <%=editCellink.getPort() %>
                                                    </td>
                                                    <td style="font-weight:bold">SIM Number</td>
                                                    <td bgcolor="#F3F3F3"><input type="text" name="simnumber"
                                                                                 value=" <%=editCellink.getSimNumber() %>"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight:bold">Version</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getVersion() %>
                                                    </td>
                                                    <td style="font-weight:bold">Type</td>
                                                    <td bgcolor="#F3F3F3"><%=editCellink.getType() %>
                                                    </td>
                                                    <td style="font-weight:bold">State</td>
                                                    <td align="center"><%=editCellink.getCellinkState()%>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="2">
                                            <jsp:include page="messages.jsp"/>
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
                                                   onclick="addController(<%=editCellink.getUserId() %> , <%=editCellink.getId()%>);"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">
                                            <b><%=editCellink.getControllers().size()%>
                                            </b>&nbsp;<%=session.getAttribute("label.records")%>
                                            <table class="table-list" border=1
                                                   >
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
                                                <%for (Controller controller : editCellink.getControllers()) {%>
                                                <% if ((rowCount % 2) == 0) {%>
                                                <tr class="odd" onMouseOver="changeOdd(this);"
                                                    onmouseout="changeOdd(this)">
                                                        <%} else {%>
                                                <tr class="even" onMouseOver="changeEven(this);"
                                                    onmouseout="changeEven(this)">
                                                    <%}%>
                                                    <td align="center"><a
                                                            href="./editcontrollerrequest.html?userId=<%=editCellink.getUserId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>"><%=controller.getTitle()%>
                                                    </a></td>
                                                    <td align="center"><%=controller.getName()%>
                                                    </td>
                                                    <td align="center"><%=controller.getNetName()%>
                                                    </td>
                                                    <td align="center">
                                                        <%if (user.getRole() != UserRole.USER) {%>
                                                        <a href="./all-screens.html?programId=<%=controller.getProgramId() %>">
                                                            <%=controller.getProgram().getName() %>
                                                        </a>
                                                        <%} else {%>
                                                        <%=controller.getProgram().getName() %>
                                                        <%}%>
                                                    </td>
                                                    <td align="center">
                                                        <%if (controller.isActive()) {%>
                                                        <input type="checkbox" name="Nactive" id="Nactive" checked
                                                               onclick="activate(<%=editCellink.getUserId()%>, <%=cellinkId%>, <%=controller.getId() %>, 'OFF')"/>
                                                        <%} else {%>
                                                        <input type="checkbox" name="Nactive" id="Nactive"
                                                               onclick="activate(<%=editCellink.getUserId()%>, <%=cellinkId%>, <%=controller.getId() %>, 'ON')"/>
                                                        <%}%>
                                                    </td>
                                                    <td align="center">
                                                        <img src="resources/images/edit.gif" style="cursor: pointer" border="0"
                                                             hspace="5"/>
                                                        <a href="./editcontrollerrequest.html?userId=<%=editCellink.getUserId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>">
                                                            <%=session.getAttribute("button.edit")%>
                                                        </a>
                                                    </td>
                                                    <% if (user.getRole() != UserRole.USER) {%>
                                                    <td align="center">
                                                        <img src="resources/images/close.png" style="cursor: pointer" border="0"
                                                             hspace="5"/>
                                                        <a href="javascript:removeController('<%=editCellink.getUserId()%>','<%=cellinkId%>','<%=controller.getId()%>');">
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
                                            <%if (user.getRole() == UserRole.USER) {%>
                                            <button name="btnCancel" type="button"
                                                    onclick='return back("./my-farms.html?userId=<%=editCellink.getUserId() %>");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <%} else {%>
                                            <button name="btnCancel" type="button"
                                                    onclick='return back("./userinfo.html?userId=<%=editCellink.getUserId() %>");'>
                                                <%=session.getAttribute("button.back") %>
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
