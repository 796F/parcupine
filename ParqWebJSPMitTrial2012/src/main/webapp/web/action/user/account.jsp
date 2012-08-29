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
	<script type="text/javascript" src="<%=request.getContextPath()%>/web/js/bootstrap-tabs.js"></script>
	<script type="text/javascript">
	   $(document).ready(function() {
	      $('.tabs').tabs();
	   });
	</script>
</head>


<body>


<div class="container-fluid">
  <div class="content">
    <div class="pill-content">

      <div class="active" id="parking-history">
		<h2>Welcome <%=user.getUsername()%></h2>
		<% 
			ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
			List<ReportingHistory> pointHis = service.getReportingHistoryForUser(user); 
		%>
		<h2>Your Current Point Balance Is: <%= pointHis.get(0).getPoints() %> </h2>	
		
		<table>
			<tr>
				<td style="width:49%">
					<!-- The Parking History Table -->
					<table class="zebra-striped">
					  <thead>
					    <th>Date</th>
					    <th>Location</th>
					    <th>Start time</th>
					    <th>End time</th>
					  </thead>
					<%
						List<ParkingHistory> pHis = service.getParkingHistoryForUser(user);
						
						if (pHis == null || pHis.isEmpty()) { %>
						  <tr>
						    <td colspan="4">
						      <h6>No history on record</h6>
						    </td>
						  </tr>
					  <% } else { 
						  for (ParkingHistory his : pHis) {%>
							  <tr>
							    <td><%= his.getDate() %></td>
							    <td><%= his.getLocationName() %></td>
							    <td><%= his.getStartTime() %></td>
							    <td><%= his.getEndTime() %></td>
							  </tr>
						  <% } %>
					  <% } %>
					</table>
				</td>
			<!-- Small Table Spacer  -->
				<td style="width:2%"/td>
				<td style="width:49%">
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
						      <h6>No history on record</h6>
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
				</td>
			</tr>
		</table>
      </div>
    </div>
  </div>
</div>

</body>

</html>
