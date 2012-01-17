<%@ page import="com.parq.web.model.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />

<div id="header">
  <a href="<%=request.getContextPath()%>/index.jsp">
    <img class="pull-left" src="<%=request.getContextPath()%>/web/images/Parqme_small.png" />
  </a>
  <div class="pull-right span5">
    <img class="pull-left" src="<%=request.getContextPath()%>/web/images/badge_logo_small.png" />
    <ul class="pull-left">
      <% if (user.isAuthenticated() == false) { %> 
      <li>
		<a class="loginButton" href="<%=request.getContextPath()%>/web/action/login.jsp">Log in</a>
      </li>      
      <li>
		<a class="signupButton" href="<%=request.getContextPath()%>/web/action/signup.jsp">Sign up</a>
      </li>
      <% }  else {
    	
    		String userNameDisplay = user.getUsername();
      		if (user.getUsername().length() > 20) {
      			userNameDisplay = user.getUsername().substring(0, 17) + "...";
      	}
      %>
	      <li><%= userNameDisplay %></li>
	      <li>
			<% if (user.isAdminUser() == true){ %>
				<a href="<%=request.getContextPath()%>/web/action/admin/clientAdmin.jsp">Parking administration</a>
			<% } else {%>
				<a href="<%=request.getContextPath()%>/web/action/user/account.jsp">Account settings</a>
			<% } %>
		      </li>      
		      <li>
			<a href="<%=request.getContextPath()%>/web/action/logout.jsp">
			  Log out
			</a>
	      </li>
      <% } %>
    </ul>
  </div>
</div>
<p class="clear" ></p>
<hr class="stripped" />
