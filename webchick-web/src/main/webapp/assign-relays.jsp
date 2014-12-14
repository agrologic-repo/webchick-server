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
    Collection<Data> dataRelays = (Collection<Data>) request.getAttribute("dataRelays");
    Collection<Relay> relayNames = (Collection<Relay>) request.getAttribute("relayNames");
%>

<%! ProgramRelay findRelay(Collection<ProgramRelay> dataRelays, Long relayType, int bitNumber) {
    for (ProgramRelay r : dataRelays) {
        if (r.getDataId().equals(relayType) && r.getBitNumber() == bitNumber) {
            return r;
        }
    }
    return null;
}
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Controller Details</title>

    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/ftabs.js">;</script>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>

    <script type="text/javascript">
        function save() {
            var datas = document.addForm.dataid;
            var bits = document.addForm.bits;
            var relaynames = document.addForm.relaynames;
            var dataMap = new Hashtable();
            var k = 0;
            for (var i = 0; i < datas.length; i++) {
                var bitMap = new Hashtable();
                var datavalue = datas[i].value;
                for (var j = k; j < (k + 16); j++) {
                    var bitvalue = bits[j].value;
                    var selected = relaynames[j].selectedIndex;
                    var relayText = relaynames[j].options[selected].innerHTML + "-" + selected;
                    bitMap.put(bitvalue, relayText);
                }
                dataMap.put(datavalue, bitMap);
                k += 16;
            }
            document.getElementById("datamap").value = dataMap;
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
            <td>
                <h1>Add Relays</h1>

                <h2>Add Relays to <%=program.getName()%>
                </h2>
            </td>
        </tr>
        <tr>
            <td>
                <form id="addForm" name="addForm" method="post" action="./assignrelays.html" onsubmit="return save();">
                    <input type="hidden" id="programId" name="programId" value="<%=program.getId()%>"/>
                    <input type="hidden" id="datamap" name="datamap"/>
                    <table border="0" cellPadding="1" cellSpacing="1">
                        <tr>
                            <%for (Data dataRelay : dataRelays) {%>
                            <td>
                                <h3><%=dataRelay.getLabel()%>
                                </h3>
                                <input type="hidden" id="dataid" name="dataid" value="<%=dataRelay.getId()%>"/>
                                <table class="table-list" border="1" cellPadding="5" cellSpacing="5" width="100%">
                                    <thead>
                                    <tr>
                                        <th align="left" width="20px">Bit</th>
                                        <th align="left" width="80px">Text</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% int bitNumbers = 16;%>
                                    <%
                                        Collection<ProgramRelay> programRelays = program.getProgramRelayByData(dataRelay.getId());%>
                                    <% for (int bitNumber = 0; bitNumber < bitNumbers; bitNumber++) {%>
                                    <% ProgramRelay relay = findRelay(programRelays, dataRelay.getId(), bitNumber);%>
                                    <tr>
                                        <td align="center" width="20px">
                                            <input style="width:20px" type="text" readonly name="bits" value="<%=bitNumber%>"/>
                                        </td>
                                        <td align="left" width="80px">
                                            <select id="relaynames" name="relaynames" style="width:120px;">
                                                <% for (Relay relayName : relayNames) {%>
                                                <% if ((relay != null) && (relay.getText().equals(relayName.getText()))) {%>
                                                <option value="<%=relayName.getId()%>" selected><%=relayName.getText()%>
                                                </option>
                                                <%} else {%>
                                                <option value="<%=relayName.getId()%>"><%=relayName.getText()%>
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
                                <button style="float:left" id="btnCancel" name="btnCancel"
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
