<%@ page import="com.parq.web.model.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />

<link href="<%=request.getContextPath()%>/web/css/base.css" rel="stylesheet"/>
<link href="<%=request.getContextPath()%>/web/css/autocomplete.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/web/css/bootstrap.css" type="text/css" rel="stylesheet" />

<div class="topbar">
  <!-- PARQUPINE header image -->
  <a href="<%=request.getContextPath()%>/index.jsp">
    <img class="pull-left" src="<%=request.getContextPath()%>/web/images/logo.png" />
  </a>
  
  <!-- Right hand side nav link -->
  <div class="pull-right span5">
	    <ul class="pull-left">
		  <li>
		  	<a href="<%=request.getContextPath()%>/index.jsp">
		  		<img alt="Account" src="<%=request.getContextPath()%>/web/images/home.png"/>
			</a>
		  </li>
		  <% if(user != null && user.isAuthenticated()) { %>
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/user/account.jsp">
					<img alt="Account" src="<%=request.getContextPath()%>/web/images/account.png"/>
				</a>
			  </li>
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/logout.jsp">
			  		<img alt="Logout" src="<%=request.getContextPath()%>/web/images/logout.png"/>
			  	</a>
			  </li>
		  <% } else { %>
		  	  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/signup.jsp">
					<img alt="Register" src="<%=request.getContextPath()%>/web/images/register.png"/>
				</a>
			  </li>
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/login.jsp">
			  		<img alt="Login" src="<%=request.getContextPath()%>/web/images/signin.png"/>
			  	</a>
			  </li>
		  <% } %>
		</ul>
    </div>
</div>
