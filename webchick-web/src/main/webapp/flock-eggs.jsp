<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.EggDao" %>
<%@ page import="com.agrologic.app.graph.DataGraphCreator" %>
<%@ page import="com.agrologic.app.management.PerGrowDayHistoryDataType" %>
<%@ page import="com.agrologic.app.model.Eggs" %>
<%@ page import="com.agrologic.app.service.history.FlockHistoryService" %>
<%@ page import="com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>

<!--------------------------------------------------------------------------------------------------->
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ include file="language.jsp" %>
<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    String pageDir = "rightPage";
    if (session.getAttribute("dir").equals("rtl")) {
        pageDir = "leftPage";
    }

%>
<!--------------------------------------------------------------------------------------------------->
<!DOCTYPE html>
<%
    //    Long flockId = Long.parseLong(request.getParameter("flockId"));     /////////////////////////////////////!!!!!!!!!!!!!!!!!!!!!
    EggDao eggDao = DbImplDecider.use(DaoType.MYSQL).getDao(EggDao.class);
    ArrayList<Eggs> historyList = getEggsList(flockId);
    ArrayList<Eggs> currentList = new ArrayList<Eggs>();
    try {
        currentList = (ArrayList<Eggs>) eggDao.getAllByFlockId(flockId);
    } catch (Exception e) {
        e.printStackTrace();
    }
    // merge data
    if (currentList.size() == 0) {
        for (Eggs histEggs : historyList) {
            histEggs.setNumOfBirds(0);
            histEggs.setEggQuantity(0);
            histEggs.setCracked(0);
            histEggs.setSoftShelled(0);
            eggDao.insert(histEggs);
        }
        try {
            currentList = (ArrayList<Eggs>) eggDao.getAllByFlockId(flockId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        if (currentList.size() < historyList.size()) {
            int workingSize = currentList.size();
            for (int i = workingSize; i < historyList.size(); i++) {
                Eggs histEggs = historyList.get(i);
                histEggs.setNumOfBirds(0);
                histEggs.setEggQuantity(0);
                histEggs.setCracked(0);
                histEggs.setSoftShelled(0);
                eggDao.insert(histEggs);
                currentList.add(histEggs);
            }
        }
    }
    request.setAttribute("list", currentList);
%>
<%!
    ArrayList<Eggs> getEggsList(Long flockId) {
        FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
        try {
            Map<Integer, String> histList = flockHistoryService.getFlockPerDayNotParsedReports(flockId);
            ArrayList<Eggs> eggsList = new ArrayList<Eggs>();
            for (Map.Entry<Integer, String> entry : histList.entrySet()) {
                Integer day = entry.getKey();
                String historyString = entry.getValue();
                Long feedConsump = DataGraphCreator.getValueByDataIdFromHistory(
                        PerGrowDayHistoryDataType.FEED_CONSUMPTION_ID.id, historyString);
                Long waterConsump = DataGraphCreator.getValueByDataIdFromHistory(
                        PerGrowDayHistoryDataType.WATER_CONSUMPTION_ID.id, historyString);
                Long dailyMortal = DataGraphCreator.getValueByDataIdFromHistory(
                        PerGrowDayHistoryDataType.DAY_MORTALITY.id, historyString);

                Eggs eggs = new Eggs();
                eggs.setFlockId(flockId);
                eggs.setDay(day);
                eggs.setFeedConsump(feedConsump);
                eggs.setWaterConsump(waterConsump);
                eggs.setDailyMortal(dailyMortal);
                eggsList.add(eggs);
            }
            return eggsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
%>
<html dir="<%=session.getAttribute("dir")%>">
<head>

<!------------------------------------------------------------------------------------------------------------------------>
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

<!------------------------------------------------------------------------------------------------------------------------>
<style>
    label, input {
        display: block;
    }

    input.text {
        margin-bottom: 12px;
        width: 95%;
        padding: .4em;
    }

    fieldset {
        padding: 0;
        border: 0;
        margin-top: 25px;
    }

    h1 {
        font-size: 1.2em;
        margin: .6em 0;
    }

    div#eggs-contain {
        /*width: 100%;*/
        margin: 20px 0;
    }

    div#eggs-contain table {
        margin: 1em 0;
        border-collapse: collapse;
        /*width: 100%;*/
    }

    div#eggs-contain table td, div#eggs-contain table th {
        border: 1px solid #eee;
        padding: .6em 10px;
        text-align: left;
    }

    .ui-dialog .ui-state-error {
        padding: .3em;
    }

    .validateTips {
        border: 1px solid transparent;
        padding: 0.3em;
    }

    div#eggs-reports-contain {
        /*width: 100%;*/
        margin: 20px 0;
    }

    div#eggs-reports-contain table {
        margin: 1em 0;
        border-collapse: collapse;
        /*width: 100%;*/
    }

    div#eggs-reports-contain table td, div#eggs-reports-contain table th {
        border: 1px solid #eee;
        padding: .6em 10px;
        text-align: left;
    }

