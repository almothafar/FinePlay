'use strict';

$(document).ready(function() {

	$('#secureKeyboardButton').popover({
		template: '<div class="races-popover popover" role="tooltip"><div class="arrow"></div><h3 class="popover-header"></h3><div class="popover-body pb-0"></div></div>',
		trigger: 'focus',
		placement: 'left',
		html: true,
		content: $('#secureKeyboardContent')[0]
	});

	$('#secureKeyboardButton').on('click', function(e){

		var secureKeyboardButton = $(e.currentTarget);

		secureKeyboardButton.popover('toggle')
	});

	$('.key').on('click', function(e){

		var key = $(e.currentTarget);
		var keyType = key.data('key');

		var inputTextField = $('#inputTextField');
		switch (keyType){
		case '':

			break;
		case 'delete':

			inputTextField.val(inputTextField.val().slice(0,-1));
			break;
		case 'clear':

			inputTextField.val("");
			break;
		case 'shuffle':
			// for Attack of Position read.

			$('#keys>.keyPosition').sort(function(){return Math.random()-0.5}).appendTo('#keys');
			break;
		default:
			// Number only
			// for Attack of Key logger.

			inputTextField.val(inputTextField.val() + keyType);
			break;
		}
	}).hover(

		function(e) {
			// for Attack of Screen capture.

			$('.key').addClass('text-transparent');
		},
		function(e) {

			$('.key').removeClass('text-transparent');
		}
	);
});
