<%@ page import="com.parq.web.model.WebUser"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>
<%@ page import="com.parq.web.PasswordChangeRequest" %>

<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />
<jsp:useBean id="changePasswordRequest" class="com.parq.web.PasswordChangeRequest" scope="request" />

<jsp:setProperty name="changePasswordRequest" property="*" />

<%
	ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
	boolean changeSuccessful = service.changePasswordForUser(user, changePasswordRequest);

	if (changeSuccessful == false) {
%>
<html>
<jsp:forward page="/web/action/user/changePassword.jsp" />
</html>
<%
	} else {
		session.removeAttribute("user");
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
								<li>Your password has been successfully changed, please re-login</li>
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
