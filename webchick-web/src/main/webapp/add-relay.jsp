<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.model.UserDto"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long translateLang = Long.parseLong(request.getParameter("translateLang"));
    Long relayId = Long.parseLong(request.getParameter("relayId"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <title>Add Relay</title>
        <script type="text/javascript">
        function reset() {
            document.getElementById("msgRelayText").innerHTML="";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.NrelayText.value == "") {
                document.getElementById("msgRelayText").innerHTML="&nbsp;Relay text can't be empty";
                document.getElementById("msgRelayText").style.color="RED";
                document.addForm.NrelayText.focus();
                valid = false;
            }
            if(!valid) {
                alert();
                return false;
            }
        }
        function closeWindow() {
            self.close();
            window.opener.location.replace("./all-relays.html?translateLang=<%=translateLang%>");
        }
        </script>
    </head>
    <body onunload="closeWindow();">
      <table cellpadding="1" cellspacing="1" border="0" width="100%">
        <tr>
          <td>
            <h1>Add Relay</h1>
            <h2>add relay</h2>
            <form id="addForm" name="addForm" action="./addrelay.html" method="post" onsubmit="return validate();">
            <table border="0">
                <input type="hidden" id="translateLang" name="translateLang" value="<%=translateLang%>">
                <input type="hidden" id="NrelayId" name="NrelayId" value="<%=relayId%>">
                <tr>
                    <td align="left">Insert text</td>
                    <td><input id="NrelayText" type="text" name="NrelayText"></td>
                </tr>
                <tr>
                    <td colspan="2" id="msgRelayText" name="msgRelayText"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <button  id="btnAdd" name="btnAdd" type="submit"><%=session.getAttribute("button.ok") %></button>
                        <button  id="btnBack" name="btnBack" type="button" onclick='self.close();'><%=session.getAttribute("button.cancel") %></button>
                    </td>
                </tr>
                </table>
            </form>
          </td>
        </tr>
      </table>
    </body>
</html>
