<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.UserDto" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long relayId = Long.parseLong(request.getParameter("relayId"));
    String relayName = request.getParameter("relayName");
    Long translateLang = Long.parseLong(request.getParameter("translateLang"));
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
    <script language="Javascript">
        function reset() {
            document.getElementById("msgText").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ntext.value == "") {
                document.getElementById("msgText").innerHTML = "&nbsp;Text can't be empty";
                document.getElementById("msgText").style.color = "RED";
                document.addForm.Ntranslate.focus();
                valid = false;
            }
            if (!valid) {
                return false;
            }
        }
        function closeWindow() {
            self.close();
            window.opener.location.replace("./all-relays.html?translateLang=<%=translateLang%>");
        }
    </script>
    <title>Edit Relay</title>
</head>
<body onunload="closeWindow();">
<table class="main" align="center" cellpadding="0" cellspacing="0" border="0" width="100%" style="padding:10px">
    <tr>
        <td>
            <h1>Edit relay</h1>

            <p>

            <h2>edit relay </h2>

            <form id="addForm" name="addForm" action="./edit-relay.html" method="post" onsubmit="return validate();">
                <table width="100%" align="left" border="0">
                    <input type="hidden" id="translateLang" name="translateLang" value="<%=translateLang%>">
                    <input type="hidden" id="NrelayId" name="NrelayId" value="<%=relayId%>">
                    <tr>
                        <td align="left">Insert &nbsp;<%=relayName%>
                    </tr>
                    <tr>
                        <td align="left"><input id="Ntext" type="text" name="Ntext" value="<%=relayName%>">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2" id="msgText" align="left"></td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnUpdate" name="btnUpdate" type="submit"><img src="img/edit.gif"
                                                                                       hspace="5"><%=session.getAttribute("button.update") %>
                            </button>
                            <button type="button" onclick='self.close();'><img src="img/close.png"
                                                                               hspace="5"><%=session.getAttribute("button.cancel") %>
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
</table>
</body>
</html>