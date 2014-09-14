<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long translateLang = Long.parseLong(request.getParameter("translateLang"));
    Long alarmId = Long.parseLong(request.getParameter("alarmId"));
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Add Alarm</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
</head>
<body onunload="closeWindow();">
<table cellpadding="1" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <h1>Add Alarm</h1>

            <h2>add alarm</h2>

            <form id="addForm" name="addForm" action="./addalarm.html" method="post" onsubmit="return validate();">
                <table border="0">
                    <input type="hidden" id="translateLang" name="translateLang" value="<%=translateLang%>">
                    <input type="hidden" id="NalarmId" name="NalarmId" value="<%=alarmId%>">
                    <tr>
                        <td align="left">Insert text</td>
                        <td><input id="NalarmText" type="text" name="NalarmText"></td>
                    </tr>
                    <tr>
                        <td colspan="2" id="msgAlarmText" name="msgAlarmText"></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                            <button id="btnAdd" name="btnAdd" type="submit"><%=session.getAttribute("button.ok") %>
                            </button>
                            <button id="btnBack" name="btnBack" type="button"
                                    onclick='self.close();'><%=session.getAttribute("button.cancel") %>
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
<script type="text/javascript">
    function reset() {
        document.getElementById("msgAlarmText").innerHTML = "";
    }
    function validate() {
        var valid = true;
        reset();
        if (document.addForm.NalarmText.value == "") {
            document.getElementById("msgAlarmText").innerHTML = "&nbsp;Alarm text can't be empty";
            document.getElementById("msgAlarmText").style.color = "RED";
            document.addForm.NalarmText.focus();
            valid = false;
        }
        if (!valid) {
            return false;
        }
    }
    function closeWindow() {
        self.close();
        window.opener.location.replace("./all-alarms.html?translateLang=<%=translateLang%>");
    }
</script>
</html>