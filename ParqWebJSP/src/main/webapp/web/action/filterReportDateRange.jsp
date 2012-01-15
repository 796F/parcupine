<%@ page import="com.parq.web.ReportDateRangeFilter" %>
<jsp:useBean id="reportDateRangeFilter" class="com.parq.web.ReportDateRangeFilter" scope="session" />
<jsp:setProperty name="reportDateRangeFilter" property="*" />

<% 
	reportDateRangeFilter.setJumpToReportSection(true);
	if (reportDateRangeFilter.getCallerPage().equals(ReportDateRangeFilter.CallerPage.ADMIN)) {%>
	<html>
		<jsp:forward page="/web/action/clientAdmin.jsp" />
	</html>
<% } else { %>
	<html>
		<jsp:forward page="/web/action/account.jsp" />
	</html>
 <% } %>
 