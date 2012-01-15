<%@ page import="com.parq.web.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />

<% if (user.isAuthenticated() == false ||  user.isAdminUser() == false) {
%>
<jsp:forward page="../../index.jsp"/>
<% } %>