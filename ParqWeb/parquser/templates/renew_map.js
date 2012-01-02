//This code is from the Google Maps API Examples Page

function initialize() {
    var map,loc,myOptions,marker;

    {% for instance in user.parq_user.current_parking_spaces %}
    myOptions = {
	zoom: 14,
	mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas{{instance.parkinginst_id}}"), myOptions);
    loc = new google.maps.LatLng({{instance.space.location.geolocation.latitude}},{{instance.space.location.geolocation.longitude}});
    marker = new google.maps.Marker({
	    position: loc, 
	    map: map
	});   
    map.setCenter(loc);
    {% endfor %}
 
}
google.maps.event.addDomListener(window,'load',initialize);