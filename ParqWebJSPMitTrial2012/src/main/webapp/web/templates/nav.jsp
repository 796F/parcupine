<%@ page import="com.parq.web.model.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />

<link href="<%=request.getContextPath()%>/web/css/nav.css" rel="stylesheet"/>

<div class="topbar span19">
  <!-- PARQUPINE header image -->
  <a href="<%=request.getContextPath()%>/index.jsp">
    <img class="pull-left" src="<%=request.getContextPath()%>/web/images/logo.png" />
  </a>
  <!-- Right hand side nav link -->
  <div class="pull-right span14">
	    <ul class="pull-left">
		  <li>
		  	<a href="<%=request.getContextPath()%>/index.jsp">Home</a>
		  </li>
		  <!--
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/map.jsp">Parking-Map</a>
			  </li>
		  <% if(user != null && user.isAuthenticated()) { %>
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/user/account.jsp">Account</a>
			  </li>
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/logout.jsp">Logout</a>
			  </li>
		  <% } else { %>
		  	  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/signup.jsp">Register</a>
			  </li>
			  <li>
			  	<a href="<%=request.getContextPath()%>/web/action/login.jsp">Login</a>
			  </li>
		  <% } %>
		-->
		</ul>
    	<!-- Lab logo -->
    	<div class="pull-right">
    		<a href="http://senseable.mit.edu/" target="_blank">
				<img alt="Login" src="<%=request.getContextPath()%>/web/images/lab_logo.png" />
			</a>
		</div>
		<!-- Bottom line image -->
		<img class="span14" style="height: 2px;" src="<%=request.getContextPath()%>/web/images/line.png">
	</div>
</div>