</style>

<!------------------------------>
<!------------------------------>
<style type="text/css">
    .menuFarme {
        border-style: 1px solid #1B213B;

        border-collapse: collapse;
    "
    }

    frame {
        border: 0px solid #1B213B;;
    }
</style>
<!------------------------------>
<!------------------------------>


<script src="resources/javascript/jquery.tablesorter.js">;</script>
<script src="resources/javascript/jquery.scrolltable.js">;</script>
<script>
$(function () {

    var day = $("#day"),
            numOfBirds = $("#numOfBirds"),
            eggQuantity = $("#eggQuantity"),
            cracked = $("#cracked"),
            softShelled = $("#softShelled"),
            feedConsump = $("#feedConsump"),
            waterConsump = $("#waterConsump"),
            dailyMortal = $("#dailyMortal"),
            allFields = $([]).add(day).add(numOfBirds).add(eggQuantity)
                    .add(cracked).add(softShelled).add(feedConsump)
                    .add(waterConsump).add(dailyMortal),
            tips = $(".validateTips");

    function updateTips(t) {
        tips.text(t).addClass("ui-state-highlight");
        setTimeout(function () {
            tips.removeClass("ui-state-highlight", 1500);
        }, 500);
    }

    function checkLength(o, n, min, max) {
        if (o.val().length > max || o.val().length < min) {
            o.addClass("ui-state-error");
            updateTips("Length of " + n + " must be between " + min + " and " + max + ".");
            return false;
        } else {
            return true;
        }
    }

    function checkRegexp(o, regexp, n) {
        if (!( regexp.test(o.val()) )) {
            o.addClass("ui-state-error");
            updateTips(n);
            return false;
        } else {
            return true;
        }
    }

    $("#egg-tabs").tabs();

//        $('.scrollTable').scrolltable({
//            stripe: true,
//            maxWidth: 300,
//            setWidths: true
//        });

    $("#dialog-form").dialog({
        autoOpen: false,
        height: 700,
        width: 350,
        modal: true,
        buttons: {
            "OK": function () {
                var bValid = true;
                allFields.removeClass("ui-state-error");
                bValid = bValid && checkLength(day, "day", 1, 6);
                bValid = bValid && checkLength(numOfBirds, "numOfBirds", 1, 6);
                bValid = bValid && checkLength(eggQuantity, "eggQuantity", 1, 16);
                bValid = bValid && checkRegexp(day, /([0-9])+$/i, "Only numbers expected.");
                bValid = bValid && checkRegexp(numOfBirds, /([0-9])+$/i, "Only numbers expected.");
                bValid = bValid && checkRegexp(eggQuantity, /([0-9])+$/i, "Only numbers expected.");

                if (bValid) {
                    $.ajax({
                        type: "POST",
                        url: "add-egg.html",
                        dataType: "json",
                        data: { flockId: <%=flockId%>, day: day.val(), numOfBirds: numOfBirds.val(),
                            eggQuantity: eggQuantity.val(), softShelled: softShelled.val(),
                            cracked: cracked.val(),
                            feedConsump: feedConsump.val(),
                            waterConsump: waterConsump.val(),
                            dailyMortal: dailyMortal.val()},
                        success: function (response) {
                            location.reload();
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            location.reload();
                        }
                    });

                    $(this).dialog("close");
                }
            },
            Cancel: function () {
                $(this).dialog("close");
            }
        },
        close: function () {
            allFields.val("").removeClass("ui-state-error");
        }
    });

    $(document).ready(function () {
        /**
         * edit
         */
        $(document).on('click', 'span.edit', function () {
            var dialog = $("#dialog-form").clone();

            var _day = $(this).parent().parent().children().get(0).innerHTML,
                    _numOfBirds = $(this).parent().parent().children().get(1).innerHTML,
                    _eggQuantity = $(this).parent().parent().children().get(2).innerHTML,
                    _cracked = $(this).parent().parent().children().get(3).innerHTML,
                    _softShelled = $(this).parent().parent().children().get(4).innerHTML,
                    _feedConsump = $(this).parent().parent().children().get(5).innerHTML,
                    _waterConsump = $(this).parent().parent().children().get(6).innerHTML,
                    _dailyMortal = $(this).parent().parent().children().get(7).innerHTML;

            var day = dialog.find(("#day"));
            var numOfBirds = dialog.find(("#numOfBirds"));
            var eggQuantity = dialog.find(("#eggQuantity"));
            var cracked = dialog.find(("#cracked"));
            var softShelled = dialog.find(("#softShelled"));
            var feedConsump = dialog.find(("#feedConsump"));
            var waterConsump = dialog.find(("#waterConsump"));
            var dailyMortal = dialog.find(("#dailyMortal"));

            day.val(_day);
            numOfBirds.val(_numOfBirds);
            eggQuantity.val(_eggQuantity);
            cracked.val(_cracked);
            softShelled.val(_softShelled);
            feedConsump.val(_feedConsump);
            waterConsump.val(_waterConsump);
            dailyMortal.val(_dailyMortal);

            dialog.dialog({
                title: "Edit eggs data",
                autoOpen: false,
                height: 700,
                width: 350,
                modal: true,
                buttons: {
                    "Save": function () {
                        $.ajax({
                            type: "POST", // HTTP method POST or GET
                            url: "edit-egg.html", //Where to make Ajax calls
                            dataType: "json", // Data type, HTML, json etc.
                            data: {
                                flockId: <%=flockId%>, day: day.val(),
                                numOfBirds: numOfBirds.val(),
                                eggQuantity: eggQuantity.val(),
                                softShelled: softShelled.val(),
                                cracked: cracked.val(),
                                feedConsump: feedConsump.val(),
                                waterConsump: waterConsump.val(),
                                dailyMortal: dailyMortal.val()
                            },
                            success: function (response) {
                                location.reload();
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                location.reload();
                            }
                        });
                        $(this).dialog("close");
                    },
                    Cancel: function () {
                        $(this).dialog("close");
                    }
                },
                close: function () {
                    allFields.val("").removeClass("ui-state-error");
                }
            });
            dialog.dialog("open");
            return false;
        });

        /**
         * delete
         */
        $(document).on('click', 'span.delete', function () {
            $.ajax({
                type: "POST",
                url: "remove-egg.html?flockId=<%=flockId%>&day=" +
                        $(this).parent().parent().attr('id'),
                success: function (response) {
                    location.reload();
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    location.reload();
                }
            });
            $(this).closest('tr').find('td').fadeOut(1000,
                    function () {
                        $(this).parents('tr:first').remove();
                    });
            return false;
        });

//            /**
//             * sorting eggs table
//             */
//            $("#eggs").tablesorter({
//                widgets: ['zebra']
//            });
    });

    /**
     *
     */
    $("#add-egg").button().click(function () {
        $("#dialog-form").dialog("open");
    });
});
</script>
</head>
<body>
<div id="egg-tabs">
<ul>
    <li><a href="#tabs-1">Eggs List</a></li>
    <li><a href="#tabs-2">Eggs Reports</a></li>
    <li><a href="#tabs-3">Eggs Graphs</a></li>
