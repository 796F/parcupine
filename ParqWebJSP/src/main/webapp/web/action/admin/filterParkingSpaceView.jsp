<%@ page import="com.parq.web.ParkingSpacesFilter" %>
<jsp:useBean id="pSpaceFilter" class="com.parq.web.ParkingSpacesFilter" scope="session" />
<jsp:setProperty name="pSpaceFilter" property="*" />

<html>
	<jsp:forward page="/web/action/admin/clientAdmin.jsp" />
</html>
