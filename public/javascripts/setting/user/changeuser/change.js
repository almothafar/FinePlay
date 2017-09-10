'use strict';

$('#confirmButton').on("click", function(){

	$('#confirmpassword').val("");
	initOperation();
});

$('#confirmOk').on("click", function(){

	initOperation();
	operation();
});

var initOperation = function(){

	var errorContainer = $('#confirmDialog form>div:first-child');
	errorContainer.empty();

	var propertyErrorContainer = $('#confirmpassword').parent().find("div");
	propertyErrorContainer.empty();

	$('#confirmMessage').text('');
	$('#confirmDescription').text('-');
	$('#confirmProgress').removeClass('progress-danger').val(0);
	$('#confirmCancel').prop('disabled', false);
	$('#confirmOk').prop('disabled', false);
}

var operation = function(){

	$('#confirmMessage').text(Messages(MessageKeys.PLEASE__WAIT));
	$('#confirmProgress').val(100);
	$('#confirmCancel').prop('disabled', true);
	$('#confirmOk').prop('disabled', true);

	var form = $('#confirmForm');
	var url = form.attr('action');
	$.ajax({
		method:"POST",
		url:url,
		data:form.serialize(),
		processData: false,
		dataType: "json",
		timeout: 3 * 1000
	})
	.then(
		function (responseJson) {

			window.location.href = Routes.controllers.setting.user.ChangeUser.index().url;
		},
		function (jqXHR, textStatus, errorThrown) {

			var responseJson = jqXHR.responseJSON||{};
			if(responseJson['globalErrors']){

				var errorContainer = $('#confirmDialog form>div:first-child');
				var errors = responseJson.globalErrors;
				$.each(errors, function(i, error){

					errorContainer.append('<p class="text-warning">'+error+'</p>');
				});
			}
			if(responseJson['errors']){

				for(var property in responseJson.errors){

					var propertyErrorContainer = $('#confirm'+property).parent().find('div')
					var propertyErrors = responseJson.errors[property];
					$.each(propertyErrors, function(i, error){

						propertyErrorContainer.append('<p class="text-warning">'+error+'</p>');
					});
				}

			}

			$('#confirmMessage').text(Messages(MessageKeys.FAILURE));
			$('#confirmDescription').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
			$('#confirmProgress>.progress-bar').addClass('bg-danger');
			$('#confirmCancel').prop('disabled', false);
			$('#confirmOk').prop('disabled', false);

			if(responseJson['globalErrors'] || responseJson['errors']) {

				shake('#confirmDialog');
			}
		}
	);
	return false;
}

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#confirmDialog").is(".show")){

			$('#confirmOk').trigger("click");
		}else{

			$('#applyButton').trigger("click");
		}

		e.preventDefault();
	}
})

if(Messages("hasErrors")) {

	shake('#changePanel');
}
