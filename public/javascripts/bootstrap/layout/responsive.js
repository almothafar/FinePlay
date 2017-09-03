'use strict';

var draw = function(){

	$("#widthLabel").text($("#responsiveElement").width() + ' px');
};

var timer = false;
$(window).resize(function() {

	if (timer !== false) {

		clearTimeout(timer);
	}
	timer = setTimeout(function() {

		draw();
	}, 100);
});

draw();
