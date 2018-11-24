'use strict';

$(window).on('load', function () {

	$("#twentytwentyContainer").twentytwenty({
		default_offset_pct: 0.95,
		before_label: messages(MessageKeys.BEFORE),
		after_label: messages(MessageKeys.AFTER)
	});
});
