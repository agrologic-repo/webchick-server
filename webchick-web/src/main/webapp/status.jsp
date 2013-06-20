<jsp:useBean id="task" scope="session" class="com.agrologic.app.web.TaskBean"/>
<html>

<head>
    <title>JSP Progress Bar</title>
    <% if (task.isRunning()) { %>
    <SCRIPT LANGUAGE="JavaScript">
        setTimeout("location='status.jsp'", 1000);
    </SCRIPT>
    <% } %>
</head>

<body>
The progress bar is built as an HTML table with 10 cells. Each cell represents 10% of the time that is necessary to
execute the task.

<h1 align="center">JSP Progress Bar</h1>

<h2 align="center">
    Result: <%= task.getResult() %><br/>
    <% int percent = task.getPercent(); %>
    <%= percent %>%
</h2>

<table width="60%" align="center"
       border="1" cellpadding="0" cellspacing="2">
    <TR>
        <% for (int i = 10; i <= percent; i += 10) { %>
        <td width="10%" bgcolor="#000080">&nbsp;</td>
        <% } %>
        <% for (int i = 100; i > percent; i -= 10) { %>
        <td width="10%">&nbsp;</TD>
        <% } %>
    </tr>
</table>
The task may be in one of the following states: "Running," "Completed," "Not Started," and "Stopped."

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="center">
            <% if (task.isRunning()) { %>
            Running
            <% } else { %>
            <% if (task.isCompleted()) { %>
            Completed
            <% } else if (!task.isStarted()) { %>
            Not Started
            <% } else { %>
            Stopped
            <% } %>
            <% } %>
        </td>
    </tr>
    At the end of the page, there is a button that lets the user stop or restart the task:

    <tr>
        <td align="center">
            <br/>
            <% if (task.isRunning()) { %>
            <form method="get" action="stop.jsp">
                <input type="submit" value="Stop">
            </form>
            <% } else { %>
            <form method="get" action="start.jsp">
                <input type="submit" value="Start">
            </form>
            <% } %>
        </td>
    </tr>
</table>

</body>
</html>