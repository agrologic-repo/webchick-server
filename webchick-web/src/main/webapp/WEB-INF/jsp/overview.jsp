<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="../../anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.CellinkState" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.util.Set" %>

<%@ include file="../../language.jsp" %>

<jsp:useBean id="userId" scope="request" type="java.lang.Long"/>
<jsp:useBean id="of" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="from" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="to" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="searchText" scope="request" type="java.lang.String"/>
<jsp:useBean id="cellinks" scope="request" type="java.util.Collection"/>

<c:set var="STATE_OFFLINE" value="<%=com.agrologic.app.model.CellinkState.STATE_OFFLINE%>"/>
<c:set var="STATE_STOP" value="<%=com.agrologic.app.model.CellinkState.STATE_STOP%>"/>
<c:set var="STATE_RUNNING" value="<%=com.agrologic.app.model.CellinkState.STATE_RUNNING%>"/>
<c:set var="STATE_START" value="<%=com.agrologic.app.model.CellinkState.STATE_START%>"/>
<c:set var="STATE_ONLINE" value="<%=com.agrologic.app.model.CellinkState.STATE_ONLINE%>"/>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./logout.html");
        return;
    }
%>

<%--<%! int countCellinksByState(Collection<Cellink> cellinks, int state) {--%>
    <%--int count = 0;--%>
    <%--for (Cellink cellink : cellinks) {--%>
        <%--if (cellink.getState() == state) {--%>
            <%--count++;--%>
        <%--}--%>
    <%--}--%>
    <%--return count;--%>
<%--}--%>
<%--%>--%>
<%--<%! Collection<Cellink> getCellinksByState(Collection<Cellink> cellinks, int state) {--%>
    <%--if (state == -1) {--%>
        <%--return cellinks;--%>
    <%--}--%>
    <%--Collection<Cellink> cellinkList = new ArrayList<Cellink>();--%>
    <%--for (Cellink cellink : cellinks) {--%>
        <%--if (cellink.getState() == state) {--%>
            <%--cellinkList.add(cellink);--%>
        <%--}--%>
    <%--}--%>
    <%--return cellinkList;--%>
<%--}--%>
<%--%>--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Webchick overview</title>

</head>
<body>
<div id="header">
    <%@include file="../../usermenuontop.jsp" %>
</div>
<div id="main-shell">
<table border="0" cellPadding=1 cellSpacing=1 width="100%">
    <tr>
        <td style="vertical-align: top" width="20%">
            <h1><%=session.getAttribute("overview.page.title")%></h1>
            <h2><%=session.getAttribute("overview.page.header")%></h2>
        </td>
        <td colspan="2" width="50%">
            <jsp:include page="../../messages.jsp"/>
        </td>
        <% if (user.getRole() == UserRole.ADMIN) {%>
        <td>
            <fieldset>
                <legend><b><%=session.getAttribute("cellink.states")%>
                </b></legend>
                <table class="table-list-small" width="200px">
                    <tr>
                        <td>
                            <table border="0" style="border-collapse: collapse;">
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img class="images" src="resources/images/online.gif"
                                             style="vertical-align: middle"
                                             hspace="5"/>
                                        <a href="overview.html?state=1"><%=session.getAttribute("cellink.state.online")%>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img src="resources/images/running.gif" style="vertical-align: middle"
                                             hspace="5">
                                        <a href="overview.html?state=3"><%=session.getAttribute("cellink.state.running")%>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img src="resources/images/offline.gif" style="vertical-align: middle"
                                             hspace="5"/>
                                        <a href="overview.html?state=0"><%=session.getAttribute("cellink.state.offline")%>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>

        <td>
            <img border="0" src="TotalCellinkStatePieChart" width="100" height="100"/>
        </td>
        <%}%>
    </tr>
    <tr bgcolor="#D5EFFF">
        <td align="justify" colspan="6">
            <input type="text" name="searchText" id="searchText" value="${searchText}"/>
            <img id="search" src="resources/images/search.png" border="0" redirectUrl="./overview.html"/>
            <img id="refresh" src="resources/images/refresh.png" border="0" onclick="redirect('./overview.html')"/>

            <form id="formFilter" name="formFilter" style="display: inline;">
                <%=session.getAttribute("cellink.states")%> :
                <select id="filterStatus">
                    <option value="-1"></option>
                    CellinkState.listState()
                    <% Set<Map.Entry<String, Integer>> states = CellinkState.listState().entrySet();%>
                    <% for (Map.Entry<String, Integer> ss : states) {%>
                    <option value='<%=ss.getValue()%>'>
                        <%=session.getAttribute("cellink.state." + ss.getKey())%>
                    </option>
                    <%}%>
                </select>
                &nbsp;
                <img id="filter" src="resources/images/filter.png" border="0"/>
            </form>

            <form name="mainForm" style="display: inline;">
                <button id="btnDisconnect" name="btnDisconnect" onclick='disconnect()'>
                    <%=session.getAttribute("button.disconnect.cellink")%>
                </button>
            </form>
        </td>
    </tr>
    <tr>
        <td colspan="5">
            <jsp:include page="../../paging.jsp"/>
        </td>
    </tr>
