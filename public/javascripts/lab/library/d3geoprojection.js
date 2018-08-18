'use strict';

$(document).ready(function() {

	var graphContainer = $('#graphContainer');
	var svgWidth = graphContainer.width();
	var svgHeight = svgWidth / 2;

	var svg = $('#geoGraph');
	svg.outerWidth(svgWidth);
	svg.outerHeight(svgHeight);

	var earth = d3.geoNaturalEarth1()
		.translate([svgWidth / 2, svgHeight / 2])
		.scale(svgWidth / 640 * 100)
		.rotate([Messages("lambda"), Messages("phi"), Messages("gamma")])

	var path = d3.geoPath()
		.projection(earth)

	var earthPath
	d3.json(Messages("map")).then(function(world) {
		earthPath = d3.select("#geoGraph")
			.selectAll("path")
			.data(world.features)
			.enter()
			.append("path")
			.attr("d", path)
			.style("fill", function(d, i) {
				if (d.properties.name == "Antarctica") {
					return "#fff";
				}
				if (d.properties.name == Messages(MessageKeys.NATURALEARTHDATA_LANG)) {
					return "red";
				}
				return "#eee";
			})
	})
	.catch(function(error) {

		notifyAlert('warning', error.message);
	});

	var timer = false;
	$(window).resize(function() {

		if (timer !== false) {

			clearTimeout(timer);
		}
		timer = setTimeout(function() {

			svgWidth = graphContainer.width();
			svgHeight = svgWidth / 2;

			svg.outerWidth(svgWidth);
			svg.outerHeight(svgHeight);

			earth.translate([svgWidth / 2, svgHeight / 2])
				.scale(svgWidth / 640 * 100);
			earthPath.attr("d", path);
		}, 100);
	});
});
