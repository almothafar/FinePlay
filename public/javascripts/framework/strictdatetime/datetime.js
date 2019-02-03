'use strict';

$(document).ready(function () {

	var datePicker = $('.datePicker').pickadate({
		selectYears: true,
		selectMonths: true,
		format: 'yyyy/mm/dd',
		formatSubmit: 'yyyy-mm-dd'
	});
	datePicker.pickadate( 'picker' ).on({

		set: function(thingSet) {

			var selectedDate = this._hidden.value;
			$('#dateTime_DateTime').val("");
			$('#dateTime_DateTime_submit').val("");

			getTimes(selectedDate, $('#times'));
		}
	});

	$('.input-group .input-group-text').on('click', function(e){

		$(e.currentTarget).parent().parent().find('.picker__input').trigger("click");
		e.stopPropagation();
	});

	$('#dateTime_DateTime').on('click', function(){

		$('#timePickerDialog').modal('show');
	});

	$('#timePickerClear').on('click', function(){

		$('#times>.list-group-item').removeClass('active');

		$('#dateTime_DateTime').val("");
		$('#dateTime_DateTime_submit').val("");

		$('#timePickerDialog').modal('hide');
	});

	var getTimes = function(selectedDate, timesElement){

		timesElement.empty();

		if(!selectedDate){

			return;
		}

		$('#ajaxDialog').modal({backdrop:false});

		$('#ajaxDialog').one('shown.bs.modal', function () {

			$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
			$('#ajaxCancel').prop('disabled', true);
			$('#ajaxMessage').text(messages(MessageKeys.PLEASE__WAIT));
			$('#ajaxDescription').text('-');

			$.ajax({
				method:"POST",
				url: Routes.controllers.framework.strictdatetime.DateTime.times().url + "?" + getToken(),
				data:JSON.stringify({
					date: selectedDate,
					min: '00:00',
					max: '23:59',
					interval: 30
				}),
				contentType: 'application/json',
				dataType: "json",
				timeout: 10 * 1000
			})
			.then(
				function(responseJson) {

					$('#ajaxDialog').modal('hide');

					for(var i in responseJson.times){

						var time = responseJson.times[i];

						var dateTime = time['dateTime'];
						var name = time['name'];

						var submitValue = $('#dateTime_DateTime_submit').val();
						var activeClass = '';
						if(submitValue == dateTime){

							$('#dateTime_DateTime').val(name);
							activeClass = "active";
						}

						var div = $('<a href="#" class="list-group-item list-group-item-action '+activeClass+'" data-datetime="'+dateTime+'">'+name+'</a>');
						timesElement.append(div);
					}

					$('#times>.list-group-item').on('click', function(){

						$('#times>.list-group-item').removeClass('active');
						$(this).addClass('active');

						$('#dateTime_DateTime').val($(this).text());
						$('#dateTime_DateTime_submit').val($(this).data('datetime'));

						$('#timePickerDialog').modal('hide');
					});
				},
				function( jqXHR, textStatus, errorThrown) {

					$('#ajaxProgress>.progress-bar').addClass('bg-danger');
					$('#ajaxCancel').prop('disabled', false);
					$('#ajaxMessage').text(messages(MessageKeys.FAILURE));
					$('#ajaxDescription').html(messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
				}
			);
		})
	}

	getTimes(datePicker.pickadate( 'picker' )._hidden.value, $('#times'));
});
