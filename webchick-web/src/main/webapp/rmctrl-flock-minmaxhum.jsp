<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="java.io.PrintWriter" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ include file="language.jsp" %>

<%
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Locale currLocal = (Locale) session.getAttribute("currLocale");
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

    //////////////////////////////////////////////////Daily Graphs/////////////////////////////////////////////////////
    String filenamemmh = GenerateGraph.generateChartFlockMinMaxTemperatureHumidity(flockId, fromDay.toString(), toDay.toString(), session, new PrintWriter(out), currLocal);
    String graphURLMMH;
    if (filenamemmh.contains("public_error")) {
        graphURLMMH = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLMMH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamemmh;
    }
%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
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
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablescroll.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablesorter.js">;</script>

</head>
</head>
<body onload="downLoad()">
<div id="loadingDiv" class="loadingClass" style="width: 100%">
    <table width="100%">
        <tr>
            <td align="center"><p><strong><em><%=session.getAttribute("graph.please.wait.while.page.loading")%>
            </em></strong></p></td>
        </tr>
    </table>
</div>
<div id="contentDiv" class="contentClass" style="width: 100%">
<fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
<table width="100%">
<tr>
    <td>
        <table>
            <tr>
                <td>
                    <form id="flock-graph" name="flock-graph" class="flock-graph"
                          action="./rmctrl-flock-minmaxhum.jsp?currLocal=<%=currLocal%>"
                          style="display: inline-block">
                        <input type="hidden" name="flockId" value="<%=flockId%>"/>
                        <%=session.getAttribute("label.show.range")%>&nbsp;
                        <%if (fromDay == -1 || toDay == -1) {%>
                        <label>
                            <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                             name="fromDay"
                                                                             style="display: inline;"/>
                        </label>
                        <label>
                            <%=session.getAttribute("label.to")%> : <input type="text" size="5"
                                                                           name="toDay"
                                                                           style="display: inline;"/>
                        </label>
                        <%} else {%>
                        <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                         name="fromDay"
                                                                         value="<%=fromDay%>"
                                                                         style="display: inline;"/>
                        <%=session.getAttribute("label.to")%> : <input type="text" size="5" name="toDay"
                                                                       value="<%=toDay%>"
                                                                       style="display: inline;"/>
                        <%}%>
                        <button id="btnGo" name="btnGo" style="min-width: 80px">
                            <%=session.getAttribute("button.apply")%>
                        </button>
                    </form>
                    <button id="btnClear" name="btnClear" style="min-width: 80px">
                        <%=session.getAttribute("button.clear")%>
                    </button>
                </td>
                <td valign="top">
                    <button id="showTable"
                            onclick="window.open('./rmctrl-flockhistory-table.jsp?flockId=<%=flockId%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')"><%=session.getAttribute("button.table")%><img
                            src="resources/images/table.gif"
                            style="cursor: pointer; vertical-align: bottom" hspace="5" border="0"
                            onclick="window.open('./rmctrl-flockhistory-table.jsp?flockId=<%=flockId%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')"/>
                    </button>
                    <button id="exportExcel"><%=session.getAttribute("button.export")%><img id="exportToExcel"
                                                                                            name="exportToExcel"
                                                                                            src="resources/images/excel.gif"
                                                                                            style="cursor: pointer; vertical-align: bottom"
                                                                                            hspace="5" border="0"/>

                    </button>
                    <div id="loading" style="display:none"></div>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td width="100%">
        <div id="graph" class="ui-accordion ui-corner-all">
            <table border="0" cellpadding="0" cellspacing="0" style="padding:1px;">
                <tr>
                    <td colspan="2" width="80%">
                        <img src="<%=graphURLMMH%>" usemap="#<%=filenamemmh%>">
                    </td>
                    <%--<td valign="top" width="20%">--%>
                    <%--<table id="thetable" class="tablescroll">--%>
                    <%--<thead>--%>
                    <%--<tr>--%>
                    <%--<td>flock id</td>--%>
                    <%--<td>grow day</td>--%>
                    <%--<td>feed consumtion</td>--%>
                    <%--<td>water consumtion</td>--%>
                    <%--</tr>--%>
                    <%--</thead>--%>
                    <%--<tbody>--%>
                    <%--<tr class="first">--%>
                    <%--<td>00501</td>--%>
                    <%--<td>1</td>--%>
                    <%--<td>73</td>--%>
                    <%--<td>73</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td>00501</td>--%>
                    <%--<td>2</td>--%>
                    <%--<td>173</td>--%>
                    <%--<td>173</td>--%>
                    <%--</tr>--%>
                    <%--</tbody>--%>
                    <%--</table>--%>
                    <%--</td>--%>
                </tr>
            </table>
        </div>
    </td>
</tr>
</table>

</fieldset>
</div>
<script>
    $("#btnClear").click(function () {
        window.location.href = "./rmctrl-flock-minmaxhum.jsp?currLocal=<%=currLocal%>&flockId=<%=flockId%>";
    });

    $("#exportToExcel").click(function (e) {
        $("#loading").show()
        window.location.href = 'exptoexcelhistory.html?flockId=<%=flockId%>';
        $("#loading").fadeOut(2000);
    });

    $("#exportExcel").click(function (e) {
        $("#loading").show()
        window.location.href = 'exptoexcelhistory.html?flockId=<%=flockId%>';
        $("#loading").fadeOut(2000);

    });

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


    <%--$(document).ready(function() {--%>
    <%--if($('#<%=filenameth%>')) {--%>
    <%--$('#<%=filenameth%> area').each(function() {--%>
    <%--var id = $(this).attr('id');--%>
    <%--$(this).mouseover(function() {--%>
    <%--$('#overlay'+id).show();--%>

    <%--});--%>

    <%--$(this).mouseout(function() {--%>
    <%--var id = $(this).attr('id');--%>
    <%--$('#overlay'+id).hide();--%>
    <%--});--%>

    <%--});--%>
    <%--}--%>
    <%--});--%>
</script>
</body>
</html>