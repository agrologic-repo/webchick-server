<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="com.agrologic.app.model.Table" %>
<%@ page import="com.agrologic.app.model.User" %>


<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Collection<Screen> screens =( Collection<Screen>) request.getAttribute("screens");
    Table editTable =(Table) request.getAttribute("editTable");
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Edit Table</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
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

                            <h2>edit table - <%=editTable.getId() %>
                            </h2></p>
                            <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                            <form action="./edittable.html" method="post" id="editForm" name="editForm">
                                <input id="programId" type="hidden" name="programId" value="<%=editTable.getProgramId() %>">
                                <input id="screenId" type="hidden" name="screenId" value="<%=editTable.getScreenId() %>">
                                <input id="tableId" type="hidden" name="tableId" value="<%=editTable.getId() %>">
                                <table cellSpacing=1 cellPadding=1 width="250px" align="left" border="0">
                                    <tr>
                                        <td class="rightCell">Table title *</td>
                                        <td><input id="Ntabletitle" type="text" name="Ntabletitle"
                                                   value="<%=editTable.getTitle() %>" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell">Row position *</td>
                                        <td><input id="Nposition" type="text" name="Nposition"
                                                   value="<%=editTable.getPosition() %>" style="width:100px"></td>
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
                                                    onclick='return back("./all-tables.html?programId=<%=editTable.getProgramId() %>&screenId=<%=editTable.getScreenId()%>");'>
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
    var screenId = <%= editTable.getScreenId() %>
    for (var i = 0; i < length; i++) {
        if (document.editForm.NscreenId.options[i].value == screenId) {
            document.editForm.NscreenId.selectedIndex = i;
            break;
        }
    }
</script>
</body>
</html>