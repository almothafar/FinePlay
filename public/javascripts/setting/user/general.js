'use strict';

$(document).ready(function() {

	$("#update" + "zoneId").css({'width': '100%'})

	$("#update" + "zoneId").select2({

	});

	$( "#update" + "locale").change(function(e) {

		$('#updateForm').submit();
	});

	$( "#update" + "zoneId").change(function(e) {

		$('#updateForm').submit();
	});

	$( "#update" + "theme").change(function(e) {

		$('#updateForm').submit();
	});
});
