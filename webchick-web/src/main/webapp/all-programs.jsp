<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.Program" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<Program> programs = (Collection<Program>) request.getAttribute("programs");
    String searchText = request.getParameter("searchText");
    if (searchText == null || searchText.equals("null")) {
        searchText = "";
    }
    request.setAttribute("searchText", searchText);

%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("maintenance.page.title")%>
    </title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <style type='text/css'>
        #search {
            cursor: pointer
        }

        #refresh {
            cursor: pointer;
        }
    </style>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-latest.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablesorter.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablesorter.min.js">;</script>

    <script type='text/javascript'>
    //<![CDATA[
    $(window).load(function () {
        $("#search").click(function () {
            var text = $("#searchText").val();
            var redirectUrl = $(this).attr('redirectUrl');
            window.location.href = redirectUrl + "?searchText=" + text;
        });
    });

    $.tablesorter.addParser({
        // set a unique id
        id: "dateTimeFormat",
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

                var result = day + month + year + hour + minutes;
                return result;
            }
        },
        type: 'numeric'
    });

    $(function () {
        $('#table-programs').tablesorter({
            sortList: [[0, 0]], widgets: ['zebra'],
            // These are detected by default,
            // but you can change or disable them
            headers: {
                2: { sorter: "dateTimeFormat" },
                3: { sorter: "dateTimeFormat" },
                4: { sorter: false }
            }
        });
    });
    //]]>
    </script>
    <script type="text/javascript">

        function addProgram() {
            <% if (user.getRole() == UserRole.USER || user.getRole() == UserRole.DISTRIBUTOR) {%>
            redirect("./access-denied.jsp");
            <%} else {%>
            redirect("./add-program.jsp");
            <%}%>
            return false;
        }
        function confirmRemove() {
            return confirm("This action will remove program from database.\nDo you want to continue ?");
        }

        function removeProgram(programId) {
            if (confirm("Are you sure ?") == true) {
                redirect("./removeprogram.html?programId=" + programId);
            }
        }
        function filterPrograms() {
            var userId = document.formFilterPrograms.Program_Filter.value;
            redirect("./all-programs.html?userId=" + userId);
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
            <td>
                <h1><%=session.getAttribute("maintenance.page.header")%>
                </h1>

                <h2><%=session.getAttribute("maintenance.page.sub.header")%>
                </h2>
                <%@ include file="messages.jsp" %>
            </td>
        </tr>
        <tr bgcolor="#D5EFFF">
            <td align="">
                <input type="text" id="searchText" value="<%=searchText%>"/>
                <img id="search" src="resources/images/search.png" border="0" redirectUrl="./all-programs.html"/>
                <img id="refresh" src="resources/images/refresh.png" border="0"
                     onclick="redirect('./all-programs.html')"/>
            <% if (user.getRole() == UserRole.ADMIN) {%>
                    <button type="button" onclick="redirect('./add-program.jsp')">
                        &nbsp;<%=session.getAttribute("button.add.program")%>
                    </button>
                    <button type="button" onclick="redirect('./all-relays.html')">
                        <%=session.getAttribute("button.add.relay")%>
                    </button>
                    <button type="button" onclick="redirect('./all-alarms.html')">
                        <%=session.getAttribute("button.add.alarm")%>
                    </button>
                    <button type="button" onclick="redirect('./all-systemstates.html')">
                        <%=session.getAttribute("button.add.system-state")%>
                    </button>
                <%}%>
            </td>
        </tr>
        <tr>
            <td>
                <jsp:include page="paging.jsp"/>
            </td>
        </tr>
        <tr>
            <td colspan="">
                <form id="formPrograms" name="formPrograms">
                    <table id="table-programs" class="tablesorter">
                        <thead>
                        <tr>
                            <th><%=session.getAttribute("table.col.program.id")%></th>
                            <th><%=session.getAttribute("table.col.program.name")%></th>
                            <th><%=session.getAttribute("table.col.program.created")%></th>
                            <th><%=session.getAttribute("table.col.program.modified")%></th>
                            <th colspan="3"><%=session.getAttribute("table.col.program.action")%></th>
                        </tr>
                        </thead>
                        <tbody>
                        <%int rawCount = 0;%>
                        <%for (Program program : programs) {%>
                        <tr>
                            <td><%=program.getId()%></td>
                            <td><a href="./all-screens.html?programId=<%=program.getId()%>"><%=program.getName()%></a></td>
                            <td><%=program.getCreatedDate()%></td>
                            <td><%=program.getModifiedDate()%></td>
                            <td align="center">
                                <img src="resources/images/preview.png" style="cursor: pointer" hspace="5" border="0"/>
                                <a href="./screen-preview.html?programId=<%=program.getId()%>">
                                    <%=session.getAttribute("button.preview")%>
                                </a>
                            </td>
                            <td align="center">
                                <img src="resources/images/edit.gif" style="cursor: pointer" hspace="5" border="0"/>
                                <a href="./editprogramrequest.html?programId=<%=program.getId()%>">
                                    <%=session.getAttribute("button.edit")%>
                                </a>
                            </td>
                            <td align="center">
                                <img src="resources/images/close.png" style="cursor: pointer" hspace="5" border="0"/>
                                <a href="./removeprogram.html?programId=<%=program.getId()%>"
                                   onclick="return confirmRemove();">
                                    <%=session.getAttribute("button.delete")%>
                                </a>
                            </td>
                        </tr>
                        <%rawCount++;%>
                        <%}%>
                        </tbody>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>

</body>
</html>