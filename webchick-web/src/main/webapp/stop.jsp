<jsp:useBean id="task" scope="session" class="com.agrologic.web.TaskBean"/>

<% task.setRunning(false); %>

<jsp:forward page="status.jsp"/>
