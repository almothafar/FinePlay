'use strict';

$(document).ready(function () {

	$('#window .modal-body').on('scroll', function(e){

		var diff = ($('#window .modal-body>div').height() - parseInt($('#window .modal-body').css('padding-top'), 10)) - $('#window .modal-body').height();
		var pos = $('#window .modal-body>div').position().top;
		var isEnd = 0 == (diff + pos);

		if(isEnd){

			$('#okButton').prop('disabled', false).addClass('default')
		}
	});
});
