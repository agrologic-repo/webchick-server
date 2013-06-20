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
<%@ page import="com.agrologic.app.model.DataDto" %>
<%@ page import="com.agrologic.app.model.HistorySettingDto" %>
<%@ page import="com.agrologic.app.model.UserDto" %>
<%@ page import="com.agrologic.app.web.UserRole" %>
<%@ page import="java.util.List" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);

    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long programId = Long.parseLong(request.getParameter("programId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));

    List<DataDto> historyData = (List<DataDto>) request.getSession().getAttribute("historyData");
    List<HistorySettingDto> historySettingData = (List<HistorySettingDto>) request.getSession().getAttribute("historySettingData");
%>

<%! String historySettingChecked(List<HistorySettingDto> historySettingData, Long dataId) {
    if (historySettingData != null) {
        for (HistorySettingDto hsd : historySettingData) {
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

        function save() {
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


            window.document.location.replace("./histbygrowday.html?userId=<%=userId%>"
                    + "&cellinkId=<%=cellinkId%>"
                    + "&flockId=<%=flockId%>"
                    + "&histDataArray=" + historySettingMap);
        }
        /**
         * Check all display chakboxes.
         */
        function checkedAll() {
            var form = document.getElementById('frmMain');

            //var checkboxList = document.getElementsByTagName('CHECKBOX');

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
<table width="1024px" style="padding:0px;" align="center">
    <tr>
        <td>
            <%@include file="toplang.jsp" %>
        </td>
        <td>
            <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1&doResetTimeout=true">
                <img src="img/display.png" style="cursor: pointer" border="0"/>
                &nbsp;<%=session.getAttribute("button.screens")%>&nbsp;
            </a>
            <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
            <a href="<%=request.getContextPath()%>/userinfo.html?userId=<%=userId%>">
                <img src="img/cellinks.png" style="cursor: pointer" border="0"/>
                &nbsp;<%=session.getAttribute("button.cellinks")%>&nbsp;
            </a>
            <%} else {%>
            <a href="<%=request.getContextPath()%>/overview.html?userId=<%=userId%>">
                <img src="img/cellinks.png" style="cursor: pointer" border="0"/>
                &nbsp;<%=session.getAttribute("button.cellinks")%>&nbsp;
            </a>
            <%}%>
            <a href="logout.html"><img src="img/logout.gif" style="cursor: pointer" border="0"/>
                &nbsp;<%=session.getAttribute("label.logout") %>&nbsp;</a>
        </td>
    </tr>
</table>
<table width="1024px" cellpadding="0" cellspacing="0" style="padding:0px;" align="center">
    <tr>
        <td>
            <form method="post" name="frmMain" id="frmMain" action="./histbygrowday.html">
                <input id="programId" type="hidden" name="userId" value="<%=userId%>">
                <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>">
                <input id="flockId" type="hidden" name="flockId" value="<%=flockId%>">

                <table width="1024px" border="0">
                    <tr>
                        <td colspan="2">
                            <h1>History Graphs</h1>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h2> Daily Graph </h2>
                        </td>
                        <td>
                            <h2> Day : <input type="text" size="5" id="growday" name="growday">
                                <input type="submit" value="submit">
                            </h2>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <img src="img/graph.jpg" style="border: 5px; border-color: black">
                        </td>
                        <td valign="top">
                            <ul>
                                <% for (int i = 0; i < historySettingData.size(); i++) {%>
                                <% DataDto data = historyData.get(i);
                                    String check = historySettingChecked(historySettingData, data.getId());%>
                                <li>
                                    <label for="<%=data.getId() %>">
                                        <input type="checkbox" name="<%=data.getId()%>"><%=data.getLabel() %>
                                        <input type="hidden" name="datalist" value="<%=data.getId()%>">
                                    </label>
                                </li>
                                <%}%>
                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">
                            <button id="btnBack" name="btnBack"
                                    onclick='return back("./flocks.html?userId=<%=user.getId() %>&cellinkId=<%=cellinkId%>");'>
                                <img alt="" src="img/blank.gif" hspace="5"/>Back
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