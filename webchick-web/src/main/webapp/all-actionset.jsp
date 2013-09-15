<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.ActionSet" %>

<jsp:directive.page import="com.agrologic.app.model.Language"/>
<%@ page import="com.agrologic.app.model.Program" %>
<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Program program = (Program) request.getSession().getAttribute("program");
    Screen screen = (Screen) request.getSession().getAttribute("screen");
    long programId = screen.getProgramId();
    Collection<Language> languages = (Collection<Language>) request.getSession().getAttribute("languages");
    String ptl = (String) request.getParameter("translateLang");
    if (ptl == null) {
        ptl = "1";
    }
    Long translateLang = Long.parseLong(ptl);
    Collection<ActionSet> actionsets = (Collection<ActionSet>) request.getSession().getAttribute("actionset");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>All Action set</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <script type="text/javascript" src="js/util.js"></script>
    <script type="text/javascript" src="js/general.js"></script>
    <script type="text/javascript">
        function validate() {
            if (document.editForm.Nprogramname.value == "") {
                alert('Enter user name');
                document.editForm.Nprogramname.focus();
                return false;
            } else {
                document.editForm.submit();
            }
        }
        function removeData(programId, screenId, tableId, dataId) {
            if (confirm("This action will remove tabledata from database.\n Do you want to continue?") == true) {
                window.document.location.replace("./removedata.html?programId=" + programId + "&screenId=" + screenId + "&tableId=" + tableId + "&dataId=" + dataId);
            }
        }
        function save(programId, screenId) {
            var showActionsetMap = new Hashtable();
            var posActionsetMap = new Hashtable();

            var chb = document.formTable.list;
            var poss = document.formTable.position;

            // if only one check table-list
            if (chb.tagName == "INPUT") {
                var dataId = chb.value;
                if (chb.checked) {
                    showActionsetMap.put(dataId, "yes");
                } else {
                    showActionsetMap.put(dataId, "no");
                }
                var poss = poss.value;
                posActionsetMap.put(dataId, poss);
                // if two or more check table-list
            } else {
                for (var i = 0; i < chb.length; i++) {
                    var dataId = chb[i].value;
                    if (chb[i].checked) {
                        showActionsetMap.put(dataId, "yes");
                    } else {
                        showActionsetMap.put(dataId, "no");
                    }
                    var pos = poss[i].value;
                    posActionsetMap.put(dataId, pos);
                }
            }
            window.document.location.replace("./save-progactionset.html?programId=" + programId + "&screenId=" + screenId + "&showActionsetMap=" + showActionsetMap + "&posActionsetMap=" + posActionsetMap);
        }
        function checkedAll() {
            var form = document.getElementById('formTable');
            var checkall = document.getElementById('listall');

            for (var i = 0; i < form.elements.length; i++) {
                if (checkall.checked == true) {
                    form.elements[i].checked = true;
                } else {
                    form.elements[i].checked = false;
                }
            }
        }
        function check(index) {
            var form = document.getElementById('formTable');
            var checkall = document.getElementById('listall');

            for (var i = 0; i < form.elements.length; i++) {
                if (form.elements[i].value == index && form.elements[i].checked == false) {
                    checkall.checked = false;
                }
            }
        }
        function back(link) {
            window.document.location.replace(link);
            return false;
        }
        function filterLanguages(programId, screenId) {
            var langId = document.formTable.Lang_Filter.value;
            window.document.location.replace("./all-tables.html?programId=" + programId + "&screenId=" + screenId + "&translateLang=" + langId);
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
                <table border=0 cellPadding=1 cellSpacing=1>
                    <tr>
                        <td>
                            <h1><%=session.getAttribute("data.page.header") %>
                            </h1>

                            <h2><%=session.getAttribute("data.page.sub.header") %>
                            </h2>

                            <h3>Program - <font color="teal"><%=program.getName()%>
                            </font>;
                                Screen - <font color="teal"><%=screen.getTitle()%>
                                </font>;</h3>
                        </td>
                        <td align="center" colspan="3">
                            <%@include file="messages.jsp" %>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <button id="btnCancel" style="float:left" name="btnCancel"
                                    onclick='return back("./all-screens.html?programId=<%=screen.getProgramId()%>&translateLang=<%=translateLang%>");'>
                                <%=session.getAttribute("button.back")%>
                            </button>
                            <button id="btnSave" style="float:left" name="btnSave"
                                    onclick='return save(<%=programId%>,<%=screen.getId()%>);'>
                                <%=session.getAttribute("button.save")%>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td colSpan=3>
                            <p>
                                <b><%=actionsets.size()  %>
                                </b> <%=session.getAttribute("label.records")%>
                            </p>

                            <form id="formTable" name="formTable">
                                <table class="table-list" border="1">
                                    <thead>
                                    <th class="centerHeader" width="100px">Send Value ID</th>
                                    <th class="centerHeader" width="200px">Title</th>
                                    <th class="leftHeader" width="240px">Text
                                        <select id="Lang_Filter" name="Lang_Filter"
                                                onchange="return filterLanguages(<%=programId%>,<%=screen.getId()%>);">
                                            <%for (Language l : languages) { %>
                                            <option value="<%=l.getId()%>"><%=l.getLanguage()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </th>
                                    <th align="center" width="140px">Show All &nbsp;
                                        <input type="checkbox" id="listall" name="listall" title="Show"
                                               onclick="checkedAll();"></th>
                                    <th align="center" width="100px">Position</th>
                                    </thead>
                                    <% int cnt = 0;%>
                                    <%for (ActionSet asd : actionsets) {%>
                                    <% if ((cnt % 2) == 0) {%>
                                    <tr class="odd" onMouseOver="changeOdd(this);" onmouseout="changeOdd(this)">
                                            <%} else {%>
                                    <tr class="even" onMouseOver="changeEven(this);" onmouseout="changeEven(this)">
                                        <%}%>
                                        <td>
                                            <%=asd.getValueId() %>
                                        </td>
                                        <td>
                                            <%=asd.getLabel()%>
                                        </td>
                                        <td ondblclick="window.open('add-actionsettranslate.jsp?valueId=<%=asd.getValueId()%>&langId=<%=translateLang%>&actionsetLabel=<%=asd.getLabel()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');">
                                            <%=asd.getUnicodeLabel() %>
                                        </td>
                                        </td>
                                        <td align="center" width="100px">
                                            <input type="checkbox" id="list" name="list" <%=asd.isChecked()%>
                                                   value="<%=asd.getValueId()%>"
                                                   onclick="check(<%=asd.getValueId()%>);"/></td>
                                        <td align="center" width="100px">
                                            <input type="text" name="position" value="<%=asd.getPosition()%>" size="5">
                                        </td>
                                    </tr>
                                    <%cnt++;%>
                                    <%}%>
                                </table>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <button id="btnCancel" style="float:left" name="btnCancel"
                                    onclick='return back("./all-screens.html?programId=<%=screen.getProgramId()%>&translateLang=<%=translateLang%>");'>
                                <%=session.getAttribute("button.back") %>
                            </button>
                            <button id="btnSave" style="float:left" name="btnSave"
                                    onclick='return save(<%=programId %>,<%=screen.getId() %>);'>
                                <%=session.getAttribute("button.save") %>
                            </button>
                        </td>
                    </tr>
                </table>
                <script language="Javascript">
                    var length = document.formTable.Lang_Filter.options.length;
                    for (var i = 0; i < length; i++) {
                        var translateLang = parseInt(<%=request.getParameter("translateLang")%>);
                        if (document.formTable.Lang_Filter.options[i].value == translateLang) {
                            document.formTable.Lang_Filter.selectedIndex = i;
                            break;
                        }
                    }
                </script>
                <br/>
            </td>
        </tr>
    </table>
</div>

</body>
</html>