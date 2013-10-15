<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Integer fromDay = -1;
    Integer toDay = -1;
    try {
        fromDay = Integer.parseInt(request.getParameter("fromDay"));
        if (fromDay == null) {
            fromDay = -1;
        }
        toDay = Integer.parseInt(request.getParameter("toDay"));
        if (toDay == null) {
            toDay = -1;
        }
    } catch (Exception ex) {
    }
    Integer growDay = 1;
    try {
        growDay = Integer.parseInt(request.getParameter("growDay"));
        if (growDay == null) {
            growDay = 1;
        }
    } catch (Exception ex) {
        growDay = 1;
    }

    String flockName = (String) request.getAttribute("flockName");
    String houseName = (String) request.getAttribute("houseName");
%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <style type="text/css">
        span.growday {
            font-weight: bold;
            font-size: small;
            color: #000000;
        }
    </style>

    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        //<!--
        menu_status = new Array();
        menu_img = new Array();
        function plusMinus(theid) {
            var img_id = document.getElementById('img' + theid);
            if (menu_status[theid] != 'show') {
                img_id.className = 'plus';
            } else {
                img_id.className = 'minus';
            }
        }
        function showHide(theid) {
            if (document.getElementById) {
                var switch_id = document.getElementById(theid);
                if (menu_status[theid] != 'show') {
                    switch_id.className = 'show';
                    menu_status[theid] = 'show';
                    plusMinus(theid);
                    set_cookie(theid, 'hide');
                } else {
                    switch_id.className = 'hide';
                    menu_status[theid] = 'hide';
                    plusMinus(theid);
                    set_cookie(theid, 'show');
                }
            }
        }
        function showHideAll() {
            var mymenu;
            for (var i = 1; i < 3; i++) {
                var mymenu = 'mymenu' + i;
                var menuState = get_cookie(mymenu);
                menu_status[mymenu] = menuState;
                showHide(mymenu);
            }
        }
        function showHideMenuAll() {
            var mymenu;
            for (var i = 1; i < 3; i++) {
                var mymenu = 'mymenus' + i;
                var menuState = get_cookie(mymenu);
                menu_status[mymenu] = menuState;
                showHide(mymenu);
            }
        }
        function get_cookie(cookie_name) {
            var results = document.cookie.match(cookie_name + '=(.*?)(;|$)');
            if (results)
                return ( unescape(results[1]) );
            else
                return null;
        }
        function set_cookie(name, value, exp_y, exp_m, exp_d, path, domain, secure) {
            var cookie_string = name + "=" + escape(value);

            if (exp_y) {
                var expires = new Date(exp_y, exp_m, exp_d);
                cookie_string += "; expires=" + expires.toGMTString();
            }

            if (path)
                cookie_string += "; path=" + escape(path);

            if (domain)
                cookie_string += "; domain=" + escape(domain);

            if (secure)
                cookie_string += "; secure";

            document.cookie = cookie_string;
        }
        //-->
    </script>

    <style type="text/css">
        .menu1 {
            font-family: Helvetica, sans-serif;
            display: block;
            overflow: auto;
            text-decoration: none;
            color: #A62A2A;
            font-weight: bold;
            font-size: 13px;
            width: 98%;
        }

        .menus1 {
            font-family: Helvetica, sans-serif;
            display: block;
            text-decoration: none;
            color: #A62A2A;
            font-weight: bold;
            font-size: 13px;
            width: 98%;
        }

        .submenu {
            background-color: #CCCCCC;
            display: block;
            color: #333333;
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-size: 10px;
            border-top: solid 1px #000000;
        }

        .hide {
            display: none;
        }

        .show {
            display: block;
        }

        div.plus {
            background-image: url('resources/images/plus.gif');
            margin: 5px;
            height: 10px;
            width: 10px;
            display: block;
        }

        div.minus {
            background-image: url('resources/images/minus.gif');
            margin: 5px;
            height: 10px;
            width: 10px;
            display: block;
        }
    </style>
</head>
<body onload="javascript:showHideAll(),showHideMenuAll(),init()">
<%@include file="loading.jsp" %>
<div>
    <input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
    <input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>
