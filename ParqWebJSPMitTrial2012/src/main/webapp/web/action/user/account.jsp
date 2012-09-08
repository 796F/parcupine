<%@ page import="com.parq.web.model.WebUser"%>
<%@ page import="com.parq.web.model.ParkingHistory" %>
<%@ page import="com.parq.web.model.ReportingHistory" %>
<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>

<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="/web/templates/nav.jsp" />
	<link href="<%=request.getContextPath()%>/web/css/base.css" rel="stylesheet"/>
	<link href="<%=request.getContextPath()%>/web/css/autocomplete.css" type="text/css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/web/css/bootstrap.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/web/js/bootstrap-tabs.js"></script>
	<script type="text/javascript">
	   $(document).ready(function() {
	      $('.tabs').tabs();
	   });
	</script>
</head>

<% 
	ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
	List<ReportingHistory> pointHis = service.getReportingHistoryForUser(user); 
%>
		
<body>
<div class="container-fluid">
  <div class="content">
    <div class="pill-content">
      <div class="active" id="parking-history">
      
		<h2 class="span14" style="text-align: center; margin-top: 50px;">
			<font style="color: #1D5169;">Welcome, <%=user.getUsername()%> <br/>
			Your current point balance is:</font> <font style="color: #5EB3D2;"><%= pointHis.get(0).getPoints() %></font> 
		</h2>	
		
	
		<div class="span14" style="margin-top:75px">
				<div class="pull-left span7">
					<!-- The Parking History Table -->
					<table class="zebra-striped">
					  <thead>
					    <th>Location</th>
					    <th>Start time</th>
					    <th>End time</th>
					  </thead>
					<%
						List<ParkingHistory> pHis = service.getParkingHistoryForUser(user);
						
						if (pHis == null || pHis.isEmpty()) { %>
						  <tr>
						    <td colspan="4">
						      <h6><small>No history on record</small></h6>
						    </td>
						  </tr>
					  <% } else { 
						  for (ParkingHistory his : pHis) {%>
							  <tr>
							    <td><%= his.getLocationName() %></td>
							    <td><%= his.getStartTime() %></td>
							    <td><%= his.getEndTime() %></td>
							  </tr>
						  <% } %>
					  <% } %>
					</table>
				</div>
			<!-- Small Table Spacer  -->
				<div class="pull-right span7">
					<!-- Point history table -->
					<table class="zebra-striped">
					  <thead>
					    <th>Date</th>
					    <th>Points</th>
					  </thead>
					<%
						if (pointHis == null || pointHis.size() < 2) { %>
						  <tr>
						    <td colspan="3">
						      <h6><small>No history on record</small></h6>
						    </td>
						  </tr>
					  <% } else { 
						  for (int i = 0; i < pointHis.size() - 1; i++) {
							  ReportingHistory curReport = pointHis.get(i);
							  ReportingHistory preReport = pointHis.get(i + 1);%>							  
						  <tr>
						    <td><%= curReport.getDate().toString() %></td>
						    <td><%= curReport.getPoints() - preReport.getPoints() %></td>
						  </tr>
						<% } %>
					  <% } %>
					</table>
				</div>
			</div>
		</div>
		
      </div>
    </div>
  </div>
</div>
<jsp:include page="/web/templates/footer.jsp" />
</body>

</html>
