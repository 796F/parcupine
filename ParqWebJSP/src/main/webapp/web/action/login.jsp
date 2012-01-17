<%@ page import="com.parq.web.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="/web/templates/base_header.jsp" />
</head>

<body>
	<jsp:include page="/web/templates/base_body_start.jsp" />

	<div class="well span8 container modal-body">
		<FORM METHOD=POST
			ACTION="<%=request.getContextPath()%>/web/action/validateLogin.jsp">
			<h2>Login</h2>
			<table>
				<tbody xmlns="http://www.w3.org/1999/xhtml">

				<% if (user.getLoginFailed() == true){ %>
					<tr>
						<td colspan="2"><ul class="errorlist">
								<li>Please enter a correct username and password. Note that
									both fields are case-sensitive.</li>
							</ul></td>
					</tr>
				<% } %>

					<tr>
						<th><label for="id_username">Email:</label></th>
						<td><input type="text" maxlength="30" name="username"
							id="id_username" /></td>
					</tr>
					<tr>
						<th><label for="id_password">Password:</label></th>
						<td><input type="password" id="id_password" name="password" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<center>
								<input type="submit" class="btn large" value="Login" />
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
