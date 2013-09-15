<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.User"/>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long translateLang = Long.parseLong(request.getParameter("translateLang"));
    Long systemstateId = Long.parseLong(request.getParameter("systemstateId"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <title>Add System State</title>
    <script type="text/javascript">
        function reset() {
            document.getElementById("msgSystemstateText").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.NsystemstateText.value == "") {
                document.getElementById("msgSystemstateText").innerHTML = "&nbsp;Systemstate text can't be empty";
                document.getElementById("msgSystemstateText").style.color = "RED";
                document.addForm.NsystemstateText.focus();
                valid = false;
            }
            if (!valid) {
                return false;
            }
        }
        function closeWindow() {
            self.close();
            window.opener.location.replace("./all-systemstates.html?translateLang=<%=translateLang%>");
        }
    </script>
</head>
<body onunload="closeWindow();">
<table cellpadding="1" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <h1>Add System State</h1>

            <h2>add system state</h2>

            <form id="addForm" name="addForm" action="./addsystemstate.html" method="post"
                  onsubmit="return validate();">
                <table border="0">
                    <input type="hidden" id="translateLang" name="translateLang" value="<%=translateLang%>">
                    <input type="hidden" id="NsystemstateId" name="NsystemstateId" value="<%=systemstateId%>">
                    <tr>
                        <td align="left">Insert text</td>
                        <td><input id="NsystemstateText" type="text" name="NsystemstateText"></td>
                    </tr>
                    <tr>
                        <td colspan="2" id="msgSystemstateText" name="msgSystemstateText"></td>
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
</html>