</div>
<table width="100%">
<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%;">
            <table width="100%">
                <tr>
                    <td>
                        <%@include file="toplang.jsp" %>
                    </td>
                    <td width="65%">
                        <h1 style="text-align: center;"><%=session.getAttribute("history.graph.page.title")%>
                        </h1>

                        <h2><%=flockName%> - <%=houseName%>
                        </h2>
                    </td>
                    <td width="20%">
                        <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1&doResetTimeout=true">
                            <img src="resources/images/display.png" style="cursor: pointer" hspace="5"
                                 border="0"/><%=session.getAttribute("button.screens")%>
                        </a>
                        <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                            <img src="resources/images/chicken-icon.png" style="cursor: pointer" hspace="5"
                                 border="0"/><%=session.getAttribute("main.screen.page.flocks")%>
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
    <table border="0" width="800px" cellpadding="0" cellspacing="0" style="padding:1px;">
        <tr>
            <td>
                <h2><%=session.getAttribute("history.graph.page.growday.graph.title")%>
                </h2>
                                        <span class="growday">
                                            <%=session.getAttribute("label.growday")%> <%=growDay%>
                                        </span>
            </td>
        </tr>
        <tr>
            <td>
                <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                    <form action="./rmctrl-flock-graphs.html">
                        <input type="hidden" name="flockId" value="<%=flockId%>"/>
                        <input type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
                        <input type="hidden" name="userId" value="<%=userId%>"/>
                        <table class="table-list-small">
                            <tr>
                                <td>
                                    <%if (fromDay == -1 || toDay == -1) {%>
                                    <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                     name="fromDay"/>
                                    <%=session.getAttribute("label.to")%> : <input type="text" size="5"
                                                                                   name="toDay"/>
                                    <%} else {%>
                                    <%=session.getAttribute("label.from")%> : <input type="text" size="5" name="fromDay"
                                                                                     value="<%=fromDay%>"/>
                                    <%=session.getAttribute("label.to")%> :
                                    <input type="text" size="5" name="toDay" value="<%=toDay%>"/>
                                    <%}%>
                                    <input type="submit" value="<%=session.getAttribute("button.submit")%>"/>
                                </td>
                                <td>
                                    <a href="./exptoexcelhistory.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>"
                                       onclick="window.location.href.replace('./exptoexcelhistory.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>')">
                                        <img src="resources/images/excel.gif" style="cursor: pointer" hspace="5"
                                             border="0"/><%=session.getAttribute("button.export")%>
                                    </a>
                                    <a title="Table" style="cursor: pointer"
                                       onclick="window.open('./rmctrl-flockhistory-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                        <img src="resources/images/table.gif" style="cursor: pointer" hspace="5"
                                             border="0"/><%=session.getAttribute("button.table")%>
                                    </a>
                                    <a title="Table" style="cursor: pointer"
                                       onclick="window.open('./rmctrl-eggcnt-flockhistory-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                        <img src="resources/images/table.gif" style="cursor: pointer" hspace="5"
                                             border="0"/><%=session.getAttribute("button.eggcount.table")%>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td>
                <table class="table-list">
                    <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">
                        <td>
                            <div id="imgmymenu1" class="plus"></div>
                        </td>
                        <td class="menu1" onclick="showHide('mymenu1')">
                            <img src="resources/images/graph2.gif"
                                 hspace="5"><%=session.getAttribute("history.graph.page.panel.fwt.label")%>
                            </img>
                        </td>
                    </tr>
                </table>

                <table style="border:solid 1px #D5EFFF;" id="mymenu1" class="hide">
                    <tr>
                        <td>
                            <img border="0"
                                 src="./feedwatergraph.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>"/>
                        </td>
                    </tr>
                </table>

                <table class="table-list">
                    <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">
                        <td>
                            <div id="imgmymenu2" class="plus"></div>
                        </td>
                        <td class="menu1" onclick="showHide('mymenu2')">
                            <img src="resources/images/graph2.gif" hspace="5"/>
                            <%=session.getAttribute("history.graph.page.panel.aw.label")%>
                        </td>
                    </tr>
                </table>
                <table style="border:solid 1px #D5EFFF;" id="mymenu2" class="hide">
                    <tr>
                        <td>
                            <img border="0"
                                 src="./avgweightgraph.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>"/>
                        </td>
                    </tr>
                </table>

                <table class="table-list">
                    <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">
                        <td>
                            <div id="imgmymenu3" class="plus"></div>
                        </td>
                        <td class="menu1" onclick="showHide('mymenu3')">
                            <img src="resources/images/graph2.gif" hspace="5"/>
                            <%=session.getAttribute("history.graph.page.panel.max.label")%>
                        </td>
                    </tr>

                </table>
                <table style="border:solid 1px #D5EFFF;" id="mymenu3" class="hide">
                    <tr>
                        <td>
                            <img border="0"
                                 src="./minmaxhumgraph.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>"/>
                        </td>
                    </tr>
                </table>

                <table class="table-list">
                    <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">
                        <td>
                            <div id="imgmymenu4" class="plus"></div>
                        </td>
                        <td class="menu1" onclick="showHide('mymenu4')">
                            <img src="resources/images/graph2.gif" hspace="5"/>
                            <%=session.getAttribute("history.graph.page.panel.mor.label")%>
                        </td>
                    </tr>
                </table>
                <table style="border:solid 1px #D5EFFF;" id="mymenu4" class="hide">
                    <tr>
                        <td>
                            <img border="0"
                                 src="./mortalitygraph.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>"/>
                        </td>
                    </tr>
                </table>

                <table class="table-list">
                    <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                        onmouseout="this.style.background='#D5EFFF'">
                        <td>
                            <div id="imgmymenu5" class="plus"></div>
                        </td>
                        <td class="menu1" onclick="showHide('mymenu5')">
                            <img src="resources/images/graph2.gif" hspace="5"/>
                            <%=session.getAttribute("history.graph.page.panel.hon.label")%>
                        </td>
                    </tr>
                </table>
                <table style="border:solid 1px #D5EFFF;" id="mymenu5" class="hide">
                    <tr>
                        <td>
                            <img border="0"
                                 src="./heatontimegraph.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table border="0" width="800px" cellpadding="0" cellspacing="0" style="padding:1px;">
    <tr>
        <td>
            <h2><%=session.getAttribute("history.graph.page.24hour.graph.title")%>
            </h2>
                                    <span class="growday">
                                        <%=session.getAttribute("label.growday")%> <%=growDay%>
                                    </span>
        </td>
    </tr>
    <tr>
        <td>
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <form action="./rmctrl-flock-graphs.html">
                    <input type="hidden" name="flockId" value="<%=flockId%>"/>
                    <input type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
                    <input type="hidden" name="userId" value="<%=userId%>"/>
                    <table class="table-list-small">
                        <tr>
                            <td>
                                <%=session.getAttribute("label.growday")%> :
                                <input type="text" size="5" name="growDay">
                                <input type="submit" value="<%=session.getAttribute("button.submit")%>"/>
                            </td>
                            <td>
                            </td>
                            <td>
                                <img src="resources/images/excel.gif" style="cursor: pointer" hspace="5" border="0"/>
                                <a href="./exptoexcelhistory24.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>">
                                    <%=session.getAttribute("button.export")%>
                                </a>
                                <img src="resources/images/table.gif" style="cursor: pointer" hspace="5" border="0"/>
                                <a onclick="window.open('./rmctrl-flockhistory24-table.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                    <%=session.getAttribute("button.table")%>
                                </a>
                            </td>
                        </tr>
                    </table>
                </form>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td align="center">
            <table class="table-list">
                <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                    onmouseout="this.style.background='#D5EFFF'">
                    <td>
                        <div id="imgmymenus1" class="plus"></div>
                    </td>
                    <td class="menus1" onclick="showHide('mymenus1')">
                        <img src="resources/images/graph2.gif" hspace="5"/>
                        <%=session.getAttribute("history.graph.page.panel.ioh24.label")%>
                    </td>
                </tr>
            </table>
            <table style="border:solid 1px #D5EFFF;" id="mymenus1" class="hide">
                <tr>
                    <td>
                        <img border="0"
                             src="./Graph24HourIOHServlet?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>"/>
                    </td>
                </tr>
            </table>

            <table class="table-list">
                <tr style="background-color:#D5EFFF;" onmouseover="this.style.background='#ADD8E6'"
                    onmouseout="this.style.background='#D5EFFF'">
                    <td>
                        <div id="imgmymenus2" class="plus"></div>
                    </td>
                    <td class="menus1" onclick="showHide('mymenus2')">
                        <img src="resources/images/graph2.gif" hspace="5"/>
                        <%=session.getAttribute("history.graph.page.panel.fwt24.label")%>
                    </td>
                </tr>
            </table>
            <table style="border:solid 1px #D5EFFF;" id="mymenus2" class="hide">
                <tr>
                    <td>
                        <img border="0"
                             src="./Graph24HourFWServlet?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</fieldset>
</td>
</tr>
</table>
</body>
</html>