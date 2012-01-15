<%@ page import="com.parq.web.WebUser"%>
<%@ page import="com.parq.web.PasswordChangeRequest" %>

<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />
<jsp:useBean id="changePasswordRequest" class="com.parq.web.PasswordChangeRequest" scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="/web/templates/checkIsUser.jsp" />
	<jsp:include page="/web/templates/base_header.jsp" />
</head>

<body>
	<jsp:include page="/web/templates/base_body_start.jsp" />

	<div class="well span8 container modal-body">
		<FORM METHOD=POST
			ACTION="<%=request.getContextPath()%>/web/action/validatePasswordChange.jsp">
			<h2>Password</h2>
			<table>
				<tbody xmlns="http://www.w3.org/1999/xhtml">

				<% if (changePasswordRequest.isInvalidPassword() == true){ %>
					<tr>
						<td colspan="2">
							<ul class="errorlist">
								<li>The password you have enter is invalid. Password must be at least 6 character long, 
								and contain no special character beyond numbers and letters </li>
							</ul>
						</td>
					</tr>
				<% } else if (changePasswordRequest.isWrongOldPassword() == true ) {%>
					<tr>
						<td colspan="2">
							<ul class="errorlist">
								<li>The old password you enter is incorrect, please try again </li>
							</ul>
						</td>
					</tr>
				<% } else if (changePasswordRequest.isNewPasswordNotMatch() == true ) {%>
					<tr>
						<td colspan="2">
							<ul class="errorlist">
								<li>The new password you enter did not match, please try again </li>
							</ul>
						</td>
					</tr>
				<% } %>

					<tr>
						<th><label for="id_oldpassword">Old Password:</label></th>
						<td><input type="password" maxlength="15" name="oldPassword"
							id="id_oldpassword" /></td>
					</tr>
					<tr>
						<th><label for="id_newpassword_1">New Password:</label></th>
						<td><input type="password" maxlength="15" id="id_newpassword_1" name="newPassword1" /></td>
					</tr>
					<tr>
						<th><label for="id_newpassword_2">Re-Enter New Password:</label></th>
						<td><input type="password" maxlength="15" id="id_newpassword_2" name="newPassword2" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<center>
								<input type="submit" class="btn large" value="Change Password" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<jsp:include page="/web/templates/base_body_end.jsp" />
</body>

</html>
