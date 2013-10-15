<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.CellinkState" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="../../anerrorpage.jsp" %>
<%@ include file="../../disableCaching.jsp" %>
<%@ include file="../../language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");

    Collection<User> users = (Collection<User>) request.getAttribute("users");
    Collection<String> companies = (Collection<String>) request.getAttribute("companies");
    int from = (Integer) request.getAttribute("from");
    int to = (Integer) request.getAttribute("to");
    int of = (Integer) request.getAttribute("of");
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
<title><%=session.getAttribute("users.page.title")%>
</title>
<link rel="StyleSheet" type="text/css" href="../../resources/style/admincontent.css"/>
<link rel="StyleSheet" type="text/css" href="../../resources/style/menubar.css"/>
<script type="text/javascript" src="remove-systemstate.html./../resources/javascript/general.js">;</script>
<script type="text/javascript">
    function confirmRemove() {
        return confirm("This action will remove user from database.\nDo you want to continue ?")
    }
    function filterUsers() {
        var role;
        if (document.formFilter.roleFilter.selectedIndex == 3) {
            role = 3;
        }
        if (document.formFilter.roleFilter.selectedIndex == 2) {
            role = 2;
        }
        if (document.formFilter.roleFilter.selectedIndex == 1) {
            role = 1;
        }
        if (document.formFilter.roleFilter.selectedIndex == 0) {
            role = 0;
        }

        var company;
        if (document.formFilter.companyFilter.selectedIndex == 0) {
            company = "All";

        } else {
            var selIndex = document.formFilter.companyFilter.selectedIndex
            company = document.formFilter.companyFilter.options[selIndex].value;
        }
        redirect("./all-users.html?role=" + role + "&company=" + company);

        return false;
    }
    function removeUser(userId) {
        if (confirm("This action will remove user from database.\nDo you want to continue ?") == true) {
            redirect("./removeuser.html?userId=" + userId);
        }
    }
    function addUser() {
        redirect("add-user.jsp");
        return false;
    }
    function userPropertyPieChart() {
        redirect("./propertysummary.html");
        return false;
    }

    function showInfo(id) {
        var showDiv = document.getElementById("showinfo" + id);
        showDiv.style.display = "block";

    }
    function hideInfo(id) {
        var showDiv = document.getElementById("showinfo" + id);
        showDiv.style.display = "none";
    }
    function searchUser() {
        var role;
        if (document.formFilter.roleFilter.selectedIndex == 3) {
            alert(role);
            role = 3;
        }
        if (document.formFilter.roleFilter.selectedIndex == 2) {
            role = 2;
        }
        if (document.formFilter.roleFilter.selectedIndex == 1) {
            role = 1;
        }
        if (document.formFilter.roleFilter.selectedIndex == 0) {
            role = 0;
        }

        var company;
        if (document.formFilter.companyFilter.selectedIndex == 0) {
            company = "All";

        } else {
            var selIndex = document.formFilter.companyFilter.selectedIndex
            company = document.formFilter.companyFilter.options[selIndex].value;
        }
        var searchText = document.getElementById('searchText');
        redirect("./all-users.html?searchText=" + searchText.value + "&role=" + role + "&company=" + company);
        return false;
    }
</script>
</head>
<body>
<div id="header">
    <%@include file="../../usermenuontop.jsp" %>
