//GeoTimerModul
var GeoTimerModul = (function() {

	var geoTimer = {};
	// Array ofgoogle.maps.Marker
	var timer;
	// Reset the update timer if necessary, and set a new timer
	// with the changed update Interval
	geoTimer.initTimer = function initCluster(updateInterval) {
		geoTimer.resetTimer();
		timer = window.setInterval(geoTimer.updateLocation, updateInterval);
	}, 
	geoTimer.resetTimer = function resetTimer() {
		
		if (timer) {
			window.clearInterval(timer);
		}
	},
	// If this function is called, the geolocationForm containing the Lat/Lng
	// values,
	// which are provided from the mobi:geolocationTag, are submitted to the
	// server using the
	// command link geolocationForm:gewinnspielsucheFormButton
	geoTimer.updateLocation = function updateLocation() {
		iceSubmitPartial(document.getElementById("geolocationForm"), document
				.getElementById("geolocationForm:locationFormButton"));
	};
	return geoTimer;
}());