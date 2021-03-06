<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.LanguageDao" %>
<%@ page import="com.agrologic.app.model.Language" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long tableId = Long.parseLong(request.getParameter("tableId"));
    Long langId = Long.parseLong(request.getParameter("langId"));

    String tableName = request.getParameter("tableName");
    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    Language lang = languageDao.getById(langId);
%>


<!DOCTYPE html>

<html dir="<%=session.getAttribute("dir")%>">
<head>

    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
    <script language="javascript" type="text/javascript">
        function reset() {
            document.getElementById("msgTranslation").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ntranslate.value == "") {
                document.getElementById("msgTranslation").innerHTML = "&nbsp;Translation can't be empty";
                document.getElementById("msgTranslation").style.color = "RED";
                document.addForm.Ntranslate.focus();
                valid = false;
            } else {
                document.addForm.Ntranslate.value = encode();
            }
            if (!valid) {
                return false;
            }
        }

        function closeWindow() {
            self.close();
            window.opener.location.reload(true);
        }
        function check() {
            if (document.addForm.chbox.checked == true) {
                document.addForm.langListBox.disabled = false;
            } else {
                document.addForm.langListBox.disabled = true;
            }
        }
    </script>
    <title>Add Translation</title>
</head>
<body onunload="closeWindow();">
<table class="main" align="center" cellpadding="0" cellspacing="0" border="0" width="100%" style="padding:10px">
    <tr>
        <td>
            <h1>Add Translation</h1>
            <p>
            <h2>add table translation </h2>
            <form id="addForm" name="addForm" action="./addtabletranslate.html" method="post"
                  onsubmit="return validate();">
                <table width="100%" align="left" border="0">
                    <input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
                    <input type="hidden" id="langId" name="langId" value="<%=langId%>">
                    <tr>
                        <td align="left">Insert &nbsp;<%=tableName%> in <%=lang.getLanguage() %>
                    </tr>
                    <tr>
                        <td align="left"><input id="Ntranslate" type="text" name="Ntranslate">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2" id="msgTranslation" align="left"></td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnAdd" name="btnAdd" type="submit"><img src="resources/images/plus1.gif">&nbsp;Add</button>
                            <button type="button" onclick='self.close();'><img src="resources/images/close.png">&nbsp;Close</button>
                        </td>
                    </tr>
                </table>

            </form>
</table>
</body>
</html>
