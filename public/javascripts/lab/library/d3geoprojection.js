'use strict';

$(document).ready(function() {

	var update = function(){

		var graphContainer = $('#graphContainer');
		var svgWidth = graphContainer.width();
		var svgHeight = svgWidth;

		var svg = $('svg');
		svg.empty();
		svg.outerWidth(svgWidth);
		svg.outerHeight(svgHeight);

		var path = d3.geoPath()
			.projection(
				d3.geoMercator()
				.translate([svgWidth/2, svgHeight/2])
				.scale(svgWidth / 640 * 100)
			)

		d3.json(Messages("map"), function(error, world) {
			d3.select("#geoGraph")
				.selectAll("path")
				.data(world.features)
				.enter()
				.append("path")
				.attr("d", path)
				.style("fill", function(d, i){
					if (d.properties.name == "Antarctica"){
						return "#fff";
					}
					if (d.properties.name == Messages(MessageKeys.NATURALEARTHDATA_LANG)){
						return "red";
					}
					return "#eee";
				})
		})
	};

	var timer = false;
	$(window).resize(function() {

		if (timer !== false) {

			clearTimeout(timer);
		}
		timer = setTimeout(function() {

			update();
		}, 100);
	});

	update();
});
