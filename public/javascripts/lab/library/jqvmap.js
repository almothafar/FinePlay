'use strict';

function touch_detect() {

	return 'ontouchstart' in window || 'onmsgesturechange' in window || navigator.msMaxTouchPoints > 0;
}

$(document).ready(function() {

	$('#worldmap').vectorMap({

		map: 'world_en',
		backgroundColor: '#333333',
		color: '#ffffff',
		hoverOpacity: 0.7,
		selectedColor: '#666666',
		enableZoom: true,
		showTooltip: true,
		scaleColors: ['#C8EEFF', '#006491'],
		values: sample_data,
		normalizeFunction: 'polynomial',
		onRegionClick: function(element, code, region) {
			if (!touch_detect()) {
				var message = 'You clicked "' + region +
					'" which has the code: ' +
					code.toUpperCase();
				alert(message);
			}
		},
		onRegionOver: function(element, code, region) {
			if (touch_detect()) {
				var message = 'You clicked "' + region +
					'" which has the code: ' +
					code.toUpperCase();
				alert(message);
			}
		}
	});

	document.getElementById('zoomOutButton').onclick = function() {

		$('#worldmap').vectorMap('zoomOut');
	};

	document.getElementById('zoomInButton').onclick = function() {

		$('#worldmap').vectorMap('zoomIn');
	};

	$('#usamap').vectorMap({

		map: 'usa_en',
		enableZoom: true,
		showTooltip: true
	});

	$('#africamap').vectorMap({

		map: 'africa_en',
		enableZoom: true,
		showTooltip: true
	});

	$('#asiamap').vectorMap({

		map: 'asia_en',
		enableZoom: true,
		showTooltip: true
	});

	$('#australiamap').vectorMap({

		map: 'australia_en',
		enableZoom: true,
		showTooltip: true
	});

	$('#europemap').vectorMap({

		map: 'europe_en',
		enableZoom: true,
		showTooltip: true
	});

	$('#north-americamap').vectorMap({

		map: 'north-america_en',
		enableZoom: true,
		showTooltip: true
	});

	$('#south-americamap').vectorMap({

		map: 'south-america_en',
		enableZoom: true,
		showTooltip: true
	});
});
