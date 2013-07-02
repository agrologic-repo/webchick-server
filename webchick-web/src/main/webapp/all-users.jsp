<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.CellinkDto" %>

<jsp:directive.page import="com.agrologic.app.web.CellinkState"/>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
//UserDto user = (UserDto) getServletContext().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<UserDto> users = (Collection<UserDto>) request.getSession().getAttribute("users");
    Collection<String> companies = (Collection<String>) request.getSession().getAttribute("companies");
    int from = (Integer) request.getSession().getAttribute("from");
    int to = (Integer) request.getSession().getAttribute("to");
    int of = (Integer) request.getSession().getAttribute("of");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=request.getSession().getAttribute("dir") %>">
<head>
<title><%=session.getAttribute("users.page.title")%>
</title>
<script language="javascript" src="js/general.js"></script>
<link rel="shortcut icon" href="img/favicon5.ico"/>
<link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
<link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
<script>
    //
    // global variables
    //
    var tbody = null;
    var theadrow = null;
    var colCount = null;
    var reverse = false;
    var lastclick = -1;					// stores the object of our last used object
    var oTR = null;
    var oStatus = null;
    var none = 0;
    function init() {

        // get TBODY - take the first TBODY for the table to sort
        tbody = element.tBodies(0);
        if (!tbody) return;

        //Get THEAD
        var thead = element.tHead;
        if (!thead)  return;

        theadrow = thead.children[0]; //Assume just one Head row
        if (theadrow.tagName != "TR") return;

        theadrow.runtimeStyle.cursor = "hand";

        colCount = theadrow.children.length;

        var l, clickCell;
        for (var i = 0; i < colCount; i++) {
            // Create our blank gif
            l = document.createElement("IMG");
            l.src = "img/empty.gif";
            l.id = "srtImg";
            l.width = 9;
            l.height = 5;

            clickCell = theadrow.children[i];

            clickCell.selectIndex = i;
            clickCell.insertAdjacentText("beforeEnd", "   ");
            clickCell.insertAdjacentElement("beforeEnd", l)
            clickCell.attachEvent("onclick", doClick);
        }

    }
    //
    // doClick handler
    //
    function doClick(e) {
        var clickObject = e.srcElement;

        while (clickObject.tagName != "TH") {
            clickObject = clickObject.parentElement;
        }


        // clear the sort images in the head
        var imgcol = theadrow.all('srtImg');
        for (var x = 0; x < imgcol.length; x++)
            imgcol[x].src = "img/empty.gif";

        if (lastclick == clickObject.selectIndex) {
            if (reverse == false) {
                clickObject.children[0].src = "img/downarrow.gif";
                reverse = true;
            }
            else {
                clickObject.children[0].src = "img/uparrow.gif";
                reverse = false;
            }
        }
        else {
            reverse = false;
            lastclick = clickObject.selectIndex;
            clickObject.children[0].src = "img/uparrow.gif";
        }

        insertionSort(tbody, tbody.rows.length - 1, reverse, clickObject.selectIndex);
        return false;
    }
    function insertionSort(t, iRowEnd, fReverse, iColumn) {


        var iRowInsertRow, iRowWalkRow, current, insert;
        for (iRowInsert = 0 + 1; iRowInsert <= iRowEnd; iRowInsert++) {
            if (iColumn) {
                if (typeof(t.children[iRowInsert].children[iColumn]) != "undefined")
                    textRowInsert = t.children[iRowInsert].children[iColumn].innerText;
                else
                    textRowInsert = "";
            } else {
                textRowInsert = t.children[iRowInsert].innerText;
            }

            for (iRowWalk = 0; iRowWalk <= iRowInsert; iRowWalk++) {
                if (iColumn) {
                    if (typeof(t.children[iRowWalk].children[iColumn]) != "undefined")
                        textRowCurrent = t.children[iRowWalk].children[iColumn].innerText;
                    else
                        textRowCurrent = "";
                } else {
                    textRowCurrent = t.children[iRowWalk].innerText;
                }

                //
                // We save our values so we can manipulate the numbers for
                // comparison
                //
                current = textRowCurrent;
                insert = textRowInsert;


                //  If the value is not a number, we sort normally, else we evaluate
                //  the value to get a numeric representation
                //
                if (!isNaN(current) || !isNaN(insert)) {
                    current = eval(current);
                    insert = eval(insert);
                }
                else {
                    current = current.toLowerCase();
                    insert = insert.toLowerCase();
                }


                if ((   (!fReverse && insert < current)
                        || ( fReverse && insert > current) )
                        && (iRowInsert != iRowWalk)) {
                    eRowInsert = t.children[iRowInsert];
                    eRowWalk = t.children[iRowWalk];
                    t.insertBefore(eRowInsert, eRowWalk);
                    iRowWalk = iRowInsert; // done
                }
            }
        }
    }
