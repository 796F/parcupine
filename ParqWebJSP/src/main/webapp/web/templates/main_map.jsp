<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.MapLocation"%>
<%@ page import="com.parq.web.WebParkingLocation"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>


<jsp:useBean id="mapLocation" class="com.parq.web.MapLocation" scope="session" >
    <%-- if the bean is has not been initialize, set the parkingLocation property of the bean --%>
    <%
		ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
		List<WebParkingLocation> parkingLocations = service.findParkingLocations(new MapLocation());
    %>
    <jsp:setProperty name="mapLocation" property="parkingLocations" value="<%=parkingLocations%>" />
</jsp:useBean>


//This code is from the Google Maps API Examples Page
function initialize() {
    var myOptions = {
	zoom: 13,
	mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    var loc;
    <%   	
    	List<WebParkingLocation> pLocations =  mapLocation.getParkingLocations();
    		for (WebParkingLocation pLocation : pLocations) {
	%> 	
		    loc = new google.maps.LatLng(<%= pLocation.getLatitude() %>, <%= pLocation.getLongitude() %>);
		    new google.maps.Marker({
			    position:loc,
				map:map,
				});
				
    <%  }  %>
    var center = new google.maps.LatLng(<%= mapLocation.getLatitude()%>, <%= mapLocation.getLongitude()%>);
    map.setCenter(center); 
}

google.maps.event.addDomListener(window,'load',initialize);