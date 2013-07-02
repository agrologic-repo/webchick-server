<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Program" %>

<jsp:directive.page import="com.agrologic.app.model.Screen"/>
<jsp:directive.page import="com.agrologic.app.model.Table"/>
<jsp:directive.page import="java.util.Collection"/>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Program program = (Program) request.getSession().getAttribute("program");
    Collection<Screen> screens = program.getScreens();
    Screen screen = (Screen) request.getSession().getAttribute("screen");
    Long tableId = Long.parseLong(request.getParameter("tableId"));
    Table table = findTableToEdit(screen.getTables(), tableId);
%>

<%! Table findTableToEdit(Collection<Table> tables, Long tableId) {
    for (Table t : tables) {
        if (t.getId().equals(tableId)) {
            return t;
        }
    }
    return null;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Edit Table</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="StyleSheet" type="text/css" href="css/menubar.css">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css">

    <script type="text/javascript">
        function validate() {
            if (document.editForm.Ntabletitle.value == "") {
                alert('Table title');
                document.editForm.Ntabletitle.focus();
                return false;
            } else if (document.editForm.Nposition.value == "") {
                alert('Table position');
                document.editForm.Nposition.focus();
                return false;
            } else if (document.editForm.NscreenId.value == "None") {
                alert('Choose screen');
                document.editForm.NscreenId.focus();
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
            <td valign="top" height="648px">
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td width="483">
                            <p>

                            <h1>Edit Table</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2>edit table - <%=table.getId() %>
                            </h2></p>
                            <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                            <form action="./edittable.html" method="post" id="editForm" name="editForm">
                                <input id="programId" type="hidden" name="programId" value="<%=program.getId() %>">
                                <input id="screenId" type="hidden" name="screenId" value="<%=table.getScreenId() %>">
                                <input id="tableId" type="hidden" name="tableId" value="<%=table.getId() %>">
                                <table cellSpacing=1 cellPadding=1 width="250px" align="left" border="0">
                                    <tr>
                                        <td class="rightCell">Table title *</td>
                                        <td><input id="Ntabletitle" type="text" name="Ntabletitle"
                                                   value="<%=table.getTitle() %>" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell">Row position *</td>
                                        <td><input id="Nposition" type="text" name="Nposition"
                                                   value="<%=table.getPosition() %>" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell"> Screen *</td>
                                        <td align="left">
                                            <select id="NscreenId" name="NscreenId" style="width:120px">
                                                <option value="None" selected>Select
                                                        <% for(Screen s:screens) {%>
                                                <option value="<%=s.getId() %>"><%=s.getTitle() %>
                                                        <%}%>
                                            </select>
                                        </td>
                                        <td id="msgScreenId"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <button id="btnBack" name="btnBack"
                                                    onclick='return back("./all-tables.html?programId=<%=program.getId() %>&screenId=<%=table.getScreenId()%>");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <button id="btnUpdate" name="btnUpdate" type="submit"
                                                    onclick='return validate();'>
                                                <%=session.getAttribute("button.ok") %>
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>


<script language="Javascript">
    var length = document.editForm.NscreenId.options.length;
    var screenId =
    <%= screen.getId() %>
    for (var i = 0; i < length; i++) {
        if (document.editForm.NscreenId.options[i].value == screenId) {
            document.editForm.NscreenId.selectedIndex = i;
            break;
        }
    }
</script>
</body>
</html>