</table>
<table border="0" cellPadding=1 cellSpacing=1 width="100%">
    <%--<%cellinks = getCellinksByState(cellinks, state);%>--%>
    <tr>
        <td colspan="4" width="100%">
            <form id="formFarms" name="formFarms" style="display:inline">
                <table id="table-cellinks" class="tablesorter">
                    <thead>
                    <tr>
                        <th>
                            <input type="checkbox" id="selectall"/>
                        </th>
                        <th width="50px"><span><%=session.getAttribute("table.col.cellink.id")%></span></th>
                        <th><%=session.getAttribute("table.col.cellink.name")%>
                        </th>
                        <th><%=session.getAttribute("table.col.cellink.version")%>
                        </th>
                        <th><%=session.getAttribute("table.col.cellink.user")%>
                        </th>
                        <th><%=session.getAttribute("table.col.cellink.sim")%>
                        </th>
                        <th><%=session.getAttribute("table.col.cellink.lastupdate")%>
                        </th>
                        <th colspan="2"><%=session.getAttribute("table.col.cellink.action")%>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${cellinks}" var="cellink" varStatus="status">
                        <c:set var="cssClass" value="odd"/>
                        <c:set var="onMouseOver" value="changeOdd(this)"/>
                        <c:set var="onmouseout" value="changeOdd(this)"/>
                        <c:if test="${status.index % 2 == 0}">
                            <c:set var="cssClass" value="even"/>
                            <c:set var="onMouseOver" value="changeEven(this)"/>
                            <c:set var="onmouseout" value="changeEven(this)"/>
                        </c:if>

                        <tr class="${cssClass}" onMouseOver="${onMouseOver}" onmouseout="${onmouseout}">
                            <td>
                                <input type="checkbox" class="selectedId" name="selectedId" id="cb${cellink.id}"
                                       value="${cellink.id}"/>
                            </td>
                            <td>${cellink.id}</td>
                            <td>
                                <c:set var="statusImg" value="online"/>
                                <c:set var="onmouseover" value="honline"/>
                                <c:set var="label" value="${session.getAttribute('cellink.state.online')}"/>
                                <c:choose>
                                    <c:when test="${cellink.cellinkState.value == STATE_RUNNING}">
                                        <c:set var="statusImg" value="running"/>
                                        <c:set var="onmouseover" value="hrunning"/>
                                        <c:set var="label" value="${session.getAttribute('cellink.state.running')}"/>
                                    </c:when>
                                    <c:when test="${cellink.cellinkState.value == STATE_OFFLINE || cellink.cellinkState.value == STATE_STOP}">
                                        <c:set var="statusImg" value="offline"/>
                                        <c:set var="onmouseover" value="offline"/>
                                        <c:set var="label" value="${session.getAttribute('cellink.state.offline')}"/>
                                    </c:when>
                                </c:choose>
                                <img src="resources/images/${statusImg}.gif"
                                     onmouseover="this.src='resources/images/${onmouseover}.gif'"
                                     onmouseout="this.src='resources/images/${statusImg}.gif'"
                                     title="${cellink.name} (${label})"
                                     onclick="redirect('./rmctrl-main-screen.html?userId=${cellink.userId}&cellinkId=${cellink.id}&screenId=1&')"/>
                                <c:choose>
                                    <c:when test="${cellink.cellinkState.value == STATE_ONLINE
                                       || cellink.cellinkState.value == STATE_RUNNING || cellink.cellinkState.value == STATE_START}">
                                        <a href="rmctrl-main-screen.html?userId=${cellink.userId}&cellinkId=${cellink.id}"> ${cellink.name}</a>
                                    </c:when>
                                    <c:otherwise>
                                        ${cellink.name}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td> ${cellink.version} </td>
                            <td><a href="./userinfo.html?userId=${cellink.userId}">${cellink.userId} </a></td>
                            <td> ${cellink.simNumber} </td>
                            <td>${cellink.formatedTime}</td>
                            <c:choose>
                                <c:when test="${cellink.cellinkState.value == STATE_ONLINE || cellink.cellinkState.value == STATE_START}">

                                    <td align="center" valign="middle">
                                        <a href="rmctrl-main-screen.html?userId=${cellink.userId}&cellinkId=${cellink.id}">
                                            <img src="resources/images/display.png" style="cursor: pointer"
                                                 title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                                 hspace="5"/><%=session.getAttribute("button.connect.cellink")%>
                                        </a>
                                    </td>

                                </c:when>
                                <c:when test="${cellink.cellinkState.value == STATE_RUNNING}">
                                    <td align="center" valign="middle">
                                        <a href="rmctrl-main-screen.html?userId=${cellink.userId}&cellinkId=${cellink.id}">
                                            <img src="resources/images/display.png" style="cursor: pointer"
                                                 title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                                 hspace="5"/><%=session.getAttribute("button.chicken.coop")%>
                                        </a>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td align="center">
                                        <img src="resources/images/not-available.gif" hspace="5"
                                             title="(<%=session.getAttribute("cellink.state.offline")%>)"/>
                                        <%=session.getAttribute("button.noaction.cellink")%>
                                    </td>

                                </c:otherwise>
                            </c:choose>
                            <td align="center" valign="middle">
                                <a href="./cellink-setting.html?userId=${cellink.userId}&cellinkId=${cellink.id}">
                                    <img src="resources/images/setting.png" style="cursor: pointer"
                                         title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                         hspace="5"/><%=session.getAttribute("button.setting")%>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </form>
        </td>
    </tr>
