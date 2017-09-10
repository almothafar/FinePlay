'use strict';

$(document).ready(function() {

	var dataTable=$('#messagelist').DataTable({
		"lengthMenu": [ [10, 50, 100, 500, -1], [10, 50, 100, 500, Messages(MessageKeys.ALL)] ]
	});

	$('#langSelect').on('change', function(e){

		var eventSource = $(e.target);
		var checked = eventSource.prop('checked');
		var index = eventSource.val();

		dataTable.column(parseInt(index, 10) + 1).visible(checked);
	});
});
