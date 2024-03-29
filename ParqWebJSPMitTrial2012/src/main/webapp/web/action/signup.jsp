<%@ page import="com.parq.web.model.UserRegistration"%>
<%@ page import="com.parq.web.model.WebUser" %>
<jsp:useBean id="userRegistration" class="com.parq.web.model.UserRegistration" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="/web/templates/nav.jsp" />
	<link href="<%=request.getContextPath()%>/web/css/base.css" rel="stylesheet"/>
	<link href="<%=request.getContextPath()%>/web/css/autocomplete.css" type="text/css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/web/css/bootstrap.css" type="text/css" rel="stylesheet" />
</head>

<body>
	<div class="well span8 container">
	  <form action="<%=request.getContextPath()%>/web/action/validateUserRegistration.jsp" method="post">
	    <h2>Sign up</h2>
	    <table>
    		<tbody>
    			<% if (userRegistration.isInvalidPassword() == true){ %>
					<tr>
						<td colspan="2">
							<ul class="errorlist">
								<li>The password you have enter is invalid. Password must be at least 6 character long, 
								and contain no special character beyond numbers and letters. </li>
							</ul>
						</td>
					</tr>
				<% } if (userRegistration.isEmailAlreadyExist() == true ) {%>
					<tr>
						<td colspan="2">
							<ul class="errorlist">
								<li>The Username you enter already exist in the system, please pick a different username. </li>
							</ul>
						</td>
					</tr>
				<% } if (userRegistration.isPasswordDoesNotMatch() == true ) {%>
					<tr>
						<td colspan="2">
							<ul class="errorlist">
								<li>The passwords you entered did not match, please try again. </li>
							</ul>
						</td>
					</tr>
				<% } %>
    		
    			<tr>
    				<th><label for="id_username">Username:</label></th>
    				<td><input type="text" id="id_username" name="email"/></td>
    			</tr>
		    	<tr>
		      		<td colspan="2">
						<center><input type="submit" value="Sign Up" class="btn large"/></center>
		      		</td>
		    	</tr>
		    </tbody>
		 </table>
		</form>
	</div>
	<jsp:include page="/web/templates/footer.jsp" />
</body>

</html>