<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.model.MapLocation"%>
<%@ page import="com.parq.web.model.WebParkingLocation"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>


<jsp:useBean id="mapLocation" class="com.parq.web.model.MapLocation" scope="session" >
    <%-- if the bean is has not been initialize, set the parkingLocation property of the bean --%>
    <%
		ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
		List<WebParkingLocation> parkingLocations = service.findParkingLocations(new MapLocation());
    %>
    <jsp:setProperty name="mapLocation" property="parkingLocations" value="<%=parkingLocations%>" />
</jsp:useBean>

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
	function initialize() {
	  var mapOptions = {
	    zoom: 15,
	    center: new google.maps.LatLng(42.358417, -71.094853),
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	  };
	  var map = new google.maps.Map(document.getElementById('map_canvas'),
	                                mapOptions);
	  var availableSpaceImage = '<%=request.getContextPath()%>/web/images/spaceAvailable.png';
	  var takenSpaceImage = '<%=request.getContextPath()%>/web/images/spaceTaken.png';                              
	  var image;
	  var loc;
	  
		<%  List<WebParkingLocation> pLocations =  mapLocation.getParkingLocations();
			for (WebParkingLocation pLocation : pLocations) {
				if (pLocation.isAvailable()) { %> 	
					var image = availableSpaceImage;
		<% 		} else { %>
					var image = takenSpaceImage;
		<% 		} %>
				loc = new google.maps.LatLng(<%= pLocation.getLatitude() %>, <%= pLocation.getLongitude() %>);
				  new google.maps.Marker({
					position:loc,
					map:map,
					icon: image
				  });
		<%  }  %>
	}
	google.maps.event.addDomListener(window,'load',initialize);
</script>