</div>
<div id="main-shell">
<table border="0" cellPadding=1 cellSpacing=1 width="100%">
<tr>
    <td valign="top">
        <table border="0" cellPadding=1 cellSpacing=1 width="100%">
            <tr>
                <td style="vertical-align: top">
                    <h1><%=session.getAttribute("users.page.header")%>
                    </h1>

                    <h2><%=session.getAttribute("users.page.sub.header")%>
                    </h2>
                </td>
                <td align="center" colspan="1">
                    <jsp:include page="../../messages.jsp"/>
                </td>
                <td colspan="2">
                    <fieldset style="padding: 5px">
                        <legend><b><%=session.getAttribute("cellink.states")%>
                        </b></legend>
                        <table border="0" cellpadding="2" cellspacing="2" style="border-collapse: collapse;">
                            <tr>
                                <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                    <img src="../../resources/images/online.gif" style="vertical-align: middle">
                                    - <%=session.getAttribute("cellink.state.online")%>&nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                    <img src="../../resources/images/running.gif" style="vertical-align: middle">
                                    - <%=session.getAttribute("cellink.state.running")%>&nbsp;</td>
                            </tr>
                            <tr>
                                <td style="padding: 1px 2px 1px 5px; vertical-align: middle"><img
                                        src="../../resources/images/offline.gif">
                                    - <%=session.getAttribute("cellink.state.offline")%>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td width="100%" colspan="5">
                    <form id="formFilter" name="formFilter">
                        <table border="0" cellPadding="1" cellSpacing="1">
                            <tr>
                                <td>
                                    <%=session.getAttribute("user.login")%>:&nbsp;<input type="text"
                                                                                         name="searchText"
                                                                                         id="searchText"
                                                                                         size="15">
                                    <button id="btnSearch" name="btnSearch"
                                            onclick="return searchUser('');"><%=session.getAttribute("button.search")%>
                                    </button>
                                    <%=session.getAttribute("user.role")%> :
                                    <select id="roleFilter" name="roleFilter">
                                        <option value="0" selected><%=session.getAttribute("user.role.all")%>
                                        </option>
                                        <option value="1">
                                            <%=session.getAttribute("user.role.admin")%>
                                        </option>
                                        <option value="2">
                                            <%=session.getAttribute("user.role.regular")%>
                                        </option>
                                        <option value="3">
                                            <%=session.getAttribute("user.role.advanced")%>
                                        </option>
                                    </select>&nbsp;
                                </td>
                                <td>
                                    <%=session.getAttribute("user.company")%> :
                                    <select id="companyFilter" name="companyFilter">
                                        <option value="All"><%=session.getAttribute("user.role.all")%>
                                        </option>
                                        <% for (String c : companies) {%>
                                        <option value="<%=c%>"><%=c%>
                                        </option>
                                        <%}%>
                                    </select>
                                </td>
                                <td>
                                    <button id="btnFilter" name="btnFilter"
                                            onclick="return filterUsers();"><%=session.getAttribute("button.filter")%>
                                    </button>
                                </td>
                                <td>
                                    <button id="btnRefresh" name="btnRefresh"
                                            onclick="redirect('./all-users.html?userId=<%=user.getId()%>')"><%=session.getAttribute("button.refresh")%>
                                    </button>
                                    <button id="btnAdd" name="btnAdd"
                                            onclick='return addUser();'><%=session.getAttribute("button.add.user")%>
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </form>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td>
        <jsp:include page="../../paging.jsp"/>
    </td>
