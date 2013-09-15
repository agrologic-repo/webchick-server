<%--
    Document   : history-setup.jsp
    Created on : Nov 14, 2010, 3:10:18 PM
    Author     : Valery Manakhimov
    Company    : Agrologic Ltd. ®
    Version    : 0.1.1.1
--%>

<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Data" %>
<%@ page import="com.agrologic.app.model.HistorySetting" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);

    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

    Long programId = Long.parseLong(request.getParameter("programId"));
    Collection<Data> historyData = (Collection<Data>) request.getSession().getAttribute("historyData");
    Collection<HistorySetting> historySettingData = (Collection<HistorySetting>) request.getSession().getAttribute("historySettingData");
%>

<%! String historySettingChecked(Collection<HistorySetting> historySettingData, Long dataId) {
    if (historySettingData != null) {
        for (HistorySetting hsd : historySettingData) {
            if (hsd.getDataId() == dataId) {
                if (hsd.getChecked().equals("true"))
                    return "checked";
                else
                    return "unchecked";
            }
        }
    }
    return "unchecked";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>History Setting</title>
    <link rel="stylesheet" type="text/css" href="css/calendar.css"/>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm.">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
    <link rel="StyleSheet" type="text/css" href="css/menubar.css">
    <link rel="StyleSheet" type="text/css" href="css/multiopt.css">
    <style type="text/css">
        div.tableHolder {
            OVERFLOW: auto;
            WIDTH: 350px;
            POSITION: relative;
            HEIGHT: 200px;
        }

        thead th {
            Z-INDEX: 20;
            position: relative;
            top: expression(this.offsetParent.scrollTop -1 );
            POSITION: relative;
            HEIGHT: 20px;
            TEXT-ALIGN: center
        }

        tfoot td {
            Z-INDEX: 20;
            POSITION: relative;
            TOP: expression(this.offsetParent.clientHeight - this.offsetParent.scrollHeight + this.offsetParent.scrollTop + 1 );
            HEIGHT: 20px;
            TEXT-ALIGN: left;
            text-wrap: suppress
        }

        .tableFonts {
            text-align: center;
            font-size: 10pt;
            font-family: MS, Sans, Serif;
            border-collapse: collapse;
        }

        .tableHeaders {
            text-align: center;
            font: bold 10pt Tahoma;
            color: white;
            height: 20px;
            background: blue;
        }

        .tableCell {
            text-align: center;
            font-size: 10pt;
            font-family: MS, Sans, Serif;
            color: black;
        }
    </style>

    <script type="text/javascript" src="js/util.js"></script>
    <script type="text/javascript">
        function back(link) {
            window.document.location.replace(link);
            return false;
        }

        function save(programId) {
            var historySettingMap = new Hashtable();
            var list = document.frmMain.list;
            var datalist = document.getElementsByName("datalist");
            // if only one check box
            for (var i = 0; i < list.length; i++) {
                if (list[i].tagName == "INPUT") {
                    if (list[i].checked) {
                        historySettingMap.put(datalist[i].value, list[i].checked);
                    } else {
                        historySettingMap.put(datalist[i].value, list[i].checked);
                    }
                }
            }

            window.document.location.replace("./save-history-setting.html?programId=" + programId + "&historySettingMap=" + historySettingMap);
        }
        /**
         * Check all display chakboxes.
         */
        function checkedAll() {
            var form = document.getElementById('frmMain');
            for (var i = 1; i < form.elements.length; i++) {
                if (form.elements[i].checked == false) {
                    form.elements[i].checked = true;
                } else {
                    form.elements[i].checked = false;
                }
            }
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
            <td>
                <form method="post" name="frmMain" id="frmMain" action="./save-history-setting.html">
                    <input id="programId" type="hidden" name="userId" value="<%=programId%>">
                    <table width="1024px">
                        <tr>
                            <td>
                                <h1>History Setting</h1>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <p>

                                <h2> History list program id <font color="teal"><%=programId%>
                                </font></h2>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <p><%=historyData.size()%>&nbsp;record found</p>
                                <input type="button" name="checkall" value="Check All" onclick="checkedAll();">
                                <ul>
                                    <% for (int i = 0; i < historyData.size(); i++) {%>
                                    <%
                                        Data data = historyData.get(i);
                                        String check = historySettingChecked(historySettingData, data.getId());
                                    %>
                                    <li>
                                        <label for="<%=data.getId() %>">
                                            <input type="checkbox" name="list" <%=check%>
                                                   id="<%=data.getId()%>"><%=data.getLabel() %> - <%=data.getId()%>
                                            <input type="hidden" name="datalist" value="<%=data.getId()%>">
                                        </label>
                                    </li>
                                    <%}%>
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <td align="left">
                                <button style="float:left" id="btnCancel" name="btnCancel"
                                        onclick='return back("./all-programs.html");'>
                                    <%=session.getAttribute("button.cancel") %>
                                </button>
                                <button id="btnSave" name="btnSave" onclick='return save(<%=programId%>);'>
                                    <%=session.getAttribute("button.save") %>
                                </button>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>


</body>
</html>