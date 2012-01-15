<%@ page import="com.parq.web.WebUser"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>

<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />
<jsp:setProperty name="user" property="*" />

<%
	ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
	WebUser result = service.validateUser(user);
	session.setAttribute("user", result);

	if (result.isAuthenticated() == false) {
		result.setLoginFailed(true);
%>
<html>
<jsp:forward page="/web/action/login.jsp" />
</html>
<%
	} else {
		result.setLoginFailed(false);
%>
<html>
<jsp:forward page="/index.jsp" />
</html>
<%
	}
%>
