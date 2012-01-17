<%@ page import="com.parq.web.WebUser"%>
<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />

<div class="topbar">
  <div class="fill">
    <div class="container">
      <%-- {% if user.parq_user and user.parq_user.is_parked%}
	<a class="alert-message renew error" href="/user/renew/">You are currently parked. Renew your spot now</a>
      {% endif %}
      --%>
      <div class="pull-right">
	<ul>
	  <li><a href="<%=request.getContextPath()%>/index.jsp">Home</a></li>
	  <li><a href="<%=request.getContextPath()%>/web/templates/features.jsp">How it Works</a></li>
	  <li><a href="<%=request.getContextPath()%>/web/templates/about.jsp">About PARQ</a></li>
	  <% if(user.isAuthenticated()) { %>
		  <li>
		  <% if (user.isAdminUser()){ %>
		  		<a href="<%=request.getContextPath()%>/web/action/admin/clientAdmin.jsp">Parking Administration</a>
		  <% } else { %>
		  	<a href="<%=request.getContextPath()%>/web/action/user/account.jsp">Account Settings</a>
		  <% } %>
		  </li>	    
	  <% } %>
	</ul>
      </div>
    </div><!-- /container -->
    <div id="message">
    </div>
  </div> <!-- /fill -->
</div><!-- /topbar -->
