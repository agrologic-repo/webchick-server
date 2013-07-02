<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.CellinkDto" %>
<%@ page import="com.agrologic.app.web.CellinkState" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Set" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    //UserDto user = (UserDto) getServletContext().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./logout.html");
        return;
    }
    int state = -1;
    try {
        state = Integer.parseInt(request.getParameter("state"));
    } catch (Exception e) {
        state = -1;
    }
    Collection<CellinkDto> cellinks = (Collection<CellinkDto>) request.getSession().getAttribute("cellinks");
    int from = (Integer) request.getSession().getAttribute("from");
    int to = (Integer) request.getSession().getAttribute("to");
    int of = (Integer) request.getSession().getAttribute("of");
    int tableline = 25;
%>

<%! int countCellinksByState(Collection<CellinkDto> cellinks, int state) {
    int count = 0;
    for (CellinkDto cellink : cellinks) {
        if (cellink.getState() == state) {
            count++;
        }
    }
    return count;
}
%>
<%! Collection<CellinkDto> getCellinksByState(Collection<CellinkDto> cellinks, int state) {
    if (state == -1) {
        return cellinks;
    }
    Collection<CellinkDto> cellinkList = new ArrayList<CellinkDto>();
    for (CellinkDto cellink : cellinks) {
        if (cellink.getState() == state) {
            cellinkList.add(cellink);
        }
    }
    return cellinkList;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html dir="<%=(String) request.getSession().getAttribute("dir")%>" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Webchick overview</title>
    <link rel="shortcut icon" href="img/favicon1.ico" type="image/x-icon"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <script language="javascript" src="js/general.js"></script>
    <script type="text/javascript">
        function activate(userId, cellinkId, controllerId, active) {
            window.document.location.replace("./activatecontroller.html?userId=" + userId
                    + "&cellinkId=" + cellinkId
                    + "&controllerId=" + controllerId
                    + "&active=" + active
                    + "&url=overview.html");
        }
        function addCellink(uid) {
            window.document.location.replace("./add-cellink.jsp?userId=" + uid);
            return false;
        }
        function disconnectCellinks() {
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

            if (!confirm("Do you sure ?")) {
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
            document.mainForm.action = "./disconnect-cellinks.html?userId=<%=user.getId()%>" + result;
            document.mainForm.method = "POST";
            document.mainForm.submit();
        }
        function removeCellink(userId, cellinkId) {
            if (confirm("Are you sure ?") == true) {
                window.document.location.replace("./removecellink.html?userId=" + userId + "&cellinkId=" + cellinkId);
            }
        }
        function filterCellinks(state) {
            window.document.location.replace("./overview.html?state=" + state);
            return false;
        }
        function filterCellinks() {
            var state = document.formFilter.cellinkStateFilter.value;
            window.document.location.replace("./overview.html?state=" + state);
            return false;
        }
        function searchCellink() {
            var state = document.formFilter.cellinkStateFilter.value;
            var name = document.getElementById('searchText');
            window.document.location.replace("./overview.html?name=" + name.value + "&state=" + state);
            return false;
        }
        function toggle(id, count) {
            var ele = document.getElementById("toggleText" + id);
            var text = document.getElementById("displayText" + id);
            if (ele.style.display == "block") {
                ele.style.display = "none";
                text.innerHTML = "<img src=\"img/plus.gif\" border=\"0\" hspace=\"5\" id=\"img\"" + id + "\"><%=session.getAttribute("button.show")%> (" + count + ")</img>";
            }
            else {
                ele.style.display = "block";
                text.innerHTML = text.innerHTML = "<img src=\"img/minus.gif\" border=\"0\" hspace=\"5\" id=\"img\"" + id + "\"><%=session.getAttribute("button.hide")%></img>";
                ;
            }
        }
        function toggle5(showHideDiv, switchImgTag) {
            var ele = document.getElementById(showHideDiv);
            var imageEle = document.getElementById(switchImgTag);

            if (ele.style.display == 'block') {
                ele.style.display = 'none';
                imageEle.innerHTML = '';
            } else {
                ele.style.display = 'block';
                imageEle.innerHTML = '';
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
    <% Map<String, Integer> stateMap = CellinkState.listState();%>
    <% Set<Map.Entry<String, Integer>> setState = stateMap.entrySet();%>
    <tr>
        <td style="vertical-align: top">
            <h1><%=session.getAttribute("overview.page.title")%>
            </h1>

            <h2><%=session.getAttribute("overview.page.header")%>
            </h2>
        </td>
        <td colspan="2">
            <%@include file="messages.jsp" %>
        </td>
        <td colspan="2" rowspan="2">
            <fieldset style="padding: 5px">
                <legend><b><%=session.getAttribute("cellink.states")%>
                </b></legend>
                <table class="table-list-small">
                    <tr>
                        <td>
                            <table border="0" cellpadding="2" cellspacing="2" style="border-collapse: collapse;">
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img src="img/online.gif" style="vertical-align: middle" hspace="5"/>
                                        <a href="overview.html?state=1"><%=session.getAttribute("cellink.state.online")%>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img src="img/running.gif" style="vertical-align: middle" hspace="5">
                                        <a href="overview.html?state=3"><%=session.getAttribute("cellink.state.running")%>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img src="img/offline.gif" style="vertical-align: middle" hspace="5"/>
                                        <a href="overview.html?state=0"><%=session.getAttribute("cellink.state.offline")%>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tr>
                                    <td>
                                        <img border="0" src="TotalCellinkStatePieChart" width="100" height="100"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td>
            <fieldset style="padding: 5px">
                <legend><b><%=session.getAttribute("label.filter")%>
                </b></legend>
                <table>
                    <tr>
                        <td nowrap>
                            <%=session.getAttribute("label.name")%> :
                            <input type="text" name="searchText" id="searchText" size="15" name="searchText">
                            &nbsp;
                            <button id="btnSearch" name="btnSearch" onclick="return searchCellink();">
                                <%=session.getAttribute("button.search")%>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap>
                            <form id="formFilter" name="formFilter" style="display: inline;">
                                <%=session.getAttribute("cellink.states")%> :
                                <select id="cellinkStateFilter" name="cellinkStateFilter">
                                    <option value="-1"></option>
                                    <% Set<Map.Entry<String, Integer>> states = stateMap.entrySet();%>
                                    <% for (Map.Entry<String, Integer> ss : states) {%>
                                    <option value='<%=ss.getValue()%>'>
                                        <%=session.getAttribute("cellink.state." + ss.getKey())%>
                                    </option>
                                    <%}%>
                                </select>
                                &nbsp;
                                <button id="btnFilter" name="btnFilter" onclick="return filterCellinks();">
                                    <%=session.getAttribute("button.filter")%>
                                </button>
                            </form>

                            <form name="mainForm" style="display: inline;">
                                <button id="btnReset" name="btnReset"
                                        onclick="window.document.location='./overview.html'">
                                    <%=session.getAttribute("button.reset")%>
                                </button>
                                <button id="btnDisconnect" name="btnDisconnect" onclick='disconnectCellinks()'>
                                    <%=session.getAttribute("button.disconnect.cellink")%>
                                </button>
                            </form>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
</table>
<table border="0" cellPadding=1 cellSpacing=1 width="100%">
    <%cellinks = getCellinksByState(cellinks, state);%>
    <%String cellinkState = (String) request.getParameter("state"); %>
    <% if (cellinkState == null) {%>
    <%cellinkState = "-1";%>
    <%}%>
    <tr>
        <td colspan="2">
            <table class="table-pages" border="0" width="100%">
                <tr>
                    <td style="width: 80%;">Showing <%=from%>-<%=to%> of <%=of%>
                    </td>
                    <td style="width: 20%;" nowrap>
                        <%int pages = (of % 25) > 0 ? (of / 25) + 1 : (of / 25);%>
                        <%for (int i = 0; i < pages; i++) {%>
                        <%if (i * 25 <= from && (i * 25) + 25 >= to) {%>
                        <a class="active" href="overview.html?index=<%=i * 25%>&state=<%=cellinkState%>"><%=i + 1%>
                        </a>
                        <%} else {%>
                        <a href="./overview.html?index=<%=i * 25%>&state=<%=cellinkState%>"><%=i + 1%>
                        </a>
                        <%}%>
                        <%}%>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="4" width="100%">
            <form id="formFarms" name="formFarms" style="display:inline">
                <table class="table-list" border="0" cellpadding="2" cellspacing="1" width="100%"
                       style="behavior:url(tablehl.htc) url(sort.htc);">
                    <thead>
                    <th nowrap>
                        <input type="checkbox" name="checkAll" id="checkAll">
                    </th>
                    <th align="center" nowrap>
                        <%=session.getAttribute("table.col.cellink.id")%>
                    </th>
                    <th align="center" width="" nowrap colspan="2">
                        <%=session.getAttribute("table.col.cellink.name")%>
                    </th>
                    <th align="center" width="" nowrap>
                        <%=session.getAttribute("table.col.cellink.version")%>
                    </th>
                    <th align="center" width="" nowrap>
                        <%=session.getAttribute("table.col.cellink.user")%>
                    </th>
                    <th align="center" width="" nowrap>
                        <%=session.getAttribute("table.col.cellink.sim")%>
                    </th>
                    <th align="center">
                        <%=session.getAttribute("table.col.cellink.lastupdate")%>
                    </th>
                    <th align="center" colspan="2">
                        <%=session.getAttribute("table.col.cellink.action")%>
                    </th>
                    </thead>
                    <tbody>
                    <%if (cellinks.size() != 0) {%>
                    <%int rows = 0;%>
                    <%for (CellinkDto cellink : cellinks) {%>
                    <% if ((rows % 2) == 0) {%>
                    <tr class="odd" onMouseOver="changeOdd(this);" onmouseout="changeOdd(this)">
                            <%} else {%>
                    <tr class="even" onMouseOver="changeEven(this);" onmouseout="changeEven(this)">
                        <%}%>
                        <td align="center">
                            <input type="checkbox" id=cb<%=cellink.getId()%> name=cb<%=cellink.getId()%>/>
                        </td>
                        <td align="center">
                            <%=cellink.getId()%>
                        </td>
                        <td align="center">
                            <%if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE || cellink.getCellinkState().getValue() == CellinkState.STATE_START) {%>
                            <img src="img/online.gif" onmouseover="this.src='img/honline.gif'"
                                 onmouseout="this.src='img/online.gif'"
                                 title="<%=cellink.getName()%> (<%=session.getAttribute("cellink.state.online")%>)"
                                 onclick="window.document.location='./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>&screenId=1&doResetTimeout=true'">
                            <%} else if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                            <img src="img/running.gif" onmouseover="this.src='img/hrunning.gif'"
                                 onmouseout="this.src='img/running.gif'"
                                 title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.running")%>)"
                                 onclick="window.document.location='./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>&screenId=1&doResetTimeout=true'"/>
                            <%} else {%>
                            <img src="img/offline.gif"
                                 title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.offline")%>)"/>
                            <%}%>
                        </td>
                        <td align="center">
                            <%
                                if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE
                                        || cellink.getCellinkState().getValue() == CellinkState.STATE_START
                                        || cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {
                            %>
                            <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>"><%=cellink.getName()%>
                            </a>
                            <%} else {%>
                            <%=cellink.getName()%>
                            <%}%>
                        </td>
                        <td align="center">
                            <%=cellink.getVersion() %>
                        </td>
                        <td align="center">
                            <a href="./userinfo.html?userId=<%=cellink.getUserId()%>"><%=cellink.getUserId()%>
                            </a>
                        </td>
                        <td align="center">
                            <%=cellink.getSimNumber()%>
                        </td>
                        <td align="center">
                            <%=cellink.getFormatedTime()%>
                        </td>
                        <%if (cellink.getCellinkState() == CellinkState.ALIVE || cellink.getCellinkState() == CellinkState.START) {%>
                        <td align="center" valign="middle">
                            <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                <img src="img/display.png" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                     hspace="5"/><%=session.getAttribute("button.connect.cellink")%>
                            </a>
                        </td>
                        <%} else if (cellink.getCellinkState() == CellinkState.RUNNING) {%>
                        <td align="center" valign="middle">
                            <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                <img src="img/display.png" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                     hspace="5"/><%=session.getAttribute("button.chicken.coop")%>
                            </a>
                        </td>
                        <%} else {%>
                        <td align="center">
                            <img src="img/IconNotAvailable.gif" hspace="5"
                                 title="(<%=session.getAttribute("cellink.state.offline")%>)"/>
                            <%=session.getAttribute("button.noaction.cellink")%>
                        </td>
                        <%}%>
                        <td align="center" valign="middle">
                            <a href="./cellink-setting.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                <img src="img/setting_16.png" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                     hspace="5"/><%=session.getAttribute("button.setting")%>
                            </a>
                        </td>
                    </tr>
                    <% rows++;%>
                    <%}%>
                    <%}%>
                    </tbody>
                </table>
            </form>
        </td>
    </tr>
</table>
</div>


<script type="text/javascript" language="javascript">
    var state =
    <%=request.getParameter("state")%>
    var length = document.formFilter.cellinkStateFilter.options.length;
    for (var i = 0; i < length; i++) {
        if (document.formFilter.cellinkStateFilter.options[i].value == state) {
            document.formFilter.cellinkStateFilter.selectedIndex = i;
            break;
        }
    }
</script>
</body>
</html>