</ul>
<div id="tabs-1">
    <table width="100%">
        <tr>
            <td valign="top" width="100%">
                <fieldset>
                    <div id="dialog-form" title="Create new egg">
                        <p class="validateTips">All form fields are required.</p>

                        <form>
                            <fieldset>
                                <label for="day"><%=session.getAttribute("table.eggs.day")%>
                                </label>
                                <input type="text" name="day" id="day" class="text ui-widget-content ui-corner-all">
                                <label for="numOfBirds"><%=session.getAttribute("table.eggs.birds")%>
                                </label>
                                <input type="text" name="numOfBirds" id="numOfBirds"
                                       class="text ui-widget-content ui-corner-all">
                                <label for="eggQuantity"><%=session.getAttribute("table.eggs.egg.quantity")%>
                                </label>
                                <input type="text" name="eggQuantity" id="eggQuantity"
                                       class="text ui-widget-content ui-corner-all">
                                <label for="softShelled"><%=session.getAttribute("table.eggs.soft.shelled")%>
                                </label>
                                <input type="text" name="softShelled" id="softShelled" value=""
                                       class="text ui-widget-content ui-corner-all">
                                <label for="cracked"><%=session.getAttribute("table.eggs.cracked")%>
                                </label>
                                <input type="text" name="cracked" id="cracked" value=""
                                       class="text ui-widget-content ui-corner-all">
                                <label for="feedConsump"><%=session.getAttribute("table.eggs.feed.consump")%>
                                </label>
                                <input type="text" name="feedConsump" id="feedConsump" value=""
                                       class="text ui-widget-content ui-corner-all">
                                <label for="waterConsump"><%=session.getAttribute("table.eggs.water.consump")%>
                                </label>
                                <input type="text" name="waterConsump" id="waterConsump" value=""
                                       class="text ui-widget-content ui-corner-all">
                                <label for="dailyMortal"><%=session.getAttribute("table.eggs.daily.mortal")%>
                                </label>
                                <input type="text" name="dailyMortal" id="dailyMortal" value=""
                                       class="text ui-widget-content ui-corner-all">
                            </fieldset>
                        </form>
                    </div>
                    <button id="add-egg"><%=session.getAttribute("button.add")%>
                    </button>
                    <div id="eggs-contain" class="ui-widget">
                        <table id="eggs" class="scrollTable">
                            <thead>
                            <tr class="ui-widget-header">
                                <th><%=session.getAttribute("table.eggs.day")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.birds")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.egg.quantity")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.soft.shelled")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.cracked")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.feed.consump")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.water.consump")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.daily.mortal")%>
                                </th>
                                <th><%=session.getAttribute("table.eggs.action")%>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${list}" var="item">
                                <tr id="<c:out value="${item.day}"/>">
                                    <td><c:out value="${item.day}"/></td>
                                    <td><c:out value="${item.numOfBirds}"/></td>
                                    <td><c:out value="${item.eggQuantity}"/></td>
                                    <td><c:out value="${item.softShelled}"/></td>
                                    <td><c:out value="${item.cracked}"/></td>
                                    <td><c:out value="${item.feedConsump}"/></td>
                                    <td><c:out value="${item.waterConsump}"/></td>
                                    <td><c:out value="${item.dailyMortal}"/></td>
                                    <td><span class="edit"><a href=""><%=session.getAttribute("button.edit")%>
                                    </a></span>
                                            <span class="delete"><a href=""><%=session.getAttribute("button.delete")%>
                                            </a></span>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </fieldset>
            </td>
        </tr>
    </table>
