<%@ page import="com.parq.web.model.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />

<% if (user.isAuthenticated() == false ||  user.isAdminUser() == false) {
%>
<jsp:forward page="../../index.jsp"/>
<% } %>