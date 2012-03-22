<%@ page import="com.parq.web.model.WebUser"%>
<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>
<%@ page import="com.parq.web.ParkingSpacesFilter" %>
<%@ page import="com.parq.web.model.ParkingSpaceStatus" %>
<%@ page import="com.parq.web.model.ParkingReport" %>
<%@ page import="com.parq.web.ReportDateRangeFilter" %>
<%@ page import="com.parq.web.model.WebClient" %>

<jsp:useBean id="user" class="com.parq.web.model.WebUser" scope="session" />
<jsp:useBean id="client" class="com.parq.web.model.WebClient" scope="session" />
<jsp:useBean id="pSpaceFilter" class="com.parq.web.ParkingSpacesFilter" scope="session" />
<jsp:useBean id="reportDateRangeFilter" class="com.parq.web.ReportDateRangeFilter" scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="/web/templates/checkIsAdmin.jsp" />
	<jsp:include page="/web/templates/base_header.jsp" />
	
	<%-- Not sure what the below line does yet --%>
	<%--{% load time_left %} --%>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/web/js/bootstrap-tabs.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		   $('.tabs').tabs();
		});
	</script>
</head>


<body>
	<jsp:include page="/web/templates/base_body_start.jsp" />


<div class="container-fluid">
  <div class="sidebar">
    <ul class="tabs">
      <% 
      	String parkingSpace = "active";
      	String parkRepActive = "";
      	if (reportDateRangeFilter.isJumpToReportSection()) {
      		parkingSpace = "";
      		parkRepActive = "active";	
      	}
      %>
      <li class="<%=parkingSpace%>"><a href="#parking-spaces">My Parking Spaces</a></li>
      <li class="<%=parkRepActive%>"><a href="#parking-reports">Parking Reports</a></li>
    </ul>
  </div>
  <div class="content">
    <div class="pill-content">
      <div class="<%=parkingSpace%>" id="parking-spaces">
		<h2>My Parking Spaces</h2>
	
		<form action="<%=request.getContextPath()%>/web/action/admin/filterParkingSpaceView.jsp" method="post">	 
		  <table>
			  <tbody>
			  	<tr>
				    <td><label style="white-space:nowrap;" for="parkingSpaceFilter">Filter Parking Status By:</label></td>
				    <td>
				    	<%
				    		boolean allSelect = pSpaceFilter.getParkingSpaceFilter().equals(ParkingSpacesFilter.Status.ALL.toString());
				    		boolean freeSelected = pSpaceFilter.getParkingSpaceFilter().equals(ParkingSpacesFilter.Status.FREE.toString());
				    		boolean occupiedSelect = pSpaceFilter.getParkingSpaceFilter().equals(ParkingSpacesFilter.Status.OCCUPIED.toString());
				    	%>
				    	<select name="parkingSpaceFilter">
					      <option value="<%=ParkingSpacesFilter.Status.ALL.toString()%>" 
					      		<%if (allSelect) {%> selected <%}%>>
					      		All Parking Spaces
					      </option>
					      <option value="<%=ParkingSpacesFilter.Status.FREE.toString()%>"
					      		<%if (freeSelected) {%> selected <%}%>>
					      		Not Occupied Parking Spaces
					      </option>
					      <option value="<%=ParkingSpacesFilter.Status.OCCUPIED.toString()%>"
					      		<%if (occupiedSelect) {%> selected <%}%>>
					      		Occupied Parking Spaces
					      </option>
				    	</select>
				    </td>
				    <td/>
				    <td><label style="white-space:nowrap;" for="locationFilter">Show Location:</label></td>
				    <td>
				    	<%
							ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
							List<String> locationIdentifiers = service
								.getParkingLocationIdentifierListForClient(client.getClientId());
						%>
				    	<select name="locationFilter">
				    		<option value="<%="NULL"%>" 
					      		<%if (null == pSpaceFilter.getLocationFilter() 
					      				|| "NULL".equalsIgnoreCase(pSpaceFilter.getLocationFilter())) {%> selected <%}%>>
					      		All Parking Locations
					      	</option>
				    		<%
				    			// show the options to filter by locations
				    			for (String locId : locationIdentifiers) {
				    				%>
				    				<option value="<%=locId%>"
					      				<%if (locId.equalsIgnoreCase(pSpaceFilter.getLocationFilter())) {%> selected <%}%>>
					      				<%=locId%>
					      			</option>
				    				<%
				    			}
				    		%>
				    	</select>
				    </td>
				    <td/>
				  	<td><input type="submit" class="btn large" value="update" /></td>
				</tr>
			  </tbody>
		  </table>
		</form>
		
		<%
			String locationIdentifier = pSpaceFilter.getLocationFilter();
			if ("NULL".equalsIgnoreCase(locationIdentifier)) {
				locationIdentifier = null;
			}
			
			List<ParkingSpaceStatus> status = service
				.getParkingStatusByClient(client.getClientId(), locationIdentifier);
		%>
		<table class="zebra-striped">
		  <thead>
		    <th>Space Identifier</th>
		    <th>Location</th>
		    <th>Space Name</th>
		    <th>Parking Level</th>
		    <th>Occupied?</th>
		    <th>Until</th>
		  </thead>
		  <% if (status == null || status.isEmpty()) { %>
		  <tr>
		    <td colspan="6">
		      <h6>No parking spaces on record</h6>
		    </td>
		  </tr>
		  <% } else {
			  	for (ParkingSpaceStatus spaceStatus : status) { 
			  		if (spaceStatus.isOccupied() && 
			  				pSpaceFilter.getParkingSpaceFilter().equals(ParkingSpacesFilter.Status.FREE.toString()))  {
			  			// don't show this row
			  		} else if (!spaceStatus.isOccupied() && 
			  				pSpaceFilter.getParkingSpaceFilter().equals(ParkingSpacesFilter.Status.OCCUPIED.toString())) {
			  			// don't show this row
			  		} else {%>
				  <tr>
				    <td><%=spaceStatus.getSpaceIdentifier() %></td>
				    <td><%=spaceStatus.getSpaceLocation() %></td>
				    <td><%=spaceStatus.getSpaceName() %></td>
				    <td><%=spaceStatus.getParkingLevel() %></td>
				    <% if(spaceStatus.isOccupied()) {%>
				    	<td style="color: red; font-weight: bold">In Use</td>
				    	<td><%=spaceStatus.getSpaceFreeOn() %></td>
				    <% } else { %>
				    	<td style="color: green; font-weight: bold">Available</td>
				    	<td> --- </td>
				    <% } %>
				  </tr>
				  <% } %>
			  <% } %>
		  <% } %>
		</table>
      </div>

      <div class="<%=parkRepActive%>" id="parking-reports">
		<h3>Parking Reports</h3>
		<form action="<%=request.getContextPath()%>/web/action/filterReportDateRange.jsp" method="post">	 
		  <%
		  		reportDateRangeFilter.setCallerPage(ReportDateRangeFilter.CallerPage.ADMIN);
		  %>
		  <table>
			  <tbody>
			  	<tr>
				    <td><label style="white-space:nowrap;" for="dateRangeFilter">Show Parking Report for:</label></td>
				    <td>
				    	<%
				    		boolean noneSelected = reportDateRangeFilter.getDateRangeFilter().equals(ReportDateRangeFilter.DateRange.NONE.toString());
				    		boolean thisMonthSelected = reportDateRangeFilter.getDateRangeFilter().equals(ReportDateRangeFilter.DateRange.THIS_MONTH.toString());
				    		boolean lastMonthSelect = reportDateRangeFilter.getDateRangeFilter().equals(ReportDateRangeFilter.DateRange.LAST_MONTH.toString());
				    		boolean lastThreeMonthSelect = reportDateRangeFilter.getDateRangeFilter().equals(ReportDateRangeFilter.DateRange.LAST_THREE_MONTH.toString());
				    	%>
				    	<select name="dateRangeFilter">
					      <option value="<%=ReportDateRangeFilter.DateRange.NONE.toString()%>" 
					      		<%if (noneSelected) {%> selected <%}%>>
					      		None
					      </option>
					      <option value="<%=ReportDateRangeFilter.DateRange.THIS_MONTH.toString()%>"
					      		<%if (thisMonthSelected) {%> selected <%}%>>
					      		This Month
					      </option>
					      <option value="<%=ReportDateRangeFilter.DateRange.LAST_MONTH.toString()%>"
					      		<%if (lastMonthSelect) {%> selected <%}%>>
					      		Last Month
					      </option>
					      <option value="<%=ReportDateRangeFilter.DateRange.LAST_THREE_MONTH.toString()%>"
					      		<%if (lastThreeMonthSelect) {%> selected <%}%>>
					      		Last Three Month
					      </option>
				    	</select>
				    </td>
				  	<td><input type="submit" class="btn large" value="update" /></td>
				</tr>
			  </tbody>
		  </table>
		</form>
		
		<table class="zebra-striped">
		  <thead>
		    <th>Parking Ref #</th>
		    <th>Location</th>
		    <th>Space #</th>
		    <th>Occupant</th>
		    <th>Date</th>
		    <th>From</th>
		    <th>To</th>
		    <%-- <th>Amount Paid</th> --%>
		  </thead>
		  <%
		  	List<ParkingReport> reports = service.getParkingReportByClientId(
		  			client.getClientId(), reportDateRangeFilter);
		  	if (reports != null && !reports.isEmpty()) { 
		  		for (ParkingReport report :  reports) { %>
				<tr>
				  <td><%= report.getParkingRefNum() %></td>
				  <td><%= report.getLocationIdentifier() %></td>
				  <td><%= report.getSpaceIdentifier() %></td>
				  <td><%= report.getUserEmail() %></td>
				  <td><%= report.getPaymentDatetime() %></td>
				  <%-- <td>$<%= report.getAmountPaid() %></td> --%>
				  <td><%= report.getParkingStartTime() %></td>
				  <td><%= report.getParkingEndTime() %></td>
				</tr>	
		  	 	<% } %>
		  	<% } else {%>
			  <tr>
			    <td colspan="7">
			      <h6>No parking reports on record</h6>
			    </td>
			  </tr>
		  	<% } %>
		</table>
      </div>
    </div>
  </div>
</div>
<% reportDateRangeFilter.setJumpToReportSection(false); %>

	<jsp:include page="/web/templates/base_body_end.jsp" />
</body>

</html>
