'use strict';

$(document).ready(function() {

	if(window.matchMedia){

		var darkMedia = window.matchMedia("(prefers-color-scheme: dark)");

		function updateBrightness(matches) {

			notify('Dark :' + matches, 3000);
		}

		if(darkMedia.addListener){

			darkMedia.addListener(function(e) {

				updateBrightness(e.matches)
			});

			updateBrightness(darkMedia.matches);
		}
	}
});
