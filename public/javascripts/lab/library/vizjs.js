'use strict';

$(document).ready(function() {

	$('#previewButton').on('click', function() {

		var engine = $('#engine>option:selected').val();

		var scale = 2;
		var src = $('#dotText').val();

		try {

			var result = Viz(src, {
				format : "svg",
				engine : engine,
				scale : scale
			});

			$('#graph').html(result)
		} catch (e) {

			notifyAlert('warning', e.message);
		}
	});
});
