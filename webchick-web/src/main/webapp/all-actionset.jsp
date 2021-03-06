<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.*" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Program program = (Program) request.getAttribute("program");
    Screen screen = (Screen) request.getAttribute("screen");
    long programId = screen.getProgramId();
    Collection<Language> languages = (Collection<Language>) request.getAttribute("languages");
    String ptl = request.getParameter("translateLang");
    if (ptl == null) {
        ptl = "1";
    }
    Long translateLang = Long.parseLong(ptl);
    Collection<ProgramActionSet> programactionsets = (Collection<ProgramActionSet>) request.getAttribute("programactionsets");
%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>All Action set</title>

    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
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
            redirect("./save-progactionset.html?programId=" + programId + "&screenId=" + screenId + "&showActionsetMap=" + showActionsetMap + "&posActionsetMap=" + posActionsetMap);
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
        function filterLanguages(programId, screenId) {
            var langId = document.formTable.Lang_Filter.value;
            redirect("./all-tables.html?programId=" + programId + "&screenId=" + screenId + "&translateLang=" + langId);
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
                            <jsp:include page="messages.jsp"/>
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
                                <b><%=programactionsets.size()  %>
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
                                    <%for (ProgramActionSet pasd : programactionsets) {%>
                                    <% if ((cnt % 2) == 0) {%>
                                    <tr class="odd" onMouseOver="changeOdd(this);" onmouseout="changeOdd(this)">
                                            <%} else {%>
                                    <tr class="even" onMouseOver="changeEven(this);" onmouseout="changeEven(this)">
                                        <%}%>
                                        <td>
                                            <%=pasd.getValueId() %>
                                        </td>
                                        <td>
                                            <%=pasd.getLabel()%>
                                        </td>
                                        <td ondblclick="window.open('add-actionsettranslate.jsp?valueId=<%=pasd.getValueId()%>&langId=<%=translateLang%>&actionsetLabel=<%=pasd.getLabel()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');">
                                            <%=pasd.getUnicodeLabel() %>
                                        </td>
                                        </td>
                                        <td align="center" width="100px">
                                            <input type="checkbox" id="list" name="list" <%=pasd.isChecked()%>
                                                   value="<%=pasd.getValueId()%>"
                                                   onclick="check(<%=pasd.getValueId()%>);"/></td>
                                        <td align="center" width="100px">
                                            <input type="text" name="position" value="<%=pasd.getPosition()%>" size="5">
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