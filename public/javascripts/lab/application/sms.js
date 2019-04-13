'use strict';

$('#sendButton').on('click', function(e){

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout = "10000";
	var form = $('#smsForm');
	var url = form.attr('action');
	$.ajax({
		method:"POST",
		url:url,
		data:form.serialize(),
		processData: false,
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			$('#ajaxDialog').modal('hide');

			$('#smsTabContent div textarea').eq(1).val(JSON.stringify(responseJson));
			$('#smsTab li a').eq(1).tab('show');
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