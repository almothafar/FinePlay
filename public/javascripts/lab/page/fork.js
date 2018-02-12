'use strict';

$('#select-content button').on('click', function(e){

	var buttons = $('#select-content button');
	var target = e.target;
	if(buttons.eq(0)[0]===target){

		buttons.eq(0).removeClass('btn-outline-primary').addClass('btn-primary');
		buttons.eq(1).removeClass('btn-primary').addClass('btn-outline-primary');
		$('#double-content').removeClass('current-right').addClass('current-left');
	}else if(buttons.eq(1)[0]===target){

		buttons.eq(0).removeClass('btn-primary').addClass('btn-outline-primary');
		buttons.eq(1).removeClass('btn-outline-primary').addClass('btn-primary');
		$('#double-content').removeClass('current-left').addClass('current-right');
	}
});
