'use strict';

//

$(document).ready(function() {

	var dataTable=$('#jobExecutionlist').DataTable({
		"language": {
			select: {
				rows: {
					_: " " + messages(MessageKeys.X__CASE__SELECTED).replace("{0}","%d")
				}
			}
		},
		select: {
			style: 'single',
		},
		"lengthMenu": [ [10, 50, 100, 500, -1], [10, 50, 100, 500, 1000] ],
		"initComplete": function () {

			dataTable
				.on( 'select', function ( e, dt, type, indexes ) {

					var rowData = dataTable.rows( indexes ).data().toArray();
				} )
				.on( 'deselect', function ( e, dt, type, indexes ) {

					var rowData = dataTable.rows( indexes ).data().toArray();
				} );

			$('#jobExecutionlist'+'_wrapper').on( 'click', function () {

				updateSelection();
			});

		}
	});

	var updateSelection = function (){

		$('#restartButton').attr("disabled", "disabled");
		$('#stopButton').attr("disabled", "disabled");
		$('#cancelButton').attr("disabled", "disabled");

		var rows = $('#jobExecutionlist>tbody>tr');
		switch(rows.length){
			case 0:

				break;
			default:

				var selectedRows = $('#jobExecutionlist>tbody>tr.selected');
				switch(selectedRows.length){
					case 0:

						break;
					case 1:

						$('#restartButton').prop('disabled', false);
						$('#stopButton').prop('disabled', false);
						$('#cancelButton').prop('disabled', false);
						break;
					default:

						break;
				};

				break;
		};
	};

	updateSelection();

	// ////////// Operate //////////

	$('#startButton').on("click", function(){

		var jobName = $('#executeJobName>option:selected').val();

		var timeout = "10000";
		$.ajax({
			method:"GET",
			url: Routes.apis.batch.Batch.start(jobName, messages("encodedUserId")).url + "?" + getToken(),
			data: $.param({
			}),
			contentType: 'text/plain',
			dataType: "json",
			timeout: timeout
		})
		.then(
			function (responseJson) {

				if(responseJson["error"]){

					notifyAlert('warning', '<strong>' + messages(MessageKeys.WARNING) + '</strong> ' + responseJson["error"], 10000);
				}else{

					window.location.href = Routes.controllers.manage.batch.Batch.index().url;
				}
			},
			function (jqXHR, textStatus, errorThrown) {

				notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + errorThrown, 10000);
			}
		);
	});

	$('#restartButton').on("click", function(){

		var selectedRows = $('#jobExecutionlist>tbody>tr.selected');
		var jobExecution = createJobExecutions(selectedRows)[0];
		var executionId = jobExecution.executionId;

		var timeout = "10000";
		$.ajax({
			method:"GET",
			url: Routes.apis.batch.Batch.restart(executionId, messages("encodedUserId")).url + "?" + getToken(),
			data: $.param({
			}),
			contentType: 'text/plain',
			dataType: "json",
			timeout: timeout
		})
		.then(
			function (responseJson) {

				if(responseJson["error"]){

					notifyAlert('warning', '<strong>' + messages(MessageKeys.WARNING) + '</strong> ' + responseJson["error"], 10000);
				}else{

					window.location.href = Routes.controllers.manage.batch.Batch.index().url;
				}
			},
			function (jqXHR, textStatus, errorThrown) {

				notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + errorThrown, 10000);
			}
		);
	});

	$('#stopButton').on("click", function(){

		var selectedRows = $('#jobExecutionlist>tbody>tr.selected');
		var jobExecution = createJobExecutions(selectedRows)[0];
		var executionId = jobExecution.executionId;

		var timeout = "10000";
		$.ajax({
			method:"GET",
			url: Routes.apis.batch.Batch.stop(executionId, messages("encodedUserId")).url + "?" + getToken(),
			data: $.param({
			}),
			contentType: 'text/plain',
			dataType: "json",
			timeout: timeout
		})
		.then(
			function (responseJson) {

				if(responseJson["error"]){

					notifyAlert('warning', '<strong>' + messages(MessageKeys.WARNING) + '</strong> ' + responseJson["error"], 10000);
				}else{

					window.location.href = Routes.controllers.manage.batch.Batch.index().url;
				}
			},
			function (jqXHR, textStatus, errorThrown) {

				notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + errorThrown, 10000);
			}
		);
	});

	$('#cancelButton').on("click", function(){

		var selectedRows = $('#jobExecutionlist>tbody>tr.selected');
		var jobExecution = createJobExecutions(selectedRows)[0];
		var executionId = jobExecution.executionId;

		var timeout = "10000";
		$.ajax({
			method:"GET",
			url: Routes.apis.batch.Batch.abandon(executionId, messages("encodedUserId")).url + "?" + getToken(),
			data: $.param({
			}),
			contentType: 'text/plain',
			dataType: "json",
			timeout: timeout
		})
		.then(
			function (responseJson) {

				if(responseJson["error"]){

					notifyAlert('warning', '<strong>' + messages(MessageKeys.WARNING) + '</strong> ' + responseJson["error"], 10000);
				}else{

					window.location.href = Routes.controllers.manage.batch.Batch.index().url;
				}
			},
			function (jqXHR, textStatus, errorThrown) {

				notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + errorThrown, 10000);
			}
		);
	});

	var createJobExecutions = function(selectedRows){

		var jobExecutions = [];
		selectedRows.each(function(){

			var tr = this;
			var index = $('#jobExecutionlist>tbody>tr').index(tr);
			var columns = dataTable.rows().data()[ index ];
			var jobExecution = {
				"executionId":columns[2]
			};
			jobExecutions.push(jobExecution);
		});

		return jobExecutions;
	};

	$('#reloadButton').on("click", function(){

		window.location.reload(true);
	});
});
