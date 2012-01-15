<%@ page import="java.util.List"%>
<%@ page import="com.parq.web.Location"%>

//This code is from the Google Maps API Examples Page
function initialize() {
    var myOptions = {
	zoom: 13,
	mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    var avglat=0;
    var avglong=0;
    var loc;
    <% 
    	if (session.getAttribute("locations") != null) {
    		List<Location> locations = 
    			(List<Location>) session.getAttribute("locations");
    		for (com.parq.web.Location location : locations) {
    %> 	
		    avglat = avglat + <%= location.latitude %>;
		    avglong = avglong + <%= location.longitude %>;
		    loc = new google.maps.LatLng(<%= location.latitude %>,<%= location.longitude %>);
		    new google.maps.Marker({
			    position:loc,
				map:map,
				});
		    avglat = avglat/<%= locations.size() %>;
		    avglong = avglong/<%= locations.size() %>;
    <% } } %>
    // center on dc if no location is found
    if (avglat == 0 && avglong == 0 ) {
    	avglat = 38.892319;
    	avglong = -77.031305;
    }    
    var center = new google.maps.LatLng(avglat,avglong);
    map.setCenter(center); 
}

google.maps.event.addDomListener(window,'load',initialize);