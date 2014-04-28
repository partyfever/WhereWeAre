//MapMarkerModul
var MapMarkerModul = (function () {
	
	var my = {};
	//Array ofgoogle.maps.Marker
	var markers = [];
	//Displayed MapMarkerCluster 
	var markerClusterer = null;
	
	//Clear markers and marker clusterer
	my.clearCluster = function clearCluster() {
		if (markerClusterer) {
			markerClusterer.clearMarkers();
			markers = [];
		}
	},
	//Add a Marker at Latitude lat, Longitute lng with the color color from the User user
	my.addMarker = function addMarker(lat, lng, color, user,id) {
		
		//Crawl google charts api for on the fly generated marker image
		var pinImage = new google.maps.MarkerImage(
				"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|"
						+ color);
		//Marker Location
		var center = new google.maps.LatLng(lat, lng);
		//Create Marker with default label
		var marker1 = new MarkerWithLabel({
		       position: center,
		       icon:pinImage,
		       map: userMap,
		       labelContent: user,
		       labelAnchor: center,

		       labelClass: "labels", // the CSS class for the label
		       labelStyle: {opacity: 0.75}
		     });
		
		google.maps.event.addDomListener(
                document.getElementById("list-user-"+id),
                "click", 
                function() 
                { 
                      userMap.setCenter(marker1.getPosition() );
                      userMap.setZoom(10);
                      return false;
                }
           );
		
		var infoWindow = new google.maps.InfoWindow({
			content : 'User ' + user
		});
		//Register On click event -->display detail
		google.maps.event.addListener(marker1, 'click', function() {
			infoWindow.open(userMap, marker);
		});
	
		//Add marker to marker array
		markers.push(marker1);
	},
	//Add clusters of the cluster array to the map
	my.initCluster = function initCluster() {
		//userMap references JSF intialized Google map
		markerClusterer = new MarkerClusterer(userMap, markers);
	};

	return my;
}());