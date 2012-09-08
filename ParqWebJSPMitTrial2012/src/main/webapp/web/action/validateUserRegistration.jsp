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

	if (registrationSuccessful == false) {
	%>
		<jsp:forward page="/web/action/signup.jsp" />	
	<%
	} else {
	%>
		<head>
			<jsp:include page="/web/templates/nav.jsp" />
		</head>
		<body>
			<div class="well span8 container modal-body">
					<h2>Success</h2>
					<table>
						<tbody xmlns="http://www.w3.org/1999/xhtml">
							<tr>
								<td colspan="2">
									<ul class="errorlist">
										<li>Your registration is complete. You can now sign-in</li>
									</ul>
								</td>
							</tr>
						</tbody>
					</table>
			</div>
		</body>
	<% } %>
</html>