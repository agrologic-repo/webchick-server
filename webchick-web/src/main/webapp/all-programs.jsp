<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="java.util.List"%>
<%@ page import="java.lang.Boolean"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>

<%  UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    List<ProgramDto> programs = (List<ProgramDto>) request.getSession().getAttribute("programs");
    int from = (Integer) request.getSession().getAttribute("from");
    int to = (Integer) request.getSession().getAttribute("to");
    int of = (Integer) request.getSession().getAttribute("of");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html dir="<%=request.getSession().getAttribute("dir") %>">
    <head>
        <title><%=session.getAttribute("maintenance.page.title")%></title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <script language="javascript" src="js/general.js"></script>
        <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <script type="text/javascript">
            function addProgram(){
                <% if (user.getRole() == UserRole.REGULAR || user.getRole() == UserRole.ADVANCED) {%>
                        window.document.location.replace("./access-denied.jsp");
                <%} else {%>
                        window.document.location.replace("./add-program.jsp");
                <%}%>
                return false;
            }
            function confirmRemove() {
                return confirm("This action will remove program from database.\nDo you want to continue ?");
            }
            function removeProgram(programId) {
                if(confirm("Are you sure ?") ==  true) {
                    window.document.location.replace("./removeprogram.html?programId="+programId);
                }
            }
            function filterPrograms() {
                var userId = document.formFilterPrograms.Program_Filter.value;
                window.document.location.replace("./all-programs.html?userId="+userId);
                return false;
            }
            function searchProgram()
                {
                    var searchText = document.getElementById('searchText');
                    window.document.location.replace("./all-programs.html?searchText=" + searchText.value);
                    return false;
                }
        </script>
    </head>
    <body>
        <div id="header">
            <%@include file="usermenuontop.jsp"%>
        </div>
        <div id="main-shell">
            <table border="0" cellPadding=1 cellSpacing=1 width="100%">
                <tr>
                    <td>
                        <h1><%=session.getAttribute("maintenance.page.header")%></h1>
                        <h2><%=session.getAttribute("maintenance.page.sub.header")%></h2>
                    </td>
                    <td>
                        <%@ include file="messages.jsp"%>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <%=session.getAttribute("table.col.program.id")%> :&nbsp;<input type="text" name="searchText" id="searchText">
                            <button id="btnSearch" name="btnSearch" onclick="return searchProgram();"><%=session.getAttribute("button.search")%></button>
                            <button id="btnRefresh" name="btnRefresh" onclick="window.document.location='./all-programs.html?userId=<%=user.getId()%>'"><%=session.getAttribute("button.refresh")%></button>
                            <button type="button" onclick="javascript:addProgram();">&nbsp;<%=session.getAttribute("button.add.program")%></button>
                            <button id="btnView" name="btnView" onclick="window.document.location='./all-relays.html'"><%=session.getAttribute("button.add.relay")%></button>
                            <button id="btnView" name="btnView" onclick="window.document.location='./all-alarms.html'"><%=session.getAttribute("button.add.alarm")%></button>
                            <button id="btnView" name="btnView" onclick="window.document.location='./all-systemstates.html'"><%=session.getAttribute("button.add.systemstate")%></button>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <table class="table-pages">
                        <tr>
                            <td style="width: 80%;">Showing <%=from%>-<%=to%> of <%=of%></td>
                            <td style="width: 20%;" nowrap>
                            <%int pages = (of % 25) > 0 ? (of / 25) + 1:(of / 25);%>
                            <%for (int i = 0; i < pages; i++) {%>
                            <%if (i * 25 <= from && (i * 25) + 25 >= to) {%>
                            <a class="active" href="all-programs.html?index=<%=i * 25%>"><%=i + 1%></a>
                            <%} else {%>
                            <a href="all-programs.html?index=<%=i * 25%>"><%=i + 1%></a>
                            <%}%>
                            <%}%>
                            </td>
                        </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <form id="formPrograms" name="formPrograms">
                            <table class="table-list" border="1" style="behavior:url(tablehl.htc) url(sort.htc);">
                                <thead>
                                    <th class="centerHeader"><%=session.getAttribute("table.col.program.id")%></th>
                                    <th class="centerHeader"><%=session.getAttribute("table.col.program.created")%></th>
                                    <th class="centerHeader"><%=session.getAttribute("table.col.program.modified")%></th>
                                    <th class="centerHeader" colspan="3"><%=session.getAttribute("table.col.program.action")%></th>
                                </thead>
                                <tbody>
                                    <%int rawCount = 0;%>
                                    <%for (ProgramDto program : programs) {%>
                                    <% if ((rawCount % 2) == 0) {%>
                                        <tr class="odd" onMouseOver="changeOdd(this);"  onmouseout="changeOdd(this)">
                                    <%} else {%>
                                        <tr class="even" onMouseOver="changeEven(this);"   onmouseout="changeEven(this)">
                                    <%}%>
                                            <td style="padding-left:25px" width="150px" align="left">
                                                <a href="./all-screens.html?programId=<%=program.getId()%>"><%=program.getName()%></a>
                                            </td>

                                            <td style="padding-left:25px" width="150px" align="left">
                                                <%=program.getCreatedDate()%>
                                            </td>

                                            <td style="padding-left:25px" width="150px" align="left">
                                                <%=program.getModifiedDate()%>
                                            </td>

                                            <td align="center">
                                                <img src="img/preview.png" style="cursor: pointer" hspace="5" border="0"/>
                                                <a href="./screen-preview.html?programId=<%=program.getId()%>">
                                                    <%=session.getAttribute("button.preview")%>
                                                </a>
                                            </td>
                                            <td align="center">
                                                <img src="img/edit.gif" style="cursor: pointer" hspace="5" border="0"/>
                                                <a href="./edit-program.jsp?programId=<%=program.getId()%>">
                                                    <%=session.getAttribute("button.edit")%></a>
                                            </td>
                                            <td align="center">
                                                <img src="img/close.png" style="cursor: pointer" hspace="5" border="0"/>
                                                <a href="./removeprogram.html?programId=<%=program.getId()%>" onclick="return confirmRemove();">

                                                    <%=session.getAttribute("button.delete")%></a>
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