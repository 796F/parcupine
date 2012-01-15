<%@ page import="com.parq.web.WebUser"%>
<%@ page import="com.parq.web.WebPaymentAccount"%>
<%@ page import="com.parq.web.ParkingHistory" %>
<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>
<%@ page import="com.parq.web.ReportDateRangeFilter" %>

<jsp:useBean id="user" class="com.parq.web.WebUser" scope="session" />
<jsp:useBean id="reportDateRangeFilter" class="com.parq.web.ReportDateRangeFilter" scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="/web/templates/checkIsUser.jsp" />
	<jsp:include page="/web/templates/base_header.jsp" />
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
      	String accOvrActive = "active";
      	String parkHisActive = "";
      	if (reportDateRangeFilter.isJumpToReportSection()) {
      		accOvrActive = "";
          	parkHisActive = "active";	
      	}
      %>
      <li class="<%=accOvrActive%>"><a href="#account-overview">Account Overview</a></li>
      <li class="<%=parkHisActive%>"><a href="#parking-history">Parking History</a></li>
      <li><a href="<%=request.getContextPath()%>/index.jsp">Map</a></li>
    </ul>
  </div>
  <div class="content">
    <div class="pill-content">
      <div class="<%=accOvrActive%>" id="account-overview">
	<h2><%=user.getUsername()%></h2>
	<a class="changePassword" href="<%=request.getContextPath()%>/web/action/changePassword.jsp">change password</a>
	<hr />
	<h3>Payment Methods
	  <%-- Functionality remove for now due to lack of time to implement credit card validation --%> 
	  <%-- <small><a class="addPayment" data-controls-modal="modal" data-backdrop="none">(add payment)</a></small> --%>
	</h3>
	<%
		ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
			List<WebPaymentAccount> accs = service.getPaymentAccountForUser(user);
			
			if (accs != null && !accs.isEmpty())
			{
	%>
	  <table class="zebra-striped">
	    <% for (WebPaymentAccount ac : accs) { %>	    
	    <tr>
	      <td><%= ac.getType() %></td>
	      <td>************<%= ac.getCreditCardNumber().substring(ac.getCreditCardNumber().length() - 4) %></td>
	      <td>
			<ul class="payment-actions">
			  <% if (ac.getDefaultPayment()) { %>
			  	<li>default</li>
			  <% } else { %>
			  	<li></li>
			  <% } %>
			  <%-- Functionality removed for now due to lack of time --%>
			  <%-- <li><a href="<%=request.getContextPath()%>/web/action/deletePaymentMethod.jsp">delete</a></li>  --%>
			</ul>
	      </td>
	    </tr>
	  	<% } %>
	  </table>	  
	  <% } else { %>
	  <h6>No payment methods found</h6>
	  <% } %>
	  <p style="clear:both"></p>
      </div>
      

      <div class="<%=parkHisActive%>" id="parking-history">
		<h2>Parking History</h2>	
		<form action="<%=request.getContextPath()%>/web/action/filterReportDateRange.jsp" method="post">	 
		  <%
		  		reportDateRangeFilter.setCallerPage(ReportDateRangeFilter.CallerPage.USER);
		  %>
		  <table>
			  <tbody>
			  	<tr>
				    <td><label style="white-space:nowrap;" for="dateRangeFilter">Show Parking History for:</label></td>
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
		    <th>Date</th>
		    <th>Location</th>
		    <th>Start time</th>
		    <th>End time</th>
		    <th>Cost</th>
		    <th>Parking Ref #</th>
		    <th>
		  </thead>
		<%
			List<ParkingHistory> pHis = service.getParkingHistoryForUser(user, reportDateRangeFilter);
			
			if (pHis == null || pHis.isEmpty())
			{
		%>
		  <tr>
		    <td colspan="6">
		      <h6>No history on record</h6>
		    </td>
		  </tr>
		  <% } else { %>
			  <% for (ParkingHistory his : pHis) {%>
			  <tr>
			    <td><%= his.getDate() %></td>
			    <td><%= his.getLocationName() %></td>
			    <td><%= his.getStartTime() %></td>
			    <td><%= his.getEndTime() %></td>
			    <td>$<%= his.getCost() %></td>
			    <td><%= his.getParkingRefNumber() %></td>
			  </tr>
			  <% } %>
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