</div>
<div id="tabs-2">
    <%--<jsp:include page="eggs-reports.jsp"/>--%>
    <div id="eggs-reports-contain" class="ui-widget">
        <table id="eggs-reports" class="scrollTable">
            <thead>
            <tr class="ui-widget-header">
                <th><%=session.getAttribute("table.eggs.day")%>
                </th>
                <th><%=session.getAttribute("table.eggs.birds")%>
                </th>
                <th><%=session.getAttribute("table.eggs.egg.quantity")%>
                </th>
                <th>Average eggs</th>
                <th>Actual eggs</th>
                <th>Cracked eggs (%)</th>
                <th>Feed per bird</th>
                <th>Water per bird</th>
                <th>Feed per egg</th>
                <th>Water per egg</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="item">
                <tr id="<c:out value="${item.day}"/>">
                    <td><c:out value="${item.day}"/></td>
                    <td><c:out value="${item.numOfBirds}"/></td>
                    <td><c:out value="${item.eggQuantity}"/></td>
                    <td><c:out value="${(item.eggQuantity/item.numOfBirds)*100}"/></td>
                    <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>
                    <td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"
                                          value="${item.cracked/item.eggQuantity}"/></td>
                    <td><fmt:formatNumber type="number" maxFractionDigits="3"
                                          value="${item.feedConsump/item.numOfBirds}"/></td>
                    <td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"
                                          value="${item.waterConsump/item.numOfBirds}"/></td>
                    <td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"
                                          value="${item.feedConsump/item.eggQuantity}"/></td>
                    <td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"
                                          value="${item.waterConsump/item.eggQuantity}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<div id="tabs-3">
   <iframe width="100%" height="500px" src="./rmctrl-flock-eggs.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" name="iframe_a" style="border:none"></iframe>
    <div id="div1" class="drop-down-show-hide" style="width: 100%;">
        <table id="daily-history-menu" border="0" cellpadding="0" cellspacing="0" border="1" style="border: 0px solid #0084e1; width: 100%">
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggs.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>"  target="iframe_a" class="link-menu"><h3>Eggs</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggstotal.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Total</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-first.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 1</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-second.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 2</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-third.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 3</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-fourth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3> Eggs Counter 4</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-fifth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 5</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-sixth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 6</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-seventh.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 7</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-eighth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 8</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-ninth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 9</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-tenth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu" ><h3>Eggs Counter 10</h3></a></td>
            </tr>
            <%--</tr>--%>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-eleventh.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3> Eggs Counter 11</h3></a></td>
            </tr>
            <tr class="row-menu ui-widget ui-corner-all">
                <td><a href="./rmctrl-flock-eggscounter-twelfth.jsp?flockId=<%=flockId%>&currLocal=<%=currLocal%>" target="iframe_a" class="link-menu"><h3>Eggs Counter 12</h3></a></td>
            </tr>
        </table>
    </div>
</div>
</div>
</body>
</html>