</table>
</div>
<script type="text/javascript" src="resources/javascript/general.js">;</script>
<script type="text/javascript" src="resources/javascript/jquery.js">;</script>
<script type="text/javascript" src="resources/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript">
    //<![CDATA[
    /**
     * search function
     */
    $(window).load(function () {
        $("#search").click(function () {
            var text = $('#searchText').val();
            var redirectUrl = $(this).attr('redirectUrl');
            window.location.href = redirectUrl + "?searchText=" + text;
        });
    });
    /**
     * filter function
     */
    $(window).load(function () {
        $("#filter").click(function () {
            var text = $('#searchText').val();
            var state = $('#filterStatus :selected').val();
            window.location.href = "./overview.html?state=" + state + "&searchText=" + text;
        });
    });//]]>

    $.tablesorter.addParser({
        // set a unique id
        id: 'dateTimeFormat',
        is: function (s) {
            return false;
        },
        format: function (s) {
            if (s == "") {
                return 0;
            } else {
                var temp = s.split(' ');
                var date = temp[1].split('/');
                var time = temp[0].split(':');

                var day = date[0] * 60 * 24;
                var month = date[1] * 60 * 24 * 31;
                var year = date[2] * 60 * 24 * 366;
                var hour = time[0] * 60;
                var minutes = time[1];
                var seconds = time[2];
                var result = day + month + year + hour + minutes + seconds;
                return result;
            }
        },
        type: 'numeric'
    });

    $(function () {
        $('#table-cellinks').tablesorter({
            sortList: [
                [1, 0]
            ], widgets: ["zebra"],
            // These are detected by default,
            // but you can change or disable them
            headers: {
                // Disable sorting on the first column
                0: { sorter: false },
                6: { sorter: "dateTimeFormat" },
                7: { sorter: false }
            }
        });
    });
    $(document).ready(function () {
        $('#selectall').click(function () {
            $('.selectedId').prop('checked', this.checked);
        });

        $('.selectedId').change(function () {
            var check = ($('.selectedId').filter(":checked").length == $('.selectedId').length);
            $('#selectall').prop("checked", check);
        });
    });
    /**
     *
     */
    function disconnect() {
        var count = 0;
        var formElems = document.getElementsByTagName('INPUT');
        for (var i = 0; i < formElems.length; i++) {
            if (formElems[i] != null && formElems[i].id.indexOf("cb") == 0) {
                if (formElems[i].checked == true) {
                    count = count + 1;
                }
            }
        }
        if (count == 0) {
            alert("No cellinks selected");
            return;
        }

        if (!confirm("Are you sure ?")) {
            return;
        }

        var result = "&cellinkIds=";
        for (var i = 0; i < formElems.length; i++) {
            if (formElems[i].id != null && formElems[i].id.indexOf("cb") == 0) {
                if (formElems[i].checked == true) {
                    result += formElems[i].id.substring(2) + "and";
                }
            }
        }
        result = result.substring(0, result.length - 3);
        document.mainForm.action = "./disconnect-cellinks.html?userId=${userId}" + result;
        document.mainForm.method = "POST";
        document.mainForm.submit();
    }
</script>

<script type="text/javascript" language="javascript">
    var state = <%=request.getParameter("state")%>;
    var length = document.formFilter.filterStatus.options.length;
    for (var i = 0; i < length; i++) {
        if (document.formFilter.filterStatus.options[i].value == state) {
            document.formFilter.filterStatus.selectedIndex = i;
            break;
        }
    }
</script>
</body>
</html>