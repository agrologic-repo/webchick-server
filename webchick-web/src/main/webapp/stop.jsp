<jsp:useBean id="task" scope="session" class="com.agrologic.app.web.TaskBean"/>

<% task.setRunning(false); %>

<jsp:forward page="status.jsp"/>
