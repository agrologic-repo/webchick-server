<%--
    Document   : add-tabletranslate.jsp
    Created on : Oct 3, 2010, 11:28:56 AM
    Author     : Valery Manakhimov
    Company    : Agrologic Ltd. ®
    Version    : 0.1.1.1
--%>

<%@ include file="disableCaching.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.dao.LanguageDao" %>

<jsp:directive.page import="com.agrologic.app.dao.impl.LanguageDaoImpl"/>
<jsp:directive.page import="com.agrologic.app.model.Language"/>
<jsp:directive.page import="com.agrologic.app.model.User"/>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long valueId = Long.parseLong(request.getParameter("valueId"));
    Long langId = Long.parseLong(request.getParameter("langId"));

    String actionsetLabel = request.getParameter("actionsetLabel");
    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    Language lang = languageDao.getById(langId);
%>

<%@page contentType="text/html" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
    <script language="Javascript">
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
        function encode() {
            if (document.addForm.Ntranslate.value != '') {
                var vText = document.addForm.Ntranslate.value;
                var vEncoded = convertToUnicode(vText);
                return vEncoded;
            }
        }
        function convertToUnicode(source) {
            result = '';
            for (i = 0; i < source.length; i++)
                result += '&#' + source.charCodeAt(i) + ';';
            return result;
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

            <form id="addForm" name="addForm" action="./addactionsettranslate.html" method="post"
                  onsubmit="return validate();">
                <table width="100%" align="left" border="0">
                    <input type="hidden" id="valueId" name="valueId" value="<%=valueId%>">
                    <input type="hidden" id="langId" name="langId" value="<%=lang.getId() %>">
                    <tr>
                        <td align="left">Insert &nbsp;<%=actionsetLabel%> in <%=lang.getLanguage() %>
                    </tr>
                    <tr>
                        <td align="left"><input id="Ntranslate" type="text" name="Ntranslate">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2" id="msgTranslation" align="left"></td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnAdd" name="btnAdd" type="submit"><img src="img/plus1.gif">&nbsp;Add</button>
                            <button type="button" onclick='self.close();'><img src="img/close.png">&nbsp;Close</button>
                        </td>
                    </tr>
                </table>
            </form>
</table>
</body>
</html>