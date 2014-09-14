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
        filenameth = GenerateGraph.generateChartFlockMinMaxTemperatureHumidity(flockId, fromDay.toString(), toDay.toString(),
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
<fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">

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
                    <table border="0" width="85%" cellpadding="0" cellspacing="0" style="padding:1px;">
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
                </td>
            </tr>
        </table>
    </div>
</fieldset>
</body>
</html>