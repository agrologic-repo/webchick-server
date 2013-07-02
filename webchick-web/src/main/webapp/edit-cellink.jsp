<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.CellinkDto" %>

<jsp:directive.page import="java.util.Collection"/>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);

    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

    UserDto editUser = (UserDto) request.getSession().getAttribute("edituser");
    Long userId = Long.parseLong(request.getParameter("userId"));
    Collection<CellinkDto> cellinks = editUser.getCellinks();
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    CellinkDto cellink = findCellinkToEdit(cellinks, cellinkId);
%>

<%! UserDto findUserToEdit(Collection<UserDto> users, Long userId) {
    for (UserDto u : users) {
        if (u.getId().equals(userId)) {
            return u;
        }
    }
    return null;
}
%>

<%! CellinkDto findCellinkToEdit(Collection<CellinkDto> cellinks, Long cellinkId) {
    for (CellinkDto c : cellinks) {
        if (c.getId().equals(cellinkId)) {
            return c;
        }
    }
    return null;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Edit Cellink</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <script type="text/javascript">
        function validate() {
            if (document.editForm.Ncellinkname.value == "") {
                alert('Enter user name');
                document.editForm.Ncellinkname.focus();
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
    <form action="./editcellink.html" method="post" id="editForm">
        <table border="0" cellPadding=1 cellSpacing=1 width="100%">
            <tr>
                <td width="483">
                    <h1>Edit Cellink</h1>
                </td>
            </tr>
            <tr>
                <td>
                    <h2>edit cellink - <%=cellink.getId()%>
                    </h2>

                    <div><p style="color:red">Boxes with an asterisk next to them are required</p></div>
                    <input id="Nuserid" type="hidden" name="Nuserid" value="<%=cellink.getUserId()%>">
                    <input id="Ncellinkid" type="hidden" name="Ncellinkid" value="<%=cellink.getId()%>">
                    <table>
                        <tr>
                            <td class="rightCell">Cellink name *</td>
                            <td><input id="Ncellinkname" type="text" name="Ncellinkname" value="<%=cellink.getName()%>"
                                       style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="rightCell">Password *</td>
                            <td><input id="Npassword" type="text" name="Npassword" value="<%=cellink.getPassword()%>"
                                       style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="rightCell">SIM Number&nbsp;&nbsp;</td>
                            <td><input id="Nsim" type="text" name="Nsim" value="<%=cellink.getSimNumber()%>"
                                       style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="">Type</td>
                            <td>
                                <select id="Ntype" name="Ntype" style="width:120px">
                                    <option value="n/a" selected>
                                            <% for (String type : CellinkDto.getTypeList()) {%>
                                    <option value="<%=type%>"><%=type%>
                                            <%}%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="">Version</td>
                            <td><input id="Nversion" type="text" name="Nversion" style="width:100px"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <button id="btnBack" name="btnBack" onclick="window.history.back();">
                        <%=session.getAttribute("button.cancel")%>
                    </button>
                    <button id="btnUpdate" name="btnUpdate" onclick='return validate();'>
                        <%=session.getAttribute("button.ok")%>
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>