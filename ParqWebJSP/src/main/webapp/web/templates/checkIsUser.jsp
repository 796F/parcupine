<%@ page import="com.parq.web.model.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />

<% if (user.isAuthenticated() == false || (user.isAuthenticated() == true && user.isAdminUser() == true)) { %>
	<jsp:forward page="../../index.jsp"/>
<% } %>