</tr>
<tr>
    <form id="formUsers" name="formUsers">
        <td colspan="9" width="100%">
            <%if (users.size() > 0) {%>
            <table class="table-list" border="0" cellpadding="2" cellspacing="1" width="100%">
                <thead>
                <th align="center" style="min-width: 40px; width: auto;">
                    <%=session.getAttribute("table.col.user.id")%>
                </th>
                <th align="center" width="100" nowrap>
                    <%=session.getAttribute("table.col.user.login")%>
                </th>
                <th align="center" width="100" nowrap>
                    <%=session.getAttribute("table.col.user.name")%>
                </th>
                <th align="center" width="100" nowrap>
                    <%=session.getAttribute("table.col.user.phone")%>
                </th>
                <th align="center" width="150" nowrap>
                    <%=session.getAttribute("table.col.user.company")%>
                </th>
                <th align="center" width="100" nowrap>
                    <%=session.getAttribute("table.col.user.cellinks")%>
                </th>
                <th align="center" width="300" nowrap colspan="3">
                    <%=session.getAttribute("table.col.user.action")%>
                </th>
                </thead>
                <tbody>
                <% int rowCount = 0;%>
                <% for (User u : users) {%>
                <% if ((rowCount % 2) == 0) {%>
                <tr class="odd" onMouseOver="changeOdd(this);" onmouseout="changeOdd(this)">
                        <%} else {%>
                <tr class="even" onMouseOver="changeEven(this);" onmouseout="changeEven(this)">
                    <%}%>
                    <td align="center" onclick="showInfo(<%=u.getId()%>)"><%=u.getId()%>
                    </td>
                    <td onclick="redirect('./userinfo.html?userId=<%=u.getId()%>')"><a
                            href="./userinfo.html?userId=<%=u.getId()%>"><%=u.getLogin()%>
                    </a></td>
                    <td onclick="redirect('./userinfo.html?userId=<%=u.getId()%>')"><%=u.getFirstName()%>
                        &nbsp;<%=u.getLastName()%>
                    </td>
                    <td align="center"
                        onclick="redirect('./userinfo.html?userId=<%=u.getId()%>')"><%=u.getPhone()%>
                    </td>
                    <td align="center"
                        onclick="redirect('./userinfo.html?userId=<%=u.getId()%>')"><%=u.getCompany()%>
                    </td>
                    <td align="center">
                        <% Collection<Cellink> cellinks = u.getCellinks();
                            for (Cellink cellink : cellinks) {
                                if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE || cellink.getCellinkState().getValue() == CellinkState.STATE_START) {%>
                        <img src="../../resources/images/online.gif" onmouseover="this.src='resources/images/honline.gif'"
                             onmouseout="this.src='resources/images/online.gif'"
                             title="<%=cellink.getName()%> (<%=session.getAttribute("cellink.state.online")%>)"
                             onclick="redirect('./rmctrl-main-screen-ajax.jsp?userId=<%=u.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getId() %>&screenId=1&doResetTimeout=true')">
                        <%} else if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                        <img src="../../resources/images/running.gif" onmouseover="this.src='resources/images/hrunning.gif'"
                             onmouseout="this.src='resources/images/running.gif'"
                             title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.running")%>)"
                             onclick="redirect('./rmctrl-main-screen-ajax.jsp?userId=<%=u.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getId() %>&screenId=1&doResetTimeout=true')"/>
                        <%} else {%>
                        <img src="../../resources/images/offline.gif" title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.offline")%>)"/>
                        <%}%>
                        <%}%>
                    </td>
                    <td align="center">
                        <img src="../../resources/images/info.gif"/>
                        <a href="./userinfo.html?userId=<%=u.getId()%>">
                            <%=session.getAttribute("button.info")%>
                        </a>
                    </td>
                    <td align="center">
                        <img src="../../resources/images/edit.gif"/>
                        <a href="../../edit-user.jsp?userId=<%=u.getId()%>">
                            <%=session.getAttribute("button.edit")%>
                        </a>
                    </td>
                    <td align="center">
                        <img src="../../resources/images/delete.gif"/>
                        <a href="javascript:removeUser(<%=u.getId()%>);">
                            <%=session.getAttribute("button.delete")%>
                        </a>
                    </td>
                </tr>
                <%rowCount++;%>
                <%}%>
                </tbody>
            </table>
            <%}%>
        </td>
    </form>
</tr>
<tr>
    <td colspan="9">
        <button id="btnBack" name="btnBack"
                onclick='return back("./main.jsp");'><%=session.getAttribute("button.back")%>
        </button>
    </td>
</tr>
</table>

</div>
<script type="text/javascript" language="javascript">
    var role =
    <%=request.getParameter("role")%>
    if (role == null) {
        document.formFilter.roleFilter.selectedIndex = 0;
    } else {
        document.formFilter.roleFilter.selectedIndex = role;
    }

    var length = document.formFilter.companyFilter.options.length;
    var company = '<%=request.getParameter("company")%>';
    for (var i = 0; i < length; i++) {
        if (document.formFilter.companyFilter.options[i].value == company) {
            document.formFilter.companyFilter.selectedIndex = i;
            break;
        }
    }
</script>
</body>
</html>