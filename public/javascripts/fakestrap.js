'use strict';

$(document).ready(function() {

	$(".unlock-handle").draggable({
		axis: "x",
		containment: ".unlock",
		revert: "invalid"
	});

	$(".unlock-end" ).droppable({
		accept : ".unlock-handle" ,
		drop : function(event , ui){

			var unlockSliderEnd = $(this);

			var unlockSliderHandle = $(ui.draggable);
			unlockSliderHandle.detach();

			unlockSliderHandle.css('left', 0);
			unlockSliderHandle.appendTo(unlockSliderEnd);

			unlockSliderHandle.closest(".unlock").trigger("slid.fs.unlock");
		}
	});
});
