<% session.removeAttribute("task");%>
<jsp:useBean id="task" scope="session" class="com.agrologic.app.web.TaskBean"/>

<% task.setRunning(true);%>
<% new Thread(task).start();%>

<jsp:forward page="status.jsp"/>