</script>
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
        window.document.location.replace("./all-users.html?role=" + role + "&company=" + company);

        return false;
    }
    function removeUser(userId) {
        if (confirm("This action will remove user from database.\nDo you want to continue ?") == true) {
            window.document.location.replace("./removeuser.html?userId=" + userId);
        }
    }
    function addUser() {
        window.document.location.replace("add-user.jsp");
        return false;
    }
    function userPropertyPieChart() {
        window.document.location.replace("./propertysummary.html");
        return false;
    }
    function back(link) {
        window.document.location.replace(link);
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
        window.document.location.replace("./all-users.html?searchText=" + searchText.value + "&role=" + role + "&company=" + company);
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
                            <%@include file="messages.jsp" %>
                        </td>
                        <td colspan="2">
                            <fieldset style="padding: 5px">
                                <legend><b><%=session.getAttribute("cellink.states")%>
                                </b></legend>
                                <table border="0" cellpadding="2" cellspacing="2" style="border-collapse: collapse;">
                                    <tr>
                                        <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                            <img src="img/online.gif" style="vertical-align: middle">
                                            - <%=session.getAttribute("cellink.state.online")%>&nbsp;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 1px 2px 1px 5px; vertical-align: middle">
                                            <img src="img/running.gif" style="vertical-align: middle">
                                            - <%=session.getAttribute("cellink.state.running")%>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 1px 2px 1px 5px; vertical-align: middle"><img
                                                src="img/offline.gif">
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
                                                    onclick="window.document.location='./all-users.html?userId=<%=user.getId()%>'"><%=session.getAttribute("button.refresh")%>
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
                <b>&nbsp;<%=users.size()%>&nbsp;</b><%=session.getAttribute("table.caption.user.records")%>
            </td>
        </tr>
        <tr>
            <form id="formUsers" name="formUsers">
                <td colspan="9" width="100%">
                    <%if (users.size() > 0) {%>
                    <table class="table-list" border="0" cellpadding="2" cellspacing="1" width="100%"
                           style="behavior:url(tablehl.htc) url(sort.htc);">
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
                        <% for (UserDto u : users) {%>
                        <% if ((rowCount % 2) == 0) {%>
                        <tr class="odd" onMouseOver="changeOdd(this);" onmouseout="changeOdd(this)">
                                <%} else {%>
                        <tr class="even" onMouseOver="changeEven(this);" onmouseout="changeEven(this)">
                            <%}%>
                            <td align="center" onclick="showInfo(<%=u.getId()%>)"><%=u.getId()%>
                            </td>
                            <td onclick="window.document.location='./userinfo.html?userId=<%=u.getId()%>'"><a
                                    href="./userinfo.html?userId=<%=u.getId()%>"><%=u.getLogin()%>
                            </a></td>
                            <td onclick="window.document.location='./userinfo.html?userId=<%=u.getId()%>'"><%=u.getFirstName()%>
                                &nbsp;<%=u.getLastName()%>
                            </td>
                            <td align="center"
                                onclick="window.document.location='./userinfo.html?userId=<%=u.getId()%>'"><%=u.getPhone()%>
                            </td>
                            <td align="center"
                                onclick="window.document.location='./userinfo.html?userId=<%=u.getId()%>'"><%=u.getCompany()%>
                            </td>
                            <td align="center">
                                <% Collection<CellinkDto> cellinks = u.getCellinks();
                                    for (CellinkDto cellink : cellinks) {
                                        if (cellink.getCellinkState().getValue() == CellinkState.STATE_ONLINE || cellink.getCellinkState().getValue() == CellinkState.STATE_START) {%>
                                <img src="img/online.gif" onmouseover="this.src='img/honline.gif'"
                                     onmouseout="this.src='img/online.gif'"
                                     title="<%=cellink.getName()%> (<%=session.getAttribute("cellink.state.online")%>)"
                                     onclick="window.document.location='./rmctrl-main-screen-ajax.jsp?userId=<%=u.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getId() %>&screenId=1&doResetTimeout=true'">
                                <%} else if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                                <img src="img/running.gif" onmouseover="this.src='img/hrunning.gif'"
                                     onmouseout="this.src='img/running.gif'"
                                     title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.running")%>)"
                                     onclick="window.document.location='./rmctrl-main-screen-ajax.jsp?userId=<%=u.getId()%>&cellinkId=<%=cellink.getId()%>&cellink=<%=cellink.getId() %>&screenId=1&doResetTimeout=true'"></img>
                                <%} else {%>
                                <img src="img/offline.gif"
                                     title="<%=cellink.getName()%>(<%=session.getAttribute("cellink.state.offline")%>)"></img>
                                <%}%>
                                <%}%>
                            </td>
                            <td align="center">
                                <img src="img/info.gif"/>
                                <a href="./userinfo.html?userId=<%=u.getId()%>">
                                    <%=session.getAttribute("button.info")%>
                                </a>
                            </td>
                            <td align="center">
                                <img src="img/edit.gif"/>
                                <a href="./edit-user.jsp?userId=<%=u.getId()%>">
                                    <%=session.getAttribute("button.edit")%>
                                </a>
                            </td>
                            <td align="center">
                                <img src="img/del.gif"/>
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