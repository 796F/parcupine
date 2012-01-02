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
    {% for location in locations %}
    avglat = avglat+{{location.geolocation.latitude}};
    avglong = avglong + {{location.geolocation.longitude}};
    loc = new google.maps.LatLng({{location.geolocation.latitude}},{{location.geolocation.longitude}});
    new google.maps.Marker({
	    position:loc,
		map:map,
		});
    {% endfor %}
    avglat = avglat/{{locations.count}};
    avglong = avglong/{{locations.count}};    
    var center = new google.maps.LatLng(avglat,avglong);
    map.setCenter(center); 
}

google.maps.event.addDomListener(window,'load',initialize);