<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.Flock" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Cellink cellink = (Cellink) request.getAttribute("cellink");
    Collection<Controller> controllers = (Collection<Controller>) request.getAttribute("controllers");
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("flock.page.title")%>
    </title>
    <link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/calendar.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>

    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
    <script type="text/javascript" src="resources/javascript/calendar.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
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
                redirect("./removeflock.html?userId=<%=userId%>&cellinkId=" + <%=cellinkId%> +
                        "&flockId=" + flockId);
            }
        }
        function closeFlock(flockid) {
            var endDate = document.getElementById('end' + flockid + 'Date').value;
            alert(endDate);
            document.getElementById('msg').style.display = 'none';
            if (endDate == "") {
                alert("Field End Date can not be empty !")
                return;
            }

            redirect("./closeflock.html?userId=<%=userId%>&cellinkId=" + <%=cellinkId%> +
                    "&flockId=" + flockid + "&endDate=" + endDate);
        }
    </script>
</head>
<body>
<table width="100%">
<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
            <table width="85%" border="0">
                <tr>
                    <td width="20%">
                        <%@include file="toplang.jsp" %>
                    </td>
                    <td align="center">
                        <h1 style="text-align: center;"><%=session.getAttribute("flock.page.title")%>
                        </h1>

                        <h2><%=cellink.getName() %>
                        </h2>
                    </td>
                    <td width="20%">
                        <a href="rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1">
                            <img src="resources/images/display.png" style="cursor: pointer" border="0"/>
                            &nbsp;<%=session.getAttribute("button.screens")%>&nbsp;
                        </a>
                        <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                            <img src="resources/images/chicken-icon.png" style="cursor: pointer" border="0"/>
                            <%=session.getAttribute("main.screen.page.flocks")%>
                        </a>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
</tr>

<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
            <table width="100%">
                <tr>
                    <td>
                        <form name="addform" id="addform" action="./addflock.html" method="post">
                            <input id="userId" type="hidden" name="userId" value="<%=userId%>"/>
                            <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
                            <table width="100%">
                                <tr>
                                    <td>
                                        <h2><%=session.getAttribute("table.caption.flock")%>
                                        </h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="ui-widget" id="msg" style="display: none;">
                                            <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                                <p><span class="ui-icon ui-icon-alert"
                                                         style="float: left; margin-right: .3em;"></span>
                                                    <strong>Error
                                                        :</strong> <%=session.getAttribute("label.message.feild.empty")%>
                                                </p>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table class="table-list" border=1 width="100%">
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
                                                    <select style="width:135px" id="house_Filter" name="house_Filter">
                                                        <%for (Controller c : controllers) { %>
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
                                                    <img src="resources/images/calendar.png" border="0"
                                                         onclick="GetDate('start');"/></td>
                                                <td align="center"><input type="text" value="" size="10" readonly>
                                                    <img src="resources/images/calendar.png" border="0"
                                                         style="filter: alpha(opacity=30);opacity: .30;background-color:#111"/>
                                                </td>
                                                <td align="center"><a href="javascript:validate();">
                                                    <img src="resources/images/plus1.gif" hspace="5"
                                                         style="cursor: pointer"
                                                         border="0" space="5"><br/></img>
                                                    <%=session.getAttribute("button.add")%>
                                                </a>
                                                </td>
                                                <td align="center">&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                            </tr>
                                            <% for (Controller controller : controllers) {%>
                                            <% Collection<Flock> flocks = controller.getFlocks(); %>
                                            <% if (flocks != null && flocks.size() > 0) {%>
                                            <% for (Flock flock : flocks) {%>
                                            <% long fid = flock.getFlockId(); %>
                                            <input id="controllerId" type="hidden"
                                                   value="<%=flock.getControllerId() %>"/>
                                            <input id="programId" type="hidden"
                                                   value="<%=controller.getProgramId() %>"/>
                                            <tr>
                                                <td align="center"><%=flock.getFlockName()%>
                                                </td>
                                                <td align="center"><%=controller.getTitle()%>
                                                </td>
                                                <td align="center"><%=flock.getStatus()%>
                                                </td>
                                                <td align="center"><input type="text" value="<%=flock.getStartTime()%>"
                                                                          size="10" readonly>
                                                    <img src="resources/images/calendar.png" border="0"
                                                         style="filter: alpha(opacity=30);opacity: .30;background-color:#111"/>
                                                </td>
                                                <td align="center">
                                                    <% if (!flock.getStatus().equals("Close")) {%>
                                                    <input type="text" value="" readonly size="10"
                                                           name="end<%=fid%>Date" id="end<%=fid%>Date">
                                                    <img src="resources/images/calendar.png" border="0"
                                                         onclick="GetDate('end<%=fid%>');"/>
                                                    <%} else {%>
                                                    <input type="text" value="<%=flock.getEndTime()%>" size="10"
                                                           readonly>
                                                    <img src="resources/images/calendar.png" border="0"
                                                         style="filter: alpha(opacity=30);opacity: .30;background-color:#111"/>
                                                    <%}%>
                                                </td>
                                                <td align="center">
                                                    <% if (!flock.getStatus().equals("Close")) {%>
                                                    <a href="javascript:closeFlock(<%=flock.getFlockId()%>);">
                                                        <img src="resources/images/lock.gif" hspace="5"
                                                             style="cursor: pointer"
                                                             border="0" space="5"><br/></img>
                                                        <%=session.getAttribute("button.close")%>
                                                    </a>
                                                    <%}%>
                                                </td>
                                                <td align="center">
                                                    <a href="./rmctrl-flock-graphs.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=<%=controller.getId()%>&flockId=<%=flock.getFlockId() %>&programId=<%=controller.getProgramId()%>">
                                                        <img src="resources/images/graph2.gif" hspace="5"
                                                             style="cursor: pointer"
                                                             border="0"><br/></img>
                                                        <%=session.getAttribute("button.graphs")%>
                                                    </a>
                                                </td>
                                                <td align="center">
                                                    <a href="./flock-manager.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=<%=controller.getId()%>&flockId=<%=flock.getFlockId() %>">
                                                        <img src="resources/images/summary.gif" hspace="5"
                                                             style="cursor: pointer" border="0"><br/></img>
                                                        <%=session.getAttribute("button.manage")%>
                                                    </a>
                                                </td>
                                                <td align="center">
                                                    <a href="javascript:removeFlock(<%=flock.getFlockId()%>);">
                                                        <img src="resources/images/close.png" hspace="5"
                                                             style="cursor: pointer"
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
                                    <td>
                                        <button id="btnBack" name="btnBack"
                                                onclick='return back("./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1")'>
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
