<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.*" %>
<%@ page import="java.util.Collection" %>


<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Program program = (Program) request.getAttribute("program");
    Collection<Data> dataSystemStates = (Collection<Data>) request.getAttribute("dataSystemStates");
    Collection<SystemState> systemStateNames = (Collection<SystemState>) request.getAttribute("systemStateNames");
%>
<%! ProgramSystemState findSystemState(Collection<ProgramSystemState> dataSystemStates, Long systemStateType, int number) {
    for (ProgramSystemState s : dataSystemStates) {
        if (s.getDataId().equals(systemStateType) && s.getNumber() == number) {
            return s;
        }
    }
    return null;
}
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Assign System States</title>

    <link rel="StyleSheet" type="text/css" href="resources/custom/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css"/>

    <script type="text/javascript" src="resources/custom/javascript/ftabs.js">;</script>
    <script type="text/javascript" src="resources/custom/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/custom/javascript/general.js">;</script>

    <script type="text/javascript">
        function save() {
            var datas = document.addForm.dataid;
            var numbers = document.addForm.numbers;
            var systemstatenames = document.addForm.systemstatenames;
            var dataMap = new Hashtable();
            var k = 0;
            for (var i = 0; i < datas.length; i++) {
                var bitMap = new Hashtable();
                var datavalue = datas[i].value;
                for (var j = k; j < (k + 10); j++) {
                    var bitvalue = numbers[j].value;
                    var selected = systemstatenames[j].selectedIndex;
                    var relayText = systemstatenames[j].options[selected].innerHTML + "-" + selected;
                    bitMap.put(bitvalue, relayText);
                }
                dataMap.put(datavalue, bitMap);
                k += 10;
            }
            document.getElementById("datamap").value = dataMap;
        }

    </script>
<head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td width="483">
                <p>

                <h1>Add Alarms</h1></p>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="2">
                <jsp:include page="messages.jsp"/>
            </td>
        </tr>
        <tr>
            <td>
                <p>

                <h2> Add System States to <%=program.getName()%>
                </h2></p>
                <form id="addForm" name="addForm" action="./assignsystemstate.html" method="post"
                      onsubmit="return save();">
                    <input type="hidden" id="programId" name="programId" value="<%=program.getId()%>">
                    <input type="hidden" id="datamap" name="datamap">
                    <table>
                        <tr>
                            <% for (Data dataSysState : dataSystemStates) {%>
                            <td valign="top"><p>

                                <h3><%=dataSysState.getLabel()%>
                                </h3>
                                <input type="hidden" id="dataid" name="dataid" value="<%=dataSysState.getId()%>">
                                <table class="table-list" border="0" cellPadding=1 cellSpacing=1 width="100%">
                                    <thead>
                                    <tr>
                                        <th align="left" width="20px">Number</th>
                                        <th align="left" width="80px">Text</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% int numbers = 10;%>
                                    <%
                                        Collection<ProgramSystemState> programSystemStates = program.getProgramSystemStateByData(dataSysState.getId());%>
                                    <% for (int number = 1; number <= numbers; number++) {%>
                                    <% ProgramSystemState systemState = findSystemState(programSystemStates, dataSysState.getId(), number);%>
                                    <tr>
                                        <td align="left" width="50px">
                                            <input style="width:50px" type="text" name="numbers" value="<%=number%>">
                                        </td>
                                        <td align="left" width="80px">
                                            <select id="systemstatenames" name="systemstatenames" style="width:auto;">
                                                <% for (SystemState systemStateName : systemStateNames) {%>
                                                <% if (systemState != null && systemState.getText().equals(systemStateName.getText())) {%>
                                                <option value="<%=systemStateName.getId()%>"
                                                        selected><%=systemStateName.getText()%>
                                                </option>
                                                <%} else {%>
                                                <option value="<%=systemStateName.getId()%>"><%=systemStateName.getText()%>
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
                                        onclick='return back("./all-screens.html?programId=<%=program.getId()%>");'>
                                    <%=session.getAttribute("button.back")%>
                                </button>
                                <button style="float:left" id="btnSave" name="btnSave" type="submit">
                                    <%=session.getAttribute("button.save")%>
                                </button>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>

</body>
</html>
