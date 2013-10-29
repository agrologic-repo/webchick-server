<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="com.agrologic.app.web.CellinkState" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Set" %>

<%@ include file="language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./logout.html");
        return;
    }

    int state;
    try {
        state = Integer.parseInt(request.getParameter("state"));
    } catch (Exception e) {
        state = -1;
    }
    request.setAttribute("state", state);

    Collection<Cellink> cellinks = (Collection<Cellink>) request.getAttribute("cellinks");
    String searchText = request.getParameter("searchText");
    if (searchText == null || searchText.equals("null")) {
        searchText = "";
    }
    request.setAttribute("searchText", searchText);
%>

<%! int countCellinksByState(Collection<Cellink> cellinks, int state) {
    int count = 0;
    for (Cellink cellink : cellinks) {
        if (cellink.getState() == state) {
            count++;
        }
    }
    return count;
}
%>
<%! Collection<Cellink> getCellinksByState(Collection<Cellink> cellinks, int state) {
    if (state == -1) {
        return cellinks;
    }
    Collection<Cellink> cellinkList = new ArrayList<Cellink>();
    for (Cellink cellink : cellinks) {
        if (cellink.getState() == state) {
            cellinkList.add(cellink);
        }
    }
    return cellinkList;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-us">
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Webchick overview</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <style type='text/css'>
        #search {
            cursor: pointer;
        }

        #refresh {
            cursor: pointer;
        }

        #filter {
            cursor: pointer;
        }
    </style>
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
                window.location.href = "./overview.html?state=" + state+"&searchText=" + text;
            });
        });//]]>

        $.tablesorter.addParser({
            // set a unique id
            id: 'dateTimeFormat',
            is: function (s) {
                return false;
            },
            format: function (s) {
                if(s == "") {
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
                sortList: [[1, 0]],widgets: ["zebra"],
                // These are detected by default,
                // but you can change or disable them
                headers: {
                    // Disable sorting on the first column
                    0: { sorter:false },
                    6: { sorter: "dateTimeFormat" },
                    7: { sorter:false }
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
            document.mainForm.action = "./disconnect-cellinks.html?userId=<%=user.getId()%>" + result;
            document.mainForm.method = "POST";
            document.mainForm.submit();
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
        <td style="vertical-align: top" width="20%">
            <h1><%=session.getAttribute("overview.page.title")%>
            </h1>
            <h2><%=session.getAttribute("overview.page.header")%>
            </h2>
        </td>
        <td colspan="2" width="50%">
            <jsp:include page="messages.jsp"/>
        </td>
        <td>
            <fieldset>
                <legend><b><%=session.getAttribute("cellink.states")%></b></legend>
                <table class="table-list-small" width="200px">
                    <tr>
                        <td>
                            <table border="0" style="border-collapse: collapse;">
                                <tr>
                                    <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                        <img class="images" src="resources/images/online.gif" style="vertical-align: middle"
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
    </tr>
    <tr bgcolor="#D5EFFF">
        <td align="justify" colspan="6">
            <input type="text" name="searchText" id="searchText" value="<%=searchText%>"/>
            <img id="search" src="resources/images/search.png" border="0" redirectUrl="./overview.html"/>
            <img id="refresh" src="resources/images/refresh.png" border="0" onclick="redirect('./overview.html')"/>

            <form id="formFilter" name="formFilter" style="display: inline;">
                <%=session.getAttribute("cellink.states")%> :
                <select id="filterStatus">
                    <option value="-1"></option>
                    <% Set<Map.Entry<String, Integer>> states = stateMap.entrySet();%>
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
            <jsp:include page="paging.jsp"/>
        </td>
    </tr>
</table>
<table border="0" cellPadding=1 cellSpacing=1 width="100%">
    <%cellinks = getCellinksByState(cellinks, state);%>
    <tr>
        <td colspan="4" width="100%">
            <form id="formFarms" name="formFarms" style="display:inline">
                <table id="table-cellinks" class="tablesorter">
                    <thead>
                    <tr>
                        <th>
                            <%--<input type="checkbox" name="checkAll" id="checkAll">--%>
                            <input type="checkbox" id="selectall"/>

                        </th>
                        <th width="50px"><span><%=session.getAttribute("table.col.cellink.id")%></span></th>
                        <th><%=session.getAttribute("table.col.cellink.name")%></th>
                        <th><%=session.getAttribute("table.col.cellink.version")%></th>
                        <th><%=session.getAttribute("table.col.cellink.user")%></th>
                        <th><%=session.getAttribute("table.col.cellink.sim")%></th>
                        <th><%=session.getAttribute("table.col.cellink.lastupdate")%></th>
                        <th colspan="2"><%=session.getAttribute("table.col.cellink.action")%></th>
                    </tr>
                    </thead>
                    <tbody>
                    <%if (cellinks.size() != 0) {%>
                    <%int rows = 0;%>
                    <%for (Cellink cellink : cellinks) {%>
                        <% if ((rows % 2) == 0) {%>
                            <tr class="odd" onMouseOver="changeOdd(this);" onmouseout="changeOdd(this)">
                        <%} else {%>
                            <tr class="even" onMouseOver="changeEven(this);" onmouseout="changeEven(this)">
                        <%}%>
                        <td>
                            <%--<input type="checkbox" id=cb<%=cellink.getId()%> name=cb<%=cellink.getId()%>/>--%>
                            <input type="checkbox" class="selectedId" id="cb<%=cellink.getId()%>" name="selectedId" value="<%=cellink.getId()%>" />
                        </td>
                        <td>
                            <%=cellink.getId()%>
                        </td>
                        <td>
                            <%if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE || cellink.getCellinkState().getValue() == CellinkState.STATE_START) {%>
                            <img src="resources/images/online.gif" onmouseover="this.src='resources/images/honline.gif'"
                                 onmouseout="this.src='resources/images/online.gif'"
                                 title="<%=cellink.getName()%> (<%=session.getAttribute("cellink.state.online")%>)"
                                 onclick="redirect('./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>&screenId=1&')"/>
                            <%} else if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                            <img src="resources/images/running.gif"
                                 onmouseover="this.src='resources/images/hrunning.gif'"
                                 onmouseout="this.src='resources/images/running.gif'"
                                 title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.running")%>)"
                                 onclick="window.location.href = './all-cellinks.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>&screenId=1&'"/>
                            <%} else {%>
                            <img src="resources/images/offline.gif"
                                 title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.offline")%>)"/>
                            <%}%>
                            <%
                                if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE
                                        || cellink.getCellinkState().getValue() == CellinkState.STATE_START
                                        || cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {
                            %>
                                <a href="rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                    <%=cellink.getName()%>
                                </a>
                            <%} else {%>
                                <%=cellink.getName()%>
                            <%}%>
                        </td>
                        <td>
                            <%=cellink.getVersion() %>
                        </td>
                        <td>
                            <a href="./userinfo.html?userId=<%=cellink.getUserId()%>"><%=cellink.getUserId()%>
                            </a>
                        </td>
                        <td>
                            <%=cellink.getSimNumber()%>
                        </td>
                        <td><%=cellink.getFormatedTime()%></td>
                        <%
                            if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE
                                    || cellink.getCellinkState().getValue() == CellinkState.STATE_START) {
                        %>
                        <td align="center" valign="middle">
                            <a href="rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                <img src="resources/images/display.png" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                     hspace="5"/><%=session.getAttribute("button.connect.cellink")%>
                            </a>
                        </td>
                        <%} else if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                        <td align="center" valign="middle">
                            <a href="rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                <img src="resources/images/display.png" style="cursor: pointer"
                                     title="<%=session.getAttribute("button.connect.cellink")%>" border="0"
                                     hspace="5"/><%=session.getAttribute("button.chicken.coop")%>
                            </a>
                        </td>
                        <%} else {%>
                        <td align="center">
                            <img src="resources/images/not-available.gif" hspace="5"
                                 title="(<%=session.getAttribute("cellink.state.offline")%>)"/>
                            <%=session.getAttribute("button.noaction.cellink")%>
                        </td>
                        <%}%>
                        <td align="center" valign="middle">
                            <a href="./cellink-setting.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                <img src="resources/images/setting.png" style="cursor: pointer"
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
    var state =<%=request.getParameter("state")%>
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