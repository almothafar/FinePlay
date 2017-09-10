'use strict';

$('#translateButton').on('click', function(e){

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(Messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout = "10000";
	$.ajax({
		method:"GET",
		url: Routes.apis.transrator.Transrator.translate().url + "?" + getToken(),
		data: $.param({
				to: Messages("langCode"),
				text: $('#translateTabContent div:eq(0) textarea').val()
		}),
		contentType: 'text/plain',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			$('#ajaxDialog').modal('hide');

			$('#translateTabContent div:eq(1) textarea').val(responseJson['text']);
			$('#translateTab li:eq(1) a').tab('show');
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(Messages(MessageKeys.FAILURE));
			var errorMessage = errorThrown;
			if(jqXHR.responseJSON){errorMessage = jqXHR.responseJSON['error'];}
			$('#ajaxDescription').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorMessage+'</strong>');
		}
	);
})
