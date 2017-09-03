'use strict';

var view = new ol.View({
	center: [0, 0],
	zoom: 2
});

var map = new ol.Map({
	layers: [
		new ol.layer.Tile({
			source: new ol.source.OSM()
		})
	],
	target: 'map',
	controls: ol.control.defaults({
		attributionOptions: /** @@type {olx.control.AttributionOptions} */ ({
			collapsible: false
		})
	}),
	view: view
});

document.getElementById('zoomOutButton').onclick = function() {

	var view = map.getView();
	var zoom = view.getZoom();
	view.setZoom(zoom - 1);
};

document.getElementById('zoomInButton').onclick = function() {

	var view = map.getView();
	var zoom = view.getZoom();
	view.setZoom(zoom + 1);
};

var geolocation = new ol.Geolocation({

	projection: view.getProjection()
});

var accuracyFeature = new ol.Feature();
geolocation.on('change:accuracyGeometry', function() {

	accuracyFeature.setGeometry(geolocation.getAccuracyGeometry());
});

var positionFeature = new ol.Feature();
positionFeature.setStyle(new ol.style.Style({
	image: new ol.style.Circle({
		radius: 6,
		fill: new ol.style.Fill({
			color: '#3399CC'
		}),
		stroke: new ol.style.Stroke({
			color: '#fff',
			width: 2
		})
	})
}));

geolocation.on('change:position', function() {

	var coordinates = geolocation.getPosition();

	positionFeature.setGeometry(coordinates ? new ol.geom.Point(coordinates) : null);

	if(coordinates){

		view.setCenter(coordinates);
	}
});

new ol.layer.Vector({
	map: map,
	source: new ol.source.Vector({
		features: [accuracyFeature, positionFeature]
	})
});

$("#geolocationButton").on('click', function(e) {

	var isStartTrack = $("#geolocationButton").attr('aria-pressed') == 'false';
	geolocation.setTracking(isStartTrack);
});

var deviceOrientation = new ol.DeviceOrientation();

deviceOrientation.on(['change:heading'], function(event) {

	var rotation = event.target.getHeading() || 0;

	view.setRotation(rotation);
});

$("#orientationButton").on('click', function(e) {

	var isStartTrack = $("#orientationButton").attr('aria-pressed') == 'false';
	deviceOrientation.setTracking(isStartTrack);
});
