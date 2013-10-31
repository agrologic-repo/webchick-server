<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.CellinkState" %>
<%@ page import="com.agrologic.app.model.User" %>

<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<User> users = (Collection<User>) request.getAttribute("users");
    Collection<String> companies = (Collection<String>) session.getAttribute("companies");
    String searchText = request.getParameter("searchText");
    if (searchText == null || searchText.equals("null")) {
        searchText = "";
    }
    request.setAttribute("searchText", searchText);

    String role = request.getParameter("role");
    if (role == null || role.equals("null")) {
        role = "";
    }
    request.setAttribute("role", role);

    String userCompany = request.getParameter("company");
    if (userCompany == null || userCompany.equals("null")) {
        userCompany = "";
    }
    request.setAttribute("company", userCompany);
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("user.page.title")%>
    </title>
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
    <script type="text/javascript" src="resources/javascript/jquery.min.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-latest.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablesorter.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablesorter.min.js">;</script>
    <script type='text/javascript'>
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
                var company = $('#companyFilter :selected').val();
                var role = $('#roleFilter :selected').val();
                window.location.href = "./all-users.html?role=" + role + "&company=" + company;
            });
        });
        /**
         * sorting users table
         */
        $(document).ready(function () {
            $("#table-users").tablesorter({
                sortList: [[0, 0]], widgets: ['zebra'],
                // These are detected by default,
                // but you can change or disable them
                headers: {
                    // Disable sorting on the first column
                    5: { sorter:false },
                    6: { sorter:false }
                }

            });

        });
        //]]>
    </script>
    <script type="text/javascript">
        function confirmRemove() {
            return confirm("This action will remove user from database.\nDo you want to continue ?")
        }
        function addUser() {
            redirect("./add-user.jsp");
            return false;
        }
        function removeUser(userId) {
            if (confirm("This action will remove user from database.\nDo you want to continue ?") == true) {
                redirect("./removeuser.html?userId=" + userId);
            }
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
    </script>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td style="vertical-align: top" width="20%">
                <h1><%=session.getAttribute("user.page.header")%>
                </h1>

                <h2><%=session.getAttribute("user.page.sub.header")%>
                </h2>
            </td>
            <td align="center" colspan="1" width="60%">
                <jsp:include page="messages.jsp"/>
            </td>
            <td>
                <fieldset>
                    <legend><b><%=session.getAttribute("cellink.states")%>
                    </b></legend>
                    <table width="150px">
                        <tr>
                            <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                <img src="resources/images/online.gif" style="vertical-align: middle">
                                - <%=session.getAttribute("cellink.state.online")%>&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                <img src="resources/images/running.gif" style="vertical-align: middle;">
                                - <%=session.getAttribute("cellink.state.running")%>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="padding: 1px 2px 1px 5px; vertical-align: middle;">
                                <img src="resources/images/offline.gif">
                                - <%=session.getAttribute("cellink.state.offline")%>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </td>
            <form id="formFilter" name="formFilter" style="display:inline">
                <table width="100%" border="0" cellPadding="1" cellSpacing="1">
                    <tr bgcolor="#D5EFFF">
                        <td align="justify">
                            <input type="text" name="searchText" id="searchText" value="<%=searchText%>">
                            <img id="search" src="resources/images/search.png" border="0"
                                 redirectUrl="./all-users.html"/>
                            <img id="refresh" src="resources/images/refresh.png" border="0"
                                 onclick="redirect('./all-users.html?userId=<%=user.getId()%>')"/>
                            <%=session.getAttribute("user.role")%> :
                            <select id="roleFilter" name="roleFilter">
                                <option value="0" selected><%=session.getAttribute("user.role.all")%>
                                </option>
                                <option value="1"><%=session.getAttribute("user.role.admin")%>
                                </option>
                                <option value="2"><%=session.getAttribute("user.role.regular")%>
                                </option>
                                <option value="3"><%=session.getAttribute("user.role.advanced")%>
                                </option>
                            </select>&nbsp;
                            <%=session.getAttribute("user.company")%> :
                            <select id="companyFilter" name="companyFilter">
                                <option value="All"><%=session.getAttribute("user.role.all")%>
                                </option>
                                <% for (String c : companies) {%>
                                <option value="<%=c%>"><%=c%>
                                </option>
                                <%}%>
                            </select>
                            <img id="filter" src="resources/images/filter.png" border="0"/>
                        </td>
                        <td>
                            <button id="btnAdd" name="btnAdd"
                                    onclick='return addUser();'><%=session.getAttribute("button.add.user")%>
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <td>
                <jsp:include page="paging.jsp"/>
            </td>
        </tr>
        <tr>
            <form id="formUsers" name="formUsers">
                <td colspan="9" width="100%">
                    <%if (users.size() > 0) {%>
                    <table id="table-users" class="tablesorter" border="1">
                        <thead>
                        <tr>
                            <th><%=session.getAttribute("table.col.user.id")%></th>
                            <th><%=session.getAttribute("table.col.user.login")%></th>
                            <th><%=session.getAttribute("table.col.user.name")%></th>
                            <th><%=session.getAttribute("table.col.user.phone")%></th>
                            <th><%=session.getAttribute("table.col.user.company")%></th>
                            <th><%=session.getAttribute("table.col.user.cellinks")%></th>
                            <th colspan="3"><%=session.getAttribute("table.col.user.action")%></th>
                        </tr>
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
                            <td onclick="redirect('./userinfo.html?userId=<%=u.getId()%>')">
                                <a href="./userinfo.html?userId=<%=u.getId()%>"><%=u.getLogin()%>
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
                                <%
                                    Collection<Cellink> cellinks = u.getCellinks();
                                    for (Cellink cellink : cellinks) {
                                        if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE
                                                || cellink.getCellinkState().getValue() == CellinkState.STATE_START) {%>
                                <img src="resources/images/online.gif"
                                     onmouseover="this.src='resources/images/honline.gif'"
                                     onmouseout="this.src='resources/images/online.gif'"
                                     title="<%=cellink.getName()%> (<%=session.getAttribute("cellink.state.online")%>)"
                                     onclick="redirect('./rmctrl-main-screen-ajax.jsp?userId=<%=u.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getId() %>&screenId=1')">
                                <%} else if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                                <img src="resources/images/running.gif"
                                     onmouseover="this.src='resources/images/hrunning.gif'"
                                     onmouseout="this.src='resources/images/running.gif'"
                                     title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.running")%>)"
                                     onclick="redirect('./rmctrl-main-screen-ajax.jsp?userId=<%=u.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getId() %>&screenId=1')"/>
                                <%} else {%>
                                <img src="resources/images/offline.gif"
                                     title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.offline")%>)"/>
                                <%}%>
                                <%}%>
                            </td>
                            <td align="center">
                                <img src="resources/images/info.gif"/>
                                <a href="./userinfo.html?userId=<%=u.getId()%>">
                                    <%=session.getAttribute("button.info")%>
                                </a>
                            </td>
                            <td align="center">
                                <img src="resources/images/edit.gif"/>
                                <a href="./edituserrequest.html?userId=<%=u.getId()%>">
                                    <%=session.getAttribute("button.edit")%>
                                </a>
                            </td>
                            <td align="center">
                                <img src="resources/images/delete.gif"/>
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
                        onclick='return redirect("./main.jsp");'><%=session.getAttribute("button.back")%>
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