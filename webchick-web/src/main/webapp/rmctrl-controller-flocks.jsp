<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.ControllerDto" %>
<%@ page import="com.agrologic.app.model.FlockDto" %>

<jsp:directive.page import="java.util.Collection"/>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Collection<ControllerDto> controllers = (Collection<ControllerDto>) request.getSession().getAttribute("controllers");
    String direction = (String) request.getSession().getAttribute("dir");
    String align;
    if (direction.equals("ltr")) {
        align = "left";
    } else {
        align = "right";
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("flock.page.title")%>
    </title>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="css/calendar.css"/>
    <script type="text/javascript" src="js/calendar.js"></script>
    <script type="text/javascript">
        function validate() {
            var flockName = document.getElementById('flockName').value;
            var startDate = document.getElementById('startDate').value;
            document.getElementById('msg').style.display = 'none';
            if (flockName == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (startDate == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            document.addform.submit();
        }
        function removeFlock(flockId) {
            if (confirm("Are you sure ?") == true) {
                window.document.location.replace("./removeflock.html?userId=<%=userId%>&cellinkId="+<%=cellinkId%>+
                "&flockId=" + flockId
            )
                ;
            }
        }
        function closeFlock(flockid) {
            var endDate = document.getElementById('end' + flockid + 'Date').value;
            document.getElementById('msg').style.display = 'none';
            if (endDate == "") {
                alert("Field End Date can not be empty !")
                return;
            }

            window.document.location.replace("./closeflock.html?userId=<%=userId%>&cellinkId="+<%=cellinkId%>+
            "&flockId=" + flockid + "&endDate=" + endDate
        )
            ;
        }
        function back(link) {
            window.document.location.replace(link);
            return false;
        }
    </script>
</head>
<body>
<table width="100%">
<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%">
            <table width="100%">
                <tr>
                    <td width="20%">
                        <%@include file="toplang.jsp" %>
                    </td>
                    <td align="center">
                        <h1 style="text-align: center;"><%=session.getAttribute("flock.page.title")%>
                        </h1>
                        <jsp:include page="/cellinkname.html?cellinkId=<%=cellinkId%>"/>

                    </td>
                    <td>
                        <img src="img/display.png" style="cursor: pointer" border="0" hspace="5"/>
                        <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1&doResetTimeout=true">
                            <%=session.getAttribute("button.screens")%>
                        </a>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
</tr>
<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%">
            <table width="100%">
                <tr>
                    <td align="<%=align%>">
                        <form name="addform" id="addform" action="./addflock.html" method="post">
                            <input id="userId" type="hidden" name="userId" value="<%=userId%>"></input>
                            <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"></input>
                            <table width="100%">
                                <tr>
                                    <td>
                                        <h2><%=session.getAttribute("table.caption.flock")%>
                                        </h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table id="msg" class="errMsg" align="center" style="display: none;">
                                            <tr>
                                                <td><%=session.getAttribute("label.message.feild.empty")%>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table class="table-list" border=1 width="100%"
                                               style="behavior:url(tablehl.htc) url(sort.htc);">
                                            <thead>
                                            <th align="center" width="100px"
                                                onmouseover="this.style.background='#ADD8E6'"
                                                onmouseout="this.style.background='#D5EFFF'">
                                                <%=session.getAttribute("table.col.flock.title")%>
                                            </th>
                                            <th align="center" width="150px"
                                                onmouseover="this.style.background='#ADD8E6'"
                                                onmouseout="this.style.background='#D5EFFF'">
                                                <%=session.getAttribute("table.col.flock.house")%>
                                            </th>
                                            <th align="center" width="100px"
                                                onmouseover="this.style.background='#ADD8E6'"
                                                onmouseout="this.style.background='#D5EFFF'">
                                                <%=session.getAttribute("table.col.flock.status")%>
                                            </th>
                                            <th align="center" width="150px"
                                                onmouseover="this.style.background='#ADD8E6'"
                                                onmouseout="this.style.background='#D5EFFF'">
                                                <%=session.getAttribute("table.col.flock.start.date")%>
                                            </th>
                                            <th align="center" width="150px"
                                                onmouseover="this.style.background='#ADD8E6'"
                                                onmouseout="this.style.background='#D5EFFF'">
                                                <%=session.getAttribute("table.col.flock.end.date")%>
                                            </th>
                                            <th align="center" width="250px" colspan="4"
                                                onmouseover="this.style.background='#ADD8E6'"
                                                onmouseout="this.style.background='#D5EFFF'">
                                                <%=session.getAttribute("table.col.flock.action")%>
                                            </th>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td align="center"><input type="text" value="" name="flockName"
                                                                          id="flockName"></td>
                                                <td align="center">
                                                    <select style="width:135px" id="house_Filter"
                                                            name="house_Filter">
                                                        <%for (ControllerDto c : controllers) { %>
                                                        <option value="<%=c.getId() %>"><%=c.getTitle()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </td>
                                                <td align="center">
                                                    <select id="status_Filter" name="status_Filter">
                                                        <option value="Open">Open</option>
                                                        <option value="Close">Close</option>
                                                    </select>
                                                </td>
                                                <td align="center"><input type="text" value="" size="10" readonly
                                                                          name="startDate" id="startDate">
                                                    <img src="img/calendar.png" border="0"
                                                         onclick="GetDate('start');"/></td>
                                                <td align="center"><input type="text" value="" size="10" readonly>
                                                    <img src="img/calendar.png" border="0"
                                                         style="filter: alpha(opacity=30);opacity: .30;background-color:#111"/>
                                                </td>
                                                <td align="center"><a href="javascript:validate();">
                                                    <img src="img/plus1.gif" hspace="5" style="cursor: pointer"
                                                         border="0" space="5"><br/></img>
                                                    <%=session.getAttribute("button.add")%>
                                                </a>
                                                </td>
                                                <td align="center">&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                            </tr>
                                            <% for (ControllerDto controller : controllers) {%>
                                            <% Collection<FlockDto> flocks = controller.getFlocks(); %>
                                            <% if (flocks != null && flocks.size() > 0) {%>
                                            <% for (FlockDto flock : flocks) {%>
                                            <% long fid = flock.getFlockId(); %>
                                            <input id="controllerId" type="hidden"
                                                   value="<%=flock.getControllerId() %>"></input>
                                            <input id="programId" type="hidden"
                                                   value="<%=controller.getProgramId() %>"></input>

                                            <tr>
                                                <td align="center"><%=flock.getFlockName()%>
                                                </td>
                                                <td align="center"><%=controller.getTitle()%>
                                                </td>
                                                <td align="center"><%=flock.getStatus()%>
                                                </td>
                                                <td align="center"><input type="text"
                                                                          value="<%=flock.getStartTime()%>"
                                                                          size="10" readonly>
                                                    <img src="img/calendar.png" border="0"
                                                         style="filter: alpha(opacity=30);opacity: .30;background-color:#111"/>
                                                </td>
                                                <td align="center">
                                                    <% if (!flock.getStatus().equals("Close")) {%>
                                                    <input type="text" value="" readonly size="10"
                                                           name="end<%=fid%>Date" id="end<%=fid%>Date">
                                                    <img src="img/calendar.png" border="0"
                                                         onclick="GetDate('end<%=fid%>');"/>
                                                    <%} else {%>
                                                    <input type="text" value="<%=flock.getEndTime()%>" size="10"
                                                           readonly>
                                                    <img src="img/calendar.png" border="0"
                                                         style="filter: alpha(opacity=30);opacity: .30;background-color:#111"/>
                                                    <%}%>
                                                </td>
                                                <td align="center">
                                                    <% if (!flock.getStatus().equals("Close")) {%>
                                                    <a href="javascript:closeFlock(<%=flock.getFlockId()%>);">
                                                        <img src="img/lock.gif" hspace="5" style="cursor: pointer"
                                                             border="0" space="5"><br/></img>
                                                        <%=session.getAttribute("button.close")%>
                                                    </a>
                                                    <%}%>
                                                </td>
                                                <td align="center">
                                                    <a href="./rmctrl-flock-graphs.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=<%=controller.getId()%>&flockId=<%=flock.getFlockId() %>&programId=<%=controller.getProgramId()%>">
                                                        <img src="img/graph2.gif" hspace="5" style="cursor: pointer"
                                                             border="0"><br/></img>
                                                        <%=session.getAttribute("button.graphs")%>
                                                    </a>
                                                </td>
                                                <td align="center">
                                                    <a href="./rmctrl-flock-management.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=<%=controller.getId()%>&flockId=<%=flock.getFlockId() %>">
                                                        <img src="img/summary.gif" hspace="5"
                                                             style="cursor: pointer" border="0"><br/></img>
                                                        <%=session.getAttribute("button.manage")%>
                                                    </a>
                                                </td>
                                                <td align="center">
                                                    <a href="javascript:removeFlock(<%=flock.getFlockId()%>);">
                                                        <img src="img/close.png" hspace="5" style="cursor: pointer"
                                                             border="0" space="5"><br/></img>
                                                        <%=session.getAttribute("button.delete")%>
                                                    </a>
                                                </td>
                                            </tr>
                                            <%}%>
                                            <%}%>
                                            <%}%>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="<%=align%>">
                                        <button id="btnBack" name="btnBack"
                                                onclick='return back("./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1&doResetTimeout=true")'>
                                            <%=session.getAttribute("button.back") %>
                                        </button>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
</tr>
</table>
</body>
</html>
