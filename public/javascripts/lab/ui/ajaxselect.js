'use strict';


$( "#bigSelect>select" ).on('change', function(e) {

	update($(this).val(), $(this).parent().next().find("select"));
});

$( "#middleSelect>select" ).on('change', function(e) {

	update($(this).val(), $(this).parent().next().find("select"));
});

$( "#smaleSelect>select" ).on('change', function(e) {

});

var update = function(parentValue, updateElement){

	$('#ajaxDialog').modal({backdrop:false});

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(Messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	updateElement.empty();

	$.ajax({
		method:"POST",
		url: Routes.apis.development.http.Http.ajaxdata().url + "?" + getToken(),
		data:JSON.stringify({
			request: "dummyRequest",
			mock: {
				wait:0.5*1000,
				response:{
					values:[parentValue+' 0', parentValue+' 1', parentValue+' 2']
				}
			}
		}),
		contentType: 'application/json',
		dataType: "json",
		timeout: 10 * 1000
	})
	.then(
		function(responseJson) {

			$('#ajaxDialog').modal('hide');

			for(var i in responseJson.values){

				var div = $('<option value="'+responseJson.values[i]+'">'+responseJson.values[i]+'</option>');
				updateElement.append(div);
			}

			updateElement.trigger("change");
		},
		function( jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(Messages(MessageKeys.FAILURE));
			$('#ajaxDescription').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
}

update("", $( "#bigSelect>select" ));
