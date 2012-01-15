<%@ page import="com.parq.web.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />

<% if (user.isAuthenticated() == false || (user.isAuthenticated() == true && user.isAdminUser() == true)) { %>
	<jsp:forward page="../../index.jsp"/>
<% } %>