<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="java.io.PrintWriter" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%
    String graphType = request.getParameter("graphType");
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Locale currLocale = (Locale) session.getAttribute("currLocale");
    String cellinkId = request.getParameter("cellinkId");

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

    String fileName = null;
    Long dataId = null;
    Integer format = null;
    String dNum = null;
    String label = null;

    if(graphType.equals("daily")) {
        dataId = Long.parseLong(request.getParameter("dataId"));
        fileName = GenerateGraph.generateGraph(flockId, dataId, fromDay.toString(), toDay.toString(), session, new PrintWriter(out), currLocale);
    } else {
        dNum = request.getParameter("dnum");
        label = request.getParameter("label");
        //values
        format = Integer.parseInt(request.getParameter("format"));
        fileName = GenerateGraph.generate_hour_graph(flockId, dNum, label, format, growDay, session, new PrintWriter(out), currLocale);
    }

    String graphURLM;
    if (fileName.contains("public_error")) {
        graphURLM = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else if (fileName.contains("public_loading")) {
        graphURLM = request.getContextPath() + "/resources/images/public_loadingdata_500x300.png";
    } else {
        graphURLM = request.getContextPath() + "/servlet/DisplayChart?filename=" + fileName;
    }

%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">

<head>

    <title>
        <%=session.getAttribute("history.graph.page.title")%>
    </title>

    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery.tablescroll.css"/>

    <style media="screen" type="text/css">
        .loadingClass {
            position: absolute;
            z-index: 1;
            top: 100px;
            left: 0px;
            visibility: visible;
        }

        .contentClass {
            position: absolute;
            z-index: 2;
            top: 0px;
            left: 0px;
            visibility: hidden
        }
    </style>



</head>

<body onload="downLoad()">

<div id="loadingDiv" class="loadingClass" style="width: 100%">
    <table width="100%">
        <tr>
            <td align="center">
                <p>
                    <strong>
                        <em>
                            <%=session.getAttribute("graph.please.wait.while.page.loading")%>
                        </em>
                    </strong>
                </p>
            </td>
        </tr>
    </table>
</div>

<div id="contentDiv" class="contentClass" style="width: 100%">
    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
        <table width="100%">
            <tr>
                <td>
                    <% if (graphType.equals("daily")) {%>

                    <table>
                        <tr>
                            <td>
                                <form id="flock-graph" name="flock-graph" class="flock-graph" action="./rmctrl-flock-graph.jsp" style="display: inline-block">
                                    <input type="hidden" name="flockId" value="<%=flockId%>"/>
                                    <input type="hidden" name="dataId" value="<%=dataId%>"/>
                                    <input id="graphType" type="hidden" name="graphType" value="<%=graphType%>"/>
                                    <%=session.getAttribute("label.show.range")%>&nbsp;
                                    <%if (fromDay == -1 || toDay == -1) {%>
                                         <label> <%=session.getAttribute("label.from")%> : <input type="text" size="5" name="fromDay" style="display: inline;"/> </label>
                                         <label> <%=session.getAttribute("label.to")%> : <input type="text" size="5" name="toDay" style="display: inline;"/> </label>
                                    <%} else {%>
                                         <%=session.getAttribute("label.from")%> : <input type="text" size="5" name="fromDay" value="<%=fromDay%>" style="display: inline;"/>
                                         <%=session.getAttribute("label.to")%> : <input type="text" size="5" name="toDay" value="<%=toDay%>" style="display: inline;"/>
                                    <%}%>
                                    <button id="apply" name="apply" style="min-width: 80px">
                                        <%=session.getAttribute("button.apply")%>
                                    </button>
                                </form>
                            </td>
                            <td valign="top">
                                <button id="showTable" onclick="window.open('./rmctrl-flockhistory-table.jsp?flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                    <%=session.getAttribute("button.table")%>
                                    <img src="resources/images/table.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>
                                </button>
                                <button id="exportToExcel">
                                    <%=session.getAttribute("button.export")%><img id="exportToExcel"name="exportToExcel" src="resources/images/excel.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>
                                </button>
                                <div id="loading" style="display:none"></div>
                            </td>
                        </tr>
                    </table>

                    <% } else { %><!--else 24 hour-->

                    <table>
                        <tr>
                            <td>
                                <form id="form_hour" class="flock-graph" target="leftPage" action="historygraphaction.html" style="display: inline-block">
                                    <input type="hidden" name="flockId" value="<%=flockId%>"/>
                                    <input type="hidden" name="dnum" value="<%=dNum%>"/>
                                    <input type="hidden" name="format" value="<%=format%>"/>
                                    <input type="hidden" name="label" value="<%=label%>"/>
                                    <input type="hidden" name="graphType" value="<%=graphType%>"/>
                                    <input type="hidden" name="cellinkId" value="<%=cellinkId%>"/>

                                    <label> <%=session.getAttribute("label.grow.day")%> : </label>
                                    <input type="text" size="5" name="growDay" value="<%=growDay%>" text="<%=growDay%>"/>

                                    <button name="btn" value="apply" style="min-width: 80px">
                                        <%=session.getAttribute("button.apply")%>
                                    </button>
                                    <%--<button name="btn" value="show_table">--%>
                                        <%--<%=session.getAttribute("button.table")%><img src="resources/images/table.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>--%>
                                    <%--</button>--%>

                                    <button id="btn" onclick="window.open('./rmctrl-flockhistory24-table.jsp?flockId=<%=flockId%>&growDay=<%=growDay%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                        <%=session.getAttribute("button.table")%><img src="resources/images/table.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>
                                    </button>

                                    <button name="btn" value="export_to_excel">
                                        <%=session.getAttribute("button.export")%><img id="exportToExcel" name="exportToExcel" src="resources/images/excel.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>
                                    </button>
                                </form>
                                <%--<form id="form_hour" class="flock-graph" style="display: inline-block">--%>
                                    <%----%>
                                    <%--<input type="hidden" name="flock_id" value="<%=flock_id%>"/>--%>
                                    <%--<input type="hidden" name="dnum" value="<%=d_num%>"/>--%>
                                    <%--<input type="hidden" name="format" value="<%=format%>"/>--%>
                                    <%--<input type="hidden" name="label" value="<%=label%>"/>--%>
                                    <%--<input type="hidden" name="graph_type" value="<%=graph_type%>"/>--%>
                                    <%--<input type="hidden" name="cellink_id" value="<%=cellink_id%>"/>--%>

                                    <%--<label> <%=session.getAttribute("label.grow.day")%> : </label>--%>
                                    <%--<input type="text" size="5" name="growDay" value="<%=grow_day%>" text="<%=grow_day%>"/>--%>

                                    <%--<button id="btnGo" name="btnGo" style="min-width: 80px">--%>
                                        <%--<%=session.getAttribute("button.apply")%>--%>
                                    <%--</button>--%>
                                    <%--<button id="showTable24" onclick="window.open('./rmctrl-flockhistory24-table.jsp?flock_id=<%=flock_id%>&growDay=<%=grow_day%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">--%>
                                        <%--<%=session.getAttribute("button.table")%><img src="resources/images/table.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>--%>
                                    <%--</button>--%>
                                    <%--<button id="exportToExcel24">--%>
                                        <%--<%=session.getAttribute("button.export")%><img id="exportToExcel" name="exportToExcel" src="resources/images/excel.gif" style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"/>--%>
                                    <%--</button>--%>
                                <%--</form>--%>
                            </td>
                            <td valign="top">

                            </td>
                        </tr>
                    </table>

                    <% } %>

                </td>
            </tr>
            <tr>
                <td width="100%">
                    <div id="graph" class="ui-accordion ui-corner-all">
                        <table border="0" cellpadding="0" cellspacing="0" style="padding:1px;">
                            <tr>
                                <td colspan="2" width="80%">
                                    <img src="<%=graphURLM%>" usemap="#<%=fileName%>">
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </fieldset>
</div>

<script type="text/javascript" src="resources/javascript/jquery.js">;</script>
<script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
<script type="text/javascript" src="resources/javascript/jquery.tablescroll.js">;</script>
<script type="text/javascript" src="resources/javascript/jquery.tablesorter.js">;</script>

<script>
    <%--$("#btnClear").click(function () {--%>
        <%--window.location.href = "./rmctrl-flock-graph.jsp?currLocal=<%=curr_locale%>&flockId=<%=flock_id%>";--%>
    <%--});--%>

//    $("#btnGo").click(function () {
//        window.location.target = "leftPage";
//        window.location.href = "./rmctrl-flock-graph-daily-hour.jsp?";
//    });

    $("#exportToExcel").click(function (e) {
        $("#loading").show()
        window.location.href = 'exptoexcelhistory.html?flockId=<%=flockId%>';
        $("#loading").fadeOut(2000);

    });

    <%--$("#exportToExcel24").click(function (e) {--%>
        <%--$("#loading24").show()--%>
        <%--window.location.href = 'exptoexcelhistory24.html?flockId=<%=flock_id%>&growDay=<%=grow_day%>';--%>
        <%--$("#loading24").fadeOut(2000);--%>

    <%--});--%>

    jQuery(document).ready(function ($) {
        $('#thetable').tableScroll({height: 465});
        $('#thetable tr').hover(function () {
            $(this).addClass('hover');
        }, function () {
            $(this).removeClass('hover');
        });
    });

    function downLoad() {
        if (document.all) {
            document.all["loadingDiv"].style.visibility = "hidden";
            document.all["contentDiv"].style.visibility = "visible";
        } else if (document.getElementById) {
            node = document.getElementById("loadingDiv").style.visibility = 'hidden';
            node = document.getElementById("contentDiv").style.visibility = 'visible';
        }
    }

//    function submit_button(form){
//
//        document.forms.namedItem(form).action = "./rmctrl-flock-graph.jsp";
//        document.forms.namedItem(form).target = "rightPage";
//        document.forms.namedItem(form).submit();
//
//        document.forms.namedItem(form).action = "./rmctrl-flock-graph-daily-hour.jsp";
//        document.forms.namedItem(form).target = "leftPage";
//        document.forms.namedItem(form).submit();
//
//    }
</script>
</body>
</html>
