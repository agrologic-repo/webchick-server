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
    Long systemstateId = Long.parseLong(request.getParameter("systemstateId"));
    String systemstateName = request.getParameter("systemstateName");
    Long translateLang = Long.parseLong(request.getParameter("translateLang"));
    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    Language lang = languageDao.getById(translateLang);
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Add Translation</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
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
            }
            document.addForm.Ntranslate.value = encode();

            if (!valid) {
                return false;
            }
        }
        function closeWindow() {
            self.close();
            window.opener.location.replace("./all-systemstates.html?translateLang=<%=translateLang%>");
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

</head>
<body onunload="closeWindow();">
<table class="main" align="center" cellpadding="0" cellspacing="0" border="0" width="100%" style="padding:10px">
    <tr>
        <td>
            <h1>Add Translation</h1>

            <p>

            <h2>add system state translation </h2>

            <form id="addForm" name="addForm" action="./addsystemstatetranslate.html" method="post"
                  onsubmit="return validate();">
                <table width="100%" align="left" border="0">
                    <input type="hidden" id="systemstateId" name="systemstateId" value="<%=systemstateId%>">
                    <input type="hidden" id="langId" name="langId" value="<%=lang.getId() %>">
                    <tr>
                        <td align="left">Insert &nbsp;<%=systemstateName%> in <%=lang.getLanguage() %>
                    </tr>
                    <tr>
                        <td align="left"><input id="Ntranslate" type="text" name="Ntranslate">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2" id="msgTranslation" align="left"></td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnAdd" name="btnAdd" type="submit"><img src="resources/images/plus1.gif">&nbsp;Add
                            </button>
                            <button type="button" onclick='self.close();'><img src="resources/images/close.png">&nbsp;Close
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
</table>
</body>
</html>

