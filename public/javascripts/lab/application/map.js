'use strict';

$(document).ready(function() {

	mapboxgl.accessToken = messages("access_token");

	if (mapboxgl.supported()) {

		$('#map1').empty();
		$('#map2').empty();
		$('#map3').empty();

		mapboxgl.setRTLTextPlugin('https://api.mapbox.com/mapbox-gl-js/plugins/mapbox-gl-rtl-text/v0.2.0/mapbox-gl-rtl-text.js');

		//

		var map1 = new mapboxgl.Map({
			container: 'map1',
			style: 'mapbox://styles/mapbox/streets-v10',
			center: [parseFloat(messages(MessageKeys.MAP_LNGLAT_LNG)), parseFloat(messages(MessageKeys.MAP_LNGLAT_LAT))],
			minZoom: 2,
			zoom: 9
		});
		map1.addControl(new MapboxLanguage({
			defaultLanguage: messages("langCode")
		}));
		map1.addControl(new mapboxgl.NavigationControl());
		map1.addControl(new mapboxgl.GeolocateControl({
			positionOptions: {
				enableHighAccuracy: true
			},
			trackUserLocation: true
		}));
		map1.addControl(new MapboxGeocoder({
			accessToken: mapboxgl.accessToken,
			mapboxgl: mapboxgl
		}), 'top-left');

		var popup1 = new mapboxgl.Popup({
				closeOnClick: false
			})
			.setLngLat([parseFloat(messages(MessageKeys.MAP_LNGLAT_LNG)), parseFloat(messages(MessageKeys.MAP_LNGLAT_LAT))])
			.setHTML('<h6>Hello World!</h6>')
			.addTo(map1);

		//

		var map2 = new mapboxgl.Map({
			container: 'map2',
			style: 'mapbox://styles/mapbox/streets-v10',
			center: [parseFloat(messages(MessageKeys.MAP_LNGLAT_LNG)), parseFloat(messages(MessageKeys.MAP_LNGLAT_LAT))],
			minZoom: 2,
			zoom: 9
		});
		map2.addControl(new MapboxLanguage({
			defaultLanguage: messages("langCode")
		}));

		var popup2 = new mapboxgl.Popup({
				closeOnClick: false
			})
			.setLngLat([parseFloat(messages(MessageKeys.MAP_LNGLAT_LNG)), parseFloat(messages(MessageKeys.MAP_LNGLAT_LAT))])
			.setHTML('<h6>Hello World!</h6>')
			.addTo(map2);

		//

		var map3 = new mapboxgl.Map({
			container: 'map3',
			style: 'mapbox://styles/mapbox/streets-v10',
			center: [parseFloat(messages(MessageKeys.MAP_LNGLAT_LNG)), parseFloat(messages(MessageKeys.MAP_LNGLAT_LAT))],
			minZoom: 2,
			zoom: 9
		});
		map3.addControl(new MapboxLanguage({
			defaultLanguage: messages("langCode")
		}));

		var popup3 = new mapboxgl.Popup({
				closeOnClick: false
			})
			.setLngLat([parseFloat(messages(MessageKeys.MAP_LNGLAT_LNG)), parseFloat(messages(MessageKeys.MAP_LNGLAT_LAT))])
			.setHTML('<h6>Hello World!</h6>')
			.addTo(map3);
	}
});
