<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="java.io.PrintWriter" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
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
    String filenameth = (String) session.getAttribute("filenameth-flockid=" + flockId + "&growday=" + growDay);
    if (filenameth == null) {
        filenameth = GenerateGraph.generateChartFlockWaterFeed(flockId, fromDay.toString(), toDay.toString(),
                session, new PrintWriter(out), currLocal);
    }

    String graphURLTH;
    if (filenameth.contains("public_error")) {
        graphURLTH = request.getContextPath() + "/resources/images/public_nodata_500x300.png";
    } else {
        graphURLTH = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenameth;
    }
%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <style media="screen" type="text/css">
        .layer1_class {
            position: absolute;
            z-index: 1;
            top: 100px;
            left: 0px;
            visibility: visible;
        }

        .layer2_class {
            position: absolute;
            z-index: 2;
            top: 10px;
            left: 10px;
            visibility: hidden
        }
    </style>
    <script>
        function downLoad() {
            if (document.all) {
                document.all["layer1"].style.visibility = "hidden";
                document.all["layer2"].style.visibility = "visible";
            } else if (document.getElementById) {
                node = document.getElementById("layer1").style.visibility = 'hidden';
                node = document.getElementById("layer2").style.visibility = 'visible';
            }
        }
    </script>
</head>
</head>
<body onload="downLoad()">
<div id="layer1" class="layer1_class">
    <table width="100%">
        <tr>
            <td align="center"><p><strong><em>Please wait while this page is loading...</em></strong></p></td>
        </tr>
    </table>
</div>
<div id="layer2" class="layer2_class">
    <table width="100%">
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                    <table border="0" width="85%" cellpadding="0" cellspacing="0" style="padding:1px;">
                        <tr>
                            <td>

                                <form id="flock-graph" name="flock-graph"
                                      action="./rmctrl-flock-feed-water.jsp?currLocal=<%=currLocal%>">
                                    <input type="hidden" name="flockId" value="<%=flockId%>"/>

                                    <%if (fromDay == -1 || toDay == -1) {%>
                                    <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                     name="fromDay"/>
                                    <%=session.getAttribute("label.to")%> : <input type="text" size="5"
                                                                                   name="toDay"/>
                                    <%} else {%>
                                    <%=session.getAttribute("label.from")%> : <input type="text" size="5"
                                                                                     name="fromDay"
                                                                                     value="<%=fromDay%>"/>
                                    <%=session.getAttribute("label.to")%> :
                                    <input type="text" size="5" name="toDay" value="<%=toDay%>"/>
                                    <%}%>
                                    <input type="submit" value="<%=session.getAttribute("button.go")%>"/>
                                    <input type="button" id="btnClear" name="btnClear"
                                           value="<%=session.getAttribute("button.clear")%>"/>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <a href="./exptoexcelhistory.htmlflockId=<%=flockId%>"
                                   onclick="window.location.href.replace('./exptoexcelhistory.html?flockId=<%=flockId%>')">
                                    <img src="resources/images/excel.gif" style="cursor: pointer"
                                         hspace="5"
                                         border="0"/><%=session.getAttribute("button.export")%>
                                </a>
                                <a title="Table" style="cursor: pointer"
                                   onclick="window.open('./rmctrl-flockhistory-table.jsp?flockId=<%=flockId%>', 'mywindow','width=800,height=600,toolbar=no,location=yes,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=yes, resizable=yes')">
                                    <img src="resources/images/table.gif" style="cursor: pointer"
                                         hspace="5"
                                         border="0"/><%=session.getAttribute("button.table")%>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2><%=session.getAttribute("history.graph.page.growday.graph.title")%>
                                </h2>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div>
                                    <img src="<%=graphURLTH%>" width=800 height=600 border=0 usemap="#<%=filenameth%>">
                                </div>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </td>
        </tr>
    </table>
</div>
</body>
</html>