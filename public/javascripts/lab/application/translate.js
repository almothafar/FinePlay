'use strict';

$('#translateButton').on('click', function(e){

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout = "10000";
	$.ajax({
		method:"GET",
		url: Routes.apis.transrator.Transrator.translate().url + "?" + getToken(),
		data: $.param({
				to: messages("langCode"),
				text: $('#translateTabContent div textarea').eq(0).val()
		}),
		contentType: 'text/plain',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			$('#ajaxDialog').modal('hide');

			$('#translateTabContent div textarea').eq(1).val(responseJson['text']);
			$('#translateTab li a').eq(1).tab('show');
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(messages(MessageKeys.FAILURE));
			var errorMessage = errorThrown;
			if(jqXHR.responseJSON){errorMessage = jqXHR.responseJSON['error'];}
			$('#ajaxDescription').html(messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorMessage+'</strong>');
		}
	);
})
