<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ page import="com.agrologic.app.model.*" %>
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
    <title>Edit Cellink</title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
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
                        <h1><%=session.getAttribute("label.editCellink.setting.title")%>
                        </h1>
                        <table style="border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;"
                               width="100%">
                            <tr>
                                <td>
                                    <h2><%=session.getAttribute("label.editCellink.setting.title")%>
                                    </h2>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <img border="0" src="resources/images/cellink.jpg"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="100%">
                                    <table class="table-list" border=1 width="100%" cellpadding="2" cellspacing="1">
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
                                                bgcolor="<%=CellinkState.getCellinkStateBGColor(editCellink.getState()) %>"
                                                style="font-weight: bold;color:<%=CellinkState.getCellinkStateColor(editCellink.getState()) %>"><%=editCellink.getCellinkState()%>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3">
                                    <jsp:include page="messages.jsp"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" width="100%"><h2>controller list</h2></td>
                            </tr>
                            <tr>
                                <td>
                                    <input class="button" type="button"
                                           value="<%=session.getAttribute("button.add.controller")%>"
                                           onclick="addController(<%=editCellink.getUserId() %>,<%=editCellink.getId()%>);"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <p><b><%=editCellink.getControllers().size()%>
                                    </b>&nbsp;<%=session.getAttribute("label.records")%>
                                    </p>
                                    <table class="table-list" border=1
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
                                        <%for (Controller controller : editCellink.getControllers()) {%>
                                        <tr onmouseover="this.style.background='#CEDEF4'"
                                            onmouseout="this.style.background='white'" title="Click for details">
                                            <td align="center"><a
                                                    href="./editcontrollerrequest.html?userId=<%=editCellink.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>"><%=controller.getTitle()%>
                                            </a></td>
                                            <td align="center"><%=controller.getName()%>
                                            </td>
                                            <td align="center"><%=controller.getNetName()%>
                                            </td>
                                            <td align="center"><a href="./all-screens.html?programId=<%=controller.getProgramId() %>"><%=((Program) controller.getProgram()).getName() %></a>
                                            </td>
                                            <td align="center">
                                                <a href="./editcontrollerrequest.html?userId=<%=editCellink.getId() %>&cellinkId=<%=controller.getCellinkId() %>&controllerId=<%=controller.getId()%>">
                                                    <img src="resources/images/edit.gif" style="cursor: pointer" border="0"
                                                         hspace="5"/><%=session.getAttribute("button.edit")%>
                                                </a>
                                            </td>
                                            <td align="center">
                                                <a href="javascript:removeController('<%=editCellink.getId()%>','<%=cellinkId%>','<%=controller.getId()%>');"
                                                   onclick="return confirmRemove();">
                                                    <img src="resources/images/close.png" style="cursor: pointer" border="0"
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
                                            onclick='return back("./userinfo.html?userId=<%=editCellink.getId() %>");'><%=session.getAttribute("button.back") %>
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