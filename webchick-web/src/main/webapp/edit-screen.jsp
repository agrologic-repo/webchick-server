<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Program" %>

<jsp:directive.page import="com.agrologic.app.model.Screen"/>
<jsp:directive.page import="java.util.Collection"/>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Program program = (Program) request.getSession().getAttribute("program");
    Collection<Screen> screens = program.getScreens();
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    Screen screen = findScreenToEdit(screens, screenId);
%>


<%! Screen findScreenToEdit(Collection<Screen> screens, Long screenId) {
    for (Screen s : screens) {
        if (s.getId().equals(screenId)) {
            return s;
        }
    }
    return null;
}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Edit Screen</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">

    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"></link>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"></link>
    <script type="text/javascript" language="javascript">
        function reset() {
            document.getElementById("msgScreenTitle").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.editForm.Ntitle.value == "") {
                alert('Screen title');
                document.editForm.Ntitle.focus();
                return false;
            } else if (document.editForm.Nposition.value == "") {
                alert('Screen position');
                document.editForm.Nposition.focus();
                return false;
            }
            if (!valid) {
                return false;
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

                            <h1>Edit Screen</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2>edit screen - <%=screen.getId() %>
                            </h2></p>
                            <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                            <form action="./editscreen.html" method="post" id="editForm" name="editForm">
                                <input id="programid" type="hidden" name="programid"
                                       value="<%=screen.getProgramId() %>">
                                <input id="screenid" type="hidden" name="screenid" value="<%=screen.getId() %>">
                                <table cellSpacing=1 cellPadding=1 width="auto" align="left" border="0">
                                    <tr>
                                        <td class="rightCell">Table title *</td>
                                        <td><input id="Ntitle" type="text" name="Ntitle" value="<%=screen.getTitle() %>"
                                                   style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell">Row position *</td>
                                        <td><input id="Nposition" type="text" name="Nposition"
                                                   value="<%=screen.getPosition() %>" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell" valign="top">Description &nbsp;</td>
                                        <td><textarea name="Ndescript" cols="60" rows="6"><%=screen.getDescript() %>
                                        </textarea></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <button id="btnBack" name="btnBack"
                                                    onclick='return back("./all-screens.html?programId=<%=program.getId() %>");'>
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

</body>
</html>