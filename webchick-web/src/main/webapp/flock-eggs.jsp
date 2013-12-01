<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.EggDao" %>
<%@ page import="com.agrologic.app.model.Eggs" %>
<%@ page import="java.util.Collection" %>
<!DOCTYPE html>
<%
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    EggDao eggDao = DbImplDecider.use(DaoType.MYSQL).getDao(EggDao.class);
    Collection<Eggs> list = eggDao.getAllByFlockId(flockId);
    request.setAttribute("list", list);
%>
<html>
<head>
    <link rel="StyleSheet" type="text/css" href="resources/style/jquery-ui.css"/>
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
            width: 100%;
            margin: 20px 0;
        }

        div#eggs-contain table {
            margin: 1em 0;
            border-collapse: collapse;
            width: 100%;
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
    </style>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
    <script>
        $(function () {
            var day = $("#day"),
                    numOfBirds = $("#numOfBirds"),
                    eggQuantity = $("#eggQuantity"),
                    cracked = $("#cracked"),
                    softShelled = $("#softShelled"),
                    allFields = $([]).add(day).add(numOfBirds).add(eggQuantity).add(cracked).add(softShelled),
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

            $("#dialog-form").dialog({
                autoOpen: false,
                height: 500,
                width: 350,
                modal: true,
                buttons: {
                    "OK": function () {
                        var bValid = true;
                        allFields.removeClass("ui-state-error");
                        bValid = bValid && checkLength(day, "day", 1, 6);
                        bValid = bValid && checkLength(numOfBirds, "numOfBirds", 1, 6);
                        bValid = bValid && checkLength(eggQuantity, "eggQuantity", 1, 16);
                        if (bValid) {
                            $("#eggs tbody").append(
                                    "<tr>" +
                                            "<td>" + day.val() + "</td>" +
                                            "<td>" + numOfBirds.val() + "</td>" +
                                            "<td>" + eggQuantity.val() + "</td>" +
                                            "<td>" + softShelled.val() + "</td>" +
                                            "<td>" + cracked.val() + "</td>" +
                                            "</tr>");
                            $.ajax({
                                type: "POST", // HTTP method POST or GET
                                url: "add-egg.html", //Where to make Ajax calls
                                dataType: "json", // Data type, HTML, json etc.
                                data: { flockId: <%=flockId%>, day: day.val(), numOfBirds: numOfBirds.val(),
                                    eggQuantity: eggQuantity.val(), softShelled: softShelled.val(),
                                    cracked: cracked.val() },
                                success: function (response) {
                                    location.reload();
                                },
                                error: function (xhr, ajaxOptions, thrownError) {
                                    //On error, we alert user
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
                $('#eggs td img.delete').click(function () {
                    $.ajax({
                        type: "POST", // HTTP method POST or GET
                        url: "remove-egg.html?id=" + $(this).parent().parent().attr('id'),
                        success: function (response) {
                            location.reload();
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            //On error, we alert user
                            location.reload();
                        }
                    });
                    $(this).parent().parent().remove();
                });
            });
            $("#add-egg")
                    .button()
                    .click(function () {
                        $("#dialog-form").dialog("open");
                    });
        });
    </script>
</head>
<body>

<table width="100%">
    <tr>
        <td valign="top" width="100%">
            <fieldset>
                <div id="dialog-form" title="Create new egg">
                    <p class="validateTips">All form fields are required.</p>

                    <form>
                        <fieldset>
                            <label for="day">Day</label>
                            <input type="text" name="day" id="day" class="text ui-widget-content ui-corner-all">
                            <label for="numOfBirds">Birds</label>
                            <input type="text" name="numOfBirds" id="numOfBirds"
                                   class="text ui-widget-content ui-corner-all">
                            <label for="eggQuantity">Eggs Quantity </label>
                            <input type="text" name="eggQuantity" id="eggQuantity"
                                   class="text ui-widget-content ui-corner-all">
                            <label for="softShelled">Soft Shelled</label>
                            <input type="text" name="softShelled" id="softShelled" value=""
                                   class="text ui-widget-content ui-corner-all">
                            <label for="cracked">Cracked Eggs</label>
                            <input type="text" name="cracked" id="cracked" value=""
                                   class="text ui-widget-content ui-corner-all">
                        </fieldset>
                    </form>
                </div>
                <legend>Eggs Manager</legend>
                <div id="eggs-contain" class="ui-widget">
                    <table id="eggs" class="ui-widget ui-widget-content">
                        <thead>
                        <tr class="ui-widget-header ">
                            <th>Day</th>
                            <th>Birds</th>
                            <th>Egg Quantity</th>
                            <th>Soft Shelled</th>
                            <th>Cracked</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${list}" var="item">
                            <tr id="<c:out value="${item.id}"/>">
                                <td><c:out value="${item.day}"/></td>
                                <td><c:out value="${item.numOfBirds}"/></td>
                                <td><c:out value="${item.eggQuantity}"/></td>
                                <td><c:out value="${item.softShelled}"/></td>
                                <td><c:out value="${item.cracked}"/></td>
                                <td><img class="delete" src="resources/images/close.png"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <button id="add-egg">Add</button>
                </div>
            </fieldset>
        </td>
    </tr>
</table>
</body>
</html>
