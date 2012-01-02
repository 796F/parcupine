//This code is from the Google Maps API Examples Page
var loc = new google.maps.LatLng({{space.location.geolocation.latitude}}, {{space.location.geolocation.longitude}});

function initialize() {
    var myOptions = {
	zoom: 18,
	mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    var marker = new google.maps.Marker({
	    position: loc, 
	    map: map
	});   
    map.setCenter(loc);
 
}
google.maps.event.addDomListener(window,'load',initialize);