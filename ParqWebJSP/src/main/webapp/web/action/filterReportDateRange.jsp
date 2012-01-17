<%@ page import="com.parq.web.ReportDateRangeFilter" %>
<jsp:useBean id="reportDateRangeFilter" class="com.parq.web.ReportDateRangeFilter" scope="session" />
<jsp:setProperty name="reportDateRangeFilter" property="*" />

<html>
	<% 
		reportDateRangeFilter.setJumpToReportSection(true);
		if (reportDateRangeFilter.getCallerPage().equals(ReportDateRangeFilter.CallerPage.ADMIN)) {%>
			<jsp:forward page="/web/action/admin/clientAdmin.jsp" />
	<% } else { %>
			<jsp:forward page="/web/action/user/account.jsp" />
	<% } %>
</html> 