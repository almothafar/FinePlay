'use strict';

$(document).ready(function() {

	var viz = new Viz();

	$('#previewButton').on('click', function() {

		var engine = $('#engine>option:selected').val();
		var src = $('#dotText').val();

		viz.renderSVGElement(src, {
			engine : engine
		})
		.then(function(element) {

			$('#graph').html(element)
			$('#graph>svg').addClass("w-100 h-100");
		})
		.catch(error => {

			viz = new Viz();

			notifyAlert('warning', error.message);
		});
	});
});
