<%@ page import="com.parq.web.model.UserRegistration"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>

<jsp:useBean id="userRegistration" class="com.parq.web.model.UserRegistration" scope="request" />
<jsp:setProperty name="userRegistration" property="*" />

<%
	ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
	boolean registrationSuccessful = service.registerNewUser(userRegistration);

	if (registrationSuccessful == false) {
%>
<html>
<jsp:forward page="/web/action/signup.jsp" />
</html>
<%
	} else {
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/web/templates/base_header.jsp" />
</head>
<body>
	<jsp:include page="/web/templates/base_body_start.jsp" />
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
	<jsp:include page="/web/templates/base_body_end.jsp" />
</body>
</html>


<% } %>