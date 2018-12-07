<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ include file="language.jsp" %>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    Long programId = (Long) session.getAttribute("progID");
    String pageDir = "rightPage";
    if (session.getAttribute("dir").equals("rtl")) {
        pageDir = "leftPage";
    }
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">

<head>
    <title><%=session.getAttribute("history.graph.page.title")%>
    </title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <style type="text/css">
        label {
            display: block;
            margin: 20px 0 0 0;
        }

        select {
            width: 100%;
        }
        table {
            font: normal 15px tahoma;
            margin: 0px;
        }

        .link-menu {
            margin: 0px;
            color: #000000;
            float: left;
            text-decoration: none;
            cursor: pointer display : block;
            width: 100%;
            height: 100%;
        }

        .row-menu {
            margin: 0px;
            background-color: #aaaaaa;
            border-bottom: 1px solid #ffffff;
            text-decoration: none;
            text-align: left;
            display: block;
            cursor: default;
            line-height: 30px;

        }

        .row-menu:hover {
            background-color: #e6e6e6;
        }

        .selected {
            background-color: #e6e6e6;
        }
    </style>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.simplesidebar.js">;</script>
</head>

<body>
<input id="cellinkId" type="hidden" name="cellinkId" value="<%=cellinkId%>"/>
<input id="flockId" type="hidden" name="flockId" value="<%=flockId%>"/>
<div class="">
    <label for="combobox" style="color: black"><h3><%=session.getAttribute("history.graph.page.select.type")%></h3></label>
    <select name="combobox" id="combobox">
        <option></option>
        <option value="div1"><%=session.getAttribute("history.graph.page.by.grow.day")%>
        </option>
        <option value="div2"><%=session.getAttribute("history.graph.page.by.hour")%>
        </option>
    </select>
</div>
<hr>
<div id="div1" class="drop-down-show-hide" style="width: 100%;">
    <table id="daily-history-menu" border="0" cellpadding="0" cellspacing="0" border="1"style="border: 0px solid #0084e1;" width="100%">
        <!------------------------------------------------SPECIALS GRAPHS----------------------------------------------------->
        <%if (programId == 131301 || programId == 96601 || programId == 106401 || programId == 116801 || programId == 93401 || programId == 95901
           || programId == 122901 || programId == 63301 || programId == 130601 || programId == 130401 || programId == 81701 || programId == 12707
           || programId == 128901 || programId == 128901 || programId == 128101 || programId == 65103 || programId == 61124 || programId == 13190
           || programId == 130902 || programId == 126102){%>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed-water-special.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" id="feed-water">
                <h3><%=session.getAttribute("history.graph.page.menu.feed.water.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
        <td class=lefttd>
                <a href="./rmctrl-flock-average-weight.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.average.weight.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td class=lefttd>
                <a href="./rmctrl-flock-minmaxhum.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.temperature.humidity.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
        <td>
            <a href="./rmctrl-flock-heaton-time.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                <h3><%=session.getAttribute("history.graph.page.menu.heaters.on.time.label")%></h3></a>
        </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
        <td>
            <a href="./rmctrl-flock-mortality.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"
               target="<%=pageDir%>" class="link-menu" style="width: 100%">
                <h3><%=session.getAttribute("history.graph.page.menu.mortality.label")%></h3></a>
        </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-co2.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.co2.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
        <td>
            <a href="./rmctrl-flock-feed-water-perbird.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                <h3><%=session.getAttribute("history.graph.page.menu.feed.water.per.bird.label")%></h3></a>
        </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-required-temperature.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.required.temp.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-daily-silo-fill.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.daily.silo.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-standard-deviation.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.standard.deviation.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-cv.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"
                   target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.cv.label")%></h3></a>
            </td>
        </tr>
        <!------------------------------------------------NORMAL GRAPHS----------------------------------------------------->
        <%} else {%>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed-water.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.feed.water.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td class=lefttd>
                <a href="./rmctrl-flock-average-weight.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.average.weight.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td class=lefttd>
                <a href="./rmctrl-flock-minmaxhum.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.temperature.humidity.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-heaton-time.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.heaters.on.time.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-mortality.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"
                   target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.mortality.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-co2.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.co2.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed-water-perbird.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.feed.water.per.bird.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-required-temperature.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.required.temp.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-daily-silo-fill.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.daily.silo.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-standard-deviation.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.standard.deviation.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-cv.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu" style="width: 100%">
                    <h3><%=session.getAttribute("history.graph.page.menu.cv.label")%></h3></a>
            </td>
        </tr>
        <%}%>
    </table>
</div>
<div id="div2" class="drop-down-show-hide" style="width: 100%;">
    <table id="hourly-history-menu" border="0" cellpadding="0" cellspacing="0" border="1" style="border: 0px solid #1B213B;" width="100%;">
        <!----------------------------------------------SPECIAL GRAPHS----------------------------------------------------------------------------------->
        <%if (programId == 131301 || programId == 96601 || programId == 106401 || programId == 116801 || programId == 93401 || programId == 95901
           || programId == 122901 || programId == 63301 || programId == 130601 || programId == 130401 || programId == 81701 || programId == 12707
           || programId == 128901 || programId == 128901 || programId == 128101 || programId == 65103 || programId == 61124 || programId == 13190
           || programId == 130902 || programId == 126102){%>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed-water-24hour-special.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.feed.water.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-temp-humidity-24hour.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.temperature.humidity.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed2-water2-24hour.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.feed2.water2.label")%></h3></a>
            </td>
        </tr>
        <%} else {%>
        <!----------------------------------------------------NORMAL GRAPHS---------------------------------------------------------------------------------->
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed-water-24hour.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.feed.water.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-temp-humidity-24hour.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.temperature.humidity.label")%></h3></a>
            </td>
        </tr>
        <tr class="row-menu ui-widget ui-corner-all">
            <td>
                <a href="./rmctrl-flock-feed2-water2-24hour.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="<%=pageDir%>" class="link-menu">
                    <h3><%=session.getAttribute("history.graph.page.menu.feed2.water2.label")%></h3></a>
            </td>
        </tr>
        <%}%>
    </table>
</div>
<script>
    $("document").ready(function () {
        $("#feed-water")[0].click();
    });
    $('.drop-down-show-hide').hide();
    $('#combobox').change(function () {
        $('.drop-down-show-hide').hide();
        $('#' + this.value).show();
        $('#daily-history-menu tr').removeClass('selected');
    });
    $("#combobox").val("div1").change();

    $('#daily-history-menu tr').on('click', function () {
        $('#daily-history-menu tr').removeClass('selected');
        $(this).toggleClass('selected');
        if ($(this).data('href') !== undefined) {
            document.location = $(this).data('href');
        }
    });

    $('#hourly-history-menu tr').on('click', function () {
        $('#hourly-history-menu tr').removeClass('selected');
        $(this).toggleClass('selected');
        if ($(this).data('href') !== undefined) {
            document.location = $(this).data('href');
        }
    });
</script>
</body>
</html>