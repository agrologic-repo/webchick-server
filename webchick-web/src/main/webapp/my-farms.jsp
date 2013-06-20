<%--
    Document   : myfamrs
    Created on : Oct 16, 2011, 11:27:09 AM
    Author     : Administrator
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.CellinkDto" %>
<%@ page import="com.agrologic.app.web.CellinkState" %>
<%@ page import="java.util.List" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    request.getSession().setAttribute("role", user.getRole());

    int state = -1;
    try {
        state = Integer.parseInt(request.getParameter("state"));
    } catch (Exception e) {
        state = -1;
    }
    List<CellinkDto> cellinks = (List<CellinkDto>) request.getSession().getAttribute("cellinks");
    int from = (Integer) request.getSession().getAttribute("from");
    int to = (Integer) request.getSession().getAttribute("to");
    int of = (Integer) request.getSession().getAttribute("of");
    int tableline = 25;
%>

<%! int countCellinksByState(List<CellinkDto> cellinks, int state) {
    int count = 0;
    for (CellinkDto cellink : cellinks) {
        if (cellink.getState() == state) {
            count++;
        }
    }
    return count;
}
%>
<%! List<CellinkDto> getCellinksByState(List<CellinkDto> cellinks, int state) {
    if (state == -1) {
        return cellinks;
    }
    List<CellinkDto> cellinkList = new ArrayList<CellinkDto>();
    for (CellinkDto cellink : cellinks) {
        if (cellink.getState() == state) {
            cellinkList.add(cellink);
        }
    }
    return cellinkList;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=request.getSession().getAttribute("dir") %>">
<head>
    <title><%=session.getAttribute("myfarms.page.title") %>
    </title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
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
        function disconnectCellink(cellinkId) {
            if (!confirm("Do you sure ?")) {
                return;
            }
            window.location.replace("./disconnect-cellink.html?userId=<%=user.getId()%>&cellinkId=" + cellinkId);
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
            window.location.replace("./disconnect-cellinks.html?userId=<%=user.getId()%>" + result);
        }
        function removeCellink(userId, cellinkId) {
            if (confirm("Are you sure ?") == true) {
                window.document.location.replace("./removecellink.html?userId=" + userId + "&cellinkId=" + cellinkId);
            }
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
        <tr>
            <td colspan="2" width="100%">
                <h1><%=session.getAttribute("myfarms.page.header") %>
                </h1>

                <h2><%=session.getAttribute("myfarms.page.sub.header")%>
                </h2>
            </td>
        </tr>
        <%for (CellinkDto cellink : cellinks) {%>
        <tr>
            <td width="100%">
                <form id="formFarms" name="formFarms" style="display:inline">
                    <table id=""
                           style="border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;"
                           width="100%">
                        <tr>
                            <td colspan="2">
                                <%if (cellink.isOnline()) {%>
                                <table id="farms-online" border=0 cellpadding="10" cellspacing="10">
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <img class="link" src="img/chicken_house1.jpg"
                                                 onclick="javascript:window.location.href='./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>' "/>
                                        </td>
                                        <td align="center">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h1><%=cellink.getName()%>
                                                        </h1>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <a class="button"
                                                           href="./rmctrl-main-screen-ajax.jsp?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>&role=<%=user.getRole() %>"><%=session.getAttribute("button.show.houses") %>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table class="table-list-small" border="0">
                                                <tr>
                                                    <td>
                                                        <hr></hr>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><%=session.getAttribute("label.cellink.version") %> <%=cellink.getVersion()%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="left"><%=session.getAttribute("label.cellink.status.online") %>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table class="table-list">
                                                            <tr>
                                                                <%if (cellink.getCellinkState().getValue() == CellinkState.STATE_RUNNING) {%>
                                                                <td>
                                                                    <img src="img/disconnect.png"
                                                                         style="cursor: pointer" border="0" hspace="5"/>
                                                                    <a href="#"
                                                                       onclick="disconnectCellink(<%=cellink.getId() %>)">
                                                                        <%=session.getAttribute("button.disconnect.cellink")%>
                                                                    </a>
                                                                </td>
                                                                <%}%>
                                                                <td>
                                                                    <img src="img/setting_16.png"
                                                                         style="cursor: pointer" border="0" hspace="5"/>
                                                                    <a href="./cellink-setting.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>">
                                                                        <%=session.getAttribute("button.setting")%>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <img src="img/refresh.gif" style="cursor: pointer"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./my-farms.html?userId=<%=cellink.getUserId()%>">
                                                                        <%=session.getAttribute("button.refresh")%>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <%} else {%>
                                <table id="farms-offline" border=0 cellpadding="10" cellspacing="10">
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <img class="link" src="img/chicken_house1.jpg"/>
                                        </td>
                                        <td align="center">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h1><%=cellink.getName()%>
                                                        </h1>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table class="table-list-small" border="0">
                                                <tr>
                                                    <td>
                                                        <hr></hr>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><%=session.getAttribute("label.cellink.version") %> <%=cellink.getVersion()%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><%=session.getAttribute("label.cellink.offline.since") %>
                                                        <%=cellink.getFormatedTime() %>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table class="table-list">
                                                            <tr>
                                                                <td>
                                                                    <img src="img/setting_16.png"
                                                                         style="cursor: pointer"
                                                                         title="<%=session.getAttribute("button.connect.cellink")%>"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./cellink-setting.html?userId=<%=cellink.getUserId()%>&cellinkId=<%=cellink.getId()%>"><%=session.getAttribute("button.setting")%>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <img src="img/refresh.gif" style="cursor: pointer"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./my-farms.html?userId=<%=cellink.getUserId()%>"><%=session.getAttribute("button.refresh")%>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>

                                <%}%>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <hr>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        <%}%>

    </table>
</div>

</body>
</html>