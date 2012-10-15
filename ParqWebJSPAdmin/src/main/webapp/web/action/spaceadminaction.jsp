<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.parq.web.model.WebParkingSpot"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
		String status0 = request.getParameter("space_0");
		String status1 = request.getParameter("space_1");
		String status2 = request.getParameter("space_2");
		String status3 = request.getParameter("space_3");
		String status4 = request.getParameter("space_4");
		String status5 = request.getParameter("space_5");
		
		String freeStatus = "Free";

		List<WebParkingSpot> parkingLocations = service.findParkingLocations();
		List<WebParkingSpot> updatedSpot = new ArrayList<WebParkingSpot>();
		
		for (WebParkingSpot ploc : parkingLocations) {
			if (ploc.getSpotName().contains("1") && !status0.isEmpty()) {
				ploc.setAvailable(status0.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().contains("2") && !status1.isEmpty()) {
				ploc.setAvailable(status1.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().contains("3") && !status2.isEmpty()) {
				ploc.setAvailable(status2.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().contains("4") && !status3.isEmpty()) {
				ploc.setAvailable(status3.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().contains("5") && !status4.isEmpty()) {
				ploc.setAvailable(status4.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().contains("6") && !status5.isEmpty()) {
				ploc.setAvailable(status5.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			}
		}
		
		System.out.println("Spot updated: " + updatedSpot.size());
		service.updateParkingStatus(1L, updatedSpot);
	%>
	<jsp:forward page="/web/spaceadmin.jsp"/>
</html>