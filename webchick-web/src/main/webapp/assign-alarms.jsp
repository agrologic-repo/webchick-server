<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Alarm" %>
<%@ page import="com.agrologic.app.model.DataDto" %>
<%@ page import="com.agrologic.app.model.ProgramAlarm" %>
<%@ page import="com.agrologic.app.model.ProgramDto" %>
<%@ page import="java.util.List" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    ProgramDto program = (ProgramDto) request.getSession().getAttribute("program");
    List<DataDto> dataAlarms = (List<DataDto>) request.getSession().getAttribute("dataAlarms");
    List<Alarm> alarmNames = (List<Alarm>) request.getSession().getAttribute("alarmNames");
    String datamap;
%>
<%! ProgramAlarm findAlarm(List<ProgramAlarm> dataAlarms, Long alarmType, int digitNumber) {
    for (ProgramAlarm pa : dataAlarms) {
        if (pa.getDataId().equals(alarmType) && pa.getDigitNumber() == digitNumber) {
            return pa;
        }
    }
    return null;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>Add alarms</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <script type="text/javascript" src="js/fglobal.js"></script>
    <script type="text/javascript" src="js/ftabs.js"></script>
    <script type="text/javascript" src="js/util.js"></script>
    <script type="text/javascript">
        function save() {
            var datas = document.addForm.dataid;
            var digits = document.addForm.digits;
            var alarmnames = document.addForm.alarmnames;
            var dataMap = new Hashtable();
            var k = 0;
            for (var i = 0; i < datas.length; i++) {
                var digitMap = new Hashtable();
                var datavalue = datas[i].value;
                for (var j = k; j < (k + 10); j++) {
                    var digitvalue = digits[j].value;
                    var selected = alarmnames[j].selectedIndex;
                    var digitText = alarmnames[j].options[selected].innerHTML + selected;
                    digitMap.put(digitvalue, digitText);
                }
                dataMap.put(datavalue, digitMap);
                k += 10;
            }
            document.getElementById("datamap").value = dataMap;
        }
        function back(link) {
            window.document.location.replace(link);
            return false;
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
            <td valign="top" height="648px">
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td width="483">
                            <p>

                            <h1>Add Alarms</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%@ include file="messages.jsp" %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2> Add Alarms to <%=program.getName()%>
                            </h2></p>
                            <form id="addForm" name="addForm" action="./assignalarms.html" method="post"
                                  onsubmit="return save();">
                                <input type="hidden" id="programId" name="programId" value="<%=program.getId() %>">
                                <table>
                                    <tr>
                                        <% for (DataDto dataAlarm : dataAlarms) {%>
                                        <td valign="top"><p>

                                            <h3><%=dataAlarm.getLabel() %>
                                            </h3>
                                            <input type="hidden" id="dataid" name="dataid"
                                                   value="<%=dataAlarm.getId() %>">
                                            <input type="hidden" id="datamap" name="datamap">
                                            <table class="table-list" border="0" cellPadding=1 cellSpacing=1
                                                   width="100%">
                                                <thead>
                                                <tr>
                                                    <th align="left" width="20px">Digit</th>
                                                    <th align="left" width="80px">Text</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <% int digitNumbers = 10; %>
                                                <%
                                                    List<ProgramAlarm> programAlarms = program.getProgramAlarmsByData(dataAlarm.getId()); %>
                                                <% for (int digitNumber = 1; digitNumber <= digitNumbers; digitNumber++) { %>
                                                <% ProgramAlarm alarm = findAlarm(programAlarms, dataAlarm.getId(), digitNumber); %>
                                                <tr>
                                                    <td align="left" width="30px">
                                                        <input style="width:30px" type="text" name="digits"
                                                               value="<%=digitNumber%>">
                                                    </td>
                                                    <td align="left" width="80px">
                                                        <select id="alarmnames" name="alarmnames" style="width:auto;">
                                                            <%for (Alarm alarmName : alarmNames) {%>
                                                            <% if (alarm != null && alarm.getText().equals(alarmName.getText())) {%>
                                                            <option value="<%=alarmName.getId()%>"
                                                                    selected><%=alarmName.getText()%>
                                                            </option>
                                                            <%} else {%>
                                                            <option value="<%=alarmName.getId()%>"><%=alarmName.getText()%>
                                                            </option>
                                                            <%}%>
                                                            <%}%>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <%}%>
                                                </tbody>
                                            </table>
                                            <%}%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <button style="float:left" id="btnBack" name="btnBack"
                                                    onclick='return back("./all-screens.html?programId=<%=program.getId() %>");'>
                                                <%=session.getAttribute("button.back") %>
                                            </button>
                                            <button style="float:left" id="btnSave" name="btnSave" type="submit">
                                                <%=session.getAttribute("button.save") %>
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
