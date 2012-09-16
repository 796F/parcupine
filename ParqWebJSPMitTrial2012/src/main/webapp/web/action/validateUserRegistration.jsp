<%@ page import="com.parq.web.model.UserRegistration"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>

<jsp:useBean id="userRegistration" class="com.parq.web.model.UserRegistration" scope="request" />
<jsp:setProperty name="userRegistration" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<%
	ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
	userRegistration.setPassword1("a");
	userRegistration.setPassword2("a");
	boolean registrationSuccessful = service.registerNewUser(userRegistration);

	%>

	<jsp:forward page="/web/main.jsp"/>	
	
</html>