<%@ page import="com.parq.web.MapLocation"%>
<%@ page import="com.parq.web.WebParkingLocation"%>
<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>
<%@ page import="com.parq.web.service.GoogleMapService"%>

<jsp:useBean id="mapLocation" class="com.parq.web.MapLocation" scope="session" />
<jsp:setProperty name="mapLocation" property="*" />
<%
	// search the map service for the lat/long of the location
	MapLocation loc = 
		GoogleMapService.getMapLocationForLocationName(mapLocation.getLocationName());

	// use the parqwebservice to find all the parking location near this lat/long
	ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
	List<WebParkingLocation> parkingLocations = service.findParkingLocations(loc);
	loc.setParkingLocations(parkingLocations);
	
	// update the mapLocation object in cache
	session.setAttribute("mapLocation", loc);
%>
<html>
	<jsp:forward page="/web/main.jsp"/>
</html>