'use strict';

$('#alert').on('click', function(e){

	bootbox.alert("Hello world!", function() {
		notifyAlert('info', 'Closed.');
	});
});

$('#confirm').on('click', function(e){

	bootbox.confirm("Are you sure?", function(result) {
		notifyAlert('info', 'Closed. :'+result);
	});
});

$('#prompt').on('click', function(e){

	bootbox.prompt("What is your name?", function(result) {

		if (result === null) {
			notifyAlert('info', 'Canceled.');
		} else {
			notifyAlert('info', 'OK :'+result);
		}
	});
});

$('#promptWithDefaultValue').on('click', function(e){

	bootbox.prompt({

		title: "What is your real name?",
		value: "makeusabrew",
		callback: function(result) {

			if (result === null) {
				notifyAlert('info', 'Canceled.');
			} else {
				notifyAlert('info', 'OK :'+result);
			}
		}
	});
});

$('#customDialog').on('click', function(e){

	bootbox.dialog({

		message: "I am a custom dialog",
		title: "Custom title",
		buttons: {

			success: {

				label: "Success!",
				className: "btn-success",
				callback: function() {
					notifyAlert('info', 'Success');
				}
			},
			danger: {

				label: "Danger!",
				className: "btn-danger",
				callback: function() {
					notifyAlert('info', 'Danger');
				}
			},
			main: {

				label: "Click ME!",
				className: "btn-primary",
				callback: function() {
					notifyAlert('info', 'Click ME');
				}
			}
		}
	});
});

$('#keyAlert').on('click', function(e){

	bootbox.alert({

		message: "Hello world!",
		buttons: {

			ok: {

				className: "btn-primary default"
			}
		},
		callback: function() {

			notifyAlert('info', 'Closed.');
		}
	});
});

$('#keyConfirm').on('click', function(e){

	bootbox.confirm({

		message: "Are you sure?",
		buttons: {

			confirm: {

				className: "btn-primary default"
			}
		},
		callback: function(result) {

			notifyAlert('info', 'Closed. :'+result);
		}
	});
});

$('#keyPrompt').on('click', function(e){

	bootbox.prompt({

		title: "What is your real name?",
		buttons: {

			confirm: {

				className: "btn-primary default"
			}
		},
		callback: function(result) {

			if (result === null) {
				notifyAlert('info', 'Canceled.');
			} else {
				notifyAlert('info', 'OK :'+result);
			}
		}
	});
});

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($(".bootbox-alert").is(".show")){

			$('.bootbox-alert .btn-primary.default').trigger("click");
		}else if($(".bootbox-confirm").is(".show")){

			$('.bootbox-confirm .btn-primary.default').trigger("click");
		}else if($(".bootbox-prompt").is(".show")){

			$('.bootbox-prompt .btn-primary.default').trigger("click");
		}

		e.preventDefault();
	}
});

$('#centerAlert').on('click', function(e){

	bootbox.alert({

		message: "Hello world!",
		centerVertical: true,
		callback: function() {

			notifyAlert('info', 'Closed.');
		}
	});
});

$('#centerConfirm').on('click', function(e){

	bootbox.confirm({

		message: "Are you sure?",
		centerVertical: true,
		callback: function(result) {

			notifyAlert('info', 'Closed. :'+result);
		}
	});
});

$('#centerPrompt').on('click', function(e){

	bootbox.prompt({

		title: "What is your real name?",
		centerVertical: true,
		callback: function(result) {

			if (result === null) {
				notifyAlert('info', 'Canceled.');
			} else {
				notifyAlert('info', 'OK :'+result);
			}
		}
	});
});
