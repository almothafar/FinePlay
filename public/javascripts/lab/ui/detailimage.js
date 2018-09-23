'use strict';

$(document).ready(function() {

	//

	var imageScalable = $('#imageScalable')[0];

	var lastScale = 1;
	var handleScale = function(scale) {

		$(imageScalable).css('transform', 'scale(' + scale + ')');
	}

	$('#minusButton').on('click', function() {

		if(1 < lastScale){lastScale--};
		handleScale(lastScale);
	});

	$('#plusButton').on('click', function() {

		if(lastScale < 10){lastScale++};
		handleScale(lastScale);
	});

	//

	var imageDraggable = $('#imageDraggable')[0];
	var image = $('#image')[0];

	var mc = new Hammer(imageDraggable);

	mc.add(new Hammer.Pan({
		direction : Hammer.DIRECTION_ALL,
		threshold : 0
	}));

	mc.on("pan", handleDrag);

	var lastX = image.offsetLeft;
	var lastY = image.offsetTop;
	function handleDrag(e) {

		var x = lastX + (e.deltaX / lastScale);
		var y = lastY + (e.deltaY / lastScale);
//		var x = lastX + e.deltaX;
//		var y = lastY + e.deltaY;

		$(image).css('transform', 'translate(' + x + 'px, ' + y + 'px)');

		if (e.isFinal) {

			lastX = x;
			lastY = y;
		}
	}
});
