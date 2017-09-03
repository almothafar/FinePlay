'use strict';

$("#taskmenuCloseButton").click(function(event) {

	$("#taskbase").removeClass('expand');
	$("#taskbase>.modal-backdrop").remove();
});

$("#taskmenuButton").click(function(event) {

	$("#taskbase").addClass('expand');
	$("#taskbase").append('<div class="modal-backdrop fade show"></div>');
});
