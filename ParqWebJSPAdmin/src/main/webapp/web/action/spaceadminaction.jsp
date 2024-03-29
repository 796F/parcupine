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
			if (ploc.getSpotName().equals("101") && !status0.isEmpty()) {
				ploc.setAvailable(status0.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().equals("102") && !status1.isEmpty()) {
				ploc.setAvailable(status1.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().equals("103") && !status2.isEmpty()) {
				ploc.setAvailable(status2.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().equals("104") && !status3.isEmpty()) {
				ploc.setAvailable(status3.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().equals("105") && !status4.isEmpty()) {
				ploc.setAvailable(status4.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			} else if (ploc.getSpotName().equals("106") && !status5.isEmpty()) {
				ploc.setAvailable(status5.equalsIgnoreCase(freeStatus));
				updatedSpot.add(ploc);
			}
		}
		
		System.out.println("Spot updated: " + updatedSpot.size());
		service.updateParkingStatus(1L, updatedSpot);
	%>
	
	<head>
		<title>Parqupine</title>
		<meta http-equiv="REFRESH" content="1;url=http://parqme.com/ParqAdmin/">
	</head>
	<BODY></BODY>
</html>